package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;

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
    public View getView(final int position,
                        View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        final View row=inflater.inflate(resource, parent, false);
        final TextView address= (TextView) row.findViewById(R.id.address);
        TextView number=(TextView) row.findViewById(R.id.number);
        TextView distance = (TextView) row.findViewById(R.id.distance);
        TextView km = (TextView) row.findViewById(R.id.km);
        TextView bin = (TextView) row.findViewById(R.id.bin);
        TextView dash = (TextView) row.findViewById(R.id.dash);


        distance.setText(Integer.toString(objects[position].distance));
        if(objects[position].getCategories().equals(TrashCategories.UNUSED)){
            //Log.d("position", Integer.toString(position));
            //Log.d("id", Integer.toString(objects[position].id));
            number.setText((CharSequence) objects[position].getCondition().toString());
            address.setText((CharSequence) objects[position].title);
            bin.setText("");
        } else {
            number.setText(Integer.toString(objects[position].size));
            address.setText((CharSequence) objects[position].description);
        }
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //row.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Intent intent = new Intent(context, IndividualTrashInfo.class);
                intent.putExtra("object", objects[position]);
                context.startActivity(intent);
            }
        });
        return row;
    }





}
