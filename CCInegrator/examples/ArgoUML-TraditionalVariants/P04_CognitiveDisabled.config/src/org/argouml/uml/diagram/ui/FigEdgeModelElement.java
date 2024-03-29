// $Id: FigEdgeModelElement.java 132 2010-09-26 23:32:33Z marcusvnac $
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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;


import org.apache.log4j.Logger;

import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoDiagramAppearanceEventListener;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;

import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.Project;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.DiElement;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.Clarifier;
import org.argouml.ui.ProjectActions;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Selection;
import org.tigris.gef.persistence.pgml.PgmlUtility;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigEdgePoly;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;

/**
 * Abstract class to display diagram lines (edges) for UML ModelElements that
 * look like lines.
 * This Fig is prepared to show a (possibly editable) name,
 * and/or multiple stereotypes.
 * <p>
 * NOTE: This will drop the ArgoNotationEventListener and 
 * ArgoDiagramAppearanceEventListener
 * interfaces in the next release.  The corresponding methods have been marked
 * as deprecated.
 */
public abstract class FigEdgeModelElement
    extends FigEdgePoly
    implements
        VetoableChangeListener,
        DelayedVChangeListener,
        MouseListener,
        KeyListener,
        PropertyChangeListener,
        ArgoNotationEventListener,
        ArgoDiagramAppearanceEventListener,
        
        IItemUID,
        ArgoFig,
        Clarifiable {
    
    private static final Logger LOG =
        Logger.getLogger(FigEdgeModelElement.class);
    
    private DiElement diElement = null;

    /**
     * Set the removeFromDiagram to false if this edge may not
     * be removed from the diagram.
     */
    private boolean removeFromDiagram = true;

    /**
     * Offset from the end of the set of popup actions at which new items
     * should be inserted by concrete figures.
    **/
    private static int popupAddOffset;

    private NotationProvider notationProviderName;
    
    @Deprecated
    private HashMap<String, Object> npArguments;

    /**
     * The Fig that displays the name of this model element.
     * Use getNameFig(), no setter should be required.
     */
    private FigText nameFig;

    /**
     * Use getStereotypeFig(), no setter should be required.
     */
    private FigStereotypesGroup stereotypeFig;

    private FigEdgePort edgePort;

    private ItemUID itemUid;

    /*
     * List of model element listeners we've registered.
     */
    private Set<Object[]> listeners = new HashSet<Object[]>();

    private DiagramSettings settings;
    
    /**
     * Construct a default FigEdgeElement.
     * 
     * @deprecated for 0.27.2 by tfmorris. The default constructor will become
     *             private so that it can't be used externally. All concrete
     *             subclasses must invoke an explicit constructor which passes
     *             an owner and render s settings
     *             {@link #FigEdgeModelElement(Object, DiagramSettings)}.
     */
    @Deprecated
    public FigEdgeModelElement() {
        nameFig = new FigNameWithAbstract(X0, Y0 + 20, 90, 20, false);
        stereotypeFig = new FigStereotypesGroup(X0, Y0, 90, 15);
        initFigs();
    }
    
    /**
     * Construct a new FigEdge. This method creates the name element that holds
     * the name of the model element and adds itself as a listener. Also a
     * stereotype is constructed.
     * <p>
     * This constructor is only intended for use by concrete subclasses.
     * 
     * @param element owning uml element
     * @param renderSettings rendering settings
     */
    protected FigEdgeModelElement(Object element, 
            DiagramSettings renderSettings) {
        super();
        // TODO: We don't have any settings that can change per-fig currently
        // so we can just use the default settings;
//        settings = new DiagramSettings(renderSettings);
        settings = renderSettings;
        
        // TODO: It doesn't matter what these get set to because GEF can't 
        // draw anything except 1 pixel wide lines
        super.setLineColor(LINE_COLOR);
        super.setLineWidth(LINE_WIDTH);
        getFig().setLineColor(LINE_COLOR);
        getFig().setLineWidth(LINE_WIDTH);
        
        nameFig = new FigNameWithAbstract(element, 
                new Rectangle(X0, Y0 + 20, 90, 20), 
                renderSettings, false);
        stereotypeFig = new FigStereotypesGroup(element, 
                new Rectangle(X0, Y0, 90, 15),
                settings);
      
        initFigs();
        initOwner(element);
    }



    private void initFigs() {
        nameFig.setTextFilled(false);
        setBetweenNearestPoints(true);
    }
    
    
    private void initOwner(Object element) {
        if (element != null) {
            if (!Model.getFacade().isAUMLElement(element)) {
                throw new IllegalArgumentException(
                        "The owner must be a model element - got a "
                        + element.getClass().getName());
            }
            super.setOwner(element);
            nameFig.setOwner(element); // for setting abstract
            if (edgePort != null) {
                edgePort.setOwner(getOwner());
            }
            stereotypeFig.setOwner(element); // this fixes issue 5414
            notationProviderName =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        getNotationProviderType(), element, this);

            addElementListener(element, "remove");
        }
    }


    /**
     * The constructor that hooks the Fig into the UML model element.
     *
     * @param edge the UML element
     * @deprecated for 0.27.2 by tfmorris.  Use 
     * {@link #FigEdgeModelElement(Object, DiagramSettings)}.
     */
    @Deprecated
    public FigEdgeModelElement(Object edge) {
        this();
        setOwner(edge);
    }

    /**
     * Create a FigCommentPort if needed
     */
    public void makeEdgePort() {
        if (edgePort == null) {
            edgePort = new FigEdgePort(getOwner(), new Rectangle(), 
                    getSettings());
            edgePort.setVisible(false);
            addPathItem(edgePort,
                    new PathItemPlacement(this, edgePort, 50, 0));
        }
    }

    /**
     * @return the FigCommentPort
     */
    public FigEdgePort getEdgePort() {
        return edgePort;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * Setter for the UID
     * @param newId the new UID
     */
    public void setItemUID(ItemUID newId) {
        itemUid = newId;
    }

    /**
     * Getter for the UID
     * @return the UID
     */
    public ItemUID getItemUID() {
        return itemUid;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getTipString(java.awt.event.MouseEvent)
     */
    @Override
    public String getTipString(MouseEvent me) {
        
        String tip = "";
        
            if (getOwner() != null) {
            try {
                tip = Model.getFacade().getTipString(getOwner());
            } catch (InvalidElementException e) {
                // We moused over an object just as it was deleted
                // transient condition - doesn't require I18N
                
                LOG.warn("A deleted element still exists on the diagram");
                
                return Translator.localize("misc.name.deleted");
            }
        } else {
            tip = toString();
        }

        if (tip != null && tip.length() > 0 && !tip.endsWith(" ")) {
            tip += " ";
        }
        return tip;
    }

    /**
     * @param me the MouseEvent that triggered the popup menu request
     * @return a Vector containing a combination of these 4 types: Action,
     *         JMenu, JMenuItem, JSeparator.
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
        ActionList popUpActions =
            new ActionList(super.getPopUpActions(me), isReadOnly());
        
        // popupAddOffset should be equal to the number of items added here:
        popUpActions.add(new JSeparator());
        popupAddOffset = 1;
        if (removeFromDiagram) {
            popUpActions.add(
                    ProjectActions.getInstance().getRemoveFromDiagramAction());
            popupAddOffset++;
        }
        popUpActions.add(new ActionDeleteModelElements());
        popupAddOffset++;

        if (TargetManager.getInstance().getTargets().size() == 1) {
            
            
            // Add stereotypes submenu
            Action[] stereoActions = getApplyStereotypeActions();
            if (stereoActions != null && stereoActions.length > 0) {
                popUpActions.add(0, new JSeparator());
                ArgoJMenu stereotypes = new ArgoJMenu(
                        "menu.popup.apply-stereotypes");
                for (int i = 0; i < stereoActions.length; ++i) {
                    stereotypes.addCheckItem(stereoActions[i]);
                }
                popUpActions.add(0, stereotypes);
            }
        }
        return popUpActions;
    }
    
    /**
     * Get the set of Actions which valid for adding/removing
     * Stereotypes on the ModelElement of this Fig's owner.
     *  
     * @return array of Actions 
     */
    protected Action[] getApplyStereotypeActions() {
        return StereotypeUtility.getApplyStereotypeActions(getOwner());
    }

    /**
     * distance formula: (x-h)^2 + (y-k)^2 = distance^2
     *
     * @param p1 point
     * @param p2 point
     * @return the square of the distance
     */
    protected int getSquaredDistance(Point p1, Point p2) {
        int xSquared = p2.x - p1.x;
        xSquared *= xSquared;
        int ySquared = p2.y - p1.y;
        ySquared *= ySquared;
        return xSquared + ySquared;
    }

    /**
     * @param g the <code>Graphics</code> object
     */
    public void paintClarifiers(Graphics g) {
        
    }

    /**
     * This is used to draw a box round the edge of any editable FigText
     * annotations of the edge when the edge is selected.
     * TODO: This logic probably belongs in our base selection class
     * SelectionEdgeClarifiers and could be written to discover what FigText
     * annotations exist rather than hard code in subclasses.
     * @param f the fig to indicate the bounds of
     * @param g the graphics
     */
    protected void indicateBounds(FigText f, Graphics g) {
        // No longer necessary, see issue 1048.
    }
    
    /**
     * @return a {@link SelectionRerouteEdge} object that manages selection and
     *         rerouting of the edge.
     * 
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        // TODO: There is a cyclic dependency between SelectionRerouteEdge
        // and FigEdgeModelElement
        return new SelectionRerouteEdge(this);
    }

    /**
     * Getter for name, the name Fig
     * @return the nameFig
     */
    protected FigText getNameFig() {
        return nameFig;
    }
    
    /**
     * Get the Rectangle in which the model elements name is displayed
     *
     * @return the bounds of the namefig
     */
    public Rectangle getNameBounds() {
        return nameFig.getBounds();
    }
    
    /**
     * @return the text of the namefig
     */
    public String getName() {
        return nameFig.getText();
    }

    /**
     * Getter for stereo, the stereotype Fig
     * @return the stereo Fig
     */
    protected FigStereotypesGroup getStereotypeFig() {
        return stereotypeFig;
    }

    /*
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
        Object src = pce.getSource();
        if (src == getOwner()) {
            DelayedChangeNotify delayedNotify =
                new DelayedChangeNotify(this, pce);
            SwingUtilities.invokeLater(delayedNotify);
        }
    }

    /*
     * @see org.argouml.kernel.DelayedVChangeListener#delayedVetoableChange(java.beans.PropertyChangeEvent)
     */
    public void delayedVetoableChange(PropertyChangeEvent pce) {
        // update any text, colors, fonts, etc.
        renderingChanged();
        // update the relative sizes and positions of internel Figs
        Rectangle bbox = getBounds();
        setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
        endTrans();
    }

    /**
     * This method gets called when a bound property gets changed. This may
     * represent a UML element value from the Model subsystem, a GEF property,
     * or something which ArgoUML itself implements.
     * 
     * @param pve the event containing the property change information
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent pve) {
        Object src = pve.getSource();
        String pName = pve.getPropertyName();
        if (pve instanceof DeleteInstanceEvent && src == getOwner()) {
            Runnable doWorkRunnable = new Runnable() {
                public void run() {
                    try {
                        removeFromDiagram();
                    } catch (InvalidElementException e) {
                        
                        LOG.error("updateLayout method accessed "
                                    + "deleted element", e);
                        
                    }
                }  
            };
            SwingUtilities.invokeLater(doWorkRunnable);
            return;
        }
        // We handle and consume editing events
        if (pName.equals("editing")
                && Boolean.FALSE.equals(pve.getNewValue())) {
            
            LOG.debug("finished editing");
            
            // parse the text that was edited
            textEdited((FigText) src);
            calcBounds();
            endTrans();
        } else if (pName.equals("editing")
                && Boolean.TRUE.equals(pve.getNewValue())) {
            textEditStarted((FigText) src);
        } else {
            // Pass everything except editing events to superclass
            super.propertyChange(pve);
        }

        if (Model.getFacade().isAUMLElement(src) 
                && getOwner() != null
                && !Model.getUmlFactory().isRemoved(getOwner())) {
            /* If the source of the event is an UML object,
             * then the UML model has been changed.*/
            modelChanged(pve);
            
            final UmlChangeEvent event = (UmlChangeEvent) pve;
            
            Runnable doWorkRunnable = new Runnable() {
                public void run() {
                    try {
                        updateLayout(event);
                    } catch (InvalidElementException e) {
                        
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("updateLayout method accessed "
                                    + "deleted element ", e);
                        }
                        
                    }
                }  
            };
            SwingUtilities.invokeLater(doWorkRunnable);
            
        }
        /* The following is a possible future improvement 
         * of the modelChanged() function.
         * Michiel: Propose not to do this to keep architecture stable. */
