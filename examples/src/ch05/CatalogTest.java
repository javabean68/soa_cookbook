/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch05;

/**
 * Example: Manually Invoking a Web Service with Dispatch using SOAPMessage in
 * Message Mode
 */
import static java.lang.System.out;

import java.io.FileInputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class CatalogTest {

    public static void main(String[] args) {
        dispatchMsgIsbnTest();

        extractDOMFromSOAPResult();
        
        dispatchPayloadIsbnTest();
    }

    public static void dispatchMsgIsbnTest() {
        try {
            URL wsdl
                    = new URL("http://admin-PC:9999/CatalogService/Catalog?wsdl");

            String ns = "http://ns.soacookbook.com/ws/catalog";

            //Create the Service qualified name
            String svcName = "CatalogService";
            QName svcQName = new QName(ns, svcName);

            //Get a delegate wrapper
            Service service = Service.create(wsdl, svcQName);

            //Create the Port name
            String portName = "CatalogPort";
            QName portQName = new QName(ns, portName);

            //Create the delegate to send the request:
            Dispatch<SOAPMessage> dispatch
                    = service.createDispatch(portQName,
                            SOAPMessage.class, Service.Mode.MESSAGE);

            String dataFile = "C:\\sandbox\\soa_cookbook\\examples\\src\\ch05\\isbnMsg.txt";

            //read in the data to use in building the soap message from a file
            FileInputStream fis = new FileInputStream(dataFile);

            //create the message, using contents of file as envelope
            SOAPMessage request
                    = MessageFactory.newInstance().createMessage(null, fis);

            //debug print what we're sending
            request.writeTo(out);

            out.println("\nInvoking...");

            //send the message as request to service and get response
            SOAPMessage response = dispatch.invoke(request);

            //just show in the console for now
            response.writeTo(System.out);

        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (WebServiceException wsex) {
            wsex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void extractDOMFromSOAPResult() {
        try {
            URL wsdl
                    = new URL("http://admin-PC:9999/CatalogService/Catalog?wsdl");

            String ns = "http://ns.soacookbook.com/ws/catalog";

            //Create the Service name
            String svcName = "CatalogService";
            QName svcQName = new QName(ns, svcName);

            //Get a delegate wrapper
            Service service = Service.create(wsdl, svcQName);

            //Create the Port name
            String portName = "CatalogPort";
            QName portQName = new QName(ns, portName);

            Dispatch<SOAPMessage> dispatch
                    = service.createDispatch(portQName,
                            SOAPMessage.class, Service.Mode.MESSAGE);

            //create the message
            SOAPMessage soapMsg
                    = MessageFactory.newInstance().createMessage();

            SOAPPart soapPart = soapMsg.getSOAPPart();
            SOAPEnvelope env = soapPart.getEnvelope();
            SOAPBody body = env.getBody();

            //Create a qualified name for the namespace of the
            //objects used by the service. 
            String iNs = "http://ns.soacookbook.com/catalog";
            String elementName = "isbn";
            QName isbnQName = new QName(iNs, elementName);

            //Add the <isbn> element to the SOAP body 
            //as its only child
            body.addBodyElement(isbnQName).setValue("12345");

            //debug print what we're sending
            soapMsg.writeTo(out);

            out.println("\nInvoking...");

            //send the message as request to service and get response
            SOAPMessage response = dispatch.invoke(soapMsg);

            //Extract response content as DOM view
            Document doc
                    = response.getSOAPBody().extractContentAsDocument();

            NodeList isbnNodes = (NodeList) doc.getElementsByTagName("lastName");

            //just get by index; we know there's only one
            String value = isbnNodes.item(0).getTextContent();
            out.println("\nAuthor LastName=" + value);

            //just show in the console for now
            //response.writeTo(System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//Example: Web Service Client using Dispatch<Source> with an XML
//        String in Payload Mode
    public static void dispatchPayloadIsbnTest() {
        try {
            URL wsdl
                    = new URL("http://admin-PC:9999/CatalogService/Catalog?wsdl");

            String ns = "http://ns.soacookbook.com/ws/catalog";
            String objNs = "http://ns.soacookbook.com/catalog";

            //Create the Service name
            String svcName = "CatalogService";
            QName svcQName = new QName(ns, svcName);

            //Get a delegate wrapper
            Service service = Service.create(wsdl, svcQName);

            //Create the Port name
            String portName = "CatalogPort";
            QName portQName = new QName(ns, portName);

            //Create the dispatcher on Source with Payload
            Dispatch<Source> dispatch
                    = service.createDispatch(portQName,
                            Source.class, Service.Mode.PAYLOAD);

            //Change to tick marks or escape double quotes
            String payload
                    = "<i:isbn xmlns:i='http://ns.soacookbook.com/catalog'>12345</i:isbn>";

            //Create a SOAP request based on our XML string
            StreamSource request = new StreamSource(new StringReader(payload));

            out.println("\nInvoking...");

            //Send the request and get the response
            Source bookResponse = dispatch.invoke(request);

            //Now we have to transform our result source object 
            //into a DOM tree to work with it
            DOMResult dom = new DOMResult();
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(bookResponse, dom);

            //Extract values with XPath
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xp = xpf.newXPath();
            NodeList resultNodes = (NodeList) xp.evaluate("//title",
                    dom.getNode(), XPathConstants.NODESET);

            //Show the result
            String title = resultNodes.item(0).getTextContent();
            out.println("TITLE=" + title);

        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (WebServiceException wsex) {
            wsex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
