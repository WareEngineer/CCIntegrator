

// $Id: FigBranchState.java 119 2010-09-15 23:46:24Z marcusvnac $
// Copyright (c) 2004-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigCircle;

/**
 * Class to display graphics for a UML Choice State in a diagram - the circle.
 *
 * TODO: This should really be renamed FigChoiceState.  It's the
 * last vestige the UML 1.3 name.
 *
 * @author pepargouml
 */
public class FigBranchState extends FigStateVertex {

    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;

    private FigCircle head;
    private FigCircle bp;


    
    /**
     * Construct a new FigBranchState.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigBranchState(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initFigs();
    }
    
    
    /**
     * Constructor.
     * @deprecated for 0.27.4 by tfmorris.  Use 
     * {@link #FigBranchState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigBranchState() {
        super();
        initFigs();
    }


    private void initFigs() {
        setEditable(false);
        bp = new FigCircle(X0, Y0, WIDTH, HEIGHT, DEBUG_COLOR, DEBUG_COLOR);
        setBigPort(bp);
        head = new FigCircle(X0, Y0, WIDTH, HEIGHT, LINE_COLOR, FILL_COLOR);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(head);

        setBlinkPorts(false); //make port invisible unless mouse enters
    }

    /**
     * Constructor.
     *
     * @param gm ignored
     * @param node the owner
     * @deprecated for 0.27.4 by tfmorris.  Use 
     * {@link #FigBranchState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigBranchState(@SuppressWarnings("unused") GraphModel gm, 
            Object node) {
        this();
        setOwner(node);
    }

    /*
     * This makes dragging connected edges very smooth.
     *
     * @see org.tigris.gef.presentation.Fig#getClosestPoint(java.awt.Point)
     */
    @Override
    public Point getClosestPoint(Point anotherPt) {
        Point p = bp.connectionPoint(anotherPt);
        return p;
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigBranchState figClone = (FigBranchState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigCircle) it.next());
        figClone.head = (FigCircle) it.next();
        return figClone;
    }


    /**
     * Choice states are fixed size.
     *
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
     * Ignored - figure has a fixed rendering.
     * @param f ignored
     */
    @Override
    public void setFilled(boolean f) {
        // ignored - fixed rendering
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

    ////////////////////////////////////////////////////////////////
    // Event handlers

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        // ignored
    }

    /**
     * Override setStandardBounds to keep shapes looking right.
     * {@inheritDoc}
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();

        getBigPort().setBounds(x, y, w, h);
        head.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }


    /**
     * The UID.
     */
    static final long serialVersionUID = 6572261327347541373L;

}
