package cuellardelgado.encrypterw;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.ComponentName;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnSaveInput, btnClose;
    EditText textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        String action = Paper.book().read("action");
        String tmp_action = "ENCRYPT";
        if(action == null) Paper.book().write(("action"), tmp_action);
        btnSaveInput = findViewById(R.id.btnSaveInput);
        btnClose = findViewById(R.id.btnFinish);
        textInput = findViewById(R.id.textInput);
        btnSaveInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write(("input"), textInput.getText().toString());
                Toast.makeText(MainActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                Context context = getApplicationContext();
                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), EncryptWidget.class));
                EncryptWidget myWidget = new EncryptWidget();
                myWidget.onUpdate(context, AppWidgetManager.getInstance(context),ids);
                //Toast.makeText(MainActivity.this, "Exit!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
}
