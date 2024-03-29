

// $Id: CrTooManyTransitions.java 41 2010-04-03 20:04:12Z marcusvnac $
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to detect when a state has too many ingoing and
 * outgoing transitions.
 */
public class CrTooManyTransitions extends AbstractCrTooMany {
    /**
     * Threshold.
     */
    private static final int TRANSITIONS_THRESHOLD = 10;

    /**
     * The constructor.
     */
    public CrTooManyTransitions() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.STATE_MACHINES);
	setThreshold(TRANSITIONS_THRESHOLD);
	addTrigger("incoming");
	addTrigger("outgoing");

    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *         java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(Model.getFacade().isAStateVertex(dm))) {
            return NO_PROBLEM;
        }

	Collection in = Model.getFacade().getIncomings(dm);
	Collection out = Model.getFacade().getOutgoings(dm);
	int inSize = (in == null) ? 0 : in.size();
	int outSize = (out == null) ? 0 : out.size();
	if (inSize + outSize <= getThreshold()) {
            return NO_PROBLEM;
        }
	return PROBLEM_FOUND;
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#getCriticizedDesignMaterials()
     */
    public Set<Object> getCriticizedDesignMaterials() {
        Set<Object> ret = new HashSet<Object>();
        ret.add(Model.getMetaTypes().getStateVertex());
        return ret;
    }
    
    /**
     * The UID.
     */
    private static final long serialVersionUID = -5732942378849267065L;

} /* end class CrTooManyTransitions */

