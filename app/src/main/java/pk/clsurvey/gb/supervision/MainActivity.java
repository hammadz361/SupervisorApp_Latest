package pk.clsurvey.gb.supervision;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    //////////////////////////
    private static final int REQUEST_LOCATION = 200 ;
    public static String cluster_selected = "";
    private TextView head;
    private static boolean sync_on_up = false;
    private ImageView stat;
    public static String date_re = "";
    public static String time_re = "";
    public static boolean go_on = false;
    public static String[] cluster_array ;
    public static String code_sup = "";
    public static EditText Cluster_input ;
    private Button btnspr,btnemr;

//    private ProgressBar spinner;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    LocationManager locationManager;
    private int activity_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBase db = new DataBase(MainActivity.this);

        final String code = getIntent().getExtras().getString("Code", null);
        final String user = getIntent().getExtras().getString("User", null);
        code_sup = code;
        cluster_array = db.fetch_cluster(code);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.bar);
//        spinner = (ProgressBar) findViewById(R.id.progressBar);

        btnspr = (Button) findViewById(R.id.btnspr);
        btnemr = (Button) findViewById(R.id.btnemr);

        btnspr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String code = getIntent().getExtras().getString("Code", null);
                openActivity("s",code);
            }
        });
        btnemr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cluster_input = (EditText)findViewById(R.id.cluster);
                String Cluster_input22 = String.valueOf(Cluster_input.getText().toString());
                if(Cluster_input22.length()==0)
                {
                    Toast.makeText(MainActivity.this, "Please enter value in cluster", Toast.LENGTH_SHORT).show();

                }
                else{
                    openActivity("e",Cluster_input22);
                }

            }
        });

        setSupportActionBar(myToolbar);
//        spinner.setVisibility(View.VISIBLE);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        stat = (ImageView)findViewById(R.id.to_be_status);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //TODO change toast message
        head = (TextView) findViewById(R.id.header);
        head.setText("Supervisor  :  " + user);
        if(sync_on_up)
        {
            stat.setImageResource(R.drawable.status);
        }

        if(cluster_array.length<1)
        {
            cluster_array = db.fetch_cluster(code);

        }

         //TODO change hard coded value with supervisor code


        if(cluster_array.length==0)
        {
            user_model us = new
                    user_model();
            us =db.check();
            if(us.user.isEmpty())
            {
                db.close();
                this.finish();
            }
            else if(cluster_array.length>0){
                ProgressBar pb = (ProgressBar) findViewById(R.id.ploading);
                pb.setVisibility(ProgressBar.GONE);
            //    setview(cluster_selected);
            }
            else
            {
                ProgressBar pb = (ProgressBar) findViewById(R.id.ploading);
                pb.setVisibility(ProgressBar.VISIBLE);
            }
        }
        cluster_array = db.fetch_cluster(code);
        db.close();




        ImageButton img_button = (ImageButton) this.findViewById(R.id.imageButton);
        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBase db = new DataBase(MainActivity.this);

                Cluster_input = (EditText)findViewById(R.id.cluster);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Cluster_input.getWindowToken(), 0);
                cluster_selected = String.valueOf(Cluster_input.getText().toString());
                //   cluster_array = db.fetch_cluster(code_sup);

                if (cluster_array.length==0) {
                    buildAlertMessageNoCluster();
                } else {
                    /////////////////////////////////////////////////////

                    if(cluster_selected.length()==0)
                    {
                        Toast.makeText(MainActivity.this, "Please enter value in cluster", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        if(Arrays.asList(cluster_array).contains(cluster_selected))
                        {
                            //   cluster_selected = cluster_selected;
                          //  Toast.makeText(MainActivity.this, "setting view", Toast.LENGTH_SHORT).show();
                            setview(cluster_selected);
                            //     setview(cluster_selected);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Cluster is Invalid- Re Enter value", Toast.LENGTH_SHORT).show();

                        }
                    }
                    /////////////////////////////////////////////////////
                }
            }
        });
      //  final Spinner myListView = (Spinner) findViewById(R.id.spinner);
  //      ArrayAdapter<String> Namearray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cluster_array);
  //      Namearray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      //  myListView.setAdapter(Namearray);

        //cluster selected
  //      myListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
 //           @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


 //               cluster_selected = parent.getSelectedItem().toString();
