package com.clase.engenios_ortega_manuel_prc6;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Cargo el fragmento de preferencias dentro del contenedor de la actividad
        getSupportFragmentManager()
                .beginTransaction() // Inicio la transacción de fragmentos
                .replace(R.id.settings_container, new SettingsFragment()) // Cargo el fragmento de ajustes de preferencias en el contenedor
                .commit(); // Confirmo la transacción
    }

    /**
     * Clase interna que representa el fragmento de preferencias, amdemás
     * en ella manejo las configuraciones de la aplicación
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // Cargo las preferencias desde el archivo XML de configuración
            setPreferencesFromResource(R.xml.settings_preferences, rootKey);

            // Obtengo una instancia de SharedPreferences para acceder a las preferencias guardadas
            SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
            // Procedo a verificar los valores de todos los switch del fragmento
            boolean showAudio = preferences.getBoolean("filter_audio", true);
            boolean showVideo = preferences.getBoolean("filter_video", true);
            boolean showStreaming = preferences.getBoolean("filter_streaming", true);
        }
    }
}