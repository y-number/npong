package de.tu_bs.cs.isf.npong0.game.view;

import javax.print.DocFlavor.URL;
import javax.swing.*;

import de.tu_bs.cs.isf.npong0.game.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Klasse zur Erstellung des Hauptmenues
 * 
 * @author Serkan Acar
 * @version Alpha 0.1.0.0001
 */

public class Menu extends JFrame implements ActionListener {
	/**
     * Button, um das Spiel zu starten.
     */
	private JButton starten;
    /**
     * Button, um Spiel einzustellen.
     */
	private JButton einstellungen;
    /**
     * Button, um Highscore einzusehen.
     */
	private JButton highscore;
    /**
     * Button, um Info einzusehen.
     */
	private JButton info;
    /**
     * Button, um das Menue zu verlassen
     */
	private JButton ende;
    /**
     * Jlabel
     */
	JLabel l1;
    /**
     * Erstellt das Menuefenster
     * @param args wird nicht genutzt
     */
	public static void main(String[] args) {
		Menu frame = new Menu("N-Pong Menü");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);		
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		JLabel background = new JLabel(new ImageIcon(Menu.class.getResource("images/menubackground.jpg")));
		frame.add(background);
		background.setLayout(new FlowLayout());
	}
    /**
     * Erzeugt alle Hauptschaltflächen des Menues
     * @param String Der Titel
     */
	public Menu(String title) {
		super(title);
		
		starten = new JButton("Spiel starten");
		starten.setBounds(300,170,200,40);
		starten.addActionListener(this);
		add(starten);
		
		einstellungen = new JButton("Einstellungen");
		einstellungen.setBounds(300,240,200,40);
		einstellungen.addActionListener(this);
		add(einstellungen);
		
		highscore = new JButton("Highscore");
		highscore.setBounds(300,310,200,40);
		highscore.addActionListener(this);
		add(highscore);
		
		info = new JButton("Credits");
		info.setBounds(300,380,200,40);
		info.addActionListener(this);
		add(info);
		
		ende = new JButton("Spiel Beenden");
		ende.setBounds(300,450,200,40);
		ende.addActionListener(this);
		add(ende);
		
		
		
	}
    /**
     * ActionListener zum Einlesen von Inputs
     * @param ActionEvent e
     */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == starten ) {
			fenster();
		}
		
		if (e.getSource() == info) {
			Object[] options = { "OK" };
			
			JOptionPane.showOptionDialog(null, "Programmiert von: \nAcar, Serkan\nAhrens, Jonas\nFilbert, Marc\nFricke, Isabelle\nGrotjahn, Alexander\nHellmers, David\nMöhlenbrock, Tristan\nSchmidt, Fabian\n\n© N-Pong TU-Braunschweig ", "Information",

			        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,

			        null, options, options[0]);
		}
		
		if (e.getSource() == einstellungen) {
		
		//	auswahl();
		}
		if (e.getSource() == highscore) {
			
			//	auswahl();
			}
		
		if (e.getSource() == ende) {
			System.exit(0);
		}
	}
    /**
     * Erstellt ein neues Spiel
     */
	public static void fenster() {
		 new Game();
	}


}