//                setview(cluster_selected);
//                setview(cluster_selected);
//                Toast.makeText(MainActivity.this, cluster_selected, Toast.LENGTH_SHORT).show();

            //}

 //           @Override
//            public void onNothingSelected(AdapterView<?> parent) {

     //       }
    //    });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void openActivity(String type,String code){
        Intent intent = new Intent(this, ProgressReport.class);
        Bundle b = new Bundle();
        b.putString("type", type); //Your id
        b.putString("code", code); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                Toast.makeText(MainActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                DataBase db = new DataBase(MainActivity.this);
                db.delete_all();
                db.close();
                sync_on_up = false;
                this.finish();
                break;

            case R.id.Sync:

                        final String code = getIntent().getExtras().getString("Code", null);
                        if (sync_on_up==true)
                        {
                            DataBase dbb = new DataBase(MainActivity.this);
                            Toast.makeText(MainActivity.this, "Syncing, new updates.......", Toast.LENGTH_SHORT).show();
                            ArrayList<status_HH_model> update_list = new ArrayList<status_HH_model>();
                            update_list=dbb.syncing_data("");
                            for(int i=0; i < update_list.size(); i++) {
                                send_date(update_list.get(i));

                                stat.setVisibility(View.INVISIBLE);
                            }
                            dbb.close();
                            sync_on_up = false;
                            dbb.set_flag();
                        }
                        else {
                            sync(code);
                            stat.setVisibility(View.INVISIBLE);

                        }
//                    }
//                else
//                    Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_setting:
//                Toast.makeText(MainActivity.this, "To be implemented.........", Toast.LENGTH_SHORT).show();
                Intent log = new Intent(this, Log_entries.class);
                startActivity(log);
                break;

            default:
                Toast.makeText(MainActivity.this, "Noopeeee", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void clustersearch(final View v) {

        DataBase db = new DataBase(MainActivity.this);

        Cluster_input = (EditText)findViewById(R.id.cluster);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Cluster_input.getWindowToken(), 0);
        cluster_selected = String.valueOf(Cluster_input.getText().toString());
     //   cluster_array = db.fetch_cluster(code_sup);

        if (cluster_array.length==0) {
            buildAlertMessageNoCluster();
        } else {
            /////////////////////////////////////////////////////

            if(cluster_selected.length()==0)
            {
                Toast.makeText(MainActivity.this, "Please enter value in cluster", Toast.LENGTH_SHORT).show();

            }
            else{
                if(Arrays.asList(cluster_array).contains(cluster_selected))
                {
                 //   cluster_selected = cluster_selected;
                    Toast.makeText(MainActivity.this, "setting view", Toast.LENGTH_SHORT).show();
                    setview(cluster_selected);
               //     setview(cluster_selected);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Cluster is Invalid- Re Enter value", Toast.LENGTH_SHORT).show();

                }
            }
            /////////////////////////////////////////////////////
        }
    }
    public void replace_handler(final View v){
        LinearLayout vwParentRow = (LinearLayout) v.getParent();
        TextView child = (TextView) vwParentRow.getChildAt(0);
        //date_select(
        final String hh_id = (String) child.getText();
        DataBase db = new DataBase(MainActivity.this);
        if(db.check_replace(hh_id, cluster_selected).equals("True")) {

//        Toast.makeText(MainActivity.this, "I am here"+hh_id+" and CLuster is "+ cluster_selected, Toast.LENGTH_LONG);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String title = "Replace HH_ID : " + hh_id + " from Sample in Cluster :" + cluster_selected;
//        Typeface bold = child.getTypeface();
//        if(bold!=Null) title.replace("Replace", "Include");
            builder.setTitle(title);

            builder.setPositiveButton("Replace", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DataBase db = new DataBase(MainActivity.this);
                    String count =  db.HH_ID_In_Cluster(cluster_selected);
                    if(Integer.parseInt(count)>0) {
                        try{
                            //TODO : Insert Enumerator ID
                            replace_enum_pop(v, hh_id, cluster_selected);
//                            db.replace_HH_ID(hh_id, cluster_selected);
                        }
                       catch (Exception e){
                            Toast.makeText(MainActivity.this, e.toString(),Toast.LENGTH_LONG).show();
                       }
                       // setview(cluster_selected);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "No Households are left for replacement", Toast.LENGTH_SHORT).show();

                    }

                }
            });


            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else if(db.check_replace(hh_id, cluster_selected).equals("visited"))
        {
            Toast.makeText(MainActivity.this, "Assign not visited status to replace the HH_ID from Cluster... ", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "HH_ID is not part of sample", Toast.LENGTH_LONG).show();

        }
    }
    public void myClickHandler(final View v) {
        LocationManager loc_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!loc_manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            /////////////////////////////////////////////////////
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Change Status...");

            builder.setPositiveButton("Visited", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    select_enum_pop(v);
                }
            });

            builder.setNeutralButton ("Not Visited", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataBase db = new DataBase(MainActivity.this);
                    // String[] ccode = cluster_selected.split("-");
                    //Log.i("qqqqq1", ccode[1]);
                    LinearLayout vwParentRow = (LinearLayout) v.getParent();
                    TextView child = (TextView) vwParentRow.getChildAt(0);
                    //date_select(
                    String hh_id = (String) child.getText();
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new java.util.Date());
                    //TODO: Sampled check and pass this value as it is
