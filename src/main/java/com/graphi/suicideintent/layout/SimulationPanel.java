
package com.graphi.suicideintent.layout;

import com.graphi.suicideintent.SuicideSimulation;
import com.graphi.suicideintent.util.SuicideNode;
import com.graphi.util.Edge;
import com.graphi.util.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;


public class SimulationPanel extends JPanel implements ActionListener
{
    private final String DELETE_SIM_CARD       =   "Delete/Kill simulation";

    private DeleteSimPanel deletePanel;
    private JComboBox simTypeBox;
    private JPanel simChangePanel;
    private SuicidePanel parentPanel;

    public SimulationPanel(SuicidePanel parentPanel)
    {
        setLayout(new MigLayout("fillx"));
        this.parentPanel    =   parentPanel;
        simChangePanel      =   new JPanel(new CardLayout());
        deletePanel         =   new DeleteSimPanel();
        simTypeBox          =   new JComboBox();

        simChangePanel.add(deletePanel, DELETE_SIM_CARD);
        simTypeBox.addItem(DELETE_SIM_CARD);
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
    
    public void clearDeadObjects()
    {
        Graph<Node, Edge> graph =   parentPanel.getPluginLayout().getData().getGraph();
        for(Node node : graph.getVertices())
            ((SuicideNode) node).setDeleted(false);

        parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphViewer().repaint();
    }

    public void executeDelete()
    {
        if(deletePanel.randomRadio.isSelected())
            deleteRandomNodes();
        else
            deleteTargetNodes();
        parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphViewer().repaint();
    }
    
    private void deleteRandomNodes()
    {
        double p  =   (Double) deletePanel.randomPanel.probField.getValue();
        SuicideSimulation.killGraphObjects(parentPanel.getPluginLayout().getGraphData().getGraph(), p, true);
    }
    
    private void deleteTargetNodes()
    {
        if(deletePanel.targetPanel.targetTypeBox.getSelectedIndex() == 0)
        {
            Map<Integer, Node> nodes    =   parentPanel.getPluginLayout().getGraphData().getNodes();
            int nodeID                  =   (int) deletePanel.targetPanel.targetField.getValue();
            Node targetNode             =   nodes.get(nodeID);
            
            if(targetNode == null)
                JOptionPane.showMessageDialog(null, "[Error] Invalid node ID");
            else
                ((SuicideNode) targetNode).setDeleted(true);
        }
        
        else
        {
            PickedState<Node> pickedNodeState   =   parentPanel.getPluginLayout().getScreenPanel().getGraphPanel().getGraphViewer().getPickedVertexState();
            Set<Node> pickedNodes               =   pickedNodeState.getPicked();
            
            for(Node node : pickedNodes)
                ((SuicideNode) node).setDeleted(true);
        }
        
    }

    private class DeleteSimPanel extends JPanel implements ActionListener
    {
        private final String DELETE_RANDOM_CARD     =   "delete_random";
        private final String DELETE_TARGET_CARD     =   "delete_target";
        
        private ButtonGroup targetDeleteGroup;
        private JButton executeButton, clearButton;
        private JRadioButton targetRadio, randomRadio;
        private DeleteRandomPanel randomPanel;
        private DeleteTargetPanel targetPanel;
        private JPanel deleteCardPanel;

        public DeleteSimPanel()
        {
            setLayout(new MigLayout("fillx"));
            targetDeleteGroup   =   new ButtonGroup();
            targetRadio         =   new JRadioButton("Target");
            randomRadio         =   new JRadioButton("Random");
            executeButton       =   new JButton("Execute");
            clearButton         =   new JButton("Clear");
            deleteCardPanel     =   new JPanel(new CardLayout());
            targetPanel         =   new DeleteTargetPanel();
            randomPanel         =   new DeleteRandomPanel();

            targetDeleteGroup.add(targetRadio);
            targetDeleteGroup.add(randomRadio);
            randomRadio.setSelected(true);
            
            deleteCardPanel.add(randomPanel, DELETE_RANDOM_CARD);
            deleteCardPanel.add(targetPanel, DELETE_TARGET_CARD);
            
            JPanel controlWrapper   =   new JPanel();
            controlWrapper.add(clearButton);
            controlWrapper.add(executeButton);

            add(randomRadio, "");
            add(targetRadio, "wrap");
            add(deleteCardPanel, "al center, span 3, wrap");
            add(controlWrapper, "al center, span 3");

            executeButton.addActionListener(this);
            clearButton.addActionListener(this);
            randomRadio.addActionListener(this);
            targetRadio.addActionListener(this);
        }
        
        private void changeDeletePanel()
        {
            CardLayout cLayout  =   (CardLayout) deleteCardPanel.getLayout();
            String card;
            
            if(randomRadio.isSelected()) card   =   DELETE_RANDOM_CARD;
            else card   =   DELETE_TARGET_CARD;    
            
            cLayout.show(deleteCardPanel, card);
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Object src  =   e.getSource();
            if(src == executeButton)
                executeDelete();

            else if(src == clearButton)
                clearDeadObjects();
            
            else if(src == randomRadio || src == targetRadio);
                changeDeletePanel();
        }
        
        private class DeleteRandomPanel extends JPanel
        {
            private JSpinner probField;
            private JLabel probLabel;
            
            public DeleteRandomPanel()
            {
                probField   =   new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
                probLabel   =   new JLabel("Probability ");
                probField.setPreferredSize(new Dimension(50, 25));
                
                add(probLabel);
                add(probField);
            }
        }
    }
    
    private class DeleteTargetPanel extends JPanel implements ActionListener
    {
        private JSpinner targetField;
        private JLabel targetLabel;
        private JComboBox targetTypeBox;
        
        public DeleteTargetPanel()
        {
            setLayout(new MigLayout("fillx"));
            targetTypeBox   =   new JComboBox();
            targetField     =   new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
            targetLabel     =   new JLabel("Node ID");
            
            targetTypeBox.addItem("Specific");
            targetTypeBox.addItem("Selected");
            add(new JLabel("Target type"));
            add(targetTypeBox, "wrap");
            add(targetLabel);
            add(targetField);
            
            targetTypeBox.addActionListener(this);
        }
        
        private void toggleTargetFields(boolean enable)
        {
            targetField.setEnabled(enable);
            targetLabel.setEnabled(enable);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            toggleTargetFields(targetTypeBox.getSelectedIndex() == 0);
        }
    }
}