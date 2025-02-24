package com.clase.engenios_ortega_manuel_prc6;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    private RecyclerView recyclerView = null; // Variable para manejar el recyclerView de la actividad
    private AdaptadorRecycler adaptador = null; // Variable para manejar el adaptador del recycler
    public MediaPlayer mediaPlayer = null; // Variable para el reproductor de audio
    private MediaController mediaController = null; // Variable para el controlador de la reproducción de audio
    private Toolbar toolbar = null; // Variable para menjar el toolbar de la aplicación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Obtengo el elemento de la interfaz
        toolbar = findViewById(R.id.toolbar);
        // Le establezco como actionbar
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtengo el recyclerView de la interfaz
        recyclerView = findViewById(R.id.recycler);
        // Le establezco el layout manager para manejar como se va a ver
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargo los recursos y datos del JSON
        RecursosContent.loadRecursosFromJSON(this);
        // Creo una lisa con los recursos donde la inicializo con los items del JSON
        List<RecursosContent.Recurso> recursosList = RecursosContent.getITEMS();

        // Inicializo un nuevo adaptador pasandole la lista y el contexto
        adaptador = new AdaptadorRecycler(recursosList, this);
        // Aplico el filtro de archivos desde el principio
        adaptador.filterResources();
        // Establezco el adaptador al recyclerView
        recyclerView.setAdapter(adaptador);

        // Procedo a inicializar el media controller y lo anclo a la vista principal
        mediaController = new MediaController(this);
        mediaController.setAnchorView(findViewById(R.id.main));
        // Asigno a esta actividad como la controladora del controlar de audio
        mediaController.setMediaPlayer(this);
    }

    /**
     * @param audioResourceId
     * Método al que llamo para empezar a reproducir un archivo
     * de audio, lo que hago es liberar el mediaplayer
     * anterior por si tiene algún dato ya, creo un nuevo
     * mediaplayer iniciandole y pasandole el id del recurso de audio
     * y procedo a iniciar la reproducción de sonido junto con el anclado
     * del mediacontroller a la pantalla del main*/
    public void playAudio(int audioResourceId) {
        // Compruebo si el media player es diferente de nulo
        if (mediaPlayer != null) { // De ser así
            mediaPlayer.stop();
            mediaPlayer.release(); // Libero el MediaPlayer anterior
        }
        // Creo un nuevo MediaPlayer con el recurso de audio
        mediaPlayer = MediaPlayer.create(this, audioResourceId);
        // Inicio la reproducción de sonido
        mediaPlayer.start();
        // Muestro el MediaController indefinidamente
        mediaController.show(0);

        // Mantenengo el MediaController visible siempre
        keepMediaControllerVisible();
    }

    /**
     * Método en el que lo que hago es utilizar un handler para
     * cada 1000 milisegundos, es decir, cada 1 segundo actualizar el mediacontroller
     * en caso de que desaparezca para que así el media controller se quede
     * en la pantalla de forma indefinida y no surjan errores del tipo de que se oculta y
     * no se puede volver a mostrar*/
    private void keepMediaControllerVisible() {
        // Uso un Handler para mantener el MediaController visible
        new android.os.Handler().postDelayed(() -> {
            // Compruebo si el media controller es diferente de nulo
            if (mediaController != null && mediaPlayer != null) {
                // Solo si el audio está reproduciéndose o está pausado
                if (mediaPlayer.isPlaying() || isPaused()) {
                    mediaController.show(0); // Muestro el media controller indefinidamente
                    keepMediaControllerVisible(); // Llamo recursivamente al método
                } else {
                    hideMediaController(); // Oculto el MediaController si no hay audio reproduciéndose ni pausado
                }
            }
        }, 1000); // Lo actualizo cada segundo
    }

    /**
     * Método para comprobar si el el media controller no es nulo, en caso
     * de no ser nulo y llamar a este método procedemos a ocultar el
     * media controller*/
    public void hideMediaController(){
        // Compruebo que el media controller no sea nulo
        if(mediaController!=null){ // En caso de no ser nulo
            // Oculto el media controller
            mediaController.hide();
        }
    }

    @Override
    public void start() {
        if (mediaPlayer != null) {
            // Inicio la reproducción del MediaPlayer
            mediaPlayer.start();
            mediaController.show(0);
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            // Pauso la reproducción del MediaPlayer
            mediaPlayer.pause();
            mediaController.show(0);
        }
    }

    /**
     * @return
     * Método que devuelve un booleano basandonos
     * en las respuestas de si el media player es distinto de nulo
     * y el media player no se está reproduciendo*/
    private boolean isPaused() {
        return mediaPlayer != null && !mediaPlayer.isPlaying();
    }

    @Override
    public int getDuration() {
        // Devuelvo la duración del MediaPlayer o 0 si no está inicializado
        return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
    }

    @Override
    public int getCurrentPosition() {
        // Devuelvo la posición actual del MediaPlayer o 0 si no está inicializado
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public void seekTo(int pos) {
        if (mediaPlayer != null) {
            // Salto a una posición específica en el MediaPlayer
            mediaPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        // Indico si el MediaPlayer está reproduciendo audio
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        // No lo uso
        return 0;
    }

    @Override
    public boolean canPause() {
        // Indico que el MediaPlayer puede pausarse
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        // Indico que el MediaPlayer puede retroceder
        return true;
    }

    @Override
    public boolean canSeekForward() {
        // Indico que el MediaPlayer puede avanzar
        return true;
    }

    @Override
    public int getAudioSessionId() {
        // Devuelvo el ID de la sesión de audio o 0 si no está inicializado
        return mediaPlayer != null ? mediaPlayer.getAudioSessionId() : 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            // Libero el MediaPlayer para evitar fugas de memoria
            mediaPlayer.release();
            // Asigno null al MediaPlayer
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aplico el filtro cuando la pantalla se reanuda
        adaptador.filterResources();

        // Procedo a comprobar si el mediaPlayer es nulo o no se está ejecutando
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) { // En caso de cumplir alguna de estas condiciones
            // Llamo al método para ocultar el media controller
            hideMediaController();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}