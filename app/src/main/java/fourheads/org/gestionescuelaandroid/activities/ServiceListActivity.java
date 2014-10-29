package fourheads.org.gestionescuelaandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fourheads.org.gestionescuelaandroid.R;
import fourheads.org.gestionescuelaandroid.dom.IsisService;
import fourheads.org.gestionescuelaandroid.dom.RestLink;
import fourheads.org.gestionescuelaandroid.dom.RestLinks;
import fourheads.org.gestionescuelaandroid.dom.Services;


public class ServiceListActivity extends Activity {

    String url;
    String user;
    String pass;
    Services services;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        ListView listview = (ListView) findViewById(R.id.listView_service);

        Intent intent = getIntent();
        url =  intent.getStringExtra("url");
        user =  intent.getStringExtra("user");
        pass =  intent.getStringExtra("pass");

        //Textview Titulo
        TextView title = (TextView) findViewById(R.id.textView_title);
        title.setText( title.getText() + ": " + user);

        //llamar al thread que devuelve una lista de servicios de Isis
        try {
            services = new FillListOfServicesThread().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //JSONService jsonService = new JSONService(url, user, pass);
        List<IsisService> isisServiceList = null;
        final List<String> list = new ArrayList<String>();
        if (services !=null) {
            isisServiceList = services.getValue();

            //tomar los titulos de los servicios

            for (IsisService isisService : isisServiceList) {
                list.add(isisService.getTitle());
            }
        }

        ArrayList<String> excluded = new ArrayList<String>();
        excluded.add("Bulk$ Interaction Context");
        excluded.add("Bookmark Holder Action Contributions");
        excluded.add("Class Discovery Service Using Reflections");
        excluded.add("Clock Service");
        excluded.add("Query Results Cache");
        excluded.add("Scratchpad");
        excluded.add("Bookmark Service Default");
        excluded.add("Memento Service Default");
        excluded.add("Xml Snapshot Service Default");
        excluded.add("Command Context");
        excluded.add("Domain Object Container Default");
        excluded.add("Background Service Default");
        excluded.add("Representation Service For Restful Objects");

        list.removeAll(excluded);

        //llenar la lista
        final StableArrayAdapter adapter = new StableArrayAdapter(getBaseContext(),
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        list.remove(item);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.service_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    private class FillListOfServicesThread extends AsyncTask<Void, Void, Services> {
        @Override
        protected Services doInBackground(Void... params) {
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

                return services;

            } catch (Exception e) {
                Log.e("main_activity", e.getMessage(), e);
            }

            return null;
        }
    }
}
