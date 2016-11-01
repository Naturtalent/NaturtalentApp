package naturtalent.it.naturtalentapp.model;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import naturtalent.it.naturtalentapp.RemoteSocketData;

/**
 * Created by dieter on 02.10.16.
 */
public class SocketModelUtil
{
    static private String SOCKET_DATA_FILE = "socketdata.xml";

    static private String SOCKET_ELEMENT = "Socket";

    public static List<RemoteSocketData> remoteSockets;


    /**
     * RemoteSockets laden
     * @param context
     * @return
     */
    public List<RemoteSocketData> loadSockets (Context context)
    {
        List<RemoteSocketData> sockets = new ArrayList<RemoteSocketData>();
        String parseName = null;

        try
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(context.openFileInput(SOCKET_DATA_FILE)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while((line =  input.readLine()) != null)
                buffer.append(line);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( buffer.toString() ) );
            int eventType = xpp.getEventType();

            // sammelt alle geparsten Elemente
            Map<String, String> parseMap = new HashMap<String, String>();


            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.START_TAG)
                {
                    if(xpp.getName().equals(SOCKET_ELEMENT))
                    {
                        // Start Socket - Map loeschen
                        parseMap.clear();
                    }
                    else
                    {
                        parseName = xpp.getName();
                    }
                }

                if(eventType == XmlPullParser.TEXT)
                {
                    if(parseName != null)
                    {
                        parseMap.put(parseName, xpp.getText());
                        parseName = null;
                    }
                }

