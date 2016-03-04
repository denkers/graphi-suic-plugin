///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.app.Consts;
import com.graphi.suicideintent.IntentComputation;
import com.graphi.util.ComponentUtils;
import com.graphi.util.GraphData;
import com.graphi.util.Node;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;

public class SuicideIntentControlPanel extends JPanel implements ActionListener
{
    private JCheckBox displaySizeCheck, displayColourCheck;
    private JComboBox computeBox;
    private JSpinner perspectiveSpinner;
    private JButton resetButton, computeButton;
    private PluginLayout parentPanel;
    
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
        
        add(panelWrapper);
        
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
        List<Entry<Node, Double>> scores    =   IntentComputation.computeEvalScores(gData, perspectiveIndex, computeAll);
        
        for(Entry<Node, Double> score : scores)
            outputNodeSelfEvaluation(score.getKey(), score.getValue());
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
}
