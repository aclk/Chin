package test.xml;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TestXml {
    private String filename = "src\\test\\resources\\person.xml";

    /**
     * Test 1 - Using JAXB to unmarshall a Java File.<br>
     * 
     * Test 1 illustrates how simple the progamming model for JAXB is. It is
     * very easy to go from an XML file to Java objects. There is no need to get
     * involved with the nitty gritty details of marshalling and parsing.
     */
    @Test
    public void testUnMarshallUsingJAXB() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(PersonList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        PersonList obj = (PersonList) unmarshaller
                .unmarshal(new File(filename));
    }

    /**
     * Test 2 - Using JAXB to unmarshall a Streamsource<br>
     * 
     * Test 2 is similar Test 1, except this time a Streamsource object wraps
     * around a File object. The Streamsource object gives a hint to the JAXB
     * implementation to stream the file.
     */
    @Test
    public void testUnMarshallUsingJAXBStreamSource() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(PersonList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StreamSource source = new StreamSource(new File(filename));
        PersonList obj = (PersonList) unmarshaller.unmarshal(source);
    }

    /**
     * Test 3 - Using JAXB to unmarshall a StAX XMLStreamReader<br>
     * 
     * Again similar to Test 1, except this time an XMLStreamReader instance
     * wraps a FileReader instance which is unmarshalled by JAXB.
     */
    @Test
    public void testUnMarshallingWithStAX() throws Exception {
        FileReader fr = new FileReader(filename);
        JAXBContext jc = JAXBContext.newInstance(PersonList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLStreamReader xmler = xmlif.createXMLStreamReader(fr);
        PersonList obj = (PersonList) unmarshaller.unmarshal(xmler);
    }

    /**
     * Test 4 - Just use DOM<br>
     * 
     * This test uses no JAXB and instead just uses the JAXP DOM approach. This
     * means straight away more code is required than any JAXB approach.
     */
    @Test
    public void testParsingWithDom() throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(filename);
        List<Person> personList = new ArrayList<Person>();
        NodeList persons = doc.getElementsByTagName("person");
        for (int i = 0; i < persons.getLength(); i++) {
            Element element = (Element) persons.item(i);
            NodeList children = (NodeList) element.getChildNodes();
            Person person = new Person();
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if (child.getNodeName().equalsIgnoreCase("id")) {
                    person.setId(child.getNodeValue());
                } else if (child.getNodeName().equalsIgnoreCase("name")) {
                    person.setName(child.getNodeValue());
                }
            }
            personList.add(person);
        }
    }

    /**
     * Test 5 - Just use SAX <br>
     * 
     * Test 5 uses no JAXB and uses SAX to parse the XML document. The SAX
     * approach involves more code and more complexity than any JAXB approach.
     * The Developer has to get involved with the parsing of the document.
     */
    @Test
    public void testParsingWithSAX() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        final List<Person> persons = new ArrayList<Person>();
        DefaultHandler handler = new DefaultHandler() {
            boolean bpersonId = false;
            boolean bpersonName = false;

            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("id")) {
                    bpersonId = true;
                    Person person = new Person();
                    persons.add(person);
                } else if (qName.equalsIgnoreCase("name")) {
                    bpersonName = true;
                }
            }

            public void endElement(String uri, String localName, String qName)
                    throws SAXException {
            }

            public void characters(char ch[], int start, int length)
                    throws SAXException {
                if (bpersonId) {
                    String personID = new String(ch, start, length);
                    bpersonId = false;
                    Person person = persons.get(persons.size() - 1);
                    person.setId(personID);
                } else if (bpersonName) {
                    String name = new String(ch, start, length);
                    bpersonName = false;
                    Person person = persons.get(persons.size() - 1);
                    person.setName(name);
                }
            }
        };
        saxParser.parse(filename, handler);
    }
}
