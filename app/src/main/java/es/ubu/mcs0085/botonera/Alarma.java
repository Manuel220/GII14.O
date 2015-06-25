package es.ubu.mcs0085.botonera;

import android.widget.TextView;

/**
 * Clase que simula una alarma en la aplicación.
 */
public class Alarma {

    /**
     * Estado de la Alarma.
     */
    private Boolean estado;
    /**
     * Variable de la interfaz de la alarma.
     */
    private TextView alarma;
    /**
     * Color de la alarma cuando está activada.
     */
    private int colorOn=0xffff0000;
    /**
     * Color de la alarma cuando está desactivada.
     */
    private int colorOff=0xffd3d3d3;

    /**
     * Constructor de la Alarma.
     */
    public Alarma() {
        estado=false;
    }

    /**
     * Devuelve el estado de la Alarma.
     *
     * @return el esato de la Alarma.
     */
    public Boolean getEstado() {
        return estado;
    }

    /**
     * Devuelve el widget con el que esta enlazado.
     *
     * @return el TextView con el que enlaza.
     */
    public TextView getAlarma(){
        return alarma;
    }

    /**
     * Enlaza el widget a la clase.
     *
     * @param i TextView con el que enlaza.
     */
    public void setAlarma(TextView i){
        this.alarma=i;
        alarma.setBackgroundColor(getColorOff());
    }

    /**
     * Cambia el estado del botón así como su color de fondo.
     */
    public void cambioEstado(){
        estado=!estado;
        if(estado){
            alarma.setBackgroundColor(getColorOn());
        }
        else{
            alarma.setBackgroundColor(getColorOff());
        }
    }

    /**
     * Devuelve el color de la alarma cuando está activada.
     *
     * @return El color en formato Integer.
     */
    public int getColorOn(){
        return colorOn;
    }

    /**
     * Devuelve el color de la alarma cuando está desactivada.
     *
     * @return El color en formato Integer.
     */
    public int getColorOff(){
        return colorOff;
    }
}
