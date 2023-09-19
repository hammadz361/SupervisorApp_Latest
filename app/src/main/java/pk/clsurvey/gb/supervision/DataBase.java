package pk.clsurvey.gb.supervision;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ehtisham Ullah on 3/4/2018.
 */
public class DataBase extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "gbcls";

    //tables
    private static final String TABLE_supervisor = "Supervisor";
    private static final String TABLE_cluster = "cluster";
    private static final String TABLE_HH = "HH";
    private static final String TABLE_EN_SUP = "EMUM_SUP";
    private static final String Table_log_date = "log_date";

    //columns supervisor
    private static final String supervisor_name = "supervisor_name";
    private static final String supervisor_code = "supervisor_code";

    //columns cluster
    private static final String Cluster_name = "cluster_name";
    private static final String Cluster_code = "cluster_code";
    private static final String District = "district";
    private static final String Supervisor = "supervisor_code";

    //columns households
    private static final String HH_id = "HH_id";
    private static final String HH_status = "HH_status";
    private static final String HH_result = "HH_result";
    private static final String HH_sampled = "HH_sampled";
    private static final String enumerator = "enumerator";
    private static final String timestamp = "timestamp";
    private static final String revisit_time = "revisit_time";
    private static final String location_HH ="location";
    private static final String HH_Sr = "HH_Sr";
    private static  final String sync ="sync";

    //colums enum_sup
    private static final String enum_name = "enum_name";
    private static final String enum_code = "enum_code";
    private static final String supervisor1 = "supervisor1";


    //colums log_date
    private static final String sn = "sn";
    private static final String log_date = "log_date";

    //create table string supersvisor
    private static final String CREATE_supervisor_table = "CREATE TABLE " + TABLE_supervisor + " ("
            +supervisor_name+ " TEXT ,"
            +supervisor_code+ " TEXT "+ ");";

    //create table string cluster
    private static final String CREATE_Cluster_table = "CREATE TABLE " + TABLE_cluster + " ("
            +District + " TEXT ,"
            +Cluster_code + " TEXT ,"
            +Cluster_name + " TEXT ,"
            +Supervisor + " TEXT "+ ");";

    //create table string HH
    private static final String CREATE_HH_table = "CREATE TABLE " + TABLE_HH + " ("
            +HH_id + " TEXT ,"
            +Cluster_code + " TEXT ,"
            +HH_result + " TEXT ,"
            +HH_status + " TEXT ,"
            +HH_sampled + " TEXT ,"
            +enumerator + " TEXT ,"
            +timestamp + " TEXT ,"
            +location_HH + " TEXT, "
            +revisit_time + " TEXT, "
            +HH_Sr + " TEXT, "
            +sync + " TEXT "+ ");";

    //create table string enum_sup
    private static final String CREATE_Enum_Sup = "CREATE TABLE " + TABLE_EN_SUP + " ("
            +enum_name + " TEXT ,"
            +enum_code + " TEXT ,"
            +supervisor1 + " TEXT "+ ");";

    private static final String Create_log_date = "CREATE TABLE " + Table_log_date + " ("
            +sn + " TEXT ,"
            +log_date + " TEXT "+ ");";


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_supervisor_table);
        db.execSQL(CREATE_Cluster_table);
        db.execSQL(CREATE_HH_table);
        db.execSQL(CREATE_Enum_Sup);
        db.execSQL(Create_log_date);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_supervisor);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_cluster);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EN_SUP);
        db.execSQL("DROP TABLE IF EXISTS " + Table_log_date);
        // Create tables again
        onCreate(db);
    }
    public void delete_all(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_supervisor);
        db.execSQL("delete from "+ TABLE_cluster);
        db.execSQL("delete from "+ TABLE_HH);
        db.execSQL("delete from "+ TABLE_EN_SUP);
        db.execSQL("delete from "+ Table_log_date);
        db.close();

    }
    public void initiate(){
        String selectQuery = "SELECT sn FROM " + Table_log_date;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Log.i("tttttt", String.valueOf(cursor.getCount()));
            Log.i("tttttt", cursor.getString(0));
            db.close();
            return;
        }
        else
        {
        ContentValues values = new ContentValues();

        values.put(sn, "1");
        // Inserting Row
        db.insert(Table_log_date, null, values);
        db.close();
    }
    }

    //supervisor table fill with successfull login
    public void login_success(user_model user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(supervisor_name, user.getUser());
        values.put(supervisor_code, user.getCode());

        // Inserting Row
        db.insert(TABLE_supervisor, null, values);
        db.close(); // Closing database connection
    }
    //populating enumarators table on successfull login of supervisor
    public void load_enum(user_model user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(enum_name, user.getUser());
        values.put(enum_code, user.getCode());

        // Inserting Row
        db.insert(TABLE_EN_SUP, null, values);
        db.close(); // Closing database connection
    }

    //fetching cluster to populate the list in Main
    public String[] fetch_cluster(String Supervisor){

        // Select All Query
        String selectQuery = "SELECT cluster_name , cluster_code FROM " + TABLE_cluster +" WHERE supervisor_code = '" +Supervisor+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = cursor.getCount();
        String[] clus = new String[i];
        int j = 0;
        if (cursor.moveToFirst()) {
            do {
                Log.i("clustersssss", cursor.getString(0).concat("-").concat(cursor.getString(1)));
                clus[j] = cursor.getString(1); //.concat("-").concat(cursor.getString(1));
                j++;
            } while (cursor.moveToNext());
        }
        db.close();
        return clus;
    }



    //fetching cluster to populate the list in Main
    public String[] fetch_enums(){
//        String[] clus = {""};
        // Select All Query
        String selectQuery = "SELECT "+enum_name + " , "+enum_code  +" FROM " + TABLE_EN_SUP ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int j = cursor.getCount();
        String [] clus = new String[j];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                clus[i] = cursor.getString(0).concat("-").concat(cursor.getString(1));
                i++;
            } while (cursor.moveToNext());
        }
        db.close();
        return clus;
    }


    //loading cluster in which are collected from mssql volleyrequest
    public void load_cluster(cluster_model ar){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_cluster +" where "+ Cluster_code +" = '"+ar.getCluster_code()+"';";

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount()>0)
        {
            db.close();
            return;
        }
        ContentValues values = new ContentValues();

        values.put(District, ar.getDistrict());
        values.put(Cluster_name, ar.getCluster_Name());
        values.put(Cluster_code, ar.getCluster_code());
        values.put(Supervisor, ar.getSupervisor_code());

        // Inserting Row
        db.insert(TABLE_cluster, null, values);
        db.close(); // Closing database connection
    }

    //loading HH_lists from volleyrequest
    public void load_HH(String hh_id, String HH_cluster, String hh_status, String hh_sampled, String hh_result, String En, String location, String time_, String revisit, String hh_Sr){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.i("lllll", HH_cluster);
        values.put(HH_id, hh_id);
        values.put(enumerator, En);
        values.put(location_HH, location);
        values.put(timestamp, time_);
        values.put(HH_sampled, hh_sampled);
        values.put(Cluster_code, HH_cluster);
        values.put(HH_status, hh_status);
        values.put(HH_result, hh_result);
        values.put(revisit_time, revisit);
        values.put(HH_Sr, hh_Sr);
        // Inserting Row
        db.insert(TABLE_HH, null, values);
        db.close(); // Closing database connection
    }

    //check if logged-in already
    public user_model check(){
        user_model user_ = new user_model();

        String selectQuery = "SELECT  * FROM " + TABLE_supervisor ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()==0)
        {
            user_.user=""; user_.code="";
            return user_;
        }
        else if (cursor.moveToFirst()) {
            do {
                user_.user = cursor.getString(0);
                user_.code = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        db.close();
        return user_;
    }

    //fetch status_hh and Hh_id of the cluster
    public ArrayList<status> fetch_statuses(String cluster){
        ArrayList<status> status_List = new ArrayList<status>();
        // Select All status of the cluster
        String selectQuery = "SELECT "+ HH_id+ "," +HH_status + "," +HH_result+ "," +HH_sampled+"," + timestamp+"," + revisit_time+" FROM " + TABLE_HH +" WHERE " + Cluster_code + "='"+cluster+"' order by " + HH_sampled + " DESC , CAST("+HH_Sr+" as INT)";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                status contact = new status();
                Log.i("HH_",cursor.getString(0) );
                Log.i("HH_",cursor.getString(0) );
                Log.i("HH_ss",cursor.getString(1));
                contact.setHH_IS(cursor.getString(0));
                contact.setStatus_HH(cursor.getString(1));
                contact.setResult_HH(cursor.getString(2));
                contact.setSampled(cursor.getString(3));
                contact.setTimestamp(cursor.getString(4));
                contact.setRevisit(cursor.getString(5));
                // Adding contact to list
                status_List.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        return status_List;
    }

    public void update_status_hh(String hh_id, String Cluster,String sampled,  String En, String location, String revisit_, String status, String hh_result, String time_, String sync_){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HH_status, status);
        values.put(HH_result, hh_result);
        values.put(HH_sampled, sampled);
        values.put(enumerator, En);
        values.put(location_HH, location);
        values.put(timestamp, time_);
        values.put(sync, sync_);
        if(revisit_.length()>0)
        {
            values.put(revisit_time, revisit_);
        }



        // updating row
//        db.update(TABLE_HH, values, HH_id + " = "+hh_id + " AND cluster_code ="+Cluster, null);
        db.update(TABLE_HH, values, Cluster_code + " = ? AND " + HH_id + " = ?",
                new String[]{Cluster, hh_id});
        db.close();
    }

    public void set_flag(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sync, "0");



        // updating row
