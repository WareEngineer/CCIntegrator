// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoggyStyle.java

package doggyStyle;

import apoSoccer.entityForAI.ApoSoccerAI;
import apoSoccer.entityForAI.ApoSoccerBall;
import java.awt.Color;
import java.util.ArrayList;

public class DoggyStyle extends ApoSoccerAI
{

    public DoggyStyle()
    {
    }

    public String getAuthor()
    {
        return "Der Kegel";
    }

    public boolean[] getGender()
    {
        boolean TeamMemberGender[] = new boolean[4];
        TeamMemberGender[2] = false;
        TeamMemberGender[3] = true;
        TeamMemberGender[0] = true;
        TeamMemberGender[1] = true;
        return TeamMemberGender;
    }

    public int[] getHair()
    {
        int TeamMemberHair[] = new int[4];
        TeamMemberHair[2] = 1;
        TeamMemberHair[3] = 2;
        TeamMemberHair[0] = 3;
        TeamMemberHair[1] = 3;
        return super.getHair();
    }

    public String[] getNames()
    {
        String TeamMemberName[] = new String[4];
        TeamMemberName[2] = "Watchdog";
        TeamMemberName[3] = "Shepard";
        TeamMemberName[0] = "Bulldog";
        TeamMemberName[1] = "Pitbull";
        return TeamMemberName;
    }

    public Color getShirtColor()
    {
        return Color.orange;
    }

    public String getTeamName()
    {
        return "Doggy Style";
    }

    public Color getTrouserColor()
    {
        return Color.magenta;
    }

    public void think(ApoSoccerBall aposoccerball, ArrayList arraylist, ArrayList arraylist1)
    {
    }
}
