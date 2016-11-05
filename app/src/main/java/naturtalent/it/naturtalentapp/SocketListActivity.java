package naturtalent.it.naturtalentapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import naturtalent.it.naturtalentapp.databinding.ActivitySocketBinding;
import naturtalent.it.naturtalentapp.databinding.DialogSocketBinding;
import naturtalent.it.naturtalentapp.dialogs.SocketDataDialog;
import naturtalent.it.naturtalentapp.model.SocketModelUtil;

import java.util.List;

/**
 * An activity representing a list of Sockets. This activity
 * has different presentations for handset and tab implements SocketActivityContract.Viewlet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SocketDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.SocketModelUtil
 */
public class SocketListActivity extends AppCompatActivity implements SocketDataDialog.SocketNameListener
{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


    //private SimpleItemRecyclerViewAdapter.ViewHolder selectedViewHolder;

    private Menu menu;

    //private RemoteSocketData selectedSocket;

    private SimpleItemRecyclerViewAdapter viewAdapter;

    //private SimpleItemRecyclerViewAdapter.ViewHolder lastHolder;

    private int selectedPosition = 0;

    private SocketDataDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Austaush with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.socket_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.socket_detail_container) != null)
        {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.SocketModelUtil
            mTwoPane = true;
        }

    }

    /**
     * Menueitems von 'menu_socket_definition.xml' in die Toolbar einblenden
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_socket_definition, menu);
        return true;
    }

    /**
     * Reaktion auf ActionButton - Click in der Toolbar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        FragmentManager manager = getFragmentManager();

        switch (item.getItemId())
        {
            // BackAction - Zurueck nach MainActivity
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                break;

            // Editaction
            case R.id.action_socketDefinition_edit:

                dialog = new SocketDataDialog();
                dialog.setTitle("Funksteckdose bearbeiten");
                dialog.setSocketData(SocketModelUtil.remoteSockets.get(selectedPosition));
                dialog.show(manager, "socketDialog");
                //Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
                break;

            // Addaction
            case R.id.action_socketDefinition_add:

                dialog = new SocketDataDialog();
                dialog.setTitle("neue Funksteckdose hinzufügen");

                dialog.setSocketData(new RemoteSocketData("neu", RemoteSocketData.SOCKET_TYPE_A, null, null));

                dialog.show(manager, "socketDialog");

                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;

            // Deleteaction
            case R.id.action_socketDefinition_delete:

               if(selectedPosition >= 0)
                {
                    AlertDialog.Builder questionDialog = new AlertDialog.Builder(this);
                    questionDialog.setTitle("Löschung");
                    questionDialog.setMessage("Funksteckdose '"+SocketModelUtil.remoteSockets.get(selectedPosition).getName()+"' wirklich löschen ?");
                    questionDialog.setIcon(R.drawable.ic_delete_forever_black_24dp);

                    // Dialog bestaetigt
                    questionDialog.setPositiveButton("Ja",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // Datensatz entfernen
                            viewAdapter.remove();

                            // Detailseite loeschen
                            updateDetailsFragment(null);

                            // das geanderte Modell speichern
                            new SocketModelUtil().saveSockets(SocketListActivity.this.getBaseContext(), SocketModelUtil.remoteSockets);
                        }
                    });

                    // Dialog abgebrochen
                    questionDialog.setNegativeButton("Nein",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });

                    questionDialog.show();
                }

                break;
        }

        return true;
    }

    /**
     * Reaktion auf Buttonclicks (Ok/Abbruch) im Socket-Dialog
     *
     * @param view
     */
    public void onDialogClick(View view)
    {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("socketDialog");

        switch (view.getId())
        {
            case R.id.btnCancel:
                Toast.makeText(this, "Abbruch", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnOk:


                RemoteSocketData remoteSocket = dialog.getSocketData();
                if(SocketModelUtil.remoteSockets.contains(remoteSocket))
                {
                    // existierenden Datensatz updaten
                    viewAdapter.update(remoteSocket);
                }
                else
                {
                   // neuen Datensatz einfuegen
                   viewAdapter.add(remoteSocket);
                }

                //updateDetailsFragment(SocketModelUtil.findRemoteSocketItem(selectedSocket).id);
                new SocketModelUtil().saveSockets(this.getBaseContext(), SocketModelUtil.remoteSockets);



                break;
        }

        if (frag != null)
            manager.beginTransaction().remove(frag).commit();
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView)
    {
        // die Funksteckdosendaten laden und die SocketItems an den Adapter uebergeben
        List<RemoteSocketData>sockets = new SocketModelUtil().loadSockets(SocketListActivity.this);
        viewAdapter = new SimpleItemRecyclerViewAdapter(sockets);

        recyclerView.setAdapter(viewAdapter);

        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(SocketModelUtil.ITEMS));


    }

    @Override
    public void onFinishUserDialog(String name)
    {
        Toast.makeText(this, "Finish: Name: "+name, Toast.LENGTH_SHORT).show();
    }


    /**
     * Die Detailseite aktualisieren.
     *
     * @param value
     */
    private void updateDetailsFragment(String value)
    {
        if (mTwoPane)
        {
            Bundle arguments = new Bundle();
            arguments.putString(SocketDetailFragment.ARG_ITEM_ID, value);
            SocketDetailFragment fragment = new SocketDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.socket_detail_container, fragment)
                    .commit();
        }
    }


    /**
     *
     * Definition der Adapterklasse
     *
     */
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
    {

        private ViewGroup parentGroup;

        // eine Liste mit den definierten Sockets
        private final List<RemoteSocketData> mValues;

        /**
         * Konstruktion des Adapters
         *
         * Ein Adapter fuer die verfuegbaren Sockets.
         *
         * @param socketData
         */
        public SimpleItemRecyclerViewAdapter(List<RemoteSocketData> socketData)
        {
            mValues = socketData;
        }

        /**
         * Pro Zeile (Position) der Liste (RecyclerView) wird eine im Layoutfile
         * definierte View uebernommen (inflate):
         * Gespeichert wird diese View in einem neugenerierten ViewHolder.
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            this.parentGroup = parent;

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.socket_list_content, parent, false);
            return new ViewHolder(view);
        }

        /**
         * Einen konkrete Datensatz der im ViewHolder gehaltenen View(s) zuordnen.
         * Vergleichbar mit ContentLabelProvider.new Integer(selectedViewHolder.mContextIdx).intValue()
         * Wird aufgerufen durch den layout manager.
         *
         * @param holder einer Zeile zugeordneten View(s)
         * @param position Position (Index) des Datensatzes im Datenmodell
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            // View zeigt den Namen des Sockets
            RemoteSocketData socket = SocketModelUtil.remoteSockets.get(position);
            holder.mContentView.setText(socket.getName());

            // versuch
            if(selectedPosition == position)
                holder.itemView.setBackgroundColor(Color.CYAN);
            else
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);

            // der holder bekommt seinen eigenen Listener
            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    notifyItemChanged(selectedPosition);
                    selectedPosition = position;
                    notifyItemChanged(selectedPosition);

                    /*
                    if(selectedPosition >= 0)
                    {
                        View holderView = parentGroup.getChildAt(selectedPosition);
                        holderView.setBackgroundColor(Color.TRANSPARENT);
                    }

                    selectedPosition = position;

                    v.setBackgroundColor(Color.CYAN);
                    */

                    showDetails(selectedPosition);
                }
            });
        }

        /**
         * Details des selektierten Eintrags anzeigen.
         *
         * @param position
         */
        private void showDetails(int position)
        {
            if (mTwoPane)
            {
                Bundle arguments = new Bundle();
                arguments.putString(SocketDetailFragment.ARG_ITEM_ID, new Integer(position).toString());
                SocketDetailFragment fragment = new SocketDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.socket_detail_container, fragment)
                        .commit();
            } else
            {
                View v = parentGroup.getChildAt(position);

                Context context = v.getContext();
                Intent intent = new Intent(context, SocketDetailActivity.class);
                intent.putExtra(SocketDetailFragment.ARG_ITEM_ID, new Integer(position).toString());

                context.startActivity(intent);
            }
        }

        @Override
        public int getItemCount()
        {
            return mValues.size();
        }


        /**
         *
         * @param socket
         */
        public void update(RemoteSocketData socket)
        {
            // Detailseite loeschen
            showDetails(selectedPosition);
            notifyItemChanged(selectedPosition);
        }

        /**
         * einen neuen Schalter einfuegen
         *
         * @param newSocket
         */

        public void add(RemoteSocketData newSocket)
        {
            View holderView;


            selectedPosition =  (selectedPosition < 0 ) ? 0 : selectedPosition;

            //holderView = parentGroup.getChildAt(selectedPosition);
            //holderView.setBackgroundColor(Color.TRANSPARENT);



            // den neuen Datensatz
            newSocket.validate();
            SocketModelUtil.remoteSockets.add(selectedPosition, newSocket);

            // ueber den neuen Eintrag informieren
            notifyItemInserted(selectedPosition);

            // Holderbindings aktualisieren
            int n = getItemCount();
            for(int i = 0;i < n;i++)
                notifyItemChanged(i); // provoziert 'onBindViewHolder' Aufruf

            showDetails(selectedPosition);

            //holderView = parentGroup.getChildAt(selectedPosition);
            //holderView.setBackgroundColor(Color.CYAN);
        }

        /**
         * einen Schalter entfernen
         *
         */
        public void remove()
        {
            View holderView = parentGroup.getChildAt(selectedPosition);
            holderView.setBackgroundColor(Color.TRANSPARENT);

            // den selektierten Datensatz entfernen
            SocketModelUtil.remoteSockets.remove(selectedPosition);

            // ueber die geloeschte Position informieren
            notifyItemRemoved(selectedPosition);

            // Holderbindings aktualisieren
            int n = getItemCount();
            for(int i = 0;i < n;i++)
                notifyItemChanged(i); // provoziert 'onBindViewHolder' Aufruf

            selectedPosition = (-1);
        }

        /**
         *
         * Definition der Klasse ViewHolder
         * Beinhaltet alle Views die fuer die Anzeige eines Eintrags erforderlich
         *
         */
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public final View mView;
            public final TextView mContentView;

            /**
             * Konstruktion
             *
             * @param view
             */
            public ViewHolder(View view)
            {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mContentView.getText() + "'";
            }


        }
    }

}
