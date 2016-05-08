///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import com.graphi.app.Consts;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SuicideIntentConfig 
{
    public static final String CONFIG_FILE  =   "data/config.xml";
    private static SuicideIntentConfig instance;
    private double directedWeight;
    private double undirectedWeight;
    private double selfWeight;
    private double deadWeight;

    public SuicideIntentConfig()
    {
        this(1.0, 0.5, 2.0, 0.1);
    }
    
    public SuicideIntentConfig(double directedWeight, double undirectedWeight, double selfWeight, double deadWeight)
    {
        this.directedWeight     =   directedWeight;
        this.undirectedWeight   =   undirectedWeight;
        this.selfWeight         =   selfWeight;
        this.deadWeight         =   deadWeight;
    }
    
    public static SuicideIntentConfig loadConfig()
    {
        try
        {
            final String DIRECTORY      =   new File(SuicideIntentConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            File configFile             =   new File(DIRECTORY + "/" + Consts.GLOBAL_CONF_FILE);
            DocumentBuilder docBuilder  =   DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document configDoc          =   docBuilder.parse(configFile);
            double undirectedW          =   Double.parseDouble(configDoc.getElementsByTagName("directedWeight").item(0).getTextContent());
            double directedW            =   Double.parseDouble(configDoc.getElementsByTagName("undirectedWeight").item(0).getTextContent());
            double selfW                =   Double.parseDouble(configDoc.getElementsByTagName("selfWeight").item(0).getTextContent());
            double deadWeight           =   Double.parseDouble(configDoc.getElementsByTagName("deadWeight").item(0).getTextContent());
            
            return new SuicideIntentConfig(directedW, undirectedW, selfW, deadWeight);
        }
        
        catch(IOException | SAXException | ParserConfigurationException | URISyntaxException e)
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

    public double getDeadWeight()
    {
        return deadWeight;
    }
    
    public static SuicideIntentConfig refreshConfig()
    {
        instance    =   loadConfig();
        return instance;
    }
    
    public static SuicideIntentConfig getConfig()
    {
        if(instance == null) instance   =   loadConfig();
        return instance;
    }
}
