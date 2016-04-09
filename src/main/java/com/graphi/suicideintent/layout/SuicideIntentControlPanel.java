///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.app.Consts;
import com.graphi.suicideintent.IntentComputation;
import com.graphi.suicideintent.util.EvalNodeColourTransformer;
import com.graphi.suicideintent.util.EvalNodeSizeTransformer;
import com.graphi.util.ComponentUtils;
import com.graphi.util.Edge;
import com.graphi.util.GraphData;
import com.graphi.util.Node;
import edu.uci.ics.jung.visualization.RenderContext;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;

public class SuicideIntentControlPanel extends JPanel implements ActionListener
{
    private JCheckBox displaySizeCheck, displayColourCheck;
    private JComboBox computeBox;
    private JSpinner perspectiveSpinner;
    private JButton resetButton, computeButton;
    private PluginLayout parentPanel;
    private JPanel suicidePanel;
    private SuicideSimulationPanel simPanel;
    private JTabbedPane controlsTabPane;
    
    public SuicideIntentControlPanel(PluginLayout parentPanel)
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Suicide intent controls"));
        
        this.parentPanel    =   parentPanel;
        displaySizeCheck    =   new JCheckBox("Display size");
        displayColourCheck  =   new JCheckBox("Display colour");
        computeBox          =   new JComboBox();
        perspectiveSpinner  =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        resetButton         =   new JButton("Reset");
        computeButton       =   new JButton("Compute");
        suicidePanel        =   new JPanel(new BorderLayout());
        simPanel            =   new SuicideSimulationPanel();
        controlsTabPane     =   new JTabbedPane();
        
        computeBox.addItem("All");
        computeBox.addItem("Selected");
        
        JPanel panelWrapper =   new JPanel(new MigLayout("fillx"));
        panelWrapper.setBackground(Consts.PRESET_COL);
        
        panelWrapper.add(displaySizeCheck);
        panelWrapper.add(displayColourCheck, "wrap");
        panelWrapper.add(new JLabel("Target options"));
        panelWrapper.add(computeBox, "wrap");
        panelWrapper.add(new JLabel("Perspective ID"));
        panelWrapper.add(perspectiveSpinner, "wrap");
        panelWrapper.add(resetButton, "al right");
        panelWrapper.add(computeButton);
        
        suicidePanel.add(panelWrapper);
        controlsTabPane.addTab("Computation", suicidePanel);
        controlsTabPane.addTab("Simulation", simPanel);
        
        add(controlsTabPane);
        
        resetButton.addActionListener(this);
        computeButton.addActionListener(this);
    }
    
    private void resetSuicideIntentDisplay()
    {
    }
    
    private void outputNodeSelfEvaluation(Node node, double selfEval)
    {
        final String format =   "(SuicideIntentPlugin) Node ID = {0}, Self Evaluation = {1}";
        String msg          =   MessageFormat.format(format, node.getID(), selfEval);
        ComponentUtils.sendToOutput(msg, parentPanel.getScreenPanel().getOutputPanel().getOutputArea());
    }
    
    public void displayEvalScores()
    {
        int perspectiveIndex                =   (int) perspectiveSpinner.getValue();
        GraphData gData                     =   parentPanel.getGraphData();
        boolean computeAll                  =   computeBox.getSelectedIndex() == 0;
        boolean displayColour               =   displayColourCheck.isSelected();
        boolean displaySize                 =   displaySizeCheck.isSelected();
        Map<Node, Double> scores            =   IntentComputation.computeEvalScores(gData, perspectiveIndex, computeAll);
        
        for(Entry<Node, Double> score : scores.entrySet())
            outputNodeSelfEvaluation(score.getKey(), score.getValue());
        
        if(displayColour || displaySize)
        {
            RenderContext<Node, Edge> context  =   parentPanel.getScreenPanel().getGraphPanel().getGraphViewer().getRenderContext();
            
            if(displayColour)
                context.setVertexFillPaintTransformer(new EvalNodeColourTransformer(scores));
            
            if(displaySize)
                context.setVertexShapeTransformer(new EvalNodeSizeTransformer(scores));
        }
    }
    

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == resetButton)
            resetSuicideIntentDisplay();
        
        else if(src == computeButton)
            displayEvalScores();
    }
    
    private class SuicideSimulationPanel extends JPanel implements ActionListener
    {
        private final String RAND_DELETE_CARD   =   "Delete random";
        
        private DeleteRandomSimPanel randomDeletePanel;
        private JComboBox simTypeBox;
        private JPanel simChangePanel;
        
        public SuicideSimulationPanel()
        {
            setLayout(new MigLayout("fillx"));
            simChangePanel      =   new JPanel(new CardLayout());
            randomDeletePanel   =   new DeleteRandomSimPanel();
            simTypeBox          =   new JComboBox();
            
            simChangePanel.add(randomDeletePanel, RAND_DELETE_CARD);
            simTypeBox.addItem(RAND_DELETE_CARD);
            simTypeBox.addActionListener(this);
            
            add(simTypeBox, "wrap, al center");
            add(simChangePanel, "al center");
        }
        
        private void changeSim(String name)
        {
            CardLayout cLayout  =   (CardLayout) simChangePanel.getLayout();
            cLayout.show(simChangePanel, name);
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            if(src == simTypeBox)
                changeSim(simTypeBox.getSelectedItem().toString());
        }
        
        private class DeleteRandomSimPanel extends JPanel implements ActionListener
        {
            private JRadioButton edgeDeleteRadio, nodeDeleteRadio;
            private ButtonGroup objDeleteGroup;
            private JSpinner probField;
            private JButton executeButton;
            
            public DeleteRandomSimPanel()
            {
                setLayout(new MigLayout("fillx"));
                edgeDeleteRadio =   new JRadioButton("Edges");
                nodeDeleteRadio =   new JRadioButton("Nodes");
                probField       =   new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
                objDeleteGroup  =   new ButtonGroup();
                executeButton   =   new JButton("Execute");
                    
                probField.setPreferredSize(new Dimension(50, 10));
                objDeleteGroup.add(edgeDeleteRadio);
                objDeleteGroup.add(nodeDeleteRadio);
                nodeDeleteRadio.setSelected(true);
                
                add(nodeDeleteRadio, "al center");
                add(edgeDeleteRadio, "wrap");
                add(new JLabel("Probability"));
                add(probField, "wrap");
                add(executeButton, "al center, span 2");
                
                executeButton.addActionListener(this);
            }
            

                
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                
            }
        }
    }
}
