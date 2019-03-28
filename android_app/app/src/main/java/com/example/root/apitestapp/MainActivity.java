package com.example.root.apitestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    RequestQueue queue;
    public enum State {LOGGED_IN, NOT_LOGGED_IN, UNKNOWN};
    public State state;
    private String url = "http://api.url/api";

    private User user;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        State _state = State.UNKNOWN;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            _state = bundle.getParcelable("state");

        // Get current state
        this.state = _state;

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login_btn);

        init_cache();
        updateView();
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

    public void loginButtonClicked(View view)
    {
        user = new User(editTextEmail.getText().toString(),
                editTextPassword.getText().toString());
        login();
    }

    private void updateView()
    {
        if(state == State.LOGGED_IN)
        {
            // move to reservation activity
            Intent i = new Intent(MainActivity.this, ReservationActivity.class);
            i.putExtra("userId", user._id.toString());
            startActivity(i);
        }
    }
    public void login()
    {
        JSONObject _user = user.toJsonObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, url+"/login", _user, new Response.Listener<JSONObject>() {

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

                            // Change State & Update View
                            state = State.LOGGED_IN;
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
}
