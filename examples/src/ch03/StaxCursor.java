/*
 * Example: Using the Cursor parsing method of parsing in StAX.
 */
package ch03;

import static java.lang.System.out;

import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

public class StaxCursor {
    private static final String db = "./Catalog.xml";

    //we'll hold values here as we find them
    private Set<String> uniqueAuthors;

    public static void main(String... args) {
        StaxCursor p = new StaxCursor();
        p.find();
    }

    //constructor
    public StaxCursor() {
        uniqueAuthors = new TreeSet<String>();
    }

    //parse the document and offload work to helpers
    public void find() {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        //forward-only, most efficient way to read
        XMLStreamReader reader = null;

        //get ahold of the file
        final InputStream is = 
            StaxCursor.class.getResourceAsStream(db);

        //whether current event represents elem, attrib, etc
        int eventType;
        String current = "";

        try {
            //create the reader from the stream
            reader = xif.createXMLStreamReader(is);

            //work with stream and get the type of event 
            //we're inspecting
            while (reader.hasNext()) {
                //because this is Cursor, we get an integer token to next event
                eventType = reader.next();

                //do different work depending on current event
                switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    //save element name for later
                    current = reader.getName().toString();

                    printSkus(current, reader);
                    break;

                case XMLEvent.CHARACTERS:
                    findAuthors(current, reader);
                    break;
                    }
            } //end loop
            out.println("Unique Authors=" + uniqueAuthors);

            } catch (XMLStreamException e) {
                out.println("Cannot parse: " + e);
            }
    }

    //get the name and value of the book's sku attribute
    private void printSkus(String current, XMLStreamReader r) {
        current = r.getName().toString();

        if ("book".equals(current)) {
            String k = r.getAttributeName(0).toString();
            String v = r.getAttributeValue(0);
            out.println("AttribName " + k + "=" + v);
        }
    }

    //inspect author elements and read their values.
    private void findAuthors(String current, XMLStreamReader r)
        throws XMLStreamException {

        if ("author".equals(current)) {
            String v = r.getText().trim();
            
            //can get whitespace value, so ignore
            if (v.length() > 0) {
                uniqueAuthors.add(v);
            }
        }       
    }
}
    

