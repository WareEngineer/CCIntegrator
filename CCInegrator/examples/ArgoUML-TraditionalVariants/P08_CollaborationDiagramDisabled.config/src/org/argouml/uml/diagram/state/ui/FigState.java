

// $Id: FigState.java 121 2010-09-24 00:46:00Z marcusvnac $
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

package org.argouml.uml.diagram.state.ui;

import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;

/**
 * The fig hierarchy should comply as much as possible to the hierarchy of the
 * UML metamodel. Reason for this is to make sure that events from the model are
 * not missed by the figs. The hierarchy of the states was not compliant to
 * this. This resulted in a number of issues (issue 1430 for example). Therefore
 * introduced an abstract FigState and made FigCompositeState and FigSimpleState
 * subclasses of this state.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Dec 30, 2002
 */
public abstract class FigState extends FigStateVertex {

    protected static final int SPACE_TOP = 0;
    protected static final int SPACE_MIDDLE = 0;
    protected static final int DIVIDER_Y = 0;
    protected static final int SPACE_BOTTOM = 6;

    protected static final int MARGIN = 2;

    protected NotationProvider notationProviderBody;

    /**
     * The text inside the state.
     */
    private FigText internal;

    /**
     * Constructor for FigState.
     * 
     * @deprecated for 0.27.3 by mvw.  Use 
     * {@link #FigState(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigState() {
        super();
        initializeState();
    }

    /**
     * Constructor for FigState, used when an UML elm already exists.
     *
     * @param gm ignored
     * @param node the UML element
     * 
     * @deprecated for 0.27.3 by mvw.  Use 
     * {@link #FigState(Object, Rectangle, DiagramSettings)}.
     */
    @Deprecated
    public FigState(@SuppressWarnings("unused") GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * Constructor used by PGML parser.
     * 
     * @param owner the owning UML element
     * @param bounds rectangle describing bounds
     * @param settings rendering settings
     */
    public FigState(Object owner, Rectangle bounds, DiagramSettings settings) {
        super(owner, bounds, settings);

        initializeState();

        NotationName notation = Notation.findNotation(
                getNotationSettings().getNotationLanguage());
        notationProviderBody =
            NotationProviderFactory2.getInstance().getNotationProvider(
                    NotationProviderFactory2.TYPE_STATEBODY, getOwner(), this, 
                    notation);
    }

    private void initializeState() {
        // TODO: Get rid of magic numbers!  Figure out which represent line
        // widths vs padding vs offsets
        setBigPort(new FigRRect(getInitialX() + 1, getInitialY() + 1,
                getInitialWidth() - 2, getInitialHeight() - 2,
                DEBUG_COLOR, DEBUG_COLOR));
        getNameFig().setLineWidth(0);
        getNameFig().setBounds(getInitialX() + 2, getInitialY() + 2,
                       getInitialWidth() - 4,
                       getNameFig().getBounds().height);
        getNameFig().setFilled(false);

        internal =
            new FigText(getInitialX() + 2,
                    getInitialY() + 2 + NAME_FIG_HEIGHT + 4,
                    getInitialWidth() - 4,
                    getInitialHeight() 
                    - (getInitialY() + 2 + NAME_FIG_HEIGHT + 4));
        internal.setFont(getSettings().getFont(Font.PLAIN));
        internal.setTextColor(TEXT_COLOR);
        internal.setLineWidth(0);
        internal.setFilled(false);
        internal.setExpandOnly(true);
        internal.setReturnAction(FigText.INSERT);
        internal.setJustification(FigText.JUSTIFY_LEFT);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public void setOwner(Object newOwner) {
        super.setOwner(newOwner);
        renderingChanged();
    }

    /*
     * @see org.argouml.uml.diagram.state.ui.FigStateVertex#initNotationProviders(java.lang.Object)
     */
    @Override
    protected void initNotationProviders(Object own) {
        if (notationProviderBody != null) {
            notationProviderBody.cleanListener(this, own);
        }
        super.initNotationProviders(own);
        NotationName notation = Notation.findNotation(
                getNotationSettings().getNotationLanguage());

        if (Model.getFacade().isAState(own)) {
            notationProviderBody =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_STATEBODY, own, this, 
                        notation);
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            notationProviderBody.updateListener(this, getOwner(), mee);
            damage();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#removeFromDiagramImpl()
     */
    @Override
    public void removeFromDiagramImpl() {
        if (notationProviderBody != null) {
            notationProviderBody.cleanListener(this, getOwner());
        }
        super.removeFromDiagramImpl();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    @Override
    public void renderingChanged() {
        super.renderingChanged();
        Object state = getOwner();
        if (state == null) {
            return;
        }
        if (notationProviderBody != null) {
            internal.setText(notationProviderBody.toString(getOwner(), 
                    getNotationSettings()));
        }
        calcBounds();
        setBounds(getBounds());
    }

    /**
     * @return the initial X
     */
    protected abstract int getInitialX();

    /**
     * @return the initial Y
     */
    protected abstract int getInitialY();

    /**
     * @return the initial width
     */
    protected abstract int getInitialWidth();

    /**
     * @return the initial height
     */
    protected abstract int getInitialHeight();

    /**
     * @param theInternal The internal to set.
     */
    protected void setInternal(FigText theInternal) {
        this.internal = theInternal;
    }

    /**
     * @return Returns the internal.
     */
    protected FigText getInternal() {
        return internal;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {
        super.textEditStarted(ft);
        if (ft == internal) {
            showHelp(notationProviderBody.getParsingHelp());
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    @Override
    public void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == getInternal()) {
            Object st = getOwner();
            if (st == null) {
                return;
            }
            notationProviderBody.parse(getOwner(), ft.getText());
            ft.setText(notationProviderBody.toString(getOwner(), 
                    getNotationSettings()));
        }
    }

    /**
     * 
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateFont()
     */
    @Override
    protected void updateFont() {
        super.updateFont();
        Font f = getSettings().getFont(Font.PLAIN);
        internal.setFont(f);
    }

}
