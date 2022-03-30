package com.example.frasser.masterteach.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frasser.masterteach.LoginActivity;
import com.example.frasser.masterteach.PerfilActivity;
import com.example.frasser.masterteach.R;
import com.example.frasser.masterteach.RegistroActivity;
import com.example.frasser.masterteach.dominio.IPerfilLogic;
import com.example.frasser.masterteach.dominio.PerfilLogic;
import com.example.frasser.masterteach.dto.TemaPrincipalDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Juan Pablo on 11/03/2018.
 */

public class EditarPerfilFragments extends Fragment {

    static public EditText informacionProfesional;

    static public EditText extra;
    static public EditText price;
    private Spinner tema;
    static public ImageView fotoPerfil;
    private TextView nombre;
    private ImageButton camara;

    private Button guardar;



static  public  String temaTrata;
    private List<TemaPrincipalDTO> listTema;

    Uri uri;

    //////////////////////////////////////////

    private  static final int REQUEST_CODE,SELECT_PICTURE;
    Bitmap fotoEstudiantes;
    static {
        REQUEST_CODE = 1888;
        SELECT_PICTURE = 200;
    }

    //////////////////////////////////////

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_perfil,container,false);

        tema =(Spinner) view.findViewById(R.id.spn_tematica_perfil);
        informacionProfesional = view.findViewById(R.id.texto_plano);
        extra = view.findViewById(R.id.edt_experiencia);
        price = view.findViewById(R.id.edt_precio_fijado);
        fotoPerfil = (ImageView) view.findViewById(R.id.imagen_foto);
        nombre = view.findViewById(R.id.txt_nombres_perfil);
        camara = (ImageButton) view.findViewById(R.id.btn_camara);

        guardar = (Button) view.findViewById(R.id.btn_guardarCambios);




        nombre.setText(LoginActivity.nomUsuario);

        if (nombre.getText().equals("")){
            nombre.setText(RegistroActivity.nombreCreado);
        }
        cargarTemaPrincipal();





        fotoPerfil.setImageBitmap(PerfilActivity.fotoPro);
        informacionProfesional.setText(PerfilActivity.perfilUsuario.getText().toString());
        extra.setText(PerfilActivity.experienciaUsuario.getText().toString());
        price.setText(PerfilActivity.precioUsuario.getText().toString());
        //tema.setSelection(Integer.parseInt(PerfilActivity.temaUsuario.toString()));







        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] options = {"Tomar foto","Elegir de galeria","Cancelar"};
                final AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                builder.setTitle("Elige una opcion :");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int seleccion) {

                        if (options[seleccion] == "Tomar foto"){

                            try {
                                Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camaraIntent, REQUEST_CODE);
                                //permite invocar un activity por medio de un intent y un valor
                                // este sobre escribe un metodo o invoca el metodo onActivityResult la cual se encuentra el metodo siguiente

                            }catch (Exception e){
                                Log.e("ERROR","ERROR ABRIENDO CAMARA "+e.getMessage());

                            }

                        }else if (options[seleccion] == "Elegir de galeria"){

                            try {
                                Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                startActivityForResult(Intent.createChooser(galeriaIntent,"SELECT IMAGEN FROM GALERY"),SELECT_PICTURE);


                            }catch (Exception e){

                                Log.e("ERROR","ERROR ABRIENDO GALERIA "+e.getMessage());
                            }
                        }else if (options[seleccion] == "Cancelar"){
                            dialogInterface.dismiss();
                        }

                    }

                });
                builder.show();



            }

        });



        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    String A= tema.getSelectedItem().toString();
                    temaTrata = A;




                    IPerfilLogic iPerfilLogic = new PerfilLogic();
                    if (RegistroActivity.cedulaCreada != null){
                        iPerfilLogic.crearPerfil(Long.valueOf(RegistroActivity.cedulaCreada),
                                nombre.getText().toString(),
                                informacionProfesional.getText().toString(),

                                extra.getText().toString(),
                                price.getText().toString(),
                                temaTrata,
                                fotoEstudiantes);

                    }else {
                        iPerfilLogic.crearPerfil(Long.valueOf(LoginActivity.ced),
                                nombre.getText().toString(),
                                informacionProfesional.getText().toString(),

                                extra.getText().toString(),
                                price.getText().toString(),
                                temaTrata,
                                fotoEstudiantes);

                    }





                    PerfilActivity.nombreUsuario.setText(nombre.getText().toString());
                    PerfilActivity.perfilUsuario.setText(informacionProfesional.getText().toString());
                    PerfilActivity.experienciaUsuario.setText(extra.getText().toString());
                    PerfilActivity.precioUsuario.setText(price.getText().toString());
                    PerfilActivity.temaUsuario.setText(temaTrata);
                    //////////// PARA CONVERTIR LA IMAGEN EN  REDONDA////////////////////////////

                    Bitmap original = ((Bitmap) fotoEstudiantes);

                    RoundedBitmapDrawable roundedDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), original);
                    roundedDrawable.setCornerRadius(original.getHeight());

                    ///////////////////////////////////////

                    PerfilActivity.foto.setImageDrawable(roundedDrawable);

                    mostrarMensaje("Perfil Completado con exito");
                    getActivity().onBackPressed();





                }catch (Exception e){
                    mostrarMensaje(e.getMessage());
                    //limpiar();
                }
            }
        });



        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == REQUEST_CODE && data != null && data.getExtras().get("data") != null){
            fotoEstudiantes = (Bitmap) data.getExtras().get("data");
            Bitmap original = ((Bitmap) fotoEstudiantes);



            fotoPerfil.setImageBitmap(original);

        }

        if (requestCode == SELECT_PICTURE ) {

            if (resultCode == RESULT_OK){
                Uri urlImagen = data.getData();
               // fotoEstudiantes = BitmapFactory.decodeFileDescriptor(urlImagen);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver() ,urlImagen);
                    //Uri.parse(String.valueOf(urlImagen) tambien funciona con este fragmento de codigo
                    fotoEstudiantes = bitmap;
                    //Bitmap ori = ((Bitmap)fotoEstudiantes);


                    Bitmap original = ((Bitmap) fotoEstudiantes);

                    RoundedBitmapDrawable roundedDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), original);
                    roundedDrawable.setCornerRadius(original.getHeight());

                    fotoPerfil.setImageDrawable(roundedDrawable);

                } catch (IOException e) {
                    e.printStackTrace();
                }


               // fotoPerfil.setImageURI(urlImagen);
