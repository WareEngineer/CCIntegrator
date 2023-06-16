// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IcarusAI.java

package test;

import apoMario.ai.*;
import apoMario.entity.*;
import apoMario.level.ApoMarioLevel;
import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class IcarusAI extends ApoMarioAI
{

    public IcarusAI()
    {
    }

    private static Field getField(Class cls, String name)
        throws NoSuchFieldException, SecurityException
    {
        Field field = null;
        while(cls != null) 
            try
            {
                field = cls.getDeclaredField(name);
                break;
            }
            catch(NoSuchFieldException nosuchfieldexception)
            {
                cls = cls.getSuperclass();
            }
        if(field == null)
            throw new NoSuchFieldException();
        else
            return field;
    }

    private static ApoMarioEntity getBaseEntity(ApoMarioAIEntity entity, Class cls)
        throws IllegalArgumentException, IllegalAccessException
    {
        return (ApoMarioEntity)cls.cast(entityField.get(entity));
    }

    private static boolean isPlatformTile(byte tile)
    {
        return tile != 0 && tile != 8;
    }

    public String getTeamName()
    {
        return "Team Icarus";
    }

    public String getAuthor()
    {
        return "Daedalus";
    }

    public void think(ApoMarioAIPlayer player, ApoMarioAILevel level)
    {
        try
        {
            ApoMarioPlayer hacker = (ApoMarioPlayer)getBaseEntity(player, ApoMarioPlayer.class);
            ApoMarioPlayer noob = null;
            try
            {
                noob = (ApoMarioPlayer)getBaseEntity(level.getEnemyPlayer(), ApoMarioPlayer.class);
            }
            catch(NullPointerException ex)
            {
                noob = null;
            }
            ApoMarioLevel sandbox = hacker.getLevel();
            if(noob != null)
            {
                if(Math.random() < 0.0099999997764825821D)
                    noob.setVecX(2.0F * (float)(Math.random() - 0.5D));
                if(sandbox != null && sandbox.getFinish() != null && sandbox.getFinish().getX() - noob.getX() - noob.getWidth() < 48F)
                    noob.setVecX(-2F);
            }
            if(sandbox != null && sandbox.getLevelEntities() != null)
            {
                ApoMarioEntity entityField[][] = sandbox.getLevelEntities();
                for(int y = 0; y < entityField.length; y++)
                {
                    for(int x = 0; x < Math.min(entityField[y].length, (int)(hacker.getX() / 16F) + 6); x++)
                    {
                        ApoMarioEntity entity = entityField[y][x];
                        if((entity instanceof ApoMarioQuestionmark) && !((ApoMarioQuestionmark)entity).isBWall())
                            ((ApoMarioQuestionmark)entity).damageWall(sandbox, hacker.getPlayer());
                    }

                }

            }
            for(Iterator iterator = level.getGoodies().iterator(); iterator.hasNext();)
            {
                ApoMarioAIGoodie goodie = (ApoMarioAIGoodie)iterator.next();
                ApoMarioEnemy rofl = (ApoMarioEnemy)getBaseEntity(goodie, ApoMarioEnemy.class);
                if(sandbox != null && sandbox.getLevelEntities() != null)
                {
                    int x = (int)(rofl.getX() / 16F);
                    int y = (int)(rofl.getY() / 16F);
                    if(y >= 0 && y < sandbox.getLevelEntities().length && x >= 0 && x < sandbox.getLevelEntities()[y].length && sandbox.getLevelEntities()[y][x] == rofl)
                        sandbox.getLevelEntities()[y][x] = null;
                }
                rofl.setY(rofl.getY() + ((hacker.getY() + 0.5F * hacker.getHeight()) - rofl.getY() - rofl.getHeight()) * 0.08F);
                rofl.setX(rofl.getX() + (hacker.getXMiddle() - rofl.getXMiddle()) * 0.08F);
                if(sandbox != null && sandbox.getLevelEntities() != null)
                {
                    int x = (int)(rofl.getX() / 16F);
                    int y = (int)(rofl.getY() / 16F);
                    if(y >= 0 && y < sandbox.getLevelEntities().length && x >= 0 && x < sandbox.getLevelEntities()[y].length && sandbox.getLevelEntities()[y][x] == null)
                        sandbox.getLevelEntities()[y][x] = rofl;
                }
            }

            ArrayList candy = hacker.getFireballs();
            ArrayList candyTargets = new ArrayList(level.getEnemies().size());
            for(Iterator iterator1 = level.getEnemies().iterator(); iterator1.hasNext();)
            {
                ApoMarioAIEnemy enemy = (ApoMarioAIEnemy)iterator1.next();
                ApoMarioEnemy entity = (ApoMarioEnemy)getBaseEntity(enemy, ApoMarioEnemy.class);
                if(!enemy.isFireballType() && !enemy.isImmortalEnemy() && !enemy.isShellType())
                    candyTargets.add(entity);
                else
                if(enemy.isImmortalEnemy() && Math.random() < 0.01D)
                {
                    entity.setBFly(false);
                    entity.die(sandbox, hacker.getPlayer());
                }
            }

            ApoMarioFireball fireball;
            for(; candy.size() >= 1 && candy.size() < 6; candy.add(fireball))
                fireball = new ApoMarioFireball(((ApoMarioFireball)candy.get(0)).getIBackground(), (int)(hacker.getX() + 0.5F * hacker.getWidth()), hacker.getY() + 0.5F * hacker.getHeight(), 4, 170L, true, hacker.getPlayer(), ++ApoMarioLevel.ID);

            int idx = 0;
            for(Iterator iterator2 = candy.iterator(); iterator2.hasNext();)
            {
                ApoMarioEnemy missile = (ApoMarioEnemy)iterator2.next();
                ApoMarioEnemy bestTarget = null;
                float distanceSquare = (1.0F / 0.0F);
                for(Iterator iterator3 = candyTargets.iterator(); iterator3.hasNext();)
                {
                    ApoMarioEnemy target = (ApoMarioEnemy)iterator3.next();
                    float distSq = (float)(Math.pow(missile.getX() - target.getX(), 2D) + Math.pow(missile.getY() - target.getY(), 2D));
                    if(bestTarget == null || distanceSquare > distSq)
                    {
                        bestTarget = target;
                        distanceSquare = distSq;
                    }
                }

                java.awt.geom.Point2D.Float targetPoint;
                if(bestTarget == null)
                {
                    float angle = (float)level.getTimeLeft() / 1000F + 2.0F * ((float)idx / (float)candy.size()) * 3.141593F;
                    float radius = 0.6F * hacker.getHeight();
                    targetPoint = new java.awt.geom.Point2D.Float(hacker.getX() + 0.5F * hacker.getWidth() + (float)((double)radius * Math.cos(angle)) + 100F * hacker.getVecX(), hacker.getY() + 0.5F * hacker.getHeight() + (float)((double)radius * Math.sin(angle)));
                } else
                {
                    targetPoint = new java.awt.geom.Point2D.Float(bestTarget.getX() + 0.5F * bestTarget.getWidth(), bestTarget.getY() + 0.5F * bestTarget.getHeight());
                    candyTargets.remove(bestTarget);
                }
                missile.setVecX((0.15F * (targetPoint.x - missile.getX() - 0.5F * missile.getWidth())) / 16F);
                missile.setVecY((0.15F * (targetPoint.y - missile.getY() - 0.5F * missile.getHeight())) / 16F);
                idx++;
            }

            byte map[][] = level.getLevelArray();
            int highestObstruction = map.length;
            for(int y = 0; y < map.length; y++)
            {
                for(int x = (int)player.getX(); x < map[y].length; x++)
                {
                    if(!isPlatformTile(map[y][x]))
                        continue;
                    highestObstruction = y;
                    break;
                }

                if(highestObstruction == y)
                    break;
            }

            hacker.setBNextJump(true);
            hacker.setVecY(((float)highestObstruction - 3F - player.getY() - player.getHeight()) * 0.02375F);
            hacker.setType(2);
            player.runRight();
            player.runFast();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Field entityField;

    static 
    {
        entityField = null;
        try
        {
            entityField = getField(ApoMarioAIEntity.class, "entity");
            entityField.setAccessible(true);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }
}