//        if (pve instanceof AttributeChangeEvent) {
//            modelAttributeChanged((AttributeChangeEvent) pve);
//        } else if (pve instanceof AddAssociationEvent) {
//            modelAssociationAdded((AddAssociationEvent) pve);
//        } else if (pve instanceof RemoveAssociationEvent) {
//            modelAssociationRemoved((RemoveAssociationEvent) pve);
//        }
    }
    
    /**
     * Called whenever we receive an AttributeChangeEvent.
     * 
     * @param ace the event
     */
    protected void modelAttributeChanged(AttributeChangeEvent ace) {
        // Default implementation is to do nothing
    }

    /**
     * Called whenever we receive an AddAssociationEvent.
     * 
     * @param aae the event
     */
    protected void modelAssociationAdded(AddAssociationEvent aae) {
        // Default implementation is to do nothing        
    }

    /**
     * Called whenever we receive an RemoveAssociationEvent.
     * 
     * @param rae the event
     */
    protected void modelAssociationRemoved(RemoveAssociationEvent rae) {
        // Default implementation is to do nothing
    }
    
    /**
     * This is a template method called by the ArgoUML framework as the result
     * of a change to a model element. Do not call this method directly
     * yourself.
     * <p>Override this in any subclasses in order to restructure the FigNode
     * due to change of any model element that this FigNode is listening to.</p>
     * <p>This method is guaranteed by the framework to be running on the 
     * Swing/AWT thread.</p>
     *
     * @param event the UmlChangeEvent that caused the change
     */
    protected void updateLayout(UmlChangeEvent event) {
    }

    /**
     * This method is called when the user doubleclicked on the text field,
     * and starts editing. Subclasses should override this method to e.g.
     * supply help to the user about the used format. <p>
     *
     * It is also possible to alter the text to be edited
     * already here, e.g. by adding the stereotype in front of the name,
     * but that seems not user-friendly.
     *
     * @param ft the FigText that will be edited and contains the start-text
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProviderName.getParsingHelp());
            ft.setText(notationProviderName.toString(getOwner(), 
                    getNotationSettings()));
        }
    }
    

    /**
     * Utility function to localize the given string with help text,
     * and show it in the status bar of the ArgoUML window.
     * This function is used in favour of the inline call
     * to enable later improvements; e.g. it would be possible to
     * show a help-balloon. TODO: Work this out.
     * One matter to possibly improve: show multiple lines.
     *
     * @param s the given string to be localized and shown
     */
    protected void showHelp(String s) {
        ArgoEventPump.fireEvent(new ArgoHelpEvent(
                ArgoEventTypes.HELP_CHANGED, this,
                Translator.localize(s)));
    }

    /**
     * This method is called after the user finishes editing a text
     * field that is in the FigEdgeModelElement.  Determine which field
     * and update the model.  This class handles the name, subclasses
     * should override to handle other text elements.
     *
     * @param ft the text Fig that has been edited
     */
    protected void textEdited(FigText ft) {
        if (ft == nameFig) {
            if (getOwner() == null) {
                return;
            }
            notationProviderName.parse(getOwner(), ft.getText());
            ft.setText(notationProviderName.toString(getOwner(), 
                    getNotationSettings()));
        }
    }

    /**
     * @param f the Fig
     * @return true if editable
     */
    protected boolean canEdit(Fig f) {
        return true;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers - MouseListener implementation

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        // Required for MouseListener interface, but we only care about clicks
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        // Required for MouseListener interface, but we only care about clicks
    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
        // Required for MouseListener interface, but we only care about clicks
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
        // Required for MouseListener interface, but we only care about clicks
    }

    /*
     * If the user double clicks on any part of this FigNode, pass it
     * down to one of the internal Figs.  This allows the user to
     * initiate direct text editing.
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        if (!me.isConsumed() && !isReadOnly() && me.getClickCount() >= 2) {
            Fig f = hitFig(new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4));
            if (f instanceof MouseListener && canEdit(f)) {
		((MouseListener) f).mouseClicked(me);
            }
        }
        me.consume();
    }
    
    /**
     * Return true if the model element that this Fig represents is read only
     * @return The model element is read only.
     */
    private boolean isReadOnly() {
        Object owner = getOwner();
        if (Model.getFacade().isAUMLElement(owner)) {
            return Model.getModelManagementHelper().isReadOnly(owner);
        }
        return false;
    }

    

    /*
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
        // Required for KeyListener interface, but not used
    }

    /*
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent ke) {
        // Required for KeyListener interface, but not used
    }

    /*
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent ke) {
        if (!ke.isConsumed()
                && !isReadOnly()
                && nameFig != null
                && canEdit(nameFig)) {
            nameFig.keyTyped(ke);
        }
    }


    /**
     * Rerenders the attached elements of the fig. <p>
     * 
     * Warning: The purpose of this function is NOT 
     * to redraw the whole Fig every time
     * something changes. That would be inefficient.<p>
     * 
     * Instead, this function should only be called
     * for major changes that require a complete redraw, 
     * such as change of owner, 
     * and change of notation language. <p>
     * 
     * Overrule this function for subclasses that add extra
     * or remove graphical parts.
     */
    public void renderingChanged() {
        // TODO: This needs to use a different method than that used by the
        // constructor if it wants to allow the method to be overridden
        initNotationProviders(getOwner());
        updateNameText();
        updateStereotypeText();
        damage();
    }
    
    ////////////////////////////////////////////////////////////////
    // internal methods

    /**
     * This is called after any part of the UML ModelElement has
     * changed. This method automatically updates the name FigText.
     * Subclasses should override and update other parts.
     *
     * @param e the event
     */
    protected void modelChanged(PropertyChangeEvent e) {
        if (e instanceof DeleteInstanceEvent) {
            // No need to update if model element went away
            return;
        }
        
        if (e instanceof AssociationChangeEvent 
                || e instanceof AttributeChangeEvent) {
            if (notationProviderName != null) {
                notationProviderName.updateListener(this, getOwner(), e);
                updateNameText();
            }
            updateListeners(getOwner(), getOwner());
        }

        // Update attached node figures
        // TODO: Presumably this should only happen on a add or remove event
        determineFigNodes();
    }
    
    /**
     * generate the notation for the modelelement and stuff it into the text Fig
     */
    protected void updateNameText() {

        if (getOwner() == null) {
            return;
        }
        if (notationProviderName != null) {
            String nameStr = notationProviderName.toString(
                    getOwner(), getNotationSettings());
            nameFig.setText(nameStr);
            updateFont();
            calcBounds();
            setBounds(getBounds());
        }
    }

    /**
     * generate the notation for the stereotype and stuff it into the text Fig
     */
    protected void updateStereotypeText() {
        if (getOwner() == null) {
            return;
        }
        Object modelElement = getOwner();
        stereotypeFig.populate();
    }

    /**
     * This method should only be called once for any one Fig instance that
     * represents a model element (ie not for a FigEdgeNote). It is called
     * either by the constructor that takes a model element as an argument or
     * it is called by PGMLStackParser after it has created the Fig by use of
     * the empty constructor.
     * <p>
     * The assigned model element (owner) must not change during the lifetime of
     * the Fig.
     * 
     * @param owner the model element that this Fig represents.
     * @throws IllegalArgumentException if the owner given is not a model
     *                 element
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     * @deprecated for 0.27.3 by tfmorris.  Owner must be specified in the
     * constructor and can't be changed afterwards.
     */
    @Deprecated
    @Override
    public void setOwner(Object owner) {
        if (owner == null) {
            throw new IllegalArgumentException("An owner must be supplied");
        }
        if (getOwner() != null) {
            throw new IllegalStateException(
                    "The owner cannot be changed once set");
        }
        if (!Model.getFacade().isAUMLElement(owner)) {
            throw new IllegalArgumentException(
                    "The owner must be a model element - got a "
                    + owner.getClass().getName());
        }
        super.setOwner(owner);
        nameFig.setOwner(owner); // for setting abstract
        if (edgePort != null) {
            edgePort.setOwner(getOwner());
        }
        stereotypeFig.setOwner(owner); // this fixes issue 5414
        initNotationProviders(owner);
        updateListeners(null, owner);
        // TODO: The following is redundant.  It's done when setLayer is 
        // called after initialization complete
//        renderingChanged();
    }



    /**
     * Replace the NotationProvider(s). <p>
     *
     * This method shall not be used for the initial creation of
     * notation providers, but only for replacing them when required.
     * Initialization must be done in the
     * constructor using methods which
     * can't be overridden. <p>
     * NotationProviders can not be updated - they
     * are lightweight throw-away objects.
     * Hence this method creates a (new) NotationProvider whenever
     * needed. E.g. when the notation language is
     * changed by the user, then the NPs are to be re-created.
     * So, this method shall not be
     * called from a Fig constructor.<p>
     *
     * After the removal of the deprecated method setOwner(),
     * this method shall contain the following statement:
     *     assert notationProviderName != null
     * 
     * @param own the current owner
     */ 
    protected void initNotationProviders(Object own) {
        if (notationProviderName != null) {
            notationProviderName.cleanListener(this, own);
        }
        /* This should NOT be looking for a NamedElement, 
         * since this is not always about the name of this 
         * modelelement alone.*/
        if (Model.getFacade().isAModelElement(own)) {
            NotationName notation = Notation.findNotation(
                    getNotationSettings().getNotationLanguage());
            notationProviderName =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        getNotationProviderType(), own, this, 
                        notation);
        }
    }


    /**
     * Overrule this for subclasses 
     * that need a different NotationProvider.
     * 
     * @return the type of the notation provider
     */
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_NAME;
    }

    /**
     * Implementations of this method should register/unregister the fig for all
     * (model)events that may cause a repaint to be necessary.
     * In the simplest case, the fig should register itself
     * as listening to (all) events fired by (only) the owner. <p>
     *  
     * But for, for example, for a
     * FigLink the fig must also register for events fired by the
     * association of the owner - because the name of 
     * the association is shown, not the name of the Link.<p>
     * 
     * In other cases, there is no need to register for any event, 
     * e.g. when a notationProvider is used. <p>
     * 
     * This function is called in 2 places: at creation (load) time of this Fig,
     * i.e. when the owner changes, 
     * and in some cases by the modelChanged() function, 
     * i.e. when the model changes. <p>
     * 
     * This function shall always register for the "remove" event of the owner!
     * Otherwise the Fig will not be deleted when the owner gets deleted.<p>
     * 
     *  IF this method is called with both the oldOwner and the 
     *  newOwner equal and not null, 
     *  AND we listen only to the owner itself,
     *  THEN we can safely ignore the call, but 
     *  ELSE we need to update the listeners of the related elements, 
     *  since the related elements may have been replaced.
     * 
     * @param newOwner the new owner for the listeners, 
     *          or null if all listeners have to be removed
     * @param oldOwner the previous owner, 
     *          or null if there was none, and all listeners have to be set
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> l = new HashSet<Object[]>();
        if (newOwner != null) {
            l.add(new Object[] {newOwner, "remove"});
        }
        updateElementListeners(l);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLayer(org.tigris.gef.base.Layer)
     */
    @Override
    public void setLayer(Layer lay) {
        super.setLayer(lay);
        getFig().setLayer(lay);
        
        // TODO: Workaround for GEF redraw problem
        // Force all child figs into the same layer
        for (Fig f : (List<Fig>) getPathItemFigs()) {
            f.setLayer(lay);
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#deleteFromModel()
     */
    @Override
    public void deleteFromModel() {
        Object own = getOwner();
        if (own != null) {
            getProject().moveToTrash(own);
        }

        /* TODO: MVW: Why is this not done in GEF? */
        Iterator it = getPathItemFigs().iterator();
        while (it.hasNext()) {
            ((Fig) it.next()).deleteFromModel();
        }
        super.deleteFromModel();
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     * @deprecated for 0.27.2 by tfmorris. Changes to notation provider are
     *             now handled by the owning diagram.
     */
    @Deprecated
    public void notationChanged(ArgoNotationEvent event) {
        if (getOwner() == null) {
            return;
        }
        renderingChanged();
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
     * @deprecated for 0.27.2 by tfmorris.
     */
    @Deprecated
    public void notationAdded(ArgoNotationEvent event) {
        // Default implementation is to do nothing
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
     * @deprecated for 0.27.2 by tfmorris.
     */
    @Deprecated
    public void notationRemoved(ArgoNotationEvent event) {
        // Default implementation is to do nothing
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
     * @deprecated for 0.27.2 by tfmorris.
     */
    @Deprecated
    public void notationProviderAdded(ArgoNotationEvent event) {
        // Default implementation is to do nothing
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
     * @deprecated for 0.27.2 by tfmorris.
     */
    @Deprecated
    public void notationProviderRemoved(ArgoNotationEvent event) {
        // Default implementation is to do nothing
    }

    /*
     * @see org.tigris.gef.presentation.Fig#hit(java.awt.Rectangle)
     */
    @Override
    public boolean hit(Rectangle r) {
	// Check if labels etc have been hit
	// Apparently GEF does require PathItems to be "annotations"
	// which ours aren't, so until that is resolved...
	Iterator it = getPathItemFigs().iterator();
	while (it.hasNext()) {
	    Fig f = (Fig) it.next();
	    if (f.hit(r)) {
		return true;
	    }
	}
	return super.hit(r);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    @Override
    public final void removeFromDiagram() {
        Fig delegate = getRemoveDelegate();
        // TODO: Dependency cycle between FigNodeModelElement and FigEdgeME
        // Is this needed?  If so, introduce a Removable interface to decouple
        if (delegate instanceof FigNodeModelElement) {
            ((FigNodeModelElement) delegate).removeFromDiagramImpl();
        } else if (delegate instanceof FigEdgeModelElement) {
            ((FigEdgeModelElement) delegate).removeFromDiagramImpl();
        } else if (delegate != null) {
            removeFromDiagramImpl();
        }
    }
    
    /**
     * Subclasses should override this to redirect a remove request from
     * one Fig to another.
     * e.g. FigEdgeAssociationClass uses this to delegate the remove to
     * its attached FigAssociationClass.
     * @return the fig handling the remove
     */
    protected Fig getRemoveDelegate() {
        return this;
    }
    
    protected void removeFromDiagramImpl() {
        Object o = getOwner();
        if (o != null) {
            removeElementListener(o);
        }
        if (notationProviderName != null) {
            notationProviderName.cleanListener(this, getOwner());
        }

        /* TODO: MVW: Why is this not done in GEF? */
        Iterator it = getPathItemFigs().iterator();
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            fig.removeFromDiagram();
        }

        super.removeFromDiagram();
        damage();
    }
    
    protected void superRemoveFromDiagram() {
        super.removeFromDiagram();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#damage()
     */
    @Override
    public void damage() {
        super.damage();
        getFig().damage();
    }

    /**
     * <p>Determines if the FigEdge is currently connected to the correct
     * FigNodes, if not the edges is the correct FigNodes set and the edge
     * rerouted.
     * <p>Typically this is used when a user has amended from the property
     * panel a relationship from one model element to another and the graph
     * needs to react to that change.
     * <p>e.g. if the participant of an association end is changed.
     * <p>Calls a helper method (layoutThisToSelf) to avoid this edge
     * disappearing if the new source and dest are the same node.
     *
     * @return boolean whether or not the update was sucessful
     */
    protected boolean determineFigNodes() {
        Object owner = getOwner();
        if (owner == null) {
            
            LOG.error("The FigEdge has no owner");
            
            return false;
        }
        if (getLayer() == null) {
            
            LOG.error("The FigEdge has no layer");
            
            return false;
        }

        Object newSource = getSource();
        Object newDest = getDestination();

        Fig currentSourceFig = getSourceFigNode();
        Fig currentDestFig = getDestFigNode();
        Object currentSource = null;
        Object currentDestination = null;
        if (currentSourceFig != null && currentDestFig != null) {
            currentSource = currentSourceFig.getOwner();
            currentDestination = currentDestFig.getOwner();
        }
        if (newSource != currentSource || newDest != currentDestination) {
            Fig newSourceFig = getNoEdgePresentationFor(newSource);
            Fig newDestFig = getNoEdgePresentationFor(newDest);
            if (newSourceFig != currentSourceFig) {
                setSourceFigNode((FigNode) newSourceFig);
                setSourcePortFig(newSourceFig);

            }
            if (newDestFig != currentDestFig) {
                setDestFigNode((FigNode) newDestFig);
                setDestPortFig(newDestFig);
            }
            ((FigNode) newSourceFig).updateEdges();
            ((FigNode) newDestFig).updateEdges();
            calcBounds();

            // adapted from SelectionWButtons from line 280
            // calls a helper method to avoid this edge disappearing
            // if the new source and dest are the same node.
            if (newSourceFig == newDestFig) {

                layoutThisToSelf();
            }

        }

        return true;
    }

    /**
     * A version of GEF's presentationFor() method which 
     * @param element ModelElement to return presentation for
     * @return the Fig representing the presentation
     */
    private Fig getNoEdgePresentationFor(Object element) {
        if (element == null) {
            throw new IllegalArgumentException("Can't search for a null owner");
        }
        
        List contents = PgmlUtility.getContentsNoEdges(getLayer());
        int figCount = contents.size();
        for (int figIndex = 0; figIndex < figCount; ++figIndex) {
            Fig fig = (Fig) contents.get(figIndex);
            if (fig.getOwner() == element) {
                return fig;
            }
        }
        throw new IllegalStateException("Can't find a FigNode representing "
                + Model.getFacade().getName(element));
    }


    /**
     * helper method for updateClassifiers() in order to automatically
     * layout an edge that is now from and to the same node type.
     * <p>adapted from SelectionWButtons from line 280
     */
    private void layoutThisToSelf() {

        FigPoly edgeShape = new FigPoly();
        //newFC = _content;
        Point fcCenter =
            new Point(getSourceFigNode().getX() / 2,
                    getSourceFigNode().getY() / 2);
        Point centerRight =
            new Point(
		      (int) (fcCenter.x
			     + getSourceFigNode().getSize().getWidth() / 2),
		      fcCenter.y);

        int yoffset = (int) ((getSourceFigNode().getSize().getHeight() / 2));
        edgeShape.addPoint(fcCenter.x, fcCenter.y);
        edgeShape.addPoint(centerRight.x, centerRight.y);
        edgeShape.addPoint(centerRight.x + 30, centerRight.y);
        edgeShape.addPoint(centerRight.x + 30, centerRight.y + yoffset);
        edgeShape.addPoint(centerRight.x, centerRight.y + yoffset);

        // place the edge on the layer and update the diagram
        this.setBetweenNearestPoints(true);
        edgeShape.setLineColor(LINE_COLOR);
        edgeShape.setFilled(false);
        edgeShape.setComplete(true);
        this.setFig(edgeShape);
    }

    /**
     * Returns the source of the edge. The source is the owner of the
     * node the edge travels from in a binary relationship. For
     * instance: for a classifierrole, this is the sender.
     * @return a model element
     */
    protected Object getSource() {
        Object owner = getOwner();
        if (owner != null) {
            return Model.getCoreHelper().getSource(owner);
        }
        return null;
    }
    /**
     * Returns the destination of the edge. The destination is the
     * owner of the node the edge travels to in a binary
     * relationship. For instance: for a classifierrole, this is the
     * receiver.
     * @return a model element
     */
    protected Object getDestination() {
        Object owner = getOwner();
        if (owner != null) {
            return Model.getCoreHelper().getDestination(owner);
        }
        return null;
    }


    /**
     * @param allowed true if the function RemoveFromDiagram is allowed
     */
    protected void allowRemoveFromDiagram(boolean allowed) {
        this.removeFromDiagram = allowed;
    }

    /**
     * Set the associated Diagram Interchange element.
     * 
     * @param element the element to be associated with this Fig
     */
    public void setDiElement(DiElement element) {
        this.diElement = element;
    }

    /**
     * @return the Diagram Interchange element associated with this Fig
     */
    public DiElement getDiElement() {
        return diElement;
    }

    /**
     * @return Returns the popupAddOffset.
     */
    protected static int getPopupAddOffset() {
        return popupAddOffset;
    }
    

    /**
     * Add an element listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected void addElementListener(Object element) {
        listeners.add(new Object[] {element, null});
        Model.getPump().addModelEventListener(this, element);
    }

    /**
     * Add a listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            name of property to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected void addElementListener(Object element, String property) {
        listeners.add(new Object[] {element, property});
        Model.getPump().addModelEventListener(this, element, property);
    }

    /**
     * Add a listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            array of property names (Strings) to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected void addElementListener(Object element, String[] property) {
        listeners.add(new Object[] {element, property});
        Model.getPump().addModelEventListener(this, element, property);
    }
    
    /**
     * Add an element listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected void removeElementListener(Object element) {
        listeners.remove(new Object[] {element, null});
        Model.getPump().removeModelEventListener(this, element);
    }
   
    
    /**
     * Unregister all listeners registered through addElementListener
     * @see #addElementListener(Object, String)
     */
    protected void removeAllElementListeners() {
        removeElementListeners(listeners);
    }

    private void removeElementListeners(Set<Object[]> listenerSet) {
        for (Object[] listener : listenerSet) {
            Object property = listener[1];
            if (property == null) {
                Model.getPump().removeModelEventListener(this, listener[0]);
            } else if (property instanceof String[]) {
                Model.getPump().removeModelEventListener(this, listener[0],
                        (String[]) property);
            } else if (property instanceof String) {
                Model.getPump().removeModelEventListener(this, listener[0],
                        (String) property);
            } else {
                throw new RuntimeException(
                        "Internal error in removeAllElementListeners");
            }
        }
        listeners.removeAll(listenerSet);
    }

    private void addElementListeners(Set<Object[]> listenerSet) {
        for (Object[] listener : listenerSet) {
            Object property = listener[1];
            if (property == null) {
                addElementListener(listener[0]);
            } else if (property instanceof String[]) {
                addElementListener(listener[0], (String[]) property);
            } else if (property instanceof String) {
                addElementListener(listener[0], (String) property);
            } else {
                throw new RuntimeException(
                        "Internal error in addElementListeners");
            }
        }
    }

    /**
     * Update the set of registered listeners to match the given set using 
     * a minimal update strategy to remove unneeded listeners and add new 
     * listeners.
     * 
     * @param listenerSet a set of arrays containing a tuple of a UML element
     * to be listened to and a set of property to be listened for.  
     */
    protected void updateElementListeners(Set<Object[]> listenerSet) {
        Set<Object[]> removes = new HashSet<Object[]>(listeners);
        removes.removeAll(listenerSet);
        removeElementListeners(removes);
        
        Set<Object[]> adds = new HashSet<Object[]>(listenerSet);
        adds.removeAll(listeners);
        addElementListeners(adds);
    }

    /**
     * @return the current notation arguments or null if none have been set
     * @deprecated for 0.27.3 by tfmorris.  Use {@link #getNotationSettings()}.
     */
    @Deprecated
    protected HashMap<String, Object> getNotationArguments() {
        return npArguments;
    }

    /**
     * @param key
     * @param element
     * @deprecated for 0.27.3 by tfmorris.  Use {@link #getNotationSettings()}.
     */
    @Deprecated
    protected void putNotationArgument(String key, Object element) {
        if (notationProviderName != null) {
            // Lazily initialize if not done yet
            if (npArguments == null) {
                npArguments = new HashMap<String, Object>();
            }
            npArguments.put(key, element);
        }
    }
    
    /**
     * This optional method is not implemented.  It will throw an
     * {@link UnsupportedOperationException} if used. Figs are 
     * added to a GraphModel which is, in turn, owned by a project.
     * @param project the project
     * @deprecated
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void setProject(Project project) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * @deprecated for 0.27.2 by tfmorris.  Implementations should have all
     * the information that they require in the DiagramSettings object.
     * 
     * @return the owning project
     * @see org.argouml.uml.diagram.ui.ArgoFig#getProject()
     */
    @Deprecated
    public Project getProject() {
        return ArgoFigUtil.getProject(this);
    }
    
    /**
     * Handles diagram font changing.
     * 
     * @param e the event
     * @see org.argouml.application.events.ArgoDiagramAppearanceEventListener#diagramFontChanged(org.argouml.application.events.ArgoDiagramAppearanceEvent)
     * @deprecated for 0.27.2 by tfmorris. Global rendering changes are now
     *             managed at the diagram level.
     */
    @Deprecated
    public void diagramFontChanged(ArgoDiagramAppearanceEvent e) {
        updateFont();
        calcBounds(); //TODO: Does this help?
        redraw();
    }
    
    /**
     * This function should, for all FigTexts, 
     * recalculate the font-style (plain, bold, italic, bold/italic),
     * and apply it by calling FigText.setFont().
     */
    protected void updateFont() {
        int style = getNameFigFontStyle();
        Font f = getSettings().getFont(style);
        nameFig.setFont(f);
        deepUpdateFont(this);
    }

    /**
     * Determines the font style based on the UML model. 
     * Overrule this in Figs that have to show bold or italic based on the 
     * UML model they represent. 
     * E.g. abstract classes show their name in italic.
     * 
     * @return the font style for the nameFig.
     */
    protected int getNameFigFontStyle() {
        return Font.PLAIN;
    }

    private void deepUpdateFont(FigEdge fe) {
        Font f = getSettings().getFont(Font.PLAIN);
        for (Object pathFig : fe.getPathItemFigs()) {
            deepUpdateFontRecursive(f, pathFig);
        }
        fe.calcBounds();
    }

    /**
     * Changes the font for all Figs contained in the given FigGroup. <p>
     * 
     *  TODO: In fact, there is a design error in this method:
     *  E.g. for a class, if the name is Italic since the class is abstract,
     *  then the classes features should be in Plain font.
     *  This problem can be fixed by implementing 
     *  the updateFont() method in all subclasses.
     *  
     * @param fg the FigGroup to change the font of.
     */
    private void deepUpdateFontRecursive(Font f, Object pathFig) {
        if (pathFig instanceof ArgoFigText) {
            ((ArgoFigText) pathFig).updateFont();
        } else if (pathFig instanceof FigText) {
            ((FigText) pathFig).setFont(f);
        } else if (pathFig instanceof FigGroup) {
            for (Object fge : ((FigGroup) pathFig).getFigs()) {
                deepUpdateFontRecursive(f, fge);
            }
        }
    }
    

    public DiagramSettings getSettings() {
        // TODO: This is a temporary crutch to use until all Figs are updated
        // to use the constructor that accepts a DiagramSettings object
        if (settings == null) {
            
            LOG.debug("Falling back to project-wide settings");
            
            Project p = getProject();
            if (p != null) {
                return p.getProjectSettings().getDefaultDiagramSettings();
            }
        }
        return settings;
    }
    
    public void setSettings(DiagramSettings renderSettings) {
        settings = renderSettings;
        renderingChanged();
    }
    
    /**
     * @return the current notation settings
     */
    protected NotationSettings getNotationSettings() {
        return getSettings().getNotationSettings();
    }
    
//    public void setLineWidth(int w) {
//        super.setLineWidth(w);
//    }
    
    public void setLineColor(Color c) {
        super.setLineColor(c);
    }
    
    public void setFig(Fig f) {
        super.setFig(f);
        // GEF sets a different Fig than the one that we had at construction
        // time, so we need to set its color and width
        f.setLineColor(getLineColor());
        f.setLineWidth(getLineWidth());
    }
}
