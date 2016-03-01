///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.app.Consts;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
    
    public SuicideIntentControlPanel()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Suicide intent controls"));
        
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
    
    public void computeSuicideIntent()
    {
        
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object src  =   e.getSource();
        
        if(src == resetButton)
            resetSuicideIntentDisplay();
        
        else if(src == computeButton)
            computeSuicideIntent();
    }
}
