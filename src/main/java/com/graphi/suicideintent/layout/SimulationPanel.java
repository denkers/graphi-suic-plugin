
package com.graphi.suicideintent.layout;

import com.graphi.suicideintent.sim.SuicideSimulation;
import com.graphi.suicideintent.util.SuicideEdge;
import com.graphi.suicideintent.util.SuicideNode;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;


public class SimulationPanel extends JPanel implements ActionListener
{
    private final String RAND_DELETE_CARD   =   "Delete random";
    private final String AUTO_DELETE_CARD   =   "Auto delete";

    private DeleteRandomSimPanel randomDeletePanel;
    private JComboBox simTypeBox;
    private JPanel simChangePanel;
    private SuicideIntentControlPanel parentPanel;

    public SimulationPanel(SuicideIntentControlPanel parentPanel)
    {
        setLayout(new MigLayout("fillx"));
        this.parentPanel    =   parentPanel;
        simChangePanel      =   new JPanel(new CardLayout());
        randomDeletePanel   =   new DeleteRandomSimPanel();
        simTypeBox          =   new JComboBox();

        simChangePanel.add(randomDeletePanel, RAND_DELETE_CARD);
        simTypeBox.addItem(RAND_DELETE_CARD);
        simTypeBox.addItem(AUTO_DELETE_CARD);
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
        private JButton executeButton, clearButton;

        public DeleteRandomSimPanel()
        {
            setLayout(new MigLayout("fillx"));
            edgeDeleteRadio =   new JRadioButton("Edges");
            nodeDeleteRadio =   new JRadioButton("Nodes");
            probField       =   new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
            objDeleteGroup  =   new ButtonGroup();
            executeButton   =   new JButton("Execute");
            clearButton     =   new JButton("Clear");

            probField.setPreferredSize(new Dimension(50, 10));
            objDeleteGroup.add(edgeDeleteRadio);
            objDeleteGroup.add(nodeDeleteRadio);
            nodeDeleteRadio.setSelected(true);

            add(nodeDeleteRadio, "al center");
            add(edgeDeleteRadio, "wrap");
            add(new JLabel("Probability"));
            add(probField, "wrap");
            add(clearButton);
            add(executeButton);

            executeButton.addActionListener(this);
            clearButton.addActionListener(this);
        }

        private void clearDeadObjects()
        {
            Graph<Node, Edge> graph =   parentPanel.getPluginLayout().getData().getGraph();
            for(Node node : graph.getVertices())
                ((SuicideNode) node).setDeleted(false);

            for(Edge edge : graph.getEdges())
                ((SuicideEdge) edge).setDeleted(false);

            parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphViewer().repaint();
        }

        private void executeDelete()
        {
            double p            =   (Double) probField.getValue();
            boolean deleteNodes =   nodeDeleteRadio.isSelected();

            SuicideSimulation.killGraphObjects(parentPanel.getPluginLayout().getGraphData().getGraph(), p, deleteNodes);
            parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphViewer().repaint();
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            if(src == executeButton)
                executeDelete();

            else if(src == clearButton)
                clearDeadObjects();
        }
    }
}