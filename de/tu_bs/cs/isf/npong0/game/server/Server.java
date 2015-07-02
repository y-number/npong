package de.tu_bs.cs.isf.npong0.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import de.tu_bs.cs.isf.npong0.game.Game;

/**
 * <p>Server Klasse f�r das n-pong Spiel.
 * Startet einen Server und stellt minimale Funktionalit�t f�r eine Verbindung bereit.
 * Kann bis zu 12 Clienten gleichzeitig bearbeiten, weitere werden abgelehnt.
 * Jeder Client bekommt einen eigenen Thread.
 * 
 * @author David Hellmers
 * @version Beta 0.1.7.0002
 */

public class Server implements Runnable {
	
	/** finales Passwort, kann nur hier im Code ge�ndert werden. */
	private static final String PASSWORD = "buggyshit1337";
	
	/** Gibt an wieviele Spieler beitreten d�rfen. */
    public static final int maxConnections = 12; // TODO Soll aus dem Optionsmen� heraus modifiziert werden k�nnen, Standardwert w�re 12.
	
    /** Gibt an wieviele Spieler vorhanden sein m�ssen bevor das Spiel beginnt. */
    public static final int minConnections = 3; // TODO Soll aus dem Optionsmen� heraus modifiziert werden k�nnen, Standardwert w�re 3.
    
	/** Der Thread, in dem der Server l�uft. */
	private Thread runningThread = null;
	
	/** Socket, auf dem der Server des Objekts l�uft. */
	private ServerSocket serverSocket = null;
	
	/** Port, auf dem der Server des Objekts l�uft. */
	private int port;
	
	/** Zeigt an, ob der Server l�uft, oder nicht. Default: false. */
	private boolean isRunning = false;
	
	/**	Das Spiel, das auf diesem Server l�uft. */
	private Game runningGame;
	
    /** Der Spielername **/
    private String playerName; // TODO Spielername soll im Controller eingegeben und an das Spiel �bertragen werde.
    
	/**
	 * <p>Erzeugt ein Server-Objekt und legt den Port des Servers fest.
	 * Der Server selbst wird nicht gestartet.
	 * 
	 * @param port Der Port des Servers.
	 * @param game Das zum Server geh�rige Spiel
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
	 * F�r jede Verbindung/jeden Clienten wird ein neuer Thread aufgemacht.
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
