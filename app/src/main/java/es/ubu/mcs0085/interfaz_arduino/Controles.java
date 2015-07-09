package es.ubu.mcs0085.interfaz_arduino;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.telephony.SmsManager;
import android.content.BroadcastReceiver;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import es.ubu.mcs0085.botonera.Dispositivos;

/**
 * Clase que controla la actividad principal de la Aplicación.
 */
public class Controles extends Activity implements View.OnClickListener, View.OnTouchListener {
    /**
     * Guarda el número de teléfono de la placa Arduino con la que se comunica.
     */
    private static String NUMBER;
    /**
     * Número de interruptores.
     */
    private static int INTERRUPTORES;
    /**
     * Número de pulsadores.
     */
    private static int PULSADORES;
    /**
     * Número de alarmas.
     */
    private static int ALARMAS;
    /**
     * Variable que guarda las instancias de los dispositivos.
     */
    private static Dispositivos dispositivos;
    /**
     * Instancia de la clase que recive los SMS y atualiza la aplicación.
     */
    private Actualizar actualizador = new Actualizar();
    /**
     * Variable de la interfaz que va recibiendo los botones.
     */
    private ViewGroup layout;
    /**
     * Variable que guarda el TextView que despliega la información de los mensajes confirmados.
     */
    public TextView correcto;

    /**
     * Está clase se encarga de actualizar la aplicación mediante mensajes SMS que recibe.
     */
    public static class Actualizar extends BroadcastReceiver {
        /**
         * Este método se activa cuando el movil recibe SMS, MMS, llamadas telefónicas, etc...
         *
         * @param context Es el contexto del actividad.
         * @param intent  Es el objeto que recibe.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            SmsMessage[] msgs;
            String message = "";
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    message = msgs[i].getMessageBody();
                }
            }
            protocoloME(message);
        }

        /**
         * Este método traduce el código el Protoclo ME para actualizar el estado de la aplicación.
         *
         * @param message Mensaje de que recibe.
         */
        public void protocoloME(String message) {
            if (comprobarMensaje(message)) {
                for (int i = primerDispositivo(message), j = 0; i > ultimoDispositivo(message); i--, j++) {
                    dispositivos.actualizarDispositivo(toBoolean(message.charAt(i)), j);
                }
            }
        }

        /**
         * Convierte el estado del dispositivo de char a boolean.
         *
         * @param estado El estado en char.
         * @return El estado en boolean.
         */
        private boolean toBoolean(char estado) {
            return estado == '1';
        }

        /**
         * Nos da el índice del estado del último dispositivo.
         *
         * @param message Mensaje con los estados recibido.
         * @return El índice del ultimo dispositivo.
         */
        private int ultimoDispositivo(String message) {
            return message.length() - 2 - dispositivos.getTamano();
        }

        /**
         * Nos da el índice del estado del primer dispositivo.
         *
         * @param message Mensaje con los estados recibido.
         * @return El índice del primer dispositivo.
         */
        private int primerDispositivo(String message) {
            return message.length() - 2;
        }

        /**
         * Comprueba si el mensaje ha recibido el formato adecuado.
         *
         * @param message Mensaje con los estados recibido.
         * @return Devuelve si el mensaje es correcto.
         */
        private boolean comprobarMensaje(String message) {
            return message.charAt(message.length() - 1) == '1';
        }

    }

    /**
     * Método que se ejecuta al lanzar la aplicación.
     *
     * @param savedInstanceState Estado de la aplicación.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controles);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        correcto = (TextView) findViewById(R.id.correcto);
        iniciarVariables();
        generarLayout();
        crearDispositivos();
    }

    /**
     * Método que inicia las variables leyendo los datos del archivo de configuración.
     */
    private void iniciarVariables() {
        InputStream flujo = null;
        BufferedReader lector;
        try {
            flujo = getResources().openRawResource(R.raw.configuracion);
            lector = new BufferedReader(new InputStreamReader(flujo));
            NUMBER = String.valueOf(iniciarVariable(lector));
            INTERRUPTORES = iniciarVariable(lector);
            PULSADORES = iniciarVariable(lector);
            ALARMAS = iniciarVariable(lector);
        } catch (Exception ex) {
            Log.e("IniciarVariables", "Error al leer fichero desde recurso raw");
            NUMBER = null;
            INTERRUPTORES = 0;
            PULSADORES = 0;
            ALARMAS = 0;
        } finally {
            try {
                if (flujo != null)
                    flujo.close();
            } catch (IOException e) {
                Log.e("IniciarVariables", "Error al cerrar flujo de lectura.");
            }
        }
    }

    /**
     * Método que lee una variable del fichero de configuración, lee dos lineas ya que el fichero
     * de configuración contiene primero el nombre de la variable y despuús su valor.
     *
     * @param lector Es el buffer para leer el fichero.
     * @return Devuelve el valor de la variable transformado a integer.
     */
    private int iniciarVariable(BufferedReader lector) {
        try {
            String texto = lector.readLine();
            texto = lector.readLine();
            return Integer.parseInt(texto);
        } catch (IOException e) {
            return 0;
        }

    }

