package buscapalabras.javax.valeriano.buscapalabras;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class Select_GameActivity extends AppCompatActivity {

    ImageButton imageSelect;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__game);
        String[] game = {"Facil (60 sec)","Medio (40 sec)","Dificil (30 sec)"};
        spin = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,game);
        spin.setAdapter(adapter);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        imageSelect = (ImageButton) findViewById(R.id.imageButton);

        imageSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Select_GameActivity.this, MainActivity.class);
                i.putExtra("duracion", spin.getSelectedItem().toString());
                startActivity(i);
            }
        });

    }
}
