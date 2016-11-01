package naturtalent.it.naturtalentapp;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingMethods;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dieter on 02.09.16.
 */
public class RemoteSocketData extends BaseObservable
{
    static public final String SOCKET_TYPE_A = "A";
    static public final String SOCKET_TYPE_B = "B";
    static public final String SOCKET_TYPE_C = "C";

    private String name;

    private boolean selected;

    private String type;

    private String houseCode;

    private String remoteCode;

    public RemoteSocketData(String name, String type, String houseCode, String remoteCode)
    {
        this.name = name;
        this.type = type;
        this.houseCode = houseCode;
        this.remoteCode = remoteCode;
        this.selected = false;
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
        notifyPropertyChanged(naturtalent.it.naturtalentapp.BR.selected);
    }

    @Bindable
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
        notifyPropertyChanged(naturtalent.it.naturtalentapp.BR.type);
    }

    @Bindable
    public String getHouseCode()
    {
        return houseCode;
    }

    public void setHouseCode(String houseCode)
    {
        this.houseCode = houseCode;
        notifyPropertyChanged(naturtalent.it.naturtalentapp.BR.houseCode);
    }

    public void validate()
    {
        name = validateString (name, "neu");
        houseCode = validateString (houseCode, "0");
        remoteCode = validateString (remoteCode, "0");

        type = validateString (type, "A");
    }

    private String validateString (String value, String defaultValue)
    {
        if(value != null)
            value = value.trim();

        value = ((value == null) || (value.isEmpty())) ? defaultValue : value;
        return value;
    }

    @Bindable
    public String getRemoteCode()
    {
        return remoteCode;
    }

    public void setRemoteCode(String remoteCode)
    {
        this.remoteCode = remoteCode;
        notifyPropertyChanged(naturtalent.it.naturtalentapp.BR.remoteCode);
    }

    /**
     * Two-way Anpassung an die 'char' Properties
     */
    public static class CharacterBindingAdapter
    {
        @BindingAdapter("android:text")
        public static void setText(EditText view, char charType)
        {
            view.setText(Character.toString(charType));
        }

        @InverseBindingAdapter(attribute = "android:text")
        public static char getText(EditText view)
        {
            return Character.valueOf(view.getText().charAt(0));
        }

    }

    /**
     * Two-way Anpassung an die 'short' Properties
     */
    public static class IntegerBindingAdapter
    {
        @BindingAdapter("android:text")
        public static void setText(EditText view, short value)
        {
            view.setText(String.valueOf(value));
        }

        @InverseBindingAdapter(attribute = "android:text")
        public static short getText(EditText view)
        {
            return Short.valueOf(view.getText().toString());
        }
    }




}
