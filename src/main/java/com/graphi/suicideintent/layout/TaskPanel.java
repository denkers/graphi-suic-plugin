
package com.graphi.suicideintent.layout;

import com.graphi.app.Consts;
import com.graphi.suicideintent.util.ButtonColumn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
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
        
        
    }
    
    private void handleAction(int index)
    {
        
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
    
    private class TaskPopupPanel extends JPanel
    {
        private JButton addButton;
        private JComboBox optionsBox;
        private JTable taskTable;
        private DefaultTableModel taskTableModel;
        
        public TaskPopupPanel()
        {
            setLayout(new BorderLayout());
            setBackground(Consts.PRESET_COL);
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
            
            taskTableModel.addColumn("");
            taskTableModel.addColumn("");
            taskTableModel.addRow(new Object[] { "show vertex labels", "" });
            taskTableModel.addRow(new Object[] { "show vertex labels", "" });
            taskTable.getColumnModel().getColumn(0).setCellRenderer(new TaskLabelCellRenderer());
            
            ButtonColumn btnColumn  =   new ButtonColumn(taskTable, new TaskItemListener(), 1, new ImageIcon(controlPanel.getPluginLayout().deleteImage));
            
            taskTable.getColumnModel().getColumn(0).setCellRenderer(new TaskLabelCellRenderer());
            taskTable.getColumnModel().getColumn(0).setPreferredWidth(120);
            taskTable.getColumnModel().getColumn(1).setPreferredWidth(5);
            taskTable.setBackground(Consts.PRESET_COL);
            
            JPanel tableWrapper =   new JPanel(new BorderLayout());
            JPanel outerWrapper =   new JPanel(new BorderLayout());
            tableWrapper.setBorder(BorderFactory.createTitledBorder("Tasks"));
            
            tableWrapper.add(taskTable);
            outerWrapper.add(tableWrapper);
            
            JPanel topControlsPanel =   new JPanel();
            topControlsPanel.add(optionsBox);
            topControlsPanel.add(addButton);
            
            initOptions();
            
            add(outerWrapper, BorderLayout.CENTER);
            add(topControlsPanel, BorderLayout.NORTH);
        }
        
        private void initOptions()
        {
            for(String option : OPTIONS)
                optionsBox.addItem(option);
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
                taskLabel.setIcon(new ImageIcon(controlPanel.getPluginLayout().executeImage));
                
                return taskLabel;
            }
        }
    }
    
}
