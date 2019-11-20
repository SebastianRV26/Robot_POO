package tec.poo.robot;

import org.junit.Test;

import Models.Article;
import Models.ArticleFactory;
import Models.Blouse;

import static org.junit.Assert.*;

public class BlouseTest {



    private Blouse mBlouse;
    private ArticleFactory mFactory;



    @Test
    public void reserveABlouseArticleWithCash() throws Exception {
        //Arrage

        ArticleFactory articlefactory = new ArticleFactory();

        int num= 2;
        //Act

        boolean prueba = articlefactory.maker(1,1,2000,"Blouse","azul",2,"cuello alto");
        //Assert
        assertEquals(true ,prueba);

    }
    @Test
    public void reserveACapArticleWithCash() throws Exception {
        //Arrage
       // Article arti = MainActivity.inventoryInitial.
        Article pant = MainActivity.searchArticle(5);
        //Act


        boolean sd = pant.payWithCard(pant,2,12,"Bryan","Perez");
        //Assert
        assertEquals(true ,sd);

    }
    @Test
    public void reserveCoatArticleWithCard() throws Exception{

        //Arrage
        ArticleFactory articlefactory = new ArticleFactory();
        int num= 1;
        //Act
        boolean prueba = articlefactory.maker(3,1,2000,"Coat","blanco",2,"deportivo");
        //Assert
        assertEquals(true ,prueba);
    }




}
