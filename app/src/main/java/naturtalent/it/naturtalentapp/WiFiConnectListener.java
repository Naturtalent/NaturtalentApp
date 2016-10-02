package naturtalent.it.naturtalentapp;

import com.tinkerforge.IPConnection;

/**
 * Created by dieter on 17.09.16.
 */
public class WiFiConnectListener implements IPConnection.ConnectedListener
{
    @Override
    public void connected(short connectedReason)
    {
        if(connectedReason == IPConnection.CONNECT_REASON_AUTO_RECONNECT)
        {
            System.out.println("ReConnect WiFi ");
        }

        else
        {
            System.out.println("Listener Connect WiFi ");
        }

    }
}