//                    if(vwParentRow.getBackground().is)
                    db.update_status_hh(hh_id, cluster_selected, "True","", "",  "NULL", "Not Visited","", "NULL", "1");
                    setview(cluster_selected);
                }
            });
            builder.setNegativeButton("Re-scheduled Visit",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LinearLayout vwParentRow = (LinearLayout) v.getParent();
                            TextView child = (TextView) vwParentRow.getChildAt(0);
                            date_select((String) child.getText());

                            //update_status("", (String) child.getText(), "Revisit");
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

            /////////////////////////////////////////////////////
        }
    }
    public void date_select(final String hh_id) {


        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog mDatePicker;

        mDatePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                selectedmonth = selectedmonth + 1;
                date_re = selectedyear + "." + selectedmonth + "." +selectedday ;
                if(datepicker.isShown())
                {
                    time_select(hh_id, date_re);
                }
                else {
                   //
                  //  Toast.makeText(MainActivity.this, "not show7", Toast.LENGTH_SHORT).show();
                }
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    mDatePicker.cancel();
                    mDatePicker.dismiss();
                  //  go_on = true;
                }
            }
        });
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
        mDatePicker.setTitle("Select Date of Revisit");
        mDatePicker.getDatePicker().setCalendarViewShown(false);
        mDatePicker.setCancelable(true);
        mDatePicker.setCanceledOnTouchOutside(true);
        //mDatePicker.
        //mDatePicker.setButton(1);


                mDatePicker.show();
       // mDatePicker.

       // return date_re;
    }
        public void time_select(final String hh_id, final String date){
        /////////////////////////////////////
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                eReminderTime.setText( selectedHour + ":" + selectedMinute);
                if(timePicker.isShown()) {
                    String loc = getLocation();
                    time_re = selectedHour + ":" + selectedMinute;
                    DataBase db = new DataBase(MainActivity.this);
                   // String[] ccode = cluster_selected.split("-");
                    //Log.i("qqqqq1", ccode[1]);
                    if (loc.contains("one"))
                        loc = "0-0";
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new java.util.Date());
                    db.update_status_hh(hh_id, cluster_selected, "True", "", loc, date + "-" + time_re, "Revisit","", timeStamp,"1");
                    Toast.makeText(MainActivity.this, hh_id + loc + time_re + date, Toast.LENGTH_SHORT).show();
                    sync_on_up = true;
                    if (sync_on_up) {
                        stat.setImageResource(R.drawable.status);
                    }
                    setview(cluster_selected);
                }
                else
                {

                }

            }
        }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                       // mTimePicker.cancel();
                      //  mTimePicker.dismiss();
                        //  go_on = true;
                    }
                }
            });
            mTimePicker.setTitle("Select Time of Revisit");
            //mTimePicker.
        mTimePicker.show();

     //   return time_re;

    }
    //Replace Enumerator Selection
    public void replace_enum_pop(View v, final String hh_id, final String cluster_selected) {
        final String[] selection = new String[1];
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout) v.getParent();
//        final int c = Color.CYAN;
//
//
//        vwParentRow.setBackgroundColor(c);
//        vwParentRow.refreshDrawableState();
        final TextView child = (TextView) vwParentRow.getChildAt(0);
        final DataBase db = new DataBase(MainActivity.this);
        final String[] select_enum = db.fetch_enums();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select enumerator who picked replacement ?");
        builder.setSingleChoiceItems(select_enum, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection[0] = select_enum[which];
                    }
                });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //to update the status upon clicking the visit and confirimg change
