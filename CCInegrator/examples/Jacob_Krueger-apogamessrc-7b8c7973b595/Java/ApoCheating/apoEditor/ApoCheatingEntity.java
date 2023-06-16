// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingEntity.java

package apoEditor;

import java.awt.*;

// Referenced classes of package apoEditor:
//            ApoCheatingPlayer

public class ApoCheatingEntity
{

    public ApoCheatingEntity(Color color, double x, double y, int width, int height, 
            int value)
    {
        this.color = color;
        oldX = x;
        oldY = y;
        this.width = width;
        this.height = height;
        bOldDetect = true;
        this.value = value;
        init();
    }

    public ApoCheatingEntity(Color color, double x, double y, int width, int height, 
            boolean bDetect, int value)
    {
        this.color = color;
        oldX = x;
        oldY = y;
        this.width = width;
        this.height = height;
        bOldDetect = bDetect;
        this.value = value;
        init();
    }

    protected void init()
    {
        x = oldX;
        y = oldY;
        bDetect = bOldDetect;
        bSelect = false;
    }

    protected int getWidth()
    {
        return width;
    }

    protected void setWidth(int width)
    {
        this.width = width;
    }

    protected int getHeight()
    {
        return height;
    }

    protected void setHeight(int height)
    {
        this.height = height;
    }

    public double getX()
    {
        return x;
    }

    protected void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    protected void setY(double y)
    {
        this.y = y;
    }

    public boolean isGoal()
    {
        return value == 3D;
    }

    public boolean isExtra()
    {
        return value == 1.0D;
    }

    public boolean isTeacher()
    {
        return value == 3D && bDetect;
    }

    public boolean isFinish()
    {
        return value == 2D && bDetect;
    }

    public boolean isBDetect()
    {
        return bDetect;
    }

    protected void setBDetect(boolean bDetect)
    {
        this.bDetect = bDetect;
    }

    protected boolean isBSelect()
    {
        return bSelect;
    }

    protected void setBSelect(boolean bSelect)
    {
        this.bSelect = bSelect;
    }

    protected boolean getIn(int x, int y)
    {
        return getX() < (double)x && getX() + (double)getWidth() >= (double)x && getY() < (double)y && getY() + (double)getHeight() >= (double)y;
    }

    protected boolean isIn(ApoCheatingPlayer player)
    {
        return (new Rectangle((int)getX() - 7, (int)getY() - 7, getWidth() + 14, getHeight() + 14)).intersects(new Rectangle((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight()));
    }

    protected boolean isHitting(ApoCheatingEntity entity, int x, int y)
    {
        return (new Rectangle((int)getX() + 4, (int)getY() + 4, getWidth() - 9, getHeight() - 9)).intersects(new Rectangle(x + 4, y + 4, entity.getWidth() - 9, entity.getHeight() - 9));
    }

    protected void action(ApoCheatingPlayer player)
    {
        if(isIn(player) && !isBDetect())
            setBDetect(true);
    }

    protected void render(Graphics2D g)
    {
        if(bDetect)
            g.setColor(color);
        else
            g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int)x, (int)y, width, height);
        g.setColor(Color.BLACK);
        java.awt.Stroke clone = g.getStroke();
        g.setStroke(new BasicStroke(2.0F, 0, 0));
        g.drawOval((int)x - 1, (int)y - 1, width + 2, height + 2);
        g.setStroke(clone);
    }

    private double x;
    private double y;
    private double oldX;
    private double oldY;
    private double value;
    private int width;
    private int height;
    private Color color;
    private boolean bDetect;
    private boolean bOldDetect;
    private boolean bSelect;
}
