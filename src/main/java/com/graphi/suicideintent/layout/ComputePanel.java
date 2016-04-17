
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class ComputePanel extends JPanel implements ActionListener
{
    private final String COMPUTE_SPECIFIC_CARD  =   "com_specific";
    private final String COMPUTE_AVERAGE_CARD   =   "com_average";
    private final String COMPUTE_ALL_CARD       =   "com_all";

    private JCheckBox displaySizeCheck, displayColourCheck;
    private JComboBox computeBox;
    private JSpinner perspectiveSpinner;
    private JButton resetButton, computeButton;
    private JPanel computeSpecificPanel;
    private JLabel dataSetsLabel;
    private JPanel averageIntentPanel;
    private JPanel computeSwitchPanel;
    private SuicideIntentControlPanel parentPanel;

    public ComputePanel(SuicideIntentControlPanel parentPanel)
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


        computeBox.addItem("Specific");
        computeBox.addItem("All");
        computeBox.addItem("Average");

        Font titleFont          =   new Font("Arial", Font.BOLD, 12);
        JLabel perspectiveTitle =   new JLabel("PerspectiveID");
        JLabel dataCountTitle   =   new JLabel("No. data sets found: ");
        perspectiveTitle.setFont(titleFont);
        dataCountTitle.setFont(titleFont);

        computeSpecificPanel =   new JPanel();
        computeSpecificPanel.add(new JLabel("Perspective ID"));
        computeSpecificPanel.add(perspectiveSpinner);

        averageIntentPanel.add(dataCountTitle);
        averageIntentPanel.add(dataSetsLabel);

        computeSwitchPanel.add(computeSpecificPanel, COMPUTE_SPECIFIC_CARD);
        computeSwitchPanel.add(new JPanel(), COMPUTE_ALL_CARD);
        computeSwitchPanel.add(averageIntentPanel, COMPUTE_AVERAGE_CARD);

      //  add(displaySizeCheck);
       // add(displayColourCheck, "wrap");
        add(new JLabel("Target options"));
        add(computeBox, "wrap");
        add(computeSwitchPanel, "span 2, wrap");
        add(resetButton, "al right");
        add(computeButton);

        resetButton.addActionListener(this);
        computeButton.addActionListener(this);    
        computeBox.addActionListener(this);
    }

    private void computeExecute()
    {
        if(computeBox.getSelectedIndex() == 2)
            computeAverageSuicideIntent();

        else
            computeSuicideIntent();
    }

    public void computeSuicideIntent()
    {
        boolean computeAll      =   computeBox.getSelectedIndex() == 0;
        int perspectiveIndex    =   computeAll? -1 : (int) perspectiveSpinner.getValue();
        GraphData gData         =   parentPanel.getPluginLayout().getGraphData();
        Node perspective        =   gData.getNodes().get(perspectiveIndex);

        Map<Node, Double> scores    =   IntentComputation.computeEvalScores(gData, perspective, computeAll);
        DefaultTableModel model     =   IntentComputation.getIntentTableModel(scores);
        parentPanel.getPluginLayout().getScreenPanel().getDataPanel().setComputationModel(model);

        String contextMessage   =   "Suicide intent for " + (computeAll? "all" : "node '" + perspectiveIndex + "'");
        parentPanel.getPluginLayout().getScreenPanel().getDataPanel().setComputationContext(contextMessage);
    }

    public void computeAverageSuicideIntent()
    {
        GraphData gData         =   parentPanel.getPluginLayout().getGraphData();
        GraphPlayback playback  =   parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphPlayback();
        DefaultTableModel model =   IntentComputation.getAverageSuicideIntent(playback, gData);

        if(model != null)
        {
            PlaybackEntry entryFirst    =   playback.getEntries().get(0);
            PlaybackEntry entryLast     =   playback.getEntries().get(playback.getSize() - 1);

            parentPanel.getPluginLayout().getScreenPanel().getDataPanel().setComputationModel(model);
            parentPanel.getPluginLayout().getScreenPanel().getDataPanel().setComputationContext("Average suicide intent from entries: " + 
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
            case 0: cardName    =   COMPUTE_ALL_CARD; break;
            case 1: cardName    =   COMPUTE_SPECIFIC_CARD; break;
            case 2: cardName    =   COMPUTE_AVERAGE_CARD; updateDataSetCount(); break;
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
        boolean computeAll                  =   computeBox.getSelectedIndex() == 0;
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
    }
}