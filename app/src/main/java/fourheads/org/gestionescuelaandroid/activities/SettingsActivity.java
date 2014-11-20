package fourheads.org.gestionescuelaandroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fourheads.org.gestionescuelaandroid.R;
import fourheads.org.gestionescuelaandroid.dom.GestionConfig;
import fourheads.org.gestionescuelaandroid.dom.GestionConfigRepositorio;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText editTextServidor = (EditText)findViewById(R.id.editText_settings_servidor);
        final EditText editTextPuerto = (EditText)findViewById(R.id.editText_settings_puerto);
        final EditText editTextDirectorio = (EditText)findViewById(R.id.editText_settings_directorio);
        Button buttonSave = (Button)findViewById(R.id.button_guardar);
        final Activity activity = this;

        final GestionConfigRepositorio gestionConfigRepositorio = new GestionConfigRepositorio();
        final GestionConfig config = gestionConfigRepositorio.recuperarConfiguracion(activity);

        editTextServidor.setText(config.getServidor());
        editTextPuerto.setText(config.getPuerto());
        editTextDirectorio.setText(config.getDirectorio());

        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final GestionConfig config = gestionConfigRepositorio.recuperarConfiguracion(activity);
                config.setServidor(editTextServidor.getText().toString());
                config.setPuerto(editTextPuerto.getText().toString());
                config.setDirectorio(editTextDirectorio.getText().toString());
                gestionConfigRepositorio.guardarConfiguracion(activity, config);

                finish();
            }
        });

        Button buttonAyuda = (Button)findViewById(R.id.button_ayuda);

        buttonAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.AYUDA");
                startActivity(intent);
            }
        });

    }

}
