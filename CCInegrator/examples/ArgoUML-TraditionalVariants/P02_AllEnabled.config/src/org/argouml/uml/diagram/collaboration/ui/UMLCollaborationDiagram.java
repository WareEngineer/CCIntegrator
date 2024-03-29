

// $Id: UMLCollaborationDiagram.java 132 2010-09-26 23:32:33Z marcusvnac $
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

package org.argouml.uml.diagram.collaboration.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.Action;


import org.apache.log4j.Logger;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.collaboration.CollabDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.ui.ActionAddAssociationRole;
import org.argouml.uml.diagram.ui.ActionAddMessage;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.FigMessage;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

/**
 * The base class of the collaboration diagram.<p>
 *
 * Defines the toolbar, provides for its initialization and provides
 * constructors for a top level diagram and one within a defined
 * namespace.<p>
 *
 * @author agauthie@ics.uci.edu
 */
public class UMLCollaborationDiagram extends UMLDiagram {
    
    /**
     * Logging.
     */
    private static final Logger LOG =
        Logger.getLogger(UMLCollaborationDiagram.class);
    
    ////////////////////////
    // actions for toolbar

    private Action actionClassifierRole;
    private Action actionGeneralize;

    private Action actionAssociation;
    private Action actionAggregation;
    private Action actionComposition;
    private Action actionUniAssociation;
    private Action actionUniAggregation;
    private Action actionUniComposition;

    private Action actionDepend;
    private Action actionMessage;

    ////////////////////////////////////////////////////////////////
    // contructors

    /**
     * This constructor is used to build a dummy collaboration diagram so
     * that a project will load properly.
     * @deprecated for 0.28 by tfmorris.  Use 
     * {@link #UMLActivityDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLCollaborationDiagram() {
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) { }
        // TODO: All super constrcutors should take a GraphModel
        setGraphModel(createGraphModel());
    }

    /**
     * The constructor.
     *
     * @param collaboration the collaboration aka namespace for the diagram
     * @deprecated for 0.28 by tfmorris.  Use 
     * {@link #UMLActivityDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLCollaborationDiagram(Object collaboration) {
        this();
        setNamespace(collaboration);
    }

    /**
     * @return the number of UML messages in the diagram
     */
    public int getNumMessages() {
        Layer lay = getLayer();
        Collection figs = lay.getContents();
        int res = 0;
        Iterator it = figs.iterator();
        while (it.hasNext()) {
            Fig f = (Fig) it.next();
            if (Model.getFacade().isAMessage(f.getOwner())) {
                res++;
            }
        }
        return res;
    }

