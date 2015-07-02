package de.tu_bs.cs.isf.npong0.controller.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * <p>Klasse fuer Clienten des p-pong Spiels.
 * Main Methode verbindet sich mit dem Server und spamt ein paar Befehle.
 * 
 * @author David Hellmers
 * @version Beta 0.1.5
 */

public class Client {
	
	/** Outputstream zum Server */
	private PrintWriter out;
	
	/** Inputstream zum Server */
	private Scanner in;
	
	/** Server, zu dem der Client aktuell verbunden ist */
	private Socket currentServer;
	
	/** Zeigt an, ob der Client darauf wartet, dem Spiel beizutreten. */
	private boolean waiting = true;
	
	/**
	 * Gibt zurueck, ob der Client noch darauf wartet, dass eine neue Runde beginnt.
	 * @return true, wenn der Client noch wartet.
	 */
	public boolean isWaiting() {
		return waiting;
	}
	
	/**
	 * <p>Sendet die gedrueckte Taste in Form eines Integers an den Server.
	 * <p>Bekannte Tasten:
	 * 0: "rightPressed", 1: "rightReleased",
	 * 2: "leftPressed", 3: "leftReleased",
	 * 4: "exit"
	 * 
	 * @param key KeyAction, die der Client ausgefuehrt hat
	 */
	public void sendPressedKey(int key) {
		out.println(key);
	}
	
	/**
	 * <p>Eroeffnet eine Verbindung zu der gegebenen IP mit dem gegebenen Port und Passwort.
	 * Falls der Server antwortet, dass kein Platz mehr im Spiel ist,
	 * wird die Verbindung beendet und der Socket geschlossen.
	 * 
	 * @param IP IP des Servers.
	 * @param port Port, auf dem der Server liegt.
	 * @param password Passwort, dass der Server bekommt.
	 * @return True, wenn die Verbindung erfolgreich war, false wenn nicht.
	 */
	public boolean openConnection(String IP, int port, String password) {
		
		currentServer = null;
		
		try {
			currentServer = new Socket(IP, port);
			currentServer.setSoTimeout(1000);
			out = new PrintWriter(currentServer.getOutputStream(), true);
			in = new Scanner(currentServer.getInputStream());
			
			out.println(password);
			
			while (!in.hasNextInt()) {
				Thread.sleep(100);
			}
			int greeting = in.nextInt();
			System.out.println("Waiting for Server message");
			switch (greeting) {
				case -1: // Server is voll
					System.out.println("Server full!");
					in.close();
					out.close();
					closeConnection();
					return false;
				case 0: // Neue Runde startet, der Client ist dabei.
					System.out.println("Game started!");
					waiting = false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Unterbricht die Verbindung zum aktuellen Server und setzt currentServer auf null.
	 */
	public void closeConnection() {
		
		try {
			currentServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			currentServer = null;
		}
	}
	
}
