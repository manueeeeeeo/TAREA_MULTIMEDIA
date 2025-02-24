package com.clase.engenios_ortega_manuel_prc6;

import android.content.Context;

public class RecursosAudio {
    /**
     * @return
     * @param context
     * @param resourceName
     * Método en el que lo que hago es pasarle como parametros el contexto
     * y el nombre del recurso a utilizar*/
    public static int getAudioResourceId(Context context, String resourceName) {
        // Utilizo un switch para manejar todas las posibles opciones basandome en el resourceName
        switch (resourceName) {
            // En caso de que el valor sea eltiempopasara
            case "eltiempopasara":
                // Retorno el id del audio de la carpeta de raw establecido
                return R.raw.eltiempopasara;
            // En caso de que el valor sea entersandman
            case "entersandman":
                // Retorno el id del audio de la carpeta de raw establecido
                return R.raw.entersandman;
            // En caso de que sea otro valor que no controlo
            default:
                // Retorno -1 dando lugar a que ese recurso no existe
                return -1;
        }
    }
}