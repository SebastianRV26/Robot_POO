/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;

/**
 *
 * @author Sebas
 */
public class Client {
    private String name;
    private String username;
    private String lastName;
    private String direction;
    private String pasword;
    private String postalApart;
    private int cardNumber;
    private boolean type;
    private ArrayList<Article> articles = new ArrayList<>();

    public Client(String name, String lastName, String username,  String pasword, String direction, String postalApart,  int cardNumber, boolean type) {
        this.name = name;
        this.username = username;
        this.lastName = lastName;
        this.direction = direction;
        this.pasword = pasword;
        this.postalApart = postalApart;
        this.cardNumber = cardNumber;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getID() {
        return this.lastName;
    }

    public void setID(String lastName) {
        this.lastName = lastName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPasword(){
        return this.pasword;
    }

    public void setPasword(String pasword){
        this.pasword = pasword;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostalApart() {
        return postalApart;
    }

    public void setPostalApart(String postalApart) {
        this.postalApart = postalApart;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(Article article) {
        this.articles.add(article);
    }
}
