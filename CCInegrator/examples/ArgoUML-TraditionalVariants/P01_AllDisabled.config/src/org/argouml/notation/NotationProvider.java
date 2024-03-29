// $Id: NotationProvider.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;



import org.argouml.model.Model;

/**
 * A class that implements this abstract class manages a text
 * shown on a diagram. This means it is able to generate
 * text that represents one or more UML objects.
 * And when the user has edited this text, the model may be adapted
 * by parsing the text.
 * Additionally, a help text for the parsing is provided,
 * so that the user knows the syntax.
 * 
 * @author mvw@tigris.org
 */
public abstract class NotationProvider {
    
    private static final String LIST_SEPARATOR = ", ";
    
    /**
     * A collection of properties of listeners registered for this notation.
     * Each entry is a 2 element array containing the element and the property
     * name(s) for which a listener is registered. This facilitates easy removal
     * of a complex set of listeners.
     */
    private final Collection<Object[]> listeners = new ArrayList<Object[]>();

    /**
     * @return a i18 key that represents a help string
     *         giving an explanation to the user of the syntax
     */
    public abstract String getParsingHelp();

    
    /**
     * Utility function to determine the presence of a key. 
     * The default is false.
     * 
     * @param key the string for the key
     * @param map the Map to check for the presence 
     * and value of the key
     * @return true if the value for the key is true, otherwise false
     */
    public static boolean isValue(final String key, final Map map) {
        if (map == null) {
            return false;
        }
        Object o = map.get(key);
        if (!(o instanceof Boolean)) {
            return false;
        }
        return ((Boolean) o).booleanValue();
    }

    /**
     * Parses the given text, and adapts the modelElement and
     * maybe related elements accordingly.
     * 
     * @param modelElement the modelelement to adapt
     * @param text the string given by the user to be parsed
     * to adapt the model
     */
    public abstract void parse(Object modelElement, String text);

    /**
     * Generates a string representation for the given model element.
     * 
     * @param modelElement the base UML modelelement
     * @param args arguments that may determine the notation
     * @return the string written in the correct notation
     * @deprecated for 0.27.3 by tfmorris.  Use 
     * {@link #toString(Object, NotationSettings)}.
     */
    @Deprecated
    public abstract String toString(Object modelElement, Map args);


    /**
     * Generate a string representation for the given model element.
     * <p>
     * <em>WARNING:</em> All subclasses must implement this as if it were
     * abstract. It will become abstract in a future release after the
     * deprecation period for toString(Object, Map) expires.
     * 
     * @param modelElement the base UML element
     * @param settings settings that control rendering of the text
     * @return the string written in the correct notation
     */
    public String toString(Object modelElement,
            NotationSettings settings) {
        return toString(modelElement, Collections.emptyMap());
    }
    
    /**
     * Initialise the appropriate model change listeners 
     * for the given modelelement to the given listener.
     * Overrule this when you need more than 
     * listening to all events from the base modelelement.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     */
    public void initialiseListener(PropertyChangeListener listener, 
            Object modelElement) {
        addElementListener(listener, modelElement);
    }
    
    /**
     * Clean out the listeners registered before.
     * The default implementation is to remove all listeners 
     * that were remembered by the utility functions below.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     */
    public void cleanListener(final PropertyChangeListener listener, 
            final Object modelElement) {
        removeAllElementListeners(listener);
    }
    
