// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoFrame.java

package steto;

import java.awt.GridLayout;
import javax.swing.JFrame;

// Referenced classes of package steto:
//            IStetoEntityPanelListener, StetoEntityPanel

public class StetoFrame extends JFrame
    implements IStetoEntityPanelListener
{

    public StetoFrame()
    {
        setTitle("ToppisToppers");
        setDefaultCloseOperation(0);
        setLayout(new GridLayout(1, 0));
        pack();
    }

    public void addStetoEntityPanel(StetoEntityPanel panel)
    {
        panel.addStetoEntityPanelListener(this);
        add(panel);
        pack();
        repaint();
    }

    public void stetoEntityPanelChanged()
    {
        repaint();
    }
}
