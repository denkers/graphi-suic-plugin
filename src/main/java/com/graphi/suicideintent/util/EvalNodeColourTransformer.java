///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.util;

import com.graphi.util.Node;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections15.Transformer;


public class EvalNodeColourTransformer implements Transformer<Node, Paint>
{
    private Map<Node, Double> scores;
    
    public EvalNodeColourTransformer(Map<Node, Double> scores)
    {
        this.scores =   scores;
    }
    
    @Override
    public Paint transform(Node i) 
    {
        List<Node> nodes    =   new ArrayList<>(scores.keySet());
        int index           =   nodes.indexOf(i);
        int colourIntensity =   255;
        int reduceValue     =   25;
        int redColour       =   colourIntensity - (index * reduceValue);
        
        return new Color(redColour, 0, 0);
    }
}