//                child.setHighlightColor(45621);
                boolean replace_status = db.replace_HH_ID(selection[0], hh_id, cluster_selected);
                if(replace_status) {
                    Toast.makeText(MainActivity.this, "HH " + hh_id + " replaced for " + selection[0], Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Sample count is exceeding or no HHID available for swap", Toast.LENGTH_SHORT).show();
                }
                setview(cluster_selected);
                //status_changed_refusal_visited(selection[0], (String) child.getText(), "visited");
                // update_status(selection[0], (String) child.getText(), "visited");

            }
        });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //////////////////////////////////////////////////////////
    public void select_enum_pop(View v) {
        final String[] selection = new String[1];
        //get the row the clicked button is in
        LinearLayout vwParentRow = (LinearLayout) v.getParent();
//        final int c = Color.CYAN;
//
//
//        vwParentRow.setBackgroundColor(c);
//        vwParentRow.refreshDrawableState();
        final TextView child = (TextView) vwParentRow.getChildAt(0);
        DataBase db = new DataBase(MainActivity.this);
        final String[] select_enum = db.fetch_enums();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select enumerator ?");
        builder.setSingleChoiceItems(select_enum, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection[0] = select_enum[which];
                    }
                });
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //to update the status upon clicking the visit and confirimg change
//                child.setHighlightColor(45621);
                status_changed_refusal_visited(selection[0], (String) child.getText(), "visited");
               // update_status(selection[0], (String) child.getText(), "visited");

            }
        });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void status_changed_refusal_visited(final String Enm, final String hh_id, String status)
    {
        final String[] Result = {"Successfully Interviewed", "refusal or unavailable"};
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Dialog));
        builder.setTitle("Response to "+Enm);
//        builder.setPositiveButton("Successfully Interviewed", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //to update the status upon clicking the visit and confirimg change
////                child.setHighlightColor(45621);
////                status_changed(selection[0], (String) child.getText(), "visited");
////                update_status(selection[0], (String) child.getText(), "visited");
//                Toast.makeText(MainActivity.this, "Permission granted to " + Enm, Toast.LENGTH_LONG).show();
//                update_status(Enm, hh_id, "Successfully interviewed");
//
//            }
//        });
//        builder.item
        builder.setItems(Result, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, Result[i] +"  BY  "+Enm, Toast.LENGTH_LONG).show();
                update_status(Enm, hh_id, Result[i]);
            }
        });
