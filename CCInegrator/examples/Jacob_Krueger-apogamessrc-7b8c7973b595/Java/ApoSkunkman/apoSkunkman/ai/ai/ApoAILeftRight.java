// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoAILeftRight.java

package apoSkunkman.ai.ai;

import apoSkunkman.ai.*;
import java.awt.Point;

public class ApoAILeftRight extends ApoSkunkmanAI
{

    public ApoAILeftRight()
    {
    }

    public String getAuthor()
    {
        return "Dirk Aporius";
    }

    public String getPlayerName()
    {
        return "LeftRightBot";
    }

    public void think(ApoSkunkmanAILevel level, ApoSkunkmanAIPlayer player)
    {
        level.getGoalXPoint().x = 2;
        level.getGoalXPoint().y = 2;
        if(player.getX() - 1.0F >= 0.0F)
        {
            int tile = level.getLevelAsByte()[(int)player.getY()][(int)(player.getX() - 1.0F)];
            if(tile == 0)
                player.movePlayerLeft();
        }
        if(player.getX() + 1.0F < (float)level.getLevelAsByte()[0].length)
        {
            int tile = level.getLevelAsByte()[(int)player.getY()][(int)(player.getX() + 1.0F)];
            if(tile == 0)
                player.movePlayerRight();
        }
    }
}
