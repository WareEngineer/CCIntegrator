

// $Id: ChecklistStatus.java 39 2010-04-03 19:16:21Z marcusvnac $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.cognitive.checklist;


/**
 * A list of CheckItems that the designer has marked off as already
 * considered.  In the ArgoUML system, this determines which items
 * in the TabChecklist have checkmarks.
 * <p>
 * The only difference between this class and its superclass is that
 * adds are counted (but there is no access to the counter, so it apparently
 * is only for debugging purposes).
 *
 * @see org.argouml.cognitive.checklist.ui.TabChecklist
 * @author Jason Robbins
 */
public class ChecklistStatus extends Checklist {

    private static int numChecks = 0;

    /**
     * The constructor.
     *
     */
    public ChecklistStatus() { 
        super();
    }


    /**
     * @param item the item to be checkmarked
     * @return true
     */
    @Override
    public boolean add(CheckItem item) {
        super.add(item);
        numChecks++;
        return true;
    }
    

} 