    /**
     * Este método genera dinamicamente los interruptores, pulsadores y alarmas de la interfaz.
     */
    private void generarLayout() {
        layout = (ViewGroup) findViewById(R.id.contenido);
        ponerTexto("Interruptores", layout);
        for (int i = 0; i < INTERRUPTORES; i++) {
            generarDispositivo(i, cogerNombre(i, "Interruptor"));
        }
        ponerTexto("Pulsadores", layout);
        for (int i = INTERRUPTORES; i < INTERRUPTORES + PULSADORES; i++) {
            generarDispositivo(i, cogerNombre(i, "Pulsador"));
        }
        ponerTexto("Alarmas", layout);
        for (int i = INTERRUPTORES + PULSADORES; i < INTERRUPTORES + PULSADORES + ALARMAS; i++) {
            generarDispositivo(i, cogerNombre(i, "Alarma"));
        }
    }

    private String cogerNombre(int N, String alternativa) {
        InputStream flujo = null;
        BufferedReader lector;
        String nombre = "";
        try {
            flujo = getResources().openRawResource(R.raw.nombres);
            lector = new BufferedReader(new InputStreamReader(flujo));
            for (int i = 0; i < N + 2; i++) {
                nombre = lector.readLine();
            }
        } catch (Exception ex) {
            Log.e("CogerNombre", "Error al leer fichero desde recurso raw");
            nombre = alternativa;
        } finally {
            try {
                if (flujo != null)
                    flujo.close();
            } catch (IOException e) {
                Log.e("CogerNombre", "Error al cerrar flujo de lectura.");
            }
        }
        if (nombre == null) {
            nombre = alternativa;
        }
        return nombre;
    }

    /**
     * Genera el dispositivo y lo introduce en el layout.
     *
     * @param i     Id del dispositivo.
     * @param texto Texto que se introduce en el dispositivo.
     */
    private void generarDispositivo(int i, String texto) {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        Button boton = new Button(this);
        boton.setId(i);
        boton.setText(texto);
        boton.setWidth(width);
        boton.setHeight(height);
        layout.addView(boton);
        añadirEspacio();
    }

    /**
     * Introduce un texto entre los tipos de dispositivo.
     *
     * @param texto  Texto a introducir.
     * @param layout Layout donde se introduce.
     */
    private void ponerTexto(String texto, ViewGroup layout) {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        TextView titulo = new TextView(this);
        titulo.setWidth(width);
        titulo.setHeight(height);
        titulo.setText(texto);
        titulo.setTextColor(0xffffffff);
        titulo.setGravity(Gravity.CENTER);
        layout.addView(titulo);
    }

    /**
     * Este método añade un espacio entre los distintos elemento del layout.
     */
    private void añadirEspacio() {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        TextView espacio = new TextView(this);
        espacio.setWidth(width);
        espacio.setGravity(Gravity.CENTER);
        layout.addView(espacio);
    }

    /**
     * Este método crea los dispositivos que se van a utilizar.
     */
    private void crearDispositivos() {
        dispositivos = new Dispositivos(INTERRUPTORES, PULSADORES, ALARMAS);

        for (int i = 0; i < INTERRUPTORES; i++) {
            dispositivos.getInterruptor(i).setButton((Button) findViewById(i));
            dispositivos.getInterruptor(i).getButton().setOnClickListener(this);
        }

        for (int i = 0, j = INTERRUPTORES; i < PULSADORES; i++, j++) {
            dispositivos.getPulsador(i).setButton((Button) findViewById(j));
            dispositivos.getPulsador(i).getButton().setOnTouchListener(this);
        }

        for (int i = 0, j = INTERRUPTORES + PULSADORES; i < ALARMAS; i++, j++) {
            dispositivos.getAlarma(i).setAlarma((TextView) findViewById(j));
        }
    }

    /**
     * Este método se ejecuta cuando la aplicación se pone en funcionamiento.
     */
    @Override
    public void onStart() {
        super.onStart();
        actualizador = new Actualizar();
        IntentFilter myFiler = new IntentFilter();
        myFiler.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(actualizador, myFiler);
    }

    /**
     * Este método se ejecuta cuando la aplicación se detiene.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (null != actualizador) {
            this.unregisterReceiver(actualizador);
        }
    }

    /**
     * Este método se encarga de realizar las acciones en función del interruptor que ha sido pulsado.
     *
     * @param v Es el botonIA que ha sido pulsado
     */
    @Override
    public void onClick(View v) {
        for (int i = 0; i < INTERRUPTORES; i++) {
            if (compararIdInterruptor(v, i)) {
                cambioEstadoInterruptor(i);
            }
        }
    }

    /**
     * Compara el identificador del View con el interruptor en la posición i.
     *
     * @param v View que se compara.
     * @param i Posición del interruptor que se compara.
     * @return Devuelve si coinciden en un parámetro booleano.
     */
    private boolean compararIdInterruptor(View v, int i) {
        return dispositivos.getInterruptor(i).getButton().getId() == v.getId();
    }

