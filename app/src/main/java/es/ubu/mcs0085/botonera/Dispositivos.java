package es.ubu.mcs0085.botonera;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que agrupa todos los interruptores, pulsadores y alarmas que controla la aplicación.
 */
public class Dispositivos {
    public List<Boton> interruptores;
    public List<Boton> pulsadores;
    public List<Alarma> alarmas;

    public Dispositivos(int inter, int pulsa, int alar) {
        interruptores = new ArrayList<Boton>(inter);
        pulsadores = new ArrayList<Boton>(pulsa);
        alarmas = new ArrayList<Alarma>(alar);

        for (int i = 0; i < inter; i++) interruptores.add(new Boton());
        for (int i = 0; i < pulsa; i++) pulsadores.add(new Boton());
        for (int i = 0; i < alar; i++) alarmas.add(new Alarma());
    }

    public List<Boton> getInterruptores() {
        return interruptores;
    }

    public Boton getInterruptor(int i){
        return interruptores.get(i);
    }

    public Boton getPulsador(int i){
        return pulsadores.get(i);
    }

    public Alarma getAlarma(int i){
        return alarmas.get(i);
    }

    public List<Boton> getPulsadores() {
        return pulsadores;
    }

    public List<Alarma> getAlarmas() {
        return alarmas;
    }
}
