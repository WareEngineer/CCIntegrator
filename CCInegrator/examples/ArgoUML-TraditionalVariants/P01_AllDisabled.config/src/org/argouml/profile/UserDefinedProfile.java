// // $Id: UserDefinedProfile.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

package org.argouml.profile;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;



import org.argouml.model.Model;

import org.argouml.profile.internal.ocl.InvalidOclException;


/**
 * Represents a profile defined by the user
 * 
 * @author maurelio1234
 */
public class UserDefinedProfile extends Profile {
    
    private String displayName;

    private File modelFile;

    private Collection profilePackages;

    private UserDefinedFigNodeStrategy figNodeStrategy 
        = new UserDefinedFigNodeStrategy();

    private class UserDefinedFigNodeStrategy implements FigNodeStrategy {

        private Map<String, Image> images = new HashMap<String, Image>();

        public Image getIconForStereotype(Object stereotype) {
            return images.get(Model.getFacade().getName(stereotype));
        }

        /**
         * Adds a new descriptor to this strategy
         * 
         * @param fnd
         */
        public void addDesrciptor(FigNodeDescriptor fnd) {
            images.put(fnd.stereotype, fnd.img);
        }
    }

    private class FigNodeDescriptor {
        private String stereotype;

        private Image img;

        private String src;

        private int length;

        /**
         * @return if this descriptor ir valid
         */
        public boolean isValid() {
            return stereotype != null && src != null && length > 0;
        }
    }

    /**
     * The default constructor for this class
     * 
     * @param file the file from where the model should be read
     * @throws ProfileException if the profile could not be loaded
     */
    public UserDefinedProfile(File file) throws ProfileException {
        
        displayName = file.getName();
        modelFile = file;
        ProfileReference reference = null;
        try {
            reference = new UserProfileReference(file.getPath());
        } catch (MalformedURLException e) {
            throw new ProfileException(
                    "Failed to create the ProfileReference.", e);
        }
        profilePackages = new FileModelLoader().loadModel(reference);

        finishLoading();
    }

    /**
     * A constructor that reads a file from an URL
     * 
     * @param url the URL
     * @throws ProfileException if the profile can't be read or is not valid
     */
    public UserDefinedProfile(URL url) throws ProfileException {
        
        ProfileReference reference = null;
        reference = new UserProfileReference(url.getPath(), url);
        profilePackages = new URLModelLoader().loadModel(reference);

        finishLoading();
    }

    /**
     * A constructor that reads a file from an URL associated with some profiles
     * 
     * @param dn the display name of the profile
     * @param url the URL of the profile mode
     * @param critics the Critics defined by this profile
     * @param dependencies the dependencies of this profile
     * @throws ProfileException if the model cannot be loaded
     */
    public UserDefinedProfile(String dn, URL url, 
            
            Set<String> dependencies) throws ProfileException {
        
        this.displayName = dn;
        if (url != null) {
            ProfileReference reference = null;
            reference = new UserProfileReference(url.getPath(), url);
            profilePackages = new URLModelLoader().loadModel(reference);
        } else {
            profilePackages = new ArrayList(0);
        }
        
        for (String profileID : dependencies) {
            addProfileDependency(profileID);
        }

        finishLoading();
    }

    /**
     * Reads the informations defined as TaggedValues
     */
    private void finishLoading() {
        Collection packagesInProfile = filterPackages();
        
        for (Object obj : packagesInProfile) {
            // if there is only one package in the model, we should suppose it's
            // the profile model, if there is more than one, we take the ones
            // marked as <<profile>>
            if (Model.getFacade().isAModelElement(obj)
                    && (Model.getExtensionMechanismsHelper().hasStereotype(obj,
                            "profile") || (packagesInProfile.size() == 1))) {

                // load profile name
                String name = Model.getFacade().getName(obj);
                if (name != null) {
                    displayName = name;
                } 
                
                
                // load profile dependencies
                String dependencyListStr = Model.getFacade()
                        .getTaggedValueValue(obj, "Dependency");
                StringTokenizer st = new StringTokenizer(dependencyListStr,
                        " ,;:");

                String profile = null;

                while (st.hasMoreTokens()) {
                    profile = st.nextToken();
                    if (profile != null) {
                        
                        this.addProfileDependency(ProfileFacade.getManager()
                                .lookForRegisteredProfile(profile));
                    }
                }

            }
        }

        // load fig nodes
        Collection allStereotypes = Model.getExtensionMechanismsHelper()
                .getStereotypes(packagesInProfile);
        for (Object stereotype : allStereotypes) {
            Collection tags = Model.getFacade().getTaggedValuesCollection(
                    stereotype);

            for (Object tag : tags) {
                String tagName = Model.getFacade().getTag(tag);
                if (tagName == null) {
                    
                } else if (tagName.toLowerCase().equals("figure")) {
                    
                    String value = Model.getFacade().getValueOfTag(tag);
                    File f = new File(value);
                    FigNodeDescriptor fnd = null;
                    try {
                        fnd = loadImage(Model.getFacade().getName(stereotype)
                                .toString(), f);
                        figNodeStrategy.addDesrciptor(fnd);
                    } catch (IOException e) {
                        
                    }
                }
            }
        }
        
    }

    /**
     * @return the packages in the <code>profilePackages</code>
     */
    private Collection filterPackages() {
        Collection ret = new ArrayList();
        for (Object object : profilePackages) {
            if (Model.getFacade().isAPackage(object)) {
                ret.add(object);
            }
        }
        return ret;
    }
    
    @SuppressWarnings("unchecked")
    private Collection<Object> getAllCommentsInModel(Collection objs) {
        Collection<Object> col = new ArrayList<Object>();
        for (Object obj : objs) {
            if (Model.getFacade().isAComment(obj)) {
                col.add(obj);
            } else if (Model.getFacade().isANamespace(obj)) {
                Collection contents = Model
                        .getModelManagementHelper().getAllContents(obj);
                if (contents != null) {
                    col.addAll(contents);
                }
            }
        }
        return col;
    }

    /**
     * @return the string that should represent this profile in the GUI.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns null. This profile has no formatting strategy.
     * 
     * @return null.
     */
    @Override
    public FormatingStrategy getFormatingStrategy() {
        return null;
    }

    /**
     * Returns null. This profile has no figure strategy.
     * 
     * @return null.
     */
    @Override
    public FigNodeStrategy getFigureStrategy() {
        return figNodeStrategy;
    }

    /**
     * @return the file passed at the constructor
     */
    public File getModelFile() {
        return modelFile;
    }

    /**
     * @return the name of the model and the file name
     */
    @Override
    public String toString() {
        File str = getModelFile();
        return super.toString() + (str != null ? " [" + str + "]" : "");
    }

    @Override
    public Collection getProfilePackages() {
        return profilePackages;
    }

    private FigNodeDescriptor loadImage(String stereotype, File f)
        throws IOException {
        FigNodeDescriptor descriptor = new FigNodeDescriptor();
        descriptor.length = (int) f.length();
        descriptor.src = f.getPath();
        descriptor.stereotype = stereotype;

        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(f));

        byte[] buf = new byte[descriptor.length];
        try {
            bis.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        descriptor.img = new ImageIcon(buf).getImage();

        return descriptor;
    }
}
