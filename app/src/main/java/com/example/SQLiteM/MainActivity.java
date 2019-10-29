package com.example.SQLiteM;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnguardar, btndescripcion, btncodigo, btneliminar, btnModificar;
    private EditText etcodigo, etarticulo, etprecio;

    boolean estadoCodigo = false;
    boolean estadoDescripcion = false;
    boolean estadoPrecio = false;
    int estadoInsert = 0;

    boolean inputEt=false;
    boolean  InputEd=false;
    boolean inputEp=false;

    modal ventanas = new modal();
    ConexionSQLite conexion=new ConexionSQLite(this);
    Dto datos = new Dto();
    AlertDialog.Builder dialogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnguardar = (Button) findViewById(R.id.btnguardar);
        btndescripcion = (Button) findViewById(R.id.btndescripcion);
        btncodigo = (Button) findViewById(R.id.btncodigo);
        btneliminar = (Button) findViewById(R.id.btneliminar);
        btnModificar = (Button) findViewById(R.id.btnModificar);

        etarticulo = (EditText) findViewById(R.id.etarticulo);
        etcodigo = (EditText) findViewById(R.id.etcodigo);
        etprecio = (EditText) findViewById(R.id.etprecio);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                ventanas.ventana1(MainActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_limpiar) {
            etarticulo.setText(null);
            etcodigo.setText(null);
            etprecio.setText(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void guardar(View v) {
        if (etcodigo.getText().toString().length() == 0) {
            estadoCodigo = false;
            etcodigo.setError("Campo Obligatorio");
        } else {
            estadoCodigo = true;
        }
        if (etarticulo.getText().toString().length() == 0) {
            estadoDescripcion = false;
            etarticulo.setError("Campo Obligatorio");
        } else {
            estadoDescripcion = true;
        }
        if (etprecio.getText().toString().length() == 0) {
            estadoPrecio = false;
            etprecio.setError("Campo Obligatorio");
        } else {
            estadoPrecio = true;
        }
        if (estadoCodigo && estadoDescripcion && estadoPrecio) {
            try {
                datos.setCodigo(Integer.parseInt(etcodigo.getText().toString()));
                datos.setDescripcion(etarticulo.getText().toString());
                datos.setPrecio(Double.parseDouble(etprecio.getText().toString()));
                if (conexion.InsertarTradicional(datos)){
                    Toast.makeText(this, "registro agregado satisfactoriamente!", Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                }else {
                    Toast.makeText(MainActivity.this, "Error. Ya existe un registro\n"+ " Codigo: "
                            +etcodigo.getText().toString(), Toast.LENGTH_SHORT).show();
                    limpiarDatos();
            }

                }catch (Exception e){
                Toast.makeText(this, "Error. Ya existe.", Toast.LENGTH_SHORT).show();
            }
        }
    }
public void mensaje (String mensaje){
    Toast.makeText(this, ""+mensaje, Toast.LENGTH_SHORT).show();
}

public void limpiarDatos(){
        etcodigo.setText(null);
        etprecio.setText(null);
        etarticulo.setText(null);
        etcodigo.requestFocus();
}

    public void consultapordescripcion(View v) {
      /*  ConexionSQLite admin = new ConexionSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String descri = etarticulo.getText().toString();
        Cursor fila = bd.rawQuery("select codigo,precio from articulos where descripcion ='" + descri + "'", null);

        if (fila.moveToFirst()) {
            etcodigo.setText(fila.getString(0));
            etprecio.setText(fila.getString(1));
        } else
            Toast.makeText(this, "No Existe Este Articulo Con Dicha Descripcion", Toast.LENGTH_SHORT).show();
        bd.close();*/
      if (etarticulo.getText().toString().length()==0){
          etarticulo.setError("Campo obligatorio");
          estadoDescripcion=false;
      }else {
          estadoDescripcion=true;
      }if (estadoDescripcion){
          String descripcion=etarticulo.getText().toString();
          datos.setDescripcion(descripcion);
          if (conexion.consultarDescripcion(datos)){
              etcodigo.setText(""+datos.getCodigo());
              etarticulo.setText(datos.getDescripcion());
              etprecio.setText(""+datos.getPrecio());
          }else {
              Toast.makeText(this,"No existe articulo condivha descripcion",Toast.LENGTH_SHORT).show();
              limpiarDatos();
          }
        }else {
          Toast.makeText(this, "Ingrese la descripcion del articulo",Toast.LENGTH_SHORT).show();
        }

    }


    public void consultaporcodigo(View v) {
       /* ConexionSQLite admin = new ConexionSQLite(this);
        SQLiteDatabase bd = admin.getWritableDatabase();
                                                                                                                                 <<<<<<<<<<
        String cod = etcodigo.getText().toString();
        Cursor fila =bd.rawQuery("select descripcion,precio from articulos where codigo=" + cod, null);

        if (fila.moveToFirst()){
            etarticulo.setText(fila.getString(0));
            etprecio.setText(fila.getString(1));
        }else
            Toast.makeText(this, "No Existe Este Articulo Con Dicho Codigo", Toast.LENGTH_SHORT).show();
        bd.close();*/

        if (etcodigo.getText().toString().length() == 0) {
            etcodigo.setError("Campo Obligatorio");
            inputEt = false;
        } else {
            inputEt = true;
        }
        if (inputEt) {
            String codigo = etcodigo.getText().toString();
            datos.setCodigo(Integer.parseInt(codigo));

            if (conexion.consultacodigo(datos) == true) {
                etarticulo.setText(datos.getDescripcion());

            etprecio.setText("" + datos.getPrecio());
        } else {
            Toast.makeText(this, "no exsit el articulo con dicho codigo ", Toast.LENGTH_SHORT).show();
            limpiarDatos();

        }
        }else{
            Toast.makeText(this, "ingrese el codigo a buscar", Toast.LENGTH_SHORT).show();
        }
    }




    public void eliminarporcodigo(View view) {
        /*ConexionSQLite admin = new ConexionSQLite(this);
        SQLiteDatabase bd =admin.getWritableDatabase();
        String cod = etcodigo.getText().toString();
        int cant = bd.delete("articulos" , "codigo=" + cod,null );
        bd.close();
        etcodigo.setText("");
        etarticulo.setText("");
        etprecio.setText("");
        if (cant == 1){
            Toast.makeText(this, "Se Borro El Articulo Con Dicho Codigo ", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No Existe Este Articulo Con Dicho Codigo", Toast.LENGTH_SHORT).show();
        }*/
        if (etcodigo.getText().toString().length()==0){
            etcodigo.setError("campo obligatorio");
            inputEt=false;
        }else {
            inputEt=true;
        }if (inputEt){
            String cod=etcodigo.getText().toString();
            datos.setCodigo(Integer.parseInt(cod));
            if (conexion.bajaCodigo(MainActivity.this,datos)){
                limpiarDatos();
            }else {
                Toast.makeText(this,"No existe el articulo con ese codigo",Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }

    }

    public void modificar(View view) {
       /* ConexionSQLite admin = new ConexionSQLite(this);
        SQLiteDatabase bd =admin.getWritableDatabase();
        String cod = etcodigo.getText().toString();
        String descri = etarticulo.getText().toString();
        String pre = etprecio.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("codigo" , cod);
        registro.put("descripcion", descri);
        registro.put("precio" , pre);
        int cant = bd.update("articulos", registro, "codigo=" + cod,null );
        bd.close();
        if (cant == 1) {
            Toast.makeText(this, "Se Modificaron Los Datos", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No Existe Un Articulo Con El Codigo Ingresado", Toast.LENGTH_SHORT).show();
        }
    }*/
       if (etcodigo.getText().toString().length()==0){
           etcodigo.setError("campo obligatorio");
           inputEt=false;
       }else {
           inputEt=true;
       }
       if (inputEt){
           String cod=etcodigo.getText().toString();
           String descripcion=etarticulo.getText().toString();
           double precio=Double.parseDouble(etprecio.getText().toString());

           datos.setCodigo(Integer.parseInt(cod));
           datos.setDescripcion(descripcion);
           datos.setPrecio(precio);
           if (conexion.modificar(datos)){
               Toast.makeText(this,"Registro modificado",Toast.LENGTH_SHORT).show();
           }else {
               Toast.makeText(this,"No se ha encontrado resultados de la busqueda",Toast.LENGTH_SHORT).show();
           }
       }
}
}

