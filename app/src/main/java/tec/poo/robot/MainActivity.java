package tec.poo.robot;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import Models.*;

public class MainActivity extends AppCompatActivity {

    /** Speech to text**/
    private static final int REQUEST_CODE = 1234;
    private Button startButton;
    //private Button getOutButton;

    /** Text to speech**/
    TextToSpeech textToSpeech;

    public static Inventory inventory;
    public static Reservation reservation;
    public static String email;
    public static Date date;
    public static ArrayList<Client> clients;
    public static Client clientActually; //cliente que actialmente está comprando
    public static String inventoryInitial = "Inventario Inicial \n";
    public static String inventoryFinal = "Inventario Final \n";
    public static String conversation = "Conversación \n";
    public static String reservated = "Reservaciones \n";
    public static String buyed = "Compras \n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.date = new Date();
        this.email = "";
        this.inventory = new Inventory("Inventory"); //único inventario existente
        this.reservation = new Reservation("Reservation"); //único apartado existente

        clients = new ArrayList<>();
        Client sebas = new Client("Sebastian", "sebas", 208200987, "Pital", "1");
        clients.add(sebas);
        Client daya = new Client("Dayana","daya", 10,"Pital", "1");
        clients.add(daya);
        Client brian = new Client("Brayan","brian", 20,"Pital", "1");
        clients.add(brian);
        Client huber = new Client("Huber","huber", 30,"Santa Clara", "1");
        clients.add(huber);

        Blouse ba = new Blouse(111, 7000, "Blusa", "azul", 10, "largo");
        Blouse bb = new Blouse(222, 8000, "Blusa", "blanco", 10, "largo");
        Blouse bn = new Blouse(333, 8000, "Blusa", "negro", 10, "largo");
        inventory.setArticles(ba, bb, bn); //se agregan de 3 en 3

        Cap ca = new Cap (444,2500, "Gorra", "azul", 10, "deportiva");
        Cap cb = new Cap (555,2000, "Gorra", "blanco", 10, "deportiva");
        Cap cn = new Cap (666,2500, "Gorra", "negro", 10, "deportiva");
        inventory.setArticles(ca, cb, cn);

        Coat coa = new Coat (777, 4000,"Abrigo", "azul", 10, "deportivo");
        Coat cob = new Coat (888, 4000,"Abrigo", "blanco", 10, "deportivo");
        Coat con = new Coat (999, 4000,"Abrigo", "negro", 10, "deportivo");
        inventory.setArticles(coa, cob, con);

        Dress da = new Dress (101, 7000, "Vestido", "azul", 10,"largo");
        Dress db = new Dress (202, 7000, "Vestido", "blanco", 10,"largo");
        Dress dn = new Dress (303, 7000, "Vestido", "negro", 10,"largo");
        inventory.setArticles(da, db, dn);

        Pant pa = new Pant (404, 10000, "Pantalón", "azul", 20, "mezquilla");
        Pant pb = new Pant (505, 10000, "Pantalón", "blanco", 20, "mezquilla");
        Pant pn = new Pant (606, 10000, "Pantalón", "negro", 20, "mezquilla");
        inventory.setArticles(pa, pb, pn);

        Shirt sa = new Shirt (707, 5000, "Camisa", "azul", 15, "manga corta");
        Shirt sb = new Shirt (808, 5000, "Camisa", "blanco", 15, "manga corta");
        Shirt sn = new Shirt (909, 5000, "Camisa", "negro", 15, "manga corta");
        inventory.setArticles(sa, sb, sn);

        Short1 sha = new Short1 (110, 4000,"Short", "azul", 5, "mezquilla");
        Short1 shb = new Short1 (120, 4000,"Short", "blanco", 5, "mezquilla");
        Short1 shn = new Short1 (130, 4000,"Short", "negro", 5, "mezquilla");
        inventory.setArticles(sha, shb, shn);

