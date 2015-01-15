package com.example.alejandro.practica2pspchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import chatterBot.ChatterBot;
import chatterBot.ChatterBotFactory;
import chatterBot.ChatterBotSession;
import chatterBot.ChatterBotType;


public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {
    private TextView etEnviar;
    private TextView tvConversacion;
    private String respuesta, digo;
    private boolean reproductor = false;
    static final int CTE_LEE = 1;
    static final int CTE_RECONOCE = 2;
    private TextToSpeech tts;
    private final int AJUSTES = 3;
    private LinearLayout lyConversacion;
    private ProgressDialog dialogo;
    private String lenguaje="espanol";
    private float tono,velocidad=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEnviar = (TextView) findViewById(R.id.etEnviar);
        tvConversacion = (TextView) findViewById(R.id.tvConversacion);

        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, CTE_LEE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            ajustes();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            reproductor = true;
            if (lenguaje.equalsIgnoreCase("Ingles")) {
                tts.setLanguage(Locale.US);
            } else {
                Locale loc = new Locale("spa", "ESP");
                tts.setLanguage(loc);//Idioma
            }
            tts.setPitch(tono);//Tono
            tts.setSpeechRate(velocidad);//Velocidad*/
        } else {
            //no se puede reproducir
        }
        Log.v("¿Funciona?", reproductor + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CTE_LEE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, this);//contexto y listener
            } else {
                Intent intent = new Intent();
                intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(intent);
            }
        }
        if (requestCode == CTE_RECONOCE && resultCode == RESULT_OK) {
            ArrayList<String> textos = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            etEnviar.setText(textos.get(0));
            for (int i = 0; i < textos.size(); i++) {
                Log.v("textos:", textos.get(i));
            }
        }
        if (resultCode == RESULT_OK && requestCode == AJUSTES) {
            Bundle dsc = data.getExtras();
            lenguaje = dsc.getString("lenguaje");
            velocidad = dsc.getFloat("velocidad");
            tono = dsc.getFloat("tono");
            Log.v("tono",tono+"");
            Log.v("velocidad",velocidad+"");
        }
    }

    @Override
    protected void onStart() {
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, CTE_LEE);
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onStop();
    }


    public void enviar(View v) {
        digo = etEnviar.getText().toString();
        etEnviar.setText("");
        lyConversacion = (LinearLayout) findViewById(R.id.lyConversacion);
        lyConversacion.setVisibility(View.VISIBLE);
        tvConversacion.append("Tú dices: " + digo);
        tvConversacion.append("\n");

        etEnviar.setEnabled(false);
        HiloFacil hf = new HiloFacil();
        hf.execute();

    }


    public void escribir(View v) {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora");
        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
        startActivityForResult(i, CTE_RECONOCE);
    }


    class HiloFacil extends AsyncTask<Object, Integer, String> {

        HiloFacil(String... p) {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogo = new ProgressDialog(MainActivity.this);
            dialogo.setMessage("Pensando");
            dialogo.show();

        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                ChatterBotFactory factory = new ChatterBotFactory();
                ChatterBot bot1 = factory.create(ChatterBotType.CLEVERBOT);
                ChatterBotSession bot1session = bot1.createSession();
                respuesta = bot1session.think(digo);
            } catch (Exception e) {
                Log.v("LOG: ", e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvConversacion.append("ChatterBot dice: " + respuesta);
            tvConversacion.append("\n");
            Log.v("respuesta", respuesta);
            etEnviar.setEnabled(true);
            if (reproductor == true) {
                tts.speak(respuesta, TextToSpeech.QUEUE_FLUSH, null);
            }

            dialogo.dismiss();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
    }

    public void ajustes() {
        Intent i = new Intent(this, Ajustes.class);
        Bundle b = new Bundle();

        startActivityForResult(i, AJUSTES);
    }


}
