// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Animals.java

package animals;

import apoSoccer.ApoSoccerConstants;
import apoSoccer.entityForAI.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.apogames.help.ApoHelp;

public class Animals extends ApoSoccerAI
{

    public Animals()
    {
        bLeft = false;
    }

    public String getAuthor()
    {
        return "Willy Wuff";
    }

    public String getTeamName()
    {
        return "The Animals";
    }

    public String getPlayerManImage()
    {
        return "apoSheep.png";
    }

    public String getPlayerFemaleImage()
    {
        return "apoDog.png";
    }

    public String[] getNames()
    {
        String names[] = new String[4];
        names[2] = "The Goalsavedog";
        names[3] = "The Mastersheep";
        names[0] = "The Watchdog";
        names[1] = "The Defenderdog";
        return names;
    }

    public int[] getHair()
    {
        int hair[] = new int[4];
        hair[2] = 0;
        hair[3] = 0;
        hair[0] = 0;
        hair[1] = 0;
        return hair;
    }

    public boolean[] getGender()
    {
        boolean gender[] = new boolean[4];
        gender[2] = true;
        gender[3] = false;
        gender[0] = true;
        gender[1] = true;
        return gender;
    }

    public void think(ApoSoccerBall ball, ArrayList ownTeam, ArrayList enemies)
    {
        bLeft = ((ApoSoccerTeam)ownTeam.get(2)).isLeftTeam();
        if(ApoHelp.getRandomValue(100, 0) > 50)
        {
            if(((ApoSoccerTeam)ownTeam.get(3)).canSpeak())
                ((ApoSoccerTeam)ownTeam.get(3)).speakText(SPEECH.length - 1);
        } else
        if(((ApoSoccerTeam)ownTeam.get(2)).canSpeak())
            ((ApoSoccerTeam)ownTeam.get(2)).speakText((int)(Math.random() * (double)(SPEECH.length - 1)));
        ArrayList goalPoint = getBallInGoal(ball, bLeft);
        java.awt.geom.Point2D.Float orgBall = new java.awt.geom.Point2D.Float(ball.getX(), ball.getY());
        if(bLeft)
            orgBall.x = (float)(orgBall.getX() - 15D);
        else
            orgBall.x = (float)(orgBall.getX() + 15D);
        if(bLeft && ball.getX() >= 450 || !bLeft && ball.getX() <= 450)
            playerMove(orgBall, (ApoSoccerTeam)ownTeam.get(3), 80);
        else
        if(bLeft)
            playerMoveToPoint(ball, FORWARD_LEFT, 50, (ApoSoccerTeam)ownTeam.get(3));
        else
            playerMoveToPoint(ball, FORWARD_RIGHT, 50, (ApoSoccerTeam)ownTeam.get(3));
        if(goalPoint == null)
        {
            if(bLeft)
                playerMoveToPoint(ball, GOALKEEPER_LEFT, 50, (ApoSoccerTeam)ownTeam.get(2));
            else
                playerMoveToPoint(ball, GOALKEEPER_RIGHT, 50, (ApoSoccerTeam)ownTeam.get(2));
        } else
        {
            java.awt.geom.Point2D.Float gPoint = null;
            if(goalPoint.size() > 15)
                gPoint = new java.awt.geom.Point2D.Float(((java.awt.geom.Point2D.Float)goalPoint.get(15)).x, ((java.awt.geom.Point2D.Float)goalPoint.get(15)).y);
            else
                gPoint = new java.awt.geom.Point2D.Float(((java.awt.geom.Point2D.Float)goalPoint.get(0)).x, ((java.awt.geom.Point2D.Float)goalPoint.get(0)).y);
            playerMoveToPoint(ball, gPoint, 50, (ApoSoccerTeam)ownTeam.get(2));
        }
        if(bLeft && ApoSoccerConstants.LEFT_SIDE_REC.intersects(ball.getX(), ball.getY(), ball.getRadius(), ball.getRadius()))
        {
            playerMoveToPoint(ball, new java.awt.geom.Point2D.Float(ball.getX(), ball.getY()), 80, (ApoSoccerTeam)ownTeam.get(0));
            playerMoveToPoint(ball, new java.awt.geom.Point2D.Float(ball.getX(), ball.getY()), 80, (ApoSoccerTeam)ownTeam.get(1));
        } else
        if(!bLeft && ApoSoccerConstants.RIGHT_SIDE_REC.intersects(ball.getX(), ball.getY(), ball.getRadius(), ball.getRadius()))
        {
            playerMoveToPoint(ball, new java.awt.geom.Point2D.Float(ball.getX(), ball.getY()), 80, (ApoSoccerTeam)ownTeam.get(0));
            playerMoveToPoint(ball, new java.awt.geom.Point2D.Float(ball.getX(), ball.getY()), 80, (ApoSoccerTeam)ownTeam.get(1));
        } else
        if(bLeft)
        {
            playerMoveToPoint(ball, DEFENDER_ONE_LEFT, 50, (ApoSoccerTeam)ownTeam.get(0));
            playerMoveToPoint(ball, DEFENDER_TWO_LEFT, 50, (ApoSoccerTeam)ownTeam.get(1));
        } else
        {
            playerMoveToPoint(ball, DEFENDER_ONE_RIGHT, 50, (ApoSoccerTeam)ownTeam.get(0));
            playerMoveToPoint(ball, DEFENDER_TWO_RIGHT, 50, (ApoSoccerTeam)ownTeam.get(1));
        }
        playerShoot((ApoSoccerTeam)ownTeam.get(2), ball, true);
        playerShoot((ApoSoccerTeam)ownTeam.get(0), ball, false);
        playerShoot((ApoSoccerTeam)ownTeam.get(1), ball, false);
        playerShoot((ApoSoccerTeam)ownTeam.get(3), ball, false);
    }

