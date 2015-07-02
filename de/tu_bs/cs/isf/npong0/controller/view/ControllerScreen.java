/**
 * nPong App ControllerScreen
 */
package de.tu_bs.cs.isf.npong0.controller.view;

import de.tu_bs.cs.isf.npong0.controller.client.Client;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Fabian, Alexander
 * @version 1.0
 * this activity is called after the joinscreen
 * has been filled out. The class has two buttons
 * - one for each direction, a nametag(which was entered
 * on the joinscreen) and an exitbutton
 *
 */
public class ControllerScreen extends Activity implements OnTouchListener {
	TextView usersNameMessage;
	final Context context = this;
	private Button leftButton;
	private Button rightButton;
	private String serverIPTV;
	private Client client;
	/**
	 * automatisch generierte Methode
	 * die aufgerufen wird, wenn die Klasse
	 * erstellt wird
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_controller);
		    serverIPTV = getIntent().getStringExtra("ServerIP");
		    client = (Client) getIntent().getSerializableExtra("clientIntent");
		    if(client.openConnection(serverIPTV, 25123, "buggyshit1337")) {
	            usersNameMessage = (TextView) findViewById(R.id.userNameTV);
	            String usersName = getIntent().getStringExtra("UsersName");
	            usersNameMessage.setText(usersName);
	            leftButton = (Button)findViewById(R.id.left_button);
	            rightButton = (Button)findViewById(R.id.right_button);
	            leftButton.setId(1);
	            rightButton.setId(2);
	            leftButton.setOnTouchListener(this);
	            rightButton.setOnTouchListener(this);
		        }
		} catch(Exception e) {
			String errorLog = "COULDN'T CREATE CONTROLLERSCREEN \n" + e.getMessage();
			Toast.makeText(getApplicationContext(), 
                    errorLog, Toast.LENGTH_LONG).show();
		}
	}
	/**
	 *  Nach klicken des ExitButtons aktiviert sich Methode
	 *  Entscheidung Yes oder No
	 *  No beendet Dialog
	 *  Yes => ruft exitApp() auf
	 */
	public void onClickExit(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to leave the current game?")
		.setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		    	//--------TODO: specify where to go after exiting------
		        exitApp();
		    }
		 })
		 .setNegativeButton("No", new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
             }
	      });
		AlertDialog alert = builder.create();
		alert.show();
	}
	/**
	 * Zurueck in den JoinScreen
	 * springen.
	 * Verbindung zum Spiel abbrechen
	 */
	public void exitApp() {
		try{
			client.sendPressedKey(4);
			client.closeConnection();
		    Intent intent = new Intent(this, controller.JoinScreen.class);
	        startActivity(intent);
	        finish();
		} catch(Exception e) {
			String errorLog = "COULDN'T LEAVE SERVER PROPERLY \n" + e.getMessage();
			Toast.makeText(getApplicationContext(), 
                    errorLog, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, controller.JoinScreen.class);
	        startActivity(intent);
	        finish();
		}
	}
	/**
	 * BackButton beendet die Verbindung zum Spiel
	 * schließt ControllerScreen
	 * bringt dich zurueck in den JoinScreen
	 */
	@Override
	public void onBackPressed() {
		try{
		client.sendPressedKey(4);
		client.closeConnection();
		Intent intent = new Intent(this, controller.JoinScreen.class);
        startActivity(intent);
        finish();
	    super.onBackPressed();
	    } catch(Exception e) {
		    String errorLog = "COULDN'T LEAVE CONTROLLER PROPERLY \n" + e.getMessage();
		    Toast.makeText(getApplicationContext(), 
                errorLog, Toast.LENGTH_LONG).show();
		    Intent intent = new Intent(this, controller.JoinScreen.class);
            startActivity(intent);
            System.exit(0);
        }
	}
	/**
	 * sendet MotionEvent vom button zum server 
	 * as Client client 
	 * @param v
	 * @param event
	 * @return true wenn motion event wahrgenommen wurde(exit point).
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		try {
	        switch(event.getAction()){
	        case MotionEvent.ACTION_DOWN:
		        switch(v.getId()){
		        case 1: client.sendPressedKey(2);
		            break;
		        case 2: client.sendPressedKey(0);	
		            break;
		        }
	            break;
	        case MotionEvent.ACTION_UP:
		        switch(v.getId()){
		        case 1: client.sendPressedKey(3);
		            break;
		        case 2: client.sendPressedKey(1);
		            break;
		        }
	        }
		} catch(Exception e) {
			String errorLog = "COULDN'T FIND SERVER \n" + e.getMessage();
			Toast.makeText(getApplicationContext(), 
                    errorLog, Toast.LENGTH_LONG).show();
		}
	    return true;                             
	}
}
