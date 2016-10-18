package naturtalent.it.naturtalentapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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


    private Menu menu;

    private RemoteSocketData selectedSocket;


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
        SocketDataDialog dialog;
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
                dialog.setSocketData(selectedSocket);
                dialog.show(manager, "socketDialog");

                break;

            // Addaction
            case R.id.action_socketDefinition_add:

                dialog = new SocketDataDialog();
                dialog.setTitle("neue Funksteckdose hinzufügen");

                dialog.show(manager, "socketDialog");

                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;

            // Deleteaction
            case R.id.action_socketDefinition_delete:


                MenuItem menuItem = menu.findItem(R.id.action_socketDefinition_edit);
                if(menuItem != null)
                     menuItem.setEnabled(false);

                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Ok: Name: "+selectedSocket.getName()+"Type: "+selectedSocket.getType(), Toast.LENGTH_SHORT).show();




                break;

        }

        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView)
    {
        List<RemoteSocketData> sockets = new SocketModelUtil().loadSockets(SocketListActivity.this);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(SocketModelUtil.ITEMS));

        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    @Override
    public void onFinishUserDialog(String name)
    {
        Toast.makeText(this, "Name: "+name, Toast.LENGTH_SHORT).show();
    }


    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
    {

        //private final List<DummyContent.DummyItem> mValues;
        private final List<SocketModelUtil.RemoteSocketItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<SocketModelUtil.RemoteSocketItem> socketItems)
        {
            mValues = socketItems;
        }


        //private final List<RemoteSocketData>sockets = new SocketModelUtil().loadSockets(SocketListActivity.this);

        /*
        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items)
        {

            mValues = items;
        }
        */

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.socket_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            // id und content von RemoteSocketItem in der Liste darstellen
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // den selektierten Datensatz sichern
                    selectedSocket = SocketModelUtil.remoteSockets.get(new Integer(holder.mItem.id).intValue() - 1);

                    if (mTwoPane)
                    {
                        Bundle arguments = new Bundle();
                        arguments.putString(SocketDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        SocketDetailFragment fragment = new SocketDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.socket_detail_container, fragment)
                                .commit();
                    } else
                    {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, SocketDetailActivity.class);
                        intent.putExtra(SocketDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public SocketModelUtil.RemoteSocketItem mItem;

            public ViewHolder(View view)
            {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
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
