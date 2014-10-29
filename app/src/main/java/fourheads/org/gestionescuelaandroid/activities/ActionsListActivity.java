package fourheads.org.gestionescuelaandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import fourheads.org.gestionescuelaandroid.R;

public class ActionsListActivity extends Activity {

    String serviceurl;
    String serviceTitle;
    String user;
    String pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_list);

        Intent intent = getIntent();
        serviceurl =  intent.getStringExtra("serviceUrl");
        serviceTitle = intent.getStringExtra("serviceTitle");
        user =  intent.getStringExtra("user");
        pass =  intent.getStringExtra("pass");

        //Textview Titulo
        TextView title = (TextView) findViewById(R.id.actionlist_title);
        title.setText( title.getText() + ": " + user + ": " + serviceTitle);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions_list, menu);
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
