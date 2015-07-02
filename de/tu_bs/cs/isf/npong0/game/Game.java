package de.tu_bs.cs.isf.npong0.game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.tu_bs.cs.isf.npong0.game.entity.EntityBall;
import de.tu_bs.cs.isf.npong0.game.entity.EntityEnemy;
import de.tu_bs.cs.isf.npong0.game.entity.EntityPlayer;
import de.tu_bs.cs.isf.npong0.game.entity.EntityWall;
import de.tu_bs.cs.isf.npong0.game.server.Server;
import de.tu_bs.cs.isf.npong0.game.server.Connection;
import de.tu_bs.cs.isf.npong0.game.sounds.MusicPlayer;

/**
 * <p>Hauptklasse des Spiels
 * <p>Alle Klassen kommunizieren ueber das Objekt dieser Klasse,
 * wobei derzeit noch mit einem normalen Objekt gearbeitet wird.
 * Ein Konstrukturaufruf startet demnach auch das komplette Spiel.
 * Die Klasse Pong stellt dabei ein Display, in welchem das Spiel lauft
 * und eine Menge an Methoden und Attributen, um dieses zu verwalten.
 * Ausserdem kann mit Zugriff auf das Pong Objekt auf so ziemlich jedes
 * andere Objekt und Element des Spiels zugegriffen werden.
 * <p>Der grobe Ablauf strukturiert sich dabei in start() des Servers,
 * init() des Displays, durchlaufen des gameLoops() bis das Spiel beendet
 * wird, woraufhin ein cleanUp() fuer einen sauberen Abgang sorgt.
 * 
 * @author David Hellmers
 * @author Isabelle Fricke
 * @version Alpha 0.2.1.0001
 */

public class Game {
	/**	Titel des Displays */
	public static final String TITLE ="Pong";
    /** Breite des Displays */
    public static final int DISPLAY_WIDTH = Display.getDisplayMode().getWidth();
    /** Hoehe des Displays */
    public static final int DISPLAY_HEIGHT = Display.getDisplayMode().getHeight();
    /** Gibt an wieviele aktive Bälle erlaubt sind. */
    public int maxBalls; // TODO Soll aus dem Optionsmenü heraus verändert und bei der Spielfeldgenerierung beachtet werden.
    /** Das Verhältnis von Bällen zu Spielern **/
    public int ballRatio; //TODO Soll aus dem Optionsmenü heraus gesetzt werden können.
    /** Globaler Geschwindigkeitsmosifikator für Bälle**/
	public double speedModifierBalls = 1; //TODO Soll über das Hauptmenü festgelegt werden.
    /** Globaler Geschwindigkeitsmosifikator für Schläger**/
	public double speedModifierPlayers = 1; //TODO Soll über das Hauptmenü festgelegt werden.
	/** Maximaler Punktestand bei dem das Spiel beendet wird. **/
	public int maxScore; //TODO Soll über das Hauptmenü gesetzt und in einer Methode zum beenden des Spiels verwendet werden.
	/** Häufigkeit mit der Powerups auf dem Feld erscheinen. **/
	public int powerupFrequency; //TODO Soll über das Hauptmenü gesetzt werden.
	/** Gibt an ob Powerups aktiviert oder deaktiviert sind. **/
	public boolean powerupsActive; // Soll über das Hauptmenü gesetzt werden
	/** Gibt an wie groß die Bälle sein sollen. **/
	public double ballSize; // Soll über das Hauptmenü gesetzt werden
	/** 
	 * Die zum Spiel zugehoerige World. Dort werden alle Entities gespeichert
	 * und im gameLoop() geupdated.
	 */
	public World theWorld;
	/**
	 * Liste der im Spiel vorhandenen Spielern bzw existierenden Connections.
	 * Ein Spieler aus dieser List muss nicht zwingend schon im Spiel sein,
	 * sondern kann z.B. noch warten
	 */
	private ArrayList<Connection> connectionList;
	/** Der Server, den das Spiel startet. Hier werden Verbindeungen aufgebaut */
	private Server runningServer;
	/** 
	 * Zeigt an, ob das Spiel dabei ist, eine neue Runde zu startet.
	 * Tritt nur ein, falls ein Spieler darauf wartet, der Session bezutreten.
	 */
	private boolean newRound = false;
	/** Zeigt an, ob mindestens ein Spieler darauf wartet, zu joinen. */
	private boolean newPlayer = false;
	/** Zeigt an, ob das Spiel derzeit laeuft. Dient dazu, das Spiel zu stoppen. */
	private boolean isRunning = false;
	/** Zeigt an, ob ein Spieler das Spiel verlassen hat */
	private boolean playerLeft = false;
	/** Der Musicplayer zum Apspielen von sounds. */
	private static MusicPlayer battleMusic = new MusicPlayer("sounds/battlemusic.wav", 1, 76000);
	/** Der Thread in welchem die Musik laufen wird. */
	private static Thread musicThread;
	
