package naturtalent.it.naturtalentapp;

import naturtalent.it.naturtalentapp.RemoteSocketData;
import naturtalent.it.naturtalentapp.SocketActivityContract;

/**
 * Created by dieter on 30.09.16.
 */
public class SocketActivityPresenter
{
    private SocketActivityContract.View view;

    public SocketActivityPresenter(SocketActivityContract.View view)
    {
        this.view = view;
    }

    public void onShowData(RemoteSocketData socketData)
    {
        view.showData(socketData);
    }
}
