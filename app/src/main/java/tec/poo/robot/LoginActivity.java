package tec.poo.robot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Models.Client;


public class LoginActivity extends AppCompatActivity {
    //variable globals
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //constructor
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText usernameText = findViewById(R.id.username);
        final EditText passwordText = findViewById(R.id.password);
        ProgressBar loadingProgressBar = findViewById(R.id.loading);

        //buttons
        loginButton = findViewById(R.id.login);
        //Click event for start button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Client c : MainActivity.clients){
                    if (c.getUsername().equals(usernameText.getText())){
                        if (c.getPasword().equals(passwordText.getText())){
                            //change view
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        Toast.makeText(getApplicationContext(),"La contraseña no coincide con el nombre de usuario", Toast.LENGTH_LONG).show();
                    }
                }
                Toast.makeText(getApplicationContext(),"Usuario no encontrado", Toast.LENGTH_LONG).show();

            }
        });
    }

}
