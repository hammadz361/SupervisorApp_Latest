package pk.clsurvey.gb.supervision;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {


    private EditText user;
    private EditText pass;
    private Button login;
    private ProgressBar spinner_login;
    private String sup_code;
    static boolean done = true;
    private Button settings;
    public static String [] cluster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        user = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login_btn);
        settings = (Button) findViewById(R.id.settings);


        spinner_login = (ProgressBar)findViewById(R.id.loading);
        check_login();


        settings.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            AlertDialog.Builder alert = new AlertDialog.Builder(login.this);

                                            alert.setTitle("Server Address");
                                            alert.setMessage(getString(R.string.test_url));

// Set an EditText view to get user input
                                            final EditText input = new EditText(login.this);
                                            alert.setView(input);

                                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {

//                                                    R.string.test_url = (Integer) input.getText();
                                                }
                                            });

                                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    // Canceled.
                                                }
                                            });

                                            alert.show();
                                        }
                                    });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                validate(user.getText().toString(), pass.getText().toString());
                if(user.equals("") && pass.equals(""))
                {
                    Toast.makeText(login.this, "Credentials Empty...", Toast.LENGTH_SHORT).show();
                }
                else {
                    validate(user.getText().toString(), pass.getText().toString());
                    spinner_login.setVisibility(View.VISIBLE);


                }    }
        });
                ///////////////////////////////////////

    }

    private void check_login() {
        user_model user_;
        DataBase db = new DataBase(login.this);
        user_ = db.check();
        db.initiate();
        Log.i("uuu", user_.user.toString());
        Log.i("uuucode", user_.code.toString());

        if(user_.user.length()>1) {
            Toast.makeText(login.this, "Welcome...." + user_.user, Toast.LENGTH_SHORT).show();
            Intent main = new Intent(this, MainActivity.class);

            main.putExtra("Code", user_.code);
            main.putExtra("User", user_.user);
            this.finish();
            startActivity(main);
        }
    }

    private void validate( final String user, final String pass){



//            Toast.makeText(login.this, "Please Wait..... Logging In", Toast.LENGTH_SHORT).show();
            //TODO ::volley login to implement for the server communication

            RequestQueue queue = Volley.newRequestQueue(this);
            String url =getString(R.string.test_url)+"login.php";

            // Request a string response from the provided URL.
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response_login", response);
                            try {
                                JSONObject obj  = new JSONObject(response);

                                if(obj.getJSONObject("result").getString("status").equals("200")){
                                    loading_start(obj.getJSONObject("result").getString("user"), obj.getJSONObject("result").getString("code"));
                                }
                                else if(obj.getJSONObject("result").getString("status").equals("403")){
                                    Toast.makeText(login.this, "Password/userCode Incorect", Toast.LENGTH_SHORT).show();
                                    spinner_login.setVisibility(View.GONE);
                                }
                                else
                                {
                                    Toast.makeText(login.this, "Check you internet Connection", Toast.LENGTH_SHORT).show();
                                    spinner_login.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                            if(error.toString().contains("TimeoutError"))
                            {
                                Toast.makeText(login.this, "Remote Server is Down", Toast.LENGTH_SHORT).show();
                                spinner_login.setVisibility(View.GONE);
                            }
                            else
                            {
                            Toast.makeText(login.this, "Check you internet Connection", Toast.LENGTH_SHORT).show();
                            spinner_login.setVisibility(View.GONE);
                        }}
                    }
            ) {
@Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("username", user);
                    params.put("password", pass);

                    return params;
                }
            };
            queue.add(postRequest);
            //after successful login model will be populated with the values
//            RequestQueue queue = Volley.newRequestQueue(login.this);

            //Fetch values of the cluster and enumerators data and HH_ids
            //load_cluster()  load_enum()     Load_HH()



    }
    public void loading_start( final String user, final String code)
    {


//        Toast.makeText(login.this, "Loading data........", Toast.LENGTH_LONG).show();
        final DataBase db = new DataBase(login.this);

//        Toast.makeText(login.this, user+"-----------"+code, Toast.LENGTH_SHORT).show();
        user_model us = new user_model(user, code);

        db.login_success(us);
        fetch_HH(code, user);
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        spinner_login.setVisibility(View.VISIBLE);
//                        Log.i("Response", "fetching done");
//
//                    }
//                });
//            }
//        };
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //fetch_HH(code);

      //  cluster = db.fetch_cluster(code);
//        fetch_HH(code);
//        fetch_enum(code);
//        fetch_cluster(code);
//        sync_date_pushed();
//        ///////////////////////////////////


//        }






    }

    void fetch_HH(final String code, final String user){
        Log.i("Response", "entered");
        final DataBase db = new DataBase(login.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =getString(R.string.test_url)+"GetAll.php";

        // Request a string response from the provided URL.
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i("Response_getHH", response);
                        try {
                            JSONObject obj  = new JSONObject(response);

                            JSONArray arr_cc = obj.getJSONArray("CC_list");
                            if(arr_cc.length()>0) {
                                for (int i = 0; i < arr_cc.length(); i++) {
                                    cluster_model cs2 = new cluster_model("", arr_cc.getJSONObject(i).getString("cluster_code"), arr_cc.getJSONObject(i).getString("cluster_name"), code);
                                    db.load_cluster(cs2);
                                }


                            }

                            JSONArray arr = obj.getJSONArray("HH_list");
                            if(arr.length()>0)
                            {
                                for(int i=0; i<arr.length();i++)
                                {
                                    db.load_HH(arr.getJSONObject(i).getString("hh_id"), arr.getJSONObject(i).getString("cluster"), arr.getJSONObject(i).getString("hh_status"),arr.getJSONObject(i).getString("hh_sampled"), arr.getJSONObject(i).getString("hh_result"), arr.getJSONObject(i).getString("Enumr"), arr.getJSONObject(i).getString("loc"), arr.getJSONObject(i).getString("timestamp"), arr.getJSONObject(i).getString("revisit"), arr.getJSONObject(i).getString("hh_sr"));
                                    Log.i("hhhhhh", arr.getJSONObject(i).getString("hh_id"));

                                }

                            }
                            JSONArray arr_en = obj.getJSONArray("enum_list");
                            for(int i=0; i<arr_en.length();i++)
                            {
                                user_model enum1 = new user_model(arr_en.getJSONObject(i).getString("enum"), arr_en.getJSONObject(i).getString("code"));
                                db.load_enum(enum1);

                            }
                            sync_date_pushed();
                            done = false;
                            Log.i("donee", "dddddddddddddddddddddd");
                            Intent main = new Intent(login.this, MainActivity.class);
                            main.putExtra("Code", code);
                            main.putExtra("User", user);
                            login.this.finish();
                            db.close();
                            startActivity(main);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(login.this, "Time out Error.", Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("supervisor", code);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(postRequest);
        queue.add(postRequest);
        db.check();

    }


    public void sync_date_pushed(){
        //TODO after final Tranaction log_date in table_log_date will be populated with now() timestamp
        //String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        //new SimpleDateFormat("YYYY-MM-dd HH:MM:SS").format(new java.util.Date());
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new java.util.Date());//new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
        DataBase db = new DataBase(login.this);
        db.pushed_data(timeStamp);
        Log.i("pussshed", timeStamp);
        db.close();


    }

}
