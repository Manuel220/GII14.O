package es.ubu.mcs0085.test;

import android.app.Application;
import android.test.ApplicationTestCase;

import es.ubu.mcs0085.botonera.Alarma;
import es.ubu.mcs0085.botonera.Boton;
import es.ubu.mcs0085.botonera.Dispositivos;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test_Boton(){
        Boton botonPrueba= new Boton();
        assertNotNull(botonPrueba);
        assertFalse(botonPrueba.getEstado());
    }

    public void test_Alarma(){
        Alarma alarmaPrueba=new Alarma();
        assertNotNull(alarmaPrueba);
        assertFalse(alarmaPrueba.getEstado());
    }

    public void test_Dispositivos(){
        Dispositivos dispositivos= new Dispositivos(1,2,3);
        for (Boton i : dispositivos.getInterruptores()) assertFalse(i.getEstado());
        for (Boton i : dispositivos.getPulsadores()) assertFalse(i.getEstado());
        for (Alarma i : dispositivos.getAlarmas()) assertFalse(i.getEstado());
        assertEquals(1,dispositivos.getInterruptores().size());
        assertEquals(2,dispositivos.getPulsadores().size());
        assertEquals(3,dispositivos.getAlarmas().size());
    }

    public void test_BitsNoUsados(){
        int tamanoByte = 8;
        int tamano;
        tamano = (6) / tamanoByte;
        tamano = (tamano + 1) * tamanoByte;
        tamano = tamano - 6;

        assertEquals(tamano,2);
    }
}