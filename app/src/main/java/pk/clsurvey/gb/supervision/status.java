package pk.clsurvey.gb.supervision;

/**
 * Created by Ehtisham Ullah on 3/6/2018.
 */
public class status {
    private String HH_IS;
    private String status_HH;
    private String result_HH;

    public String getSampled() {
        return sampled;
    }

    public void setSampled(String sampled) {
        this.sampled = sampled;
    }

    private String sampled;

    public String getRevisit() {
        return revisit;
    }

    public void setRevisit(String revisit) {
        this.revisit = revisit;
    }

    private String revisit;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;

    public status() {
        this.HH_IS = "";
        this.status_HH = "";
        this.result_HH="";
        this.timestamp ="";
        this.revisit="";
        this.sampled="";

    }
    public status(String HH_IS, String status_HH, String result_HH, String timestamp, String revist, String sampled) {
        this.HH_IS = HH_IS;
        this.status_HH = status_HH;
        this.result_HH = result_HH;
        this.timestamp = timestamp;
        this.revisit = revist;
        this.sampled = sampled;
    }

    public String getHH_IS() {
        return HH_IS;
    }

    public void setHH_IS(String HH_IS) {
        this.HH_IS = HH_IS;
    }

    public String getStatus_HH() {
        return status_HH;
    }

    public void setStatus_HH(String status_HH) {
        this.status_HH = status_HH;
    }
    public String getResult_HH() { return result_HH; }
    public void setResult_HH(String result_HH) { this.result_HH = result_HH; }

}
