package fourheads.org.gestionescuelaandroid.services;

import java.util.ArrayList;
import java.util.List;

import fourheads.org.gestionescuelaandroid.dom.IsisService;

/**
 * Created by leandro on 26/10/14.
 */
public class JSONService {

    String url;
    String user;
    String pass;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public JSONService (String url, String user, String pass){

        setUrl(url);
        setUser(user);
        setPass(pass);
    }

    public List<IsisService> findServices(){

        List<IsisService> isisServiceList = new ArrayList<IsisService>();

        //dummy services
        IsisService service01= new IsisService();
        IsisService service02= new IsisService();
        IsisService service03= new IsisService();

        service01.setTitle("Servicio 01");
        service02.setTitle("Servicio 02");
        service03.setTitle("Servicio 03");

        isisServiceList.add(service01);
        isisServiceList.add(service02);
        isisServiceList.add(service03);

        return isisServiceList;
    }


}
