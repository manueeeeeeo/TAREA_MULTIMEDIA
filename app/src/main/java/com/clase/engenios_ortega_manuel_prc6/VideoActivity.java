package com.clase.engenios_ortega_manuel_prc6;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class VideoActivity extends AppCompatActivity {
    private ExoPlayer player = null; // Variable para manejar el reproductor de exoplayer
    private PlayerView playerView = null; // Variable para manejar la vista del reproductor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Obtengo el reproductor de la interfaz de la actividad
        playerView = findViewById(R.id.player_view);

        // Obtengo la URI del video a reproducir que le paso con el Intent
        String uriString = getIntent().getStringExtra("uri");
        // Procedo a convertir la URI recibida a un objeto de tipo URI
        Uri uri = Uri.parse(uriString);

        // Creo una nueva instancia de ExoPlayer con build
        player = new ExoPlayer.Builder(this).build();
        // Asigno el reproductor con el elemento visual
        playerView.setPlayer(player);

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
     * Método que se ejecuta cuando se
     * destruye por completo está activdad*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Comprobamos si el player es distinto de nulo, lo que significará que se está reproduciendo algo
        if (player != null) { // De ser así
            // Librero recursos
            player.release();
            // Establecemos el player como nulo
            player = null;
        }
    }
}