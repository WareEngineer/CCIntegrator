// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StetoHelp.java

package steto;


public class StetoHelp
{

    public StetoHelp()
    {
    }

    public static boolean canPlayerStop(double speed, double distance)
    {
        return distance >= getDistanceToStop(speed);
    }

    public static double getAngle(double x1, double y1, double x2, double y2)
    {
        if(x1 == x2 && y1 == y2)
            return 0.0D;
        if(x1 == x2)
            return (double)(y1 > y2 ? 270 : 'Z');
        double result = Math.toDegrees(Math.atan((y1 - y2) / (x1 - x2)));
        if(x2 < x1)
            result += 180D;
        for(; result < 0.0D; result += 360D);
        return result % 360D;
    }

    public static double getDistanceToStop(double speed)
    {
        double distance;
        for(distance = 0.0D; (speed * 4.5D) / 100D > 0.0099999997764825821D; distance += (speed * 4.5D) / 100D)
            speed += -4D;

        return distance;
    }

    public static double getDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow(x2 - x1, 2D) + Math.pow(y2 - y1, 2D));
    }

    public static double getMyAngle(double angle)
    {
        if(angle < 0.0D || angle > 360D)
            throw new IllegalArgumentException();
        if(angle <= 90D)
            return angle;
        if(angle <= 270D)
            return (angle - 180D) * -1D;
        else
            return angle - 360D;
    }

    public static double[] getPoint(double x, double y, double distance, double angle)
    {
        double result[] = new double[2];
        result[0] = Math.sin(Math.toRadians(angle)) * distance;
        result[1] = Math.cos(Math.toRadians(angle)) * distance;
        return result;
    }
}
