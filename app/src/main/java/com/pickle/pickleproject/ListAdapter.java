package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

/**
 * Created by Yanuar Wicaksana on 10/22/15.
 */
public class ListAdapter extends ArrayAdapter<Trash> {

    private Context context;
    private int resource;
    private Trash[] objects;

    public ListAdapter(Context context, int resource,Trash[] objects) {

        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        View row=inflater.inflate(resource, parent, false);
        TextView address= (TextView) row.findViewById(R.id.address);
        TextView number=(TextView) row.findViewById(R.id.number);
        TextView distance = (TextView) row.findViewById(R.id.distance);
        TextView km = (TextView) row.findViewById(R.id.km);
        TextView bin = (TextView) row.findViewById(R.id.bin);
        TextView dash = (TextView) row.findViewById(R.id.dash);

        address.setText((CharSequence)
                objects[position].description);
        number.setText(Integer.toString(
                objects[position].total));
        distance.setText(Integer.toString(
                objects[position].distance));

        return row;
    }


}
