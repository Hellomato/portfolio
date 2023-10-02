/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopapplication.Model.Strategies;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 *
 * @author gabryel
 */
public class XMLParserTest {
    
    public XMLParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testParse() {
                String filePath;
        filePath = "Test Files\\XMLParsingTestFile.XML";
                
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     
        //API to obtain DOM Document instance
        DocumentBuilder builder;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
         
            //Parse the content to Document object
            Document xmlDocument = builder.parse(new File(filePath));
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = tf.newTransformer();
         
            //A character stream that collects its output in a string buffer,
            //which can then be used to construct a string.
            StringWriter writer = new StringWriter();
 
            //transform document to string
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
 
            String xmlString = writer.getBuffer().toString();  

            // Convert string to a StringBuffer for parsing
            StringBuffer xmlStringBuffer;            
            xmlStringBuffer = new StringBuffer();            
            xmlStringBuffer.append(xmlString);
            
            // Parse StringBuffer and return resultant ArrayList
            desktopApplication.Model.Strategies.XMLParser parser;            
            parser = new desktopApplication.Model.Strategies.XMLParser();
            
            ArrayList<String[]> stringList;            
            stringList = parser.parse(xmlStringBuffer);
            
            // Split returned list into two datasets
            String[] testDataOne;
            String[] testDataTwo;
            
            testDataOne = stringList.get(0);
            testDataTwo = stringList.get(1);
            
            // Test first dataset in XML document
            assertEquals(testDataOne[0], "1");
            assertEquals(testDataOne[1], "Jeff");
            assertEquals(testDataOne[2], "Testington");
            assertEquals(testDataOne[3], "Testing XML Parsing");
            
            
            // Test second dataset in XML document
            assertEquals(testDataTwo[0], "2");
            assertEquals(testDataTwo[1], "Bill");
            assertEquals(testDataTwo[2], "Parsingness");
            assertEquals(testDataTwo[3], "Parsing Incoming XML");
         
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseXML() {
    }
    
}
