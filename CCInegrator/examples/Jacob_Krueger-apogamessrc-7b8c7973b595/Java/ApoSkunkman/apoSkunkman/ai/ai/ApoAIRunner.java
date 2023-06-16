// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoAIRunner.java

package apoSkunkman.ai.ai;

import apoSkunkman.ApoSkunkmanConstants;
import apoSkunkman.ai.*;
import java.awt.Point;
import java.util.ArrayList;

// Referenced classes of package apoSkunkman.ai.ai:
//            ApoAIAStar, ApoAIAstarHelp

public class ApoAIRunner extends ApoSkunkmanAI
{

    public ApoAIRunner()
    {
        oldX = -1;
        oldY = -1;
    }

    public String getAuthor()
    {
        return "Dirk Aporius";
    }

    public String getPlayerName()
    {
        return "Runnerbot";
    }

    public String getImage()
    {
        return "king.png";
    }

    public void load(String path)
    {
        oldX = -1;
        oldY = -1;
        count = 0;
        oldPositions = new ArrayList();
    }

    public void think(ApoSkunkmanAILevel level, ApoSkunkmanAIPlayer player)
    {
        if(oldX == (int)player.getX() && oldY == (int)player.getY())
            this.count++;
        else
            this.count = 0;
        oldX = (int)player.getX();
        oldY = (int)player.getY();
        if(oldPositions.size() <= 0 || ((Point)oldPositions.get(oldPositions.size() - 1)).x != oldX || ((Point)oldPositions.get(oldPositions.size() - 1)).y != oldY)
            oldPositions.add(new Point(oldX, oldY));
        Point start = new Point((int)player.getX(), (int)player.getY());
        Point goal = new Point(level.getGoalXPoint().x, level.getGoalXPoint().y);
        if(level.getGoalXPoint().x < 0)
            if(level.getEnemies() != null && level.getEnemies().length >= 1)
            {
                int playerInt = player.getPlayer() + 1;
                for(int count = 0; count < 5; count++)
                {
                    if(playerInt > ApoSkunkmanConstants.PLAYER_MAX_PLAYER)
                        playerInt = 0;
                    if(playerInt != player.getPlayer())
                    {
                        for(int i = 0; i < level.getEnemies().length; i++)
                        {
                            if(playerInt != level.getEnemies()[i].getPlayer() || !level.getEnemies()[i].isVisible())
                                continue;
                            goal = new Point((int)level.getEnemies()[i].getX(), (int)level.getEnemies()[i].getY());
                            count = 10;
                            break;
                        }

                    }
                    playerInt++;
                }

                if(goal.x < 0)
                    goal = new Point((int)(Math.random() * (double)(level.getLevelAsByte().length - 3) + 1.0D), (int)(Math.random() * (double)(level.getLevelAsByte()[0].length - 3) + 1.0D));
            } else
            {
                goal = new Point(level.getLevelAsByte().length - 2, level.getLevelAsByte()[0].length - 2);
                if(goal.x == start.x && goal.y == start.y)
                    goal = new Point((int)(Math.random() * (double)(level.getLevelAsByte().length - 3) + 1.0D), (int)(Math.random() * (double)(level.getLevelAsByte()[0].length - 3) + 1.0D));
            }
        ApoAIAStar star = new ApoAIAStar();
        ApoAIAstarHelp help = star.getBestWay(level, start, goal);
        if(help != null)
            if(this.count > 1)
            {
                if(player.getCurSkunkmanLay() <= 0)
                    player.laySkunkman();
                this.count = 0;
            } else
            {
                ArrayList bestWay = help.getBestWay();
                if(bestWay.size() > 0)
                    player.movePlayer(Math.abs(((Integer)bestWay.get(0)).intValue()));
                if(help.isBack())
                    this.count = 0;
            }
    }

    private int oldX;
    private int oldY;
    private ArrayList oldPositions;
    private int count;
}
