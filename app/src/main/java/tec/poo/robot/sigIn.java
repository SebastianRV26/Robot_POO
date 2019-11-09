package tec.poo.robot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Exceptions.AndroidException;
import Models.Client;

public class sigIn extends AppCompatActivity {

    private EditText lblname;
    private EditText lbllastName;
    private EditText lblusername;
    private EditText lblpasword;
    private EditText lbladress;
    private EditText lblpostal;
    private EditText lblcardnumber;
    private Button btnSigIn;
    private Button before;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //the "labels"
        setContentView(R.layout.activity_sig_in);
        lblname = findViewById(R.id.lblname);
        lbllastName = findViewById(R.id.lbllastname);
        lblusername = findViewById(R.id.lblusername);
        lblpasword = findViewById(R.id.lblpasword);
        lbladress = findViewById(R.id.lbladress);
        lblpostal = findViewById(R.id.lblpostal);
        lblcardnumber = findViewById(R.id.lblcardnumber);

        //button
        btnSigIn = findViewById(R.id.btnSigIn);
        //Click event for start button
        btnSigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((lblname != null) && (lbllastName != null) && (lblusername != null) && (lblpasword != null) && (lbladress != null) && (lblpostal != null) && (lblcardnumber != null)) {
                        String gg = lblcardnumber.getText().toString();
                        int entero = new Integer(gg).intValue();
                        //sigin the new user and add at the list in the mainActivity
                        Client client = new Client(lblname.getText().toString(), lbllastName.getText().toString(), lblusername.getText().toString(),
                                lblpasword.getText().toString(), lbladress.getText().toString(), lblpostal.getText().toString(), entero, false);
                        MainActivity.clients.add(client);
                        Intent i = new Intent(sigIn.this, LoginActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Registrado correctamente!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }catch (NumberFormatException ex){
                    Toast.makeText(getApplicationContext(), "Favor no ingresar letras en el ID o número de targeta", Toast.LENGTH_LONG).show();
                }catch(NullPointerException ex){
                    Toast.makeText(getApplicationContext(), "Favor llenar todo el formulario", Toast.LENGTH_LONG).show();
                }


                Toast.makeText(getApplicationContext(), "Favor finalice el formulario!", Toast.LENGTH_LONG).show();
            }
        });
        before = findViewById(R.id.btnBefore);
        //Click event for start button
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(sigIn.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }
}
