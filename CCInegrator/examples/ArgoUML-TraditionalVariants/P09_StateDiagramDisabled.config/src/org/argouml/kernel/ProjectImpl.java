// $Id: ProjectImpl.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;

import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ApplicationVersion;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileFacade;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.ProjectMemberModel;

import org.argouml.uml.cognitive.ProjectMemberTodoList;

import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.tigris.gef.presentation.Fig;

/**
 * The ProjectImpl is a data structure that represents the designer's
 * current project. It manages the list of diagrams and UML models.
 * <p>
 * NOTE: This was named Project until 0.25.4 when it was replaced by an
 * interface of the same name and renamed to ProjectImpl.
 */
public class ProjectImpl implements java.io.Serializable, Project {
    
    private static final Logger LOG = Logger.getLogger(ProjectImpl.class);
    
    /**
     * Default name for a project.
     */
    private static final String UNTITLED_FILE =
	Translator.localize("label.projectbrowser-title");

    /**
     * The UID.
     */
    static final long serialVersionUID = 1399111233978692444L;

    /**
     * TODO: should just be the directory to write.
     */
    private URI uri;

    /* The preferences with project-scope: */
    private String authorname;
    private String authoremail;
    private String description;
    /* The ArgoUML version with which this project was last saved: */
    private String version;

    private ProjectSettings projectSettings;

    private final List<String> searchpath = new ArrayList<String>();

    // TODO: break into 3 main member types
    // model, diagram and other
    private final List<ProjectMember> members = new MemberList();

    private String historyFile;

    /**
     * The version number of the persistence format that last
     * saved this project.
     */
    private int persistenceVersion;

    /**
     * Instances of the UML model.
     */
    private final List models = new ArrayList();
    
    private Object root;
    private final Collection roots = new HashSet();

    /**
     * Instances of the UML diagrams.
     */
    private final List<ArgoDiagram> diagrams = new ArrayList<ArgoDiagram>();
    
    private Object currentNamespace;
    private Map<String, Object> uuidRefs;
    private transient VetoableChangeSupport vetoSupport;

    private ProfileConfiguration profileConfiguration;

    /**
     * The active diagram, pointer to a diagram in the list with diagrams.
     */
    private ArgoDiagram activeDiagram;
    
    /** The name of the diagram to show by default after loading a project. */
    private String savedDiagramName;

    /**
     * Cache for the default model.
     */
    private HashMap<String, Object> defaultModelTypeCache;

    private final Collection trashcan = new ArrayList();

    // TODO: Change this to use an UndoManager instance per project when
    // GEF has been enhanced.
    private UndoManager undoManager = DefaultUndoManager.getInstance();

    private boolean dirty = false;
    
    /**
     * Constructor.
     *
     * @param theProjectUri Uri to read the project from.
     */
    public ProjectImpl(URI theProjectUri) {
        this();
        /* TODO: Why was this next line in the code so long? */
//        uri = PersistenceManager.getInstance().fixUriExtension(theProjectUri);
        uri = theProjectUri;
    }

    /**
     * Constructor.
     */
    public ProjectImpl() {
        setProfileConfiguration(new ProfileConfiguration(this));

        projectSettings = new ProjectSettings();

        Model.getModelManagementFactory().setRootModel(null);

        authorname = Configuration.getString(Argo.KEY_USER_FULLNAME);
        authoremail = Configuration.getString(Argo.KEY_USER_EMAIL);
        description = "";
        // this should be moved to a ui action.
        version = ApplicationVersion.getVersion();

        historyFile = "";
        defaultModelTypeCache = new HashMap<String, Object>();
        
        LOG.info("making empty project with empty model");
        
        addSearchPath("PROJECT_DIR");
    }


    public String getName() {
        // TODO: maybe separate name
        if (uri == null) {
            return UNTITLED_FILE;
        }
        return new File(uri).getName();
    }


    public URI getUri() {
        return uri;
    }


    public URI getURI() {
        return uri;
    }

