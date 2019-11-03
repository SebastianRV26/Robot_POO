package tec.poo.robot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Models.Article;

public class editArticle extends AppCompatActivity {

    private EditText IDText;
    private Button editebutton;
    private Button search;
    private EditText articlename;
    private EditText articlecolor;
    private EditText articleprice;
    private EditText articlequantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);
        search = findViewById(R.id.okbutton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articlename = findViewById(R.id.lblarticle);
                articlecolor = findViewById(R.id.lblcolor);
                articleprice = findViewById(R.id.lblprice);
                articlequantity = findViewById(R.id.lblquantity);
                IDText = findViewById(R.id.editText);
                if (IDText != null) {
                    Article a = MainActivity.searchArticle(Integer.parseInt(IDText.getText().toString()));
                    if (a!=null){
                        articlename.setText(a.getName());
                        articlecolor.setText(a.getColor());
                        articleprice.setText(""+a.getPrice());
                        articlequantity.setText(""+a.getQuantity());
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Artículo no encontrado", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(), "Digite un ID válido", Toast.LENGTH_LONG).show();
            }
        });

        editebutton = findViewById(R.id.btnedit);
        editebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleprice = findViewById(R.id.lblprice);
                articlequantity = findViewById(R.id.lblquantity);
                IDText = findViewById(R.id.editText);
                if (IDText != null) {
                    Article a = MainActivity.searchArticle(Integer.parseInt(IDText.getText().toString()));
                    if (a!=null){
                        a.setPrice(Integer.parseInt(articleprice.getText().toString()));
                        a.setQuantity(Integer.parseInt(articlequantity.getText().toString()));
                        Toast.makeText(getApplicationContext(), "Artículo editado correctamente", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(editArticle.this, manager.class);
                        startActivity(i);
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Artículo no encontrado", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(), "Digite un ID válido", Toast.LENGTH_LONG).show();
            }
        });
    }
}
