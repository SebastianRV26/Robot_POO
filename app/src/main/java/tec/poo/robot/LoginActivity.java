package tec.poo.robot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import Models.Client;
import Models.Inventory;


public class LoginActivity extends AppCompatActivity {
    //variable globals
    private Button loginButton;
    private Button sigIntButton;
    private EditText usernameText;
    private EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //constructor
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Inventory inventory = Inventory.getInstance();
        if (inventory.getCont()==0){
            //ProgressBar loadingProgressBar = findViewById(R.id.loading);
            MainActivity.initialUsers();
            inventory.setCont();
        }
        //buttons
        loginButton = findViewById(R.id.login);
        //Click event for start button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameText = findViewById(R.id.username);
                passwordText = findViewById(R.id.password);
                if ((usernameText!=null) && (passwordText!=null)) {
                    Client res = MainActivity.login(usernameText.getText().toString(), passwordText.getText().toString());
                    if (res != null) {
                        //change view
                        if (res.getType()==false) {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(LoginActivity.this, manager.class);
                            startActivity(i);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        sigIntButton = findViewById(R.id.btnSigIn);
        //getOutButton = findViewById(R.id.btnGetOut);

        //Click event for start button
        sigIntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, sigIn.class);
                startActivity(i);
            }
        });

    }

}
