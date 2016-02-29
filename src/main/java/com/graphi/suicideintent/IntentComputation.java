///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import com.graphi.suicideintent.util.SuicideGModelTransformer;
import com.graphi.util.Edge;
import com.graphi.util.MatrixTools;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;

public class IntentComputation 
{
    public static SparseDoubleMatrix2D getSelfEvaluationVector(int nodeIndex, Graph<Node, Edge> g)
    {
        SuicideGModelTransformer transformer    =   new SuicideGModelTransformer(nodeIndex, SuicideIntentPlugin.CONFIG);
        SparseDoubleMatrix2D matrix             =   transformer.transform(g);
        SparseDoubleMatrix2D evalVector         =   MatrixTools.powerIterationFull(matrix);
        
        return evalVector;
    }
    
    public static double getSelfEvaluation(int nodeIndex, Graph<Node, Edge> g)
    {
        SparseDoubleMatrix2D evalVector =   getSelfEvaluationVector(nodeIndex, g);
        return evalVector.get(nodeIndex, nodeIndex);
    }
}
