// $Id: MDRModelImplementation.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 2005-2008 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;


import org.apache.log4j.Logger;

import org.argouml.model.ActivityGraphsFactory;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.AggregationKind;
import org.argouml.model.ChangeableKind;
import org.argouml.model.CollaborationsFactory;
import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CommonBehaviorFactory;
import org.argouml.model.CommonBehaviorHelper;
import org.argouml.model.ConcurrencyKind;
import org.argouml.model.CoreFactory;
import org.argouml.model.CoreHelper;
import org.argouml.model.DataTypesFactory;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.DiagramInterchangeModel;
import org.argouml.model.DirectionKind;
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.Facade;
import org.argouml.model.MetaTypes;
import org.argouml.model.ModelEventPump;
import org.argouml.model.ModelImplementation;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.OrderingKind;
import org.argouml.model.PseudostateKind;
import org.argouml.model.ScopeKind;
import org.argouml.model.StateMachinesFactory;
import org.argouml.model.StateMachinesHelper;
import org.argouml.model.UUIDManager;
import org.argouml.model.UmlException;
import org.argouml.model.UmlFactory;
import org.argouml.model.UmlHelper;
import org.argouml.model.UseCasesFactory;
import org.argouml.model.UseCasesHelper;
import org.argouml.model.VisibilityKind;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.omg.uml.UmlPackage;

/**
 * The handle to find all helper and factories.
 */
public class MDRModelImplementation implements ModelImplementation {
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(MDRModelImplementation.class);
    
    private Facade theFacade;

    private ModelEventPumpMDRImpl theModelEventPump;

    private CopyHelper theCopyHelper;
    private ActivityGraphsHelper theActivityGraphsHelper;
    private CoreHelper theCoreHelper;

    private MetaTypes theMetaTypes = new MetaTypesMDRImpl();

    private ModelManagementFactory theModelManagementFactory;

    private ModelManagementHelper theModelManagementHelper;

    private StateMachinesHelper theStateMachinesHelper;

    private UmlFactory theUmlFactory;

    private UmlHelper theUmlHelper;

    private UseCasesFactory theUseCasesFactory;

    private UseCasesHelper theUseCasesHelper;
    private ActivityGraphsFactory theActivityGraphsFactory;
    private CollaborationsFactory theCollaborationsFactory;

    private CollaborationsHelper theCollaborationsHelper;

    private CommonBehaviorFactory theCommonBehaviorFactory;

    private CommonBehaviorHelper theCommonBehaviorHelper;

    private DataTypesFactory theDataTypesFactory;

    private DataTypesHelper theDataTypesHelper;

    private ExtensionMechanismsFactory theExtensionMechanismsFactory;

    private ExtensionMechanismsHelper theExtensionMechanismsHelper;

    private StateMachinesFactory theStateMachinesFactory;

    private CoreFactory theCoreFactory;

    private KindsMDRImpl theKindsObject;

    private MDRepository repository;

    /**
     * Package containing user UML model.
     */
    private UmlPackage umlPackage;

    /**
     * MOF Package containing UML metamodel (M2).
     */
    private MofPackage mofPackage;

    /**
     * Top level MOF extent.
     */
    private ModelPackage mofExtent;

    /**
     * Map of model elements to xmi.ids used to keep xmi.ids stable
     * across read/write cycles.
     */
    private Map<String, XmiReference> objectToId = 
        Collections.synchronizedMap(new HashMap<String, XmiReference>());
    
    /**
     * Set of known public IDs of models that could be used to resolve URLs 
     * from model element IDs.
     */
    private Map<String, String> public2SystemIds = 
        Collections.synchronizedMap(new HashMap<String, String>());

    private List<String> searchDirs = new ArrayList<String>();
    
    
    /**
     * Set of extents and their read-only status. 
     */
    private Map<UmlPackage, Boolean> extents = 
        new HashMap<UmlPackage, Boolean>(10, (float) .5);

