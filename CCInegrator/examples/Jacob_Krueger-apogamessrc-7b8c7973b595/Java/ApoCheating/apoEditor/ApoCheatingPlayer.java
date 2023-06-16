// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingPlayer.java

package apoEditor;

import java.awt.Color;
import java.awt.Graphics2D;

// Referenced classes of package apoEditor:
//            ApoCheatingEntity

public class ApoCheatingPlayer extends ApoCheatingEntity
{

    public ApoCheatingPlayer(int x, int y, int width, int height, int player)
    {
        super(Color.BLUE, x, y, width, height, 0);
        getClass();
        throwCoinTicks = (int)(128D / 2D);
        detect = 0.0D;
        this.player = player;
        oldCoins = 0;
        init();
    }

    public ApoCheatingPlayer(int x, int y, int width, int height, int player, int coins)
    {
        super(Color.BLUE, x, y, width, height, 0);
        getClass();
        throwCoinTicks = (int)(128D / 2D);
        detect = 0.0D;
        this.player = player;
        oldCoins = coins;
        init();
    }

    protected void init()
    {
        super.init();
        oldLeftDirection = 0;
        oldUpDirection = 0;
        leftDirection = 0;
        upDirection = 0;
        coinLeftDirection = 0;
        coinUpDirection = 0;
        detect = 0.0D;
        coinX = 0.0D;
        coinY = 0.0D;
        currentCoinTicks = 0;
        percentColor = Color.RED;
        cheat = 0.0D;
        cheatColor = Color.GREEN;
        coins = oldCoins;
        bFinish = false;
        bCoin = false;
        bCoinBounce = false;
    }

    public void setDirection(int up, int left)
    {
        upDirection = up;
        leftDirection = left;
    }

    public void setAction(boolean bAction)
    {
        this.bAction = bAction;
    }

    protected int getPlayer()
    {
        return player;
    }

    protected void setPlayer(int player)
    {
        this.player = player;
    }

    protected boolean isBAction()
    {
        return bAction;
    }

    protected void setBAction(boolean bAction)
    {
        this.bAction = bAction;
    }

    protected double getSpeed()
    {
        getClass();
        return 2D;
    }

    protected int getLeftDirection()
    {
        return leftDirection;
    }

    protected void setLeftDirection(int leftDirection)
    {
        this.leftDirection = leftDirection;
    }

    protected int getUpDirection()
    {
        return upDirection;
    }

    protected void setUpDirection(int upDirection)
    {
        this.upDirection = upDirection;
    }

    public double getDetected()
    {
        return detect;
    }

    protected void setDetected(double percent)
    {
        if(percent > 100D)
            percent = 100D;
        detect = percent;
    }

    protected Color getPercentColor()
    {
        return percentColor;
    }

    protected void setPercentColor(Color percentColor)
    {
        this.percentColor = percentColor;
    }

    public double getCheated()
    {
        return cheat;
    }

    protected void setCheat(double cheat)
    {
        if(cheat > 100D)
            cheat = 100D;
        this.cheat = cheat;
    }

    protected Color getCheatColor()
    {
        return cheatColor;
    }

    protected void setCheatColor(Color cheatColor)
    {
        this.cheatColor = cheatColor;
    }

    protected int getCoins()
    {
        return coins;
    }

    protected boolean isBCoin()
    {
        return bCoin;
    }

    protected void setBCoin(boolean bCoin)
    {
        this.bCoin = bCoin;
    }

    protected boolean isBCoinBounce()
    {
        if(bCoinBounce)
        {
            bCoinBounce = false;
            return true;
        } else
        {
            return bCoinBounce;
        }
    }

    protected boolean isBFinish()
    {
        return bFinish;
    }

    protected void setBFinish(boolean bFinish)
    {
        this.bFinish = bFinish;
    }

    protected void setNewXAndY()
    {
        getClass();
        if(upDirection == 0)
        {
            getClass();
            if(leftDirection == 0)
                return;
        }
        oldUpDirection = upDirection;
        oldLeftDirection = leftDirection;
        getClass();
        if(leftDirection == -1)
        {
            getClass();
            setX(getX() + (double)leftDirection * 2D);
        }
        getClass();
        if(leftDirection == 1)
        {
            getClass();
            setX(getX() + (double)leftDirection * 2D);
        }
        getClass();
        if(upDirection == -1)
        {
            getClass();
            setY(getY() + (double)upDirection * 2D);
        }
        getClass();
        if(upDirection == 1)
        {
            getClass();
            setY(getY() + (double)upDirection * 2D);
        }
    }

    public void throwCoin()
    {
        if(!bCoin && coins > 0)
        {
            coins--;
            currentCoinTicks = 0;
            bCoin = true;
            coinLeftDirection = oldLeftDirection;
            coinUpDirection = oldUpDirection;
            coinX = getX() + (double)(getWidth() / 2);
            coinY = getY() + (double)(getHeight() / 2);
        }
    }

    protected void render(Graphics2D g)
    {
        super.render(g);
        if(bCoin)
        {
            g.setColor(Color.YELLOW);
            g.fillOval((int)coinX - 3, (int)coinY - 3, 6, 6);
            g.setColor(Color.BLACK);
            g.drawOval((int)coinX - 3, (int)coinY - 3, 6, 6);
        }
    }

    protected int getCoinLeftDirection()
    {
        return coinLeftDirection;
    }

    protected void setCoinLeftDirection(int coinLeftDirection)
    {
        this.coinLeftDirection = coinLeftDirection;
    }

    protected int getCoinUpDirection()
    {
        return coinUpDirection;
    }

    protected void setCoinUpDirection(int coinUpDirection)
    {
        this.coinUpDirection = coinUpDirection;
    }

    protected double getCoinX()
    {
        return coinX;
    }

    protected void setCoinX(double coinX)
    {
        this.coinX = coinX;
    }

    protected double getCoinY()
    {
        return coinY;
    }

    protected void setCoinY(double coinY)
    {
        this.coinY = coinY;
    }

    protected int getCurrentCoinTicks()
    {
        return currentCoinTicks;
    }

    protected void setCurrentCoinTicks(int currentCoinTicks)
    {
        this.currentCoinTicks = currentCoinTicks;
    }

    protected int getMaxCoinTicks()
    {
        return 300;
    }

    protected int getThrowCoinTicks()
    {
        return throwCoinTicks;
    }

    protected void setCoins(int coins)
    {
        this.coins = coins;
    }

    protected void setBCoinBounce(boolean coinBounce)
    {
        bCoinBounce = coinBounce;
    }

    private final double speed = 2D;
    private final int empty = 0;
    private final int up = -1;
    private final int right = 1;
    private final int down = 1;
    private final int left = -1;
    private final int maxCoinTicks = 300;
    private final int throwCoinTicks;
    private int coins;
    private int oldCoins;
    private int leftDirection;
    private int upDirection;
    private int oldLeftDirection;
    private int oldUpDirection;
    private int coinLeftDirection;
    private int coinUpDirection;
    private int currentCoinTicks;
    private double coinX;
    private double coinY;
    private boolean bAction;
    private boolean bFinish;
    private boolean bCoin;
    private boolean bCoinBounce;
    private double detect;
    private Color percentColor;
    private Color cheatColor;
    private double cheat;
    private int player;
}
