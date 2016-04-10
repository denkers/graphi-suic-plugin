///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.suicideintent.IntentComputation;
import com.graphi.suicideintent.SuicideIntentConfig;
import com.graphi.suicideintent.SuicideIntentPlugin;
import com.graphi.suicideintent.sim.SuicideSimulation;
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
import java.awt.Font;
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
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class SuicideIntentControlPanel extends JPanel
{
    private PluginLayout parentPanel;
    private SuicideSimulationPanel simPanel;
    private SuicideConfigPanel configPanel;
    private ComputePanel computePanel;
    private JTabbedPane controlsTabPane;
    
    public SuicideIntentControlPanel(PluginLayout parentPanel)
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Suicide intent controls"));
        
        this.parentPanel    =   parentPanel;
        computePanel        =   new ComputePanel();
        simPanel            =   new SuicideSimulationPanel();
        configPanel         =   new SuicideConfigPanel();
        controlsTabPane     =   new JTabbedPane();
        
        controlsTabPane.addTab("Computation", computePanel);
        controlsTabPane.addTab("Simulation", simPanel);
        controlsTabPane.addTab("Config", configPanel);
        
        add(controlsTabPane);
    }
    
    private class ComputePanel extends JPanel implements ActionListener
    {
        private JCheckBox displaySizeCheck, displayColourCheck;
        private JComboBox computeBox;
        private JSpinner perspectiveSpinner;
        private JButton resetButton, computeButton;
        private JPanel computeSpecificPanel;
        
        public ComputePanel()
        {
            setLayout(new MigLayout("fillx"));
            displaySizeCheck    =   new JCheckBox("Display size");
            displayColourCheck  =   new JCheckBox("Display colour");
            computeBox          =   new JComboBox();
            perspectiveSpinner  =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
            resetButton         =   new JButton("Reset");
            computeButton       =   new JButton("Compute");

            computeBox.addItem("All");
            computeBox.addItem("Specific");
            
            computeSpecificPanel =   new JPanel();
            computeSpecificPanel.add(new JLabel("Perspective ID"));
            computeSpecificPanel.add(perspectiveSpinner);

            toggleSpecificPanel();
            
            add(displaySizeCheck);
            add(displayColourCheck, "wrap");
            add(new JLabel("Target options"));
            add(computeBox, "wrap");
            add(computeSpecificPanel, "span 2, wrap");
            add(resetButton, "al right");
            add(computeButton);

            resetButton.addActionListener(this);
            computeButton.addActionListener(this);    
            computeBox.addActionListener(this);
        }
        
        private void computeSuicideIntent()
        {
            boolean computeAll      =   computeBox.getSelectedIndex() == 0;
            int perspectiveIndex    =   computeAll? -1 : (int) perspectiveSpinner.getValue();
            GraphData gData         =   parentPanel.getGraphData();
            
            DefaultTableModel model =   IntentComputation.getIntentTableModel(gData, perspectiveIndex, computeAll);
            parentPanel.getScreenPanel().getDataPanel().setComputationModel(model);
            
            String contextMessage   =   "Suicide intent for " + (computeAll? "all" : "node '" + perspectiveIndex + "'");
            parentPanel.getScreenPanel().getDataPanel().setComputationContext(contextMessage);
        }
        
        private void toggleSpecificPanel()
        {
            computeSpecificPanel.setVisible(computeBox.getSelectedIndex() == 1);
        }
        
        private void resetSuicideIntentDisplay() {}
        
        private void outputNodeSelfEvaluation(int nodeID, double selfEval)
        {
            final String format =   "(SuicideIntentPlugin) Node ID = {0}, Self Evaluation = {1}";
            String msg          =   MessageFormat.format(format, nodeID, selfEval);
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
                outputNodeSelfEvaluation(score.getKey().getID(), score.getValue());

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
            
            else if(src == computeBox)
                toggleSpecificPanel();
            
            else if(src == computeButton)
                computeSuicideIntent();
        }
    }
    
    private class SuicideConfigPanel extends JPanel implements ActionListener
    {
        private JButton reloadConfigBtn;
        private JLabel dirWeightLabel, undirWeightLabel, selfWeightLabel;
        private JLabel deadWeightLabel;
        
        public SuicideConfigPanel()
        {
            setLayout(new MigLayout("fillx"));
            reloadConfigBtn     =   new JButton("Reload config");
            dirWeightLabel      =   new JLabel();
            undirWeightLabel    =   new JLabel();
            selfWeightLabel     =   new JLabel();
            deadWeightLabel     =   new JLabel();

            JLabel dirWeightTitle   =   new JLabel("Directed weight:");
            JLabel undirWeightTitle =   new JLabel("Undirected weight:");
            JLabel selfWeightTitle  =   new JLabel("Self weight:");
            JLabel deadWeightTitle  =   new JLabel("Dead weight:");
            Font titleFont          =   new Font("Arial", Font.BOLD, 12);
            
            dirWeightTitle.setFont(titleFont);
            undirWeightTitle.setFont(titleFont);
            selfWeightTitle.setFont(titleFont);
            deadWeightTitle.setFont(titleFont);
            
            add(dirWeightTitle);
            add(dirWeightLabel, "wrap");
            add(undirWeightTitle);
            add(undirWeightLabel, "wrap");
            add(selfWeightTitle);
            add(selfWeightLabel, "wrap");
            add(deadWeightTitle);
            add(deadWeightLabel, "wrap");
            add(reloadConfigBtn, "al center, span 2");
            
            updateConfig();
            reloadConfigBtn.addActionListener(this);
        }
        
        private void updateConfig()
        {
            SuicideIntentConfig config  =   SuicideIntentPlugin.CONFIG;
            dirWeightLabel.setText("" + config.getDirectedWeight());
            undirWeightLabel.setText("" + config.getUndirectedWeight());
            selfWeightLabel.setText("" + config.getSelfWeight());
            deadWeightLabel.setText("" + config.getDeadWeight());
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            
            if(src == reloadConfigBtn)
            {
                SuicideIntentPlugin.reloadConfig();
                updateConfig();
            }
        }
        
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
            
            private void executeDelete()
            {
                double p            =   (Double) probField.getValue();
                boolean deleteNodes =   nodeDeleteRadio.isSelected();
                
                SuicideSimulation.deleteGraphObjs(p, parentPanel.getData().getGraph(), deleteNodes);
                parentPanel.getScreenPanel().getGraphPanel().getGraphViewer().repaint();
            }
                
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Object src  =   e.getSource();
                if(src == executeButton)
                    executeDelete();
            }
        }
    }
}