    /**
     * Method to perform a number of important initializations of a
     * <em>CollaborationDiagram</em>.<p>
     *
     * Each diagram type has a similar <em>UMLxxxDiagram</em> class.<p>
     *
     * Changed <em>lay</em> from <em>LayerPerspective</em> to
     * <em>LayerPerspectiveMutable</em>.  This class is a child of
     * <em>LayerPerspective</em> and was implemented to correct some
     * difficulties in changing the model.  <em>Lay</em> is used mainly
     * in <em>LayerManager</em>(GEF) to control the adding, changing and
     * deleting layers on the diagram...
     *
     * @param handle the collaboration from the UML model
     * @author psager@tigris.org Jan. 24, 2002
     */
    public void setNamespace(Object handle) {
        if (!Model.getFacade().isANamespace(handle)) {
            
            LOG.error(
                "Illegal argument. Object " + handle + " is not a namespace");
            
            throw new IllegalArgumentException(
                "Illegal argument. Object " + handle + " is not a namespace");
        }
        super.setNamespace(handle);
        CollabDiagramGraphModel gm = createGraphModel();
        gm.setCollaboration(handle);
        LayerPerspective lay =
            new LayerPerspectiveMutable(Model.getFacade().getName(handle), gm);
        CollabDiagramRenderer rend = new CollabDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);
    }
    
    
    // TODO: Needs to be tidied up after stable release. Graph model
    // should be created in constructor
    private CollabDiagramGraphModel createGraphModel() {
	if ((getGraphModel() instanceof CollabDiagramGraphModel)) {
	    return (CollabDiagramGraphModel) getGraphModel();
	} else {
	    return new CollabDiagramGraphModel();
	}
    }

    /**
     * Get the actions from which to create a toolbar or equivalent
     * graphic triggers.
     * {@inheritDoc}
     */
    protected Object[] getUmlActions() {
        Object[] actions = {
	    getActionClassifierRole(),
	    null,
	    getAssociationActions(),
	    getActionGeneralize(),
	    getActionDepend(),
            null,
            getActionMessage(), //this one behaves differently, hence seperated!
        };
        return actions;
    }

    private Object[] getAssociationActions() {
        Object[][] actions = {
	    {getActionAssociation(), getActionUniAssociation() },
	    {getActionAggregation(), getActionUniAggregation() },
	    {getActionComposition(), getActionUniComposition() },
        };
        ToolBarUtility.manageDefault(actions, 
                "diagram.collaboration.association");
        return actions;
    }

    /**
     * After loading the diagram it is necessary to connect
     * every FigMessage to its FigAssociationRole.
     * This is done by adding the FigMessage
     * to the PathItems of its FigAssociationRole.
     */
    public void postLoad() {

        super.postLoad();

        if (getNamespace() == null) {
            throw new IllegalStateException(
                    "The namespace of the collaboration diagram is not set");
        }

        Collection messages;
        Iterator msgIterator;
        Collection ownedElements =
            Model.getFacade().getOwnedElements(getNamespace());
        Iterator oeIterator = ownedElements.iterator();
        Layer lay = getLayer();
        while (oeIterator.hasNext()) {
            Object me = oeIterator.next();
            if (Model.getFacade().isAAssociationRole(me)) {
                messages = Model.getFacade().getMessages(me);
                msgIterator = messages.iterator();
                while (msgIterator.hasNext()) {
                    Object message = msgIterator.next();
                    FigMessage figMessage =
                        (FigMessage) lay.presentationFor(message);
                    if (figMessage != null) {
                        figMessage.addPathItemToFigAssociationRole(lay);
                    }
                }
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    public String getLabelName() {
        return Translator.localize("label.collaboration-diagram");
    }

    /**
     * @return Returns the actionClassifierRole.
     */
    private Action getActionClassifierRole() {
        if (actionClassifierRole == null) {
            actionClassifierRole =
                new RadioAction(new ActionAddClassifierRole());
        }
        return actionClassifierRole;
    }

    /**
     * @return Returns the actionAssociation.
     */
    protected Action getActionAssociation() {
        if (actionAssociation == null) {
            actionAssociation =
                new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getNone(),
                        false,
                        "button.new-associationrole",
                        "Association"));
        }
        return actionAssociation;
    }
    /**
     * @return Returns the actionComposition.
     */
    protected Action getActionComposition() {
        if (actionComposition == null) {
            actionComposition =
                new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getComposite(),
                        false,
                        "button.new-composition"));
        }
        return actionComposition;
    }
    /**
     * @return Returns the actionDepend.
     */
    protected Action getActionDepend() {
        if (actionDepend == null) {
            actionDepend =
                new RadioAction(
                    new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getDependency(),
                        "button.new-dependency"));
        }
        return actionDepend;
    }
    /**
     * @return Returns the actionGeneralize.
     */
    protected Action getActionGeneralize() {
        if (actionGeneralize == null) {
            actionGeneralize =
                new RadioAction(
                    new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getGeneralization(),
                        "button.new-generalization"));
        }
        return actionGeneralize;
    }

    /**
     * @return Returns the actionUniAggregation.
     */
    protected Action getActionUniAggregation() {
        if (actionUniAggregation == null) {
            actionUniAggregation =
                new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getAggregate(),
                        true,
                        "button.new-uniaggregation"));
        }
        return actionUniAggregation;
    }
    /**
     * @return Returns the actionUniAssociation.
     */
    protected Action getActionUniAssociation() {
        if (actionUniAssociation  == null) {
            actionUniAssociation =
                new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getNone(),
                        true,
                        "button.new-uniassociation"));
        }
        return actionUniAssociation;
    }
    /**
     * @return Returns the actionUniComposition.
     */
    protected Action getActionUniComposition() {
        if (actionUniComposition == null) {
            actionUniComposition =
                new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getComposite(),
                        true,
                        "button.new-unicomposition"));
        }
        return actionUniComposition;
    }

    /**
     * @return Returns the actionAggregation.
     */
    private Action getActionAggregation() {
        if (actionAggregation == null) {
            actionAggregation =
                new RadioAction(
                    new ActionAddAssociationRole(
                        Model.getAggregationKind().getAggregate(),
                        false,
                        "button.new-aggregation"));
        }
        return actionAggregation;
    }

    /**
     * @return Returns the actionMessage.
     */
    private Action getActionMessage() {
        if (actionMessage == null) {
            actionMessage = ActionAddMessage.getTargetFollower();
        }
        return actionMessage;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getDependentElement()
     */
    @Override
    public Object getDependentElement() {
        return getNamespace(); /* The collaboration. */
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base) {
        /* TODO: We may return the following when the
         * relocate() has been implemented.
         */
//      if (Model.getFacade().isAOperation(base)
//      || Model.getFacade().isANamespace(base))
//      return Model.getCollaborationsHelper()
//      .isAddingCollaborationAllowed(base);
        return false;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        return false;
    }

    @SuppressWarnings("unchecked")
    public Collection getRelocationCandidates(Object root) {
        /* TODO: We may return something useful when the
         * relocate() has been implemented. */
        Collection c =  new HashSet();
        c.add(getOwner());
        return c;
    }

    public void encloserChanged(FigNode enclosed, 
            FigNode oldEncloser, FigNode newEncloser) {
        // Do nothing.        
    }
    
    /**
     * A sequence diagram can accept all classifiers. It will add them as a new 
     * Classifier Role with that classifier as a base. All other accepted figs 
     * are added as is.
     * @param objectToAccept
     * @return true if the diagram can accept the object, else false
     * @see org.argouml.uml.diagram.ui.UMLDiagram#doesAccept(java.lang.Object)
     */
    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAClassifierRole(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAMessage(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComment(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAClassifier(objectToAccept)) {
            return true;
        }
        return false;
    }
    
    /**
     * Creates a new Classifier Role with a specified base.
     * @param base
     * @return The new CR
     */
    private Object makeNewCR(Object base) {
        Object node = null;
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (gm instanceof CollabDiagramGraphModel) {
            Object collaboration =
                ((CollabDiagramGraphModel) gm).getHomeModel();
            node =
                Model.getCollaborationsFactory().buildClassifierRole(
                        collaboration);
          }
       
        Model.getCollaborationsHelper().addBase(node, base);
        return node;
    }
    
    /**
     * Creates the Fig for the CR. Y position will be adjusted to match other 
     * the other CRs.
     * @param classifierRole
     * @param location The position where to put the new fig.
     * @return
     */
    private FigClassifierRole makeNewFigCR(Object classifierRole, 
            Point location) {
        if (classifierRole != null) {
            FigClassifierRole newCR = new FigClassifierRole(classifierRole,
                    new Rectangle(location), getDiagramSettings());
            
            getGraphModel().getNodes().add(newCR.getOwner());

            return newCR;
        }
        return null;
    }
    
    @Override
    public FigNode drop(Object droppedObject, Point location) {
        FigNode figNode = null;
        GraphModel gm = getGraphModel();
        Layer lay = Globals.curEditor().getLayerManager().getActiveLayer();
        
        // If location is non-null, convert to a rectangle that we can use
        Rectangle bounds = null;
        if (location != null) {
            bounds = new Rectangle(location.x, location.y, 0, 0);
        }
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAClassifierRole(droppedObject)) {
            figNode = new FigClassifierRole(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAMessage(droppedObject)) {
            figNode = new FigMessage(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAComment(droppedObject)) {
            figNode = new FigComment(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAClassifierRole(droppedObject)) {
            figNode = makeNewFigCR(droppedObject, location);           
        } else if (Model.getFacade().isAClassifier(droppedObject)) {
            figNode = makeNewFigCR(makeNewCR(droppedObject), location);
        }
        
        if (figNode != null) {            
            LOG.debug("Dropped object " + droppedObject + " converted to " 
                    + figNode);
        } else {
            LOG.debug("Dropped object NOT added " + droppedObject);
        }
        
        return figNode;
    }
    
    @Override
    public String getInstructions(Object droppedObject) {
        if (Model.getFacade().isAClassifierRole(droppedObject)) {
    		return super.getInstructions(droppedObject);
    	} else if (Model.getFacade().isAClassifier(droppedObject)) {
            return Translator.localize(
                    "misc.message.click-on-diagram-to-add-as-cr", 
                    new Object[] {Model.getFacade().toString(droppedObject)});
        }
        return super.getInstructions(droppedObject);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 8081715986963837750L;
}
