// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ApoCheatingHudPanel.java

package apoEditor;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Referenced classes of package apoEditor:
//            ApoCheatingImage, ApoCheatingButton, ApoCheatingButtonText, ApoCheatingButtonTextImage, 
//            ApoCheatingTeacher, ApoCheatingTeacherWay, ApoCheatingTeacherView, ApoCheatingGamePanel, 
//            ApoCheatingEntityHand, ApoCheatingGoal, ApoCheatingFinish, ApoCheatingPlayer

public class ApoCheatingHudPanel extends JPanel
    implements MouseListener, MouseMotionListener, ListSelectionListener
{

    public ApoCheatingHudPanel()
    {
        setLayout(null);
        currentI = -1;
        fontTitle = new Font("Dialog", 1, 20);
        ApoCheatingImage image = new ApoCheatingImage();
        buttons = new ApoCheatingButton[3];
        buttons[0] = new ApoCheatingButton(image.setPicsMain("/images/button/button_quit.png", false), 135, 455, 25, 25, "Quit");
        buttons[1] = new ApoCheatingButton(image.setPicsMain("/images/button/hud_load.png", false), 20, 55, 41, 18, "Load");
        buttons[2] = new ApoCheatingButton(image.setPicsMain("/images/button/hud_save.png", false), 100, 55, 43, 18, "Save");
        textButtons = new ApoCheatingButtonText[13];
        textButtons[0] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 0, 83, 53, 20, "Level");
        textButtons[0].setBVisible(false);
        textButtons[1] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 53, 83, 54, 20, "Person");
        textButtons[2] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 107, 83, 53, 20, "Setting");
        textButtons[3] = new ApoCheatingButtonText(Color.BLUE, Color.BLACK, 2, 120, 50, 20, "Player");
        textButtons[3].setBVisible(false);
        textButtons[4] = new ApoCheatingButtonText(Color.RED, Color.BLACK, 56, 120, 50, 20, "Teacher");
        textButtons[4].setBVisible(false);
        textButtons[5] = new ApoCheatingButtonText(Color.YELLOW, Color.BLACK, 109, 120, 50, 20, "Extra");
        textButtons[5].setBVisible(false);
        textButtons[6] = new ApoCheatingButtonText(Color.GREEN, Color.BLACK, 15, 145, 50, 20, "Goal");
        textButtons[6].setBVisible(false);
        textButtons[7] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 95, 145, 50, 20, "Finish");
        textButtons[7].setBVisible(false);
        textButtons[8] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 55, 345, 50, 20, "Ok");
        textButtons[8].setBVisible(false);
        textButtons[9] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 50, 320, 30, 15, "add");
        textButtons[9].setBVisible(false);
        textButtons[10] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 50, 345, 30, 15, "del");
        textButtons[10].setBVisible(false);
        textButtons[11] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 127, 320, 30, 15, "add");
        textButtons[11].setBVisible(false);
        textButtons[12] = new ApoCheatingButtonText(Color.WHITE, Color.BLACK, 127, 345, 30, 15, "del");
        textButtons[12].setBVisible(false);
        java.awt.image.BufferedImage iBackground = image.setPicsMain("/images/tilesheet.png", false);
        textImageButtons = new ApoCheatingButtonTextImage[13];
        textImageButtons[0] = new ApoCheatingButtonTextImage(iBackground, 0, 1, Color.BLUE, Color.WHITE, 8, 120, 35, 35, "Chair_up_left");
        textImageButtons[1] = new ApoCheatingButtonTextImage(iBackground, 1, 1, Color.BLUE, Color.WHITE, 45, 120, 35, 35, "Chair_up_right");
        textImageButtons[2] = new ApoCheatingButtonTextImage(iBackground, 2, 1, Color.BLUE, Color.WHITE, 82, 120, 35, 35, "Chair_down_left");
        textImageButtons[3] = new ApoCheatingButtonTextImage(iBackground, 3, 1, Color.BLUE, Color.WHITE, 119, 120, 35, 35, "Chair_down_right");
        textImageButtons[4] = new ApoCheatingButtonTextImage(iBackground, 6, 2, Color.BLUE, Color.WHITE, 25, 157, 35, 35, "Chair_left_left");
        textImageButtons[5] = new ApoCheatingButtonTextImage(iBackground, 6, 3, Color.BLUE, Color.WHITE, 65, 157, 35, 35, "Chair_left_right");
        textImageButtons[6] = new ApoCheatingButtonTextImage(iBackground, 3, 0, Color.BLUE, Color.WHITE, 105, 157, 35, 35, "Chair_right_right");
        textImageButtons[7] = new ApoCheatingButtonTextImage(iBackground, 5, 0, 6, 2, Color.BLUE, Color.WHITE, 8, 194, 35, 35, "Door_left");
        textImageButtons[8] = new ApoCheatingButtonTextImage(iBackground, 4, 0, 5, 2, Color.BLUE, Color.WHITE, 45, 194, 35, 35, "Door_right");
        textImageButtons[9] = new ApoCheatingButtonTextImage(iBackground, 4, 2, 5, 4, Color.BLUE, Color.WHITE, 82, 194, 35, 35, "Window_left");
        textImageButtons[10] = new ApoCheatingButtonTextImage(iBackground, 5, 2, 6, 4, Color.BLUE, Color.WHITE, 119, 194, 35, 35, "Window_right");
        textImageButtons[11] = new ApoCheatingButtonTextImage(iBackground, 0, 0, 2, 1, Color.BLUE, Color.WHITE, 45, 231, 35, 35, "Desk_up");
        textImageButtons[12] = new ApoCheatingButtonTextImage(iBackground, 6, 0, 7, 2, Color.BLUE, Color.WHITE, 82, 231, 35, 35, "Desk_left");
        currentString = "";
        fieldX = new JTextField("0");
        fieldX.setSize(40, 20);
        fieldX.setLocation(20, 295);
        fieldX.setVisible(false);
        add(fieldX);
        fieldY = new JTextField("0");
        fieldY.setSize(40, 20);
        fieldY.setLocation(100, 295);
        fieldY.setVisible(false);
        add(fieldY);
        fieldPlayer = new JTextField("1");
        fieldPlayer.setSize(40, 20);
        fieldPlayer.setLocation(20, 320);
        fieldPlayer.setVisible(false);
        add(fieldPlayer);
        fieldPlus = new JTextField("0.3");
        fieldPlus.setSize(40, 20);
        fieldPlus.setLocation(20, 320);
        fieldPlus.setVisible(false);
        add(fieldPlus);
        fieldCoin = new JTextField("0");
        fieldCoin.setSize(40, 20);
        fieldCoin.setLocation(100, 320);
        fieldCoin.setVisible(false);
        add(fieldCoin);
        fieldSpeed = new JTextField("2.0");
        fieldSpeed.setSize(40, 20);
        fieldSpeed.setLocation(20, 425);
        fieldSpeed.setVisible(false);
        add(fieldSpeed);
        fieldTicks = new JTextField("100");
        fieldTicks.setSize(40, 20);
        fieldTicks.setLocation(100, 425);
        fieldTicks.setVisible(false);
        add(fieldTicks);
        fieldStartDir = new JTextField("30");
        fieldStartDir.setSize(40, 20);
        fieldStartDir.setLocation(20, 400);
        fieldStartDir.setVisible(false);
        add(fieldStartDir);
        fieldEndDir = new JTextField("150");
        fieldEndDir.setSize(40, 20);
        fieldEndDir.setLocation(100, 400);
        fieldEndDir.setVisible(false);
        add(fieldEndDir);
        fieldHeight = new JTextField("270");
        fieldHeight.setSize(40, 20);
        fieldHeight.setLocation(100, 375);
        fieldHeight.setVisible(false);
        add(fieldHeight);
        fieldWidth = new JTextField("50");
        fieldWidth.setSize(40, 20);
        fieldWidth.setLocation(20, 375);
        fieldWidth.setVisible(false);
        add(fieldWidth);
        checkBoxDetect = new JCheckBox("Detect");
        checkBoxDetect.setSize(70, 20);
        checkBoxDetect.setLocation(80, 320);
        checkBoxDetect.setVisible(false);
        checkBoxDetect.setSelected(false);
        add(checkBoxDetect);
        textAreaFirst = new JTextArea("");
        textAreaFirst.setBackground(Color.WHITE);
        textAreaFirst.setForeground(Color.BLACK);
        textAreaFirst.setLineWrap(true);
        textAreaFirst.setWrapStyleWord(true);
        areaScrollPane = new JScrollPane(textAreaFirst);
        areaScrollPane.setVerticalScrollBarPolicy(20);
        areaScrollPane.setLocation(10, 110);
        areaScrollPane.setSize(140, 155);
        areaScrollPane.setVisible(false);
        add(areaScrollPane);
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setBackground(Color.BLACK);
        list.setForeground(Color.WHITE);
        list.setSelectionForeground(Color.BLACK);
        list.setSelectionBackground(Color.WHITE);
        list.setSelectionMode(0);
        list.setToolTipText("Zeigt alle Wege des Lehrers an.");
        list.addListSelectionListener(this);
        listScrollPane = new JScrollPane(list);
        listScrollPane.setSize(40, 45);
        listScrollPane.setLocation(5, 320);
        listScrollPane.setVisible(false);
        add(listScrollPane);
        listViewModel = new DefaultListModel();
        listView = new JList(listViewModel);
        listView.setBackground(Color.BLACK);
        listView.setForeground(Color.WHITE);
        listView.setSelectionForeground(Color.BLACK);
        listView.setSelectionBackground(Color.WHITE);
        listView.setSelectionMode(0);
        listView.setToolTipText("Zeigt alle Wege des Lehrers an.");
        listView.addListSelectionListener(this);
        listViewScrollPane = new JScrollPane(listView);
        listViewScrollPane.setSize(40, 45);
        listViewScrollPane.setLocation(85, 320);
        listViewScrollPane.setVisible(false);
        add(listViewScrollPane);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if(currentTeacher == null)
            return;
        if(!fieldSpeed.isVisible())
        {
            fieldSpeed.setVisible(true);
            fieldTicks.setVisible(true);
            fieldStartDir.setVisible(true);
            fieldEndDir.setVisible(true);
            fieldWidth.setVisible(true);
            fieldHeight.setVisible(true);
        }
        Object source = e.getSource();
        if(source == list)
        {
            listView.setSelectedIndex(-1);
            int i = list.getSelectedIndex();
            if(i >= 0 && currentI != i || bView)
            {
                currentI = i;
                ApoCheatingTeacherWay way = currentTeacher.getWay(i);
                if(way != null)
                {
                    fieldSpeed.setText((new StringBuilder(String.valueOf(way.getSpeed()))).toString());
                    fieldTicks.setText((new StringBuilder(String.valueOf(way.getMaxTicks()))).toString());
                    fieldStartDir.setText((new StringBuilder(String.valueOf(way.getFinishX()))).toString());
                    fieldEndDir.setText((new StringBuilder(String.valueOf(way.getFinishY()))).toString());
                    fieldWidth.setText((new StringBuilder(String.valueOf(way.getStartX()))).toString());
                    fieldHeight.setText((new StringBuilder(String.valueOf(way.getStartY()))).toString());
                }
            }
            bView = false;
        } else
        if(source == listView)
        {
            list.setSelectedIndex(-1);
            int i = listView.getSelectedIndex();
            if(i >= 0 && currentI != i || !bView)
            {
                currentI = i;
                ApoCheatingTeacherView view = currentTeacher.getView(i);
                if(view != null)
                {
                    fieldSpeed.setText((new StringBuilder(String.valueOf(view.getSpeed()))).toString());
                    fieldTicks.setText((new StringBuilder(String.valueOf(view.getMaxTicks()))).toString());
                    fieldStartDir.setText((new StringBuilder(String.valueOf(view.getStartDir()))).toString());
                    fieldEndDir.setText((new StringBuilder(String.valueOf(view.getEndDir()))).toString());
                    fieldWidth.setText((new StringBuilder(String.valueOf(view.getWidth()))).toString());
                    fieldHeight.setText((new StringBuilder(String.valueOf(view.getHeight()))).toString());
                }
            }
            bView = true;
        }
    }

    protected ArrayList getPlayer()
    {
        return player;
    }

    protected void setPlayer(ArrayList player)
    {
        this.player = player;
    }

    protected ApoCheatingGamePanel getApoCheatingGamePanel()
    {
        return apoCheatingGamePanel;
    }

    protected void setApoCheatingGamePanel(ApoCheatingGamePanel apoCheatingGamePanel)
    {
        this.apoCheatingGamePanel = apoCheatingGamePanel;
    }

    protected void setReloadButton()
    {
        buttons[1].setBVisible(false);
        buttons[2].setBVisible(false);
        buttons[3].setBVisible(false);
        buttons[4].setBVisible(true);
        buttons[5].setBVisible(false);
        buttons[6].setBVisible(false);
    }

    private void save()
    {
        if(apoCheatingGamePanel != null)
            apoCheatingGamePanel.save();
    }

    private void load()
    {
        if(apoCheatingGamePanel != null)
            apoCheatingGamePanel.load(null);
    }

    private void setLevel()
    {
        if(apoCheatingGamePanel != null && !apoCheatingGamePanel.isBThread())
        {
            textButtons[0].setBVisible(false);
            setLevel(true);
            textButtons[1].setBVisible(true);
            setPerson(false);
            textButtons[2].setBVisible(true);
            setSetting(false);
            setInputComponents(false);
        }
    }

    private void setPerson()
    {
        if(apoCheatingGamePanel != null && !apoCheatingGamePanel.isBThread())
        {
            textButtons[0].setBVisible(true);
            setLevel(false);
            textButtons[1].setBVisible(false);
            setPerson(true);
            textButtons[2].setBVisible(true);
            setSetting(false);
            setInputComponents(false);
        }
    }

    private void setSetting()
    {
        if(apoCheatingGamePanel != null && !apoCheatingGamePanel.isBThread())
        {
            textButtons[0].setBVisible(true);
            setLevel(false);
            textButtons[1].setBVisible(true);
            setPerson(false);
            textButtons[2].setBVisible(false);
            setSetting(true);
            setInputComponents(false);
            areaScrollPane.setVisible(true);
        }
    }

    private void setLevel(boolean bLevel)
    {
        textImageButtons[0].setBVisible(bLevel);
        textImageButtons[1].setBVisible(bLevel);
        textImageButtons[2].setBVisible(bLevel);
        textImageButtons[3].setBVisible(bLevel);
        textImageButtons[4].setBVisible(bLevel);
        textImageButtons[5].setBVisible(bLevel);
        textImageButtons[6].setBVisible(bLevel);
        textImageButtons[7].setBVisible(bLevel);
        textImageButtons[8].setBVisible(bLevel);
        textImageButtons[9].setBVisible(bLevel);
        textImageButtons[10].setBVisible(bLevel);
        textImageButtons[11].setBVisible(bLevel);
        textImageButtons[12].setBVisible(bLevel);
    }

    private void setPerson(boolean bPerson)
    {
        textButtons[3].setBVisible(bPerson);
        textButtons[4].setBVisible(bPerson);
        textButtons[5].setBVisible(bPerson);
        textButtons[6].setBVisible(bPerson);
        textButtons[7].setBVisible(bPerson);
        textButtons[8].setBVisible(bPerson);
        textButtons[9].setBVisible(bPerson);
        textButtons[10].setBVisible(bPerson);
        textButtons[11].setBVisible(bPerson);
        textButtons[12].setBVisible(bPerson);
    }

    private void setSetting(boolean flag)
    {
    }

    private void setLevel(int value)
    {
        if(apoCheatingGamePanel != null && !apoCheatingGamePanel.isBThread())
            apoCheatingGamePanel.setLevel(value);
    }

    private void setPerson(int value)
    {
        if(apoCheatingGamePanel != null && !apoCheatingGamePanel.isBThread())
            apoCheatingGamePanel.setPerson(value);
    }

    protected void setInputComponents(boolean bInput)
    {
        if(!bInput)
        {
            currentEntity = null;
            currentGoal = null;
            currentFinish = null;
            currentPlayer = null;
            currentTeacher = null;
            currentString = null;
        }
        fieldX.setVisible(bInput);
        fieldY.setVisible(bInput);
        fieldPlayer.setVisible(bInput);
        fieldPlus.setVisible(bInput);
        fieldCoin.setVisible(bInput);
        fieldStartDir.setVisible(bInput);
        fieldEndDir.setVisible(bInput);
        fieldSpeed.setVisible(bInput);
        fieldTicks.setVisible(bInput);
        fieldWidth.setVisible(bInput);
        fieldHeight.setVisible(bInput);
        checkBoxDetect.setVisible(bInput);
        areaScrollPane.setVisible(bInput);
        listScrollPane.setVisible(bInput);
        listViewScrollPane.setVisible(bInput);
        textButtons[8].setBVisible(bInput);
        textButtons[8].setX(55D);
        textButtons[8].setY(345D);
        textButtons[9].setBVisible(bInput);
        textButtons[10].setBVisible(bInput);
        textButtons[11].setBVisible(bInput);
        textButtons[12].setBVisible(bInput);
        repaint();
    }

    protected void setSelected(ApoCheatingEntityHand currentEntity)
    {
        setInputComponents(false);
        currentString = "Extra";
        fieldX.setVisible(true);
        fieldY.setVisible(true);
        checkBoxDetect.setVisible(true);
        textButtons[8].setBVisible(true);
        this.currentEntity = currentEntity;
        currentGoal = null;
        currentFinish = null;
        currentPlayer = null;
        currentTeacher = null;
        fieldX.setText((new StringBuilder()).append((int)((this.currentEntity.getX() + (double)(this.currentEntity.getWidth() / 2)) / 32D)).toString());
        fieldY.setText((new StringBuilder()).append((int)((this.currentEntity.getY() + (double)(this.currentEntity.getHeight() / 2)) / 32D)).toString());
        checkBoxDetect.setSelected(this.currentEntity.isBDetect());
        repaint();
    }

    protected void setSelected(ApoCheatingGoal currentGoal)
    {
        setInputComponents(false);
        currentString = "Goal";
        fieldX.setVisible(true);
        fieldY.setVisible(true);
        fieldPlus.setVisible(true);
        checkBoxDetect.setVisible(true);
        textButtons[8].setBVisible(true);
        currentEntity = null;
        this.currentGoal = currentGoal;
        currentFinish = null;
        currentPlayer = null;
        currentTeacher = null;
        fieldX.setText((new StringBuilder()).append((int)((this.currentGoal.getX() + (double)(this.currentGoal.getWidth() / 2)) / 32D)).toString());
        fieldY.setText((new StringBuilder()).append((int)((this.currentGoal.getY() + (double)(this.currentGoal.getHeight() / 2)) / 32D)).toString());
        fieldPlus.setText((new StringBuilder()).append(this.currentGoal.getOldPlus()).toString());
        checkBoxDetect.setSelected(this.currentGoal.isBDetect());
        repaint();
    }

    protected void setSelected(ApoCheatingFinish currentFinish)
    {
        setInputComponents(false);
        currentString = "Finish";
        fieldX.setVisible(true);
        fieldY.setVisible(true);
        fieldPlayer.setVisible(true);
        textButtons[8].setBVisible(true);
        currentEntity = null;
        currentGoal = null;
        this.currentFinish = currentFinish;
        currentPlayer = null;
        currentTeacher = null;
        fieldX.setText((new StringBuilder()).append((int)((this.currentFinish.getX() + (double)(this.currentFinish.getWidth() / 2)) / 32D)).toString());
        fieldY.setText((new StringBuilder()).append((int)((this.currentFinish.getY() + (double)(this.currentFinish.getHeight() / 2)) / 32D)).toString());
        fieldPlayer.setText((new StringBuilder()).append(this.currentFinish.getPlayer()).toString());
        repaint();
    }

    protected void setSelected(ApoCheatingPlayer currentPlayer)
    {
        setInputComponents(false);
        currentString = "Player";
        fieldX.setVisible(true);
        fieldY.setVisible(true);
        fieldPlayer.setVisible(true);
        fieldCoin.setVisible(true);
        textButtons[8].setBVisible(true);
        currentEntity = null;
        currentGoal = null;
        currentFinish = null;
        this.currentPlayer = currentPlayer;
        currentTeacher = null;
        fieldX.setText((new StringBuilder()).append((int)((this.currentPlayer.getX() + (double)(this.currentPlayer.getWidth() / 2)) / 32D)).toString());
        fieldY.setText((new StringBuilder()).append((int)((this.currentPlayer.getY() + (double)(this.currentPlayer.getHeight() / 2)) / 32D)).toString());
        fieldPlayer.setText((new StringBuilder()).append(this.currentPlayer.getPlayer()).toString());
        fieldCoin.setText((new StringBuilder()).append(this.currentPlayer.getCoins()).toString());
        repaint();
    }

    protected void setSelected(ApoCheatingTeacher currentTeacher)
    {
        setInputComponents(false);
        currentString = "Teacher";
        fieldX.setVisible(true);
        fieldY.setVisible(true);
        listScrollPane.setVisible(true);
        listViewScrollPane.setVisible(true);
        textButtons[8].setBVisible(true);
        textButtons[8].setX(55D);
        textButtons[8].setY(450D);
        textButtons[9].setBVisible(true);
        textButtons[10].setBVisible(true);
        textButtons[11].setBVisible(true);
        textButtons[12].setBVisible(true);
        currentEntity = null;
        currentGoal = null;
        currentFinish = null;
        currentPlayer = null;
        this.currentTeacher = currentTeacher;
        fieldX.setText((new StringBuilder()).append((int)((this.currentTeacher.getX() + (double)(this.currentTeacher.getWidth() / 2)) / 32D)).toString());
        fieldY.setText((new StringBuilder()).append((int)((this.currentTeacher.getY() + (double)(this.currentTeacher.getHeight() / 2)) / 32D)).toString());
        bView = false;
        fieldStartDir.setVisible(true);
        fieldEndDir.setVisible(true);
        fieldSpeed.setVisible(true);
        fieldTicks.setVisible(true);
        fieldWidth.setVisible(true);
        fieldHeight.setVisible(true);
        setModelView();
        setModelWay();
        repaint();
    }

    private void setOK()
    {
        int x = 0;
        try
        {
            x = Integer.parseInt(fieldX.getText());
        }
        catch(Exception e)
        {
            x = 0;
            fieldX.setText((new StringBuilder()).append(x).toString());
        }
        int y = 0;
        try
        {
            y = Integer.parseInt(fieldY.getText());
        }
        catch(Exception e)
        {
            y = 0;
            fieldY.setText((new StringBuilder()).append(y).toString());
        }
        int player = 0;
        try
        {
            player = Integer.parseInt(fieldPlayer.getText());
        }
        catch(Exception e)
        {
            player = 0;
            fieldPlayer.setText((new StringBuilder()).append(player).toString());
        }
        int coin = 0;
        try
        {
            coin = Integer.parseInt(fieldCoin.getText());
        }
        catch(Exception e)
        {
            coin = 0;
            fieldCoin.setText((new StringBuilder()).append(coin).toString());
        }
        double plus = 0.0D;
        try
        {
            plus = Double.parseDouble(fieldPlus.getText());
        }
        catch(Exception e)
        {
            plus = 0.0D;
            fieldPlus.setText((new StringBuilder()).append(plus).toString());
        }
        if(currentEntity != null)
        {
            if(x > 0 && x < 14 && y > 0 && y < 14 && apoCheatingGamePanel.isFree(x, y, currentEntity))
            {
                currentEntity.setX(x * 32 + 6);
                currentEntity.setY(y * 32 + 6);
                boolean bSelect = checkBoxDetect.isSelected();
                currentEntity.setBDetect(bSelect);
                apoCheatingGamePanel.setHands(currentEntity);
                apoCheatingGamePanel.repaint();
            }
        } else
        if(currentGoal != null)
        {
            if(x > 0 && x < 14 && y > 0 && y < 14 && plus >= 0.0D && plus <= 1.0D && apoCheatingGamePanel.isFree(x, y, currentGoal))
            {
                currentGoal.setX(x * 32 + 6);
                currentGoal.setY(y * 32 + 6);
                boolean bSelect = checkBoxDetect.isSelected();
                currentGoal.setBDetect(bSelect);
                currentGoal.setOldPlus(plus);
                apoCheatingGamePanel.setHands(currentGoal);
                apoCheatingGamePanel.repaint();
            }
        } else
        if(currentFinish != null)
        {
            if(x > 0 && x < 14 && y > 0 && y < 14 && player > 0 && player <= apoCheatingGamePanel.getPlayer().size() && apoCheatingGamePanel.isFree(x, y, currentFinish))
            {
                currentFinish.setX(x * 32 + 16);
                currentFinish.setY(y * 32 + 16);
                currentFinish.setPlayer(player);
                apoCheatingGamePanel.repaint();
            }
        } else
        if(currentPlayer != null)
        {
            if(x > 0 && x < 14 && y > 0 && y < 14 && player > 0 && player <= apoCheatingGamePanel.getPlayer().size() && coin >= 0 && coin <= 10 && apoCheatingGamePanel.isFree(x, y, currentPlayer))
            {
                currentPlayer.setX(x * 32 + 6);
                currentPlayer.setY(y * 32 + 6);
                currentPlayer.setPlayer(player);
                currentPlayer.setCoins(coin);
                apoCheatingGamePanel.repaint();
            }
        } else
        if(currentTeacher != null && x > 0 && x < 14 && y > 0 && y < 14 && apoCheatingGamePanel.isFree(x, y, currentTeacher))
        {
            currentTeacher.setX(x * 32 + 6);
            currentTeacher.setY(y * 32 + 6);
            if(bView)
            {
                int i = listView.getSelectedIndex();
                if(i < 0)
                {
                    apoCheatingGamePanel.repaint();
                    return;
                }
                ApoCheatingTeacherView view = currentTeacher.getView(i);
                double speed = 0.0D;
                try
                {
                    speed = Double.parseDouble(fieldSpeed.getText());
                    view.setSpeed(speed);
                }
                catch(Exception e)
                {
                    speed = 1.5D;
                    fieldSpeed.setText((new StringBuilder()).append(speed).toString());
                }
                int maxTicks = 0;
                try
                {
                    maxTicks = Integer.parseInt(fieldTicks.getText());
                    view.setMaxTicks(maxTicks);
                }
                catch(Exception e)
                {
                    maxTicks = 100;
                    fieldTicks.setText((new StringBuilder()).append(maxTicks).toString());
                }
                int oldX = 0;
                try
                {
                    oldX = Integer.parseInt(fieldWidth.getText());
                    view.setWidth(oldX);
                }
                catch(Exception e)
                {
                    oldX = 50;
                    fieldWidth.setText((new StringBuilder()).append(oldX).toString());
                }
                int oldY = 0;
                try
                {
                    oldY = Integer.parseInt(fieldHeight.getText());
                    view.setHeight(oldY);
                }
                catch(Exception e)
                {
                    oldY = 270;
                    fieldHeight.setText((new StringBuilder()).append(oldY).toString());
                }
                double startDir = 0.0D;
                try
                {
                    startDir = Double.parseDouble(fieldStartDir.getText());
                    view.setStartDir((int)startDir);
                }
                catch(Exception e)
                {
                    startDir = 30D;
                    fieldStartDir.setText((new StringBuilder()).append((int)startDir).toString());
                }
                double endDir = 0.0D;
                try
                {
                    endDir = Double.parseDouble(fieldEndDir.getText());
                    view.setEndDir(endDir);
                }
                catch(Exception e)
                {
                    endDir = 150D;
                    fieldEndDir.setText((new StringBuilder()).append((int)endDir).toString());
                }
            } else
            {
                int i = list.getSelectedIndex();
                if(i < 0)
                {
                    apoCheatingGamePanel.repaint();
                    return;
                }
                ApoCheatingTeacherWay way = currentTeacher.getWay(i);
                double speed = 0.0D;
                try
                {
                    speed = Double.parseDouble(fieldSpeed.getText());
                    way.setSpeed(speed);
                }
                catch(Exception e)
                {
                    speed = 1.5D;
                    fieldSpeed.setText((new StringBuilder()).append(speed).toString());
                }
                int maxTicks = 0;
                try
                {
                    maxTicks = Integer.parseInt(fieldTicks.getText());
                    way.setMaxTicks(maxTicks);
                }
                catch(Exception e)
                {
                    maxTicks = 100;
                    fieldTicks.setText((new StringBuilder()).append(maxTicks).toString());
                }
                int oldX = 0;
                try
                {
                    oldX = Integer.parseInt(fieldWidth.getText());
                    way.setStartX(oldX);
                }
                catch(Exception e)
                {
                    oldX = 100;
                    fieldWidth.setText((new StringBuilder()).append(oldX).toString());
                }
                int oldY = 0;
                try
                {
                    oldY = Integer.parseInt(fieldHeight.getText());
                    way.setStartY(oldY);
                }
                catch(Exception e)
                {
                    oldY = 100;
                    fieldHeight.setText((new StringBuilder()).append(oldY).toString());
                }
                int newX = 0;
                try
                {
                    newX = Integer.parseInt(fieldStartDir.getText());
                    way.setFinishX(newX);
                }
                catch(Exception e)
                {
                    newX = 326;
                    fieldStartDir.setText((new StringBuilder()).append(newX).toString());
                }
                int newY = 0;
                try
                {
                    newY = Integer.parseInt(fieldEndDir.getText());
                    way.setFinishY(newY);
                }
                catch(Exception e)
                {
                    newY = 300;
                    fieldEndDir.setText((new StringBuilder()).append(newY).toString());
                }
            }
            apoCheatingGamePanel.repaint();
        }
    }

    private void setAddWay()
    {
        currentTeacher.addWay(currentI + 1);
        setModelWay();
        bView = false;
        listView.setSelectedIndex(-1);
        apoCheatingGamePanel.repaint();
    }

    private void setDeleteWay()
    {
        currentTeacher.deleteWay(currentI);
        setModelWay();
    }

    private void setModelWay()
    {
        int size = currentTeacher.getWay().size();
        listModel.removeAllElements();
        if(size > 0)
        {
            for(int i = 0; i < size; i++)
                listModel.addElement((new StringBuilder()).append(i).toString());

            list.setModel(listModel);
            list.setSelectedIndex(0);
            ApoCheatingTeacherWay way = currentTeacher.getWay(0);
            fieldSpeed.setText((new StringBuilder(String.valueOf(way.getSpeed()))).toString());
            fieldTicks.setText((new StringBuilder(String.valueOf(way.getMaxTicks()))).toString());
            fieldStartDir.setText((new StringBuilder(String.valueOf(way.getFinishX()))).toString());
            fieldEndDir.setText((new StringBuilder(String.valueOf(way.getFinishY()))).toString());
            fieldWidth.setText((new StringBuilder(String.valueOf(way.getStartX()))).toString());
            fieldHeight.setText((new StringBuilder(String.valueOf(way.getStartY()))).toString());
        } else
        {
            list.setModel(listModel);
        }
    }

    private void setAddView()
    {
        currentTeacher.addView(currentI + 1);
        setModelView();
        bView = true;
        list.setSelectedIndex(-1);
        apoCheatingGamePanel.repaint();
    }

    private void setDeleteView()
    {
        currentTeacher.deleteView(currentI + 1);
        setModelView();
    }

    private void setModelView()
    {
        int size = currentTeacher.getView().size();
        listViewModel.removeAllElements();
        if(size > 0)
        {
            for(int i = 0; i < size; i++)
                listViewModel.addElement((new StringBuilder()).append(i).toString());

            listView.setModel(listViewModel);
            listView.setSelectedIndex(0);
            ApoCheatingTeacherView view = currentTeacher.getView(0);
            fieldSpeed.setText((new StringBuilder(String.valueOf(view.getSpeed()))).toString());
            fieldTicks.setText((new StringBuilder(String.valueOf(view.getMaxTicks()))).toString());
            fieldStartDir.setText((new StringBuilder(String.valueOf(view.getStartDir()))).toString());
            fieldEndDir.setText((new StringBuilder(String.valueOf(view.getEndDir()))).toString());
            fieldWidth.setText((new StringBuilder(String.valueOf(view.getWidth()))).toString());
            fieldHeight.setText((new StringBuilder(String.valueOf(view.getHeight()))).toString());
        } else
        {
            listView.setModel(listViewModel);
        }
    }

    protected String getText()
    {
        return textAreaFirst.getText();
    }

    protected void setText(String text)
    {
        textAreaFirst.setText(text);
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        for(int i = 0; i < buttons.length; i++)
        {
            if(!buttons[i].getPressed(x, y))
                continue;
            repaint();
            break;
        }

        for(int i = 0; i < textButtons.length; i++)
            if(textButtons[i].getPressed(x, y))
            {
                repaint();
                return;
            }

        for(int i = 0; i < textImageButtons.length; i++)
            if(textImageButtons[i].getPressed(x, y))
            {
                repaint();
                return;
            }

    }

    public void mouseReleased(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        for(int i = 0; i < buttons.length; i++)
            if(buttons[i].getReleased(x, y))
            {
                String function = buttons[i].getFunction();
                if("Quit".equals(function))
                    System.exit(0);
                else
                if("Save".equals(function))
                {
                    save();
                    buttons[i].setBOver(false);
                } else
                if("Load".equals(function))
                {
                    load();
                    buttons[i].setBOver(false);
                }
            }

        for(int i = 0; i < textButtons.length; i++)
        {
            if(!textButtons[i].getReleased(x, y))
                continue;
            String function = textButtons[i].getFunction();
            if("Level".equals(function))
            {
                setLevel();
                continue;
            }
            if("Person".equals(function))
            {
                setPerson();
                continue;
            }
            if("Setting".equals(function))
            {
                setSetting();
                continue;
            }
            if("Player".equals(function))
            {
                setPerson(5);
                continue;
            }
            if("Teacher".equals(function))
            {
                setPerson(4);
                continue;
            }
            if("Extra".equals(function))
            {
                setPerson(1);
                continue;
            }
            if("Goal".equals(function))
            {
                setPerson(3);
                continue;
            }
            if("Finish".equals(function))
            {
                setPerson(2);
                continue;
            }
            if("Ok".equals(function))
            {
                setOK();
                continue;
            }
            if("add".equals(function))
            {
                if(i == 9)
                    setAddWay();
                else
                    setAddView();
                break;
            }
            if(!"del".equals(function))
                continue;
            if(i == 10)
                setDeleteWay();
            else
                setDeleteView();
            break;
        }

        for(int i = 0; i < textImageButtons.length; i++)
            if(textImageButtons[i].getReleased(x, y))
            {
                String function = textImageButtons[i].getFunction();
                if("Chair_up_left".equals(function))
                    setLevel(1);
                else
                if("Chair_up_right".equals(function))
                    setLevel(2);
                else
                if("Chair_down_left".equals(function))
                    setLevel(3);
                else
                if("Chair_down_right".equals(function))
                    setLevel(4);
                else
                if("Chair_left_left".equals(function))
                    setLevel(5);
                else
                if("Chair_left_right".equals(function))
                    setLevel(6);
                else
                if("Chair_right_right".equals(function))
                    setLevel(8);
                else
                if("Desk_up".equals(function))
                    setLevel(9);
                else
                if("Desk_left".equals(function))
                    setLevel(13);
                else
                if("Door_left".equals(function))
                    setLevel(31);
                else
                if("Door_right".equals(function))
                    setLevel(27);
                else
                if("Window_left".equals(function))
                    setLevel(25);
                else
                if("Window_right".equals(function))
                    setLevel(29);
            }

        repaint();
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
    }

    public void mouseMoved(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();
        for(int i = 0; i < buttons.length; i++)
            if(buttons[i].getMove(x, y))
            {
                repaint();
                return;
            }

        for(int i = 0; i < textButtons.length; i++)
            if(textButtons[i].getMove(x, y))
            {
                repaint();
                return;
            }

        for(int i = 0; i < textImageButtons.length; i++)
            if(textImageButtons[i].getMove(x, y))
            {
                repaint();
                return;
            }

    }

    public void update(Graphics g)
    {
        Image dbImage = createImage(getSize().width, getSize().height);
        Graphics dbGraphics = dbImage.getGraphics();
        dbGraphics.setColor(getBackground());
        dbGraphics.fillRect(0, 0, getSize().width, getSize().height);
        dbGraphics.setColor(getForeground());
        paint(dbGraphics);
        getGraphics().drawImage(dbImage, 0, 0, this);
        dbGraphics.dispose();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D gfx = (Graphics2D)g;
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBackground(gfx);
        Font clone = gfx.getFont();
        drawTitle(gfx);
        gfx.setFont(clone);
        for(int i = 0; i < buttons.length; i++)
            buttons[i].render(gfx);

        for(int i = 0; i < textButtons.length; i++)
            textButtons[i].render(gfx);

        for(int i = 0; i < textImageButtons.length; i++)
            textImageButtons[i].render(gfx);

        if(currentString != null)
        {
            gfx.setColor(Color.BLACK);
            gfx.drawString(currentString, 60, 290);
        }
        if(fieldX.isVisible())
            gfx.drawString("x", fieldX.getX() - 10, fieldX.getY() + 15);
        if(fieldY.isVisible())
            gfx.drawString("y", fieldY.getX() - 10, fieldY.getY() + 15);
        if(fieldPlayer.isVisible())
            gfx.drawString("p", fieldPlayer.getX() - 10, fieldPlayer.getY() + 15);
        if(fieldCoin.isVisible())
            gfx.drawString("coins", fieldCoin.getX() - 32, fieldCoin.getY() + 15);
        if(fieldSpeed.isVisible())
            gfx.drawString("s", fieldSpeed.getX() - 10, fieldSpeed.getY() + 15);
        if(fieldTicks.isVisible())
            gfx.drawString("t", fieldTicks.getX() - 10, fieldTicks.getY() + 15);
        if(fieldWidth.isVisible())
            if(bView)
                gfx.drawString("w", fieldWidth.getX() - 10, fieldWidth.getY() + 15);
            else
                gfx.drawString("x", fieldWidth.getX() - 10, fieldWidth.getY() + 15);
        if(fieldHeight.isVisible())
            if(bView)
                gfx.drawString("h", fieldHeight.getX() - 10, fieldHeight.getY() + 15);
            else
                gfx.drawString("y", fieldHeight.getX() - 10, fieldHeight.getY() + 15);
        if(fieldStartDir.isVisible())
            if(bView)
                gfx.drawString("sD", fieldStartDir.getX() - 15, fieldStartDir.getY() + 15);
            else
                gfx.drawString("x", fieldStartDir.getX() - 10, fieldStartDir.getY() + 15);
        if(fieldEndDir.isVisible())
            if(bView)
                gfx.drawString("eD", fieldEndDir.getX() - 15, fieldEndDir.getY() + 15);
            else
                gfx.drawString("y", fieldEndDir.getX() - 10, fieldEndDir.getY() + 15);
    }

    private void drawBackground(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLUE);
        int x = 0;
        int y = 0;
        for(int i = 0; i <= 3; i++)
        {
            g.drawLine(0, 0 + y, getWidth(), 0 + y);
            y += 16;
        }

        for(int i = 0; i < 10; i++)
        {
            g.drawLine(0 + x, 0, 0 + x, 48);
            x += 16;
        }

    }

    private void drawTitle(Graphics2D g)
    {
        g.setFont(fontTitle);
        String title = "Apo-Cheating";
        int w = g.getFontMetrics().stringWidth(title);
        g.setColor(Color.BLACK);
        g.drawString(title, (getWidth() / 2 - w / 2) + 1, 22);
        g.setColor(Color.RED);
        g.drawString(title, getWidth() / 2 - w / 2, 21);
        title = "Editor";
        w = g.getFontMetrics().stringWidth(title);
        g.setColor(Color.BLACK);
        g.drawString(title, (getWidth() / 2 - w / 2) + 1, 40);
        g.setColor(Color.RED);
        g.drawString(title, getWidth() / 2 - w / 2, 39);
        java.awt.Stroke clone = g.getStroke();
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3F, 0, 0));
        g.drawLine(2, 47, getWidth() - 2, 47);
        g.drawLine(2, 81, getWidth() - 2, 81);
        g.drawLine(2, 275, getWidth() - 2, 275);
        g.drawLine(2, 370, getWidth() - 2, 370);
        g.setStroke(clone);
    }

    private static final long serialVersionUID = 1L;
    private Font fontTitle;
    private ApoCheatingButton buttons[];
    private ApoCheatingButtonText textButtons[];
    private ApoCheatingButtonTextImage textImageButtons[];
    private ArrayList player;
    private JTextField fieldX;
    private JTextField fieldY;
    private JTextField fieldPlayer;
    private JTextField fieldCoin;
    private JTextField fieldPlus;
    private JTextField fieldSpeed;
    private JTextField fieldTicks;
    private JTextField fieldWidth;
    private JTextField fieldHeight;
    private JTextField fieldStartDir;
    private JTextField fieldEndDir;
    private JTextArea textAreaFirst;
    private JScrollPane areaScrollPane;
    private JCheckBox checkBoxDetect;
    private JList list;
    private DefaultListModel listModel;
    private JScrollPane listScrollPane;
    private JList listView;
    private DefaultListModel listViewModel;
    private JScrollPane listViewScrollPane;
    private String currentString;
    private ApoCheatingGamePanel apoCheatingGamePanel;
    private ApoCheatingEntityHand currentEntity;
    private ApoCheatingGoal currentGoal;
    private ApoCheatingPlayer currentPlayer;
    private ApoCheatingFinish currentFinish;
    private ApoCheatingTeacher currentTeacher;
    private boolean bView;
    private int currentI;
}
