package naturtalent.it.naturtalentapp.dialogs;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import naturtalent.it.naturtalentapp.R;
import naturtalent.it.naturtalentapp.RemoteSocketData;
import naturtalent.it.naturtalentapp.SocketActivityContract;
import naturtalent.it.naturtalentapp.SocketActivityPresenter;
import naturtalent.it.naturtalentapp.databinding.DialogSocketBinding;

/**
 * Created by dieter on 12.10.16.
 */
public class SocketDataDialog extends DialogFragment implements TextView.OnEditorActionListener
{
    // Dialogtitel
    private String title = "Funksteckdose definieren";


    private EditText mEditText;


    private RemoteSocketData socketData;


    public interface SocketNameListener
    {
        void onFinishUserDialog(String name);
    }

    // Empty constructor required for DialogFragment
    public SocketDataDialog()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Databinding UI 'dialog_socket' und Data 'socketData' zusammenbringen
        View view = inflater.inflate(R.layout.dialog_socket, container);
        DialogSocketBinding binding = DialogSocketBinding.bind(view);
        binding.setSocket(socketData);

        mEditText = (EditText) view.findViewById(R.id.edit_socket_name);

        // set this instance as callback for editor action
        mEditText.setOnEditorActionListener(this);

        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(title);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        // Return input text to activity
        SocketNameListener activity = (SocketNameListener) getActivity();

        // ruft die in Activity implementierte Funktion auf und uebergibt die Daten
        activity.onFinishUserDialog(mEditText.getText().toString());

        this.dismiss();


        return true;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setSocketData(RemoteSocketData socketData)
    {
        this.socketData = socketData;
    }
}
