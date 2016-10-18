package naturtalent.it.naturtalentapp;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import naturtalent.it.naturtalentapp.R;
import naturtalent.it.naturtalentapp.RemoteSocketData;
import naturtalent.it.naturtalentapp.SocketActivityContract;
import naturtalent.it.naturtalentapp.databinding.ActivitySocketBinding;
import naturtalent.it.naturtalentapp.databinding.DialogSocketBinding;

/**
 * Created by dieter on 01.10.16.
 * <p/>
 * Die Verwaltung der Funksteckdosen erfolgt in einem separaten Fenster
 */
public class SocketSettingActivity extends Activity implements SocketActivityContract.View
{

    /**
     * Implementiert SocketActivityContract
     *
     * @param socketData
     */
    @Override
    public void showData(RemoteSocketData socketData)
    {
        String name = socketData.getName();
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActivitySocketBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_socket);
        SocketActivityPresenter socketActivityPresenter = new SocketActivityPresenter(this);

        RemoteSocketData remoteSocketData = new RemoteSocketData("Pumpenschalter", (short) 1, (short) 1);
        binding.setSocket(remoteSocketData);
        binding.setPresenter(socketActivityPresenter);
    }

}
