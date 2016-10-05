package naturtalent.it.naturtalentapp.model;

import android.content.Context;
import android.util.Xml;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
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
                        // XML-Socketdaten komplett geparst
                        Character type = new Character(parseMap.get("type").charAt(0));

                        // mit den geparsten XML-Socketdaten realen Socket generieren
                        if(type.equals(RemoteSocketData.SOCKET_TYPE_B))
                        {
                            // Type B Socket
                            String name = parseMap.get("name");
                            short houseCode = new Short(parseMap.get("houseCode")).shortValue();
                            short remoteCode = new Short(parseMap.get("remoteCode")).shortValue();

                            RemoteSocketData socket = new RemoteSocketData(name, houseCode, remoteCode);
                            sockets.add(socket);
                        }
                    }
                }
                eventType = xpp.next();
            }

        } catch (XmlPullParserException e)
        {
            System.out.println("Fehler beim Parsen");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("Fehler beim Lesen der Datendatei");
        }

        if(sockets.isEmpty())
            sockets = getDefaultModel();

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
                serializeSocketOLD(xmlSerializer, socket);
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



    private void serializeSocketOLD (XmlSerializer xmlSerializer, RemoteSocketData socketData) throws IOException
    {

        // start Elemnt Socket
        xmlSerializer.startTag("", SOCKET_ELEMENT);

        // name
        xmlSerializer.startTag("", "name");
        xmlSerializer.text(socketData.getName());
        xmlSerializer.endTag("", "name");

        if(socketData.getType() == RemoteSocketData.SOCKET_TYPE_B)
        {
            // type
            xmlSerializer.startTag("", "type");
            xmlSerializer.text(new Character(socketData.getType()).toString());
            xmlSerializer.endTag("", "type");

            // houseCode
            xmlSerializer.startTag("", "houseCode");
            xmlSerializer.text(new Short(socketData.getHouseCode()).toString());
            xmlSerializer.endTag("", "houseCode");

            // remoteCode
            xmlSerializer.startTag("", "remoteCode");
            xmlSerializer.text(new Short(socketData.getRemoteCode()).toString());
            xmlSerializer.endTag("", "remoteCode");
        }


        // ende Element Socket
        xmlSerializer.endTag("", SOCKET_ELEMENT);
    }


    private String serializeSocket (RemoteSocketData socketData) throws IOException
    {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        // Start Document
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        // start Elemnt Socket
        xmlSerializer.startTag("", SOCKET_ELEMENT);

        // name
        xmlSerializer.startTag("", "name");
        xmlSerializer.text(socketData.getName());
        xmlSerializer.endTag("", "name");

        if(socketData.getType() == RemoteSocketData.SOCKET_TYPE_B)
        {
            // type
            xmlSerializer.startTag("", "type");
            xmlSerializer.text(new Character(socketData.getType()).toString());
            xmlSerializer.endTag("", "type");

            // houseCode
            xmlSerializer.startTag("", "houseCode");
            xmlSerializer.text(new Short(socketData.getHouseCode()).toString());
            xmlSerializer.endTag("", "houseCode");

            // remoteCode
            xmlSerializer.startTag("", "remoteCode");
            xmlSerializer.text(new Short(socketData.getRemoteCode()).toString());
            xmlSerializer.endTag("", "remoteCode");
        }

        String string = writer.toString();
        string = writer.getBuffer().toString();
        StringBuffer buf = writer.getBuffer();

        return writer.toString();
    }





    private List<RemoteSocketData> getDefaultModel()
    {
        List<RemoteSocketData> list = new ArrayList<RemoteSocketData>();
        list.add(get("Pumpe 1", (short) 1, (short) 1));
        list.add(get("Pumpe 2", (short) 1, (short) 2));
        list.add(get("Spot 1", (short) 1, (short) 4));
        list.add(get("Spot 2", (short) 1, (short) 8));

        return list;
    }


    private RemoteSocketData get(String name, short houseCode, short remoteCode)
    {
        return new RemoteSocketData(name, houseCode, remoteCode);
    }

}
