package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;
import com.pickle.pickleproject.CircleImageView;

/**
 * Created by Yanuar Wicaksana on 10/22/15.
 */
public class ListAdapter extends ArrayAdapter<Trash> {

    private Context context;
    private int resource;
    private Trash[] objects;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public ListAdapter(Context context, int resource,Trash[] objects) {

        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;

        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    static class ViewHolder{

    }

    @Override
    public View getView(final int position,
                        View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View row=inflater.inflate(resource, parent, false);
        final TextView address= (TextView) row.findViewById(R.id.address);
        TextView number=(TextView) row.findViewById(R.id.number);
        TextView distance = (TextView) row.findViewById(R.id.distance);
        TextView km = (TextView) row.findViewById(R.id.km);
        TextView bin = (TextView) row.findViewById(R.id.bin);
        TextView dash = (TextView) row.findViewById(R.id.dash);
        CircleImageView thumbnail = (CircleImageView) row.findViewById(R.id.thumbnail);
        thumbnail.setImageUrl("http://i63.tinypic.com/312zpeu.jpg", mImageLoader);

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
       /* row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //row.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Intent intent = new Intent(context, IndividualTrashInfo.class);
                intent.putExtra("object", objects[position]);
                context.startActivity(intent);
            }
        });*/
        return row;
    }





}