/*
                // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                // Ponemos nuestro bitmap en un ImageView que tengamos en la vista
                ImageView mImg = (ImageView) findViewById(R.id.ivImagen);
                mImg.setImageBitmap(bmp);
*/

            }




    }

    }
    private void cargarSpinnerTema() {
        ArrayAdapter<TemaPrincipalDTO> adapterTema = new ArrayAdapter<TemaPrincipalDTO>(this.getContext(),android.R.layout.simple_spinner_item,listTema);
        adapterTema.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tema.setAdapter(adapterTema);

        tema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Error",tema.getSelectedItem().toString());
                tema.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void cargarTemaPrincipal() {
        try {
            listTema = new ArrayList<TemaPrincipalDTO>();

            //CONSULTANDO TODOS LOS TEMAS
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference("temaprincipal");

            ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("Info","FIRE BASE DETECTO CAMBIOS");
                    listTema.clear();

                    for (DataSnapshot iterario : dataSnapshot.getChildren()){
                        TemaPrincipalDTO principalDTO = iterario.getValue(TemaPrincipalDTO.class);
                        listTema.add(principalDTO);
                    }
                    cargarSpinnerTema();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            Log.e("ERROR","ERROR------> ",e);
        }
    }


    public void mostrarMensaje(String mensaje){
        Toast toast = Toast.makeText(this.getContext(),mensaje,Toast.LENGTH_LONG);
        toast.show();
    }
    public void limpiar(){
        informacionProfesional.setText("");
        extra.setText("");
        price.setText("");


    }


}
