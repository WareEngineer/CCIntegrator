// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoEntity.java

package steto;

import java.util.Iterator;
import java.util.Vector;

// Referenced classes of package steto:
//            IStetoListener, IStetoEntityListener, StetoEntityPanel

public abstract class StetoEntity
    implements IStetoListener
{

    public StetoEntity()
    {
        subscribers = new Vector();
    }

    public abstract int getAngle();

    public final int getMyAngle()
    {
        int angle = getAngle();
        if(angle < 0 || angle > 360)
            throw new IllegalArgumentException();
        if(angle <= 90)
            return angle;
        if(angle <= 270)
            return (angle - 180) * -1;
        else
            return angle - 360;
    }

    public final int getAngleToEntity(StetoEntity entity)
    {
        return getAngleToPoint(entity.getX(), entity.getY());
    }

    public final int getAngleToPoint(float x, float y)
    {
        if((float)getX() == x && (float)getY() == y)
            return 0;
        if((float)getX() == x)
            return (float)getY() > y ? 270 : 'Z';
        double result = Math.toDegrees(Math.atan(((float)getY() - y) / ((float)getX() - x)));
        if(x < (float)getX())
            result += 180D;
        for(; result < 0.0D; result += 360D);
        return (int)(result % 360D);
    }

    public final int getTurnAngleToEntity(StetoEntity entity)
    {
        return getTurnAngleToPoint(entity.getX(), entity.getY());
    }

    public final int getTurnAngleToPoint(int x, int y)
    {
        int angle = getAngleToPoint(x, y) - getAngle();
        if(angle > 180)
            return angle - 360;
        if(angle < -180)
            return angle + 360;
        else
            return angle;
    }

    public final int getDistanceToEntity(StetoEntity entity)
    {
        return getDistanceToPoint(entity.getX(), entity.getY());
    }

    public final int getDistanceToPoint(float x, float y)
    {
        return (int)Math.sqrt(Math.pow((float)getX() - x, 2D) + Math.pow((float)getY() - y, 2D));
    }

    public final int getRealDistanceToEntity(StetoEntity entity)
    {
        return getDistanceToEntity(entity) - getRadius() - entity.getRadius();
    }

    public abstract int getRadius();

    public abstract int getSpeed();

    public abstract int getX();

    public abstract int getY();

    public final void addStetoEntityListener(IStetoEntityListener listener)
    {
        subscribers.add(listener);
    }

    protected final void onStetoEntityChanged()
    {
        IStetoEntityListener listener;
        for(Iterator iterator = subscribers.iterator(); iterator.hasNext(); listener.stetoEntityChanged(this))
            listener = (IStetoEntityListener)iterator.next();

    }

    public void onThink()
    {
        onStetoEntityChanged();
    }

    public final boolean isInLeftPenaltyArea()
    {
        return getX() <= 70 && getY() >= 115 && getY() <= 365;
    }

    public final boolean isInRightPenaltyArea()
    {
        return getX() >= 410 && getY() >= 115 && getY() <= 365;
    }

    public abstract StetoEntityPanel getPanel();

    private Vector subscribers;
}
