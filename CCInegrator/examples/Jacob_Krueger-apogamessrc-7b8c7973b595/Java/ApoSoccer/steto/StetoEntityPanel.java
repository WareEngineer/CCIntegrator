// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoEntityPanel.java

package steto;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.JPanel;

// Referenced classes of package steto:
//            IStetoEntityListener, IStetoEntityPanelListener

public abstract class StetoEntityPanel extends JPanel
    implements IStetoEntityListener
{

    public StetoEntityPanel()
    {
        subscribers = new Vector();
    }

    public final void addStetoEntityPanelListener(IStetoEntityPanelListener listener)
    {
        subscribers.add(listener);
    }

    protected final void onStetoEntityPanelChanged()
    {
        IStetoEntityPanelListener listener;
        for(Iterator iterator = subscribers.iterator(); iterator.hasNext(); listener.stetoEntityPanelChanged())
            listener = (IStetoEntityPanelListener)iterator.next();

    }

    private Vector subscribers;
}
