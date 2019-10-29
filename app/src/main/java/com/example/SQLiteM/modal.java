package com.example.SQLiteM;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




    public class modal {
        Dialog myDialog;
        AlertDialog.Builder dialogo;
        String codigo;
        String descripcion;
        String precio;

        public void ventana1(final Context context) {

            myDialog = new Dialog(context);
            myDialog.setContentView(R.layout.ventana1);
            myDialog.setTitle("Search");
            myDialog.setCancelable(true);

            final EditText et_codigo = (EditText) myDialog.findViewById(R.id.et_codigo);
            Button btnbuscar = (Button) myDialog.findViewById(R.id.btnbuscar);
            TextView tv_close = (TextView) myDialog.findViewById(R.id.tv_close);
            tv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                }
            });

            btnbuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConexionSQLite admin = new ConexionSQLite(context);
                    SQLiteDatabase db = admin.getWritableDatabase();
                    String cod = et_codigo.getText().toString();
                    Cursor fila = db.rawQuery("select codigo,descripcion,precio from articulos where codigo=" + cod, null);
                    if (fila.moveToFirst()) {
                        codigo = fila.getString(0);
                        descripcion = fila.getString(1);
                        precio = fila.getString(2);
                        String action;
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("senal", 1);
                        intent.putExtra("codigo", codigo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("precio", precio);
                        context.startActivity(intent);
                        myDialog.dismiss();
                    } else
                        Toast.makeText(context, "No existe un articulo con ese Codigo", Toast.LENGTH_LONG).show();
                    db.close();
                }
            });

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();

        }


    }