//        builder.setNegativeButton("No eligble Child", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //to update the status upon clicking the visit and confirimg change
////                child.setHighlightColor(45621);
////                status_changed(selection[0], (String) child.getText(), "visited");
////                update_status(selection[0], (String) child.getText(), "visited");
//                Toast.makeText(MainActivity.this, "No elgible Child "+Enm, Toast.LENGTH_LONG).show();
//                update_status(Enm, hh_id, "No eligible Child");
//
//            }
//        });
//        builder.setNegativeButton("Covid Assesment failed", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //to update the status upon clicking the visit and confirimg change
////                child.setHighlightColor(45621);
////                status_changed(selection[0], (String) child.getText(), "visited");
////                update_status(selection[0], (String) child.getText(), "visited");
//                Toast.makeText(MainActivity.this, "Covid Assesment Failed "+Enm, Toast.LENGTH_LONG).show();
//                update_status(Enm, hh_id, "Covid Assesment Failed");
//
//            }
//        });
//        builder.setNeutralButton("Refusal",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // dialog.dismiss();
//                        Toast.makeText(MainActivity.this, "Refusal  " + Enm, Toast.LENGTH_LONG).show();
//                        update_status(Enm, hh_id, "refusal");
//                    }
//                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //    update satatus function function
    public void update_status(String enumerator, String HH_id_, String status) {

        sync_on_up = true;
        String loc = getLocation();

        Log.i("hhhhhhhhhhhhhhh", HH_id_);
        if(loc.contains("Non")){
            if (loc.contains("Non"))
            {
                loc = "0-0";
            }
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new java.util.Date());
            Toast.makeText(MainActivity.this, enumerator + status + "---- " + HH_id_ + "@@@@" + timeStamp + "--------" + loc, Toast.LENGTH_SHORT).show();
            DataBase db = new DataBase(MainActivity.this);
           // String[] ccode =cluster_selected.split("-");
          //  Log.i("qqqqq1", ccode[1]);
            db.update_status_hh(HH_id_,cluster_selected,"True", enumerator,loc , "", "visited", status, timeStamp,"1");
//            sync_date_pushed();
//            finish();
//            startActivity(getIntent());
        }
        else {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new java.util.Date());
            Toast.makeText(MainActivity.this, enumerator + status + "---- " + HH_id_ + "@@@@" + timeStamp + "--------" + loc, Toast.LENGTH_SHORT).show();
            DataBase db = new DataBase(MainActivity.this);
         //   String[] ccode =cluster_selected.split("-");
         //   Log.i("qqqqq1", ccode[1]);
            db.update_status_hh(HH_id_, cluster_selected, "True", enumerator, loc, "","visited", status, timeStamp,"1");
