// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Hard.java

package apoIcejump.ai;

import java.awt.*;
import java.util.ArrayList;

// Referenced classes of package apoIcejump.ai:
//            ApoIcejumpAI, ApoIcejumpAILevel, ApoIcejumpAIPlayer, ApoIcejumpAIEnemy, 
//            ApoIcejumpAIBlock

public class Hard extends ApoIcejumpAI
{

    public Hard()
    {
        bOver = true;
        bEnemyNear = true;
        bGoLeft = false;
        bHaveToGo = false;
    }

    public String getAuthor()
    {
        return "Dirk Aporius";
    }

    public String[] getRidicule()
    {
        String ridicule[] = {
            "Apo ist nunmal einfach besser als #enemy", "Gaehn ... ist das alles was du kannst?", "Yipee ya yeah Schweinebacke!", "Hasta la vista, #enemy", "Spring, #enemy, spring", "#enemy, ich bin dein Vater", "Spiels noch einmal, #enemy", "Ich bin der Koenig der Welt", "Mein Eisblock gehoert zu mir, klar?", "Setz dich, nimm dir nen Keks - du #enemy!"
        };
        return ridicule;
    }

    public Color getColor()
    {
        return new Color(255, 255, 255, 128);
    }

    public boolean shouldOwnRender()
    {
        return true;
    }

    public boolean renderPlayer(Graphics2D g)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(255, 255, 255, 128));
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 11, 11);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 11, 11);
        java.awt.Stroke stroke = g.getStroke();
        g.setStroke(new BasicStroke(3F));
        if(bEnemyNear)
        {
            if(!bOver)
            {
                g.drawLine((0 + getWidth() / 2) - 4, (0 + getHeight() / 2) - 5, (0 + getWidth() / 2) - 7, 0 + getHeight() / 2);
                g.drawLine(0 + getWidth() / 2 + 3, (0 + getHeight() / 2) - 5, 0 + getWidth() / 2 + 6, 0 + getHeight() / 2);
            } else
            {
                g.drawLine((0 + getWidth() / 2) - 4, (0 + getHeight() / 2) - 5, (0 + getWidth() / 2) - 4, 0 + getHeight() / 2);
                g.drawLine(0 + getWidth() / 2 + 3, (0 + getHeight() / 2) - 5, 0 + getWidth() / 2 + 3, 0 + getHeight() / 2);
            }
        } else
        {
            g.drawLine((0 + getWidth() / 2) - 7, (0 + getHeight() / 2) - 5, (0 + getWidth() / 2) - 4, 0 + getHeight() / 2);
            g.drawLine(0 + getWidth() / 2 + 6, (0 + getHeight() / 2) - 5, 0 + getWidth() / 2 + 3, 0 + getHeight() / 2);
        }
        g.setStroke(stroke);
        if(bEnemyNear)
        {
            Polygon p = new Polygon();
            if(bOver)
            {
                p.addPoint(getWidth() / 2 - 4, getHeight() / 2 + 5);
                p.addPoint(getWidth() / 2 + 4, getHeight() / 2 + 5);
                p.addPoint(getWidth() / 2, getHeight() / 2 + 10);
            } else
            {
                p.addPoint(getWidth() / 2 - 4, getHeight() / 2 + 10);
                p.addPoint(getWidth() / 2 + 4, getHeight() / 2 + 10);
                p.addPoint(getWidth() / 2, getHeight() / 2 + 5);
            }
            g.setColor(Color.RED);
            g.fillPolygon(p);
            g.setColor(Color.BLACK);
            g.drawPolygon(p);
        } else
        {
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(2.0F));
            g.drawLine(getWidth() / 2 - 4, getHeight() / 2 + 7, getWidth() / 2 + 4, getHeight() / 2 + 7);
            g.setStroke(stroke);
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        return true;
    }

    public String getImage()
    {
        return "player_apo.gif";
    }

    public String getName()
    {
        return "Hard";
    }

    public int getWidth()
    {
        return 30;
    }

    public int getHeight()
    {
        return 30;
    }

    public void think(ApoIcejumpAILevel level)
    {
        if(level.getPlayer().getY() < ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getY())
            bOver = true;
        else
            bOver = false;
        boolean bEnemyOver = false;
        if(level.getPlayer().intersects(((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getX() + 1.0F, 0.0F, ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getWidth() - 2.0F, 480F))
        {
            bEnemyOver = true;
            bEnemyNear = true;
            float difference = (level.getPlayer().getX() + level.getPlayer().getWidth() / 2.0F) - ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getX() - ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getWidth() / 2.0F;
            if(level.getPlayer().getY() + level.getPlayer().getHeight() / 2.0F < ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getY())
            {
                bHaveToGo = false;
                if(difference > 3F)
                    level.getPlayer().setVecX(-0.16F);
                else
                if(difference < -3F)
                    level.getPlayer().setVecX(0.16F);
                else
                    level.getPlayer().setVecX(0.0F);
                return;
            }
            bHaveToGo = true;
        } else
        {
            bEnemyNear = false;
        }
        if(bHaveToGo)
        {
            float difference = (level.getPlayer().getX() + level.getPlayer().getWidth() / 2.0F) - ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getX() - ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getWidth() / 2.0F;
            if(Math.abs(difference) >= 35F)
                bHaveToGo = false;
            else
            if(level.getPlayer().getX() < 40F)
                bGoLeft = false;
            else
            if(level.getPlayer().getX() + level.getPlayer().getWidth() > 600F)
                bGoLeft = true;
            if(bHaveToGo && (double)level.getPlayer().getVecY() < 0.10000000000000001D)
            {
                if(bGoLeft)
                    level.getPlayer().setVecX(-0.16F);
                else
                    level.getPlayer().setVecX(0.16F);
                return;
            }
        }
        if(!bEnemyOver && level.getPlayer().getVecY() < 0.0F)
        {
            if(level.getPlayer().getX() - ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getX() > 3F)
            {
                level.getPlayer().setVecX(-0.16F);
                return;
            }
            if(level.getPlayer().getX() - ((ApoIcejumpAIEnemy)level.getEnemies().get(0)).getX() < -3F)
            {
                level.getPlayer().setVecX(0.16F);
                return;
            }
        }
        if(level.getBlocks().size() > 0)
        {
            float difference = (level.getPlayer().getX() + level.getPlayer().getWidth() / 2.0F) - ((ApoIcejumpAIBlock)level.getBlocks().get(0)).getX() - ((ApoIcejumpAIBlock)level.getBlocks().get(0)).getWidth() / 2.0F;
            if(difference > 3F)
                level.getPlayer().setVecX(-0.16F);
            else
            if(difference < -3F)
                level.getPlayer().setVecX(0.16F);
            else
                level.getPlayer().setVecX(0.0F);
        }
    }

    private final int AWAY = 35;
    private boolean bOver;
    private boolean bEnemyNear;
    private boolean bGoLeft;
    private boolean bHaveToGo;
}
