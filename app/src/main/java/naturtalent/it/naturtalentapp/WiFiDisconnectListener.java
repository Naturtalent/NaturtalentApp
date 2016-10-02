package naturtalent.it.naturtalentapp;

import com.tinkerforge.IPConnection;

/**
 * Created by dieter on 17.09.16.
 */
public class WiFiDisconnectListener implements IPConnection.DisconnectedListener
{
    @Override
    public void disconnected(short i)
    {
        System.out.println("Disconnect WiFi "+i);
    }
}
