package naturtalent.it.naturtalentapp;

import naturtalent.it.naturtalentapp.RemoteSocketData;

/**
 * Created by dieter on 30.09.16.
 */
public class SocketActivityContract
{
    public interface Presenter
    {
        void onShowData(RemoteSocketData socketData);
    }

    public interface View
    {
        void showData(RemoteSocketData socketData);
    }
}