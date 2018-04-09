/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch03;



import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

public class UnmarshalWithElement {
    
  public static void main(String...arg) {
    try {

        //Create context
        JAXBContext ctx = JAXBContext.newInstance(new Class[]{Book.class});
        
        //Create marshaller
        Unmarshaller um = ctx.createUnmarshaller();

        //Read in the XML from anywhere
        //In this case it is a complete XML book as string.
        StringReader sr = new StringReader(getBookXml());
        
        //um.setValidating(true);
        
        //Get XML from object
        JAXBElement<Book> b = um.unmarshal(
                new StreamSource(sr), Book.class);
        
        //Start working with object
        Book book = b.getValue();
        
        System.out.printf("Title: %s", book.getTitle());

    } catch (JAXBException ex) {
        ex.printStackTrace();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
  }
  
  private static String getBookXml(){
      return 
              "<Book>" +
              "<title>On Friendship</title>" +
              "<price>39.95</price>" + 
              "</Book>";
  }
}

