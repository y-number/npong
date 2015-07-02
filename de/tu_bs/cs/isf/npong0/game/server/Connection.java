package de.tu_bs.cs.isf.npong0.game.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import de.tu_bs.cs.isf.npong0.game.entity.EntityPlayer;
import de.tu_bs.cs.isf.npong0.game.entity.EntityScore;
import de.tu_bs.cs.isf.npong0.game.Game;
import de.tu_bs.cs.isf.npong0.game.keys.Key;
import de.tu_bs.cs.isf.npong0.game.keys.KeyAction;

/**
 * <p>Eine Klasse um eine einzelne Verbindung mit einem Clienten zu bearbeiten
 * Es werden Befehle vom Clienten eingelesen und entsprechend bearbeitet.
 * Der Client kann mit dem "exit" Befehl die Verbindung beenden.
 * 
 * @author David Hellmers
 * @version Beta 0.1.7.0001
 */

public class Connection implements Runnable {
    
    /** Passwort, das der Server vorgibt. */
    private final String PASSWORD;

	/** Der Client, der die Verbindung aufgebaut hat. */
    private Socket client;
    
    /** Der Server, zu dem die Verbindung besteht. */
    private Server server;

    /** Inputstream vom Clienten */
    private Scanner in;
    
    /** Outputstream zum Clienten */
    private PrintWriter out;
    
    /** Der Balken, den der Client steuert. */
    private EntityPlayer player;
    
    /** Die Punkteanzeige mit der die Punke eines Clients wiedergegeben werden. */
    private EntityScore score;
    
	/**	Das Spiel, auf dem der Client spielt. */
    private Game runningGame;
    
    /** Unique ID der Connection im Spiel. */
    private int clientID;
    
    /** Zeigt an, ob rechts gerade gedrueckt wird */
    private boolean rightDown = false;
    
    /** Zeigt an, ob links gerade gedrueckt wird */
    private boolean leftDown = false;
    
    /** Gibt wieder wieviele Punkte ein Spieler besitzt. */
    private static int points;
    
    /**
     * <p>Konsturktor, speichert Server, Clienten und das vom Server vorgegebene Passwort.
     * Außerdem wird des Hauptspiel, zu dem sich der Spieler verbindet, gespeichert.
     * 
     * @param client Der Client, der die Verbindung aufgebaut hat.
     * @param server Der Server, zu dem die Verbindung besteht.
     * @param password Das vom Server vorgegebene Passwort.
     * @param runningGame Das Spiel, zu dem sich verbunden wurde.
     */
    public Connection(Socket client, Server server, String password, Game runningGame) {
        this.client = client;
        this.server = server;
        this.PASSWORD = password;
        this.runningGame = runningGame;
    }
    
    /**
     * Gibt zurueck, ob der abgefragte Key gerade gedrueckt wird
     * Derzeit sind alle Keys der Key Klasse bekannt.
     * @param key Key, der abgefragt wird.
     * @return true, wenn der Key gerade gedrueckt wird.
     */
    public boolean isKeyDown(Key key) {
    	switch (key) {
    		case RIGHT:
    			return rightDown;
    		case LEFT:
    			return leftDown;
			default:
				return false;
    	}
    }
    
    /**
     * Gibt den zur Connection zugeörigen EntityPlayer zurück
     * @return EntityPlayer der Connection
     */
    public EntityPlayer getPlayer() {
    	return this.player;
    }
    
    /**
     * Gibt die unique ID der Connection zurueck
     * @return ID der Connection
     */
    public int getClientID() {
    	return this.clientID;
    }
    
    /**
     * Gibt den Punktestand des Spielers zurueck
     * @return Punktestand des Spielers
     */
    public static int getPoints() {
		return points;
	}

	/**
     * Updated die unique ID der Connection. Wird genutzt, wenn Spieler geleaved haben.
     */
    public void updateClientID() {
    	this.clientID = runningGame.getConnectionList().indexOf(this);
    }
    
    /**
     * Setzt den zur Connection zugehörigen Spieler
     * Wird gerufen, wenn eine neue Runde beginnt.
     * @param p Spieler, der gesetzt wird
     */
    public void setPlayer(EntityPlayer p) {
    	this.player = p;
    }
    
    /**
     * Setzt die zur Connection zugehörige Punkteanzeige
     * Wird gerufen, wenn eine neue Runde beginnt.
     * @param p Spieler, der gesetzt wird
     */
    public void setScore(EntityScore p) {
        this.score = p;
    }
    
    /**
     * Erhöht die Punktezahl eines Spielers um 1
     */
    public int score() {
    	this.points++;
    	System.out.println("Spieler " + clientID + ": " + points);
    	return this.points;
    }
    
