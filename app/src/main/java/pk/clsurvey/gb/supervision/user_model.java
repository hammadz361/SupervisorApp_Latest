package pk.clsurvey.gb.supervision;

/**
 * Created by Ehtisham Ullah on 3/5/2018.
 */
public class user_model {

    public String user;
    public String code;


    public user_model() {
        this.user = "";
        this.code = "";
    }
    public user_model(String user, String code) {
        this.user = user;
        this.code = code;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
