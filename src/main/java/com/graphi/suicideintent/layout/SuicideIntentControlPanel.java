///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.app.Consts;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class SuicideIntentControlPanel extends JPanel
{
    private JCheckBox displaySizeCheck, displayColourCheck;
    private JComboBox computeBox;
    
    public SuicideIntentControlPanel()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Suicide intent controls"));
        
        displaySizeCheck    =   new JCheckBox("Display size");
        displayColourCheck  =   new JCheckBox("Display colour");
        computeBox          =   new JComboBox();
        
        computeBox.addItem("All");
        computeBox.addItem("Selected");
        
        JPanel panelWrapper =   new JPanel(new MigLayout("fillx"));
        panelWrapper.add(displaySizeCheck);
        panelWrapper.add(displayColourCheck, "wrap");
        panelWrapper.add(new JLabel("Target options"));
        panelWrapper.add(computeBox);
        panelWrapper.setBackground(Consts.PRESET_COL);
        
        add(panelWrapper);
    }
}
