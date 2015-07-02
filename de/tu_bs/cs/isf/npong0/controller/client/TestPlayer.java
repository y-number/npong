package de.tu_bs.cs.isf.npong0.controller.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * <p>Testklasse fuer die Verbindung, da das Android SDK noch nicht geht
 * Main Methode verbindet sich mit dem Server. Der Spieler kann dann alleine spielen.
 * 
 * @author David Hellmers
 * @version Beta 0.1.5
 */

public class TestPlayer implements KeyListener, Runnable {
	
	private Client player;
	private JFrame testFrame;
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				player.sendPressedKey(2);
				System.out.println("LeftPressed");
				break;
			case KeyEvent.VK_RIGHT:
				player.sendPressedKey(0);
				System.out.println("RightPressed");
				break;
		
			case KeyEvent.VK_E:
				player.sendPressedKey(4);
				System.out.println("exit");
				this.stop();
				break;
			default:
				break;
		}

	}

	private void stop() {
		this.player.closeConnection();
		testFrame.dispose();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				player.sendPressedKey(3);
				System.out.println("LeftReleased");
				break;
			case KeyEvent.VK_RIGHT:
				player.sendPressedKey(1);
				System.out.println("RightReleased");
				break;
		}

	}
	
	@Override
	public void run() {
		testFrame = new JFrame("Testfield");
		testFrame.setSize(200, 100);
		testFrame.addKeyListener(this);
		testFrame.setVisible(true);
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
		
		player = new Client();
		if (player.openConnection("127.0.0.1", 25123, "buggyshit1337")) {
			System.out.println("Verbindung erfolgreich.");
		} else {
			System.out.println("Verbindung fehlgeschlagen.");
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Wie viele TestPlayer sollen gestartet werden (1-12)? ");
		int playerCount = sc.nextInt();
		Thread[] t = new Thread[playerCount];
		for (int i = 0; i < playerCount; i++) {
			TestPlayer p = new TestPlayer();
			t[i] = new Thread(p);
			t[i].start();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
