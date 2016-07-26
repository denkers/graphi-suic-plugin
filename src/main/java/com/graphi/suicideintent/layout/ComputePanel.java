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
import com.graphi.util.ComponentUtils;
import com.graphi.util.Edge;
import com.graphi.util.GraphData;
import com.graphi.util.Node;
import edu.uci.ics.jung.visualization.RenderContext;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class ComputePanel extends JPanel implements ActionListener
{
    private final String COMPUTE_PERCEPT_CARD   =   "com_perception";
    private final String COMPUTE_INDEX_CARD     =   "com_index";

    private JCheckBox displaySizeCheck, displayColourCheck;
    private JComboBox computeBox;
    private JSpinner perspectiveSpinner;
    private JButton resetButton, computeButton;
    private JPanel selfPercPanel;
    private JLabel dataSetsLabel;
    private JPanel averageIntentPanel;
    private JPanel computeSwitchPanel;
    private JRadioButton targetSpecificRadio, targetAllRadio;
    private SuicidePanel parentPanel;
    private JLabel perspectiveLabel;

    public ComputePanel(SuicidePanel parentPanel)
    {
        setLayout(new MigLayout("fillx"));
        this.parentPanel    =   parentPanel;
        displaySizeCheck    =   new JCheckBox("Display size");
        displayColourCheck  =   new JCheckBox("Display colour");
        computeBox          =   new JComboBox();
        perspectiveSpinner  =   new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        resetButton         =   new JButton("Reset");
        computeButton       =   new JButton("Compute");
        averageIntentPanel  =   new JPanel();
        dataSetsLabel       =   new JLabel();
        computeSwitchPanel  =   new JPanel(new CardLayout());
        selfPercPanel       =   new JPanel(new MigLayout("fillx"));
        targetSpecificRadio =   new JRadioButton("Specific");
        targetAllRadio      =   new JRadioButton("All");
        perspectiveLabel    =   new JLabel("Perspective ID");
        
        ButtonGroup targetBtnGroup  =   new ButtonGroup();
        targetBtnGroup.add(targetSpecificRadio);
        targetBtnGroup.add(targetAllRadio);
        targetSpecificRadio.setSelected(true);

        computeBox.addItem("Self-Perception");
        computeBox.addItem("Suicide-Index");

        Font titleFont          =   new Font("Arial", Font.BOLD, 12);
        JLabel perspectiveTitle =   new JLabel("PerspectiveID");
        JLabel dataCountTitle   =   new JLabel("No. data sets found: ");
        perspectiveTitle.setFont(titleFont);
        dataCountTitle.setFont(titleFont);

        selfPercPanel.add(targetSpecificRadio);
        selfPercPanel.add(targetAllRadio, "wrap");
        selfPercPanel.add(perspectiveLabel);
        selfPercPanel.add(perspectiveSpinner);

        averageIntentPanel.add(dataCountTitle);
        averageIntentPanel.add(dataSetsLabel);

        computeSwitchPanel.add(selfPercPanel, COMPUTE_PERCEPT_CARD);
        computeSwitchPanel.add(averageIntentPanel, COMPUTE_INDEX_CARD);

        add(new JLabel("Target options"), "al right");
        add(computeBox, "wrap");
        add(computeSwitchPanel, "span 2, al center, wrap");
        add(resetButton, "al right");
        add(computeButton);

        resetButton.addActionListener(this);
        computeButton.addActionListener(this);    
        computeBox.addActionListener(this);
        targetAllRadio.addActionListener(this);
        targetSpecificRadio.addActionListener(this);
    }
    
    private void togglePerceptionOptions()
    {
        boolean enable  =   targetSpecificRadio.isSelected();
        perspectiveSpinner.setEnabled(enable);
        perspectiveLabel.setEnabled(enable);
    }

    private void computeExecute()
    {
        if(computeBox.getSelectedIndex() == 1)
            computeAverageSuicideIntent();

        else
            computeSuicideIntentDefault();
    }
    
    public void computeSuicideIntentDefault()
    {
        boolean computeAll      =   targetAllRadio.isSelected();
        int perspectiveIndex    =   computeAll? -1 : (int) perspectiveSpinner.getValue();
        computeSuicideIntent(computeAll, perspectiveIndex);
    }

    public void computeSuicideIntent(boolean computeAll, int perspectiveIndex)
    {
        GraphData gData         =   parentPanel.getPluginLayout().getGraphData();
        Node perspective        =   gData.getNodes().get(perspectiveIndex);

        Map<Node, Double> scores    =   IntentComputation.computeEvalScores(gData, perspective, computeAll);
        DefaultTableModel model     =   IntentComputation.getIntentTableModel(scores, false);
        parentPanel.getPluginLayout().getScreenPanel().getDataPanel().setComputationModel(model);

        String contextMessage   =   "Self perception for " + (computeAll? "all" : "node '" + perspectiveIndex + "'");
        parentPanel.getPluginLayout().getScreenPanel().getDataPanel().setComputationContext(contextMessage);
    }

    public void computeAverageSuicideIntent()
    {
        GraphData gData         =   parentPanel.getPluginLayout().getGraphData();
        GraphPlayback playback  =   parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphPlayback();
        if(playback.getEntries().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "[Error] You must have atleast two recorded entries to perform this operation");
            return;
        }
        
        DefaultTableModel model =   IntentComputation.getAverageSuicideIntent(playback, gData);
        if(model != null)
        {
            PlaybackEntry entryFirst    =   playback.getEntries().get(0);
            PlaybackEntry entryLast     =   playback.getEntries().get(playback.getSize() - 1);

            parentPanel.getPluginLayout().getScreenPanel().getDataPanel().setComputationModel(model);
            parentPanel.getPluginLayout().getScreenPanel().getDataPanel().setComputationContext("Suicide-index for entries: " + 
                                                        entryFirst.getName() + " - " + entryLast.getName());
        }

        else JOptionPane.showMessageDialog(null, "[Error] Invalid playback structure");
    }

    public void updateDataSetCount()
    {
        int size = parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphPlayback().getSize();
        dataSetsLabel.setText("" + size);
    }

    private void changeComputePanel()
    {
        int selectedOption  =   computeBox.getSelectedIndex();
        String cardName;

        switch(selectedOption)
        {
            case 0: cardName    =   COMPUTE_PERCEPT_CARD; break;
            case 1: cardName    =   COMPUTE_INDEX_CARD; updateDataSetCount(); break;
            default: return;
        }

        CardLayout cLayout  =   (CardLayout) computeSwitchPanel.getLayout();
        cLayout.show(computeSwitchPanel, cardName);
    }



    private void resetSuicideIntentDisplay() {}

    private void outputNodeSelfEvaluation(int nodeID, double selfEval)
    {
        final String format =   "(SuicideIntentPlugin) Node ID = {0}, Self Evaluation = {1}";
        String msg          =   MessageFormat.format(format, nodeID, selfEval);
        ComponentUtils.sendToOutput(msg, parentPanel.getPluginLayout().getScreenPanel().getOutputPanel().getOutputArea());
    }

    public void displayEvalScores()
    {
        int perspectiveIndex                =   (int) perspectiveSpinner.getValue();
        GraphData gData                     =   parentPanel.getPluginLayout().getGraphData();
        Node perspective                    =   gData.getNodes().get(perspectiveIndex);
        boolean computeAll                  =   computeBox.getSelectedIndex() == 1;
        boolean displayColour               =   displayColourCheck.isSelected();
        boolean displaySize                 =   displaySizeCheck.isSelected();
        Map<Node, Double> scores            =   IntentComputation.computeEvalScores(gData, perspective, computeAll);

        for(Map.Entry<Node, Double> score : scores.entrySet())
            outputNodeSelfEvaluation(score.getKey().getID(), score.getValue());

        if(displayColour || displaySize)
        {
            RenderContext<Node, Edge> context  =   parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphViewer().getRenderContext();

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

        else if(src == computeBox)
            changeComputePanel();

        else if(src == computeButton)
            computeExecute();
        
        else if(src == targetSpecificRadio || src == targetAllRadio)
            togglePerceptionOptions();
    }
}