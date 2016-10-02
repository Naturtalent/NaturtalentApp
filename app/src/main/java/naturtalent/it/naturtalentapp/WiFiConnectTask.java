package naturtalent.it.naturtalentapp;

import android.os.AsyncTask;

import com.tinkerforge.IPConnection;

/**
 * Created by dieter on 30.08.16.
 */
public class WiFiConnectTask extends AsyncTask
{

    //private static final String HOST = "localhost";
    public static final String HOST = "wifi-extension-v1";
    public static final int PORT = 4223;

    // UID des Remote Switch Bricklet
    //private static final String UID = "v1T";


    // IPConnection - statisch definiert
    private static IPConnection ipcon = null;


    @Override
    protected Object doInBackground(Object[] objects)
    {
        ipcon = new IPConnection();


        while(true)
        {
            try
            {
                System.out.println("Start Connect");
                ipcon.connect(HOST, PORT);

                break;

            } catch (Exception e)
            {
                System.out.println("exception Connect");
            }

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
            }
        }

       MainActivity.ma.ipcon = ipcon;
       ipcon.addConnectedListener(new WiFiConnectListener());
       ipcon.addDisconnectedListener(new WiFiDisconnectListener());

       System.out.println("End Connect");
       return ipcon;

    }

    public static IPConnection getIpcon()
    {
        return ipcon;
    }
}
