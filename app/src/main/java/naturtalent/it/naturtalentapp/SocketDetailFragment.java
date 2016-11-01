package naturtalent.it.naturtalentapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import naturtalent.it.naturtalentapp.model.SocketModelUtil;

//import naturtalent.it.naturtalentapp.dummy.DummyContent;

/**
 * A fragment representing a single Socket detail screen.
 * This fragment is either contained in a {@link SocketListActivity}
 * in two-pane mode (on tablets) or a {@link SocketDetailActivity}
 * on handsets.
 */
public class SocketDetailFragment extends Fragment
{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**holder.mContextIdx
     * The dummy content this fragment is presenting.
     */
    private RemoteSocketData mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SocketDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // mit dem Argument ARG_ITEM_ID wird der Index des RemoteSocketDatensatzes uebergeben
        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            if(getArguments().getString(ARG_ITEM_ID) != null)
            {
                // den indexierten Datensatz in mItem hinterlegen
                mItem = SocketModelUtil.remoteSockets.get(new Integer(getArguments().getString(ARG_ITEM_ID)).intValue());
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null)
            {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.socket_detail, container, false);

        // Detail des Datensatzes anzeigen
        if (mItem != null)
        {
            ((TextView) rootView.findViewById(R.id.socket_detail)).setText(SocketModelUtil.makeDetails(mItem));
        }

        return rootView;
    }
}
