// $Id: FigPackage.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;


import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.StereotypeContainer;
import org.argouml.uml.diagram.VisibilityContainer;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.ArgoFigText;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Geometry;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.tigris.gef.undo.UndoableAction;

/**
 * Class to display graphics for a UML package in a class diagram,
 * consisting of a "tab" and a "body". <p>
 * 
 * The tab of the Package Fig is build of 2 pieces: 
 * the stereotypes at the top, and the name below it. <p>
 * 
 * The name box covers the whole tab, i.e. its size
 * is always equal to the total size of the tab. 
 * It is not transparent, and has a line border. 
 * Its text sits at the bottom of the fig, to leave room for stereotypes. <p>
 * 
 * The stereotype fig is transparent, and sits at the top
 * inside the name fig. It is drawn on top of the name fig box. <p>
 * 
 * The tab of the Package Fig can only be resized by the user horizontally.
 * The body can be resized horizontally and vertically by the user. <p>
 * 
 * Double clicking on the body has a special consequence: 
 * the user is asked if he wants to create a new class diagram
 * for this package. <p>
 * 
 * ArgoUML does not support the option of showing the name 
 * of the package in the body, 
 * as described in the UML standard (chapter Notation - Package). <p>
 * 
 * Neither does ArgoUML currently support showing properties in the tab, 
 * see issue 1214. <p>
 * 
 * In front of the name, ArgoUML may optionally show the visibility.
 */
