package accountinfo;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.0
 * 2017-11-09T17:09:16.813+03:00
 * Generated source version: 3.1.0
 * 
 */
@WebServiceClient(name = "AccountInfoService", 
                  wsdlLocation = "file:/Volumes/MacintoshHD/Users/salmond/Documents/job/projects/soap-client/examples/src/main/resources/wsdl/AccountsInfo.wsdl",
                  targetNamespace = "http://AccountInfo") 
public class AccountInfoService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://AccountInfo", "AccountInfoService");
    public final static QName AccountInfoPort = new QName("http://AccountInfo", "AccountInfoPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/Volumes/MacintoshHD/Users/salmond/Documents/job/projects/soap-client/examples/src/main/resources/wsdl/AccountsInfo.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(AccountInfoService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/Volumes/MacintoshHD/Users/salmond/Documents/job/projects/soap-client/examples/src/main/resources/wsdl/AccountsInfo.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public AccountInfoService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AccountInfoService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AccountInfoService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public AccountInfoService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public AccountInfoService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public AccountInfoService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns AccountInfoPortType
     */
    @WebEndpoint(name = "AccountInfoPort")
    public AccountInfoPortType getAccountInfoPort() {
        return super.getPort(AccountInfoPort, AccountInfoPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AccountInfoPortType
     */
    @WebEndpoint(name = "AccountInfoPort")
    public AccountInfoPortType getAccountInfoPort(WebServiceFeature... features) {
        return super.getPort(AccountInfoPort, AccountInfoPortType.class, features);
    }

}