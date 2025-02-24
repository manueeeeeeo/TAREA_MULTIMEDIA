package com.clase.engenios_ortega_manuel_prc6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorRecycler extends RecyclerView.Adapter<AdaptadorRecycler.MyViewHolder> {
    private List<RecursosContent.Recurso> dataList = new ArrayList<>(); // Variable para manejar la lista de datos del JSON
    private Context context = null; // Variable para manejar el contexto de la app
    private List<RecursosContent.Recurso> filteredList = new ArrayList<>(); // Variable para manejar la lista de datos filtrada

    /**
     * @param context
     * @param dataList
     * Constructor del adaptador del recyclerview al que
     * le paso como parametros la lista de datos, el contexto,
     * inicializo esas dos variables y además, inicializo
     * la lista de filtradas que en caso de que la lista de datos
     * sea nula inicializo un arraylist limpio*/
    public AdaptadorRecycler(List<RecursosContent.Recurso> dataList, Context context) {
        // Inicializo la lista de datos con el parametro que le paso
        this.dataList = dataList;
        // Inicializo el contexto con el parametro que le paso
        this.context = context;
        // Inicializo la lista de filtradas con la lista de datos y en caso de que este vacia inicializo como un arraylist
        this.filteredList = new ArrayList<>(dataList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecursosContent.Recurso item = filteredList.get(position);
        // Establezco al item su nombre
        holder.titleTextView.setText(item.nombre);
        // Establezco al item su descripcion
        holder.subtitleTextView.setText(item.descripcion);
        // Establezco al item su imagen
        holder.imageView.setImageBitmap(item.imagen);

        // Establezco el ícono según el tipo de recurso
        switch (item.tipo) {
            case "0": // En caso de que sea de tipo de audio
                // Establezco el icono de audio
                holder.typeIconImageView.setImageResource(R.drawable.audio);
                break;
            case "1": // En caso de que sea de tipo de video
                // Establezco el icono de video
                holder.typeIconImageView.setImageResource(R.drawable.video);
                break;
            case "2": // En caso de que sea de tipo de streaming
                // Establezco el icono de streaming
                holder.typeIconImageView.setImageResource(R.drawable.streaming);
                break;
        }

        // Le doy funcionalidad al botón para reproducir cada archivo
        holder.playButton.setOnClickListener(view -> {
            // Compruebo que tipo de archivo es
            if (item.tipo.equals("0")) { // En caso de que sea de tipo 0 --> audio
                // Obtengo en una variable con la clase auxiliar de RecursosAudio el audio a reproducir
                int resourceId = RecursosAudio.getAudioResourceId(context, item.uri);
                // Compruebo que todo haya salido bien
                if (resourceId != -1) { // En caso de que sea diferente de -1
                    ((MainActivity) context).playAudio(resourceId);
                } else { // En caso de que sea -1
                    // Lanzo un Toast indicando que ese recurso no existe
                    Toast.makeText(context, "Este recurso no existe!!", Toast.LENGTH_LONG).show();
                }
            } else if (item.tipo.equals("1")) { // En caso de que sea de tipo 1 --> video
                // Antes de iniciar el video, detengo y libero el MediaPlayer si está reproduciendo algo
                if (((MainActivity) context).mediaPlayer != null) {
                    ((MainActivity) context).mediaPlayer.stop();
                    ((MainActivity) context).mediaPlayer.release();
                    ((MainActivity) context).mediaPlayer = null;
                }

                // Creo un uri en donde estableceré el uri con la ruta para ejecutar el video
                Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + item.uri);
                // Procedo a crear un nuevo intent para pasar a la actividad de video
                Intent intent = new Intent(view.getContext(), VideoActivity.class);
                // Establezco como putextra el uri del video a reproducir
                intent.putExtra("uri", uri.toString());
                // Lanzo el nuevo intent
                view.getContext().startActivity(intent);
            } else if (item.tipo.equals("2")) { // En caso de que sea de tipo 2 --> streamig
                // Antes de iniciar el video, detengo y libero el MediaPlayer si está reproduciendo algo
                if (((MainActivity) context).mediaPlayer != null) {
                    ((MainActivity) context).mediaPlayer.stop();
                    ((MainActivity) context).mediaPlayer.release();
                    ((MainActivity) context).mediaPlayer = null;
                }

                // Procedo a crear un nuevo intent para pasar a la actividad de streaming
                Intent intent = new Intent(view.getContext(), StreamingActivity.class);
                // Establezco como putextra el uri del video streaming para reproducir
                intent.putExtra("uri", item.uri);
                // Lanzo el nuevo intent
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Devuelvo el tamaño de la lista filtrada
        return filteredList.size();
    }

    /**
     * Método en el que primero que todo obtengo las preferencias
     * del usuario y a partir de ellas voy filtrando la lista, comprobando
     * el tipo de archivo de cada recurso y si en las preferencias
     * hemos establecido que se vea ese tipo de archivo*/
    public void filterResources() {
        // Creo una variable y la inicializo para las preferencias del usuario
        SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        // Obtengo en un booleano si lo guardado en preferencias es true o false
        boolean showAudio = preferences.getBoolean("filter_audio", true);
        // Obtengo en un booleano si lo guardado en preferencias es true o false
        boolean showVideo = preferences.getBoolean("filter_video", true);
        // Obtengo en un booleano si lo guardado en preferencias es true o false
        boolean showStreaming = preferences.getBoolean("filter_streaming", true);

        // Limpio la lista de filtradas antes de proceder con el filtrado por si acaso
        filteredList.clear();
        // Utilizo un bucle for de la clase recursor para ir recorriendo la lista de los datos
        for (RecursosContent.Recurso recurso : dataList) {
            // Voy comprobando si el tipo de uno determinado y la variable booleana de las preferencias es true
            if ((recurso.tipo.equalsIgnoreCase("0") && showAudio==true) ||
                    (recurso.tipo.equalsIgnoreCase("1") && showVideo==true) ||
                    (recurso.tipo.equalsIgnoreCase("2") && showStreaming==true)) { // De ser así
                // Agrego el recurso a la lista filtrada
                filteredList.add(recurso);
            }
        }

        // Notifico los cambios al adaptador
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView subtitleTextView;
        ImageButton playButton;
        ImageView typeIconImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            subtitleTextView = itemView.findViewById(R.id.subtitleTextView);
            playButton = itemView.findViewById(R.id.playButton);
            typeIconImageView = itemView.findViewById(R.id.typeIconImageView);
        }
    }
}