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
import javax.swing.JScrollPane;

public class PluginLayout extends MainPanel
{
    public PluginLayout(AppManager appManager) 
    {
        super(appManager);
        
        data.setEdgeFactory(new SuicideEdgeFactory());
        data.setNodeFactory(new SuicideNodeFactory());
    }
    
    @Override
    protected void initComponents()
    {
        super.initComponents();
        controlPanel    =   new SuicideControlPanel(this);
        controlScroll   =   new JScrollPane(controlPanel);
    }
}
