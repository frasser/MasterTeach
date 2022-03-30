package com.example.frasser.masterteach;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.frasser.masterteach.R;
import com.example.frasser.masterteach.RegistroActivity;
import com.example.frasser.masterteach.dto.ModalidadDTO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Pablo on 27/02/2018.
 */

public class AgendarActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private Spinner modalidad;
    private ImageView fotografia;
    private ImageButton camara;
    private ImageButton locacion;
    private EditText telefono;
    private Button solicitar;

    private GoogleMap mapa;
    private LatLng miPosicion;
    private ProgressDialog miProgress;

    private List<ModalidadDTO> listModalidad;

    //////////////////////////////////////////

    private static final int REQUEST_CODE, SELECT_PICTURE;
    Bitmap fotoEstudiantes;

    static {
        REQUEST_CODE = 1888;
        SELECT_PICTURE = 200;
    }

    //////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar);
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapa);
        supportMapFragment.getMapAsync(this);
        //getSupportActionBar().hide();


        modalidad = (Spinner) findViewById(R.id.spn_modalidad);
        fotografia = findViewById(R.id.imagen_foto_estudiante);
        camara = findViewById(R.id.btn_foto);
        locacion = findViewById(R.id.btn_ubicacion);
        telefono = findViewById(R.id.edt_telefono);
        solicitar = findViewById(R.id.button5);
        miProgress = new ProgressDialog(this);

        cargarModalidad();
        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Tomar foto", "Elegir de galeria", "Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Elige una opcion :");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int seleccion) {

                        if (options[seleccion] == "Tomar foto") {

                            try {
                                Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camaraIntent, REQUEST_CODE);
                                //permite invocar un activity por medio de un intent y un valor
                                // este sobre escribe un metodo o invoca el metodo onActivityResult la cual se encuentra el metodo siguiente

                            } catch (Exception e) {
                                Log.e("ERROR", "ERROR ABRIENDO CAMARA " + e.getMessage());

                            }

                        } else if (options[seleccion] == "Elegir de galeria") {

                            try {
                                Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                startActivityForResult(Intent.createChooser(galeriaIntent, "SELECT IMAGEN FROM GALERY"), SELECT_PICTURE);


                            } catch (Exception e) {

                                Log.e("ERROR", "ERROR ABRIENDO GALERIA " + e.getMessage());
                            }
                        } else if (options[seleccion] == "Cancelar") {
                            dialogInterface.dismiss();
                        }

                    }

                });
                builder.show();
            }
        });

        solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.setClass(AgendarActivity.this,NotificacionesActivity.class);
                    startActivity(intent);





            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE && data != null && data.getExtras().get("data") != null) {

           ///////////////////////////////////////////
            miProgress.setTitle("Subiendo....");
            miProgress.setMessage("Cargando foto");
            miProgress.setCancelable(false);
            miProgress.show();

            ///////////////////////////////////////////

            fotoEstudiantes = (Bitmap) data.getExtras().get("data");
            Bitmap original = ((Bitmap) fotoEstudiantes);


            fotografia.setImageBitmap(original);
            miProgress.dismiss();

        }

        if (requestCode == SELECT_PICTURE) {

            /////////////////////////////

            ////////////////////////////

            if (resultCode == RESULT_OK) {
                Uri urlImagen = data.getData();
                // fotoEstudiantes = BitmapFactory.decodeFileDescriptor(urlImagen);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(AgendarActivity.this.getContentResolver(), urlImagen);
                    //Uri.parse(String.valueOf(urlImagen) tambien funciona con este fragmento de codigo
                    fotoEstudiantes = bitmap;
                    Bitmap ori = ((Bitmap) fotoEstudiantes);
                    fotografia.setImageBitmap(ori);

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


    private void cargarModalidad() {
        try {

            listModalidad = new ArrayList<ModalidadDTO>();

            //CONSULTANDO TODOS LOS TIPOS DE MODALIDAD
            FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = fireDatabase.getReference("modalidad");


            ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.i("Info", "FIRE BASE DETECTO CAMBIOS");
                    listModalidad.clear();


                    for (DataSnapshot iterado : dataSnapshot.getChildren()) {

                        ModalidadDTO modalidadDTO = iterado.getValue(ModalidadDTO.class);

                        listModalidad.add(modalidadDTO);
                    }
                    cargarSpinner();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            Log.e("ERROR", "ERROR----->", e);

        }


    }

    public void cargarSpinner() {
        ArrayAdapter<ModalidadDTO> adapter = new ArrayAdapter<ModalidadDTO>(this, android.R.layout.simple_spinner_item, listModalidad);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modalidad.setAdapter(adapter);

        modalidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Error", modalidad.getSelectedItem().toString());
                modalidad.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        LatLng latitudLongitud = new LatLng(3.4372200,-76.522500);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latitudLongitud,13));
        mapa.setOnMarkerClickListener(this);





    }




    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}

