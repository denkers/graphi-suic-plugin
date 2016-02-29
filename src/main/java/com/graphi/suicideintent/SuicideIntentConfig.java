///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;


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
