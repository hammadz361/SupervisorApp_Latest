package pk.clsurvey.gb.supervision;

/**
 * Created by Ehtisham Ullah on 3/5/2018.
 */
public class cluster_model {


    private String District;
    private String Cluster_code;
    private String Cluster_Name;
    private String Supervisor_code;


    public cluster_model(){
        this.District="";
        this.Cluster_code="";
        this.Cluster_Name="";
        this.Supervisor_code="";
    }

    public cluster_model(String D, String CC, String C, String S){
        this.District=D;
        this.Cluster_code=CC;
        this.Cluster_Name=C;
        this.Supervisor_code=S;
    }
    public String getDistrict() {
        return District;
    }

    public String getCluster_code() {
        return Cluster_code;
    }

    public String getCluster_Name() {
        return Cluster_Name;
    }

    public String getSupervisor_code() {
        return Supervisor_code;
    }


    public void setDistrict(String district) {
        District = district;
    }

    public void setCluster_code(String cluster_code) {
        Cluster_code = cluster_code;
    }

    public void setCluster_Name(String cluster_Name) {
        Cluster_Name = cluster_Name;
    }

    public void setSupervisor_code(String supervisor_name) {
        Supervisor_code = supervisor_name;
    }
}
