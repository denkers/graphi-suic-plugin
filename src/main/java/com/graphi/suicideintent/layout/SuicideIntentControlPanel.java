///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.sim.GraphPlayback;
import com.graphi.sim.PlaybackEntry;
import com.graphi.suicideintent.IntentComputation;
import com.graphi.suicideintent.util.EvalNodeColourTransformer;
import com.graphi.suicideintent.util.EvalNodeSizeTransformer;
import com.graphi.suicideintent.util.SuicideFillTransformer;
import com.graphi.util.ComponentUtils;
import com.graphi.util.Edge;
import com.graphi.util.GraphData;
import com.graphi.util.Node;
import edu.uci.ics.jung.visualization.RenderContext;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class SuicideIntentControlPanel extends JPanel
{
    private PluginLayout parentPanel;
    private SimulationPanel simPanel;
    private ConfigPanel configPanel;
    private ComputePanel computePanel;
    private TaskPanel taskPanel;
    private JTabbedPane controlsTabPane;
    
    public SuicideIntentControlPanel(PluginLayout parentPanel)
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Suicide intent controls"));
        
        this.parentPanel    =   parentPanel;
        computePanel        =   new ComputePanel(this);
        simPanel            =   new SimulationPanel(this);
        configPanel         =   new ConfigPanel();
        taskPanel           =   new TaskPanel(this);
        controlsTabPane     =   new JTabbedPane();
        
        controlsTabPane.addTab("Computation", computePanel);
        controlsTabPane.addTab("Simulation", simPanel);
        controlsTabPane.addTab("Task", taskPanel);
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
    
    
}