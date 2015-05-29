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

        boton1 = (Button) findViewById(R.id.interruptor_a);
        boton1.setOnClickListener(this);

        boton2 = (Button) findViewById(R.id.interruptor_b);
        boton2.setOnClickListener(this);

        boton3 = (Button) findViewById(R.id.pulsador_a);
        boton3.setOnTouchListener(this);

        boton4 = (Button) findViewById(R.id.pulsador_b);
        boton4.setOnTouchListener(this);
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
     * @param v Es el boton que ha sido pulsado
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.interruptor_a: {
                if (uno) {
                    boton1.setBackgroundColor(0xff00ff00);
                    uno = false;
                    sendSMS("InterruptorA On");
                } else {
                    boton1.setBackgroundColor(0xffd3d3d3);
                    uno = true;
                    sendSMS("InterruptorA Off");
                }
                break;
            }
            case R.id.interruptor_b: {
                if (dos) {
                    boton2.setBackgroundColor(0xff00ff00);
                    dos = false;
                    sendSMS("InterruptorB On");
                } else {
                    boton2.setBackgroundColor(0xffd3d3d3);
                    dos = true;
                    sendSMS("InterruptorB Off");
                }
                break;
            }
            default: {
            }
        }
    }

    /**
     * En método detecta cuando uno de los pulsadores es pulsado y cuando deja de estar pulsado.
     *
     * @param v Contiene que pulsador es con el que se ha interactuado.
     * @param m Contiene la acción sobre el pulsador.
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent m) {
        switch (v.getId()) {
            case (R.id.pulsador_a): {
                if (m.getAction() == MotionEvent.ACTION_UP) {
                    boton3.setBackgroundColor(0xffd3d3d3);
                    sendSMS("PulsadorA Off");
                }
                if (m.getAction() == MotionEvent.ACTION_DOWN) {
                    boton3.setBackgroundColor(0xff00ff00);
                    sendSMS("PulsadorA On");
                }
                break;
            }
            case R.id.pulsador_b: {
                if (m.getAction() == MotionEvent.ACTION_UP) {
                    boton4.setBackgroundColor(0xffd3d3d3);
                    sendSMS("PulsadorB Off");
                }
                if (m.getAction() == MotionEvent.ACTION_DOWN) {
                    boton4.setBackgroundColor(0xff00ff00);
                    sendSMS("PulsadorB On");
                }
                break;
            }
        }
        return true;
    }

    /**
     * Método que se comunica con el Arduino mediante SMS.
     *
     * @param message Mensaje a enviar.
     */
    private void sendSMS(String message) {
        SmsManager sms = SmsManager.getDefault();
       // if(NUMBER!="") {
            sms.sendTextMessage(NUMBER, null, message, null, null);
       // }
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