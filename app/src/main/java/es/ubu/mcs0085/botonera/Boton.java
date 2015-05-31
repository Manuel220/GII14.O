package es.ubu.mcs0085.botonera;

import android.widget.Button;

/**
 * Created by USUARIO on 29/05/2015.
 */
public class Boton {
    /**
     * Estado del Botón.
     */
    private static Boolean estado;
    /**
     * Variable de la interfaz del botón.
     */
    private static Button boton;

    /**
     * Color del botón cuando esta activo.
     */
    private int colorOn=0xff00ff00;
    /**
     * Color del botón cuando esta desactivado.
     */
    private int colorOff=0xffd3d3d3;

    public Boton(){
        estado=false;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void cambioEstado(){
        estado=!estado;
    }

    public Button getButton(){
        return boton;
    }

    public int getColorOn(){
        return colorOn;
    }

    public int getColorOff(){
        return colorOff;
    }

}
