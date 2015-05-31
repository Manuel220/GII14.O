package es.ubu.mcs0085.botonera;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USUARIO on 29/05/2015.
 */
public class Dispositivos {
    List<Boton> interruptores;
    List<Boton> pulsadores;
    List<Alarma> alarmas;

    public Dispositivos(int inter, int pulsa, int alar) {
        interruptores = new ArrayList<Boton>(inter);
        pulsadores = new ArrayList<Boton>(pulsa);
        alarmas = new ArrayList<Alarma>(alar);

        for (Boton i : interruptores) i = new Boton();

        for (Boton i : pulsadores) i = new Boton();

        for (Alarma i : alarmas) i = new Alarma();
    }

    public List<Boton> getInterruptores() {
        return interruptores;
    }

    public List<Boton> getPulsadores() {
        return pulsadores;
    }

    public List<Alarma> getAlarmas() {
        return alarmas;
    }
}
