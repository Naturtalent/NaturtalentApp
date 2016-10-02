package naturtalent.it.naturtalentapp;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by dieter on 02.09.16.
 */
public class RemoteSocketData extends BaseObservable
{
    static public final Character SOCKET_TYPE_A = 'A';
    static public final Character SOCKET_TYPE_B = 'B';

    private String name;

    private boolean selected;

    private char type;

    private short houseCode;

    private short remoteCode;

    public RemoteSocketData(String name, short houseCode, short remoteCode)
    {
        this.name = name;
        this.houseCode = houseCode;
        this.remoteCode = remoteCode;
        this.selected = false;
        this.setType(SOCKET_TYPE_B);
    }

    @Bindable
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        notifyPropertyChanged(naturtalent.it.naturtalentapp.BR.name);
    }

    @Bindable
    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    @Bindable
    public char getType()
    {
        return type;
    }

    public void setType(char type)
    {
        this.type = type;
    }

    @Bindable
    public short getHouseCode()
    {
        return houseCode;
    }

    public void setHouseCode(short houseCode)
    {
        this.houseCode = houseCode;
    }

    @Bindable
    public short getRemoteCode()
    {
        return remoteCode;
    }

    public void setRemoteCode(short remoteCode)
    {
        this.remoteCode = remoteCode;
    }






}
