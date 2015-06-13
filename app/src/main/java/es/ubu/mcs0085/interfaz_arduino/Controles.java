package es.ubu.mcs0085.interfaz_arduino;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.telephony.SmsManager;
import android.content.BroadcastReceiver;
import android.widget.TextView;

import es.ubu.mcs0085.botonera.Dispositivos;

/**
 * Clase que controla la actividad principal de la Aplicación.
 */
public class Controles extends Activity implements View.OnClickListener, View.OnTouchListener {
    /**
     * Estado del interruptor A.
     */
    private static Boolean uno = true;
    /**
     * Estado del interruptor B.
     */
    private static Boolean dos = true;
    /**
     * Variables del interruptor A.
     */
    private static Button boton1;
    /**
     * Variable del interruptor B.
     */
    private static Button boton2;
    /**
     * Variable del pulsador A.
     */
    private static Button boton3;
    /**
     * Variable del pulsador B.
     */
    private static Button boton4;
    /**
     * Guarda el número de teléfono de la placa Arduino con la que se comunica.
     */
    private static final String NUMBER = "686600465";
    /**
     * Número de interruptores.
     */
    private static final int INTERRUPTORES=2;
    /**
     * Número de pulsadores.
     */
    private static final int PULSADORES=2;
    /**
     * Número de alarmas.
     */
    private static final int ALARMAS=1;
    /**
     * Variable que guarda las instancias de los dispositivos.
     */
    private Dispositivos dispositivos;
    /**
     * Instancia de la clase que recive los SMS y atualiza la aplicación.
     */
    private Actualizar actualizador = new Actualizar();

    /**
     * Está clase se encarga de actualizar la aplicación mediante mensajes SMS que recibe.
     */
    static public class Actualizar extends BroadcastReceiver {
        /**
         * Este método se activa cuando el movil recibe SMS, MMS, llamadas telefónicas, etc...
         *
         * @param context Es el contexto del actividad.
         * @param intent Es el objeto que recibe.
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
                    message = msgs[i].getMessageBody().toString();
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
            if (message.charAt(message.length() - 1) == '1') {
                if (message.charAt(message.length() - 2) == '1') {
                    uno = false;
                    boton1.setBackgroundColor(0xff00ff00);
                } else {
                    uno = true;
                    boton1.setBackgroundColor(0xffd3d3d3);
                }
                if (message.charAt(message.length() - 3) == '1') {
                    dos = false;
                    boton2.setBackgroundColor(0xff00ff00);
                } else {
                    dos = true;
                    boton2.setBackgroundColor(0xffd3d3d3);
                }
                if (message.charAt(message.length() - 4) == '1') {
                    boton3.setBackgroundColor(0xff00ff00);
                } else {
                    boton3.setBackgroundColor(0xffd3d3d3);
                }
                if (message.charAt(message.length()- 5) == '1') {
                    boton4.setBackgroundColor(0xff00ff00);
                } else {
                    boton4.setBackgroundColor(0xffd3d3d3);
                }
            }
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

        dispositivos=new Dispositivos(INTERRUPTORES,PULSADORES,ALARMAS);

        dispositivos.getInterruptor(0).setButton((Button) findViewById(R.id.interruptor_a));
        dispositivos.getInterruptor(1).setButton((Button) findViewById(R.id.interruptor_b));
        dispositivos.getPulsador(0).setButton((Button) findViewById(R.id.pulsador_a));
        dispositivos.getPulsador(1).setButton((Button) findViewById(R.id.pulsador_b));
        dispositivos.getAlarma(0).setAlarma((TextView) findViewById(R.id.alarma_a));

        for (int i=0;i<INTERRUPTORES;i++){
            dispositivos.getInterruptor(i).getButton().setOnClickListener(this);
        }

        for (int i=0; i<PULSADORES;i++){
            dispositivos.getPulsador(i).getButton().setOnTouchListener(this);
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
        for (int i=0;i<INTERRUPTORES;i++){
            if(compararId(v, i)){
                cambioEstadoInterruptor(i);
            }
        }
    }

    private boolean compararId(View v, int i) {
        return dispositivos.getInterruptor(i).getButton().getId()==v.getId();
    }

    private void cambioEstadoInterruptor(int i) {
        dispositivos.getInterruptor(i).cambioEstado();
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
        for(int i=0;i<PULSADORES;i++){
            if(dispositivos.getPulsador(i).getButton().getId()==v.getId()){
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

    private boolean compararIdAccionDown(MotionEvent m, int i) {
        return m.getAction() == MotionEvent.ACTION_DOWN && !dispositivos.getPulsador(i).getEstado();
    }

    private boolean compararIdAccionUp(MotionEvent m, int i) {
        return m.getAction() == MotionEvent.ACTION_UP && dispositivos.getPulsador(i).getEstado();
    }

    private void cambioEstadoPulsador(int i) {
        dispositivos.getPulsador(i).cambioEstado();
    }

    /**
     * Método que se comunica con el Arduino mediante SMS.
     *
     * @param message Mensaje a enviar.
     */
    private void sendSMS(String message) {
        SmsManager sms = SmsManager.getDefault();
        if(NUMBER!="") {
            //sms.sendTextMessage(NUMBER, null, message, null, null);
        }
    }

    /**
     * Crea el menu de la aplicación.
     *
     * @param menu Es el menu de la pantalla Controles.
     * @return
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