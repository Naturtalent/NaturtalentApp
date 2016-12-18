package naturtalent.it.naturtalentapp.socketActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.IPConnection;

import java.util.List;

import naturtalent.it.naturtalentapp.InteractiveArrayAdapter;
import naturtalent.it.naturtalentapp.MainActivity;
import naturtalent.it.naturtalentapp.R;
import naturtalent.it.naturtalentapp.RemoteSocketData;
import naturtalent.it.naturtalentapp.model.SocketModelUtil;

import static android.R.attr.host;

/**
 * Funksteckdosen ein- und ausschalten
 */
public class SocketActivity extends AppCompatActivity
{

    public static SocketActivity sa;

    private ArrayAdapter<RemoteSocketData> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socket_activity);

        sa = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        adapter = new InteractiveArrayAdapter(this, new SocketModelUtil().loadSockets(this));
        ListView listView = (ListView) findViewById(R.id.socketListView);
        listView.setAdapter(adapter);

        updateWidgets(listView);

    }

    public void onClick(View view)
    {
        List<RemoteSocketData> remoteSockets = ((InteractiveArrayAdapter) adapter).getList();

        switch (view.getId())
        {
            case R.id.btnAllSelect:

                remoteSockets = ((InteractiveArrayAdapter) adapter).getList();
                if ((remoteSockets != null) && (!remoteSockets.isEmpty()))
                    setSocketViewStatus(true);
                break;

            case R.id.btnAllDeselect:

                remoteSockets = ((InteractiveArrayAdapter) adapter).getList();
                if ((remoteSockets != null) && (!remoteSockets.isEmpty()))
                    setSocketViewStatus(false);
                break;

            // selektierte Funksteckdosen einschalten
            case R.id.button3:
                doSwitchSocket(true);
                break;

            // selektierte Funksteckdosen ausschalten
            case R.id.button4:
                doSwitchSocket(false);
                break;



        }
    }

    // ein-/ausschalten
    private void doSwitchSocket(boolean switchState)
    {
        IPConnection ipcon = MainActivity.ma.ipcon;

        if (ipcon != null)
        {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.ma);
            String uid = preferences.getString("brick_id","");

            short switchCode = (switchState) ? BrickletRemoteSwitch.SWITCH_TO_ON : BrickletRemoteSwitch.SWITCH_TO_OFF;
            BrickletRemoteSwitch rs = new BrickletRemoteSwitch(uid, ipcon);

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
                            //rs.switchSocketA(new Short(remoteSocket.getHouseCode()).shortValue(), new Short(remoteSocket.getRemoteCode()).shortValue(), switchCode);
                            //Thread.sleep(500);
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
        ListView views = (ListView) findViewById(R.id.socketListView);

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
    public void updateWidgets(ListView listView)
    {
        boolean status = false;

        Button buttonSwitchOn = (Button)findViewById(R.id.button3);
        Button buttonSwitchOff = (Button)findViewById(R.id.button4);

        // Ein-Ausschalter zunaechst definiert disablen
        buttonSwitchOn.setEnabled(false);
        buttonSwitchOff.setEnabled(false);

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
                    buttonSwitchOn.setEnabled(true);
                    buttonSwitchOff.setEnabled(true);
                    return;
                }
            }
        }
    }
}
