package com.example.root.apitestapp;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ReservationType {
    String _id;
    String reservation_type;

    public ReservationType() {}

    @Override
    public String toString() {
        return this.reservation_type;
    }

    public String getId() { return _id; }

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
