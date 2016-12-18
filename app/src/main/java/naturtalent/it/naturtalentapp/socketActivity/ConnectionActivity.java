package naturtalent.it.naturtalentapp.socketActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import naturtalent.it.naturtalentapp.MainActivity;
import naturtalent.it.naturtalentapp.R;
import naturtalent.it.naturtalentapp.model.SocketModelUtil;

/**
 * Created by dieter on 07.12.16.
 */

public class ConnectionActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
    }

    public void onClickConnection(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonConnection:
                MainActivity.ma.ipcon = null;
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }
}
