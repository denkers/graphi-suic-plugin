///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.app.AppManager;
import com.graphi.display.layout.MainPanel;
import com.graphi.suicideintent.util.SuicideEdgeFactory;
import com.graphi.suicideintent.util.SuicideNodeFactory;
import java.awt.Dimension;
import javax.swing.Box;

public class PluginLayout extends MainPanel
{
    SuicidePanel suicideControlPanel;
    
    public PluginLayout(AppManager appManager) 
    {
        super(appManager);
        suicideControlPanel =   new SuicidePanel(this);
        
        controlPanel.add(Box.createRigidArea(new Dimension(230, 30)));
        controlPanel.add(suicideControlPanel);
        
        data.setEdgeFactory(new SuicideEdgeFactory());
        data.setNodeFactory(new SuicideNodeFactory());
    }
    
    @Override
    protected void initComponents()
    {
        
    }
}
