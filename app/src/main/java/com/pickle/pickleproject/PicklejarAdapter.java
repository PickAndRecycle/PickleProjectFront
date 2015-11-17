package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;

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
    public View getView(final int position,
                        View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        View row=inflater.inflate(resource, parent, false);
        TextView title= (TextView) row.findViewById(R.id.Title);
        TextView desc=(TextView) row.findViewById(R.id.description);
        TextView time = (TextView) row.findViewById(R.id.Time);
        TextView stat = (TextView) row.findViewById(R.id.Stat);
        TextView dash = (TextView) row.findViewById(R.id.dash);


        if(objects[position].getCategories().equals(TrashCategories.UNUSED)){
            //Log.d("position", Integer.toString(position));
            //Log.d("id", Integer.toString(objects[position].id));
            title.setText((CharSequence) objects[position].title);
            desc.setText((CharSequence) objects[position].getCategories().toString());
            time.setText(Integer.toString(objects[position].timestamp) + " mins ago");
        } else {
            title.setText((CharSequence)objects[position].getDesc());
            desc.setText((CharSequence) objects[position].getCategories().toString());
            time.setText(Integer.toString(objects[position].getsize()) + " bin");
        }
        if (objects[position].getStatus() == 1){
            stat.setText("Picked");
        }
        else if (objects[position].getStatus() == 0){
            stat.setText("Available");
        }
        else{
            stat.setText("Done");
        }
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //row.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Intent intent = new Intent(context, ModifyConfirmation.class);
                intent.putExtra("object", objects[position]);
                context.startActivity(intent);
            }
        });

        return row;
    }



}