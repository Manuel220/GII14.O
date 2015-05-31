package es.ubu.mcs0085.botonera;

import android.widget.Button;

/**
 * Created by USUARIO on 29/05/2015.
 */
public class Alarma {

    /**
     * Estado de la Alarma.
     */
    private static Boolean estado;
    /**
     * Variable de la interfaz de la alarma.
     */
    private static Button boton;
    /**
     * Color de la alarma cuando está activada.
     */
    private int colorOn=0xffff0000;
    /**
     * Color de la alarma cuando está desactivada.
     */
    private int colorOff=0xffd3d3d3;

    public Alarma() {
        estado=false;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void cambioEstado(){
        estado=!estado;
    }

    public int getColorOn() {
        return colorOn;
    }

    public int getColorOff() {
        return colorOff;
    }
}
