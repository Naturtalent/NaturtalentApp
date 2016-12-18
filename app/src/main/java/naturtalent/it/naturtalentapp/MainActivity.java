package naturtalent.it.naturtalentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import naturtalent.it.naturtalentapp.model.SocketModelUtil;
import naturtalent.it.naturtalentapp.settings.SettingsActivity;
import naturtalent.it.naturtalentapp.socketActivity.ConnectionActivity;
import naturtalent.it.naturtalentapp.socketActivity.ExampleSocketsActivity;
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
    public IPConnection ipcon = null;

    List<RemoteSocketData> remoteSockets;

    // WiFi - Connetionparameter
    /*
    public static final String DEFAULT_WIFI_PREFERNVE_HOST = "wifi-extension-v1";
    private String HOST = DEFAULT_WIFI_PREFERNVE_HOST;
    public static final String DEFAULT_WIFI_PREFERNVE_PORT = "4223";
    private String PORT = DEFAULT_WIFI_PREFERNVE_PORT;
    */

    private static WiFiConnectTask connectTask;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ma = this;


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String host = preferences.getString("wifi_name","");
        String port = preferences.getString("wifi_port","");
        System.out.println("Host: "+host+"  Port:"+port);



        //Vebindung zum Brick herstellen
        //if(ipcon == null)
            connect();







        //new WiFiConnectTask().execute(new Object[]{});

       // String s = settings.getString("wifi_name",R.string.pref_default_wifiname_value);
       // System.out.println(s);

/*
        Preference preference = bindPreferenceMap.get(R.string.pref_wifiname_key);
        if(preference != null)
        {
            SharedPreferences sharedPreferences = preference.getSharedPreferences();
            String host = sharedPreferences.getString("wifi_name",MainActivity.DEFAULT_WIFI_PREFERNVE_HOST);
            MainActivity.ma.setHOST(host);
        }
        */

        //Toast.makeText(this, "Host: "+HOST, Toast.LENGTH_SHORT).show();


    }

    public void connect()
    {
        if(connectTask != null)
        {
            connectTask.setRunning(false);

            try
            {
                System.out.println("Reconnect");
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
            }
        }

        Toast.makeText(ma, "Verbindung herstellen", Toast.LENGTH_SHORT).show();
        connectTask = new WiFiConnectTask();
        connectTask.execute(new Object[]{});
    }

    private class WiFiConnectTask extends AsyncTask
    {
        private boolean running = true;
        private String host;
        private String port;

        @Override
        protected IPConnection doInBackground(Object[] objects)
        {
            ipcon = new IPConnection();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            host = preferences.getString("wifi_name","");
            port = preferences.getString("wifi_port","");

            while (true)
            {

                if(!running)
                {
                    ipcon = null;
                    break;
                }

                try
                {
                    System.out.println(ipcon+"  Start Connect  Host: "+host+"  Port: "+port);
                    ipcon.connect(host, new Integer(port).intValue());

                    break;

                } catch (Exception e)
                {
                    System.out.println("exception Connect");
                    e.printStackTrace();
                }

                try
                {
                    Thread.sleep(500);
                } catch (InterruptedException e)
                {
                }
            }

            return ipcon;
        }

        public void setRunning(boolean running)
        {
            this.running = running;
        }

        @Override
        protected void onPostExecute(Object Result)
        {
            if(ipcon != null)
                Toast.makeText(ma, "Verbindung hergestellt mit Host: "+host+" Port: "+port, Toast.LENGTH_SHORT).show();
        }
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

                Object test = intentSocket.getClass();
                adapter = new InteractiveArrayAdapter(this, new SocketModelUtil().loadSockets(this));
                //ListView listView = (ListView) findViewById(R.id.socketListView);
                //listView.setAdapter(adapter);


                remoteSockets = ((InteractiveArrayAdapter) adapter).getList();




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
            /*
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
                */


        }
    }

    /**
     * Menues einbinden (z.B. Settings)
     *
     * @param menu
     * @return
     */
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
                            rs.switchSocketA(new Short(remoteSocket.getHouseCode()).shortValue(), new Short(remoteSocket.getRemoteCode()).shortValue(), switchCode);
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
        System.out.println("MainActivity - onStop()");
    }

    /**
     * Reaktion auf die Selektion eines Menuepunktes in der Toolbar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;

        switch (item.getItemId())
        {
            // Funksteckdosen definieren
            case R.id.action_socketDefinition:
                startActivity(new Intent(this, SocketListActivity.class));
                break;

            // sonstige Settings (Praeferenzen) (WiFi, Tinkerforge)
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            // Verbindungsaufbau neu starten
            case R.id.action_connection:
                intent = new Intent(this, ConnectionActivity.class);
                startActivity(intent);
                break;

            // Beispieldaten Funksteckdosen setzen
            case R.id.action_socketdefault_settings:
                intent = new Intent(this, ExampleSocketsActivity.class);
                startActivity(intent);
                break;

            // Zurueck
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


