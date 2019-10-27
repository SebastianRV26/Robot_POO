/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import tec.poo.robot.MainActivity;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Sebas
 */
public class Coat extends Article{
    private String type;

    public Coat(int ID, int price, String name, String color, int quantity, String type) {
        super(ID, price, name, color, quantity);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean reserveArticleWithCash (Article article, int cant, int amount) //reservar artículo        
    {
        Date date = new Date();
        ArrayList<Article> articles =((MainActivity.reservation).getArticles());
        if (article.getQuantity()<cant){
            return false;
        }else if (articles.contains(article)){           
            articles.get(articles.indexOf(article)).incQuantity(cant); 
        }else{
        Coat art = new Coat(this.getID(), this.getPrice(), this.getName(), this.getColor(), this.getQuantity(), this.getType());
        articles.add(art);
        }
        double priceToReserve = article.getPrice()*cant / 0.85;
        double cambio = amount - priceToReserve;
        DateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
        String mail =
                   "Para: " + MainActivity.clientActually.getName()+ "\tEntregado en Santa Clara el día "+dateFormat.format(date)+ "\n"
                    +"Cantidad "+cant +"\t Artículo " +article.getName()+"\t Precio: ₡"+priceToReserve +"\t Efectivo: ₡"+amount +"\t Vuelto: ₡"+cambio +"\n"+
                    "----------------------------------------------------------------------------------------------";
            MainActivity.email = MainActivity.email +mail;
        return true;
    }

    @Override
    public boolean reserveArticleWithCard(Article article, int cant) {
        Date date = new Date();
        ArrayList<Article> articles =((MainActivity.reservation).getArticles());
        if (article.getQuantity()<cant){
            return false;
        }else if (articles.contains(article)){           
            articles.get(articles.indexOf(article)).incQuantity(cant); 
        }else{
        Coat art = new Coat(this.getID(), this.getPrice(), this.getName(), this.getColor(), this.getQuantity(), this.getType());
        articles.add(art);
        }
        double priceToReserve = article.getPrice()*cant / 0.85;
        DateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
        String mail =
                   "Para: " + MainActivity.clientActually.getName()+ "\tEntregado en Santa Clara el día "+dateFormat.format(date)+ "\n"
                    +"Cantidad "+cant +"\t Artículo " +article.getName()+"\t Precio: ₡"+priceToReserve +"\n"+
                    "----------------------------------------------------------------------------------------------";
            MainActivity.email = MainActivity.email +mail;
        return true;
    }
}
