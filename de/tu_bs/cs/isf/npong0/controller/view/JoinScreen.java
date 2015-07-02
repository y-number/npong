/**
 * nPong App JoinScreen
 */
package de.tu_bs.cs.isf.npong0.controller.view;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;

import pong.Controller.R;
import netzwerkkomponente.Client;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * @author Fabian, Alexander
 * @version 1.0
 * Activity for JoinScreen
 * displays two Edit Texts to enter
 * name and Host-IP and Joinbutton
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class JoinScreen extends Activity implements Serializable {
	/**
	 * serial ID
	 */
	private static final long serialVersionUID = 5707477329277430575L;
	/**
	 * client für die Netzwerkkomponente
	 */
	private Client client;
	/**
	 * String Array mit Ip Vorschlägen für das IPEditText
	 */
	private static final String[] IPS = new String[] {
		"127.133.123", "1337", "11880"
	};
	/**
	 * ArrayList mit Namen Vorschlägen für das IPEditText
	 */
	ArrayList<String> playerNames = new ArrayList<String>();
	/**
	 * ArrayList mit Ip Vorschlägen für das IPEditText
	 */
	ArrayList<String> serverIPs = new ArrayList<String>();
	/**
	 * filename für die Datenspeicherung
	 */
	private final static String STORENAMES = "storenames.txt";
	/**
	 * automatisch generierte Methode
	 * @param savedInstanceState wird für Neugenierung der Activity benutzt 
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joinscreen);
		playerNames.add("Hans Peden");
		serverIP.add("1337");
		AutoCompleteTextView playerNameTV = (AutoCompleteTextView) findViewById(R.id.Name_ET);
		AutoCompleteTextView IPTV = (AutoCompleteTextView) findViewById(R.id.IP_ET);
		ArrayAdapter<String> adapterIP = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, IPS);
		ArrayAdapter<String> adapterPlayerNames = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, playerNames);
		playerNameTV.setAdapter(adapterPlayerNames);
		IPTV.setAdapter(adapterIP);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
	}
	/**
	 * erstellt den Intent, mit dem auf ControllerScreen gewechselt 
	 * und username und ip gesendet wird
	 * @return sendData Intent
	 */
	public Intent createIntent() {
		EditText usersNameEditText = (EditText) findViewById(R.id.Name_ET);
		EditText serverIPEditText = (EditText) findViewById(R.id.IP_ET);
		String usersName = String.valueOf(usersNameEditText.getText());
		String serverIP = String.valueOf(serverIPEditText.getText());
		Intent sendData = new Intent(this, controller.ControllerScreen.class);
		sendData.putExtra("ServerIP", serverIP);
		sendData.putExtra("UsersName", usersName);
		sendData.putExtra("clientIntent", client);
		return sendData;
	}
	/**
	 * OnClickListener für Joinbutton
	 * aus joinscreen.xml.
	 * Löst Sendung der Daten an ControllerScreen aus
	 * @param view
	 */
	public void onClickJoin(View view) {
		EditText serverIPEditText = (EditText) findViewById(R.id.IP_ET);
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(serverIPEditText.getWindowToken(), 0);
		this.launchRingDialog(view); //Serversuchwindow oeffnen
		this.saveClicked(view); //config txt schreiben
	}
	/**
	 * speichert eingegebene Daten in einer configfile,
	 * um sie später auszulesen und im Autocomplete zu verwenden.
	 * @param v view
	 */
	public void saveClicked(View v) {
		try { 
		    OutputStreamWriter out =
		        new OutputStreamWriter(openFileOutput(STORENAMES, 0));
		    EditText serverNameEditText = (EditText) findViewById(R.id.Name_ET);
		    String tempName = serverNameEditText.getText().toString();
		    out.write(tempName);
		    out.close(); 
		    playerNames.add(tempName);
		    Toast
		    .makeText(this, "The contents are saved in the file.", Toast.LENGTH_LONG)
		    .show();
		} catch (Throwable t) {
		    Toast
		    .makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG)
		    .show();
		}    		 
	}
	/**
	 * liest eingespeicherte Daten aus 
	 */
	public void readFileInEditor() {
	    try {
	        InputStream in = openFileInput(STORENAMES);
            if (in != null) {
	            InputStreamReader tmp = new InputStreamReader(in);
	            BufferedReader reader = new BufferedReader(tmp);
	            String str;
	            StringBuilder buf = new StringBuilder();
	            while ((str = reader.readLine()) != null) {
	                buf.append(str+"n");
	            }
	        in.close();
	        //txtEditor.setText(buf.toString());
	        }
        } catch (java.io.FileNotFoundException e) {
	 
	    } catch (Throwable t) {
	        Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG)
	        .show();
	    } 
	}
	/**
	 * launchRingDialog öffnet sich wenn Joinbutton gedrückt
	 * wird. Erzeugt einen Ladebildschirm und sendet Daten an
	 * ControllerScreen. Ruft createIntent auf, um Inten für die Daten zu erzeugen.
	 * @param view
	 */
	public void launchRingDialog(View view) {
		final ProgressDialog ringProgressDialog = ProgressDialog.show(JoinScreen.this, "Please wait ...",	"Joining Server ...", true);
		ringProgressDialog.setCancelable(true);
		//create THREAD
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					client = new Client();
					int waitingLoops = 0;
					while(waitingLoops < 10) {
						if(!client.isWaiting()) {
					        startActivity(createIntent());
					        finish();
						}
						Thread.sleep(2000);
						waitingLoops++;
					}
				} catch (Exception e) {
					String errorLog = "COULDN'T LEAVE SERVER PROPERLY \n" + e.getMessage();
					Toast.makeText(getApplicationContext(), 
		                    errorLog, Toast.LENGTH_LONG).show();
				}
				ringProgressDialog.dismiss();
			}
		}).start();
	}
	/**
	 * App beenden
	 * @param view
	 */
	public void onClickClose(View view) { 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to close the App?")
		.setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		    	System.exit(0);
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
	@Override
	public void onBackPressed() {
        //TODO!!!!
	}
}
