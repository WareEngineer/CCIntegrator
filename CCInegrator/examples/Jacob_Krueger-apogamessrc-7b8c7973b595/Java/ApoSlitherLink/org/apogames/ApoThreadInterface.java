package org.apogames;

/**
 * Das Interface, welches alle Klassen importieren m�ssen, um mit dem Timer/Thread zu kommunzieren
 * @author Dirk Aporius
 *
 */
public interface ApoThreadInterface {
	public void think( int delta );
	public void render();
	public boolean isBThink();
	public boolean isBRepaint();
	public void renderGraphics();
}
