package com.example.root.apitestapp;

import android.util.Log;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class Reservation {
    String _id;
    Date date;
    String type;
    int duration;
    String state;
    String userId;
	int turnNumber;

    public Reservation(Date date, String type, String userId)
    {
        this.date = date;
        this.type = type;
        this.userId = userId;
    }

    public String getId() { return _id; }

    @Override
    public String toString() {
        return this.date +":\t"+ this.type;
    }

    public JSONObject toJsonObject()
    {
        Gson gson = new Gson();
        String reservationStr = gson.toJson(this);
        JSONObject jsonObject =  new JSONObject();

        try {
            jsonObject = new JSONObject(reservationStr);

        } catch (JSONException e) {
            Log.d("debug", e.toString());
        } finally {
            return jsonObject;
        }
    }
}
