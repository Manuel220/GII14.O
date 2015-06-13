package es.ubu.mcs0085.botonera;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que agrupa todos los interruptores, pulsadores y alarmas que controla la aplicación.
 */
public class Dispositivos {
    /**
     * Lista de interruptores de la aplicación.
     */
    private List<Boton> interruptores;
    /**
     * Lista de pulsadores de la aplicación.
     */
    private List<Boton> pulsadores;
    /**
     * Lista de alarmas de la aplicación.
     */
    private List<Alarma> alarmas;
    /**
     * Número de dispositivos que controla la aplicación.
     */
    private int tamano;

    /**
     * Constructor de la clase.
     *
     * @param interuptores Número de interruptores.
     * @param pulsadores Número de pulsadores.
     * @param alarmas Número de alarmas.
     */
    public Dispositivos(int interuptores, int pulsadores, int alarmas) {
        interruptores = new ArrayList<>(interuptores);
        this.pulsadores = new ArrayList<>(pulsadores);
        this.alarmas = new ArrayList<>(alarmas);
        tamano=interuptores+pulsadores+alarmas;

        for (int i = 0; i < interuptores; i++) interruptores.add(new Boton());
        for (int i = 0; i < pulsadores; i++) this.pulsadores.add(new Boton());
        for (int i = 0; i < alarmas; i++) this.alarmas.add(new Alarma());
    }

    /**
     * Devuelve la lista que guarda los interruptores.
     *
     * @return la lista de interruptores.
     */
    public List<Boton> getInterruptores() {
        return interruptores;
    }

    /**
     * Devuelve la lista que guarda los pulsadores.
     *
     * @return la lista de pulsadores.
     */
    public List<Boton> getPulsadores() {
        return pulsadores;
    }

    /**
     * Devuelve la lista que guarda las alarmas.
     *
     * @return la lista de alarmas.
     */
    public List<Alarma> getAlarmas() {
        return alarmas;
    }

    /**
     * Devuelve el interruptor cuyo índice se pasa como parámtero.
     *
     * @param i índice del interruptor.
     * @return el interruptor buscado.
     */
    public Boton getInterruptor(int i){
        return interruptores.get(i);
    }

    /**
     * Devuelve el pulsador cuyo índice se pasa como parámatero.
     *
     * @param i índice del pulsador.
     * @return el pulsador buscado.
     */
    public Boton getPulsador(int i){
        return pulsadores.get(i);
    }

    /**
     * Devuelve la alarma cuyo índice se pasa como parámatero.
     *
     * @param i índice de la alarma.
     * @return la alarma buscada.
     */
    public Alarma getAlarma(int i){
        return alarmas.get(i);
    }

    /**
     * Devuelve el número de dispositivos que guarda la clase.
     *
     * @return número de dispositivos.
     */
    public int getTamano(){
        return tamano;
    }
}
