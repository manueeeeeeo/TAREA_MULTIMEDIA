package com.clase.engenios_ortega_manuel_prc6;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class StreamingActivity extends AppCompatActivity {
    private ExoPlayer player = null; // Variable para manejar el reproductor de exoplayer
    private PlayerView playerView = null; // Variable para manejar la vista del reproductor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        // Obtengo la URI del video a reproducir que le paso con el Intent
        String uriString = getIntent().getStringExtra("uri");
        // Compruebo que la uri no este vacia
        if (uriString == null || uriString.isEmpty()) { // En caso de que esté vacia
            // Cierro la actividad
            finish();
            // Retornamos
            return;
        }

        // Obtengo el reproductor de la interfaz de la actividad
        playerView = findViewById(R.id.player_view);
        // Creo una nueva instancia de ExoPlayer con build
        player = new ExoPlayer.Builder(this).build();
        // Asigno el reproductor con el elemento visual
        playerView.setPlayer(player);

        // Procedo a convertir la URI recibida a un objeto de tipo URI
        Uri uri = Uri.parse(uriString);
        // Creo un MediaItem pasandole la uri para que ExoPlayer lo reproduzca
        MediaItem mediaItem = MediaItem.fromUri(uri);
        // Establezco el MediaItem al reproductor de la interfaz
        player.setMediaItem(mediaItem);

        // Preparo el reproductor para que se pueda ejecutar
        player.prepare();
        // Inicio la reproducción del video del uri
        player.play();
    }

    /**
     * Método que se ejecuta en el caso
     * de que la actividad sea destruida por completo*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Compruebo si en player tiene algo guardado
        if (player != null) { // De ser así
            // Librero recursos
            player.release();
        }
    }
}