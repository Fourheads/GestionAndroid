package fourheads.org.gestionescuelaandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fourheads.org.gestionescuelaandroid.R;


public class MainActivity_old extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Captura del evento Click del boton
        final Button button = (Button)findViewById(R.id.button_connect);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
/*
            final EditText editTextUrl = (EditText)findViewById(R.id.editText_server);
            final EditText editTextUser = (EditText)findViewById(R.id.editText_user_name);
            final EditText editTextPass = (EditText)findViewById(R.id.editText_password);

            String url = editTextUrl.getText().toString();
            String user = editTextUser.getText().toString();
            String pass = editTextPass.getText().toString();

            Intent intent = new Intent("android.intent.action.SERVICE_LIST");

            intent.putExtra("url", url);
            intent.putExtra("user", user);
            intent.putExtra("pass", pass);

            startActivity(intent);*/
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


}