    /**
	 * <p>Wickelt die Verbindung mit einem Spieler ab.
	 * Erstellt einen Inputstream und fragt zuerst das Passwort ab.
	 * Nach erfolgreicher Authetifizierung wird festgestellt, ob noch Platz im Spiel ist.
	 * Falls das der Fall ist, wird die ID des Spielers berechnet und der regulaere Spielablauf beginnt.
	 * Ansonsten wird der Spieler mit einer entsprechenden Nachricht gekickt.
     * <p>Die aktuell bekannten Servernachrichten sind:
	 * -1: Spiel ist voll, 0: Runde beginnt
	 * <p>Waehrend des Spiels wird in einer Schleife auf Befehle des Spielers gewartet und entsprechend reagiert.
	 * Mit dem "exit" Befehl kann der Spieler das Spiel beenden.
	 * Sein Platz wird dann sofort freigegeben.
	 */
    public void run() {
    	try {
			this.in = new Scanner(client.getInputStream());
			this.out = new PrintWriter(client.getOutputStream(), true);

	    	// Authentifizierung
			String password = in.nextLine();
			if (!password.equals(this.PASSWORD)) {
				in.close();
				return;
			}
			
			/* 
			 * ID des Clienten wird berechnet.
			 * Der Server sendet dann einen int-Wert, der dem aktuellen Status entspricht.
			 * -1: Spiel ist voll, 0: Runde beginnt
			 */
			this.clientID = calculateID();
			if (clientID == -1) {
				out.print(-1);
				out.close();
				in.close();
				return;
			}
			
			while(!runningGame.isNewRound()) {
				Thread.sleep(100);
				
			}
			
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}
		out.println(0);
		
		// bearbeite eingehende Befehle
		KeyAction command = KeyAction.NONE;
		KeyAction lastCommand = KeyAction.NONE;
	
		try {
			while (command != KeyAction.EXIT) {
				if (in.hasNextInt()) {
					command = KeyAction.getKey(in.nextInt());
					processInput(command, lastCommand);
					lastCommand = command;
				} else {
					this.disconnect();
				}
			}
		} catch (IllegalStateException e) {
			System.out.println("A player has left illegaly.");
		}
		
    }
	
	/**
	 * <p>Geht den playerList Array durch und sucht nach einem freien Platz.
	 * Wird einer gefunden, so wird dieser besetzt.
	 * 
	 * @return -1, falls es keinen freien Platz gibt, ansonsten den Index
	 */
	public int calculateID() {
		if (runningGame.getConnectionCount() < 12) {
			runningGame.addConnection(this);
			return runningGame.getConnectionCount() - 1;
		}
		return -1;
	}
	
	/**
	 * <p>Bearbeitet eine eingehende KeyAction.
	 * 
	 * @param command zu bearbeitender KeyAction
	 * @param lastCommand letzter gehoerter KeyAction
	 * @param clientID ID des Clienten
	 */
	public void processInput(KeyAction command, KeyAction lastCommand) {
		switch(command) {
			case RIGHTPRESSED:
				// Bewege den Balken nach rechts oder tu nichts, wenn rechts schon gedrueckt wird
				if (lastCommand != KeyAction.RIGHTPRESSED) {
					this.leftDown = false;
					this.rightDown = true;
					System.out.println("Player " + this.clientID + ": going right...");
				}
				break;
			case RIGHTRELEASED:
				// Stoppe den Balken, wenn er gerade nach rechts geht
				if (lastCommand == KeyAction.RIGHTPRESSED) {
					this.rightDown = false;
					System.out.println("Player " + this.clientID + ": stop going right...");
				}
				break;
			case LEFTPRESSED:
				// Bewege den Balken nach links oder tu nichts, wenn links schon gedrueckt wird
				if (lastCommand != KeyAction.LEFTPRESSED) {
					this.rightDown = false;
					this.leftDown = true;
					System.out.println("Player " + this.clientID + ": going left...");
				}
				break;
			case LEFTRELEASED:
				// Stoppe den Balken, wenn er gerade nach links geht
				if (lastCommand == KeyAction.LEFTPRESSED) {
					this.leftDown = false;
					System.out.println("Player " + this.clientID + ": stop going left...");
				}
				break;
			case EXIT:
				// Spiel verlassen
				this.disconnect();
				break;
			default:
				// mache nichts (Test)
				this.rightDown = false;
				this.leftDown = false;
				break;
		}
	}
	
	/**
	 * Ein Versuch, das Beenden des Spiels etwas eleganter zu machen.
	 */
	public synchronized void disconnect() {
		this.rightDown = false;
		this.leftDown = false;
		this.player.leave();
		this.runningGame.removeConnection(this.clientID);
		System.out.println("Player " + this.clientID + ": exiting the game...");
		
		this.in.close();
		this.out.close();
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