//            sync_date_pushed();

        }
        sync_on_up = true;
        if (sync_on_up) {
            stat.setImageResource(R.drawable.status);
        }
        setview(cluster_selected);

    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void buildAlertMessageNoCluster() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Cluster list is empty Try loggin in again?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        DataBase db = new DataBase(MainActivity.this);
                        db.delete_all();
                        db.close();
                        sync_on_up = false;
                       // MainActivity.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    //to fetch location of the user
    public String getLocation()

    {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!= null)
            {
                double longi = location.getLongitude();
                double latti = location.getLatitude();
                return Double.toString(longi) + "-"+Double.toString(latti);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
                Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if(location!= null)
                {
                    double longi = location2.getLongitude();
                    double latti = location2.getLatitude();
                    return Double.toString(longi) + "-"+Double.toString(latti);
                }
                else
                {
                    Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location!= null)
                    {
                        double longi = loc.getLongitude();
                        double latti = loc.getLatitude();
                        return Double.toString(longi) + "-"+Double.toString(latti);
                    }
                }

            }
        }
        return "None";
    }
    private void setview(String selection) {
        //db linkup

        ListView listView = (ListView) findViewById(R.id.hh_list);

        //db link up and fetch HH_id status
        ArrayList<status> status_List = new ArrayList<status>();
        DataBase db = new DataBase(MainActivity.this);
       // String[] ar = selection.split("-");
       // Log.i("split", ar[0] + "aaaaaaa");
       // Log.i("split", ar[1] + "aaaaaaa");
        status_List = db.fetch_statuses(selection);
        listView.setAdapter(null);
        custom_adapter adapter = new custom_adapter(MainActivity.this, R.layout.list_rows, status_List);
        listView.setAdapter(adapter);
        sync_on_up = db.get_flag();
        if(sync_on_up)
        {
            stat.setImageResource(R.drawable.status);
            stat.setVisibility(View.VISIBLE);
        }

    }

    public void sync(final String code) {
        //TODO volley request and publishing data to the remote database
        RequestQueue queue = Volley.newRequestQueue(this);
        String url2 =getString(R.string.test_url)+"GetDate.php";
        final DataBase db = new DataBase(MainActivity.this);
        // Request a string response from the provided URL.
        StringRequest postRequest2 = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        String date;
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject obj  = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("Date_log");
                            if (arr.getJSONObject(0).getString("date").isEmpty())
                            {
                               // Toast.makeText(MainActivity.this, response+ "if get date", Toast.LENGTH_SHORT).show();
                                date = "2018.03.10";
                            }
                            else
                                date = arr.getJSONObject(0).getString("date");
                           // Toast.makeText(MainActivity.this, date+ "else get date", Toast.LENGTH_SHORT).show();
                            Log.i("ddddmssql",date);
                            check_final_sync(date);


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
                        Log.d("Error.Response get date", error.toString());
                        Toast.makeText(MainActivity.this, "Internet Connection error- Timeout", Toast.LENGTH_SHORT).show();

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

        queue.add(postRequest2);
        db.close();






        ///////////////////////////////////////////////

    }

    ///fetch time from mssql
    void check_final_sync(final String datesq){
        ///loading enums
        DataBase db = new DataBase(MainActivity.this);

        String sql_date = datesq;
        sql_date = sql_date.replace("-", ".");
        String[] new_sql = sql_date.split(" ");
        String sqlite_date = db.sync_data_request();
        Toast.makeText(MainActivity.this, new_sql[0]+"-----"+sqlite_date, Toast.LENGTH_LONG).show();
        if(sqlite_date.contains("Non") && sql_date.equals(null)){
            Toast.makeText(MainActivity.this, "No date found", Toast.LENGTH_SHORT).show();
        }
        else if(datesq.equals("2018.03.10")){
//            Toast.makeText(MainActivity.this, "Syncing data", Toast.LENGTH_SHORT).show();
            ArrayList<status_HH_model> update_list = new ArrayList<status_HH_model>();
            update_list=db.syncing_data(datesq);
            for(int i=0; i < update_list.size(); i++) {
//                if(update_list.get(i).getTimestamp().length()>0)
                send_date(update_list.get(i));

            }
            Toast.makeText(MainActivity.this, "Successfully Updated....", Toast.LENGTH_SHORT).show();
        }
        else {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
            try {
                Date sql =   date.parse(sql_date.replace(".000","").replace(".","-"));
//                Date lite = (Date) date.parse(sqlite_date);
//                java.util.Date sql = date.parse(new_sql[0]);
                java.util.Date lite = date.parse(sqlite_date);

                if ((sql.compareTo(lite)) != 0) //sql is before lite
                {
                    //populate to mssql data base volley request
                    Toast.makeText(MainActivity.this, sql_date+"Syncing......."+sqlite_date, Toast.LENGTH_SHORT).show();
                    ArrayList<status_HH_model> update_list = new ArrayList<status_HH_model>();
                    update_list=db.syncing_data(sql.toString());
                    for(int i=0; i < update_list.size(); i++) {
                        send_date(update_list.get(i));
                        Toast.makeText(MainActivity.this, "Successfully Updated....", Toast.LENGTH_SHORT).show();
                    }
                    db.set_flag();
                } else if ((sql.compareTo(lite)) == 0) {

                    Toast.makeText(MainActivity.this, "upto date with : "+sqlite_date, Toast.LENGTH_SHORT).show();
                }
                else if ((sql.compareTo(lite)) > 0)//sql is after lite
                {
                    Toast.makeText(MainActivity.this, "Updated on mssql", Toast.LENGTH_SHORT).show();
//                    ArrayList<status_HH_model> update_list = new ArrayList<status_HH_model>();
//                    update_list=db.syncing_data();
//                    for(int i=0; i < update_list.size(); i++) {
//                        send_date(update_list.get(i));
//                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    void send_date(final status_HH_model status_hh_model){
         final String code = getIntent().getExtras().getString("Code", null);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url2 =getString(R.string.test_url)+"Update_hh.php";
            // Request a string response from the provided URL.
            StringRequest postRequest2 = new StringRequest(Request.Method.POST, url2,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
//                            Toast.makeText(MainActivity.this, "please wait Syncing in process", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            // error
//                            Log_entries.d("Error.Respon_send_date", error.toString());
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("hh_id", status_hh_model.getHh_id());
                    params.put("cluster", status_hh_model.getCluster());
                    params.put("hh_status", status_hh_model.getHH_status());
                    params.put("hh_sampled", status_hh_model.getHH_sampled());
                    params.put("hh_result", status_hh_model.getHH_Result());
                    params.put("timestamp", status_hh_model.getTimestamp());
                    params.put("loc", status_hh_model.getLoc());
                    params.put("enum",status_hh_model.getEnum_code() );
                    params.put("supervisor", code);
                    params.put("revist",status_hh_model.getRevist_time());
                    return params;
                }
            };

            queue.add(postRequest2);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://pk.clsurvey.gb.supervision/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://pk.clsurvey.gb.supervision/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