	/**
	 * <p>Der Konstruktor startet das Spiel, indem zuert Connections vorbereitet
	 * und der Server in einem eigenen Thread gestartet werden.
	 * Dann wird das Display mit init() erzeugt und der gameLoop() gestartet.
	 * Sobald der gameLoop() gestoppt wurde, folgt ein cleanUp() und das Spiel
	 * ist vorbei.
	 * <p>Da es pro Spiel exakt ein Pong Objekt gibt, ist dieses als Singleton
	 * zu betrachten, wenn auch die Vorteile eines solchen hier nicht weiter
	 * genutzt werden. Allerdings hat das Pong Objekt Zugriff auf jedes Element
	 * des Spiels und liegt auch im Main Thread. Tritt hier ein Fehler auf, so
	 * stuerzt das gesamte Spiel ab. 
	 */
	public Game() {
		isRunning = true;
		connectionList = new ArrayList<>();
		this.runningServer = new Server(25123, this);
		Thread serverThread = new Thread(this.runningServer);
		serverThread.start();
		
		this.init();
		this.gameLoop();
		this.cleanUp();
	}
	
	/**
	 * <p>Berechnet die Anzahl der offenen Verbindungen zu Controllern.
	 * Hier werden also auch solche Spieler gezaehlt, die noch auf den
	 * Spielbeitritt warten.
	 * @return Anzahl der offenen Verbindungen.
	 */
	public int getConnectionCount() {
		return connectionList.size();
	}
	
	/**
	 * <p>Getter Methode fuer newRound. Eine neue Runde wird dann gestaret,
	 * wenn ein Punkt erzielt wurde und neue Spieler auf Beitritt warten, oder
	 * Spieler das Spiel verlassen haben.
	 * @return true, wenn derzeit eine neue Runde gestartet wird.
	 */
	public boolean isNewRound() {
		return newRound;
	}
	
	/**
	 * <p>Getter Methode fuer newPlayer.
	 * @return true, wenn ein Spieler darauf wartet, dem Spiel beizutreten.
	 */
	public boolean hasNewPlayer() {
		return newPlayer;
	}

	/**
	 * <p>Getter Methode fuer das Server Objekt, ueber welches die Connections
	 * abgewickelt werden.
	 * @return Das zum Spiel gehoerige Server Objekt.
	 */
	public Server getRunningServer() {
		return this.runningServer;
	}
	
	/**
	 * <p>Getter Methode fuer die connectionList.
	 * @return connectionList des Spiels
	 */
	public ArrayList<Connection> getConnectionList() {
		return connectionList;
	}
	
	/**
	 * <p>Gibt die Systemzeit in Sekunden durch die Ticks pro Sekunde.
	 * Dient der Kollisionerktennung.
	 * @return Systemzeit in Sekunden durch Ticks pro Sekunde.
	 */
	public long getSystemTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * <p>Hilfsmethode, die eine zufaellige Gleitkommazahl zwischen
	 * min und max berechnet und zurueckgibt.
	 * @param min Minimum des Intervalls.
	 * @param max Maximum des Intervalls.
	 * @return Zufallszahl zwischen min und max.
	 */
	public double getRandRange (double min, double max) {
		return min + (Math.random() * max);
	}
	