    /**
     * @return Returns the root UML Factory package for user model.
     * @deprecated for 0.26. Use RefObject.refOutermostPackage instead if at all
     *             possible. In some cases (like an unqualified createClass()),
     *             additional infrastructure work is required in the ArgoUML app
     *             before this will be possible.
     */
    public UmlPackage getUmlPackage() {
        synchronized (extents) {
            
            if (umlPackage == null) {
                LOG.debug("umlPackage is null - no current extent");
            }
            
            return umlPackage;
        }
    }

    RefPackage createExtent(String name, boolean readOnly) {
        try {
            synchronized (extents) {
                UmlPackage extent = (UmlPackage) getRepository().createExtent(
                        name, getMofPackage());
                extents.put(extent, Boolean.valueOf(readOnly));

                if (!readOnly) {
                    // TODO: This will need to change when we support multiple
                    // user models.

                    // Delete the old extent first
                    if (umlPackage != null) {
                        try {
                            deleteExtentUnchecked(umlPackage);
                        } catch (InvalidObjectException e) {
                            
                            LOG.debug("User model extent already deleted");
                            
                        }
                    }
                    umlPackage = extent;
                }
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Created new " + (readOnly ? "readonly " : "")
                            + "extent " + umlPackage);
                    LOG.debug("All registered extents = "
                            + Arrays.toString(repository.getExtentNames()));
                }
                
                return extent;
            }
        } catch (CreationFailedException e) {
            
            LOG.error("Extent creation failed for " + name);
            
            return null;
        }
    }

    void deleteExtent(UmlPackage extent) {
        synchronized (extents) {
            if (umlPackage.equals(extent)) {
                // Make sure we always have a default extent.
                // The old extent will get deleted as part of creating the
                // new extent.
                createDefaultExtent();
            } else {
                deleteExtentUnchecked(extent);
            }
        }
    }

    private void deleteExtentUnchecked(UmlPackage extent) {
        synchronized (extents) {
            extents.remove(extent);
            extent.refDelete();
        }
    }
    
    Collection<UmlPackage> getExtents() {
        return Collections.unmodifiableSet(extents.keySet());
    }
    
    boolean isReadOnly(Object extent) {
        synchronized (extents) {
            Boolean result = extents.get(extent);
            if (result == null) {
                
                LOG.warn("Unable to find extent " + extent);
                
                return false;
            }
            return result.booleanValue();
        }
    }
    
    /**
     * @return MOF Package containing UML metamodel (M2).
     */
    public MofPackage getMofPackage() {
        return mofPackage;
    }

    /**
     * @return Top level MOF extent.
     */
    ModelPackage getModelPackage() {
        return mofExtent;
    }

    /**
     * @return The MDRepository.
     */
    MDRepository getRepository() {
        return repository;
    }

    static final String MOF_EXTENT_NAME = "MOF Extent";

    static final String MODEL_EXTENT_NAME = "model extent";

    /**
     * UML 1.4 metamodel definition in XMI format.
     */
    static final String METAMODEL_URL = "mof/01-02-15_Diff.xml";


    /**
     * Constructor intended for use by decorators which need to override some of
     * the bootstrapping logic. Decorators must call
     * {@link #initializeFactories} on the new model after loading the default
     * extent.
     * 
     * @param r the underlying repository implementation
     * @throws UmlException on any fatal error. Actual cause will be inclused as
     *             a nested exception
     */
    public MDRModelImplementation(MDRepository r) throws UmlException {
        repository = r;
        initializeM2();
    }
    
    /**
     * Constructor.
     *
     * @throws UmlException if construction fails.  Some possible nested 
     * exceptions include:
     * <ul>
     * <el>CreationFailedException - If the creation of the Extent fail</el>
     * <el>MalformedXMIException If the metamodel XMI is badly formed</el>
     * <el>IOException If there is a problem opening a file</el>
     * </ul>
     */
    public MDRModelImplementation() throws UmlException {
        this(getDefaultRepository());

        createDefaultExtent();
        if (umlPackage == null) {
            throw new UmlException("Could not create UML extent");
        }
        
        LOG.debug("MDR Init - created UML extent");
        
        initializeFactories(umlPackage);
    }

    private static MDRepository getDefaultRepository() {
        
        LOG.debug("Starting MDR system initialization");
        
        String storageImplementation =
            System.getProperty(
                "org.netbeans.mdr.storagemodel.StorageFactoryClassName",
                "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
        System.setProperty(
                "org.netbeans.mdr.storagemodel.StorageFactoryClassName",
                storageImplementation);

        /*
         * Set the storage id for our repository so that MofIds will be unique
         * (they are composed as "storageId":"serialNumber"). NOTE: The storage
         * manager only looks for a few property names such as the
         * StorageFactoryClassName. Everything else needs to be prefixed with
         * "MDRStorageProperty." which gets deleted from the property name
         * before it and its associated value are copied to an *internal*
         * property table separate from the system property table.
         */
        System.setProperty(
                "MDRStorageProperty.org.netbeans.mdr.persistence.memoryimpl.id",
                UUIDManager.getInstance().getNewUUID());

        // Connect to the repository
        MDRepository defaultRepository = 
            MDRManager.getDefault().getDefaultRepository();
        
        LOG.debug("MDR Init - got default repository");
        
        return defaultRepository;
    }


    private void initializeM2() throws UmlException {
        mofExtent = (ModelPackage) repository.getExtent(MOF_EXTENT_NAME);
        
        LOG.debug("MDR Init - tried to get MOF extent");
        
        // Create an extent and read in our metamodel (M2 model)
        if (mofExtent == null) {

            try {
                mofExtent = 
                    (ModelPackage) repository.createExtent(MOF_EXTENT_NAME);
            } catch (CreationFailedException e) {
                throw new UmlException(e);
            }
            
            LOG.debug("MDR Init - created MOF extent");
            
            XMIReader reader = XMIReaderFactory.getDefault().createXMIReader();
            
            LOG.debug("MDR Init - created XMI reader");
            
            String metafacade =
                System.getProperty("argouml.model.mdr.facade", METAMODEL_URL);
            URL resource = getClass().getResource(metafacade);
            try {
                reader.read(resource.toString(), mofExtent);
            } catch (IOException e) {
                throw new UmlException(e);
            } catch (MalformedXMIException e) {
                throw new UmlException(e);
            }
            
            LOG.debug("MDR Init - read UML metamodel");
            
        }

        mofPackage = null;
        for (MofPackage pkg : (Collection<MofPackage>) mofExtent
                .getMofPackage().refAllOfClass()) {
            if ("UML".equals(pkg.getName())) {
                mofPackage = pkg;
                break;
            }
        }
    }

    /**
     * Initialize factories and other sub-objects.  The default constructor
     * takes care of this automatically, but decorating implementations
     * using the non-default constructor need to call it after they
     * have loaded the default extent.
     *
     * @param up default UmlPackage instance which has been created or loaded
     */
    public void initializeFactories(UmlPackage up) {
        umlPackage = up;
        
        // Create and start event pump first so it's available for all others
        theModelEventPump = new ModelEventPumpMDRImpl(this, repository);
        theModelEventPump.startPumpingEvents();
        
        LOG.debug("MDR Init - event pump started");
        

        // DataTypes is next so it's available for Kinds, ModelManagement,
        // & Extensions
        theDataTypesFactory = new DataTypesFactoryMDRImpl(this);
        theDataTypesHelper = new DataTypesHelperMDRImpl(this);

        theKindsObject = new KindsMDRImpl(this);
        theModelManagementFactory = new ModelManagementFactoryMDRImpl(this);
        theExtensionMechanismsHelper =
            new ExtensionMechanismsHelperMDRImpl(this);
        theExtensionMechanismsFactory =
            new ExtensionMechanismsFactoryMDRImpl(this);
        
        LOG.debug("MDR Init - initialized package Extension mechanism");
        
        // Initialize remaining factories and helpers
        // (but defer heavyweight ones until needed)
        theCopyHelper = new CopyHelper(this);
        theActivityGraphsHelper = new ActivityGraphsHelperMDRImpl();
        theCoreHelper = 
            new UndoCoreHelperDecorator(new CoreHelperMDRImpl(this));
        
        LOG.debug("MDR Init - initialized package Core helper");
        
        theModelManagementHelper = new ModelManagementHelperMDRImpl(this);
        theStateMachinesHelper = new StateMachinesHelperMDRImpl(this);
        
        LOG.debug("MDR Init - initialized package StateMachines");
        
        theUseCasesFactory = new UseCasesFactoryMDRImpl(this);
        theUseCasesHelper = new UseCasesHelperMDRImpl(this);
        
        LOG.debug("MDR Init - initialized package Use Cases");
        
        theActivityGraphsFactory = new ActivityGraphsFactoryMDRImpl(this);
            
        LOG.debug("MDR Init - initialized package Collaborations");
            
        theCommonBehaviorFactory = new CommonBehaviorFactoryMDRImpl(this);
        theCommonBehaviorHelper = new CommonBehaviorHelperMDRImpl(this);
        
        LOG.debug("MDR Init - initialized package CommonBehavior");
        
        theStateMachinesFactory = new StateMachinesFactoryMDRImpl(this);
        theCoreFactory = new CoreFactoryMDRImpl(this);
        
        LOG.debug("MDR Init - all packages initialized");
        
    }


    RefPackage createDefaultExtent() {
        // Create a default extent for the user UML model. This will get
        // replaced if a new model is read in from an XMI file.
        synchronized (extents) {
            umlPackage = (UmlPackage) repository.getExtent(MODEL_EXTENT_NAME);
            if (umlPackage != null) {
                // NOTE: If we switch to a persistent repository like the b-tree
                // repository we'll want to keep the old extent(s) around
                try {
                    UmlPackage oldPackage = umlPackage;
                    umlPackage = null;
                    deleteExtentUnchecked(oldPackage);
                    
                    LOG.debug("MDR Init - UML extent existed - "
                            + "deleted it and all UML data");
                    
                } catch (InvalidObjectException e) {
                    
                    LOG.debug("Got error deleting old default user extent");
                    
                }
            }
            umlPackage = (UmlPackage) createExtent(MODEL_EXTENT_NAME, false);
            
            LOG.debug("Created default extent");
            
            return umlPackage;
        }
    }

    /**
     * Shutdown repository in a graceful fashion
     * (currently unused).
     */
    public void shutdown() {
        theModelEventPump.flushModelEvents();
        theModelEventPump.stopPumpingEvents();
        MDRManager.getDefault().shutdownAll();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getDiagramInterchangeModel()
     */
    public DiagramInterchangeModel getDiagramInterchangeModel() {
        return null;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getFacade()
     */
    public Facade getFacade() {
        if (theFacade == null) {
            theFacade = new FacadeMDRImpl(this);
        }
        return theFacade;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getModelEventPump()
     */
    public ModelEventPump getModelEventPump() {
        return theModelEventPump;
    }
    /*
     * @see org.argouml.model.ModelImplementation#getActivityGraphsFactory()
     */
    public ActivityGraphsFactory getActivityGraphsFactory() {
        return theActivityGraphsFactory;
    }
    /*
     * @see org.argouml.model.ModelImplementation#getActivityGraphsHelper()
     */
    public ActivityGraphsHelper getActivityGraphsHelper() {
        return theActivityGraphsHelper;
    }
    /*
     * @see org.argouml.model.ModelImplementation#getCollaborationsFactory()
     */
    public CollaborationsFactory getCollaborationsFactory() {
        if (theCollaborationsFactory == null) {
            theCollaborationsFactory =
                new CollaborationsFactoryMDRImpl(this);
        }
        return theCollaborationsFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCollaborationsHelper()
     */
    public CollaborationsHelper getCollaborationsHelper() {
        if (theCollaborationsHelper == null) {
            theCollaborationsHelper =
                new CollaborationsHelperMDRImpl(this);
        }
        return theCollaborationsHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCommonBehaviorFactory()
     */
    public CommonBehaviorFactory getCommonBehaviorFactory() {
        return theCommonBehaviorFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCommonBehaviorHelper()
     */
    public CommonBehaviorHelper getCommonBehaviorHelper() {
        return theCommonBehaviorHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCopyHelper()
     */
    public org.argouml.model.CopyHelper getCopyHelper() {
        return theCopyHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCoreFactory()
     */
    public CoreFactory getCoreFactory() {
        return theCoreFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCoreHelper()
     */
    public CoreHelper getCoreHelper() {
        return theCoreHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getDataTypesFactory()
     */
    public DataTypesFactory getDataTypesFactory() {
        return theDataTypesFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getDataTypesHelper()
     */
    public DataTypesHelper getDataTypesHelper() {
        return theDataTypesHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getExtensionMechanismsFactory()
     */
    public ExtensionMechanismsFactory getExtensionMechanismsFactory() {
        return theExtensionMechanismsFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getExtensionMechanismsHelper()
     */
    public ExtensionMechanismsHelper getExtensionMechanismsHelper() {
        return theExtensionMechanismsHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getModelManagementFactory()
     */
    public ModelManagementFactory getModelManagementFactory() {
        return theModelManagementFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getModelManagementHelper()
     */
    public ModelManagementHelper getModelManagementHelper() {
        return theModelManagementHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getStateMachinesFactory()
     */
    public StateMachinesFactory getStateMachinesFactory() {
        return theStateMachinesFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getStateMachinesHelper()
     */
    public StateMachinesHelper getStateMachinesHelper() {
        return theStateMachinesHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getUmlFactory()
     */
    public UmlFactory getUmlFactory() {
        if (theUmlFactory == null) {
            theUmlFactory = new UmlFactoryMDRImpl(this);
        }
        return theUmlFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getUmlHelper()
     */
    public UmlHelper getUmlHelper() {
        if (theUmlHelper == null) {
            theUmlHelper = new UmlHelperMDRImpl(this);
        }
        return theUmlHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getUseCasesFactory()
     */
    public UseCasesFactory getUseCasesFactory() {
        return theUseCasesFactory;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getUseCasesHelper()
     */
    public UseCasesHelper getUseCasesHelper() {
        return theUseCasesHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getMetaTypes()
     */
    public MetaTypes getMetaTypes() {
        return theMetaTypes;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getChangeableKind()
     */
    public ChangeableKind getChangeableKind() {
        return theKindsObject;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getAggregationKind()
     */
    public AggregationKind getAggregationKind() {
        return theKindsObject;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getPseudostateKind()
     */
    public PseudostateKind getPseudostateKind() {
        return theKindsObject;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getScopeKind()
     */
    public ScopeKind getScopeKind() {
        return theKindsObject;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getConcurrencyKind()
     */
    public ConcurrencyKind getConcurrencyKind() {
        return theKindsObject;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getDirectionKind()
     */
    public DirectionKind getDirectionKind() {
        return theKindsObject;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getOrderingKind()
     */
    public OrderingKind getOrderingKind() {
        return theKindsObject;
    }


    public VisibilityKind getVisibilityKind() {
        return theKindsObject;
    }

    
    public XmiReader getXmiReader() throws UmlException {
        XmiReader reader = new XmiReaderImpl(this);
        return reader;
    }


    public XmiWriter getXmiWriter(Object model, OutputStream stream,
            String version) throws UmlException {
        return new XmiWriterMDRImpl(this, model, stream, version);
    }

    /**
     * Return the Object to ID Map.
     *
     * @return the map
     */
    protected Map<String, XmiReference> getObjectToId() {
        return objectToId;
    }
    
    Map<String, String> getPublic2SystemIds() {
        return public2SystemIds;
    }

    void addSearchPath(String path) {
        searchDirs.add(path);
    }

    void removeSearchPath(String path) {
        searchDirs.remove(path);
    }

    List<String> getSearchPath() {
        return searchDirs;
    }

}
