///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.util;

import com.graphi.util.Node;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections15.Transformer;


public class EvalNodeSizeTransformer implements Transformer<Node, Shape>
{
    private Map<Node, Double> scores;
    
    public EvalNodeSizeTransformer(Map<Node, Double> scores)
    {
        this.scores =   scores;
    }
    
    @Override
    public Shape transform(Node i)  
    {
        List<Node> nodes    =   new ArrayList<>(scores.keySet());
        int index           =   nodes.indexOf(i);
        int maxSize         =   60;
        int minSize         =   15;
        int reduceValue     =   5;
        int size            =   maxSize - (index * reduceValue);
        
        if(size < minSize) size = minSize;
        
        return new Ellipse2D.Double(-10, -10, size, size);
    }
}
