package buscapalabras.javax.valeriano.buscapalabras;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    TextView palabras;
    private ArrayList<String> formList;
    RandomData datos = new RandomData();
    RandomDataMini datosMini = new RandomDataMini();
    private Random randData = new Random();
    private Integer[] colores = new Integer[] {Color.RED,Color.BLUE,Color.GREEN,Color.CYAN,Color.YELLOW,Color.MAGENTA};
    //BackgroundSound mBackgroundSound = new BackgroundSound();
    MediaPlayer player;
    int time = 30;
    Timer t;
    TimerTask task;
    int puntaje = 0;
    int tipoPantalla = 0;
    Dialog settingsDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        settingsDialog = new Dialog(this);
        //Screen Size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        tipoPantalla = width;

        Log.d("X Size: ", String.valueOf(width));
        Log.d("Y Size: ", String.valueOf(height));

        //DisplayMetrics metrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //Log.d("ApplicationTagName", "Display width in px is " + metrics.widthPixels);

        player = MediaPlayer.create(MainActivity.this, R.raw.littleidea);
        player.setLooping(true); // Set looping
        player.setVolume(1.0f, 1.0f);
        player.start();
        startTimer();

        palabras = (TextView)findViewById(R.id.palabras);

        gridView = (GridView) findViewById(R.id.gv);

        if(tipoPantalla < 1080){
            gridView.setNumColumns(9);
        }else{
            gridView.setNumColumns(10);
        }

        gridView.setOnTouchListener(new GridView.OnTouchListener(){

            ArrayList<String> arrayData = new ArrayList<String>();
            ArrayList<String> arrayCompletado = new ArrayList<String>();
            ArrayList<String> arrayPosicion = new ArrayList<String>();
            String salida = "";
            int color = colores[randData.nextInt(6)];

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float currentXPosition = event.getX();
                float currentYPosition = event.getY();
                int position = gridView.pointToPosition((int) currentXPosition, (int) currentYPosition);
                int firstPosition = gridView.getFirstVisiblePosition();
                int childPosition = position - firstPosition;
                TextView txtView = (TextView)gridView.getChildAt(childPosition);
                if(childPosition != -1){
                    String valor = txtView.getText().toString() + "-" + String.valueOf(childPosition);
                    if(!arrayData.contains(valor)){
                        Log.d("Valor", valor);
                        arrayData.add(valor.substring(0));
                    }
                    txtView.setBackgroundColor(color);
                }

                if (MotionEvent.ACTION_UP == event.getAction()) {
                    for(String resul: arrayData){
                        salida += resul.substring(0,1);
                    }

                    Log.d("Result", salida);
                    String compara = "";
                    if(tipoPantalla < 1080){
                        compara = datosMini.getPalabras().substring(1);
                    }else{
                        compara = datos.getPalabras().substring(1);
                    }
                    String[] arrayCompara = compara.split("-");
                    ArrayList<String> ListCompara = new ArrayList<String>();
                    for(int i=0; i<arrayCompara.length;i++){
                        ListCompara.add(arrayCompara[i].toString());
                    }
                    if(ListCompara.contains(salida)){
                        for(int i=0; i<arrayData.size();i++){
                            arrayPosicion.add(arrayData.get(i).substring(2));
                        }
                        puntaje += 5;
                    }
                    if(!ListCompara.contains(salida)){
                        for(int i=0; i<arrayData.size();i++){
                            TextView txtView2 = (TextView)gridView.getChildAt(Integer.parseInt(arrayData.get(i).substring(2)));
                            if(!arrayPosicion.contains(arrayData.get(i).substring(2))){
                                txtView2.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }
                    }else if (!arrayCompletado.contains(salida)){
                        arrayCompletado.add(salida);
                        color = colores[randData.nextInt(6)];
                        if(arrayCompletado.size() == 3){
                            changePalabras();
                            arrayCompletado.clear();
                            arrayPosicion.clear();
                            if(time > 0){
                                time = 30 + time;
                            }else{
                                gameOver();
                            }
                        }else{
                            String compara2 = "";
                            if(tipoPantalla < 1080){
                                compara2 = datosMini.getPalabras().substring(1);
                            }else{
                                compara2 = datos.getPalabras().substring(1);
                            }
                            String[] arrayCompara2 = compara.split("-");
                            String salidaTexto = "";
                            for(String salida: arrayCompletado){
                                Log.d("TotalPalabras: ", String.valueOf(arrayCompletado.size()));
                                for(int j = 0; j< arrayCompara2.length; j++){
                                    if(!arrayCompletado.contains(arrayCompara2[j])){
                                        if(arrayCompletado.size() == 2){
                                            salidaTexto = "-" + arrayCompara2[j];
                                        }else{
                                            salidaTexto += "-" + arrayCompara2[j];
                                        }
                                    }
                                }
                            }
                            palabras.setText("Palabras: " + salidaTexto);
                        }

                    }
                    salida = "";
                    arrayData.clear();

                } else if (MotionEvent.ACTION_MOVE == event.getAction()) {

                }
                return true;
            }
        });

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("Palabras");
            formList = new ArrayList<String>();

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Nombre-->", jo_inside.getString("nombre"));
                String nombre = jo_inside.getString("nombre");

                formList.add(nombre);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(tipoPantalla < 1080){
            datosMini.setPalabras(formList);
        }else{
            datos.setPalabras(formList);
        }

        ArrayAdapter<String> adapter = null;

        if(tipoPantalla < 1080){
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, datosMini.getDatos()){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    /// Get the Item from ListView
                    View view = super.getView(position, convertView, parent);

                    TextView tv = (TextView) view.findViewById(android.R.id.text1);

                    // Set the text size 25 dip for ListView each item
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                    tv.setHeight(1);
                    tv.setWidth(1);
                    tv.setLineSpacing(1,1);
                    // Return the view
                    return view;
                }
            };
        }else{
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, datos.getDatos()){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){
                    /// Get the Item from ListView
                    View view = super.getView(position, convertView, parent);

                    TextView tv = (TextView) view.findViewById(android.R.id.text1);

                    // Set the text size 25 dip for ListView each item
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                    tv.setHeight(1);
                    tv.setWidth(1);
                    tv.setLineSpacing(1,1);
                    // Return the view
                    return view;
                }
            };
        }


        palabras.setText("Palabras: " + datos.getPalabras().substring(1));
        gridView.setAdapter(adapter);

    }

    public void changePalabras(){
        datos = new RandomData();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("Palabras");
            formList.clear();

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Nombre-->", jo_inside.getString("nombre"));
                String nombre = jo_inside.getString("nombre");

                formList.add(nombre);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        datos.setPalabras(formList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, datos.getDatos()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                tv.setHeight(1);
                tv.setWidth(1);
                tv.setLineSpacing(1,1);
                // Return the view
                return view;
            }
        };

        palabras.setText("Palabras: " + datos.getPalabras().substring(1));
        gridView.setAdapter(adapter);
    }

    public void gameOver(){
        puntaje = 0;
        settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.modal_layout, null));
        settingsDialog.show();
    }

    public void dismissListener(View view) {
        settingsDialog.dismiss();
        Intent mainIntent = new Intent().setClass(
                MainActivity.this, Select_GameActivity.class);
        startActivity(mainIntent);

        // Close the activity so the user won't able to go back this
        // activity pressing Back button
        finish();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getResources().openRawResource(R.raw.jsonpalabras);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onResume() {
        super.onResume();
        player.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        player.pause();
    }

    public void startTimer(){
        t = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TextView tv1 = (TextView) findViewById(R.id.timeX);
                        tv1.setText("Tiempo Restante: " + time + "       Puntaje: " + puntaje);
                        if (time > 0)
                            time -= 1;
                        else {
                            tv1.setText("GAME OVER");
                            if(settingsDialog.isShowing()){
                                Log.d("Showin"," Show the Dialog");
                            }else{
                                gameOver();
                                task.cancel();
                            }

                        }
                    }
                });
            }
        };
        t.scheduleAtFixedRate(task, 0, 1000);
    }

}
