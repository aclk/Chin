package test.xml.sax;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TestSaxParsing {

    private void doSaxParsing(String xmlFileName) throws Exception {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        try {
            DefaultHandler handler = new DefaultHandler() {
                boolean idFlag = false;
                boolean name = false;
                boolean salaryFlag = false;

                @Override
                public void startElement(String uri, String localName,
                        String qName, Attributes attributes)
                        throws SAXException {
                    System.out.println("Start Element :" + qName);
                    if (qName.equals("id")) {
                        idFlag = true;
                    }
                    if (qName.equals("name")) {
                        name = true;
                    }
                    if (qName.equals("salary")) {
                        salaryFlag = true;
                    }
                }

                @Override
                public void endElement(String uri, String localName,
                        String qName) throws SAXException {
                    System.out.println("End Element :" + qName);
                }

                @Override
                public void characters(char ch[], int start, int length)
                        throws SAXException {
                    if (idFlag) {
                        System.out.println("id : "
                                + new String(ch, start, length));
                        idFlag = false;
                    }
                    if (name) {
                        System.out.println("name : "
                                + new String(ch, start, length));
                        name = false;
                    }
                    if (salaryFlag) {
                        System.out.println("salary : "
                                + new String(ch, start, length));
                        salaryFlag = false;
                    }
                }
            };
            InputStream xmlInputStream = getClass().getResourceAsStream(
                    xmlFileName);
            parser.parse(xmlInputStream, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        (new TestSaxParsing()).doSaxParsing("employee.xml");
    }
}