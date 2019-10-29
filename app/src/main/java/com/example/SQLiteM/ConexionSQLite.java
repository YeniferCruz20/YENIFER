package com.example.SQLiteM;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class ConexionSQLite extends SQLiteOpenHelper {
    ArrayList<String> listaArticulos;
    ArrayList<Dto> articuloslist;
    boolean estadoDelete = true;


    public ConexionSQLite(Context context) {
        super(context, "administracion.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table articulos (codigo integer not null primary key, descripcion text, precio real)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists articulos");
        onCreate(db);
    }

    public SQLiteDatabase bd() {
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd;
    }

    public boolean InsertarTradicional(Dto datos) {
        boolean estado = true;
        int resultado;

        try {
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" + datos.getCodigo() + "'", null);
            if (fila.moveToFirst() == true) {
                estado = false;
            } else {
                String SQL = "INSERT INTO articulos \n" +
                        "(codigo,descripcion,precio)\n" +
                        "VALUES \n" +

                        "('" + String.valueOf(codigo) + "', '" + descripcion + "', '" + String.valueOf(precio) + "');";

                bd().execSQL(SQL);
                bd().close();
                estado = true;
            }
        } catch (Exception e) {
            estado = false;
            Log.e("error", e.toString());
        }
        return estado;
    }

    public boolean consultacodigo(Dto datos) {
        boolean estado = true;
        int resultado;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int codigo = datos.getCodigo();
            Cursor fila = db.rawQuery("select codigo, descripcion, precio  from articulos where codigo=" + codigo, null);
            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;

            }
            db.close();
        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public boolean consultarArticulos(Dto datos) {
        boolean estado = true;
        int resultado;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] parametros = {String.valueOf(datos.getCodigo())};
            String[] campos = {"codigo", "descripcion", "precio"};
            Cursor fila = db.query("articulos", campos, "codigo=?", parametros, null, null, null);

            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;
            }
            fila.close();

        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public boolean consultarDescripcion(Dto datos) {
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getReadableDatabase();
        try {
            String descripcion = datos.getDescripcion();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from articulos where descripcion='" + descripcion + "'", null);
            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;
            }
            fila.close();

        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public boolean bajaCodigo (final Context context ,final Dto datos){
        estadoDelete = true;
        try{
            int codigo = datos.getCodigo();
            Cursor fila = bd().rawQuery("select * from articulos where codigo=" + codigo, null);
            if (fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_delete_forever_black_24dp);
                builder.setTitle("Warning");
                builder.setMessage("¿Esta seguro de borrar el registro? \nCódigo: " + datos.getCodigo()+"\nDescripción: "+datos.getDescripcion());
                builder.setCancelable(false);
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int codigo = datos.getCodigo();
                        int cant = bd().delete("articulos", "codigo=" + codigo, null);
                        if (cant > 0) {
                            estadoDelete = true;
                            Toast.makeText(context, "Registro eliminado satisfactoriamente.", Toast.LENGTH_SHORT).show();
                        }else {
                            estadoDelete = false;
                        }
                        bd().close();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                Toast.makeText(context, "No hay resultados encontrados para la búsqueda específica", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            estadoDelete = false;
            Log.e("Error.", e.toString());
        }
        return estadoDelete;
    }


    public boolean modificar (Dto datos){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            int cant = (int) bd.update("articulos", registro, "codigo=" + codigo, null);

            bd.close();
            if (cant>0) estado = true;
            else estado = false;
        }catch (Exception e){
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }


    public ArrayList<Dto> consultarListaArticuloslist() {
        boolean estado = false;

        SQLiteDatabase bd = this.getReadableDatabase();

        Dto articulos = null;
        articuloslist = new ArrayList<Dto>();

        try {
            Cursor fila = bd.rawQuery("select * from articulos", null);
            while (fila.moveToNext()) {
                articulos = new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));

                articuloslist.add(articulos);
                Log.i("codigo", String.valueOf(articulos.getCodigo()));
                Log.i("descripcion", articulos.getDescripcion().toString());
                Log.i("precio", String.valueOf(articulos.getPrecio()));
            }
            obtenerListaArticulos();
        } catch (Exception e) {

        }
        return articuloslist;

    }
    public ArrayList<String> obtenerListaArticulos () {
        listaArticulos = new ArrayList<String>();
        listaArticulos.add("Seleccione");

        for (int i = 0; i < articuloslist.size(); i++) {
            listaArticulos.add(articuloslist.get(i).getCodigo() + "~" + articuloslist.get(i).getDescripcion());
        }
        return listaArticulos;
    }

}










































