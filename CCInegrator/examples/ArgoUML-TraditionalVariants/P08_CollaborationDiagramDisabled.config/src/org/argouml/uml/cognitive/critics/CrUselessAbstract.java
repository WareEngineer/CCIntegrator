

// $Id: CrUselessAbstract.java 41 2010-04-03 20:04:12Z marcusvnac $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.ListSet;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.util.ChildGenerator;


/** A critic to detect when a class can never have instances (of
 *
 * @author jrobbins
 *  itself of any subclasses). */
public class CrUselessAbstract extends CrUML {

    /**
     * The constructor.
     */
    public CrUselessAbstract() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.INHERITANCE);
	addSupportedGoal(Goal.getUnspecifiedGoal());
	addTrigger("specialization");
	addTrigger("isAbstract");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    @Override
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAClass(dm))) {
            return false;
        }
	Object cls = dm;
	if (!Model.getFacade().isAbstract(cls)) {
	    return false;  // original class was not abstract
	}
	ListSet derived =
	    (new ListSet(cls)).reachable(new ChildGenDerivedClasses());
        for (Object c : derived) {
	    if (!Model.getFacade().isAbstract(c)) {
		return false;  // found a concrete subclass
            }
	}
	return true; // no concrete subclasses defined, this class is "useless"
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#getCriticizedDesignMaterials()
     */
    @Override
    public Set<Object> getCriticizedDesignMaterials() {
        Set<Object> ret = new HashSet<Object>();
        ret.add(Model.getMetaTypes().getUMLClass());
        return ret;
    }
    
} /* end class CrUselessAbstract */



class ChildGenDerivedClasses implements ChildGenerator {
    public Iterator childIterator(Object o) {
        Object c = o;
        Collection specs = new ArrayList(Model.getFacade()
                .getSpecializations(c));
        if (specs == null) {
            return Collections.emptySet().iterator();
        }
        List specClasses = new ArrayList(specs.size());
        for (Object g : specs) {
            Object ge = Model.getFacade().getSpecific(g);
            if (ge != null) {
                specClasses.add(ge);
            }
        }
        return specClasses.iterator();
    }

}

