/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Strategies;

import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Reecus
 */
public class XMLParser implements ParserStrategy
{

    public XMLParser() {
    }

    /**
     * Generates a string list of attributes from the received XML and returns it.
     * @param receivedData The StringBuffer received from the API data.
     * @return String list object containing attributes parsed from the XML.
     */
    @Override
    public ArrayList<String[]> parse(StringBuffer receivedData)
    {
        try
        {
            DocumentBuilder parser;
            StringReader reader;
            InputSource source;
            Document document;
            
            ArrayList<String[]> returnString;
            returnString = new ArrayList<>();
            
            // Generates required inputs for the parser to read the XML
            parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            reader = new StringReader(receivedData.toString());
            source = new InputSource(reader);
            document = parser.parse(source);
            
            NodeList nodes = document.getChildNodes();
            
            // If nodes are returned, get the first and add specific attributes to the returnString list.
            if (nodes.getLength() > 0)
            {
                // Get the first element, which will be the <ArrayOfX> tag if several values are retrieved.
                Element e = (Element) nodes.item(0);
                
                // For every item in the XML retrieved, produce a string array and add to an arrayList of returned values.
                for (int i = 0; i < e.getChildNodes().getLength(); i++)
                {
                    Element data;
                    data = (Element) e.getChildNodes().item(i);
                    
                    String[] itemString;
                
                    itemString = new String[data.getChildNodes().getLength()];
                    
                    for (int j = 0; j < data.getChildNodes().getLength(); j++)
                    {                        
                        itemString[j] = (data.getChildNodes().item(j).getTextContent());
                    }
                
                    returnString.add(itemString);
                    
                }            
                
            }
            
            return returnString;
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        // Return null if an exception is thrown and the return list is not generated.
        return null;
        
    }
    public ArrayList<String[]> ParseXML(StringBuffer response)
    {
        ArrayList<String[]> returnList;        
        
        returnList = parse(response);
        
        return returnList;
    }
}
