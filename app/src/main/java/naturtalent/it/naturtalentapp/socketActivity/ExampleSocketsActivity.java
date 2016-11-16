package naturtalent.it.naturtalentapp.socketActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import naturtalent.it.naturtalentapp.MainActivity;
import naturtalent.it.naturtalentapp.R;
import naturtalent.it.naturtalentapp.model.SocketModelUtil;

/**
 * Mit dieser Aktion werden die Beispieldaten uebernommen.
 * Evtl. existierende Daten werden geloescht.
 */
public class ExampleSocketsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_sockets);
   }

    public void onClickExampleSockets(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonExampleSockets:
                new SocketModelUtil().saveSockets(view.getContext(),null);
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }
}
