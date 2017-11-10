package com.salmondx.cxf.client.example;

import accountinfo.AccountInfoGetInParms;
import accountinfo.AccountInfoGetOutParms;
import accountinfo.AccountInfoPortType;
import accountinfo.BankInfo;
import com.salmondx.cxf.client.annotation.Param;
import com.salmondx.cxf.client.annotation.SoapClient;
import com.salmondx.cxf.client.annotation.SoapMethod;
import rx.Observable;
import rx.Single;

import java.time.LocalDate;
import java.util.List;


/**
 * Created by Salmond on 09/11/17.
 */
@SoapClient(service = AccountInfoPortType.class)
public interface AccountInfoService {
    @SoapMethod(value = "AccountInfoGet", autowired = {BankInfo.class})
    Observable<AccountInfo> getInfo(AccountRequest accountRequest);

    @SoapMethod(value = "AccountInfoGet", autowired = {BankInfo.class})
    AccountInfo getInfo(@Param("accNumber") String accountNumber, @Param("date") LocalDate date);

    @SoapMethod(value = "AccountInfoList", autowired = {BankInfo.class})
    List<AccountInfoRow> getInfoList(@Param("currency") String currency);

    @SoapMethod(value = "AccountInfoList", autowired = {BankInfo.class})
    Single<List<AccountInfoRow>> getInfoListSingle(@Param("currency") String currency);

    @SoapMethod(value = "AccountInfoGet")
    AccountInfoGetOutParms getAsIs(AccountInfoGetInParms inParms, BankInfo bankInfo);

    @SoapMethod(value = "AccountInfoGet", autowired = {BankInfo.class})
    AccountInfoGetOutParms getAsIsWithAutowired(AccountInfoGetInParms inParms);

    @SoapMethod(value = "AccountInfoGet", autowired = {BankInfo.class})
    Observable<AccountInfoGetOutParms> getAsIsAsync(AccountInfoGetInParms inParms);
}
