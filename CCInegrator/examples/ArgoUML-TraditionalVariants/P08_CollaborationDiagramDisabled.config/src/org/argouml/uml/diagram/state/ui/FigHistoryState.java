

// $Id: FigHistoryState.java 119 2010-09-15 23:46:24Z marcusvnac $
// Copyright (c) 1996-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML HistoryState in a diagram.
 *
 * This abstract class is used for both a DeepHistory and a ShallowHistory.
 *
 * @author jrobbins
 */
public abstract class FigHistoryState extends FigStateVertex {

    private static final int X = X0;
    private static final int Y = Y0;
    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    /**
     * The main label on this icon.
     */
    private FigText h;
    private FigCircle head;

    
    /**
     * Construct a new FigSubactivityState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigHistoryState(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs();
    }
    
    /**
     * Main constructor.
     * @deprecated for 0.27.4 by tfmorris.  Use 
     * {@link #FigHistoryState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigHistoryState() {
        initFigs();
    }

    private void initFigs() {
        setEditable(false);
        setBigPort(new FigCircle(X, Y, WIDTH, HEIGHT, DEBUG_COLOR, 
                DEBUG_COLOR));
        head = new FigCircle(X, Y, WIDTH, HEIGHT, LINE_COLOR, FILL_COLOR);
        h = new FigText(X, Y, WIDTH - 10, HEIGHT - 10);
        h.setFont(getSettings().getFontPlain());
        h.setText(getH());
        h.setTextColor(TEXT_COLOR);
        h.setFilled(false);
        h.setLineWidth(0);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(head);
        addFig(h);

        setBlinkPorts(false); //make port invisible unless mouse enters
    }


    /**
     * The constructor that hooks the Fig into the UML modelelement.
     *
     * @param gm ignored
     * @param node the UML element
     * @deprecated for 0.27.4 by tfmorris.  Use 
     * {@link #FigHistoryState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigHistoryState(@SuppressWarnings("unused") GraphModel gm, 
            Object node) {
        this();
        setOwner(node);
    }
    
    /**
     * Override setBounds to keep shapes looking right.
     * {@inheritDoc}
     */
    @Override
    protected void setStandardBounds(int x, int y, 
            int width, int height) {        
    	if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();

        getBigPort().setBounds(x, y, WIDTH, HEIGHT);
        head.setBounds(x, y, WIDTH, HEIGHT);
        this.h.setBounds(x, y, WIDTH - 10, HEIGHT - 10);
        this.h.calcBounds();

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }
    
    /**
     * This should return the text shown at the center of the history state.
     *
     * @return the text at the center (H or H*)
     */
    protected abstract String getH();

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    @Override
    public String placeString() {
        return "H";
    }


    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigHistoryState figClone = (FigHistoryState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.head = (FigCircle) it.next();
        figClone.h = (FigText) it.next();
        return figClone;
    }


    /**
     * History states are fixed size.
     * @return false
     */
    @Override
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        head.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return head.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        head.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return head.getFillColor();
    }

    /**
     * Ignored - figure has fixed rendering
     * @param f ignored
     */
    @Override
    public void setFilled(boolean f) {
        // ignored
    }

    @Override
    public boolean isFilled() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        head.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return head.getLineWidth();
    }


    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        // ignored
    }

    /**
     * The UID.
     */
    static final long serialVersionUID = 6572261327347541373L;

}
