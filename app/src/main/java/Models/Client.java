/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Sebas
 */
public class Client {
    private String name;
    private String username;
    private int ID;
    private String direction;
    private String pasword;

    public Client(String name, String username, int ID, String direction, String pasword) {
        this.name = name;
        this.username = username;
        this.ID = ID;
        this.direction = direction;
        this.pasword = pasword;
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
}
