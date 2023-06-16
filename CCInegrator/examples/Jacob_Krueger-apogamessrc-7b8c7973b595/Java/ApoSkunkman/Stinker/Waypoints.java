// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Waypoints.java

package Stinker;

import apoSkunkman.ai.ApoSkunkmanAILevel;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package Stinker:
//            MyPoint

public class Waypoints
{

    public Waypoints()
    {
        isInit = false;
        boollevel = null;
        waypoints = null;
        distances = null;
        pPoints = null;
        minP = null;
        maxP = null;
    }

    public void init(ApoSkunkmanAILevel level)
    {
        pPoints = new ArrayList();
        makeBoolLevel(level.getLevelAsByte());
        minP = new MyPoint(0, 0);
        maxP = new MyPoint(boollevel.length - 1, boollevel[0].length - 1);
        initWayPoints();
        isInit = true;
    }

    public boolean isInit()
    {
        return isInit;
    }

    private void makeBoolLevel(byte bytelevel[][])
    {
        boollevel = new boolean[bytelevel.length][bytelevel[0].length];
        for(int y = 0; y < bytelevel.length; y++)
        {
            for(int x = 0; x < bytelevel[y].length; x++)
                if(bytelevel[y][x] != 1)
                {
                    boollevel[y][x] = true;
                    pPoints.add(new MyPoint(y, x));
                }

        }

    }

    public int getPath(MyPoint from, MyPoint to)
    {
        int dir = waypoints[from.toInt(maxP)][to.toInt(maxP)];
        if(dir == -2)
        {
            dir = calcPath(from, to);
            dir = waypoints[from.toInt(maxP)][to.toInt(maxP)];
        }
        return dir;
    }

    public int getDistance(MyPoint from, MyPoint to)
    {
        int dist = distances[from.toInt(maxP)][to.toInt(maxP)];
        if(dist == 0xf4240)
        {
            calcPath(from, to);
            dist = distances[from.toInt(maxP)][to.toInt(maxP)];
        }
        return dist;
    }

    private void initWayPoints()
    {
        int maxy = boollevel.length;
        int maxx = boollevel[0].length;
        waypoints = new byte[maxy * maxx][maxy * maxx];
        distances = new int[maxy * maxx][maxy * maxx];
        for(int y = 0; y < maxy * maxx; y++)
        {
            for(int x = 0; x < maxy * maxx; x++)
            {
                waypoints[y][x] = (byte)(x != y ? -2 : -1);
                distances[y][x] = x != y ? 0xf4240 : 0;
            }

        }

        for(Iterator iterator = pPoints.iterator(); iterator.hasNext();)
        {
            MyPoint cur = (MyPoint)iterator.next();
            for(int dir = 0; dir < 4; dir++)
            {
                MyPoint next = MyPoint.dirToMyPoint(dir).add(cur);
                if(next.isInRange(minP, maxP) && boollevel[next.y][next.x])
                {
                    waypoints[cur.toInt(maxP)][next.toInt(maxP)] = (byte)dir;
                    distances[cur.toInt(maxP)][next.toInt(maxP)] = 1;
                }
            }

        }

    }

    private byte calcPath(MyPoint from, MyPoint to)
    {
        for(int i = 0; i < pPoints.size() - 1; i++)
        {
            for(Iterator iterator = pPoints.iterator(); iterator.hasNext();)
            {
                MyPoint pfrom = (MyPoint)iterator.next();
                for(Iterator iterator1 = pPoints.iterator(); iterator1.hasNext();)
                {
                    MyPoint pto = (MyPoint)iterator1.next();
                    if(!pfrom.eq(pto))
                    {
                        int dist = distances[pfrom.toInt(maxP)][pto.toInt(maxP)];
                        int dnew = distances[from.toInt(maxP)][pfrom.toInt(maxP)] + dist;
                        if(dnew < distances[from.toInt(maxP)][pto.toInt(maxP)])
                        {
                            distances[from.toInt(maxP)][pto.toInt(maxP)] = dnew;
                            waypoints[from.toInt(maxP)][pto.toInt(maxP)] = waypoints[from.toInt(maxP)][pfrom.toInt(maxP)];
                        }
                    }
                }

            }

        }

        return waypoints[from.toInt(maxP)][to.toInt(maxP)];
    }

    private boolean isInit;
    private boolean boollevel[][];
    private byte waypoints[][];
    private int distances[][];
    private ArrayList pPoints;
    private MyPoint minP;
    private MyPoint maxP;
}
