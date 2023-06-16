// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingMain.java

package apoEditor;

import java.awt.Dimension;
import javax.swing.JFrame;

// Referenced classes of package apoEditor:
//            ApoCheatingGamePanel, ApoCheatingHudPanel

public class ApoCheatingMain extends JFrame
{

    public ApoCheatingMain()
    {
        apoCheatingMain = this;
        setTitle("=== ApoCheatingEditor ===");
        setLayout(null);
        setDefaultCloseOperation(3);
        apoCheatingGamePanel = new ApoCheatingGamePanel();
        apoCheatingGamePanel.setSize(480, 480);
        apoCheatingGamePanel.setLocation(0, 0);
        add(apoCheatingGamePanel);
        apoCheatingHudPanel = new ApoCheatingHudPanel();
        apoCheatingHudPanel.setSize(160, 480);
        apoCheatingHudPanel.setLocation(480, 0);
        add(apoCheatingHudPanel);
        apoCheatingGamePanel.setApoCheatingHudPanel(apoCheatingHudPanel);
        apoCheatingHudPanel.setApoCheatingGamePanel(apoCheatingGamePanel);
        setUndecorated(false);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(646, 512);
    }

    public static void main(String args[])
    {
        new ApoCheatingMain();
    }

    private static final long serialVersionUID = 1L;
    private ApoCheatingGamePanel apoCheatingGamePanel;
    private ApoCheatingHudPanel apoCheatingHudPanel;
    public static ApoCheatingMain apoCheatingMain;
}