        Socks soa = new Socks (140, 1000, "Medias", "azul", 10,"cortas");
        Socks sob = new Socks (150, 1000, "Medias", "blanco", 10,"cortas");
        Socks son = new Socks (160, 1000, "Medias", "negro", 10,"cortas");
        inventory.setArticles(soa, sob, son);

        Swimwear swa = new Swimwear(170, 6000, "Traje de baño", "azul", 5, "2");
        Swimwear swb = new Swimwear(180, 6000, "Traje de baño", "blanco", 5, "2");
        Swimwear swn = new Swimwear(190, 6000, "Traje de baño", "negro", 5, "2");
        inventory.setArticles(swa, swb, swn);

        T_Shirt ta = new T_Shirt(200, 5000, "Camiseta", "azul", 10, "manga corta");
        T_Shirt tb = new T_Shirt(200, 5000, "Camiseta", "banco", 10, "manga corta");
        T_Shirt tn = new T_Shirt(200, 5000, "Camiseta", "negro", 10, "manga corta");
        inventory.setArticles(ta, tb, tn);

        inventoryInitial=runInventory();

        //Set language
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });

        // Linking start button with code behind
        startButton = findViewById(R.id.btnStart);
        //getOutButton = findViewById(R.id.btnGetOut);

        //Click event for start button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    executeCommand(0); //Start with first command: saying hello!
                }else{
                    Toast.makeText(getApplicationContext(),"Por favor, conectarse a Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            ArrayList<String> matchesTextSpeeches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String speechText = matchesTextSpeeches.get(0); // Takes first speech recognized
            int command = getCommand(speechText);           // Passes first speech in text to get the command number needed.
            executeCommand(command);
        }
    }

    /**
     To Check if the net is available and connected
     * @return true if the net is available and connected
     * and false in other case
     */
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!= null && net.isAvailable() && net.isConnected()){
            return true;
        }   else {
            return false;
        }
    }

    /**
     * Gets the command number for each speech in text
     * @param speechText
     * @return command number
     */
    private int getCommand(String speechText){
        if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("camisa") || speechText.contains("Camisa")) ||
                (speechText.contains("camisas") || speechText.contains("Camisas"))){
            return 1;
        }
        else if ((speechText.contains("camisa") || speechText.contains("Camisa")) &&
                (speechText.contains("blanca") || speechText.contains("Blanca"))){
            return 2;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("camiseta") || speechText.contains("Camiseta")) ||
                (speechText.contains("camisetas") || speechText.contains("Camisetas"))){
            return 3;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("short") || speechText.contains("Short")) ||
                (speechText.contains("shorts") || speechText.contains("Shorts"))){
            return 4;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("sueter") || speechText.contains("Sueter")) ||
                (speechText.contains("suéter") || speechText.contains("Suéter")) ||
                (speechText.contains("suéters") || speechText.contains("Suéters"))||
                (speechText.contains("suéters") || speechText.contains("Suéters"))){
            return 5;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("pantalón") || speechText.contains("Pantalón")) ||
                (speechText.contains("pantalónes") || speechText.contains("Pantalónes"))){
            return 6;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("abrigo") || speechText.contains("Abrigo")) ||
                (speechText.contains("abrigos") || speechText.contains("Abrigos"))){
            return 7;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("medias") || speechText.contains("Medias"))){
            return 8;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("vestido") || speechText.contains("Vestido")) ||
                (speechText.contains("vestidos") || speechText.contains("Vestidos"))){
            return 9;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("blusa") || speechText.contains("Blusa")) ||
                (speechText.contains("blusas") || speechText.contains("Blusas"))){
            return 10;
        }
        else if((speechText.contains("comprar") || speechText.contains("Comprar")) &&
                (speechText.contains("gorra") || speechText.contains("Gorra")) ||
                (speechText.contains("gorras") || speechText.contains("Gorras"))){
            return 11;
        }
        if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("camisa") || speechText.contains("Camisa")) ||
                (speechText.contains("camisas") || speechText.contains("Camisas"))){
            return 12;
        }
        else if((speechText.contains("apartar") || speechText.contains("apartar")) &&
                (speechText.contains("camiseta") || speechText.contains("Camiseta")) ||
                (speechText.contains("camisetas") || speechText.contains("Camisetas"))){
            return 13;
        }
        else if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("short") || speechText.contains("Short")) ||
                (speechText.contains("shorts") || speechText.contains("Shorts"))){
            return 14;
        }
        else if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("sueter") || speechText.contains("Sueter")) ||
                (speechText.contains("sueters") || speechText.contains("Sueters"))){
            return 15;
        }
        else if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("pantalón") || speechText.contains("Pantalón")) ||
                (speechText.contains("pantalónes") || speechText.contains("Pantalónes"))){
            return 16;
        }
        else if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("abrigo") || speechText.contains("Abrigo")) ||
                (speechText.contains("abrigos") || speechText.contains("Abrigos"))){
            return 17;
        }
        else if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("medias") || speechText.contains("Medias"))){
            return 18;
        }
        else if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("vestido") || speechText.contains("Vestido")) ||
                (speechText.contains("vestidos") || speechText.contains("Vestidos"))){
            return 19;
        }
        else if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("blusa") || speechText.contains("Blusa")) ||
                (speechText.contains("blusas") || speechText.contains("Blusas"))){
            return 20;
        }
        else if((speechText.contains("apartar") || speechText.contains("Apartar")) &&
                (speechText.contains("gorra") || speechText.contains("Gorra")) ||
                (speechText.contains("gorras") || speechText.contains("Gorras"))){
            return 21;
        } else if ((speechText.contains("camisa")) || (speechText.contains("Camisa")) ||
                (speechText.contains("camisas")) || (speechText.contains("Camisas")) &&
                (speechText.contains("blanca")) || (speechText.contains("Blanca")) ||
                (speechText.contains("blancas")) || (speechText.contains("Blancas"))){
            return 22;
        }
        else if (speechText.contains("Salir") || speechText.contains("salir") || speechText.contains("bye")){
            return 100;
        }
        return -1;
    }

    /**
     * Excecute commands, interacting with users
     * @param command number to excecute
     */
    private void executeCommand(int command){
        switch (command){
            case 0:     //Saying hello command
                speak("Hola! ¿En qué le puedo ayudar?"); //Text to speech, robot speaking
                while (textToSpeech.isSpeaking()){

                }
                recordSpeech();   //Speech to text: Client answer
                break;
            case 1:     //Buy a shirt command
                speak("Tengo camisas blancas, negras y azules, de manga corta. ¿De qué color la quiere?");
                while (textToSpeech.isSpeaking()){

                }
                recordSpeech();  //Speech to text: Client answer
                break;
            case 2:     //Buy a shirt command
                speak("La camisa blanca cuesta treinta mil colones. ¿La desea comprar?");
                while (textToSpeech.isSpeaking()){

                }
                recordSpeech();  //Speech to text: Client answer
                break;
            case 22: //camisa blanca
                Article art = searchArticle("Camisa","blanco", "manga corta");
                speak("Cada camisa cuesta "+art.getPrice()+".¿Cuántas camisas desea comprar?");
                while (textToSpeech.isSpeaking()){

                }
            case 100:   //Saying good bye command
                speak("Adiós, fue un placer ayudarte");
                while (textToSpeech.isSpeaking()){

                }

                inventoryFinal=runInventory();
                email = inventoryInitial + conversation + buyed + reservated + inventoryFinal;

                //send the mail
                Intent emailIntent = new Intent (Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String("sebasrv26@gmail.com")); //to
                emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, "Proyecto de POO"); //tytle
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Correo desde Android Studio"); //subject
                emailIntent.setType("message/rfc822"); //type
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, email); //email
                startActivity(emailIntent); //send

                finish(); //close the program
                break;
            default:
                speak("No he logrado entender, por favor, me puede explicar de nuevo");
                while (textToSpeech.isSpeaking()){

                }
                recordSpeech();  //Speech to text: Client answer
                break;
        }
    }

    /**
     * Speak a speech text
     * @param speechText to speak
     */
    private void speak(String speechText){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            textToSpeech.speak(speechText, TextToSpeech.QUEUE_FLUSH,null, null);
        }else {
            textToSpeech.speak(speechText, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    /**
     * Records the speech by the user, transform it to text
     * Shows an dialog activity of voice recognizing
     */
    private void recordSpeech(){
        if(isConnected()){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public boolean sendByMail(Article article, int cant){ //enviar por correos CR

        //hay que validar :v
        Toast.makeText(getApplicationContext(),"Artículo enviado por correo exitosamente!", Toast.LENGTH_LONG).show();
        return true;
    }
    public boolean sendPackage(Article article, int cant){ //se lo puede llevar

        //hay que validar :v
        Toast.makeText(getApplicationContext(),"Puede retirar el artículo "+article.getName(), Toast.LENGTH_LONG).show();
        return true;
    }

    public Article searchArticle(String name, String color, String strin){
        for (Article a : inventory.getArticles()){
            if (a instanceof Shirt) {
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Shirt) a).getManga().equals(strin)) {
                    return a;
                }
            }else if (a instanceof Blouse){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Blouse) a).getNeckline().equals(strin)) {
                    return a;
                }
            }else if (a instanceof Cap){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Cap) a).getType().equals(strin)) {
                    return a;
                }
            }else if (a instanceof Coat){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Coat) a).getType().equals(strin)) {
                    return a;
                }
            }else if (a instanceof Dress){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Dress) a).getLong().equals(strin)) {
                    return a;
                }
            }else if (a instanceof Pant){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Pant) a).getWorkManShip().equals(strin)) {
                    return a;
                }
            }else if (a instanceof Short1){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Short1) a).getWorkManShip().equals(strin)) {
                    return a;
                }
            }else if (a instanceof Socks){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Socks) a).getType().equals(strin)) {
                    return a;
                }
            }else if (a instanceof T_Shirt){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((T_Shirt) a).getSleeveType().equals(strin)) {
                    return a;
                }
            }else if (a instanceof Swimwear){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Swimwear) a).getCantPieces().equals(strin)){
                    return a;
                }
            }
        }
        return null;
    }
    public Article searchSwimwear (String name, String color, int pieces){
        for (Article a : inventory.getArticles()){
            if (a instanceof Swimwear){
                if (a.getName().equals(name) && a.getColor().equals(color) && ((Blouse) a).getPrice()==pieces){
                    return a;
                }
            }
        }
        return null;
    }

    public Client searchClient (String username){
        for (Client c : clients){
            if (c.getUsername().equals(username)){
                return c;
            }
        }
        return null;
    }

    public int entero (String num){
        if (num.equals("uno"))
            return 1;
        if (num.equals("dos"))
            return 2;
        if (num.equals("tres"))
            return 3;
        if (num.equals("cuatro"))
            return 4;
        if (num.equals("cinco"))
            return 5;
        if (num.equals("seis"))
            return 6;
        if (num.equals("siete"))
            return 7;
        if (num.equals("ocho"))
            return 8;
        if (num.equals("nueve"))
            return 9;
        if (num.equals("diez"))
            return 10;
        if (num.equals("once"))
            return 11;
        if (num.equals("doce"))
            return 12;
        if (num.equals("trece"))
            return 13;
        if (num.equals("catorce"))
            return 14;
        if (num.equals("quince"))
            return 15;
        return 0;
    }

    public String runInventory(){
        String invent="";
        for (Article a : inventory.getArticles()){
            invent+=a.getName()+" "+a.getColor()+ ": "+a.getQuantity()+"\n";
        }
        return invent;
    }

}
