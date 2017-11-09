
package accountinfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the accountinfo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AccountInfoGetInParms_QNAME = new QName("http://AccountInfo", "AccountInfoGetInParms");
    private final static QName _BankInfo_QNAME = new QName("http://AccountInfo", "BankInfo");
    private final static QName _AccountInfoGetResponse_QNAME = new QName("http://AccountInfo", "AccountInfoGetResponse");
    private final static QName _AccountInfoListInParms_QNAME = new QName("http://AccountInfo", "AccountInfoListInParms");
    private final static QName _AccountInfoList_QNAME = new QName("http://AccountInfo", "AccountInfoList");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: accountinfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NotFoundException }
     * 
     */
    public NotFoundException createNotFoundException() {
        return new NotFoundException();
    }

    /**
     * Create an instance of {@link AccountInfoGetInParms }
     * 
     */
    public AccountInfoGetInParms createAccountInfoGetInParms() {
        return new AccountInfoGetInParms();
    }

    /**
     * Create an instance of {@link BankInfo }
     * 
     */
    public BankInfo createBankInfo() {
        return new BankInfo();
    }

    /**
     * Create an instance of {@link AccountInfoGetOutParms }
     * 
     */
    public AccountInfoGetOutParms createAccountInfoGetOutParms() {
        return new AccountInfoGetOutParms();
    }

    /**
     * Create an instance of {@link AccountInfoListInParms }
     * 
     */
    public AccountInfoListInParms createAccountInfoListInParms() {
        return new AccountInfoListInParms();
    }

    /**
     * Create an instance of {@link ResultSet }
     * 
     */
    public ResultSet createResultSet() {
        return new ResultSet();
    }

    /**
     * Create an instance of {@link AccountInfoRow }
     * 
     */
    public AccountInfoRow createAccountInfoRow() {
        return new AccountInfoRow();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountInfoGetInParms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://AccountInfo", name = "AccountInfoGetInParms")
    public JAXBElement<AccountInfoGetInParms> createAccountInfoGetInParms(AccountInfoGetInParms value) {
        return new JAXBElement<AccountInfoGetInParms>(_AccountInfoGetInParms_QNAME, AccountInfoGetInParms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BankInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://AccountInfo", name = "BankInfo")
    public JAXBElement<BankInfo> createBankInfo(BankInfo value) {
        return new JAXBElement<BankInfo>(_BankInfo_QNAME, BankInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountInfoGetOutParms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://AccountInfo", name = "AccountInfoGetResponse")
    public JAXBElement<AccountInfoGetOutParms> createAccountInfoGetResponse(AccountInfoGetOutParms value) {
        return new JAXBElement<AccountInfoGetOutParms>(_AccountInfoGetResponse_QNAME, AccountInfoGetOutParms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountInfoListInParms }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://AccountInfo", name = "AccountInfoListInParms")
    public JAXBElement<AccountInfoListInParms> createAccountInfoListInParms(AccountInfoListInParms value) {
        return new JAXBElement<AccountInfoListInParms>(_AccountInfoListInParms_QNAME, AccountInfoListInParms.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://AccountInfo", name = "AccountInfoList")
    public JAXBElement<ResultSet> createAccountInfoList(ResultSet value) {
        return new JAXBElement<ResultSet>(_AccountInfoList_QNAME, ResultSet.class, null, value);
    }

}
