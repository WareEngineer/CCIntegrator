

// $Id: FigActionState.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Iterator;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.argouml.uml.diagram.ui.FigMultiLineTextWithBold;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML ActionState in a diagram.
 * It contains a multiline textfield for the Entry Action Expression.
 *
 * @author ics 125b silverbullet team
 */
public class FigActionState extends FigStateVertex {

    private static final int HEIGHT = 25;

    private static final int STATE_WIDTH = 90;

    private static final int PADDING = 8;

    private FigRRect cover;

    /**
     * The notation provider for the textfield.
     */
    private NotationProvider notationProvider;


    /**
     * Constructor FigActionState.
     * 
     * @deprecated for 0.27.3 by mvw.  Use 
     * {@link #FigActionState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigActionState() {
        initializeActionState();
    }

    /**
     * Constructor FigActionState.
     *
     * @param gm ignored!
     * @param node owner
     * 
     * @deprecated for 0.27.3 by mvw.  Use 
     * {@link #FigActionState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigActionState(@SuppressWarnings("unused") GraphModel gm, 
            Object node) {
        setOwner(node);
        initializeActionState();
    }
    
    /**
     * Constructor used by PGML parser.
     * 
     * @param owner the owning UML element
     * @param bounds rectangle describing bounds
     * @param settings rendering settings
     */
    public FigActionState(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initializeActionState();
    }

    private void initializeActionState() {
        
        setBigPort(new FigRRect(X0 + 1, Y0 + 1, STATE_WIDTH - 2, HEIGHT - 2,
                DEBUG_COLOR, DEBUG_COLOR));
        ((FigRRect) getBigPort()).setCornerRadius(getBigPort().getHeight() / 2);
        cover = new FigRRect(X0, Y0, STATE_WIDTH, HEIGHT, 
                LINE_COLOR, FILL_COLOR);
        cover.setCornerRadius(getHeight() / 2);

        // overrule the single-line name-fig created by the parent
        Rectangle bounds = new Rectangle(X0 + PADDING, Y0, 
                STATE_WIDTH - PADDING * 2, HEIGHT);
        setNameFig(new FigMultiLineTextWithBold(
                getOwner(),
                bounds,
                getSettings(),
                true));
        getNameFig().setText(placeString());
        getNameFig().setBotMargin(7); // make space for the clarifier
        getNameFig().setTopMargin(7); // for vertical symmetry
        getNameFig().setRightMargin(4); // margin between text and border
        getNameFig().setLeftMargin(4);
        getNameFig().setJustification(FigText.JUSTIFY_CENTER);

        getBigPort().setLineWidth(0);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(getStereotypeFig());
        addFig(getNameFig());

        //setBlinkPorts(false); //make port invisible unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigStateVertex#initNotationProviders(java.lang.Object)
     */
    @Override
    protected void initNotationProviders(Object own) {
        if (notationProvider != null) {
            notationProvider.cleanListener(this, own);
        }
        super.initNotationProviders(own);
        NotationName notationName = Notation.findNotation(getNotationSettings()
                .getNotationLanguage());
        if (Model.getFacade().isAActionState(own)) {
            notationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        getNotationProviderType(), own, this, notationName);
        }
    }
    
    /**
     * Overrule this for subclasses of the FigActionState 
     * that need a different NotationProvider.
     * 
     * @return the type of the notation provider
     */
    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_ACTIONSTATE;
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigActionState figClone = (FigActionState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRRect) it.next());
        figClone.cover = (FigRRect) it.next();
        figClone.setNameFig((FigText) it.next());
        /* TODO: Do we need to clone the stereotype(s)? */
        return figClone;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();

        int w = Math.max(stereoDim.width, nameDim.width) + PADDING * 2;
        /* The stereoDim has height=2, even if it is empty, 
         * hence the -2 below: */
        int h = stereoDim.height - 2 + nameDim.height + PADDING;
        w = Math.max(w, h + 44); // the width needs to be > the height
        return new Dimension(w, h);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     *
     * Override setBounds to keep shapes looking right.
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) {
            return;
        }
        Rectangle oldBounds = getBounds();

        Dimension stereoDim = getStereotypeFig().getMinimumSize();
        Dimension nameDim = getNameFig().getMinimumSize();
        getNameFig().setBounds(x + PADDING, y + stereoDim.height,
                w - PADDING * 2, nameDim.height);
        getStereotypeFig().setBounds(x + PADDING, y,
                w - PADDING * 2, stereoDim.height);
        getBigPort().setBounds(x + 1, y + 1, w - 2, h - 2);
        cover.setBounds(x, y, w, h);
        ((FigRRect) getBigPort()).setCornerRadius(h);
        cover.setCornerRadius(h);

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        cover.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return cover.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        cover.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return cover.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }

    @Override
    public boolean isFilled() {
        return cover.isFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AddAssociationEvent
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            notationProvider.updateListener(this, getOwner(), mee);
            damage();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#removeFromDiagramImpl()
     */
    @Override
    public void removeFromDiagramImpl() {
        if (notationProvider != null) {
            notationProvider.cleanListener(this, getOwner());
        }
        super.removeFromDiagramImpl();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    @Override
    protected void updateNameText() {
        if (notationProvider != null) {
            getNameFig().setText(notationProvider.toString(getOwner(), 
                    getNotationSettings()));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    @Override
    protected void updateStereotypeText() {
        getStereotypeFig().setOwner(getOwner());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEdited(FigText ft) throws PropertyVetoException {
        notationProvider.parse(getOwner(), ft.getText());
        ft.setText(notationProvider.toString(getOwner(), 
                getNotationSettings()));
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProvider.getParsingHelp());
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3526461404860044420L;
}
