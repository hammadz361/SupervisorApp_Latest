package pk.clsurvey.gb.supervision;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Log_entries extends Activity {

    private ListView listView;
//    public static String [] values ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
//        String [] values= {"adsfasfda", "asdfasdfasdf"};
        DataBase dbb = new DataBase(Log_entries.this);

        ArrayList<status_HH_model> update_list = new ArrayList<status_HH_model>();
        update_list=dbb.log_entries();
        String [] values =new String[update_list.size()];
        for(int i=0; i < update_list.size(); i++) {
                    String revist = "__Revisit : " + update_list.get(i).getRevist_time();
                    if(revist.contains("null")) revist ="";
                    values[i] = "Cluster : " + update_list.get(i).getCluster() +"  HH_ID : " + update_list.get(i).getHh_id() +  "_" + update_list.get(i).getHH_status() + "_By : " + update_list.get(i).getEnum_code() + "__On : " + update_list.get(i).getTimestamp()+ revist ;


        }
        dbb.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView = (ListView) findViewById(R.id.log_list);
         // Assign adapter to ListView
        listView.setAdapter(adapter);


    }
}
