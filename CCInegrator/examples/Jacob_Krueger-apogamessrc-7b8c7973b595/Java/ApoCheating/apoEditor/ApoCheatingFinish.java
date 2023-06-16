// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingFinish.java

package apoEditor;

import java.awt.*;

// Referenced classes of package apoEditor:
//            ApoCheatingEntity, ApoCheatingPlayer

public class ApoCheatingFinish extends ApoCheatingEntity
{

    public ApoCheatingFinish(int x, int y, int width, int height, int player)
    {
        super(Color.WHITE, x, y, width, height, 2);
        bFinish = false;
        this.player = player;
    }

    protected void init()
    {
        super.init();
        bFinish = false;
    }

    protected boolean isFinish(ApoCheatingPlayer player)
    {
        if(player.getCheated() >= 100D && player.getPlayer() == this.player)
        {
            bFinish = true;
            return (new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight())).intersects(new Rectangle(((int)player.getX() + player.getWidth() / 2) - 3, ((int)player.getY() + player.getHeight() / 2) - 3, 7, 7));
        } else
        {
            return false;
        }
    }

    protected boolean isOnFinish(ApoCheatingPlayer player)
    {
        if(player.getPlayer() == this.player)
            return super.isIn(player);
        else
            return false;
    }

    protected void render(Graphics2D g)
    {
        if(bFinish)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.BLACK);
        java.awt.Composite clone = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(3, 0.35F));
        g.fillOval((int)super.getX() - 10, (int)super.getY() - 10, 20, 20);
        java.awt.Stroke cloneStroke = g.getStroke();
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2.0F, 0, 0));
        g.drawOval((int)super.getX() - 11, (int)super.getY() - 11, 22, 22);
        g.setComposite(clone);
        g.setStroke(cloneStroke);
    }

    protected int getPlayer()
    {
        return player;
    }

    protected void setPlayer(int player)
    {
        this.player = player;
    }

    private boolean bFinish;
    private int player;
}
