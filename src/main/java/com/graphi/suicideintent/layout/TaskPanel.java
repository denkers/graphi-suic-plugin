
package com.graphi.suicideintent.layout;

import com.graphi.app.Consts;
import com.graphi.suicideintent.util.ButtonColumn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.miginfocom.swing.MigLayout;

public class TaskPanel extends JPanel implements ActionListener
{
    private final String[] OPTIONS    =   
    { 
        "Record graph", 
        "Simulate network", 
        "Suicide computation", 
        "Reset network simulation",
        "Suicide simulation",
        "Reset suicide simulation",
        "Average suicide computation"
    };
    
    private JButton setupButton, repeatButton;
    private SuicideIntentControlPanel controlPanel;
    private JComboBox repeatBox;
    private TaskPopupPanel setupPanel, repeatPanel;
    
    public TaskPanel(SuicideIntentControlPanel controlPanel)
    {
        setLayout(new MigLayout("fillx"));
        
        this.controlPanel   =   controlPanel;
        setupPanel          =   new TaskPopupPanel();
        repeatPanel         =   new TaskPopupPanel();
        setupButton         =   new JButton("Setup tasks");
        repeatButton        =   new JButton("Repeat tasks");
        repeatBox           =   new JComboBox();
        
        repeatBox.addItem("Automatic");
        repeatBox.addItem("Manual");

        add(setupButton);
        add(repeatButton, "wrap");
        add(new JLabel("Repeat method"));
        add(repeatBox);
        
        setupButton.addActionListener(this);
        repeatButton.addActionListener(this);
    }
    
    public void executeActions(boolean setup)
    {
        DefaultTableModel model =   setup? setupPanel.taskTableModel : repeatPanel.taskTableModel;
        int rowCount            =   model.getRowCount();
        JComboBox comboBox      =   setupPanel.optionsBox;
        
        for(int row = 0; row < rowCount; row++)
        {
            String option   =   model.getValueAt(row, 0).toString();
            int actionIndex =   ((DefaultComboBoxModel) comboBox.getModel()).getIndexOf(option);
            
            if(actionIndex != -1) handleAction(actionIndex);
        }
    }
    
    private void handleAction(int actionIndex)
    {
        PluginLayout middleMan  =   controlPanel.getPluginLayout();
        
        switch(actionIndex)
        {
            case 0: middleMan.getScreenPanel().getGraphPanel().addRecordedGraph(); break;
            case 1: middleMan.getControlPanel().showGeneratorSim(); break;
            case 2: controlPanel.getComputePanel().computeSuicideIntent(); break;
            case 3: middleMan.getControlPanel().resetSim(); break;
            case 4: controlPanel.getSimPanel().executeDelete(); break;
            case 5: controlPanel.getSimPanel().clearDeadObjects(); break;
            case 6: controlPanel.getComputePanel().computeAverageSuicideIntent(); break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        if(src == setupButton)
            JOptionPane.showMessageDialog(null, setupPanel, "Manage setup tasks", JOptionPane.INFORMATION_MESSAGE);
        
        else if(src == repeatButton)
            JOptionPane.showMessageDialog(null, repeatPanel, "Manage repeat tasks", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private class TaskPopupPanel extends JPanel implements ActionListener
    {
        private JButton addButton;
        private JComboBox optionsBox;
        private JTable taskTable;
        private DefaultTableModel taskTableModel;
        
        public TaskPopupPanel()
        {
            setLayout(new BorderLayout());
            setBackground(Consts.PRESET_COL);
            setPreferredSize(new Dimension(300, 270));
            
            addButton       =   new JButton("Add task");
            optionsBox      =   new JComboBox();
            taskTableModel  =   new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int col)
                {
                    return col != 0;
                }
            };
            
            taskTable       =   new JTable(taskTableModel);
            addButton.setIcon(new ImageIcon(controlPanel.getPluginLayout().addIcon));
            
            taskTableModel.addColumn("");
            taskTableModel.addColumn("");
            taskTable.getColumnModel().getColumn(0).setCellRenderer(new TaskLabelCellRenderer());
            
            ButtonColumn btnColumn  =   new ButtonColumn(taskTable, new TaskItemListener(), 1, new ImageIcon(controlPanel.getPluginLayout().removeIcon));
            
            taskTable.getColumnModel().getColumn(0).setCellRenderer(new TaskLabelCellRenderer());
            taskTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            taskTable.getColumnModel().getColumn(1).setPreferredWidth(5);
            taskTable.setBackground(Consts.PRESET_COL);
            
            JPanel tableWrapper =   new JPanel(new BorderLayout());
            JPanel outerWrapper =   new JPanel(new BorderLayout());
            tableWrapper.setBorder(BorderFactory.createTitledBorder("Tasks"));
            
            tableWrapper.add(taskTable);
            outerWrapper.add(tableWrapper);
            
            JPanel topControlsPanel =   new JPanel(new BorderLayout());
            topControlsPanel.add(optionsBox, BorderLayout.CENTER);
            topControlsPanel.add(addButton, BorderLayout.EAST);
            
            initOptions();
            addButton.addActionListener(this);
            
            add(outerWrapper, BorderLayout.CENTER);
            add(topControlsPanel, BorderLayout.NORTH);
        }
        
        private void initOptions()
        {
            for(String option : OPTIONS)
                optionsBox.addItem(option);
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            taskTableModel.addRow(new Object[] { optionsBox.getSelectedItem(), "" });
        }
        
        private class TaskItemListener extends AbstractAction
        {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                int row =   Integer.valueOf(e.getActionCommand());
                taskTableModel.removeRow(row);
            }
        }
        
        private class TaskLabelCellRenderer implements TableCellRenderer
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                JLabel taskLabel    =   new JLabel("" + value);
                taskLabel.setIcon(new ImageIcon(controlPanel.getPluginLayout().executeIcon));
                
                return taskLabel;
            }
        }
    }
    
}
