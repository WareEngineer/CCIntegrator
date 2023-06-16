// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Middle.java

package apoIcejump.ai;

import java.awt.Color;
import java.util.ArrayList;

// Referenced classes of package apoIcejump.ai:
//            ApoIcejumpAI, ApoIcejumpAILevel, ApoIcejumpAIPlayer, ApoIcejumpAIEnemy, 
//            ApoIcejumpAIBlock

public class Middle extends ApoIcejumpAI
{

    public Middle()
    {
    }

    public String getAuthor()
    {
        return "Dirk Aporius";
    }

    public Color getColor()
    {
        return new Color(120, 120, 120);
    }

    public String getImage()
    {
        return "player_snowman.png";
    }

    public String getName()
    {
        return "Middle";
    }

    public void think(ApoIcejumpAILevel level)
    {
        if(level.getPlayer().getVecY() < 0.0F)
        {
            if(level.getPlayer().getX() - ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getX() > 4F)
            {
                level.getPlayer().setVecX(-0.12F);
                return;
            }
            if(level.getPlayer().getX() - ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getX() < -4F)
            {
                level.getPlayer().setVecX(0.12F);
                return;
            }
        }
        if(level.getBlocks().size() > 0)
        {
            float difference = (level.getPlayer().getX() + level.getPlayer().getWidth() / 2.0F) - ((ApoIcejumpAIBlock)level.getBlocks().get(0)).getX() - ((ApoIcejumpAIBlock)level.getBlocks().get(0)).getWidth() / 2.0F;
            if(difference > 4F)
                level.getPlayer().setVecX(-0.12F);
            else
            if(difference < -4F)
                level.getPlayer().setVecX(0.12F);
            else
                level.getPlayer().setVecX(0.0F);
        }
    }
}
