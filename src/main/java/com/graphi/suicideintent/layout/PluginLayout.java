///================================================
//  Kyle Russell
//  AUT University 2015
//  https://github.com/denkers/graphi-suic-plugin
//================================================

package com.graphi.suicideintent.layout;

import com.graphi.app.AppManager;
import com.graphi.app.Consts;
import com.graphi.display.layout.MainPanel;
import com.graphi.suicideintent.util.SuicideEdgeFactory;
import com.graphi.suicideintent.util.SuicideNodeFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class PluginLayout extends MainPanel
{
    SuicideIntentControlPanel suicideControlPanel;
    
    protected BufferedImage deleteImage, executeImage;
    
    public PluginLayout(AppManager appManager) 
    {
        super(appManager);
        suicideControlPanel =   new SuicideIntentControlPanel(this);
        controlPanel.add(suicideControlPanel);
        
        data.setEdgeFactory(new SuicideEdgeFactory());
        data.setNodeFactory(new SuicideNodeFactory());
    }
    
    @Override
    public void initResources()
    {
        super.initResources();
        
        try
        {
            deleteImage     =   ImageIO.read(new File(Consts.IMG_DIR + "removeSmallIcon.png"));
            executeImage    =   ImageIO.read(new File(Consts.IMG_DIR + "execute.png"));
        }
        
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "[Suicide-Plugin] Failed to load plugin resources");
        }
    }
}
