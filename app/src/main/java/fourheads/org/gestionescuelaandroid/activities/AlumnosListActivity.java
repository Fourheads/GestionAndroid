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

import com.fasterxml.jackson.databind.DeserializationFeature;

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

import fourheads.org.gestionescuelaandroid.R;
import fourheads.org.gestionescuelaandroid.dom.Alumno;
import fourheads.org.gestionescuelaandroid.dom.Alumnos;
import fourheads.org.gestionescuelaandroid.dom.IsisService;
import fourheads.org.gestionescuelaandroid.dom.RestLink;
import fourheads.org.gestionescuelaandroid.dom.RestLinks;
import fourheads.org.gestionescuelaandroid.dom.Services;

public class AlumnosListActivity extends Activity {

    String url;
    String user;
    String pass;
    Alumnos alumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        ListView listview = (ListView) findViewById(R.id.listView_students);

        Intent intent = getIntent();
        url =  intent.getStringExtra("url")+ "services/alumno/actions/listAll/invoke";
        user =  intent.getStringExtra("user");
        pass =  intent.getStringExtra("pass");

        try {
            alumnos = new FillListOfAlumnosThread().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<RestLink> LinksAlumnosList = null;
        final List<String> listNombres = new ArrayList<String>();
        if (alumnos !=null) {
            LinksAlumnosList = alumnos.getResult().getValue();

            //tomar nombres de los alumnos

            for (RestLink alumnoLink : LinksAlumnosList) {
                listNombres.add(alumnoLink.getTitle());
            }
        }

        //llenar la lista
        final StableArrayAdapter adapter = new StableArrayAdapter(getBaseContext(),
                android.R.layout.simple_list_item_1, listNombres);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, final long id) {
                final String item = (String) parent.getItemAtPosition(position);

                Log.v("nombre",alumnos.getResult().getValue().get(position).getTitle());
                Log.v("link",alumnos.getResult().getValue().get(position).getHref());

                String urlAlumno = alumnos.getResult().getValue().get(position).getHref();

                Intent newIntent = new Intent("android.intent.action.DATOS_ALUMNO");
                newIntent.putExtra("user",user);
                newIntent.putExtra("pass",pass);
                newIntent.putExtra("url",urlAlumno);

                startActivity(newIntent);


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_students_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FillListOfAlumnosThread extends AsyncTask<Void, Void, Alumnos> {
        @Override
        protected Alumnos doInBackground(Void... params) {
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

                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                restTemplate.getMessageConverters().add(converter);

                // Make the HTTP GET request to the Basic Auth protected URL
                ResponseEntity<Alumnos> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Alumnos.class);

                Alumnos alumnos = response.getBody();

                Log.v("listado Alumnos contiene", alumnos.getResult().getValue().size() +"");

                // Tomar nombres de los alumnos

                int arraySize = alumnos.getResult().getValue().size();
                RestLink[] alumnosArray = new RestLink[arraySize];
                for (int i=0; i< arraySize;i++){
                    alumnosArray[i] = alumnos.getResult().getValue().get(i);
                    Log.v("Alumno Encontrado", alumnosArray[i].getTitle());
                }


                return alumnos;

            } catch (Exception e) {
                Log.e("main_activity", e.getMessage(), e);
            }

            return null;
        }
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
}