    private boolean playerMoveToPoint(ApoSoccerBall ball, java.awt.geom.Point2D.Float point, int speed, ApoSoccerTeam player)
    {
        playerSight(player, point);
        if(player.isBallInArea() && !player.canShoot())
        {
            playerSight(player, new java.awt.geom.Point2D.Float(450F, 250F));
            player.setSpeed(0);
            return true;
        }
        if(point.x == (float)player.getX() && point.y == (float)player.getY())
        {
            player.setSpeed(0);
            playerSight(player, ball);
        } else
        if(Math.abs(point.x - (float)player.getX()) < 4F && Math.abs(point.y - (float)player.getY()) < 4F)
            player.setSpeed(3);
        else
        if(Math.abs(point.x - (float)player.getX()) < 15F && Math.abs(point.y - (float)player.getY()) < 15F)
            player.setSpeed(10);
        else
            player.setSpeed(speed);
        return true;
    }

    private boolean playerMove(java.awt.geom.Point2D.Float point, ApoSoccerTeam player, int speed)
    {
        playerSight(player, point);
        player.setSpeed(speed);
        return true;
    }

    private boolean playerSight(ApoSoccerTeam player, ApoSoccerBall ball)
    {
        return playerSight(player, new java.awt.geom.Point2D.Float(ball.getX(), ball.getY()));
    }

    private boolean playerSight(ApoSoccerTeam player, java.awt.geom.Point2D.Float point)
    {
        int dif = 360 - player.getLineOfSight();
        int change = player.getAngleToPoint((float)point.getX(), (float)point.getY()) + dif;
        if(change > 360)
            change -= 360;
        if(change > 180)
            player.setLineOfSight(-Math.abs(change));
        else
            player.setLineOfSight(Math.abs(change));
        if(point.distance(player.getX(), player.getY()) < 1.0D)
        {
            player.setLineOfSight(0);
            if(!bLeft)
                player.setLineOfSight(180);
        }
        return true;
    }

    private boolean playerShoot(ApoSoccerTeam ownTeam, ApoSoccerBall ball, boolean bShootAlways)
    {
        return playerShoot(ownTeam, ball, 150, bShootAlways);
    }

