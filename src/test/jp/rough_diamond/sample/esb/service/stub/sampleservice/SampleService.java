
/*
 * 
 */

package jp.rough_diamond.sample.esb.service.stub.sampleservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 2.1.2
 * Fri Apr 02 17:07:32 JST 2010
 * Generated source version: 2.1.2
 * 
 */


@WebServiceClient(name = "SampleService", 
                  wsdlLocation = "file:/D:/products/isken/kaede/workspace/framework/etc/wsdl/SampleService.wsdl",
                  targetNamespace = "http://service.esb.sample.rough_diamond.jp/") 
public class SampleService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://service.esb.sample.rough_diamond.jp/", "SampleService");
    public final static QName SampleServicePort = new QName("http://service.esb.sample.rough_diamond.jp/", "SampleServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/D:/products/isken/kaede/workspace/framework/etc/wsdl/SampleService.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/D:/products/isken/kaede/workspace/framework/etc/wsdl/SampleService.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public SampleService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SampleService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SampleService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns SampleServicePortType
     */
    @WebEndpoint(name = "SampleServicePort")
    public SampleServicePortType getSampleServicePort() {
        return super.getPort(SampleServicePort, SampleServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SampleServicePortType
     */
    @WebEndpoint(name = "SampleServicePort")
    public SampleServicePortType getSampleServicePort(WebServiceFeature... features) {
        return super.getPort(SampleServicePort, SampleServicePortType.class, features);
    }

}
