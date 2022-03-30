package com.example.frasser.masterteach.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.frasser.masterteach.R;
import com.example.frasser.masterteach.dto.FormaPagoDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Pablo on 27/02/2018.
 */

public class RegistroFragments extends android.support.v4.app.Fragment{

    static public Spinner pago;
    private List<FormaPagoDTO> listPago;

    static public int tipopa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro,container,false);

        pago = (Spinner) view.findViewById(R.id.spn_tipo_pago);

        cargarFormaPago();





        return view;
    }
    public void cargarSpinner() {

        ArrayAdapter<FormaPagoDTO> adapterPago = new ArrayAdapter<FormaPagoDTO>(this.getContext(),android.R.layout.simple_spinner_item,listPago);

        adapterPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pago.setAdapter(adapterPago);

        pago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Error",pago.getSelectedItem().toString());
                pago.setSelection(i);

                tipopa = (int) l + 1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
    }

    public void cargarFormaPago() {

        try {
            listPago = new ArrayList<FormaPagoDTO>();

            //CONSULTANDO LOS TIPOS DE PAGO
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference("formapago");

            ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("Info","FIRE BASE DETECTO CAMBIOS");
                    listPago.clear();

                    for (DataSnapshot iterado : dataSnapshot.getChildren()){
                        FormaPagoDTO formaPago = iterado.getValue(FormaPagoDTO.class);
                        listPago.add(formaPago);
                    }
                    cargarSpinner();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }catch (Exception e){
            Log.e("ERROR","ERROR--------> ",e);
        }
    }


}
