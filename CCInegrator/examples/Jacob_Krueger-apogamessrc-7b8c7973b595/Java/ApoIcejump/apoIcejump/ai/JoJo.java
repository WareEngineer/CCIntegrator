// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JoJo.java

package apoIcejump.ai;

import java.awt.Color;
import java.util.ArrayList;

// Referenced classes of package apoIcejump.ai:
//            ApoIcejumpAI, ApoIcejumpAILevel, ApoIcejumpAIEntity, ApoIcejumpAIBlock, 
//            ApoIcejumpAIPlayer

public class JoJo extends ApoIcejumpAI
{

    public JoJo()
    {
        status = 4;
        player = null;
        enemy = null;
        blocks = null;
    }

    public String getImage()
    {
        return "player_monkey.png";
    }

    public String getName()
    {
        return "JoJo";
    }

    public String getAuthor()
    {
        return "Johannes Hense";
    }

    public Color getColor()
    {
        return Color.CYAN;
    }

    public void think(ApoIcejumpAILevel level)
    {
        player = level.getPlayer();
        enemy = (ApoIcejumpAIEntity)level.getEnemies().get(0);
        blocks = level.getBlocks();
        reviewSituation();
        if(blocks.size() < 1)
            getToEnemy();
        else
            switch(status)
            {
            case 3: // '\003'
                moveTo(((ApoIcejumpAIBlock)blocks.get(0)).getX(), ((ApoIcejumpAIBlock)blocks.get(0)).getWidth() / 4F);
                break;

            case 1: // '\001'
                escapeFromEnemy();
                break;

            case 2: // '\002'
                getToEnemy();
                break;
            }
    }

    private void getToEnemy()
    {
        moveTo(enemy.getX(), enemy.getWidth() / 4F);
    }

    private void escapeFromEnemy()
    {
        float v = player.getX() > 300F ? -0.16F : 0.16F;
        ApoIcejumpAIBlock block = (ApoIcejumpAIBlock)blocks.get(calcLatestReachableBlock(v));
        moveTo(block.getX(), block.getWidth() / 4F);
    }

    private void moveTo(float x, float d)
    {
        if(x - d > player.getX() + player.getWidth() / 2.0F)
            player.setVecX(0.16F);
        else
        if(x + d < player.getX() + player.getWidth() / 2.0F)
            player.setVecX(-0.16F);
        else
            player.setVecX(0.0F);
    }

    private int calcLatestReachableBlock(float v)
    {
        return 1;
    }

    public void reviewSituation()
    {
        if(enemy.getX() + enemy.getWidth() > player.getX() && enemy.getX() + enemy.getWidth() > player.getX() + player.getWidth())
        {
            if(enemy.getY() > player.getY())
                status = 2;
            else
                status = 1;
        } else
        {
            status = 3;
        }
    }

    private int status;
    private ApoIcejumpAIPlayer player;
    private ApoIcejumpAIEntity enemy;
    private ArrayList blocks;
}
