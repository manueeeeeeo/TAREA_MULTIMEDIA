package com.clase.engenios_ortega_manuel_prc6;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecursosContent {
    public static List<Recurso> ITEMS = new ArrayList<>();

    public static void loadRecursosFromJSON(Context c) {
        String json = null;
        try {
            InputStream is = c.getAssets().open("recursosList.json");
            int size = is.available();
            if (size == 0) {
                Toast.makeText(c, "El JSON está vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray recursosList = jsonObject.getJSONArray("recursos_list");
            for (int i = 0; i < recursosList.length(); i++) {
                JSONObject jsonRecurso = recursosList.getJSONObject(i);
                String nombre = jsonRecurso.getString("nombre");
                String descripcion = jsonRecurso.getString("descripcion");
                String tipo = jsonRecurso.getString("tipo");
                String uri = jsonRecurso.getString("URI");
                Bitmap imagen = null;
                try {
                    imagen = BitmapFactory.decodeStream(
                            c.getAssets().open("images/" + jsonRecurso.getString("imagen")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ITEMS.add(new Recurso(nombre, descripcion, tipo, uri, imagen));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Recurso> getITEMS() {
        return ITEMS;
    }

    public static class Recurso implements Parcelable {
        public String nombre;
        public String descripcion;
        public String tipo;
        public String uri;
        public Bitmap imagen;

        public Recurso(String nombre, String descripcion, String tipo, String uri, Bitmap imagen) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.tipo = tipo;
            this.uri = uri;
            this.imagen = imagen;
        }

        @Override
        public String toString() {
            return nombre + " " + descripcion;
        }

        protected Recurso(Parcel in) {
            nombre = in.readString();
            descripcion = in.readString();
            tipo = in.readString();
            uri = in.readString();
            imagen = in.readParcelable(Bitmap.class.getClassLoader());
        }

        public static final Creator<Recurso> CREATOR = new Creator<Recurso>() {
            @Override
            public Recurso createFromParcel(Parcel in) {
                return new Recurso(in);
            }

            @Override
            public Recurso[] newArray(int size) {
                return new Recurso[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(nombre);
            dest.writeString(descripcion);
            dest.writeString(tipo);
            dest.writeString(uri);
            dest.writeParcelable(imagen, flags);
        }
    }
}