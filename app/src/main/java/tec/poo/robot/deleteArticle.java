package tec.poo.robot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Models.Article;

public class deleteArticle extends AppCompatActivity {

    private EditText IDText;
    private Button deleteButton;
    private Button search;
    private EditText articlename;
    private EditText articlecolor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_article);
        //Click event for start button
        deleteButton = findViewById(R.id.btndel);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDText = findViewById(R.id.editText);
                if (IDText != null) {
                    Article a = MainActivity.searchArticle(Integer.parseInt(IDText.getText().toString()));
                    if (a!=null){
                        MainActivity.inventory.getArticles().remove(a); //delete article
                        Toast.makeText(getApplicationContext(), "Artículo eliminado correctamente", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(deleteArticle.this, manager.class);
                        startActivity(i);
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Artículo no encontrado", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(), "Digite un ID válido", Toast.LENGTH_LONG).show();
            }
        });

        search = findViewById(R.id.okbuttondelete);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articlename = findViewById(R.id.lblarticle);
                articlecolor = findViewById(R.id.lblcolor);
                IDText = findViewById(R.id.delete);
                if (IDText != null) {
                    Article a = MainActivity.searchArticle(Integer.parseInt(IDText.getText().toString()));
                    if (a!=null){
                        articlename.setText(a.getName());
                        articlecolor.setText(a.getColor());
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Artículo no encontrado", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(), "Digite un ID válido", Toast.LENGTH_LONG).show();
            }
        });
    }
}