public class FigPackage extends FigNodeModelElement
    implements StereotypeContainer, VisibilityContainer {
    
    /** The minimal height of the name. */
    private static final int MIN_HEIGHT = 21;
    /** The minimal width of the name. */
    private static final int MIN_WIDTH = 50;

    /** The initial width of the outer box. */
    private int width = 140;
    /** The initial height of the outer box. */
    private int height = 100;
    
    /** The width of the cut out area at the right top corner. */
    private int indentX = 50;

    private int textH = 20;

    /**
     * The total height of the tab.
     */
    private int tabHeight = 20;

    private FigText body;

    /**
     * Flag that indicates if the user wants any stereotype to be shown. 
     * It corresponds to the check-mark on the Presentation tab.
     * There is no relation with the actual presence of any stereotypes.
     * This setting has Fig-scope, hence it is saved with the Fig layout data.
     */
    private boolean stereotypeVisible = true;

    /**
     * The main constructor.
     *
     * @param node the UML package
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @deprecated for 0.27.3 by tfmorris. Use
     *             {@link #FigPackage(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigPackage(Object node, int x, int y) {
        // Create a Body that reacts to double-clicks and jumps to a diagram.
        body = new FigPackageFigText(0, textH, width, height - textH);

        setOwner(node);

        initialize();

        setLocation(x, y);
    }

    private void initialize() {
        body.setEditable(false);
        
        setBigPort(
            new PackagePortFigRect(0, 0, width, height, indentX, tabHeight));

        getNameFig().setBounds(0, 0, width - indentX, textH + 2);
        getNameFig().setJustification(FigText.JUSTIFY_LEFT);

        getBigPort().setFilled(false);
        getBigPort().setLineWidth(0);

        // Set properties of the stereotype box. 
        // Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.

        getStereotypeFig().setVisible(false);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(getNameFig());
        addFig(getStereotypeFig());
        addFig(body);

        setBlinkPorts(false); //make port invisible unless mouse enters

        // Make all the parts match the main fig
        setFilled(true);
        setFillColor(FILL_COLOR);
        setLineColor(LINE_COLOR);
        setLineWidth(LINE_WIDTH);

        updateEdges();
    }

    /**
     * Constructor that hooks the fig into the UML element.
     * 
     * @param gm ignored
     * @param node the UML element
     * @deprecated for 0.27.3 by tfmorris. Use
     *             {@link #FigPackage(Object, Rectangle, DiagramSettings)}.
     */
    @Deprecated
    public FigPackage(@SuppressWarnings("unused") GraphModel gm, Object node) {
        this(node, 0, 0);
    }

    /**
     * Construct a package figure with the given owner, bounds, and rendering
     * settings. This constructor is used by the PGML parser.
     * 
     * @param owner owning model element
     * @param bounds position and size or null if fig hasn't been placed
     * @param settings rendering settings
     */
    public FigPackage(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);

        // Create a Body that reacts to double-clicks and jumps to a diagram.
        body = new FigPackageFigText(getOwner(), 
                new Rectangle(0, textH, width, height - textH), getSettings());
        
        initialize();
        
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }
        setBounds(getBounds());
    }

    /*
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        FigPackage figClone = (FigPackage) super.clone();
        for (Fig thisFig : (List<Fig>) getFigs()) {
            if (thisFig == body) {
                figClone.body = (FigText) thisFig;
            }
        }
        return figClone;
    }


    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    @Override
    public void setLineColor(Color col) {
        super.setLineColor(col);
        getStereotypeFig().setLineColor(null);
        getNameFig().setLineColor(col);
        body.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    @Override
    public Color getLineColor() {
        return body.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    @Override
    public void setFillColor(Color col) {
        super.setFillColor(col);
        getStereotypeFig().setFillColor(null);
        getNameFig().setFillColor(col);
        body.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    @Override
    public Color getFillColor() {
        return body.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    @Override
    public void setFilled(boolean f) {
        getStereotypeFig().setFilled(false);
        getNameFig().setFilled(f);
        body.setFilled(f);
    }

    
    @Override
    public boolean isFilled() {
        return body.isFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    @Override
    public void setLineWidth(int w) {
        // There are 2 boxes showing lines: the tab and the body.
        getNameFig().setLineWidth(w);
        body.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return body.getLineWidth();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    @Override
    protected void updateStereotypeText() {
        Object modelElement = getOwner();

        if (modelElement == null) {
            return;
        }

        Rectangle rect = getBounds();

        /* check if any stereotype is defined */
        if (Model.getFacade().getStereotypes(modelElement).isEmpty()) {
            if (getStereotypeFig().isVisible()) {
                getNameFig().setTopMargin(0);
                getStereotypeFig().setVisible(false);
            } // else nothing changed
        } else {
            /* we got at least one stereotype */
            /* This populates the stereotypes area: */
            super.updateStereotypeText();
            if (!isStereotypeVisible()) {
                // the user wants to hide them
                getNameFig().setTopMargin(0);
                getStereotypeFig().setVisible(false);
            } else if (!getStereotypeFig().isVisible()) {
                getNameFig().setTopMargin(
                        getStereotypeFig().getMinimumSize().height);
                getStereotypeFig().setVisible(true);
            } // else nothing changed
        }

        forceRepaintShadow();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    @Override
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "stereotypeVisible=" + isStereotypeVisible()
                + ";"
                + "visibilityVisible=" + isVisibilityVisible();
    }


    /*
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    @Override
    public boolean getUseTrapRect() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    @Override
    public Dimension getMinimumSize() {
        // Use "aSize" to build up the minimum size. Start with the size of the
        // name fig and build up.
        Dimension aSize = new Dimension(getNameFig().getMinimumSize());
        aSize.height = Math.max(aSize.height, MIN_HEIGHT);
        aSize.width = Math.max(aSize.width, MIN_WIDTH);

        // If we have any number of stereotypes displayed, then allow 
        // some space for that (only width, height is included in nameFig):
        if (isStereotypeVisible()) {
            Dimension st = getStereotypeFig().getMinimumSize();
            aSize.width =
		Math.max(aSize.width, st.width);
        }

        // take into account the tab is not as wide as the body:
        aSize.width += indentX + 1; 

        // we want at least some of the package body to be displayed
        aSize.height += 30;

        // And now aSize has the answer
        return aSize;
    }

    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}.<p>
     *
     * If the required height is bigger, then the additional height is
     * not distributed among all figs (i.e. compartments), 
     * but goes into the body. Hence, the
     * accumulated height of all visible figs equals the demanded height<p>.
     *
     * Some of this has "magic numbers" hardcoded in. In particular there is
     * a knowledge that the minimum height of a name compartment is 21
     * pixels. This height is needed to be able to display the "Clarifier"
     * inside the name compartment.
     *
     * @param xa  Desired X coordinate of upper left corner
     *
     * @param ya  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the FigClass
     *
     * @param h  Desired height of the FigClass
     */
    @Override
    protected void setStandardBounds(int xa, int ya, int w, int h) {
        // Save our old boundaries (needed later), and get minimum size
        // info. "aSize" will be used to maintain a running calculation of our
        // size at various points.

        Rectangle oldBounds = getBounds();

        // The new size can not be smaller than the minimum.
        Dimension minimumSize = getMinimumSize();
        int newW = Math.max(w, minimumSize.width);
        int newH = Math.max(h, minimumSize.height);

        // Now resize all sub-figs, including not displayed figs. Start by the
        // name. We override the getMinimumSize if it is less than our view (21
        // pixels hardcoded!). Add in the shared extra, plus in this case the
        // correction.

        Dimension nameMin = getNameFig().getMinimumSize();
        int minNameHeight = Math.max(nameMin.height, MIN_HEIGHT);

        // Now sort out the stereotype display. If the stereotype is displayed,
        // move the upper boundary of the name compartment up and set new
        // bounds for the name and the stereotype compartments and the
        // stereoLineBlinder that blanks out the line between the two

        int currentY = ya;

        int tabWidth = newW - indentX;

        if (isStereotypeVisible()) {
            Dimension stereoMin = getStereotypeFig().getMinimumSize();
            getNameFig().setTopMargin(stereoMin.height);
            getNameFig().setBounds(xa, currentY, tabWidth + 1, minNameHeight);

            getStereotypeFig().setBounds(xa, ya,
                tabWidth, stereoMin.height + 1);

            if (tabWidth < stereoMin.width + 1) {
                tabWidth = stereoMin.width + 2;
            }
        } else {
            getNameFig().setBounds(xa, currentY, tabWidth + 1, minNameHeight);
        }
        
        // Advance currentY to where the start of the body box is,
        // remembering that it overlaps the next box by 1 pixel. Calculate the
        // size of the body box, and update the Y pointer past it if it is
        // displayed.

        currentY += minNameHeight - 1; // -1 for 1 pixel overlap
        body.setBounds(xa, currentY, newW, newH + ya - currentY);

        tabHeight = currentY - ya;
        // set bounds of big box

        getBigPort().setBounds(xa, ya, newW, newH);

        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Package.
     *
     * @param     me     a mouse event
     * @return          a collection of menu items
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Modifier ...
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                buildModifierPopUp(ABSTRACT | LEAF | ROOT));

        // Visibility ...
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                buildVisibilityPopUp());

        return popUpActions;
    }

    @Override
    protected ArgoJMenu buildShowPopUp() {
        ArgoJMenu showMenu = super.buildShowPopUp();
        /* Only show the menuitems if they make sense: */
        Editor ce = Globals.curEditor();
        List<Fig> figs = ce.getSelectionManager().getFigs();
        boolean sOn = false;
        boolean sOff = false;
        boolean vOn = false;
        boolean vOff = false;
        for (Fig f : figs) {
            if (f instanceof StereotypeContainer) {
                boolean v = ((StereotypeContainer) f).isStereotypeVisible();
                if (v) {
                    sOn = true;
                } else {
                    sOff = true;
                }
                v = ((VisibilityContainer) f).isVisibilityVisible();
                if (v) {
                    vOn = true;
                } else {
                    vOff = true;
                }
            }
        }

        if (sOn) {
            showMenu.add(new HideStereotypeAction());
        }

        if (sOff) {
            showMenu.add(new ShowStereotypeAction());
        }

        if (vOn) {
            showMenu.add(new HideVisibilityAction());
        }

        if (vOff) {
            showMenu.add(new ShowVisibilityAction());
        }

        return showMenu;
    }

    /**
     * Change the visibility of the stereotypes of all Figs.
     *
     * @param value true if it needs to become visible
     */
    private void doStereotype(boolean value) {
        Editor ce = Globals.curEditor();
        List<Fig> figs = ce.getSelectionManager().getFigs();
        for (Fig f : figs) {
            if (f instanceof StereotypeContainer) {
                ((StereotypeContainer) f).setStereotypeVisible(value);
            }
            if (f instanceof FigNodeModelElement) {
                ((FigNodeModelElement) f).forceRepaintShadow();
                ((ArgoFig) f).renderingChanged();
            }
            f.damage();
        }
    }

    /**
     * Change the visibility of the Visibility of all Figs.
     *
     * @param value true if it needs to become visible
     */
    private void doVisibility(boolean value) {
        Editor ce = Globals.curEditor();
        List<Fig> figs = ce.getSelectionManager().getFigs();
        for (Fig f : figs) {
            if (f instanceof VisibilityContainer) {
                ((VisibilityContainer) f).setVisibilityVisible(value);
            }
            f.damage();
        }
    }

    /**
     * A text fig for the body of a a Package 
     * which does not contain any text, 
     * but solely exists to trigger a jump to a diagram for
     * the named package when double clicked.
     */
    class FigPackageFigText extends ArgoFigText {
        
        /**
         * The constructor.
         * 
         * @param xa
         * @param ya
         * @param w
         * @param h
         * @deprecated 0.27.2 by tfmorris. Use
         * {@link #FigPackageFigText(Object, Rectangle, DiagramSettings)}
         */
        @SuppressWarnings("deprecation")
        @Deprecated
        public FigPackageFigText(int xa, int ya, int w, int h) {
            super(xa, ya, w, h);
        }

        /**
         * Construct a text fig for a Package which will jump to diagram for
         * the named package when double clicked.
         * 
         * @param owner owning UML element
         * @param bounds position and size
         * @param settings render settings
         */
        public FigPackageFigText(Object owner, Rectangle bounds, 
                DiagramSettings settings) {
            super(owner, bounds, settings, false);
        }
        
	/**
	 * TODO: mvw: Would it not be better if this code 
	 * would go in startTextEditor(), not overruling mouseClicked(). 
	 * But we made this fig not editable, 
	 * to stop it from reacting on key-presses.
	 * Anyhow - this is a hack - abusing a FigText - GEF does 
	 * not really support double-clicking on a Fig to trigger some action.
	 */
	@Override
	public void mouseClicked(MouseEvent me) {

	    String lsDefaultName = "main";

	    // TODO: This code appears to be designed to jump to the diagram
	    // containing the contents of the package that was double clicked
	    // but it looks like it's always searching for the name "main"
	    // instead of the package name.
	    // TODO: But in any case, it should be delegating this work to 
	    // to something that knows about the diagrams and they contents -tfm
	    if (me.getClickCount() >= 2) {
		Object lPkg = FigPackage.this.getOwner();
		if (lPkg != null) {
		    Object lNS = lPkg;

		    Project lP = getProject();

		    List<ArgoDiagram> diags = lP.getDiagramList();
		    ArgoDiagram lFirst = null;
		    for (ArgoDiagram lDiagram : diags) {
			Object lDiagramNS = lDiagram.getNamespace();
			if ((lNS == null && lDiagramNS == null)
			    || (lNS.equals(lDiagramNS))) {
			    /* save first */
			    if (lFirst == null) {
				lFirst = lDiagram;
			    }

			    if (lDiagram.getName() != null
				&& lDiagram.getName().startsWith(
				        lsDefaultName)) {
			        me.consume();
				super.mouseClicked(me);
				TargetManager.getInstance().setTarget(lDiagram);
				return;
			    }
			}
		    } /*while*/

		    /* If we get here then we didn't get the
		     * default diagram.
                     */
		    if (lFirst != null) {
			me.consume();
			super.mouseClicked(me);

			TargetManager.getInstance().setTarget(lFirst);
			return;
		    }

		    /* Try to create a new class diagram.
                     */
		    me.consume();
		    super.mouseClicked(me);
		    try {
		        createClassDiagram(lNS, lsDefaultName, lP);
		    } catch (Exception ex) {
		        
		    }

		    return;

		} /*if package */
	    } /* if doubleclicks */
	    super.mouseClicked(me);
	}

        /**
         * The UID.
         */
        private static final long serialVersionUID = -1355316218065323634L;
    }

    private void createClassDiagram(
            Object namespace,
            String defaultName,
            Project project) throws PropertyVetoException {

        String namespaceDescr;
        if (namespace != null
                && Model.getFacade().getName(namespace) != null) {
            namespaceDescr = Model.getFacade().getName(namespace);
        } else {
            namespaceDescr = Translator.localize("misc.name.anon");
        }

        String dialogText = "Add new class diagram to " + namespaceDescr + "?";
        int option =
            JOptionPane.showConfirmDialog(
                null,
                dialogText,
                "Add new class diagram?",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {

            ArgoDiagram classDiagram =
                DiagramFactory.getInstance().
                    createDiagram(DiagramType.Class, namespace, null);

            String diagramName = defaultName + "_" + classDiagram.getName();

            project.addMember(classDiagram);

            TargetManager.getInstance().setTarget(classDiagram);
            /* change prefix */
            classDiagram.setName(diagramName);
            ExplorerEventAdaptor.getInstance().structureChanged();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.StereotypeContainer#isStereotypeVisible()
     */
    public boolean isStereotypeVisible() {
        return stereotypeVisible;
    }

    /*
     * @see org.argouml.uml.diagram.ui.StereotypeContainer#setStereotypeVisible(boolean)
     */
    public void setStereotypeVisible(boolean isVisible) {
        stereotypeVisible = isVisible;
        renderingChanged();
        damage();
    }

    /*
     * @see org.argouml.uml.diagram.ui.VisibilityContainer#isVisibilityVisible()
     */
    public boolean isVisibilityVisible() {
        return getNotationSettings().isShowVisibilities();
    }

    /*
     * @see org.argouml.uml.diagram.ui.VisibilityContainer#setVisibilityVisible(boolean)
     */
    public void setVisibilityVisible(boolean isVisible) {
        getNotationSettings().setShowVisibilities(isVisible);
        renderingChanged();
        damage();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    @Override
    protected void textEditStarted(FigText ft) {

        if (ft == getNameFig()) {
            showHelp("parsing.help.fig-package");
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getClosestPoint(java.awt.Point)
     */
    @Override
    public Point getClosestPoint(Point anotherPt) {
        Rectangle r = getBounds();
        int[] xs = {
            r.x, r.x + r.width - indentX, r.x + r.width - indentX,
            r.x + r.width,   r.x + r.width,  r.x,            r.x,
        };
        int[] ys = {
            r.y, r.y,                     r.y + tabHeight,
            r.y + tabHeight, r.y + r.height, r.y + r.height, r.y,
        };
        Point p =
            Geometry.ptClosestTo(
                xs,
                ys,
                7,
                anotherPt);
        return p;
    }
    
    @Override
    protected void modelChanged(PropertyChangeEvent mee) {
	
	if (mee instanceof RemoveAssociationEvent
		&& "ownedElement".equals(mee.getPropertyName())
		&& mee.getSource() == getOwner()) {
	    // A model element has been removed from this packages namespace
	    // If the Fig representing that model element is on the same
	    // diagram as this package then make sure it is not enclosed by
	    // this package.
	    // TODO: In my view the Fig representing the model element should be
	    // removed from the diagram. Yet to be agreed. Bob.
	    
	    
	    LayerPerspective layer = (LayerPerspective) getLayer();
	    Fig f = layer.presentationFor(mee.getOldValue());
	    if (f != null && f.getEnclosingFig() == this) {
		removeEnclosedFig(f);
		f.setEnclosingFig(null);
	    }
	}
	super.modelChanged(mee);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3617092272529451041L;

    private class HideStereotypeAction extends UndoableAction {
        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY =
            "menu.popup.show.hide-stereotype";

        /**
         * Constructor.
         */
        HideStereotypeAction() {
            super(Translator.localize(ACTION_KEY),
                    ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
        }
        /*
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            doStereotype(false);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID =
            1999499813643610674L;
    }

    private class ShowStereotypeAction extends UndoableAction {
        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY =
            "menu.popup.show.show-stereotype";

        /**
         * Constructor.
         */
        ShowStereotypeAction() {
            super(Translator.localize(ACTION_KEY),
                    ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
        }
        /*
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            doStereotype(true);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID =
            -4327161642276705610L;
    }

    private class HideVisibilityAction extends UndoableAction {
        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY =
            "menu.popup.show.hide-visibility";

        /**
         * Constructor.
         */
        HideVisibilityAction() {
            super(Translator.localize(ACTION_KEY),
                    ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
        }
        /*
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            doVisibility(false);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID =
            8574809709777267866L;
    }

    private class ShowVisibilityAction extends UndoableAction {
        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY =
            "menu.popup.show.show-visibility";

        /**
         * Constructor.
         */
        ShowVisibilityAction() {
            super(Translator.localize(ACTION_KEY),
                    ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
        }
        /*
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            doVisibility(true);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID =
            7722093402948975834L;
    }

} /* end class FigPackage */

/**
 * The bigport needs to overrule the getClosestPoint,
 * because it is the port of this FigNode.
 *
 * @author mvw@tigris.org
 */
class PackagePortFigRect extends FigRect {
    private int indentX;
    private int tabHeight;

    /**
     * The constructor.
     *
     * @param x The x.
     * @param y The y.
     * @param w The width.
     * @param h The height.
     * @param ix The indent.
     * @param th The tab height.
     */
    public PackagePortFigRect(int x, int y, int w, int h, int ix, int th) {
        super(x, y, w, h, null, null);
        this.indentX = ix;
        tabHeight = th;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getClosestPoint(java.awt.Point)
     */
    @Override
    public Point getClosestPoint(Point anotherPt) {
        Rectangle r = getBounds();
        int[] xs = {
            r.x, r.x + r.width - indentX, r.x + r.width - indentX,
            r.x + r.width,   r.x + r.width,  r.x,            r.x,
        };
        int[] ys = {
            r.y, r.y,                     r.y + tabHeight,
            r.y + tabHeight, r.y + r.height, r.y + r.height, r.y,
        };
        Point p =
            Geometry.ptClosestTo(
                xs,
                ys,
                7,
                anotherPt);
        return p;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7083102131363598065L;
}