                if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equals(SOCKET_ELEMENT))
                    {
                        // jetzt ist XML-Socketdaten komplett geparst
                        String type = parseMap.get("type");

                        // mit den geparsten XML-Socketdaten realen Socket generieren
                        if(type.equals(RemoteSocketData.SOCKET_TYPE_A))
                        {
                            // Type A Socket (Haus- und Remote(Receiver)code
                            RemoteSocketData socket = new RemoteSocketData(parseMap.get("name"), RemoteSocketData.SOCKET_TYPE_A, parseMap.get("houseCode"), parseMap.get("remoteCode"));
                            sockets.add(socket);
                        }
                    }
                }
                eventType = xpp.next();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(context, "Fehler beim Lesen der Datendatei\nEs werden Beispiele benutzt", Toast.LENGTH_SHORT).show();
            sockets = null;
        }

        // mindestens die Defaultwerte laden
        if((sockets == null) || (sockets.isEmpty()))
            sockets = getDefaultModel();

        sockets = getDefaultModel();

        remoteSockets = sockets;
        return sockets;
    }


    /**
     * RemoteSockets speichern
     * @param context
     * @param sockets
     */
    public void saveSockets (Context context, List<RemoteSocketData> sockets)
    {
        if(sockets == null)
            sockets = getDefaultModel();

        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try
        {
            xmlSerializer.setOutput(writer);

            // Start Document
            xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            xmlSerializer.startDocument("UTF-8", true);

            // Open Tag <sockets>
            xmlSerializer.startTag("", "RemoteSockets");

            for(RemoteSocketData socket : sockets)
            {
                serializeSocket(xmlSerializer, socket);
                xmlSerializer.flush();
            }

            // Ende der Sockets
            xmlSerializer.endTag("", "RemoteSockets");

            // Ende des Dokuments
            xmlSerializer.endDocument();

            FileOutputStream openFileOutput = context.openFileOutput(SOCKET_DATA_FILE, Context.MODE_PRIVATE);
            openFileOutput.write(writer.toString().getBytes());

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    private void serializeSocket(XmlSerializer xmlSerializer, RemoteSocketData socketData) throws IOException
    {

        // start Elemnt Socket
        xmlSerializer.startTag("", SOCKET_ELEMENT);

        // name
        xmlSerializer.startTag("", "name");
        xmlSerializer.text(socketData.getName());
        xmlSerializer.endTag("", "name");

        if(socketData.getType().equals(RemoteSocketData.SOCKET_TYPE_A))
        {
            // type
            xmlSerializer.startTag("", "type");
            xmlSerializer.text(socketData.getType());
            xmlSerializer.endTag("", "type");

            // houseCode
            xmlSerializer.startTag("", "houseCode");
            xmlSerializer.text(socketData.getHouseCode());
            xmlSerializer.endTag("", "houseCode");

            // remoteCode
            xmlSerializer.startTag("", "remoteCode");
            xmlSerializer.text(socketData.getRemoteCode());
            xmlSerializer.endTag("", "remoteCode");
        }


        // ende Element Socket
        xmlSerializer.endTag("", SOCKET_ELEMENT);
    }



    // Definiert Beispieldaten
    private List<RemoteSocketData> getDefaultModel()
    {
        List<RemoteSocketData> list = new ArrayList<RemoteSocketData>();
        list.add(new RemoteSocketData("Pumpe1",RemoteSocketData.SOCKET_TYPE_A,"1","1"));
        list.add(new RemoteSocketData("Pumpe2",RemoteSocketData.SOCKET_TYPE_A,"1","2"));
        list.add(new RemoteSocketData("Skimmer",RemoteSocketData.SOCKET_TYPE_A,"1","4"));
        list.add(new RemoteSocketData("Strahler",RemoteSocketData.SOCKET_TYPE_A,"1","8"));
        return list;
    }

    /**
     * An array of sample (dummy) items.
     */
    public static final List<RemoteSocketData> ITEMS = new ArrayList<RemoteSocketData>();

    /**
     * Eine Map die SocketData unter einer ID speichert.
     * Die ID ist der Index des Datensatzes im Modell
     */
    public static final Map<String, RemoteSocketData> ITEM_MAP = new HashMap<String, RemoteSocketData>();

    private static final int COUNT = 25;


    /**
     * Ausgehen von den Socketdaten werden SocketItems definiert und in
     * ITEMS und im Map ITEM_MAP gesoeichert.
     */
    /*
    private static void initializeSocketItems()
    {
        // Itemsspeicher loeschen
        ITEMS.clear();
        ITEM_MAP.clear();

        for(int position = 0; position < remoteSockets.size(); position++)
        {
            // Abbruch, wenn max. Anzahl erreicht
            if(position > COUNT)
                break;

            // Items erzeugen, gleicher Index(Position) wie SocketData, ItemID identisch mit poition
            RemoteSocketData socket = remoteSockets.get(position);
            addItem(new RemoteSocketItem(socket.getName(),makeDetails(socket)), socket);
        }
    }
    */

    /*
     * Ein SocketItem an einer definierten Position hinzufuegen.
     */
    /*
    private static void addItem(RemoteSocketItem item, RemoteSocketData socket )
    {
        ITEMS.add(item);
        ITEM_MAP.put(socket, item);
    }
    */


    /*
     * Ein SocketItem an einer definierten Position hinzufuegen.
     */

    /*
    private static void addItem(int position, RemoteSocketItem item)
    {
        ITEMS.add(position,item);
        ITEM_MAP.put(item.id, item);
    }
    */

    /*
    public static int addRemoteSocket(RemoteSocketData socket)
    {
        socket.validate();
        remoteSockets.add(socket);

        int position = remoteSockets.size() - 1;
        addItem(new RemoteSocketItem(socket.getName(),makeDetails(socket)),socket);
        return position;
    }

    public static int removeRemoteSocket(RemoteSocketData socketData)
    {
        int position = remoteSockets.indexOf(socketData);
        remoteSockets.remove(position);

        // Items reorganisieren
        initializeSocketItems();

        return position;
    }
    */

    /**
     * SocketItem mit den mit den Socketdaten sysnchronisieren und die
     * betroffene Postion zurueckgeben.
     *
     * @param socketData
     * @return
     */

    public static int updateRemoteSocket(RemoteSocketData socketData)
    {
        socketData.validate();
        int position = remoteSockets.indexOf(socketData);

        /*
        String itemID = new Integer(position).toString();
        RemoteSocketItem item = ITEM_MAP.get(socketData);
        item.master = socketData.getName();
        item.details = makeDetails(socketData);
        */

        /*

        // altes Item loeschen
        String itemID = new Integer(position).toString();
        RemoteSocketItem item = ITEM_MAP.get(itemID);
        ITEMS.remove(item);
        ITEM_MAP.remove(itemID);

        // neues Item mit neuem Inhalt an alter Position einfuegen
        RemoteSocketItem itemUpdate = createSocketItem(position, socketData.getName());
        addItem(position, itemUpdate);

        */

        return position;
    }

    // ein einzelnes Item pro Socket generieren
    /*
    private static RemoteSocketItem createSocketItem(RemoteSocketData remoteSocketData)
    {
        return new RemoteSocketItem(remoteSocketData.getName(),makeDetails(remoteSocketData));
    }
    */

    // generiert einen String der die Detailinformation eines Sockets repraesentiert.
    public static String makeDetails(RemoteSocketData remoteSocketData)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Bezeichnung: ").append(remoteSocketData.getName());
        builder.append("\n\nType: "+remoteSocketData.getType());
        builder.append("\nHausCode: "+remoteSocketData.getHouseCode());
        builder.append("\nGeräteCode: "+remoteSocketData.getRemoteCode());

        return builder.toString();
    }

    /**
     * Item wird vom originaeren Socketdatensatz abgeleitet und benutzt um Details
     * darzustellen.
     */

    /*
    public static class RemoteSocketItem
    {
        public String master;    // Bezeichnung des Schalter (wird in Masterliste gezeigt)
        public String details;    // in einem String zusammengefasste Deteilinformation

        public RemoteSocketItem(String master, String details)
        {
            this.master = master;
            this.details = details;
        }

        @Override
        public String toString()
        {
            return master;
        }
    }
*/

    /*
    public static class RemoteSocketItem
    {
        public String id;         // identisch mit position
        public String content;    // Bezeichnung des Schalter (wird in Masterliste gezeigt)
        public String details;    // in einem String zusammengefasste Deteilinformation

        public RemoteSocketItem(String id, String content, String details)
        {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString()
        {
            return content;
        }
    }
    */

}
