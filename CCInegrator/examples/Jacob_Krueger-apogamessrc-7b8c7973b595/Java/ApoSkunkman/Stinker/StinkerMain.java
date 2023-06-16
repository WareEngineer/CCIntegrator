// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StinkerMain.java

package Stinker;

import apoSkunkman.ai.*;
import java.io.PrintStream;
import java.util.Random;

// Referenced classes of package Stinker:
//            MyPoint, Waypoints

public class StinkerMain extends ApoSkunkmanAI
{

    public String getPlayerName()
    {
        return "Stinker";
    }

    public String getAuthor()
    {
        return "EnRico Ebert";
    }

    public StinkerMain()
    {
        r = new Random(System.nanoTime());
    }

    public void load(String path)
    {
        direction = -1;
        waypoints = new Waypoints();
        target = new MyPoint(-1, -1);
    }

    public void save(String s)
    {
    }

    private boolean check(byte lev[][], MyPoint p, int dir, int dist, byte cnst)
    {
        MyPoint np = MyPoint.dirToMyPoint(dir).mul(dist).add(p);
        return lev[np.y][np.x] == cnst;
    }

    public void think(ApoSkunkmanAILevel level, ApoSkunkmanAIPlayer player)
    {
        MyPoint xp = new MyPoint(level.getGoalXPoint());
        if(!waypoints.isInit())
            waypoints.init(level);
        float x = player.getX();
        float y = player.getY();
        MyPoint p = new MyPoint((int)y, (int)x);
        if(target.eq(-1, -1) || p.eq(target))
            if(!xp.eq(-1, -1))
                target.set(xp);
            else
                do
                    target.set(r.nextInt(maxP.y - 1) + 1, r.nextInt(maxP.x - 1) + 1);
                while(level.getLevelAsByte()[target.y][target.x] == 1);
        direction = waypoints.getPath(p, target);
        if(direction == 0)
            player.movePlayerDown();
        else
        if(direction == 1)
            player.movePlayerLeft();
        else
        if(direction == 2)
            player.movePlayerUp();
        else
        if(direction == 3)
            player.movePlayerRight();
        else
            System.out.println((new StringBuilder("Hier stimmt was nicht mit den Richtungen :) ")).append(direction).toString());
    }

    private final MyPoint minP = new MyPoint(0, 0);
    private final MyPoint maxP = new MyPoint(14, 14);
    Random r;
    private int direction;
    private Waypoints waypoints;
    private MyPoint target;
}
