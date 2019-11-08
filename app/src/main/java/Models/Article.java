/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import tec.poo.robot.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Sebas
 */
public abstract class Article {
    private int ID;
    private int price;
    private String name;
    private String color;
    private int quantity;
    private Date date = new Date();
    public Article(int ID, int price, String name, String color, int quantity) {
        this.ID = ID;
        this.price = price;
        this.name = name;
        this.color = color;
        this.quantity = quantity;
    }

    public Article() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void incQuantity(int cant){
        this.quantity += cant;
    }
    public void decQuantity(int cant){
        this.quantity -= cant;
    }
    public abstract boolean reserveArticleWithCash (Article article, int cant, int amount); //reservar artículo con efectivo

    public abstract boolean reserveArticleWithCard (Article article, int cant);

    public boolean sendByMail(Article article, int cant){ //enviar por correos CR 
        /*
        //hay que validar :v
        btnDefecto.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View arg0) {
        Toast toast1 =
            Toast.makeText(getApplicationContext(),
                    "Artículo enviado por correo exitosamente! ", Toast.LENGTH_SHORT);
 
        toast1.show();
    }
});
        */
        return true;
    }
    public boolean sendPackage(Article article, int cant){ //enviar por correos CR 
        /*
        //hay que validar :v
        btnDefecto.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View arg0) {
        Toast toast1 =
            Toast.makeText(getApplicationContext(),
                    "Puede retirar el artículo "+article.getName(), Toast.LENGTH_SHORT);
 
        toast1.show();
    }
});
        */
        return true;
    }
    public boolean payWithCash(Article article, int cant, double amount){
        double price;
        if (article instanceof Dress){
            price = ((Dress) article).makeDiscount()*cant;
        }else if(article instanceof Pant){
            price = ((Pant) article).makeDiscount()*cant;
        }else{
            price = article.getPrice()*cant;
        }

        if (price>amount){ //no tiene suficiente plata
            return false;
        }
        else if (cant>article.getQuantity()){ //no hay artículos suficientes
            return false;
        }else{
            double cambio = amount - article.getPrice()*cant;
            article.quantity -=cant; //se resta en el inventario
            DateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
            String mail =
                    "Para: " + MainActivity.clientActually.getName()+ "\tEntregado en Santa Clara el día "+dateFormat.format(date)+ "\n"
                            +"Cantidad "+cant +"\t Artículo " +article.name +"\t Precio: ₡"+price +"\t Efectivo: ₡"+amount +"\t Vuelto: ₡"+cambio +"\n"+
                            "----------------------------------------------------------------------------------------------";
            MainActivity.email = MainActivity.email +mail;
            return true;
        }
    }
    public boolean payWithCard(Article article, int cant, int IDCard, String nameClient, String lastName){ //número de targeta
        double price;
        if (article instanceof Dress){
            price = ((Dress) article).makeDiscount()*cant;
        }else if(article instanceof Pant){
            price = ((Pant) article).makeDiscount()*cant;
        }else{
            price = article.getPrice()*cant;
        }
        if (cant>article.getQuantity()){ //no hay artículos suficientes
            return false;
        }else{
            article.quantity -=cant; //se resta en el inventario
            DateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
            String mail =
                    "Para: " + nameClient+"\tApellido: "+lastName +"\tEntregado en Santa Clara el día "+dateFormat.format(date)+ "\n"
                            +"Cantidad "+cant +"\t Artículo " +article.name +"\t Precio: ₡"+article.getPrice()*cant +"\n"+
                            "----------------------------------------------------------------------------------------------";
            MainActivity.email = MainActivity.email +mail;
        }
        return true;
    }
    /*
        public boolean reservewithCard(Article article, int cant, int IDCard, String nameClient, int cedula){
            double price;
            if (article instanceof Dress){
                price = ((Dress) article).makeDiscount()*cant;
            }else if(article instanceof Pant){
                price = ((Pant) article).makeDiscount()*cant;
            }else{
                price = article.getPrice()*cant;
            }
            if (cant>article.getQuantity()){ //no hay artículos suficientes
                return false;
            }else{
                article.quantity -=cant; //se resta en el inventario
                DateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
                String mail =
                        "Para: " + nameClient+"\tID: "+cedula +"\tEntregado en Santa Clara el día "+dateFormat.format(date)+ "\n"
                                +"Cantidad "+cant +"\t Artículo " +article.name +"\t Precio: ₡"+article.getPrice()*cant +"\n"+
                                "----------------------------------------------------------------------------------------------";
                MainActivity.email = MainActivity.email +mail;
            }
            return true;
        }
        */
    public boolean takeOutFromReservation(Article article, int cant){
        for (Article a :MainActivity.reservation.getArticles()){
            if (a.equals(article)){
                a.decQuantity(cant);
                return true;
            }
        }
        return false;
    }
}
