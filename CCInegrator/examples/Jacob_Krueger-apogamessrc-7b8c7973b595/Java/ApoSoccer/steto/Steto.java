// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Steto.java

package steto;

import apoSoccer.entityForAI.*;
import java.util.*;

// Referenced classes of package steto:
//            StetoFrame, StetoPlayer, StetoBall, StetoEntityCoordinate, 
//            StetoHelp, IStetoListener

public class Steto extends ApoSoccerAI
{

    public Steto()
    {
        subscribers = new Vector();
        players = new Vector();
        frame = new StetoFrame();
    }

    public String getAuthor()
    {
        return "Stefan Topf";
    }

    public String getTeamName()
    {
        return "Die, Loosers";
    }

    public void think(ApoSoccerBall ball, ArrayList ownTeam, ArrayList enemies)
    {
        if(goalkeeper == null)
        {
            goalkeeper = new StetoPlayer((ApoSoccerTeam)ownTeam.get(2));
            addStetoListener(goalkeeper);
            frame.addStetoEntityPanel(goalkeeper.getPanel());
        }
        if(forward == null)
        {
            forward = new StetoPlayer((ApoSoccerTeam)ownTeam.get(3));
            addStetoListener(forward);
            frame.addStetoEntityPanel(forward.getPanel());
            players.add(forward);
        }
        if(defender1 == null)
        {
            defender1 = new StetoPlayer((ApoSoccerTeam)ownTeam.get(0));
            addStetoListener(defender1);
            players.add(defender1);
        }
        if(defender2 == null)
        {
            defender2 = new StetoPlayer((ApoSoccerTeam)ownTeam.get(1));
            addStetoListener(defender2);
            players.add(defender2);
        }
        if(this.ball == null)
        {
            this.ball = new StetoBall(ball);
            addStetoListener(this.ball);
            frame.addStetoEntityPanel(this.ball.getPanel());
        }
        onThink();
        ball2Goal();
        nextPlayerToBall();
    }

    public String[] getNames()
    {
        String names[] = new String[4];
        names[2] = "Der Fliegenfaenger";
        names[3] = "Der Blinde";
        names[0] = "Der schweizer Loecherkaese";
        names[1] = "Der Flascheleerspieler";
        return names;
    }

    private void ball2Goal()
    {
        int distance = -1;
        if(goalkeeper.isLeftTeam() && ball.isAimingLeftGoal() || goalkeeper.isRightTeam() && ball.isAimingRightGoal())
        {
            ArrayList coordinates = ball.getCoordinates();
            StetoEntityCoordinate goalCoordinate = null;
            int i = -1;
            for(i = coordinates.size() - 1; i > 0 && goalCoordinate == null; i--)
            {
                StetoEntityCoordinate tempCoordinate = (StetoEntityCoordinate)coordinates.get(i);
                if(goalkeeper.isLeftTeam() && tempCoordinate.getX() > goalkeeper.getRadius() || goalkeeper.isRightTeam() && tempCoordinate.getX() < 900 - goalkeeper.getRadius() - 3)
                    goalCoordinate = tempCoordinate;
            }

            if(goalCoordinate == null)
                if(coordinates.size() > 0)
                    goalCoordinate = (StetoEntityCoordinate)coordinates.get(0);
                else
                    goalCoordinate = new StetoEntityCoordinate(ball.getX(), ball.getY(), 100, 0);
            distance = goalkeeper.getDistanceToPoint(goalCoordinate.getX(), goalCoordinate.getY());
            if(distance > 0)
                goalkeeper.goTo(goalCoordinate.getX(), goalCoordinate.getY(), 100, true);
            else
                goalkeeper.turnToEntity(ball);
        } else
        if(!goalkeeper.isBallInArea())
        {
            double tempAngle = StetoHelp.getAngle(goalkeeper.isLeftTeam() ? '\0' : 900, 250D, ball.getX(), ball.getY());
            double tempLength1 = (Math.sin(Math.toRadians(tempAngle)) * 200D) / 2D;
            double tempLength2 = (Math.cos(Math.toRadians(tempAngle)) * 200D) / 2D;
            double verh = tempLength1 / (Math.abs(tempLength1) + Math.abs(tempLength2));
            double difY = (verh * 200D) / 2D;
            double difX = Math.cos(Math.toRadians(tempAngle)) * difY;
            if(tempAngle > 180D)
                difX *= -1D;
            int goToX = goalkeeper.isLeftTeam() ? (int)difX : 900 + (int)difX;
            int goToY = 250 + (int)difY;
            goalkeeper.goTo(goToX, goToY, 100, true);
        }
        if(distance == 0)
            goalkeeper.turnToEntity(ball);
        if(goalkeeper.isBallInArea())
        {
            goalkeeper.turnToEntity(ball);
            goalkeeper.setAimSpeed(0);
        }
        if(goalkeeper.canShot())
            goalkeeper.setShoot(goalkeeper.getAngle(), 100);
    }

    private void nextPlayerToBall()
    {
        StetoPlayer nplayer = forward;
        if(defender1.getDistanceToEntity(ball) < nplayer.getDistanceToEntity(ball))
            nplayer = defender1;
        if(defender2.getDistanceToEntity(ball) < nplayer.getDistanceToEntity(ball))
            nplayer = defender2;
        if(ball.getX() == 450 && ball.getSpeed() == 0)
            nplayer = defender1;
        for(Iterator iterator = players.iterator(); iterator.hasNext();)
        {
            StetoPlayer player = (StetoPlayer)iterator.next();
            player.goTo(ball.getX(), ball.getY(), 100, true);
            if(player.isBallInArea())
                player.turnToEntity(ball);
            if(player.canShot() && (player.getX() >= ball.getX()) == player.isRightTeam())
                player.setShoot(player.getAngle(), 100);
        }

    }

    public void addStetoListener(IStetoListener listener)
    {
        subscribers.add(listener);
    }

    private void onThink()
    {
        IStetoListener listener;
        for(Iterator iterator = subscribers.iterator(); iterator.hasNext(); listener.onThink())
            listener = (IStetoListener)iterator.next();

    }

    private StetoFrame frame;
    private Vector subscribers;
    private StetoBall ball;
    private StetoPlayer goalkeeper;
    private StetoPlayer forward;
    private StetoPlayer defender1;
    private StetoPlayer defender2;
    Vector players;
}
