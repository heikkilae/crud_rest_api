package com.example.root.apitestapp;

import android.util.Log;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
    String _id;
    String email;
    String password;
    String forename;
    String surname;
    String role;
    boolean loggedIn;
    String identityNumber;
    String address;
    int zipCode;
    String phone;
    Reservation[] reservations;

    public User(String id)
    {
        this._id = id;
    }

    public User(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public JSONObject toJsonObject()
    {
        Gson gson = new Gson();
        String userStr = gson.toJson(this);
        JSONObject jsonObject =  new JSONObject();

        try {
            jsonObject = new JSONObject(userStr);

        } catch (JSONException e) {
            Log.d("debug", e.toString());
        } finally {
            return jsonObject;
        }
    }
}