//        db.update(TABLE_HH, values, HH_id + " = "+hh_id + " AND cluster_code ="+Cluster, null);
        db.update(TABLE_HH, values, sync + " = ? ",
                new String[]{"1"});
        db.close();
    }
    public boolean get_flag(){
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_HH +" Where "+ sync +"= '1'" ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()>0) {

            db.close();
            return true;
        }
        db.close();
        return false;

    }


    public String sync_data_request(){
        String selectQuery = "SELECT "+ log_date +" FROM " + Table_log_date ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            String ar = cursor.getString(0);
            db.close();
            return ar;
        }
        else
            return "None";
    }

    public void pushed_data(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(log_date, date);
        // updating row
        db.update(Table_log_date, values, sn + " = 1", null);
        db.close();
    }

    public ArrayList<status_HH_model> syncing_data(String date)
    {
        String filter = " WHERE "+ sync + " = 1 " ;
        if(!date.equals(""))
        {
            filter = filter + " OR "+ timestamp +" >= strftime('%Y-%m-%d %H:%M:%S', '"+date+"')";//+ date+"'";
        }
        String selectQuery = "SELECT * FROM " + TABLE_HH +filter ;
        ArrayList<status_HH_model> status_List = new ArrayList<status_HH_model>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                status_HH_model contact = new status_HH_model();
                contact.setHh_id(cursor.getString(0));
                contact.setCluster(cursor.getString(1));
                contact.setHH_Result(cursor.getString(2));
                contact.setHH_status(cursor.getString(3));
                contact.setHH_sampled(cursor.getString(4));
                contact.setEnum_code(cursor.getString(5));
                contact.setTimestamp(cursor.getString(6));
                contact.setLoc(cursor.getString(7));
                contact.setRevist_time(cursor.getString(8));
                // Adding contact to list
                status_List.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        return status_List;
    }

    public ArrayList<status_HH_model> log_entries()
    {
        String selectQuery = "SELECT * FROM " + TABLE_HH +" WHERE "+HH_status+"  not like '%Not%' order by "+Cluster_code;
        ArrayList<status_HH_model> status_List = new ArrayList<status_HH_model>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                status_HH_model contact = new status_HH_model();
                contact.setHh_id(cursor.getString(0));
                contact.setCluster(cursor.getString(1));
                contact.setHH_status(cursor.getString(3));
                contact.setHH_Result(cursor.getString(2));
                contact.setTimestamp(cursor.getString(6));
                contact.setEnum_code(cursor.getString(5));
                contact.setHH_sampled(cursor.getString(4));
                contact.setLoc(cursor.getString(7));
                contact.setRevist_time(cursor.getString(8));
                // Adding contact to list
                status_List.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        return status_List;
    }

    public boolean replace_HH_ID(String Enum, String hh_id, String Cluster) {

        int spare_count = Integer.parseInt(cluster_spare(Cluster));
        if(cluster_count(Cluster).equals("22") && spare_count>0)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            String replace_option = "select "+HH_id+"  from "+TABLE_HH+" where "+HH_sampled+" = 'False' AND "+Cluster_code+"='"+Cluster+"' ORDER BY CAST("+HH_Sr+" AS INTEGER) LIMIT 1";
            Cursor cursor1 = db.rawQuery(replace_option, null);
            cursor1.moveToFirst();
            String New_HHID = cursor1.getString(0);
            cursor1.close();
            String selectQuery = "update "+TABLE_HH+" set "+HH_sampled+" = 'True',  "+timestamp+" = strftime('%Y-%m-%d %H:%M:%S', 'now') , "+sync+"='1' where "+Cluster_code+"="+Cluster+" AND "+HH_id+" = '"+New_HHID+"'"  ;
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            cursor.close();
            String true_new = "Select "+HH_sampled+" from " + TABLE_HH + " where "+Cluster_code+"="+Cluster+" AND "+HH_id+" = '"+New_HHID+"'"  ;
            Cursor cursor1_ = db.rawQuery(true_new, null);
            cursor1_.moveToFirst();
            if(cluster_count(Cluster).equals("23") && cursor1_.getString(0).equals("True")) {
                String update_query = "update " + TABLE_HH + " set " + HH_sampled + " = 'Excluded' ,  "+timestamp+" = strftime('%Y-%m-%d %H:%M:%S','now') , "+sync+"='1', " +enumerator+"='"+Enum+"'  where " + HH_id + " = '" + hh_id + "' AND " + Cluster_code + " = '" + Cluster + "'";
                Cursor cursor2 = db.rawQuery(update_query, null);
                cursor2.moveToFirst();
                cursor2.close();
                db.close();
                return true;
            }
            cursor.close();
            db.close();
        }
            return false;

    }

    public String HH_ID_In_Cluster(String cluster_selected) {
        String selectQuery = "SELECT COUNT("+HH_sampled+") FROM " + TABLE_HH +" where "+HH_sampled+" = 'False' AND "+Cluster_code+" = '"+cluster_selected+"'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String value = cursor.getString(0);
        db.close();

        return value;
    }
    public String check_replace(String hh_id, String cluster_selected) {
        String selectQuery = "SELECT "+HH_sampled+", "+HH_status+" FROM " + TABLE_HH +" where "+HH_id+" = '"+hh_id+"' AND "+Cluster_code+" = '"+cluster_selected+"'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String value = cursor.getString(0);
        String hh_status = cursor.getString(1);
        db.close();
        if(hh_status.equals("visited")) {
            return "visited";
        }
       return value;
    }
    public String cluster_count(String cluster_selected) {
        String selectQuery = "SELECT COUNT("+HH_id+") FROM " + TABLE_HH +" where "+Cluster_code+" = '"+cluster_selected+"' AND " + HH_sampled + " = 'True' " ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String value = cursor.getString(0);
//        db.close();
        return value;
    }
    public String cluster_spare(String cluster_selected) {
        String selectQuery = "SELECT COUNT("+HH_id+") FROM " + TABLE_HH +" where "+Cluster_code+" = '"+cluster_selected+"' AND " + HH_sampled + " = 'False' " ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String value = cursor.getString(0);
//        db.close();
        return value;
    }
}