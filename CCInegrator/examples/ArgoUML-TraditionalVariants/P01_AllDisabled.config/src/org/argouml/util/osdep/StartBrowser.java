// $Id: StartBrowser.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 2002-2006 The Regents of the University of California. All
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

package org.argouml.util.osdep;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;




/**
 * @stereotype utility
 */
public class StartBrowser {
    
    /**
     * Open an URL in the system's default browser.
     *
     * @param url string containing the given URL
     */
    public static void openUrl(String url) {
	try {
	    if (OsUtil.isWin32()) {
		Runtime.getRuntime().exec(
                        "rundll32 url.dll,FileProtocolHandler " + url);
	    }
	    else if (OsUtil.isMac()) {
		try {
		    ClassLoader cl = ClassLoader.getSystemClassLoader();
		    Class c = cl.loadClass("com.apple.mrj.MRJFileUtils");
		    Class[] argtypes = {
			String.class,
		    };
		    Method m = c.getMethod("openURL", argtypes);
		    Object[] args = {
			url,
		    };
		    m.invoke(c.newInstance(), args);
		} catch (Exception cnfe) {
		    
		    String[] commline = {
			"netscape", url,
		    };
		    Runtime.getRuntime().exec(commline);
		}
	    }
	    else {
                Runtime.getRuntime().exec("firefox " + url);
	    }
	}
	catch (IOException ioe) {
	    // Didn't work.
	    
	}

    }
    
    /**
     * Open an URL in the system's default browser.
     *
     * @param url the URL to open
     */
    public static void openUrl(URL url) {
        openUrl(url.toString());
    }

}
