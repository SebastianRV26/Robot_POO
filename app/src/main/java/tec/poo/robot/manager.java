package tec.poo.robot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class manager extends AppCompatActivity {

    private Button edittButton;
    private Button deletetButton;
    private Button addtButton;
    private Button before;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        addtButton= findViewById(R.id.btnadd);
        addtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(manager.this, addArticle.class);
                startActivity(i);
            }
        });

        edittButton = findViewById(R.id.btnEdit);
        //getOutButton = findViewById(R.id.btnGetOut);

        //Click event for start button
        edittButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(manager.this, editArticle.class);
                startActivity(i);
            }
        });

        deletetButton = findViewById(R.id.btnDelete);
        //getOutButton = findViewById(R.id.btnGetOut);

        //Click event for start button
        deletetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(manager.this, deleteArticle.class);
                startActivity(i);
            }
        });

        before = findViewById(R.id.btnBefore);
        //Click event for start button
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(manager.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
