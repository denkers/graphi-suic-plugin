///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import static com.graphi.app.ConfigManager.GLOBAL_CONF_FILE;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class SuicideIntentConfig 
{
    public static final String CONFIG_FILE  =   "SuicideIntentConfig.xml";
    private double directedWeight;
    private double undirectedWeight;
    private double selfWeight;

    public SuicideIntentConfig()
    {
        this(1.0, 0.5, 2.0);
    }
    
    public SuicideIntentConfig(double directedWeight, double undirectedWeight, double selfWeight)
    {
        this.directedWeight     =   directedWeight;
        this.undirectedWeight   =   undirectedWeight;
        this.selfWeight         =   selfWeight;
    }
    
    public static SuicideIntentConfig getConfig()
    {
        try
        {
            File configFile             =   new File(GLOBAL_CONF_FILE);
            DocumentBuilder docBuilder  =   DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document configDoc          =   docBuilder.parse(configFile);
            double undirectedW          =   Double.parseDouble(configDoc.getElementsByTagName("directedWeight").item(0).getTextContent());
            double directedW            =   Double.parseDouble(configDoc.getElementsByTagName("undirectedWeight").item(0).getTextContent());
            double selfW                =   Double.parseDouble(configDoc.getElementsByTagName("selfWeight").item(0).getTextContent());
            
            return new SuicideIntentConfig(directedW, undirectedW, selfW);
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "[Error] Failed to read " + CONFIG_FILE);
            return new SuicideIntentConfig();
        }
    }
    
    public double getDirectedWeight() 
    {
        return directedWeight;
    }

    public double getUndirectedWeight() 
    {
        return undirectedWeight;
    }

    public double getSelfWeight() 
    {
        return selfWeight;
    }
}
