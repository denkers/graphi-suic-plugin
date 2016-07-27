///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.suicideintent.util.SuicideFillTransformer;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.visualization.RenderContext;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class SuicidePanel extends JPanel
{
    private PluginLayout parentPanel;
    private SimulationPanel simPanel;
    private ConfigPanel configPanel;
    private ComputePanel computePanel;
    private JTabbedPane controlsTabPane;
    
    public SuicidePanel(PluginLayout parentPanel)
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Suicide intent controls"));
        
        this.parentPanel    =   parentPanel;
        computePanel        =   new ComputePanel(this);
        simPanel            =   new SimulationPanel(this);
        configPanel         =   new ConfigPanel();
        controlsTabPane     =   new JTabbedPane();
        
        controlsTabPane.addTab("Computation", computePanel);
        controlsTabPane.addTab("Simulation", simPanel);
        controlsTabPane.addTab("Config", configPanel);
        
        RenderContext<Node, Edge> rc  =   parentPanel.getScreenPanel().getGraphPanel().getGraphViewer().getRenderContext();
        rc.setVertexFillPaintTransformer(new SuicideFillTransformer<>(rc.getPickedVertexState()));
        rc.setEdgeDrawPaintTransformer(new SuicideFillTransformer<>(rc.getPickedEdgeState()));
        
        add(controlsTabPane);
    }
    
    public PluginLayout getPluginLayout()
    {
        return parentPanel;
    }

    public PluginLayout getParentPanel() 
    {
        return parentPanel;
    }

    public SimulationPanel getSimPanel()
    {
        return simPanel;
    }

    public ConfigPanel getConfigPanel() 
    {
        return configPanel;
    }

    public ComputePanel getComputePanel() 
    {
        return computePanel;
    }
}