package es.ubu.mcs0085.botonera;

import android.widget.Button;

/**
 * Clase que simula un boton, ya sea interruptor o pulsador en la aplicación.
 */
public class Boton {
    /**
     * Estado del Botón.
     */
    private Boolean estado;
    /**
     * Variable de la interfaz del botón.
     */
    private Button boton;
    /**
     * Color del botón cuando esta activo.
     */
    private int colorOn=0xff00ff00;
    /**
     * Color del botón cuando esta desactivado.
     */
    private int colorOff=0xffd3d3d3;

    /**
     * Constructor de la clase.
     */
    public Boton(){
        estado=false;
    }

    /**
     * Devuelve el estado del botón.
     *
     * @return el estado del botón.
     */
    public Boolean getEstado() {
        return estado;
    }

    /**
     * Cambia el estado del botón así como su color de fondo.
     */
    public void cambioEstado(){
        estado=!estado;
        if(estado){
            boton.setBackgroundColor(getColorOn());
        }
        else{
            boton.setBackgroundColor(getColorOff());
        }
    }

    /**
     * Devuelve el widget Button de la clase.
     *
     * @return Button de la clase.
     */
    public Button getButton(){
        return boton;
    }

    /**
     * Enlaza el widget a la clase.
     *
     * @param i Button con el que enlaza.
     */
    public void setButton(Button i){
        this.boton=i;
    }

    /**
     * Devuelve el color del botón cuando está activado.
     *
     * @return El color en formato Integer.
     */
    public int getColorOn(){
        return colorOn;
    }

    /**
     * Devuelve el color del botón cuando está desactivado.
     *
     * @return El color en formato Integer.
     */
    public int getColorOff(){
        return colorOff;
    }

}
