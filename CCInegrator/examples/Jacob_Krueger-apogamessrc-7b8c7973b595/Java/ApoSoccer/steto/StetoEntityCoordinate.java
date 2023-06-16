// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoEntityCoordinate.java

package steto;


public class StetoEntityCoordinate
{

    public StetoEntityCoordinate(int x, int y, int speed, int angle)
    {
        this(x, y, speed, angle, false);
    }

    public StetoEntityCoordinate(int x, int y, int speed, int angle, boolean goal)
    {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
        this.goal = goal;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getAngle()
    {
        return angle;
    }

    public boolean getGoal()
    {
        return goal;
    }

    private int x;
    private int y;
    private int speed;
    private int angle;
    private boolean goal;
}
