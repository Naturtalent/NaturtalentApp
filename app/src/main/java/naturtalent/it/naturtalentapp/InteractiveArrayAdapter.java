package naturtalent.it.naturtalentapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dieter on 03.09.16.
 *
 * Mit dem Adapter wird ein Datenmodell einer Liste zuordnen
 */
public class InteractiveArrayAdapter extends ArrayAdapter<RemoteSocketData>
{
    private final List<RemoteSocketData> list;
    private final Activity context;

    private ListView listView;


    public InteractiveArrayAdapter(Activity context, List<RemoteSocketData>list)
    {
        super(context, R.layout.rowbuttonlayout, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder
    {
        protected TextView text;
        protected CheckBox checkBox;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = null;
        if(convertView == null)
        {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.rowbuttonlayout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView)view.findViewById(R.id.label);
            viewHolder.checkBox = (CheckBox)view.findViewById(R.id.check);
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    RemoteSocketData element = (RemoteSocketData) viewHolder.checkBox.getTag();
                    element.setSelected(buttonView.isChecked());
                    MainActivity.updateWidgets(listView);
                }
            });

            view.setTag(viewHolder);
            viewHolder.checkBox.setTag(list.get(position));
        }
        else
        {
            view = convertView;
            ((ViewHolder)view.getTag()).checkBox.setTag(list.get(position));
        }

        ViewHolder holder = (ViewHolder)view.getTag();
        holder.text.setText(list.get(position).getName());

        holder.checkBox.setChecked(list.get(position).isSelected());
        return view;

    }

    public List<RemoteSocketData> getList()
    {
        return list;
    }

    public ListView getListView()
    {
        return listView;
    }

    public void setListView(ListView listView)
    {
        this.listView = listView;
    }
}