    /**
     * @param theUri the URI for the project
     */
    public void setUri(URI theUri) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting project URI from \"" + uri
                      + "\" to \"" + theUri + "\".");
        }
        
        uri = theUri;
    }

    public void setFile(final File file) {
        URI theProjectUri = file.toURI();
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting project file name from \""
                      + uri
                      + "\" to \""
                      + theProjectUri
                      + "\".");
        }
        
        uri = theProjectUri;
    }


    public List<String> getSearchPathList() {
        return Collections.unmodifiableList(searchpath);
    }


    public void addSearchPath(final String searchPathElement) {
        if (!searchpath.contains(searchPathElement)) {
            searchpath.add(searchPathElement);
        }
    }


    public List<ProjectMember> getMembers() {
        
        LOG.info("Getting the members there are " + members.size());
        
        return members;
    }

    /**
     * @param d the diagram
     */
    private void addDiagramMember(ArgoDiagram d) {
        // Check for duplicate name and rename if necessary
        int serial = getDiagramCount();
        while (!isValidDiagramName(d.getName())) {
            try {
                d.setName(d.getName() + " " + serial);
            } catch (PropertyVetoException e) {
                serial++;            
            }
        }
        ProjectMember pm = new ProjectMemberDiagram(d, this);
        addDiagram(d);
        // if diagram added successfully, add the member too
        members.add(pm);
    }

    
    /**
     * @param pm the member to be added
     */
    private void addTodoMember(ProjectMemberTodoList pm) {
        // Adding a todo member removes any existing one.
        members.add(pm);
        
        LOG.info("Added todo member, there are now " + members.size());
        
    }
    

    public void addMember(Object m) {

        if (m == null) {
            throw new IllegalArgumentException(
                    "A model member must be suppleid");
        } else if (m instanceof ArgoDiagram) {
            
            LOG.info("Adding diagram member");
            
            addDiagramMember((ArgoDiagram) m);
        } 
        
        else if (m instanceof ProjectMemberTodoList) {
            
            LOG.info("Adding todo member");
            
            addTodoMember((ProjectMemberTodoList) m);
        } 
        
        else if (Model.getFacade().isAModel(m)) {
            
            LOG.info("Adding model member");
            
            addModelMember(m);
        } else {
            throw new IllegalArgumentException(
                    "The member must be a UML model todo member or diagram."
                    + "It is " + m.getClass().getName());
        }
        
        LOG.info("There are now " + members.size() + " members");
        
    }

    /**
     * @param m the model
     */
    private void addModelMember(final Object m) {

        boolean memberFound = false;
        Object currentMember =
            members.get(0);
        if (currentMember instanceof ProjectMemberModel) {
            Object currentModel =
                ((ProjectMemberModel) currentMember).getModel();
            if (currentModel == m) {
                memberFound = true;
            }
        }

        if (!memberFound) {
            if (!models.contains(m)) {
                addModel(m);
            }
            // got past the veto, add the member
            ProjectMember pm = new ProjectMemberModel(m, this);
            
            LOG.info("Adding model member to start of member list");
            
            members.add(pm);
        } else {
            
            LOG.info("Attempted to load 2 models");
            
            throw new IllegalArgumentException(
                    "Attempted to load 2 models");
        }
    }


    public void addModel(final Object model) {

        if (!Model.getFacade().isAModel(model)) {
            throw new IllegalArgumentException();
	}
        if (!models.contains(model)) {
            setRoot(model);
        }        
    }

    private void addModelInternal(final Object model) {
        models.add(model);
        roots.add(model);
        setCurrentNamespace(model);
        setSaveEnabled(true);
        if (models.size() > 1 || roots.size() > 1) {
            
            LOG.debug("Multiple roots/models");
            
        }
    }

    /**
     * Removes a project member diagram completely from the project.
     * @param d the ArgoDiagram
     */
    protected void removeProjectMemberDiagram(ArgoDiagram d) {
        if (activeDiagram == d) {
            
            LOG.debug("Deleting active diagram " + d);
            
            ArgoDiagram defaultDiagram = null;
            if (diagrams.size() == 1) {
                // We're deleting the last diagram so lets create a new one
                // TODO: Once we go MDI we won't need this.
                
                
                LOG.debug("Deleting last diagram - creating new default diag");
                
                Object projectRoot = getRoot();
                if (!Model.getUmlFactory().isRemoved(projectRoot)) {
                    defaultDiagram = DiagramFactory.getInstance()
                            .createDefaultDiagram(projectRoot);
                    addMember(defaultDiagram);
                }
            } else {
                // Make the topmost diagram (that is not the one being deleted)
                // current.
                defaultDiagram = diagrams.get(0);
                
                LOG.debug("Candidate default diagram is " + defaultDiagram);
                
                if (defaultDiagram == d) {
                    defaultDiagram = diagrams.get(1);
                    
                    LOG.debug("Switching default diagram to " + defaultDiagram);
                    
                }
            }
            activeDiagram = defaultDiagram;
            TargetManager.getInstance().setTarget(activeDiagram);
            
            LOG.debug("New active diagram is " + defaultDiagram);
            
        }

        removeDiagram(d);
        members.remove(d);
        d.remove();
        setSaveEnabled(true);
    }
    
    /**
     * Enables the save action if this project is the current project
     * @param enable true to enable
     */
    private void setSaveEnabled(boolean enable) {
        ProjectManager pm = ProjectManager.getManager();
        if (pm.getCurrentProject() == this) {
            pm.setSaveEnabled(enable);
        }
    }


    public String getAuthorname() {
        return authorname;
    }


    public void setAuthorname(final String s) {
        final String oldAuthorName = authorname;
        AbstractCommand command = new AbstractCommand() {
            public Object execute() {
                authorname = s;
                return null;
            }

            public void undo() {
                authorname = oldAuthorName;
            }
        };
        undoManager.execute(command);
    }


    public String getAuthoremail() {
        return authoremail;
    }


    public void setAuthoremail(final String s) {
        final String oldAuthorEmail = authoremail;
        AbstractCommand command = new AbstractCommand() {
            public Object execute() {
                authoremail = s;
                return null;
            }

            public void undo() {
                authoremail = oldAuthorEmail;
            }
        };
        undoManager.execute(command);
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String s) {
        version = s;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(final String s) {
        final String oldDescription = description;
        AbstractCommand command = new AbstractCommand() {
            public Object execute() {
                description = s;
                return null;
            }

            public void undo() {
                description = oldDescription;
            }
        };
        undoManager.execute(command);
    }


    public String getHistoryFile() {
        return historyFile;
    }


    public void setHistoryFile(final String s) {
        historyFile = s;
    }


    public List getUserDefinedModelList() {
        return models;
    }


    public Collection getModels() {
        Set result = new HashSet();
        result.addAll(models);
        for (Profile profile : getProfileConfiguration().getProfiles()) {
            try {
                result.addAll(profile.getProfilePackages());
            } catch (org.argouml.profile.ProfileException e) {
                
                LOG.error("Exception when fetching models from profile "
                        + profile.getDisplayName(), e);
                
            }
        }
        return Collections.unmodifiableCollection(result);
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public Object getModel() {
        if (models.size() != 1) {
            return null;
        }
        return models.iterator().next();
    }


    public Object findType(String s) {
        return findType(s, true);
    }


    public Object getDefaultAttributeType() {
        if (profileConfiguration.getDefaultTypeStrategy() != null) {
            return profileConfiguration.getDefaultTypeStrategy()
                    .getDefaultAttributeType();
        }
        return null;
    }


    public Object getDefaultParameterType() {
        if (profileConfiguration.getDefaultTypeStrategy() != null) {
            return profileConfiguration.getDefaultTypeStrategy()
                    .getDefaultParameterType();
        }
        return null;
    }
    

    public Object getDefaultReturnType() {
        if (profileConfiguration.getDefaultTypeStrategy() != null) {
            return profileConfiguration.getDefaultTypeStrategy()
                    .getDefaultReturnType();
        }
        return null;
    }


    public Object findType(String s, boolean defineNew) {
        if (s != null) {
            s = s.trim();
        }
        if (s == null || s.length() == 0) {
            return null;
        }
        Object cls = null;
        for (Object model : models) {
            cls = findTypeInModel(s, model);
            if (cls != null) {
                return cls;
            }
        }
        cls = findTypeInDefaultModel(s);

        if (cls == null && defineNew) {
            
            LOG.debug("new Type defined!");
            
            cls =
                Model.getCoreFactory().buildClass(getCurrentNamespace());
            Model.getCoreHelper().setName(cls, s);
        }
        return cls;
    }


    public Collection<Fig> findFigsForMember(Object member) {
        Collection<Fig> figs = new ArrayList<Fig>();
        for (ArgoDiagram diagram : diagrams) {
            Fig fig = diagram.getContainingFig(member);
            if (fig != null) {
                figs.add(fig);
            }
        }
        return figs;
    }


    public Collection findAllPresentationsFor(Object obj) {
        Collection<Fig> figs = new ArrayList<Fig>();
        for (ArgoDiagram diagram : diagrams) {
            Fig aFig = diagram.presentationFor(obj);
            if (aFig != null) {
                figs.add(aFig);
            }
        }
        return figs;
    }

    public Object findTypeInModel(String typeName, Object namespace) {
        if (typeName == null) {
            throw new IllegalArgumentException("typeName must be non-null");
        }
        if (!Model.getFacade().isANamespace(namespace)) {
            throw new IllegalArgumentException(
                    "Looking for the classifier " + typeName
                    + " in a non-namespace object of " + namespace
                    + ". A namespace was expected.");
    	}

        Collection allClassifiers =
            Model.getModelManagementHelper()
	        .getAllModelElementsOfKind(namespace,
	                Model.getMetaTypes().getClassifier());

        for (Object classifier : allClassifiers) {
            if (typeName.equals(Model.getFacade().getName(classifier))) {
                return classifier;
            }
        }

        return null;
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public void setCurrentNamespace(final Object m) {

        if (m != null && !Model.getFacade().isANamespace(m)) {
            throw new IllegalArgumentException();
    	}

        currentNamespace = m;
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public Object getCurrentNamespace() {
        return currentNamespace;
    }


    public List<ArgoDiagram> getDiagramList() {
        return Collections.unmodifiableList(diagrams);
    }


    public int getDiagramCount() {
        return diagrams.size();
    }


    public ArgoDiagram getDiagram(String name) {
        for (ArgoDiagram ad : diagrams) {
            if (ad.getName() != null && ad.getName().equals(name)) {
                return ad;
            }
            if (ad.getItemUID() != null
                    && ad.getItemUID().toString().equals(name)) {
                return ad;
            }
        }
        return null;
    }


    public void addDiagram(final ArgoDiagram d) {
        // send indeterminate new value instead of making copy of vector
	d.setProject(this);
        diagrams.add(d);

        d.addPropertyChangeListener("name", new NamePCL());
        setSaveEnabled(true);
    }

    /**
     * Listener to events from the Diagram class. <p>
     *
     * Purpose: changing the name of a diagram shall set the need save flag.
     *
     * @author Michiel
     */
    private class NamePCL implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            setSaveEnabled(true);
        }
    }

    /**
     * Removes a diagram from the list with diagrams.
     *
     * Removes (hopefully) the event listeners for this diagram.  Does
     * not remove the diagram from the project members. This should
     * not be called directly. Use moveToTrash if you want to remove a
     * diagram.
     *
     * @param d the ArgoDiagram
     */
    protected void removeDiagram(ArgoDiagram d) {
        diagrams.remove(d);
        /* Remove the dependent
         * modelelements, such as the statemachine
         * for a statechartdiagram:
         */
        Object o = d.getDependentElement();
        if (o != null) {
            moveToTrash(o);
        }
    }

    public int getPresentationCountFor(Object me) {

        if (!Model.getFacade().isAUMLElement(me)) {
            throw new IllegalArgumentException();
    	}

        int presentations = 0;
        for (ArgoDiagram d : diagrams) {
            presentations += d.getLayer().presentationCountFor(me);
        }
        return presentations;
    }


    public Object getInitialTarget() {
        if (savedDiagramName != null) {
            /* Hence, a diagram name was saved in the project 
             * that we are loading. So, we use this name 
             * to retrieve any matching diagram. */
            return getDiagram(savedDiagramName);
        }
        if (diagrams.size() > 0) {
            /* Use the first diagram. */
            return diagrams.get(0);
        }
        if (models.size() > 0) {
            /* If there was no diagram at all, 
             * then use the (first) UML model. */
            return models.iterator().next();
        }
        return null;
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public VetoableChangeSupport getVetoSupport() {
        if (vetoSupport == null) {
            vetoSupport = new VetoableChangeSupport(this);
        }
        return vetoSupport;
    }


    public void preSave() {
        for (ArgoDiagram diagram : diagrams) {
            diagram.preSave();
        }
    }


    public void postSave() {
        for (ArgoDiagram diagram : diagrams) {
            diagram.postSave();
        }
        setSaveEnabled(true);
    }


    public void postLoad() {
        long startTime = System.currentTimeMillis();
        for (ArgoDiagram diagram : diagrams) {
            diagram.postLoad();
        }
        long endTime = System.currentTimeMillis();
        
        LOG.debug("Diagram post load took " + (endTime - startTime) + " msec.");
        
        
        // issue 1725: the root is not set, which leads to problems
        // with displaying prop panels
        Object model = getModel();
        
        LOG.info("Setting root model to " + model);
        
        setRoot(model);

        setSaveEnabled(true);
        // we don't need this HashMap anymore so free up the memory
        uuidRefs = null;
    }

    ////////////////////////////////////////////////////////////////
    // trash related methods

    /**
     * Empty the trash can and permanently delete all objects that it contains.
     */
    private void emptyTrashCan() {
        trashcan.clear();
    }

    public void moveToTrash(Object obj) {
        if (obj instanceof Collection) {
            Iterator i = ((Collection) obj).iterator();
            while (i.hasNext()) {
                Object trash = i.next();
                if (!trashcan.contains(trash)) {
                    trashInternal(trash);
                }
            }
        } else {
            if (!trashcan.contains(obj)) {
                trashInternal(obj);
            }
        }
    }

    /**
     * Removes some object from the project.
     *
     * @param obj the object to be thrown away
     */
    protected void trashInternal(Object obj) {
        // TODO: This should only be checking for the top level package
        // (if anything at all)
        if (Model.getFacade().isAModel(obj)) {
            return; //Can not delete the model
        }

        if (obj != null) {
            trashcan.add(obj);
        }
        if (Model.getFacade().isAUMLElement(obj)) {

            Model.getUmlFactory().delete(obj);

            // TODO: Presumably this is only relevant if
            // obj is actually a Model.
            // An added test of Model.getFacade.isAModel(obj) would clarify what
            // is going on here.
            if (models.contains(obj)) {
                models.remove(obj);
            }
        } else if (obj instanceof ArgoDiagram) {
            removeProjectMemberDiagram((ArgoDiagram) obj);
            // Fire an event some anyone who cares about diagrams being
            // removed can listen for it
            ProjectManager.getManager()
                    .firePropertyChanged("remove", obj, null);
        } else if (obj instanceof Fig) {
            ((Fig) obj).deleteFromModel();
            // If we delete a FigEdge
            // or FigNode we actually call this method with the owner not
            // the Fig itself. However, this method 
            // is called by ActionDeleteModelElements
            // for primitive Figs (without owner).
            
            
            LOG.info("Request to delete a Fig " + obj.getClass().getName());
            
        } else if (obj instanceof CommentEdge) {
            // TODO: Why is this a special case? - tfm
            CommentEdge ce = (CommentEdge) obj;
            
            LOG.info("Removing the link from " + ce.getAnnotatedElement()
                    + " to " + ce.getComment());
            
            ce.delete();
        }
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean isInTrash(Object obj) {
        return trashcan.contains(obj);
    }



    public Object findTypeInDefaultModel(String name) {
        if (defaultModelTypeCache.containsKey(name)) {
            return defaultModelTypeCache.get(name);
        }
        
        Object result = profileConfiguration.findType(name);
        
        defaultModelTypeCache.put(name, result);
        return result;
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public final Object getRoot() {
        return root;
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public void setRoot(final Object theRoot) {

        if (theRoot == null) {
            throw new IllegalArgumentException(
        	    "A root model element is required");
        }
        if (!Model.getFacade().isAModel(theRoot)) {
            throw new IllegalArgumentException(
        	    "The root model element must be a model - got "
        	    + theRoot.getClass().getName());
        }

        Object treeRoot = Model.getModelManagementFactory().getRootModel();
        if (treeRoot != null) {
            models.remove(treeRoot);
        }
        root = theRoot;
        // TODO: We don't really want to do the following, but I'm not sure
        // what depends on it - tfm - 20070725
        Model.getModelManagementFactory().setRootModel(theRoot);
        // TODO: End up with multiple models here
        addModelInternal(theRoot);
        roots.clear();
        roots.add(theRoot);
    }


    public final Collection getRoots() {
        return Collections.unmodifiableCollection(roots);
    }


    public void setRoots(final Collection elements) {
        boolean modelFound = false;
        for (Object element : elements) {
            
            if (!Model.getFacade().isAPackage(element)) {                
                LOG.warn("Top level element other than package found - " 
                        + Model.getFacade().getName(element));
                
            }
            
            if (Model.getFacade().isAModel(element)) {
                addModel(element);
                if (!modelFound) {
                    setRoot(element);
                    modelFound = true;
                }
            }
        }
        roots.clear();
        roots.addAll(elements);
    }

    public boolean isValidDiagramName(String name) {
        boolean rv = true;
        for (ArgoDiagram diagram : diagrams) {
            if (diagram.getName().equals(name)) {
                rv = false;
                break;
            }
        }
        return rv;
    }


    public Map<String, Object> getUUIDRefs() {
        return uuidRefs;
    }

    
    public void setSearchPath(final List<String> theSearchpath) {
        searchpath.clear();
        searchpath.addAll(theSearchpath);
    }

    public void setUUIDRefs(Map<String, Object> uUIDRefs) {
        uuidRefs = uUIDRefs;
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public void setVetoSupport(VetoableChangeSupport theVetoSupport) {
        vetoSupport = theVetoSupport;
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    public ArgoDiagram getActiveDiagram() {
        return activeDiagram;
    }



    @SuppressWarnings("deprecation")
    @Deprecated
    public void setActiveDiagram(final ArgoDiagram theDiagram) {
        activeDiagram = theDiagram;
    }
    
    public void setSavedDiagramName(String diagramName) {
        savedDiagramName = diagramName;
    }

    public void remove() {
        for (ArgoDiagram diagram : diagrams) {
            diagram.remove();
        }

        members.clear();
        if (!roots.isEmpty()) {
            try {
                Model.getUmlFactory().deleteExtent(roots.iterator().next());
            } catch (InvalidElementException e) {
                
                LOG.warn("Extent deleted a second time");
                
            }
            roots.clear();
        }
        models.clear();
        diagrams.clear();
        searchpath.clear();

        if (uuidRefs != null) {
            uuidRefs.clear();
        }

        if (defaultModelTypeCache != null) {
            defaultModelTypeCache.clear();
        }

        uuidRefs = null;
        defaultModelTypeCache = null;

        uri = null;
        authorname = null;
        authoremail = null;
        description = null;
        version = null;
        historyFile = null;
        currentNamespace = null;
        vetoSupport = null;
        activeDiagram = null;
        savedDiagramName = null;

        emptyTrashCan();
    }


    public int getPersistenceVersion() {
        return persistenceVersion;
    }


    public void setPersistenceVersion(int pv) {
        persistenceVersion = pv;
    }


    public String repair() {
        StringBuilder report = new StringBuilder();
        Iterator it = members.iterator();
        while (it.hasNext()) {
            ProjectMember member = (ProjectMember) it.next();
            report.append(member.repair());
        }
        return report.toString();
    }


    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }
    public UndoManager getUndoManager() {
        return undoManager;
    }
        
    public ProfileConfiguration getProfileConfiguration() {
        return profileConfiguration;
    }

    public void setProfileConfiguration(ProfileConfiguration pc) {
        if (this.profileConfiguration != pc) {
            if (this.profileConfiguration != null) {
                this.members.remove(this.profileConfiguration);
            }

            this.profileConfiguration = pc;

            // there's just one ProfileConfiguration in a project
            // and there's no other way to add another one
            members.add(pc);
        }
        
        ProfileFacade.applyConfiguration(pc);
    }

    public boolean isDirty() {
        // TODO: Placeholder implementation until support for tracking on
        // a per-project basis is implemented
//        return dirty;
        return ProjectManager.getManager().isSaveActionEnabled();
    }
    
    public void setDirty(boolean isDirty) {
        // TODO: Placeholder implementation until support for tracking on
        // a per-project basis is implemented
        dirty = isDirty;
        ProjectManager.getManager().setSaveEnabled(isDirty);
    }
}