    private boolean playerShoot(ApoSoccerTeam ownTeam, ApoSoccerBall ball, int strength, boolean bShootAlways)
    {
        ApoSoccerTeam player = ownTeam;
        if(player.canShoot())
        {
            int los = player.getAngleToPoint(ball.getX(), ball.getY());
            int middle = player.getAngleToPoint(0.0F, 200F, ball.getX(), ball.getY());
            if(player.getY() > 250)
                middle = player.getAngleToPoint(0.0F, 300F, ball.getX(), ball.getY());
            if(bLeft)
            {
                middle = player.getAngleToPoint(900F, 200F, ball.getX(), ball.getY());
                if(player.getY() > 250)
                    middle = player.getAngleToPoint(900F, 300F, ball.getX(), ball.getY());
                if(los > 270 || los < 90)
                {
                    int left;
                    for(left = los - 60; left < 0; left += 360);
                    int right;
                    for(right = los + 60; right >= 360; right -= 360);
                    boolean bMiddle = false;
                    if(left > 270)
                    {
                        if(right < 180)
                        {
                            if(left < middle || right > middle)
                                bMiddle = true;
                        } else
                        if(left < middle && right > middle)
                            bMiddle = true;
                    } else
                    if(left < middle && right > middle)
                        bMiddle = true;
                    if(bMiddle)
                    {
                        player.setShoot(middle, strength);
                        return true;
                    }
                    if(los > 270)
                        player.setShoot(los + 60, strength);
                    else
                        player.setShoot(los - 60, strength);
                } else
                if(bShootAlways)
                    if(los > 180)
                        player.setShoot(los + 60, strength);
                    else
                        player.setShoot(los - 60, strength);
            } else
            if(los > 90 && los < 270)
            {
                if(middle < los + 60 && middle > los - 60)
                {
                    player.setShoot(middle, strength);
                    return true;
                }
                if(los < 180)
                    player.setShoot(los + 60, strength);
                else
                    player.setShoot(los - 60, strength);
            } else
            if(bShootAlways)
                if(los >= 0)
                    player.setShoot(los + 60, strength);
                else
                    player.setShoot(los - 60, strength);
        }
        return true;
    }

    private ArrayList getBallInGoal(ApoSoccerBall ball, boolean bLeft)
    {
        ArrayList lastPoints = new ArrayList();
        int count = 0;
        java.awt.geom.Rectangle2D.Float leftGoal = new java.awt.geom.Rectangle2D.Float(-30F, 125F, 40F, 250F);
        java.awt.geom.Rectangle2D.Float rightGoal = new java.awt.geom.Rectangle2D.Float(890F, 125F, 40F, 250F);
        while(count <= 400) 
        {
            count++;
            if(ball.whereIsTheBallInXSteps(count) != null)
            {
                lastPoints.add(ball.whereIsTheBallInXSteps(count));
                if(lastPoints.size() > 20)
                    lastPoints.remove(0);
                if(leftGoal.intersects(((java.awt.geom.Point2D.Float)lastPoints.get(lastPoints.size() - 1)).getX(), ((java.awt.geom.Point2D.Float)lastPoints.get(lastPoints.size() - 1)).getY(), ball.getRadius(), ball.getRadius()) && bLeft)
                    return lastPoints;
                if(rightGoal.intersects(((java.awt.geom.Point2D.Float)lastPoints.get(lastPoints.size() - 1)).getX(), ((java.awt.geom.Point2D.Float)lastPoints.get(lastPoints.size() - 1)).getY(), ball.getRadius(), ball.getRadius()) && !bLeft)
                    return lastPoints;
            }
        }
        return null;
    }

    private final java.awt.geom.Point2D.Float GOALKEEPER_LEFT = new java.awt.geom.Point2D.Float(6F, 250F);
    private final java.awt.geom.Point2D.Float GOALKEEPER_RIGHT = new java.awt.geom.Point2D.Float(894F, 250F);
    private final java.awt.geom.Point2D.Float FORWARD_LEFT = new java.awt.geom.Point2D.Float(550F, 250F);
    private final java.awt.geom.Point2D.Float FORWARD_RIGHT = new java.awt.geom.Point2D.Float(350F, 250F);
    private final java.awt.geom.Point2D.Float DEFENDER_ONE_LEFT = new java.awt.geom.Point2D.Float(100F, 200F);
    private final java.awt.geom.Point2D.Float DEFENDER_ONE_RIGHT = new java.awt.geom.Point2D.Float(800F, 200F);
    private final java.awt.geom.Point2D.Float DEFENDER_TWO_LEFT = new java.awt.geom.Point2D.Float(100F, 300F);
    private final java.awt.geom.Point2D.Float DEFENDER_TWO_RIGHT = new java.awt.geom.Point2D.Float(800F, 300F);
    private final String SPEECH[] = {
        "Wuff Wuff", "Wau Wau", "Maeeeeeeeeeeeh"
    };
    private boolean bLeft;
}
