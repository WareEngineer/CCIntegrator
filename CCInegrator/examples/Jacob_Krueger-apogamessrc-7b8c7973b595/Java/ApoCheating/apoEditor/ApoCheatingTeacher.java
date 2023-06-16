// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingTeacher.java

package apoEditor;

import java.awt.*;
import java.util.ArrayList;

// Referenced classes of package apoEditor:
//            ApoCheatingEntity, ApoCheatingTeacherView, ApoCheatingTeacherWay, ApoCheatingPlayer

public class ApoCheatingTeacher extends ApoCheatingEntity
{

    public ApoCheatingTeacher(int x, int y, int width, int height)
    {
        super(Color.RED, x, y, width, height, 4);
        oldPlus = 5D;
        init(x, y, width, height, oldPlus);
    }

    public ApoCheatingTeacher(int x, int y, int width, int height, double plus)
    {
        super(Color.RED, x, y, width, height, 4);
        oldPlus = plus;
        init(x, y, width, height, oldPlus);
    }

    protected void init()
    {
        super.init();
        init((int)getX(), (int)getY(), getWidth(), getHeight(), oldPlus);
    }

    private void init(int x, int y, int width, int height, double plus)
    {
        cheatColor = Color.GREEN;
        bCoin = false;
        currentWay = 0;
        currentView = 0;
        currentCoinWay = 0;
        view = new ArrayList();
        for(int i = 0; i < view.size(); i++)
            ((ApoCheatingTeacherView)view.get(i)).init(this);

        way = new ArrayList();
        for(int i = 0; i < way.size(); i++)
            ((ApoCheatingTeacherWay)way.get(i)).init();

        this.plus = plus;
    }

