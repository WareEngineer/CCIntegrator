// $Id: Profile.java 41 2010-04-03 20:04:12Z marcusvnac $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.argouml.cognitive.Critic;

/**
 * Abstract class representing a Profile. It contains default types and
 * presentation characteristics that can be tailored to various modeling
 * environments.
 * 
 * @author maurelio1234
 */
public abstract class Profile {
    
    private Set<String> dependencies = new HashSet<String>();
    
    /**
     * The critics provided by this profile
     */
    private Set<Critic> critics = new HashSet<Critic>();
    
    /**
     * Add a dependency on the given profile from this profile.
     * 
     * @param p the profile
     * @throws IllegalArgumentException never thrown
     */
    protected final void addProfileDependency(Profile p)
        throws IllegalArgumentException {
        addProfileDependency(p.getProfileIdentifier());
    }

    /**
     * Add a dependency on the given profile from this profile.
     * 
     * @param profileIdentifier the profile identifier
     */
    protected void addProfileDependency(String profileIdentifier) {
        dependencies.add(profileIdentifier);
    }

    /**
     * @return the dependencies
     */
    public final Set<Profile> getDependencies() {
        if (ProfileFacade.isInitiated()) {
            Set<Profile> ret = new HashSet<Profile>();
            for (String pid : dependencies) {
                Profile p = ProfileFacade.getManager()
                        .lookForRegisteredProfile(pid);
                if (p != null) {
                    ret.add(p);
                    ret.addAll(p.getDependencies());
                }
            }
            return ret;
        } else {
            return new HashSet<Profile>();
        }
    }

    /**
     * @return the ids of the dependencies
     */
    public final Set<String> getDependenciesID() {
        return dependencies;
    }

    /**
     * @return the name for this profile
     */
    public abstract String getDisplayName();

    /**
     * @return the formating strategy offered by this profile, if any. Returns
     *         <code>null</code> if this profile has no formating strategy.
     */
    public FormatingStrategy getFormatingStrategy() {
        return null;
    }

    /**
     * @return the FigNodeStrategy offered by this profile, if any. Returns
     *         <code>null</code> if this profile has no FigNodeStrategy.
     */
    public FigNodeStrategy getFigureStrategy() {
        return null;
    }

    /**
     * @return the DefaultTypeStrategy offered by this profile, if any. Returns
     *         <code>null</code> if this profile has no DefaultTypeStrategy.
     */
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return null;
    }

    /**
     * @return a collection of the top level UML Packages containing the
     *         profile.
     * @throws ProfileException if failed to get profile.
     */
    public Collection getProfilePackages() throws ProfileException {
        return new ArrayList();
    }

    /**
     * @return the display name
     */
    @Override
    public String toString() {
        return getDisplayName();
    }
    
    /**
     * @return Returns the critics defined by this profile.
     */
    public Set<Critic> getCritics() {
        return critics;
    }
    
    /**
     * @return a unique identifier for this profile
     * 
     * For technical reasons this identifier should not contain stars '*'
     */
    public String getProfileIdentifier() {
        return getDisplayName();
    }
    
    /**
     * @param criticsSet The critics to set.
     */
    protected void setCritics(Set<Critic> criticsSet) {
        this.critics = criticsSet;
    }
    
}
