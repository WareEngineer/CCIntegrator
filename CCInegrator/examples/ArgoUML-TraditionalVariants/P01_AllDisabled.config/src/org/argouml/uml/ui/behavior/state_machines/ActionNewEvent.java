// $Id: ActionNewEvent.java 127 2010-09-25 22:23:13Z marcusvnac $
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

// $header$
package org.argouml.uml.ui.behavior.state_machines;

import java.awt.event.ActionEvent;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * Abstract action to create new events.
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class ActionNewEvent extends AbstractActionNewModelElement {

    /**
     * The constant defining the role the event to be created plays for its
     * parent. For example, if one wishes to create a trigger event for a
     * transition, this is filled with "trigger". The values are defined in the
     * interface Roles
     */
    public static final String ROLE = "role";

    /**
     *
     *
     */
    public static interface Roles {

        /**
         * The trigger for some transition
         */
        public static final  String TRIGGER = "trigger";

        /**
         * The deferrable event key
         */
        public static final String DEFERRABLE_EVENT = "deferrable-event";

    }
    /**
     * Constructor for ActionNewEvent.
     */
    protected ActionNewEvent() {
        super();
    }
    
    /**
     * Creates the event, sets its role and namespace,
     * and navigates towards it.
     *
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object target = getTarget();
        Object model =
        	ProjectManager.getManager().getCurrentProject().getModel();
        
    }

    /**
     * @param role the role the event plays
     * @param t the transition or state to get the event for
     * @return the event
     */
    public static Object getAction(String role, Object t) {
        if (role.equals(Roles.TRIGGER)) {
            return Model.getFacade().getTrigger(t);
        }
        return null;
    }
}
