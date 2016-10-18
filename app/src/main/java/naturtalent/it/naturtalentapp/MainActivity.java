package naturtalent.it.naturtalentapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.IPConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import naturtalent.it.naturtalentapp.settings.SettingsActivity;
import naturtalent.it.naturtalentapp.socketActivity.SocketActivity;


//public class MainActivity extends AppCompatActivity
public class MainActivity extends AppCompatActivity
{
    public static MainActivity ma;

    // UID des Remote Switch Bricklet
    private static final String UID = "v1T";

    private ArrayAdapter<RemoteSocketData> adapter;

    static private Button button3;
    static private Button button4;

    // IPConnection - statisch definiert
    public static IPConnection ipcon = null;

    List<RemoteSocketData> remoteSockets;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ma = this;

        // Vebindung zum Brick herstellen
       // new WiFiConnectTask().execute(new Object[]{});


    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonSettings:

                // Settings
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

               /*
               SocketModelUtil util = new SocketModelUtil();
               util.saveSockets(this, null);
               List<RemoteSocketData> sockets = util.loadSockets(this);

               Intent intent = new Intent(this, SocketListActivity.class);
               startActivity(intent);
               */

                break;

            // die View 'Funksteckdosen' anzeigen
            case R.id.button:

                Intent intentSocket = new Intent(this, SocketActivity.class);
                startActivity(intentSocket);

                /*
                setContentView(R.layout.remote_control);
                ListView listView = (ListView) findViewById(R.id.listView);

                Spinner dropdown = (Spinner)findViewById(R.id.spinnerFilter);
                String[]items = new String[]{"all","Pumpen","Spots"};
                ArrayAdapter<String>dropDownadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(dropDownadapter);

                setupActionBar();
                */

                /*

                // button3 u. button4 sichtbar machen fuer update Status
                button3 = (Button) findViewById(R.id.button3);
                button4 = (Button) findViewById(R.id.button4);

                // mit dem Adapter ein Datenmodell der Liste zuordnen
                adapter = new InteractiveArrayAdapter(this, getModel());
                listView.setAdapter(adapter);

                // ListView an Adapter uebergeben - ermoeglicht 'updateWidget'
                // bei Checkstatusaenderungen in der ListView
                ((InteractiveArrayAdapter) adapter).setListView(listView);

                // Vebindung zum Brick herstellen
                //new WiFiConnectTask().execute(listView);

                updateWidgets(listView);
                */

                break;

            // selektierte Funksteckdosen einschalten
            case R.id.button3:
                doSwitchSocket(true);
                break;

            // selektierte Funksteckdosen ausschalten
            case R.id.button4:
                doSwitchSocket(false);
                break;

            // alle Funksteckdosen selektieren
            case R.id.button5:
                remoteSockets = ((InteractiveArrayAdapter) adapter).getList();
                if ((remoteSockets != null) && (!remoteSockets.isEmpty()))
                    setSocketViewStatus(true);
                break;

            // alle Funksteckdosen selektieren
            case R.id.button6:
                remoteSockets = ((InteractiveArrayAdapter) adapter).getList();
                if ((remoteSockets != null) && (!remoteSockets.isEmpty()))
                    setSocketViewStatus(false);
                break;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_socket, menu);
        return true;
    }

    // ein-/ausschalten
    private void doSwitchSocket(boolean switchState)
    {
        if (ipcon != null)
        {
            short switchCode = (switchState) ? BrickletRemoteSwitch.SWITCH_TO_ON : BrickletRemoteSwitch.SWITCH_TO_OFF;
            BrickletRemoteSwitch rs = new BrickletRemoteSwitch(UID, ipcon);

            List<RemoteSocketData> remoteSockets = ((InteractiveArrayAdapter) adapter).getList();
            if ((remoteSockets != null) && (!remoteSockets.isEmpty()))
            {
                for (RemoteSocketData remoteSocket : remoteSockets)
                {
                    if (remoteSocket.isSelected())
                    {
                        // nur die selektierten Sockets werden geschaltet
                        try
                        {
                            rs.switchSocketA(remoteSocket.getHouseCode(), remoteSocket.getRemoteCode(), switchCode);
                            Thread.sleep(500);
                            System.out.println(remoteSocket.getName() + " schalten");
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    private List<RemoteSocketData> getModel()
    {
        List<RemoteSocketData> list = new ArrayList<RemoteSocketData>();
        list.add(get("Pumpe Quellbecken", (short) 1, (short) 1));
        list.add(get("Skimmer", (short) 1, (short) 2));
        list.add(get("Spot Amphore", (short) 1, (short) 4));
        list.add(get("Spot Teich", (short) 1, (short) 8));

        return list;
    }

    private RemoteSocketData get(String name, short houseCode, short remoteCode)
    {
        return new RemoteSocketData(name, houseCode, remoteCode);
    }

    // Checkstatus der gelisteten Sockets setzen
    private void setSocketViewStatus(boolean status)
    {
        ListView views = (ListView) findViewById(R.id.listView);

        int count = views.getChildCount();
        for (int i = 0; i < count; i++)
        {
            View childView = views.getChildAt(i);
            Object tag = childView.getTag();
            if (tag instanceof InteractiveArrayAdapter.ViewHolder)
            {
                InteractiveArrayAdapter.ViewHolder viewHolder = (InteractiveArrayAdapter.ViewHolder) tag;
                viewHolder.checkBox.setChecked(status);
            }
        }
    }

    // die Views (Ein-Auschalteknopf) en-/disablen

    static public void updateWidgets(ListView listView)
    {
        boolean status = false;

        // Ein-Ausschalter zunaechst definiert disablen
        button3.setEnabled(false);
        button4.setEnabled(false);

        // Ein-Ausbutton en-/disablen
        int count = listView.getChildCount();
        for (int i = 0; i < count; i++)
        {
            View childView = listView.getChildAt(i);
            Object tag = childView.getTag();
            if (tag instanceof InteractiveArrayAdapter.ViewHolder)
            {
                InteractiveArrayAdapter.ViewHolder viewHolder = (InteractiveArrayAdapter.ViewHolder) tag;
                if (viewHolder.checkBox.isChecked())
                {
                    // Ein-/Aus enable, wenn mindestens 1 Socket gecheckt ist
                    button3.setEnabled(true);
                    button4.setEnabled(true);
                    return;
                }
            }
        }
    }


    private void storeSocketModel()
    {
        AssetManager manager = getAssets();
        try
        {
            InputStream is = manager.open("sockets.xml");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private void loadSocketModel()
    {
        AssetManager manager = getAssets();
        try
        {
            InputStream is = manager.open("sockets.xml");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        System.out.println("start");

    }

    @Override
    public void onStop()
    {
        super.onStop();
        System.out.println("stop");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_socketDefinition:
                startActivity(new Intent(this, SocketListActivity.class));

                //Toast.makeText(this, "Funkschalter definieren selected", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

        return true;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */

    /*
    private AppCompatDelegate mDelegate;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_socketDefinition:
                Toast.makeText(this, "Funkschalter definieren selected", Toast.LENGTH_SHORT)
                        .show();
                break;

            case android.R.id.home:
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT)
                        .show();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

        return true;
    }

    @Override
    public MenuInflater getMenuInflater()
    {
        return getDelegate().getMenuInflater();
    }

    private void setupActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public ActionBar getSupportActionBar()
    {
        return getDelegate().getSupportActionBar();
    }

    private AppCompatDelegate getDelegate()
    {
        if (mDelegate == null)
        {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
    */
}


