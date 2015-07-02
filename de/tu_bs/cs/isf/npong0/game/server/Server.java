package de.tu_bs.cs.isf.npong0.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import de.tu_bs.cs.isf.npong0.game.Game;

/**
 * <p>Server Klasse für das n-pong Spiel.
 * Startet einen Server und stellt minimale Funktionalität für eine Verbindung bereit.
 * Kann bis zu 12 Clienten gleichzeitig bearbeiten, weitere werden abgelehnt.
 * Jeder Client bekommt einen eigenen Thread.
 * 
 * @author David Hellmers
 * @version Beta 0.1.7.0002
 */

public class Server implements Runnable {
	
	/** finales Passwort, kann nur hier im Code geändert werden. */
	private static final String PASSWORD = "buggyshit1337";
	
	/** Gibt an wieviele Spieler beitreten dürfen. */
    public static final int maxConnections = 12; // TODO Soll aus dem Optionsmenü heraus modifiziert werden können, Standardwert wäre 12.
	
    /** Gibt an wieviele Spieler vorhanden sein müssen bevor das Spiel beginnt. */
    public static final int minConnections = 3; // TODO Soll aus dem Optionsmenü heraus modifiziert werden können, Standardwert wäre 3.
    
	/** Der Thread, in dem der Server läuft. */
	private Thread runningThread = null;
	
	/** Socket, auf dem der Server des Objekts läuft. */
	private ServerSocket serverSocket = null;
	
	/** Port, auf dem der Server des Objekts läuft. */
	private int port;
	
	/** Zeigt an, ob der Server läuft, oder nicht. Default: false. */
	private boolean isRunning = false;
	
	/**	Das Spiel, das auf diesem Server läuft. */
	private Game runningGame;
	
    /** Der Spielername **/
    private String playerName; // TODO Spielername soll im Controller eingegeben und an das Spiel übertragen werde.
    
	/**
	 * <p>Erzeugt ein Server-Objekt und legt den Port des Servers fest.
	 * Der Server selbst wird nicht gestartet.
	 * 
	 * @param port Der Port des Servers.
	 * @param game Das zum Server gehörige Spiel
	 */
	public Server(int port, Game game) {
		this.port = port;
		this.runningGame = game;
	}
	
	/**
	 * <p>Beendet den Server.
	 * 
	 * @param server zu beendender Server
	 * @throws IOException Kommt bei Fehlschlag
	 */
	public synchronized void stop() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			isRunning = false;
		}
	}
	
	/**
	 * <p>Run staret den Server und wartet auf eingehende Verbindungen.
	 * Für jede Verbindung/jeden Clienten wird ein neuer Thread aufgemacht.
	 */
	public void run() {
		synchronized(this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();

		while (isRunning) {
			if (runningGame.getConnectionCount() < minConnections) {
				System.out.println("Waiting for " + minConnections + "+ Players.");
			}
			
			Socket client = null;
			
			try {
				client = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				new Thread(
						new Connection(client, this, PASSWORD, runningGame)
					).start();
				System.out.println("A player has joined");
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Server stopped...");
	}
	
	/** <p>Startet den Server auf dem vordefinierten Port. */
	public void openServerSocket() {
		try {
			serverSocket = new ServerSocket(port);
			isRunning = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
