///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.display.layout.controls.ControlPanel;
import java.awt.Dimension;
import javax.swing.Box;


public class SuicideControlPanel extends ControlPanel
{
    private SuicidePanel suicidePanel; 
    
    public SuicideControlPanel(PluginLayout mainPanel)
    {
        super(mainPanel);
        
        suicidePanel =   new SuicidePanel(mainPanel);
        
        add(Box.createRigidArea(new Dimension(230, 30)));
        add(suicidePanel);
    }
    
    @Override
    protected void initControls()
    {
        super.initControls();
        taskPanel   =   new TaskPanel(this);
    }
    
    public SuicidePanel getSuicidePanel()
    {
        return suicidePanel;
    }
}
