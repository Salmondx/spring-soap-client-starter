package com.salmondx.cxf.client

import accountinfo.*
import com.netflix.hystrix.exception.HystrixRuntimeException
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl
import configuration.TestConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import com.salmondx.cxf.client.example.AccountInfoService
import com.salmondx.cxf.client.example.AccountRequest
import spock.lang.Specification

import java.time.LocalDate

import static org.mockito.Matchers.eq
import static org.mockito.Matchers.refEq
import static org.mockito.Mockito.reset
import static org.mockito.Mockito.when

/**
 * Created by Salmondx on 09/09/16.
 */
@ContextConfiguration(classes = [Application, TestConfiguration])
class SoapClientTest extends Specification {
    @Autowired
    AccountInfoService testClient;

    @Autowired
    BankInfo bankInfo;

    @Autowired
    AccountInfoPortType mockService

    def accountNumber = "21345"

    def "it should send request to test service with custom object and return observable"() {
        given:
        def date = LocalDate.now()
        def soapIn = getSoapIn(accountNumber, date)
        def soapResponse = getSoapOut(accountNumber)
        when(mockService.accountInfoGet(eq(bankInfo), refEq(soapIn))).thenReturn(soapResponse)
        when:
        def resp = testClient.getInfo(new AccountRequest(accountNumber, date)).toBlocking().first()
        then:
        resp.balance == soapResponse.balance
        resp.accountNumber == soapResponse.accNumber
        resp.currency == soapResponse.currencyUnit

        cleanup:
        reset(mockService)
    }

    def "it should send plain text request to test service and return custom object"() {
        given:
        def date = LocalDate.now()
        def soapIn = getSoapIn(accountNumber, date)
        def soapOut = getSoapOut(accountNumber)
        when(mockService.accountInfoGet(eq(bankInfo), refEq(soapIn))).thenReturn(soapOut)
        when:
        def resp = testClient.getInfo(accountNumber, date)
        then:
        resp.balance == soapOut.balance
        resp.accountNumber == soapOut.accNumber
        resp.currency == soapOut.currencyUnit

        cleanup:
        reset(mockService)
    }

    def "it should retrieve list of objects from test service"() {
        given:
        def soapIn = new AccountInfoListInParms()
        soapIn.currency = "USD"

        def resultSet = new ResultSet()
        def first = new AccountInfoRow()
        first.accNumber = "1234"
        first.currencyUnit = "USD"
        first.balance = BigDecimal.ONE
        def second = new AccountInfoRow()
        second.accNumber = "12345"
        second.currencyUnit = "USD"
        second.balance = BigDecimal.ONE
        def third = new AccountInfoRow()
        third.accNumber = "12346"
        third.currencyUnit = "USD"
        third.balance = BigDecimal.ONE

        resultSet.resultSetRow = [first, second, third]

        when(mockService.accountInfoList(eq(bankInfo), refEq(soapIn))).thenReturn(resultSet)

        def accNumbers = ["1234", "12345", "12346"] as Set
        when:
        def resp = testClient.getInfoList("USD")
        then:
        resp.size() == 3
        resp.forEach {
            assert it.balance == BigDecimal.ONE
            assert it.currency == "USD"
            assert accNumbers.contains(it.accountNumber)
        }

        cleanup:
        reset(mockService)
    }

    def "it should retrieve list in a single wrapper from test service"() {
        given:
        def soapIn = new AccountInfoListInParms()
        soapIn.currency = "USD"

        def resultSet = new ResultSet()
        def first = new AccountInfoRow()
        first.accNumber = "1234"
        first.currencyUnit = "USD"
        first.balance = BigDecimal.ONE
        def second = new AccountInfoRow()
        second.accNumber = "12345"
        second.currencyUnit = "USD"
        second.balance = BigDecimal.ONE
        def third = new AccountInfoRow()
        third.accNumber = "12346"
        third.currencyUnit = "USD"
        third.balance = BigDecimal.ONE

        resultSet.resultSetRow = [first, second, third]

        when(mockService.accountInfoList(eq(bankInfo), refEq(soapIn))).thenReturn(resultSet)

        def accNumbers = ["1234", "12345", "12346"] as Set
        when:
        def resp = testClient.getInfoListSingle("USD").toBlocking().value()
        then:
        resp.size() == 3
        resp.forEach {
            assert it.balance == BigDecimal.ONE
            assert it.currency == "USD"
            assert accNumbers.contains(it.accountNumber)
        }

        cleanup:
        reset(mockService)
    }

    def "it should pass parameters from cxf service as is"() {
        given:
        def date = LocalDate.now()
        def soapIn = getSoapIn(accountNumber, date)
        def soapOut = getSoapOut(accountNumber)
        when(mockService.accountInfoGet(eq(bankInfo), refEq(soapIn))).thenReturn(soapOut)
        when:
        def resp = testClient.getAsIs(soapIn, bankInfo)
        then:
        resp.balance == soapOut.balance
        resp.currencyUnit == soapOut.currencyUnit
        resp.accNumber == soapOut.accNumber
    }

    def "it should autowire and pass rest parameters from cxf service as is"() {
        given:
        def date = LocalDate.now()
        def soapIn = getSoapIn(accountNumber, date)
        def soapOut = getSoapOut(accountNumber)
        when(mockService.accountInfoGet(eq(bankInfo), refEq(soapIn))).thenReturn(soapOut)
        when:
        def resp = testClient.getAsIsWithAutowired(soapIn)
        then:
        resp.balance == soapOut.balance
        resp.currencyUnit == soapOut.currencyUnit
        resp.accNumber == soapOut.accNumber
    }

    def "it should pass parameters as is and work with async"() {
        given:
        def date = LocalDate.now()
        def soapIn = getSoapIn(accountNumber, date)
        def soapOut = getSoapOut(accountNumber)
        when(mockService.accountInfoGet(eq(bankInfo), refEq(soapIn))).thenReturn(soapOut)
        when:
        def resp = testClient.getAsIsAsync(soapIn).toBlocking().first()
        then:
        resp.balance == soapOut.balance
        resp.currencyUnit == soapOut.currencyUnit
        resp.accNumber == soapOut.accNumber
    }

    def "it should throw HystrixRuntimeException if exception occurred"() {
        given:
        def date = LocalDate.now()
        def soapIn = getSoapIn(accountNumber, date)
        when(mockService.accountInfoGet(eq(bankInfo), refEq(soapIn))).thenThrow(new MsgNotFoundException())
        when:
        testClient.getInfo(new AccountRequest(accountNumber, date)).toBlocking().first()
        then:
        thrown(HystrixRuntimeException)

        cleanup:
        reset(mockService)
    }

    def "it should call toString() method"() {
        expect:
        testClient.toString() != null

    }

    def "it should call hashCode() method"() {
        when:
        def first = testClient.hashCode()
        def second = testClient.hashCode()
        then:
        first == second
    }

    public AccountInfoGetOutParms getSoapOut(String accountNumber) {
        def soapResponse = new AccountInfoGetOutParms()
        soapResponse.accNumber = accountNumber
        soapResponse.balance = BigDecimal.ONE
        soapResponse.currencyUnit = "USD"
        return soapResponse
    }

    public AccountInfoGetInParms getSoapIn(String accountNumber, LocalDate date) {
        def soapIn = new AccountInfoGetInParms()
        soapIn.accNumber = accountNumber
        soapIn.date = new XMLGregorianCalendarImpl(new GregorianCalendar(date.getYear(), date.getMonthValue(), date.getDayOfMonth()))
        return soapIn
    }
}
