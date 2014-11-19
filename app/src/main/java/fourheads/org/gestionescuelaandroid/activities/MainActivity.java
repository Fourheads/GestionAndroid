package fourheads.org.gestionescuelaandroid.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpException;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

import fourheads.org.gestionescuelaandroid.R;
import fourheads.org.gestionescuelaandroid.dom.GestionConfig;
import fourheads.org.gestionescuelaandroid.dom.GestionConfigRepositorio;
import fourheads.org.gestionescuelaandroid.dom.IsisService;
import fourheads.org.gestionescuelaandroid.dom.RestLink;
import fourheads.org.gestionescuelaandroid.dom.RestLinks;
import fourheads.org.gestionescuelaandroid.dom.Services;

public class MainActivity extends Activity {

    String url;
    String pass;
    String user;
    String error = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText et_user = (EditText)findViewById(R.id.editText_user);
        final EditText et_pass = (EditText)findViewById(R.id.editText_pass);
        final Button button_connect = (Button)findViewById(R.id.button_connect);
        final CheckBox cb_save = (CheckBox)findViewById(R.id.checkBox_save);
        final CheckBox cb_mostrar = (CheckBox)findViewById(R.id.checkBox_mostrar);
        final Activity activity = this;

        final GestionConfigRepositorio gestionConfigRepositorio = new GestionConfigRepositorio();
        final GestionConfig config = gestionConfigRepositorio.recuperarConfiguracion(this);

        et_user.setText(config.getUser());
        et_pass.setText(config.getPass());
        cb_save.setChecked(config.getSave());

        button_connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                GestionConfig config = gestionConfigRepositorio.recuperarConfiguracion(activity);

                if (et_pass.getText().toString().isEmpty()){

                    mostrarMensaje("El campo \"pass\" no puede quedar en blanco");
                    return;
                }

                if (config.getUrlRestful().isEmpty()){

                    mostrarMensaje("No ha configurado una URL. Hágalo desde el menu \"Settings\"");
                    return;
                }

                config.setUser(et_user.getText().toString());


                if (cb_save.isChecked()){
                    config.setPass(et_pass.getText().toString());
                    config.setSave(true);
                } else {
                    config.setPass("");
                    config.setSave(false);
                }
                Log.v("user",config.getUser());
                Log.v("pass",config.getPass());
                Log.v("save",config.getSave().toString());
                Log.v("url",config.getUrlRestful());

                url = config.getUrlRestful();
                user = config.getUser();
                pass = config.getPass();

                //check connection

                RestLinks restLinks= null;
                try {
                    restLinks = new ContactarRestfulThread().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (restLinks == null){
                    mostrarMensaje(error);
                    return;
                }

                gestionConfigRepositorio.guardarConfiguracion(activity, config);

                Intent intent = new Intent("android.intent.action.SERVICE_LIST");

                intent.putExtra("url", config.getUrlRestful());
                intent.putExtra("user", et_user.getText().toString());
                intent.putExtra("pass", et_pass.getText().toString());

                startActivity(intent);

            }
        });

        cb_mostrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    et_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                if (b) {
                    et_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                Log.v("estado", b +"");

            }

        });

    }

    private void mostrarMensaje(CharSequence text) {
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent("android.intent.action.SETTINGS");
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ContactarRestfulThread extends AsyncTask<Void, Void, RestLinks> {
        @Override
        protected RestLinks doInBackground(Void... params) {
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

                ResponseEntity<RestLinks> response = null;

                response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, RestLinks.class);


                //return response.getBody();

                RestLinks restLinks = response.getBody();

                Log.v("leido", restLinks.getLinks().size()+"");


                return restLinks;

            }  catch (Exception e) {
                if (e.getMessage().contains("401")){
                    error = "Nombre de usuario o contraseña incorrectos.";
                }
                else {
                    error = "No se puede acceder al servidor. Verifique la dirección.";
                }
                Log.e("Error","Error de conexion");

            }
            return null;
        }
    }

}
