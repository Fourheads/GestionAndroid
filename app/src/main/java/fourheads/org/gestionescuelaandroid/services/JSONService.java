package fourheads.org.gestionescuelaandroid.services;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import fourheads.org.gestionescuelaandroid.dom.IsisService;
import fourheads.org.gestionescuelaandroid.dom.RestLink;
import fourheads.org.gestionescuelaandroid.dom.RestLinks;
import fourheads.org.gestionescuelaandroid.dom.Services;

/**
 * Created by leandro on 26/10/14.
 */
public class JSONService {

    String url;
    String user;
    String pass;
    Services services;

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

        new FindServicesThread().execute();
        //List<IsisService> isisServiceList = services.getValue();

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

    private class FindServicesThread extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Services services = null;
                Log.v("ingresando User y Pass", user + " : " + pass);
                // Set the username and password for creating a Basic Auth request
                HttpAuthentication authHeader = new HttpBasicAuthentication(user, pass);
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setAuthorization(authHeader);
                HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

                Log.v("ingresando URL",url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                // Make the HTTP GET request to the Basic Auth protected URL
                ResponseEntity<RestLinks> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, RestLinks.class);

                //return response.getBody();

                RestLinks restLinks = response.getBody();

                Log.v("leido", restLinks.getLinks().size()+"");

                //Buscar los servicios

                for (RestLink restlink : restLinks.getLinks()){

                    if (restlink.getRel().equals("urn:org.restfulobjects:rels/services")){
                        Log.v("Servicios Encontrados en", restlink.getHref());


                        // Make the HTTP GET request to the Basic Auth protected URL
                        ResponseEntity<Services> response2 = restTemplate.exchange(restlink.getHref(), HttpMethod.GET, requestEntity, Services.class);

                        //return response.getBody();

                        services = response2.getBody();

                        Log.v("leido", services.getValue().size()+"");

                        //Buscar los servicios

                        int arraySize = services.getValue().size();
                        IsisService[] serviceArray = new IsisService[arraySize];
                        for (int i=0; i< arraySize;i++){
                            serviceArray[i] = services.getValue().get(i);
                            Log.v("Servicios Encontrados", serviceArray[i].getTitle());
                        }

                    }

                }

                //return services;

            } catch (Exception e) {
                Log.e("main_activity", e.getMessage(), e);
            }

            return null;
        }
    }

}
