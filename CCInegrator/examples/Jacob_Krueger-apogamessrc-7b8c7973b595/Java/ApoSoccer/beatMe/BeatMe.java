// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BeatMe.java

package beatMe;

import apoSoccer.ApoSoccerConstants;
import apoSoccer.entityForAI.*;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class BeatMe extends ApoSoccerAI
{

    public BeatMe()
    {
    }

    public String getAuthor()
    {
        return "Dirk \"Apo\" Aporius";
    }

    public String getTeamName()
    {
        return "BeatMe-Team";
    }

    public void think(ApoSoccerBall ball, ArrayList ownTeam, ArrayList enemyTeam)
    {
        ApoSoccerTeam defender = (ApoSoccerTeam)ownTeam.get(0);
        if(defender.isLeftTeam())
            defender = (ApoSoccerTeam)ownTeam.get(1);
        if(defender.canSpeak())
            defender.speakText((int)(Math.random() * (double)(SPEECH.length - 1)));
        int angleToBall = defender.getAngleToPoint(ball.getX(), ball.getY());
        if(defender.getLineOfSight() != angleToBall)
            defender.setLineOfSight(angleToBall - defender.getLineOfSight());
        playerShoot(defender, ball, true);
        defender.setSpeed(70);
        boolean bLeft = defender.isLeftTeam();
        java.awt.geom.Rectangle2D.Float mySide = bLeft ? ApoSoccerConstants.LEFT_SIDE_REC : ApoSoccerConstants.RIGHT_SIDE_REC;
        defender = (ApoSoccerTeam)ownTeam.get(1);
        if(defender.isLeftTeam())
            defender = (ApoSoccerTeam)ownTeam.get(0);
        angleToBall = defender.getAngleToPoint(ball.getX(), ball.getY());
        if(mySide.intersects(ball.getX() - ball.getRadius(), ball.getY() - ball.getRadius(), 2 * ball.getRadius(), 2 * ball.getRadius()))
        {
            if(defender.getLineOfSight() != angleToBall)
                defender.setLineOfSight(angleToBall - defender.getLineOfSight());
            playerShoot(defender, ball, true);
            defender.setSpeed(100);
        } else
        {
            defender.setLineOfSight(angleDif(defender.getLineOfSight(), defender.getAngleToPoint(450 + (bLeft ? -150 : '\226'), 250F)));
            defender.setSpeed(Math.max(0, (int)(new java.awt.geom.Point2D.Float(defender.getX(), defender.getY())).distance(450 + (bLeft ? -150 : '\226'), 250D)));
        }
        ApoSoccerTeam goalkeeper = (ApoSoccerTeam)ownTeam.get(2);
        boolean bInGoal = false;
        if(bLeft)
        {
            if(ApoSoccerConstants.LEFT_GOAL_REC.contains(ball.whereIsTheBallInXSteps(400).x, ball.whereIsTheBallInXSteps(400).y))
                bInGoal = true;
        } else
        if(ApoSoccerConstants.RIGHT_GOAL_REC.contains(ball.whereIsTheBallInXSteps(400).x, ball.whereIsTheBallInXSteps(400).y))
            bInGoal = true;
        if(bInGoal)
        {
            java.awt.geom.Point2D.Float p = ball.whereIsTheBallInXSteps(400);
            if(bLeft)
                p.x = p.x + 10F;
            else
                p.x = p.x - 10F;
            goalkeeper.setLineOfSight(angleDif(goalkeeper.getLineOfSight(), goalkeeper.getAngleToPoint(p.x, p.y)));
            goalkeeper.setSpeed(Math.max(0, (int)(new java.awt.geom.Point2D.Float(goalkeeper.getX(), goalkeeper.getY())).distance(p.x, p.y)));
        } else
        {
            java.awt.geom.Point2D.Float p = new java.awt.geom.Point2D.Float(20F, 250F);
            if(!bLeft)
                p.x = 880F;
            goalkeeper.setLineOfSight(angleDif(goalkeeper.getLineOfSight(), goalkeeper.getAngleToPoint(p.x, p.y)));
            goalkeeper.setSpeed(Math.max(0, (int)(new java.awt.geom.Point2D.Float(goalkeeper.getX(), goalkeeper.getY())).distance(p.x, p.y)));
        }
        playerShoot(goalkeeper, ball, true);
        bLeft = defender.isLeftTeam();
        mySide = bLeft ? ApoSoccerConstants.LEFT_SIDE_REC : ApoSoccerConstants.RIGHT_SIDE_REC;
        ApoSoccerTeam forward = (ApoSoccerTeam)ownTeam.get(3);
        angleToBall = forward.getAngleToPoint(ball.getX(), ball.getY());
        if(!mySide.intersects(ball.getX() - ball.getRadius(), ball.getY() - ball.getRadius(), 2 * ball.getRadius(), 2 * ball.getRadius()))
        {
            if(forward.getLineOfSight() != angleToBall)
                forward.setLineOfSight(angleToBall - forward.getLineOfSight());
            playerShoot(forward, ball, false);
            forward.setSpeed(100);
        } else
        {
            forward.setLineOfSight(angleDif(forward.getLineOfSight(), forward.getAngleToPoint(450 + (bLeft ? 200 : '\uFF38'), 250F)));
            forward.setSpeed(Math.max(0, (int)(new java.awt.geom.Point2D.Float(forward.getX(), forward.getY())).distance(450 + (bLeft ? 200 : '\uFF38'), 250D)));
        }
    }

    private boolean playerShoot(ApoSoccerTeam ownTeam, ApoSoccerBall ball, boolean bShootAlways)
    {
        ApoSoccerTeam player = ownTeam;
        if(player.canShoot())
        {
            int los = player.getAngleToPoint(ball.getX(), ball.getY());
            int middle = player.getAngleToPoint(0.0F, 250F);
            if(player.isLeftTeam())
            {
                middle = player.getAngleToPoint(900F, 250F);
                if(los > 270 || los < 90)
                {
                    if(middle < los + 60 && middle > los - 60)
                    {
                        player.setShoot(middle, 150);
                        return true;
                    }
                    if(los > 270)
                        player.setShoot(los + 60, 150);
                    else
                        player.setShoot(los - 60, 150);
                } else
                if(bShootAlways)
                    if(los > 180)
                        player.setShoot(los + 60, 150);
                    else
                        player.setShoot(los - 60, 150);
            } else
            if(los > 90 && los < 270)
            {
                if(middle < los + 60 && middle > los - 60)
                {
                    player.setShoot(middle, 150);
                    return true;
                }
                if(los < 180)
                    player.setShoot(los + 60, 150);
                else
                    player.setShoot(los - 60, 150);
            } else
            if(bShootAlways)
                if(los >= 0)
                    player.setShoot(los + 60, 150);
                else
                    player.setShoot(los - 60, 150);
        }
        return true;
    }

    private int angleDif(int from, int to)
    {
        int out = to - from;
        if(out < -180)
            out += 360;
        if(out > 180)
            out -= 360;
        return out;
    }

    public String[] getSpeech()
    {
        return SPEECH;
    }

    public String getEmblem()
    {
        return "logo.png";
    }

    public boolean[] getGender()
    {
        boolean gender[] = new boolean[4];
        gender[2] = false;
        gender[3] = true;
        gender[0] = true;
        gender[1] = true;
        return gender;
    }

    public String[] getNames()
    {
        String names[] = new String[4];
        names[2] = "Torwart";
        names[3] = "Stuermerin";
        names[0] = "Laeuferin";
        names[1] = "Verteidigerin";
        return names;
    }

    public int[] getHair()
    {
        int hair[] = new int[4];
        hair[3] = 23;
        hair[2] = 5;
        hair[0] = 19;
        hair[1] = 19;
        return hair;
    }

    public Color getShirtColor()
    {
        return Color.white;
    }

    public Color getTrouserColor()
    {
        return Color.white;
    }

    private final String SPEECH[] = {
        "Los schlagt mich!", "Macht uns alle.", "Programmieren macht Spass", "BeatMe beat you?", "Tor schiessen macht Spa\337"
    };
}
