package Models;


import Exceptions.AndroidException;
import tec.poo.robot.MainActivity;

/**
 *
 * @author BrianP
 */


/*
1: Blouse
2: Cap
3: Coat
4: Dress
5: Pant
6: Shirt
7: Short1
8: Socks
9: Swimwear
10: T_Shiry
*/
public class ArticleFactory {

    public static boolean maker(int num, int ID, int price, String name, String color, int quantity, String tipo) throws AndroidException{
        Article article = null;

        try{
        switch (num) {
            case 1:
                article = new Blouse(ID, price, name, color, quantity, tipo);
                break;
            case 2:
                article = new Cap(ID, price, name, color, quantity, tipo);
                break;
            case 3:
                article = new Coat(ID, price, name, color, quantity, tipo);
                break;
            case 4:
                article = new Dress(ID, price, name, color, quantity, tipo);
                break;
            case 5:
                article = new Pant(ID, price, name, color, quantity, tipo);
                break;
            case 6:
                article = new Shirt(ID, price, name, color, quantity, tipo);
                break;
            case 7:
                article = new Short1(ID, price, name, color, quantity, tipo);
                break;
            case 8:
                article = new Socks(ID, price, name, color, quantity, tipo);
                break;
            case 9:
                article = new Swimwear(ID, price, name, color, quantity, tipo);
                break;
            case 10:
                article = new T_Shirt(ID, price, name, color, quantity, tipo);
                break;
            default:
                return false;
            }
        }catch(NullPointerException ex){
            throw new AndroidException("Favor llenar todo el formulario");
        }

        MainActivity.inventory.setArticles(article);
        return true;
    }
}