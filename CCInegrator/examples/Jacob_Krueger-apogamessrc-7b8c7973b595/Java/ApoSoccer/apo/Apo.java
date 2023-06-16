// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Apo.java

package apo;

import apoSoccer.ApoSoccerConstants;
import apoSoccer.entityForAI.ApoSoccerAI;
import apoSoccer.entityForAI.ApoSoccerBall;
import apoSoccer.entityForAI.ApoSoccerEnemy;
import apoSoccer.entityForAI.ApoSoccerTeam;
import apoSoccer.entityForAI.ApoSoccerTeamSet;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Apo extends ApoSoccerAI
{

    public Apo()
    {
        bLeft = false;
        ballStop = 0;
    }

    public String getAuthor()
    {
        return "The incredible Apo";
    }

    public String getTeamName()
    {
        return "Apo's WoMan";
    }

    public String[] getSpeech()
    {
        return SPEECH;
    }

    public void think(ApoSoccerBall ball, ArrayList ownTeam, ArrayList enemies)
    {
        bLeft = ((ApoSoccerTeam)ownTeam.get(2)).isLeftTeam();
        int los = 0;
        if(!bLeft)
            los = 180;
        ApoSoccerTeam forward = (ApoSoccerTeam)ownTeam.get(3);
        if(((ApoSoccerTeam)ownTeam.get(3)).canSpeak())
            ((ApoSoccerTeam)ownTeam.get(3)).speakText((int)(Math.random() * (double)SPEECH.length));
        ArrayList goalPoint = getBallInGoal(ball, bLeft);
        java.awt.geom.Point2D.Float orgBall = ball.whereIsTheBallInXSteps(10);
        if(getDistanceToBall((ApoSoccerEnemy)ownTeam.get(3), ball) < 50)
        {
            orgBall = new java.awt.geom.Point2D.Float(ball.getX(), ball.getY());
            if(bLeft)
                orgBall.x = (float)(orgBall.getX() - 9D);
            else
                orgBall.x = (float)(orgBall.getX() + 9D);
        }
        if(bLeft && ball.getX() >= 450 || !bLeft && ball.getX() <= 450)
            playerMove(orgBall, forward, 80);
        else
        if(bLeft)
            playerMoveToPoint(ball, FORWARD_LEFT, los, 50, forward);
        else
            playerMoveToPoint(ball, FORWARD_RIGHT, los, 50, forward);
        if(goalPoint == null)
        {
            if(bLeft)
            {
                playerMoveToPoint(ball, GOALKEEPER_LEFT, los, 50, (ApoSoccerTeam)ownTeam.get(2));
                playerMoveToPoint(ball, DEFENDER_ONE_LEFT, los, 80, (ApoSoccerTeam)ownTeam.get(0));
                playerMoveToPoint(ball, DEFENDER_TWO_LEFT, los, 80, (ApoSoccerTeam)ownTeam.get(1));
            } else
            {
                playerMoveToPoint(ball, GOALKEEPER_RIGHT, los, 50, (ApoSoccerTeam)ownTeam.get(2));
                playerMoveToPoint(ball, DEFENDER_ONE_RIGHT, los, 80, (ApoSoccerTeam)ownTeam.get(0));
                playerMoveToPoint(ball, DEFENDER_TWO_RIGHT, los, 80, (ApoSoccerTeam)ownTeam.get(1));
            }
        } else
        {
            java.awt.geom.Point2D.Float gPoint = null;
            if(goalPoint.size() >= 20)
                gPoint = new java.awt.geom.Point2D.Float(((java.awt.geom.Point2D.Float)goalPoint.get(15)).x, ((java.awt.geom.Point2D.Float)goalPoint.get(15)).y);
            else
                gPoint = new java.awt.geom.Point2D.Float(((java.awt.geom.Point2D.Float)goalPoint.get(0)).x, ((java.awt.geom.Point2D.Float)goalPoint.get(0)).y);
            playerMoveToPoint(ball, gPoint, ((ApoSoccerTeam)ownTeam.get(2)).getAngleToPoint((float)gPoint.getX(), (float)gPoint.getY()), 100, (ApoSoccerTeam)ownTeam.get(2));
            playerMoveToPoint(ball, gPoint, ((ApoSoccerTeam)ownTeam.get(0)).getAngleToPoint((float)gPoint.getX(), (float)gPoint.getY()), 100, (ApoSoccerTeam)ownTeam.get(0));
            playerMoveToPoint(ball, gPoint, ((ApoSoccerTeam)ownTeam.get(1)).getAngleToPoint((float)gPoint.getX(), (float)gPoint.getY()), 100, (ApoSoccerTeam)ownTeam.get(1));
        }
        if(ball.getSpeed() <= 5)
        {
            if(bLeft && ball.getX() < 450 || !bLeft && ball.getX() > 450)
                ballStop++;
            else
                ballStop = 0;
        } else
        {
            ballStop = 0;
        }
        if(ballStop > 100)
            if(getDistanceToBall((ApoSoccerEnemy)ownTeam.get(0), ball) < getDistanceToBall((ApoSoccerEnemy)ownTeam.get(1), ball))
                playerMove(orgBall, (ApoSoccerTeam)ownTeam.get(0), 70);
            else
                playerMove(orgBall, (ApoSoccerTeam)ownTeam.get(1), 70);
        playerShoot((ApoSoccerTeam)ownTeam.get(2), ball, true);
        playerShoot((ApoSoccerTeam)ownTeam.get(0), ball, 100, true);
        playerShoot((ApoSoccerTeam)ownTeam.get(1), ball, 100, true);
        playerShoot((ApoSoccerTeam)ownTeam.get(3), ball, false);
    }

    private int getDistanceToBall(ApoSoccerEnemy player, ApoSoccerBall ball)
    {
        int distance = 0;
        distance = (ball.getX() - player.getX()) * (ball.getX() - player.getX()) + (ball.getY() - player.getY()) * (ball.getY() - player.getY());
        return (int)Math.sqrt(distance);
    }

    private boolean playerMoveToPoint(ApoSoccerBall ball, java.awt.geom.Point2D.Float point, int lineOfSight, int speed, ApoSoccerTeam player)
    {
        playerSight(player, point);
        if(player.isBallInArea() && !player.canShoot())
        {
            playerSight(player, new java.awt.geom.Point2D.Float(450F, 250F));
            player.setSpeed(0);
            return true;
        }
        if(Math.abs(point.x - (float)player.getX()) < 15F && Math.abs(point.y - (float)player.getY()) < 15F)
        {
            if(Math.abs(point.x - (float)player.getX()) < 2.0F && Math.abs(point.y - (float)player.getY()) < 2.0F)
            {
                player.setSpeed(0);
                playerSight(player, ball);
            } else
            {
                player.setSpeed(8);
            }
        } else
        {
            player.setSpeed(speed);
        }
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
            int angle = 0;
            boolean bShoot = false;
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
                    bShoot = true;
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
                        angle = middle;
                    else
                    if(los > 270)
                        angle = los + 60;
                    else
                        angle = los - 60;
                } else
                if(bShootAlways)
                {
                    bShoot = true;
                    if(los > 180)
                        angle = los + 60;
                    else
                        angle = los - 60;
                }
            } else
            if(los > 90 && los < 270)
            {
                bShoot = true;
                if(middle < los + 60 && middle > los - 60)
                    angle = middle;
                else
                if(los < 180)
                    angle = los + 60;
                else
                    angle = los - 60;
            } else
            if(bShootAlways)
            {
                bShoot = true;
                if(los >= 0)
                    angle = los + 60;
                else
                    angle = los - 60;
            }
            if(bShoot)
                player.setShoot(angle, strength);
        }
        return true;
    }

    private ArrayList getBallInGoal(ApoSoccerBall ball, boolean bLeft)
    {
        ArrayList lastPoints = new ArrayList();
        int count = 0;
        java.awt.geom.Rectangle2D.Float leftGoal = ApoSoccerConstants.LEFT_GOAL_REC;
        java.awt.geom.Rectangle2D.Float rightGoal = ApoSoccerConstants.RIGHT_GOAL_REC;
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

    public String getEmblem()
    {
        return "apoLogo.png";
    }

    public String getPlayerManImage()
    {
        return "player_apo.png";
    }

    public String getPlayerFemaleImage()
    {
        return null;
    }

    public String[] getNames()
    {
        String names[] = new String[4];
        names[2] = "Die Kaeseantje";
        names[3] = "The one and only Apo";
        names[0] = "Die Anneblockerin";
        names[1] = "Die Lindiwand";
        return names;
    }

    public int[] getHair()
    {
        int hair[] = new int[4];
        hair[2] = 17;
        hair[3] = 0;
        hair[0] = 13;
        hair[1] = 19;
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

    public void setPosition(ApoSoccerTeamSet team[])
    {
        ApoSoccerTeamSet p = team[0];
        if(p.isLeftTeam())
            p.setPosition(p.getRadius() + 1, 200, 0);
        else
            p.setPosition(900 - p.getRadius() - 1, 200, 0);
        p = team[1];
        if(p.isLeftTeam())
            p.setPosition(p.getRadius() + 1, 300, 0);
        else
            p.setPosition(900 - p.getRadius() - 1, 300, 0);
    }

    public Color getShirtColor()
    {
        return Color.black;
    }

    public Color getTrouserColor()
    {
        return Color.black;
    }

    private final java.awt.geom.Point2D.Float GOALKEEPER_LEFT = new java.awt.geom.Point2D.Float(15F, 250F);
    private final java.awt.geom.Point2D.Float GOALKEEPER_RIGHT = new java.awt.geom.Point2D.Float(887F, 250F);
    private final java.awt.geom.Point2D.Float FORWARD_LEFT = new java.awt.geom.Point2D.Float(550F, 250F);
    private final java.awt.geom.Point2D.Float FORWARD_RIGHT = new java.awt.geom.Point2D.Float(350F, 250F);
    private final java.awt.geom.Point2D.Float DEFENDER_ONE_LEFT = new java.awt.geom.Point2D.Float(6F, 150F);
    private final java.awt.geom.Point2D.Float DEFENDER_ONE_RIGHT = new java.awt.geom.Point2D.Float(894F, 150F);
    private final java.awt.geom.Point2D.Float DEFENDER_TWO_LEFT = new java.awt.geom.Point2D.Float(6F, 350F);
    private final java.awt.geom.Point2D.Float DEFENDER_TWO_RIGHT = new java.awt.geom.Point2D.Float(894F, 350F);
    private final String SPEECH[] = {
        "We love Apo Soccer", "Apo is so sexy", "Apo is a god", "The Apo's new cloth", "We are Apo's and great!", "Du wirst niedergemaehht!"
    };
    private boolean bLeft;
    private int ballStop;
}
