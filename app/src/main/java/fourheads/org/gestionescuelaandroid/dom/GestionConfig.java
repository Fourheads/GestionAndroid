package fourheads.org.gestionescuelaandroid.dom;

/**
 * Created by leandro on 16/11/14.
 */
public class GestionConfig {

    String user;
    String pass;
    String servidor;
    String puerto;
    String directorio;
    Boolean save;

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

    public Boolean getSave() {
        return save;
    }

    public void setSave(Boolean save) {
        this.save = save;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public String getUrlRestful(){

        String servidor = getServidor();
        String puerto = getPuerto();
        String directorio = getDirectorio();
        if (servidor != null) {
            if (servidor.isEmpty()) {
                return "";
            }


            if (puerto.isEmpty()){
                puerto = "8080";
            }

            if (directorio.isEmpty()){
                directorio = "/restful";
            }

            String url = "http://" + servidor + ":" + puerto + directorio + "/";

            return url;
        }
        return "";
    }
}
