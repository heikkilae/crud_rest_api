package com.example.root.apitestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {

    RequestQueue queue;
    private String url = "http://api.url/api";
    private User user;
    private List<ReservationType> reservationTypes;
    private String selectedReservationType;

    private Button logoutBtn;
    private TextView loginText;

    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        init_cache();

        loginText = (TextView) findViewById(R.id.login_txt);

        getReservationTypes();

        // Get user of given id we got on login
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            String id = bundle.getString("userId");
            user = new User(id);
            getUserById(id);
        }
    }

    public void init_cache()
    {
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        queue = new RequestQueue(cache, network);

        // Start the queue
        queue.start();
    }

    public void loadReservationTypes()
    {
        Spinner dropdown = findViewById(R.id.spinner1);

        if(reservationTypes != null) {
            ArrayAdapter<ReservationType> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, reservationTypes);

            dropdown.setAdapter(adapter);

            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ReservationType selectedType = (ReservationType)adapterView.getSelectedItem();
                    selectedReservationType = selectedType.reservation_type;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    void updateView()
    {
        loginText.setText("Terve "+user.forename+" "+user.surname+"!");
        loadReservationTypes();
        ListView listView = (ListView)findViewById(R.id.reservation_txt);

        if(user.reservations != null)
        {
            ArrayAdapter<Reservation> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, user.reservations);

            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Reservation reservation = (Reservation) adapterView.getItemAtPosition(i);
                    String reservationId = reservation.getId();
                    removeReservation(reservationId);
                }
            });
        }
    }

    public void updateUser()
    {
        getUserById(user._id);
    }

    public void logoutButtonClicked(View view)
    {
        logout();
    }

    public void reserveButtonClicked(View view) { reserve_new_reservation(); }

    private void removeReservation(String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url+"/reservations/"+id.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        updateView();
                        Log.d("response", response.toString());
                        updateUser();
                        updateView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("debug", error.toString());
            }
        });

        queue.add(stringRequest);
    }

    private void getUserById(String userId)
    {
        JSONObject _user = user.toJsonObject();;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url+"/users/"+userId.toString(), _user, new Response.Listener<JSONObject>() {

                    // Success
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            // If error does not occur & user founds
                            if (response.getString("error") == "false" &&
                                    response.getString("data") != null )
                            {
                                Gson gson = new Gson();
                                String userData = response.getString("data");
                                user = gson.fromJson(userData, User.class);
                                updateView();
                            }
                        }
                        catch (JSONException e)
                        {
                            //TODO: Handle exeption
                        }
                        finally
                        {
                            // Whole response
                            Log.d("response", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    // Error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("debug:", error.toString());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void getReservationTypes()
    {
        JSONObject _types = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url+"/reservations_types/", _types, new Response.Listener<JSONObject>() {

                    // Success
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            // If error does not occur & types found
                            if (response.getString("error") == "false" &&
                                    response.getString("data") != null )
                            {
                                Gson gson = new Gson();
                                String reservationTypesData = response.getString("data");

                                Type reservationTypeListType = new TypeToken<ArrayList<ReservationType>>(){}.getType();
                                reservationTypes = gson.fromJson(reservationTypesData, reservationTypeListType);

                                updateView();
                            }
                        }
                        catch (JSONException e)
                        {
                            //TODO: Handle exeption
                        }
                        finally
                        {
                            // Whole response
                            Log.d("response", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    // Error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("debug:", error.toString());
                    }
                });

        queue.add(jsonObjectRequest);
    }

    public void reserve_new_reservation()
    {
        // TODO: collect proper reservaion parameters
        Reservation reservation = new Reservation(new Date(), selectedReservationType, user._id);

        JSONObject _reservation = reservation.toJsonObject();;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url+"/reservations", _reservation, new Response.Listener<JSONObject>() {

                    // Success
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            // If error does not occur & user founds
                            if (response.getString("error") == "false" &&
                                    response.getString("data") != null )
                            {
                                // Success
                                // Do somethings

                            }
                        }
                        catch (JSONException e)
                        {
                            //TODO: Handle exeption
                        }
                        finally
                        {
                            // Whole response
                            Log.d("response", response.toString());
                            updateUser();
                            updateView();
                        }
                    }
                }, new Response.ErrorListener() {
                    // Error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("debug:", error.toString());
                    }
                });

        queue.add(jsonObjectRequest);
    }
    public void logout()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/logout/"+user._id.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // move to main activity
                        Intent i = new Intent(ReservationActivity.this, MainActivity.class);
                        i.putExtra("state", MainActivity.State.NOT_LOGGED_IN);
                        startActivity(i);

                        Log.d("response", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("debug", error.toString());
            }
        });

        queue.add(stringRequest);
    }

}
