
package com.graphi.suicideintent.layout;

import com.graphi.display.layout.controls.TaskControlPanel;
import net.miginfocom.swing.MigLayout;

public class TaskPanel extends TaskControlPanel
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
    
 /*   private JButton setupButton, repeatButton;
    private SuicideIntentControlPanel controlPanel;
    private JComboBox repeatBox;
    private TaskPopupPanel setupPanel, repeatPanel;
    private JButton runSetupButton, runRepeatButton;
    private JPanel repeatManyPanel;
    private JSpinner repeatCountSpinner; */
    
    private SuicidePanel outer;
    
    public TaskPanel(SuicidePanel outer)
    {
        super(outer.getPluginLayout().getControlPanel());
        setLayout(new MigLayout("fillx"));
        
        this.outer   =   outer;
        /*setupPanel          =   new TaskPopupPanel();
        repeatPanel         =   new TaskPopupPanel();
        setupButton         =   new JButton("Setup tasks");
        repeatButton        =   new JButton("Repeat tasks");
        runSetupButton      =   new JButton("Run setup");
        runRepeatButton     =   new JButton("Run repeat");
        repeatManyPanel     =   new JPanel(new MigLayout("fillx"));
        repeatBox           =   new JComboBox();
        repeatCountSpinner  =   new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        repeatManyPanel.add(new JLabel("Repeat count"));
        repeatManyPanel.add(repeatCountSpinner, "al center");
        
        repeatBox.addItem("Automatic");
        repeatBox.addItem("Manual");

        add(new JLabel("Repeat method"), "al center");
        add(repeatBox, "wrap");
        add(repeatManyPanel, "al center, span 2, wrap");
        add(setupButton, "al center");
        add(repeatButton, "wrap");
        add(runSetupButton, "al center");
        add(runRepeatButton);
        
        setupButton.addActionListener(this);
        repeatButton.addActionListener(this);
        repeatBox.addActionListener(this);
        runSetupButton.addActionListener(this);
        runRepeatButton.addActionListener(this); */
    }
    
    /*public void executeActions(boolean setup, int n)
    {
        DefaultTableModel model =   setup? setupPanel.taskTableModel : repeatPanel.taskTableModel;
        int rowCount            =   model.getRowCount();
        JComboBox comboBox      =   setupPanel.optionsBox;
        
        for(int i = 0; i < n; i++)
        {
            for(int row = 0; row < rowCount; row++)
            {
                String option   =   model.getValueAt(row, 0).toString();
                int actionIndex =   ((DefaultComboBoxModel) comboBox.getModel()).getIndexOf(option);

                if(actionIndex != -1) handleAction(actionIndex);
            }
        }
    }
    
    public void runRepeat()
    {
        boolean repeatMany  =   repeatBox.getSelectedIndex() == 0;
        
        if(repeatMany) 
            executeActions(false, (int) repeatCountSpinner.getValue());
        else 
            executeActions(false, 1);
    }
    
    
    private void handleAction(int actionIndex)
    {
        PluginLayout middleMan  =   controlPanel.getPluginLayout();
        
        switch(actionIndex)
        {
            case 0: middleMan.getScreenPanel().getGraphPanel().addRecordedGraph(); break;
            case 1: middleMan.getControlPanel().getSimulationPanel().showGeneratorSim(); break;
            case 2: controlPanel.getComputePanel().computeSuicideIntent(); break;
            case 3: middleMan.getControlPanel().getSimulationPanel().resetSim(); break;
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
        
        else if(src == repeatBox)
            repeatManyPanel.setVisible(repeatBox.getSelectedIndex() == 0);
        
        else if(src == runSetupButton)
            executeActions(true, 1);
        
        else if(src == runRepeatButton)
            runRepeat();
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
    } */
    
}