	/**
	 * <p>Startet eine neue Runde, indem zuerst alle EntityListen der World
	 * per reset() neu erstellt werden und dann ein neues N-Gon und neue
	 * Baelle erzeugt werden. Danach wird newRound auf true gesetzt und
	 * 200ms gewartet, um neuen Spieler die Zeit zum joinen zu geben, bevor
	 * newPlayer, newRound und playerLeft wieder auf false gesetzt werden.
	 * <p>Potentielle Bugs koennten sein, dass ein Spieler waehrend der
	 * Wartezeit leaved und dann nicht als leaver erkannt wird (not tested)
	 */
	public void startNewRound() {
		
		if (newPlayer || playerLeft) {
			theWorld.reset();
			this.nPolygon(getConnectionCount());
		} else {
			theWorld.resetBalls();
		}
		
		EntityBall newBall = new EntityBall(this); // TODO Baelle an's N-Gon abpassen.
		theWorld.getActiveBalls().add(newBall);
		
		newRound = true;
		try {
			Thread.sleep(200);
			
			/* 
			 * TODO An dieser Stelle sollte ein Wartescreen, potentielle Animationen und
			 * evtl. ein Countdown eingebaut werden.
			 */
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		newPlayer = false;
		newRound = false;
		playerLeft = false;
	}
	
	
	/**
	 * <p>Fuegt eine neue Verbindung/Spieler in die Liste aller Verbindungen ein,
	 * sofern noch genug Platz vorhanden ist. Setzt ausserdem newPlayer auf true.
	 * @param newConnection Connection des Spielers, der sich neu verbunden hat.
	 */
	public void addConnection(Connection newConnection) {
		connectionList.add(newConnection);
		newPlayer = true;
	}
	
	/**
	 * <p>Entfernt die i-te Connection der connectionList wieder aus dem Spiel, indem
	 * sie auf null gesetzt wird. Ausserdem wird playerLeft auf true gesetzt.
	 * Wird derzeit nur von der Connection gerufen, wenn der Spieler korrekt das
	 * Spiel verlaesst, was potentiele Bugs in sich traegt. 
	 * @param i i-te Connection der connectionList, die geloeschte werden soll.
	 */
	public void removeConnection(int i) {
		connectionList.remove(i);
		for (Connection c : connectionList) {
			c.updateClientID();
		}
		playerLeft = true;
	}
	
	/**
	 * <p>Init erzeugt das World Objekt und erstellt das Fenster.
	 */
	private void init() {
		
		this.theWorld = new World(this);
		
		try {
			Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
			Display.setInitialBackground(1.0F, 1.0F, 1.0F);
			Display.setTitle(this.TITLE);
            Display.setFullscreen(true);
			Display.create();
			
			
		} catch (LWJGLException  e) {
			isRunning = false;
			e.printStackTrace();
			
		}
	}
	
	/**
	 * <p>Im gameLoop() lauft das eigentliche Spiel. In einer Schleife wird stets
	 * zuerst abgefragt, ob ein Spieler neu dazu gekommen ist oder ein Spieler das
	 * Spiel verlassen hat. Sofern das der Fall ist, die Spielerzahl mindestens 3
	 * betraegt und auch ein Punkt erzielt wurde, wird das Spielfeld angepasst und
	 * dann die naechste Runde gestartet.
	 * Danach wird das Display komplett gecleared und neu gerendered. Ueber die
	 * sync() Methode der Display Klasse wird dabei versucht auf 60 FPS zu
	 * synchronisieren.   
	 */
	private void gameLoop() {
		
		while (!Display.isCloseRequested() && isRunning) {
			if ((newPlayer || playerLeft) && getConnectionCount() >= Server.minConnections) {
				startNewRound(); // TODO Nur, wenn auch ein Punkt erzielt wurde.
			}
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			this.init2D(); // muss das eig im GameLoop sein?
			
			this.theWorld.run();
			
			Display.sync(60);
			Display.update();
		}
		
	}
	
	/**
	 * Setzt verschiedene OpenGL Variabeln.
	 */
	public void init2D() {
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0, Display.getWidth(), Display.getHeight(), 0.0, -1.0, 1.0);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.4f);
		
	}
	
	/**
	 * <p>Stoppt das Spiel, den Server, disconnected alle Spieler und kehrt dann
	 * ins Hauptmenu zurueck. 
	 */
	private void cleanUp() {
		
		isRunning = false;
		// TODO Sauberes Disconnecten der Spieler
//		for (EntityPlayer p : playerList) {
//			if (p != null)
//			p.disconnect();
//		}
		runningServer.stop();
		Display.destroy();
	}
	
	/**
	 * <p>Erzeugt ein N-Gon mit gegebenem n und fuegt entsprechend viele Spieler ein.
	 * <p>Via Einheitskreis werden die Koordinaten der Ecken des N-Gons berechnet.
	 * Gleichzeitig werden immer EntityWall und EntityPlayer Objekte erzeugt und
	 * entsprechend platziert. Dabei werden derzeit die alle EntityPlayer Objekte
	 * in den Connections ueberschrieben, was bei der Punktezaehlung beruecksichigt
	 * werden muss.
	 * @param n Anzahl der Ecken des zu erzeugenden N-Polygons.
	 * @throws IllegalArgumentException Wird geschmissen, falls n nicht der Spielerzahl entspricht.
	 */
	public void nPolygon(int n) throws IllegalArgumentException {
		if (n < 3)
			throw new IllegalArgumentException("Das N-Gon muss mindestens 3 Ecken haben");
		
		double rotation = 35.0;
		double radius = DISPLAY_HEIGHT / 2 - 30.0;
        double interiorAngle = 360.0 / n;
        double lastX = 0.0;
        double lastY = 0.0;
        double currentX = 0.0;
    	double currentY = 0.0;
    	
		for (int i = 0; i <= n; i++) {
        	double currentAngle = interiorAngle * (i + 1);
        	double deltaX = Math.sin((currentAngle + rotation) * Math.PI / 180.0) * radius; // Bogenmass
        	double deltaY = Math.cos((currentAngle + rotation) * Math.PI / 180.0) * radius;
        	currentX = DISPLAY_WIDTH / 2 + deltaX;
        	currentY = DISPLAY_HEIGHT / 2 + deltaY;
        	
        	if (i != n) {
        		EntityWall temp = new EntityWall(this, currentX, currentY);
        		theWorld.getWalls().add(temp);
        	}
        	/*
        	 * Aktuell werden nach jeder Veraenderung des Spielfelds alle Spieler neu erstellt
        	 * TODO Gleitkommazahlen koennten zu Ungenauigkeit beim letzten Spieler fuehren
        	 */
            if (i > 0) { 
//            	if (connectionList[i-1].getPlayer() == null) {
	            	EntityPlayer p = new EntityPlayer(this, connectionList.get(i-1),
	            			lastX, lastY, currentX, currentY);
	            	connectionList.get(i-1).setPlayer(p);
	            	theWorld.getActivePlayers().add(p);
//            	} else {
//            		connectionList[i-1].getPlayer().updatePosition(
//            				lastX, lastY, currentX, currentY
//            				);
//            	}
            }
            
            lastX = currentX;
            lastY = currentY;
        }
        
    }

	/**
	 * <p>Erzeugt ein Server Objekt, womit das Spiel gestartet wird.
	 * Derzeit nur Placeholder, bis ein Hauptmenu existiert.
	 * @param args nicht benutzt
	 */
	public static void main(String[] args) {
		Game main = new Game();
	}
}
