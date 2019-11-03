/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebas
 */
public class Inventory {
    private String name;
    private ArrayList<Article>articles = new ArrayList<>();

    private static Inventory instance = null;
    public static Inventory getInstance(){
        if (instance == null){
            instance = new Inventory("Inventory");
        }
        return instance;
    }
    public Inventory(String name) {
        this.name = name;
    }

    public Inventory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(Article article, Article article2, Article article3) {
        this.articles.add(article);
        this.articles.add(article2);
        this.articles.add(article3);
    }

    
}
