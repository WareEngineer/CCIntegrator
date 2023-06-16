// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MyPoint.java

package Stinker;

import java.awt.Point;

public class MyPoint
{

    public MyPoint(int y, int x)
    {
        this.y = y;
        this.x = x;
    }

    public MyPoint(Point p)
    {
        y = p.y;
        x = p.x;
    }

    public MyPoint(MyPoint mp)
    {
        y = mp.y;
        x = mp.x;
    }

    public MyPoint set(MyPoint other)
    {
        y = other.y;
        x = other.x;
        return this;
    }

    public MyPoint set(int y, int x)
    {
        this.y = y;
        this.x = x;
        return this;
    }

    public MyPoint add(MyPoint other)
    {
        y += other.y;
        x += other.x;
        return this;
    }

    public MyPoint add(int y, int x)
    {
        this.y += y;
        this.x += x;
        return this;
    }

    public MyPoint mul(int factor)
    {
        y *= factor;
        x *= factor;
        return this;
    }

    public boolean isInRange(MyPoint min, MyPoint max)
    {
        if(y < min.y)
            return false;
        if(x < min.x)
            return false;
        if(y > max.y)
            return false;
        return x <= max.x;
    }

    public boolean eq(MyPoint other)
    {
        return eq(other.y, other.x);
    }

    public boolean eq(int y, int x)
    {
        return this.y == y && this.x == x;
    }

    public int toInt(MyPoint scale)
    {
        return (scale.x + 1) * y + x;
    }

    public static MyPoint dirToMyPoint(int dir)
    {
        if(dir % 4 == 0)
            return new MyPoint(1, 0);
        if(dir % 4 == 1)
            return new MyPoint(0, -1);
        if(dir % 4 == 2)
            return new MyPoint(-1, 0);
        if(dir % 4 == 3)
            return new MyPoint(0, 1);
        else
            return null;
    }

    public static byte myPointToDir(MyPoint mp)
    {
        if(mp.eq(1, 0))
            return 0;
        if(mp.eq(0, -1))
            return 1;
        if(mp.eq(-1, 0))
            return 2;
        return ((byte)(!mp.eq(0, 1) ? -1 : 3));
    }

    public int y;
    public int x;
}
