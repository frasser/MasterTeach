package com.example.frasser.masterteach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Juan Pablo on 24/03/2018.
 */

public class NotificacionesActivity extends AppCompatActivity {


    private TextView informacion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        informacion = findViewById(R.id.texto_info);

        if (getIntent().getExtras() != null){
            for (String key: getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString(key);
                informacion.append("\n" + key + ": " + value);
            }
        }

    }
}