    /**
     * Update the set of listeners based on the given event. <p>
     * 
     * The default implementation just removes all listeners, and then 
     * re-initialises completely - this is method 1. 
     * A more efficient way would be to dissect 
     * the propertyChangeEvent, and only adapt the listeners
     * that need to be adapted - this is method 2. <p>
     * 
     * Method 2 is explained by the code below that is commented out.
     * Method 1 is the easiest to implement, since at every arrival of an event,
     * we just remove all old listeners, and then inspect the current model, 
     * and add listeners where we need them. I.e. the advantage is 
     * that we only need to traverse the model structure in one location, i.e. 
     * the initialiseListener() method.
     * 
     * @param listener the given listener
     * @param modelElement the modelelement that we provide 
     * notation for
     * @param pce the received event, that we base the changes on
     */
    public void updateListener(final PropertyChangeListener listener, 
            Object modelElement,
            PropertyChangeEvent pce) {
        // e.g. for an operation:
        // if pce.getSource() == modelElement
        // && event.propertyName = "parameter"
        //     if event instanceof AddAssociationEvent
        //         Get the parameter instance from event.newValue
        //         Call model to add listener on parameter on change 
        //             of "name", "type"
        //     else if event instanceof RemoveAssociationEvent
        //         Get the parameter instance from event.oldValue
        //         Call model to remove listener on parameter on change 
        //             of "name", "type"
        //     end if
        // end if 
        if (Model.getUmlFactory().isRemoved(modelElement)) {
            
            return;
        }
        cleanListener(listener, modelElement);
        initialiseListener(listener, modelElement);
    }
    
    /*
     * Add an element listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected final void addElementListener(PropertyChangeListener listener, 
            Object element) {
        if (Model.getUmlFactory().isRemoved(element)) {
            
            return;
        }
        Object[] entry = new Object[] {element, null};
        if (!listeners.contains(entry)) {
            listeners.add(entry);
            Model.getPump().addModelEventListener(listener, element);
        } 
        
    }
    
    /*
     * Utility function to add a listener for a given property name 
     * and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            name of property to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener,
     *      Object, String)
     */
    protected final void addElementListener(PropertyChangeListener listener, 
            Object element, String property) {
        if (Model.getUmlFactory().isRemoved(element)) {
            
            return;
        }
        Object[] entry = new Object[] {element, property};
        if (!listeners.contains(entry)) {
            listeners.add(entry);
            Model.getPump().addModelEventListener(listener, element, property);
        } 
        
    }

    /*
     * Utility function to add a listener for an array of property names 
     * and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            array of property names (Strings) to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener,
     *      Object, String)
     */
    protected final void addElementListener(PropertyChangeListener listener, 
            Object element, String[] property) {
        if (Model.getUmlFactory().isRemoved(element)) {
            
            return;
        }
        Object[] entry = new Object[] {element, property};
        if (!listeners.contains(entry)) {
            listeners.add(entry);
            Model.getPump().addModelEventListener(listener, element, property);
        } 
        
    }
    
    /*
     * Utility function to remove an element listener 
     * and adapt the remembered list of registration.
     * 
     * @param element
     *            element to listen for changes on
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected final void removeElementListener(PropertyChangeListener listener, 
            Object element) {
        listeners.remove(new Object[] {element, null});
        Model.getPump().removeModelEventListener(listener, element);
    }
    
    /*
     * Utility function to unregister all listeners 
     * registered through addElementListener.
     * 
     * @see #addElementListener(Object, String)
     */
    protected final void removeAllElementListeners(
            PropertyChangeListener listener) {
        for (Object[] lis : listeners) {
            Object property = lis[1];
            if (property == null) {
                Model.getPump().removeModelEventListener(listener, lis[0]);
            } else if (property instanceof String[]) {
                Model.getPump().removeModelEventListener(listener, lis[0],
                        (String[]) property);
            } else if (property instanceof String) {
                Model.getPump().removeModelEventListener(listener, lis[0],
                        (String) property);
            } else {
                throw new RuntimeException(
                        "Internal error in removeAllElementListeners");
            }
        }
        listeners.clear();
    }
    
    protected StringBuilder formatNameList(Collection modelElements) {
        return formatNameList(modelElements, LIST_SEPARATOR);
    }

    protected StringBuilder formatNameList(Collection modelElements, 
            String separator) {
        StringBuilder result = new StringBuilder();
        for (Object element : modelElements) {
            String name = Model.getFacade().getName(element);
            // TODO: Any special handling for null names? append will use "null"
            result.append(name).append(separator);
        }
        if (result.length() >= separator.length()) {
            result.delete(result.length() - separator.length(), 
                    result.length());
        }
        return result;
    }
}
