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
public class Swimwear extends Article{
    private int cantPieces;

    public Swimwear(int ID, int price, String name, String color, int quantity, int cantPieces) {
        super(ID, price, name, color,  quantity);
        this.cantPieces = cantPieces;
    }

    public int getCantPieces() {
        return cantPieces;
    }

    public void setCantPieces(int cantPieces) {
        this.cantPieces = cantPieces;
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
        Swimwear art = new Swimwear(this.getID(), this.getPrice(), this.getName(), this.getColor(), this.getQuantity(), this.getCantPieces());
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
        Swimwear art = new Swimwear(this.getID(), this.getPrice(), this.getName(), this.getColor(), this.getQuantity(), this.getCantPieces());
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
