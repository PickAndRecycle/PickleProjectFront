package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pickle.pickleprojectmodel.Trash;
import com.pickle.pickleprojectmodel.TrashCategories;
import com.pickle.pickleprojectmodel.UnusedCondition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TabRecycle extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mQueue = CustomVolleyRequestQueue.getInstance(getActivity().getApplicationContext())
                .getRequestQueue();
        String url = "http://104.155.237.238:8080/trash/";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonRequest);
        return inflater.inflate(R.layout.fragment_tab_unused, container, false);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Error:", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            JSONObject parentObject = response;
            Log.d("json:", response.getString("result"));
            JSONArray parentArray = parentObject.getJSONArray("result");

            List<Trash> Trashlist = new ArrayList<Trash>();

            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);

                Trash trashObj;
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(TrashCategories.class, new TrashCategoriesDeserialize());
                Gson gson = gsonBuilder.create();
                boolean bool = Boolean.parseBoolean(finalObject.getString("report"));
                if (bool == false) {
                    if (Integer.parseInt(finalObject.getString("status")) == 0) {
                        if (finalObject.getString("categories").equals("Recycleable Waste")) {
                            trashObj = gson.fromJson(String.valueOf(finalObject), Trash.class);

                            Trashlist.add(trashObj);
                        }
                    }
                }
            }

            Trash[] trashArray = Trashlist.toArray(new Trash[0]);
            final ListAdapter myAdapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.rowlayout, trashArray);
            ListView myList = (ListView) getView().findViewById(R.id.unusedList);
            myList.setAdapter(myAdapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), IndividualTrashInfo.class);
                    Trash trash = (Trash) myAdapter.getItem(position);
                    intent.putExtra("object", trash);
                    startActivity(intent);
                    //intent putExtra("object", listView.getChildAt(position));
                }
            });

            //return Trashlist;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class TrashCategoriesDeserialize implements JsonDeserializer<TrashCategories> {
        @Override
        public TrashCategories deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json.getAsString().equals("Unused Goods")){
                return TrashCategories.UNUSED;
            } else if (json.getAsString().equals("General Waste")){
                return TrashCategories.GENERAL;
            } else if (json.getAsString().equals("Recycleable Waste")){
                return TrashCategories.RECYCLED;
            } else if (json.getAsString().equals("Green Waste")){
                return TrashCategories.GREEN;
            } else {
                return TrashCategories.UNSPECIFIED;
            }
        }
    }
}




