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
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        //ProgressBar loadingProgressBar = findViewById(R.id.loading);
        MainActivity.initialUsers();
        //buttons
        loginButton = findViewById(R.id.login);
        //Click event for start button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int res  = MainActivity.login(usernameText.getText().toString(), passwordText.getText().toString());
                if (res==0){
                    //change view
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else if (res==1){
                    Toast.makeText(getApplicationContext(),"La contraseña no coincide con el nombre de usuario", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario no encontrado", Toast.LENGTH_LONG).show();
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
