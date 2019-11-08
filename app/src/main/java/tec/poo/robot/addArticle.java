package tec.poo.robot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import Models.ArticleFactory;

public class addArticle extends AppCompatActivity {

    private Button addtButton;
    private Button before;
    private Button okbutton;

    private EditText articleprice;
    private EditText articlequantity;
    private EditText articleid;
    private EditText articletype;
    private String articlename;
    private String articlecolor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);


        addtButton = findViewById(R.id.btnadd);
        //getOutButton = findViewById(R.id.btnGetOut);

        //Click event for start button
        addtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleprice = findViewById(R.id.lblprice);
                articlequantity = findViewById(R.id.lblquantity);
                articleid = findViewById(R.id.lblprice);
                articletype = findViewById(R.id.lblquantity);
                Spinner Spinnername=(Spinner) findViewById(R.id.spnArticles);
                articlename = Spinnername.getSelectedItem().toString();
                Spinner SpinnerColor=(Spinner) findViewById(R.id.spnColors);
                articlename = SpinnerColor.getSelectedItem().toString();

                String tipo="";
                int num=0;
                if (articlename=="Blusa"){
                    num=1;
                    tipo = "largo";
                }
                else if (articlename=="Gorra"){
                    num=2;
                    tipo = "deportiva";
                }
                else if (articlename=="Abrigo"){
                    num=3;
                    tipo = "deportivo";
                }
                else if (articlename=="Vestido"){
                    num=4;
                    tipo = "largo";
                }
                else if (articlename=="Pantalon"){
                    num=5;
                    tipo = "mezquilla";
                }
                else if (articlename=="Camisa"){
                    num=6;
                    tipo = "manga corta";
                }
                else if (articlename=="Short"){
                    num=7;
                    tipo = "mezquilla";
                }
                else if (articlename=="Medias"){
                    num=8;
                    tipo = "cortas";
                }
                else if (articlename=="Traje de baño"){
                    num=9;
                    tipo = "2";
                }
                else if (articlename=="Camiseta"){
                    num=10;
                    tipo = "manga corta";
                }
                ArticleFactory factory = new ArticleFactory();
                try {
                    //int ID, int price, String name, String color, int quantity, String tipo
                    if (factory.maker(num, Integer.parseInt(articleid.getText().toString()),
                            Integer.parseInt(articleprice.getText().toString()), articlename, articlecolor,
                            Integer.parseInt(articlequantity.getText().toString()), tipo)) {
                        Toast.makeText(getApplicationContext(), "Artículo agregado correctamente!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }catch(NullPointerException ex){
                    Toast.makeText(getApplicationContext(), "Favor llenar todo el formulario", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(),"El artículo no ha sido agregado!", Toast.LENGTH_LONG).show();
            }
        });

        before = findViewById(R.id.btnBefore);
        //Click event for start button
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(addArticle.this, manager.class);
                startActivity(i);
            }
        });
    }
}
