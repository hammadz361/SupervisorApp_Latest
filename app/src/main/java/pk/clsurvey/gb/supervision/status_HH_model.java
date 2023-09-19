package pk.clsurvey.gb.supervision;

/**
 * Created by Ehtisham Ullah on 3/12/2018.
 */
public class status_HH_model {




    private String hh_id;
    private String HH_status;



    private String HH_sampled;
    private String HH_Result;
    private String Cluster;
    private String timestamp;
    private String loc;
    private String enum_code;
    private String revist_time;




    public status_HH_model() {
        this.hh_id = "";
        this.HH_status = "";
        this.Cluster="";
        this.timestamp = "";
        this.loc = "";
        this.enum_code = "";
        this.revist_time="";
        this.HH_Result = "";
        this.HH_sampled="";
    }

    public status_HH_model(String hh_id, String HH_status, String HH_sampled, String timestamp, String loc, String enum_code, String cluster, String revist, String hh_result) {
        this.hh_id = hh_id;
        this.HH_status = HH_status;
        this.timestamp = timestamp;
        this.loc = loc;
        this.HH_sampled = HH_sampled;
        this.Cluster=cluster;
        this.enum_code = enum_code;
        this.revist_time = revist;
        this.HH_Result = hh_result;
    }
    public String getHH_sampled() {
        return HH_sampled;
    }

    public void setHH_sampled(String HH_sampled) {
        this.HH_sampled = HH_sampled;
    }

    public String getHh_id() {
        return hh_id;
    }

    public void setHh_id(String hh_id) {
        this.hh_id = hh_id;
    }

    public String getHH_status() {
        return HH_status;
    }

    public void setHH_status(String HH_status) {
        this.HH_status = HH_status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getEnum_code() {
        return enum_code;
    }

    public void setEnum_code(String enum_code) {
        this.enum_code = enum_code;
    }

    public String getCluster() {
        return Cluster;
    }

    public void setCluster(String cluster) {
        Cluster = cluster;
    }
    public String getRevist_time() {
        return revist_time;
    }

    public void setRevist_time(String revist_time) {
        this.revist_time = revist_time;
    }
    public String getHH_Result() {
        return HH_Result;
    }

    public void setHH_Result(String HH_Result) {
        this.HH_Result = HH_Result;
    }
}
