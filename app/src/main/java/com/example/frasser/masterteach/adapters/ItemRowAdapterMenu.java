package com.example.frasser.masterteach.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frasser.masterteach.R;
import com.example.frasser.masterteach.dataItems.DataItem;

import java.util.List;

/**
 * Created by Juan Pablo on 7/03/2018.
 */

public class ItemRowAdapterMenu extends ArrayAdapter <DataItem> {

private Context context;
private int layoutResourceId;
private List <DataItem> data = null;

    public ItemRowAdapterMenu(@NonNull Context context, int resource, @NonNull List<DataItem> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;
    }

    public static class DataHolder {

        ImageView imageView;
        TextView nombre;
        TextView descripcion;
        TextView precio;
        TextView horas;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DataHolder holder = null;

        try {
            if (convertView == null){
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId,null);

                holder = new DataHolder();
                holder.imageView = convertView.findViewById(R.id.imageView2);
                holder.nombre = convertView.findViewById(R.id.txt_nombre_profesor);
                holder.descripcion = convertView.findViewById(R.id.txt_descripcion);
                holder.precio = convertView.findViewById(R.id.txt_precio);
                holder.horas = convertView.findViewById(R.id.txt_horas_experiencia);

                convertView.setTag(holder);
            }else {
                holder = (DataHolder) convertView.getTag();
            }

            DataItem dataItem = data.get(position);
            holder.imageView.setImageBitmap(dataItem.getId());
            holder.nombre.setText(dataItem.getNobreprofesor());
            holder.descripcion.setText(dataItem.getTextDescrip());
            holder.precio.setText(dataItem.getPrecio());
            holder.horas.setText(dataItem.getHoras());


        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
