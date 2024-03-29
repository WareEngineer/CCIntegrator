// $Id: AwtExceptionHandler.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 2009 The Regents of the University of California. All
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

package org.argouml.util.logging;


import org.apache.log4j.Logger;


/**
 * Last chance exception handler for AWT thread to make sure things get logged.
 */
public class AwtExceptionHandler {
    
    private static final Logger LOG = 
        Logger.getLogger(AwtExceptionHandler.class);
    
    /**
     * DO NOT CHANGE THIS METHOD SIGNATURE.
     * @param t the uncaught exception
     */
    public void handle(Throwable t) {
        
        try {
            LOG.error("Last chance error handler in AWT thread caught", t);
        } catch (Throwable t2) {
            // Ignore any nested exceptions. We don't want infinite loop.
        }
        
    }

    /**
     * Register our exception handler with AWT.
     */
    public static void registerExceptionHandler() {
        System.setProperty("sun.awt.exception.handler",
                AwtExceptionHandler.class.getName());
    }
}

