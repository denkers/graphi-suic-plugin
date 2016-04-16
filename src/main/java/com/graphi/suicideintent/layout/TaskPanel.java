
package com.graphi.suicideintent.layout;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class TaskPanel extends JPanel implements ActionListener
{
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
        private JPanel taskListPanel;
        
        public TaskPopupPanel()
        {
            setLayout(new BorderLayout());
            addButton       =   new JButton("Add task");
            optionsBox      =   new JComboBox();
            taskListPanel   =   new JPanel();
            
            
            JPanel topControlsPanel =   new JPanel();
            topControlsPanel.add(optionsBox);
            topControlsPanel.add(addButton);
            taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
            
            optionsBox.addItem("Show Vertex Labels");
            TaskItemPanel itemOne   =    new TaskItemPanel(0);
            taskListPanel.add(itemOne);
            
            add(taskListPanel, BorderLayout.CENTER);
            add(topControlsPanel, BorderLayout.NORTH);
        }
        
        private class TaskItemPanel extends JPanel implements ActionListener
        {
            private JButton removeButton;
            private JLabel taskLabel;
            private int optionIndex;
            
            public TaskItemPanel(int optionIndex)
            {
                setLayout(new BorderLayout());
                this.optionIndex    =   optionIndex;
                removeButton        =   new JButton(new ImageIcon(controlPanel.getPluginLayout().deleteImage));
                taskLabel           =   new JLabel("" + optionsBox.getItemAt(optionIndex));
                taskLabel.setIcon(new ImageIcon(controlPanel.getPluginLayout().executeImage));

                removeButton.addActionListener(this);
                add(taskLabel, BorderLayout.CENTER);
                add(removeButton, BorderLayout.EAST);
            }

            public int getOptionIndex() 
            {
                return optionIndex;
            }
            
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                taskListPanel.remove(this);
                taskListPanel.revalidate();
            }
        }
    }
}
