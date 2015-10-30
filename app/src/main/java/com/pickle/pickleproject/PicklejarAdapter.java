package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pickle.pickleprojectmodel.Trash;

/**
 * Created by admin on 10/28/2015.
 */
public class PicklejarAdapter extends ArrayAdapter<Trash> {

    private Context context;
    private int resource;
    private Trash[] objects;

    public PicklejarAdapter(Context context, int resource, Trash[] objects) {

        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        View row=inflater.inflate(resource, parent, false);
        TextView title= (TextView) row.findViewById(R.id.Title);
        TextView desc=(TextView) row.findViewById(R.id.description);
        TextView time = (TextView) row.findViewById(R.id.Time);
        TextView stat = (TextView) row.findViewById(R.id.Stat);
        TextView dash = (TextView) row.findViewById(R.id.dash);


        title.setText((CharSequence) objects[position].title);
        desc.setText((CharSequence) objects[position].description);
        time.setText(Integer.toString( objects[position].timestamp));
        stat.setText(Integer.toString(objects[position].status));

        return row;
    }



}