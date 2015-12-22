package com.pickle.pickleproject;

/**
 * Created by admin on 12/3/2015.
 */
import android.content.Intent;
import android.content.SharedPreferences;
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
public class TabThrower extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    private RequestQueue mQueue;
    public static final String PREFS_NAME = "PicklePrefs";
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
        Log.d("error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        String getUsername = settings.getString("username", "0");
        try{
            JSONObject parentObject = response;
            Log.d("json:", response.getString("result"));
            JSONArray parentArray = parentObject.getJSONArray("result");

            List<Trash> Trashlist = new ArrayList<Trash>();
            String username = getUsername;
            for (int i=0; i<parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                Log.d("categories", finalObject.getString("categories"));
                Trash trashObj;
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(UnusedCondition.class, new UnusedConditionDeserialize());
                Gson gson = gsonBuilder.create();
                boolean bool = Boolean.parseBoolean(finalObject.getString("report"));
                if (bool == false) {
                    if (finalObject.getString("username").equals(username)) {
                        trashObj = gson.fromJson(String.valueOf(finalObject), Trash.class);
                        if (finalObject.getString("categories").equals("Unused Goods")) {
                            trashObj.setCategories(TrashCategories.UNUSED);
                        } else if (finalObject.getString("categories").equals("General Waste")) {
                            trashObj.setCategories(TrashCategories.GENERAL);
                        } else if (finalObject.getString("categories").equals("Recycleable Waste")) {
                            trashObj.setCategories(TrashCategories.RECYCLED);
                        } else if (finalObject.getString("categories").equals("Green Waste")) {
                            trashObj.setCategories(TrashCategories.GREEN);
                        }
                        if (Integer.parseInt(finalObject.getString("status")) == 0) {
                            Trashlist.add(0, trashObj);
                        } else if (Integer.parseInt(finalObject.getString("status")) == 1) {
                            if (Trashlist.size() == 0 ) {
                                Trashlist.add(trashObj);
                            }
                            else if( Trashlist.get(Trashlist.size()-1).getStatus() == 0 || Trashlist.get(Trashlist.size()-1).getStatus() == 1){
                                Trashlist.add(trashObj);
                            }
                            else {
                                for (int j = 0; j < Trashlist.size(); j++) {
                                    int status = Trashlist.get(j).getStatus();
                                    if (status == 2) {
                                        Trashlist.add(j, trashObj);
                                        break;
                                    }
                                }
                            }

                        } else {
                            Trashlist.add(trashObj);
                        }
                    }
                }
            }
            Log.d("list",Trashlist.toString());
            final Trash[] trashArray = Trashlist.toArray(new Trash[0]);
            final PicklejarAdapter myAdapter=new PicklejarAdapter(getActivity().getApplicationContext(), R.layout.rowpicklejar, trashArray);
            final ListView myList = (ListView) getView().findViewById(R.id.unusedList);
            myList.setAdapter(myAdapter);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(trashArray[position].getStatus()!=2){
                        Intent intent = new Intent(getActivity().getApplicationContext(), ModifyConfirmation.class);
                        Trash trash =  myAdapter.getItem(position);
                        intent.putExtra("object", trash);
                        startActivity(intent);

                    }
                    else{

                    }
                    //intent putExtra("object", listView.getChildAt(position));
                }
            });

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    private class UnusedConditionDeserialize implements JsonDeserializer<UnusedCondition> {
        @Override
        public UnusedCondition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.getAsString().equals("Good")) {
                return UnusedCondition.GOOD;
            } else if (json.getAsString().equals("Bad")) {
                return UnusedCondition.BAD;
            } else if (json.getAsString().equals("New")) {
                return UnusedCondition.NEW;
            } else {
                return UnusedCondition.UNSPECIFIED;
            }
        }
    }
}