    protected boolean isVisible(ApoCheatingPlayer player)
    {
        if(super.isIn(player))
        {
            player.setDetected(100D);
            cheatColor = Color.RED;
            player.setCheatColor(cheatColor);
            return true;
        }
        Polygon poly;
        if(!bCoin)
            poly = ((ApoCheatingTeacherView)view.get(currentView)).getPolygon(this);
        else
            poly = ((ApoCheatingTeacherView)coinView.get(0)).getPolygon(this);
        boolean bCut = poly.intersects(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        if(bCut && player.getDetected() < 100D && !player.isBFinish())
        {
            int red = cheatColor.getRed() + (int)plus;
            if(red > 255)
                red = 255;
            int blue = cheatColor.getBlue();
            int green = cheatColor.getGreen() - (int)plus;
            if(green < 0)
                green = 0;
            player.setDetected((plus * 100D) / 255D + player.getDetected());
            cheatColor = new Color(red, green, blue);
            player.setCheatColor(cheatColor);
        }
        return bCut;
    }

    protected void think()
    {
        if(bCoin)
        {
            ((ApoCheatingTeacherView)coinView.get(0)).next(this);
            if(!((ApoCheatingTeacherWay)coinWay.get(currentCoinWay)).next(this))
            {
                currentCoinWay++;
                if(currentCoinWay >= coinWay.size())
                {
                    bCoin = false;
                    return;
                }
            }
        } else
        {
            if(!((ApoCheatingTeacherWay)way.get(currentWay)).next(this))
            {
                currentWay++;
                if(currentWay >= way.size())
                    currentWay = 0;
            }
            if(!((ApoCheatingTeacherView)view.get(currentView)).next(this))
            {
                currentView++;
                if(currentView >= view.size())
                    currentView = 0;
                ((ApoCheatingTeacherView)view.get(currentView)).makeView(this);
            }
        }
    }

    protected Color getCheatColor()
    {
        return cheatColor;
    }

    protected boolean isCoinHearing(double x, double y)
    {
        java.awt.geom.Rectangle2D.Double r = new java.awt.geom.Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
        java.awt.geom.Rectangle2D.Double s = new java.awt.geom.Rectangle2D.Double(x - 150D, y - 150D, 300D, 300D);
        return s.intersects(r);
    }

    protected ArrayList getView()
    {
        return view;
    }

    protected void deleteView(int i)
    {
        if(i < 0 || i >= view.size())
        {
            return;
        } else
        {
            view.remove(i);
            return;
        }
    }

    protected void addView(int i)
    {
        if(i < 0 || i > view.size() && view.size() != 0)
            return;
        if(view.size() == 0)
            i = 0;
        view.add(i, new ApoCheatingTeacherView(50, 270, 50D, 130D, 1.0D, 1));
        ((ApoCheatingTeacherView)view.get(i)).init(this);
    }

    protected ApoCheatingTeacherView getView(int i)
    {
        if(i < 0 || i >= view.size())
            return null;
        else
            return (ApoCheatingTeacherView)view.get(i);
    }

    protected void setView(ArrayList view)
    {
        this.view = view;
    }

    protected void deleteWay(int i)
    {
        if(i < 0 || i >= way.size())
        {
            return;
        } else
        {
            way.remove(i);
            return;
        }
    }

    protected void addWay(int i)
    {
        if(i < 0 || i > way.size() && way.size() != 0)
            return;
        if(way.size() == 0)
            i = 0;
        if(way.size() == 0)
            way.add(i, new ApoCheatingTeacherWay((int)getX(), (int)getY(), 200, 200, 1.5D, 100));
        else
            way.add(i, new ApoCheatingTeacherWay(((ApoCheatingTeacherWay)way.get(way.size() - 1)).getFinishX(), ((ApoCheatingTeacherWay)way.get(way.size() - 1)).getFinishY(), 200, 200, 1.5D, 100));
    }

    protected ArrayList getWay()
    {
        return way;
    }

    protected ApoCheatingTeacherWay getWay(int i)
    {
        if(i < 0 || i >= way.size())
            return null;
        else
            return (ApoCheatingTeacherWay)way.get(i);
    }

    protected void setWay(ArrayList way)
    {
        this.way = way;
    }

    protected void coinHearAndMakeViewAndWay(ArrayList points)
    {
        coinView = new ArrayList();
        coinWay = new ArrayList();
        currentCoinWay = 0;
        int currentX = (int)(getX() + (double)(getWidth() / 2) + 16D);
        int currentY = (int)(getY() + (double)(getHeight() / 2) + 16D);
        double realX = getX();
        double realY = getY();
        double finishX = ((Point)points.get(points.size() - 1)).getX() * 32D + 16D;
        finishX = finishX * 32D + 16D;
        double finishY = ((Point)points.get(points.size() - 1)).getX() * 32D + 16D;
        finishY = finishY * 32D + 16D;
        int startDir = 0;
        int endDir = 0;
        int middleDir = 0;
        double way = Math.sqrt(Math.pow(Math.abs(currentX - ((Point)points.get(0)).x * 32), 2D) * Math.pow(Math.abs(currentY - ((Point)points.get(0)).y * 32), 2D));
        currentX = ((Point)points.get(0)).x * 32;
        currentY = ((Point)points.get(0)).y * 32;
        middleDir = (int)Math.toDegrees(Math.acos(((double)currentX - (getX() + (double)(getWidth() / 2) + 16D)) / way));
        startDir = middleDir - 60;
        if(startDir < 0)
            startDir += 360;
        endDir = middleDir + 60;
        if(endDir >= 360)
            endDir -= 360;
        coinView.add(new ApoCheatingTeacherView(50, 270, startDir, endDir, 2D, 0x186a0));
        ((ApoCheatingTeacherView)coinView.get(0)).makeView(this);
        for(int i = 1; i < points.size(); i++)
        {
            int ticks = -1;
            if(i == 1)
                ticks = 180;
            coinWay.add(0, new ApoCheatingTeacherWay(((Point)points.get(i)).x * 32, ((Point)points.get(i)).y * 32, currentX, currentY, 1.6000000000000001D, ticks));
            coinWay.add(coinWay.size(), new ApoCheatingTeacherWay(currentX, currentY, ((Point)points.get(i)).x * 32, ((Point)points.get(i)).y * 32, 1.6000000000000001D, -1));
            currentX = ((Point)points.get(i)).x * 32;
            currentY = ((Point)points.get(i)).y * 32;
        }

        realX = getX();
        realY = getY();
        finishX = ((ApoCheatingTeacherWay)coinWay.get(0)).getStartX();
        finishY = ((ApoCheatingTeacherWay)coinWay.get(0)).getStartY();
        coinWay.add(0, new ApoCheatingTeacherWay((int)realX, (int)realY, (int)finishX, (int)finishY, 1.6000000000000001D, -1));
        coinWay.add(coinWay.size(), new ApoCheatingTeacherWay((int)finishX, (int)finishY, (int)realX, (int)realY, 1.6000000000000001D, -1));
        bCoin = true;
    }

    protected void render(Graphics2D g)
    {
        java.awt.Composite clone = g.getComposite();
        g.setColor(cheatColor);
        g.setComposite(AlphaComposite.getInstance(3, 0.4F));
        if(!bCoin && view.size() > 0)
            g.fillPolygon(((ApoCheatingTeacherView)view.get(currentView)).getPolygon(this));
        else
        if(bCoin)
            g.fillPolygon(((ApoCheatingTeacherView)coinView.get(0)).getPolygon(this));
        g.setComposite(clone);
        super.render(g);
    }

    private Color cheatColor;
    private ArrayList way;
    private ArrayList coinWay;
    private ArrayList view;
    private ArrayList coinView;
    private int currentWay;
    private int currentView;
    private int currentCoinWay;
    private double plus;
    private double oldPlus;
    private boolean bCoin;
}