    /**
     * Cambia el estado del interruptor cuyo índice es introducido por parámetro y manda un SMS para comunicar el cambio de estado.
     *
     * @param i Índice del interruptor.
     */
    private void cambioEstadoInterruptor(int i) {
        dispositivos.getInterruptor(i).cambioEstado();
        mandarSMS();
    }

    /**
     * En método detecta cuando uno de los pulsadores es pulsado y cuando deja de estar pulsado.
     *
     * @param v Contiene que pulsador es con el que se ha interactuado.
     * @param m Contiene la acción sobre el pulsador.
     * @return devuelve si se han realizado correctamente la pulsación.
     */
    @Override
    public boolean onTouch(View v, MotionEvent m) {
        for (int i = 0; i < PULSADORES; i++) {
            if (compararIdPulsador(v, i)) {
                if (compararIdAccionUp(m, i)) {
                    cambioEstadoPulsador(i);
                }
                if (compararIdAccionDown(m, i)) {
                    cambioEstadoPulsador(i);
                }
            }
        }
        return true;
    }

    /**
     * Compara el identificador del View con el pulsador en la posición i.
     *
     * @param v View que se compara.
     * @param i Posición del pulsador que se compara.
     * @return Devuelve si coinciden en un parámetro booleano.
     */
    private boolean compararIdPulsador(View v, int i) {
        return dispositivos.getPulsador(i).getButton().getId() == v.getId();
    }

    /**
     * Comprueba si se presiona un pulsador y si su estado esta desactivado.
     *
     * @param m Evento realizado.
     * @param i Posición del pulsador a comprobar el estado.
     * @return Devuelve si la comprobación es positva o no en un booleano.
     */
    private boolean compararIdAccionDown(MotionEvent m, int i) {
        return m.getAction() == MotionEvent.ACTION_DOWN && !dispositivos.getPulsador(i).getEstado();
    }

    /**
     * Comprueba si se despresiona un pulsador y si su estado esta activado.
     *
     * @param m Evento realizado.
     * @param i Posición del pulsador a comprobar el estado.
     * @return Devuelve si la comprobación es positva o no en un booleano.
     */
    private boolean compararIdAccionUp(MotionEvent m, int i) {
        return m.getAction() == MotionEvent.ACTION_UP && dispositivos.getPulsador(i).getEstado();
    }

    /**
     * Cambia el estado del pulsador cuyo índice es introducido por parámetro y manda un SMS para comunicar el cambio de estado.
     *
     * @param i Índice del pulsador.
     */
    private void cambioEstadoPulsador(int i) {
        dispositivos.getPulsador(i).cambioEstado();
        mandarSMS();
    }

    /**
     * Método que se comunica con el Arduino mediante SMS.
     */
    protected void mandarSMS() {
        SmsManager sms = SmsManager.getDefault();
        String message = estadoAplicacion();
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent("SMS_DELIVERED"), 0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        correcto.setText("Estado consistente");
                        correcto.setBackgroundColor(0xff00ff00);
                        break;
                    case Activity.RESULT_CANCELED:
                        correcto.setText("Estado no cosistente");
                        correcto.setBackgroundColor(0xffff0000);
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED"));

        if (NUMBER != null) {
            sms.sendTextMessage(NUMBER, null, message, null, deliveredPI);
        }
    }

    /**
     * Crea el mensaje a mandar al Arduino.
     *
     * @return Devuelve el mensaje a enviar.
     */
    private String estadoAplicacion() {
        String message = "1";
        int tamano = calculoTamanoMensaje();

        for (int i = 0; i < INTERRUPTORES; i++) {
            if (dispositivos.getInterruptor(i).getEstado()) {
                message = "1" + message;
            } else {
                message = "0" + message;
            }
        }

        for (int i = 0; i < PULSADORES; i++) {
            if (dispositivos.getPulsador(i).getEstado()) {
                message = "1" + message;
            } else {
                message = "0" + message;
            }
        }

        for (int i = 0; i < ALARMAS; i++) {
            if (dispositivos.getAlarma(i).getEstado()) {
                message = "1" + message;
            } else {
                message = "0" + message;
            }
        }

        for (int i = 0; i < tamano; i++) {
            message = "0" + message;
        }

        return message;
    }

    /**
     * Calcula el número de bits que no se utilizan, para completar el mensaje a enviar según el Protocolo ME.
     *
     * @return Número de bits no utilizados.
     */
    private int calculoTamanoMensaje() {
        int tamanoByte = 8;
        int tamano;
        tamano = (dispositivos.getTamano() + 1) / tamanoByte;
        tamano = (tamano + 1) * tamanoByte;
        tamano = tamano - dispositivos.getTamano() - 1;

        return tamano;
    }

    /**
     * Crea el menu de la aplicación.
     *
     * @param menu Es el menu de la pantalla Controles.
     * @return Devuelve si se ha creado correctamente el menu de opciones.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_controles, menu);
        return true;
    }

    /**
     * Metodo que lleva a acabo las acciones de las distintas opciones del menu.
     *
     * @param item Es la opción del menu seleccionado.
     * @return Realiza la acción de la opción del menu seleccionado.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salir:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}