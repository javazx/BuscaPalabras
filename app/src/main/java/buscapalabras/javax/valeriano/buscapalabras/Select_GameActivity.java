package buscapalabras.javax.valeriano.buscapalabras;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Select_GameActivity extends AppCompatActivity {

    ImageButton imageSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__game);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        imageSelect = (ImageButton) findViewById(R.id.imageButton);

        imageSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Select_GameActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}
