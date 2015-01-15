package com.example.alejandro.practica2pspchat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class Ajustes extends ActionBarActivity {
    private String idioma;
    private TextView tvVelocidad,tvTono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        final Spinner spIdioma;

        spIdioma = (Spinner)findViewById(R.id.spinnerIdioma);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.idioma, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIdioma.setAdapter(adapter);
        spIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                idioma= parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        SeekBar sbVelocidad;
        tvVelocidad = (TextView)findViewById(R.id.tvVelocidad);
        sbVelocidad = (SeekBar)findViewById(R.id.seekBarVelocidad);
        sbVelocidad.setProgress(5);
        sbVelocidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float progress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                //progress = progresValue;
                progress = ((float)progresValue / 5);
                tvVelocidad.setText(progress + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        SeekBar sbTono;
        tvTono = (TextView)findViewById(R.id.tvTono);
        sbTono = (SeekBar)findViewById(R.id.seekBarTono);
        sbTono.setProgress(5);
        sbTono.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float progress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = ((float)progresValue / 5);
                //progress = progresValue;
                tvTono.setText(progress + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }


        });

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajustes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void aceptar(View v){
        Intent result = new Intent();
        result.putExtra("lenguaje", idioma);
        result.putExtra("velocidad", Float.parseFloat(tvVelocidad.getText().toString()));
        result.putExtra("tono", Float.parseFloat(tvTono.getText().toString()));
        setResult(Activity.RESULT_OK, result);
        finish();
        this.finish();
    }
}
