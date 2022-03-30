package com.example.frasser.masterteach.utilidades;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Juan Pablo on 2/03/2018.
 */

@SuppressLint("ValidFragment")
public class MensajeDialog extends DialogFragment {


    private  String mensajeMostrar;
    private  Class claseNavegar;
    private Activity activityInvoca;

    @SuppressLint("ValidFragment") // se utiliza para suprimir warnings
    public MensajeDialog(String mensaje) {
        this.mensajeMostrar = mensajeMostrar;
    }

    @SuppressLint("ValidFragment")
    public MensajeDialog(String mensaje, Activity activityInvoca, Class claseNavegar ) {
        this.mensajeMostrar = mensaje;
        this.claseNavegar = claseNavegar;
        this.activityInvoca = activityInvoca;
    }
    public MensajeDialog(){

    }

    @Override
    public Dialog onCreateDialog(Bundle instance) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mensajeMostrar);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentInicio = new Intent();
                intentInicio.setClass(activityInvoca,claseNavegar);
                startActivity(intentInicio);
                activityInvoca.finish();
                getDialog().dismiss();

            }
        });
        return builder.create();
    }
}
