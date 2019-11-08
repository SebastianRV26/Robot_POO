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
    private Button before;

    //private Button getOutButton;

    /** Text to speech**/
    TextToSpeech textToSpeech;

    public static Inventory inventory = Inventory.getInstance();
    public static Reservation reservation;
    public static String email;
    public static Date date;
    public static ArrayList<Client> clients = new ArrayList<>();
    public static Client clientActually; //cliente que actualmente está comprando
    public static String inventoryInitial = "Inventario Inicial \n";
    public static String inventoryFinal = "Inventario Final \n";
    public static String conversation = "Conversación \n";
    public static String reservated = "Reservaciones \n";
    public static String buyed = "Compras \n";


    String action;              //Si va a comprar, apartar, retitar un articulo
    String paymentMethod;       //Si paga en efectivo o con tarjeta
    String userName;            //Guarda el nombre de usuario
    String color;               //Guarda el color del articulo
    String nameArticle;         //Guarda el nombre del articulo
    String characteristic;      //Guarda la caracteristica del articulo
    String delivery;            //Guarda si por encomienda o si se lo lleva
    String chanceCardNumber;    //Guarda si el cliente desea cambiar la tarjeta
    int priceArt;               //Guarda el precio del articulo
    int cardNumber;             //Guarda el numero de tarjeta
    int totalPay;            //Guarda el monto total a pagar
    int cantArticles;           //Guarda la cantidad de articulos que desea la parsona
    int payColones;             //Guarda la cantidad de dinero con el que se va a pagar
    Article art;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.date = new Date();
        this.email = "";
        this.reservation = new Reservation("Reservation"); //único apartado existente



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

        before = findViewById(R.id.btnBefore);
        //Click event for start button
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            ArrayList<String> matchesTextSpeeches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            conversation+="-"+matchesTextSpeeches.get(0);
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
        String[] partsSpeechText = speechText.split(" "); //Divide el speechText y lo mete en un arreglo para obtener cada palabra por separado

        if((speechText.contains("comprar")) || (speechText.contains("Comprar"))){  //Comprar articulos
            action = partsSpeechText[partsSpeechText.length-3];
            nameArticle = partsSpeechText[partsSpeechText.length-1];
            return 1;
        }
        if((speechText.contains("color")) || (speechText.contains("Color"))){  //color de articulos
            color = partsSpeechText[partsSpeechText.length-1];
            return 2;
        }
        if((speechText.contains("quiero")) || (speechText.contains("Quiero"))){  //cantidad de articulos
            cantArticles = entero(partsSpeechText[partsSpeechText.length-1]);
            return 3;
        }
        if((speechText.contains("efectivo")) || (speechText.contains("efectivo"))||
                (speechText.contains("tarjeta")) || (speechText.contains("Tarjeta"))){  //pagar con efectivo o con tarjeta
            paymentMethod = partsSpeechText[partsSpeechText.length-1];
            return 4;
        }
        if((speechText.contains("colones")) || (speechText.contains("Colones"))){  //cantidad de dinero con el que piensa pagar
            payColones =  Integer.parseInt(partsSpeechText[partsSpeechText.length-2]);
            return 5;
        }
        if((speechText.contains("llevo")) || (speechText.contains("Llevo"))||
                (speechText.contains("encomienda")) || (speechText.contains("Encomienda"))||
                (speechText.contains("llevar")) || (speechText.contains("Llevar"))){  //cantidad de dinero con el que piensa pagar
            delivery =  partsSpeechText[partsSpeechText.length-1];
            return 6;
        }
        if((speechText.contains("Sí")) || (speechText.contains("sí"))||
                (speechText.contains("Si")) || (speechText.contains("si"))||
                (speechText.contains("No")) || (speechText.contains("no"))){  //cantidad de dinero con el que piensa pagar
            chanceCardNumber =  partsSpeechText[partsSpeechText.length-1];
            return 7;
        }

        if((speechText.contains("apartar")) || (speechText.contains("Apartar"))){//Apartar articulos
            return 2;
        }

        else if (speechText.contains("Salir") || speechText.contains("salir") || speechText.contains("bye")){//Salir de la aplicación
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
            case 1:     //Buy a article command
                if(nameArticle.equalsIgnoreCase("Blusa")){
                    characteristic = "cuello alto";
                    speak("Tengo blusas de cuello alto, de color blanco, negro y azul. ¿De qué color la quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Gorra")){
                    characteristic = "deportiva";
                    speak("Tengo gorras deportivas de color blanco, negro y azul. ¿De qué color la quiere?");
                            while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Abrigo")){
                    characteristic = "deportivo";
                    speak("Tengo abrigos deportivos de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Vestido")){
                    characteristic = "largo";
                    speak("Tengo vestidos largos de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Pantalón")){
                    characteristic = "mezclilla";
                    speak("Tengo pantalones de mezclilla de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Camisa")){
                    characteristic = "manga corta";
                    speak("Tengo camisas de manga corta de color blanco, negro y azul. ¿De qué color la quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Short")){
                    characteristic = "mezclilla";
                    speak("Tengo shorts de mezclilla de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Medias")){
                    characteristic = "cortas";
                    speak("Tengo medias cortas de color blanco, negro y azul. ¿De qué color las quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Traje de baño")){
                    characteristic = "2";
                    speak("Tengo trajes de baño de dos piezas de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                if(nameArticle.equalsIgnoreCase("Camiseta")){
                    characteristic = "cuello v";
                    speak("Tengo camisetas de cuello V de color blanco, negro y azul. ¿De qué color la quiere?");
                    while (textToSpeech.isSpeaking()){

                    }
                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 2:     //Ofrece articulo con las especificaciones y pregunta cuantos desea
                art = searchArticle(nameArticle,color,characteristic);
                priceArt = art.getPrice();
                speak("Cada"+ nameArticle +"tiene un precio de" + art.getPrice()+ "contamos con"+art.getQuantity()+
                        "artículos en el inventario.Cuantós desea?");
                while (textToSpeech.isSpeaking()){

                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 3:     //Pregunta si va a pagar con tarjeta o con efectivo
                if(art.getQuantity() < cantArticles){
                    speak("No contamos con los artículos suficientes. Intente de nuevo");
                    while (textToSpeech.isSpeaking()){

                    }
                }else{
                    totalPay = cantArticles * priceArt;
                    speak("Su total a pagar es de" + totalPay + "colones. Paga con efectivo o con tarjeta");
                    while (textToSpeech.isSpeaking()){

                    }
                }

                recordSpeech();  //Speech to text: Client answer
                break;

            case 4:     //Pregunta con cuanto dinero va a pagar o si desea pagar con la tarjeta registrada
                totalPay = cantArticles * priceArt;
                if(paymentMethod.equalsIgnoreCase("efectivo")) {
                    speak("Con cuánto dinero va a pagar?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }else{
                    speak("Desea pagar con la tarjeta"+ clientActually.getCardNumber()+" o la desea cambiar");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 5:     //Paga en efectivo, y pregunta si se lo lleva o se le envía por encomienda
                totalPay = cantArticles * priceArt;
                int total = totalPay - payColones;
                if(action.equals("comprar"))
                    if(totalPay > payColones){
                        speak("El dinero no es suficiente. Intente de nuevo");
                        while (textToSpeech.isSpeaking()){

                        }
                    }else{
                        art.payWithCash(art,cantArticles,totalPay);
                        speak("Su vuelto es de " + total + "colones. Se lo lleva o se le envía por encomienda");
                        while (textToSpeech.isSpeaking()){

                        }
                    }

                recordSpeech();  //Speech to text: Client answer
                break;

            case 6:   //Enviar por encomienda o llevárselo
                if(delivery.equalsIgnoreCase("llevar")){
                    speak("Muchas gracias por su visita, esperamos verlo pronto.");
                    while (textToSpeech.isSpeaking()){

                    }
                }else{
                    speak("Muchas gracias por su visita, esperamos verlo pronto.");
                    while (textToSpeech.isSpeaking()){

                    }
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
        if (num.equals("uno")||num.equals("1"))
            return 1;
        if (num.equals("dos")||num.equals("2"))
            return 2;
        if (num.equals("tres")||num.equals("3"))
            return 3;
        if (num.equals("cuatro")||num.equals("4"))
            return 4;
        if (num.equals("cinco")||num.equals("5"))
            return 5;
        if (num.equals("seis")||num.equals("6"))
            return 6;
        if (num.equals("siete")||num.equals("7"))
            return 7;
        if (num.equals("ocho")||num.equals("8"))
            return 8;
        if (num.equals("nueve")||num.equals("9"))
            return 9;
        if (num.equals("diez")||num.equals("10"))
            return 10;
        if (num.equals("once")||num.equals("11"))
            return 11;
        if (num.equals("doce")||num.equals("12"))
            return 12;
        if (num.equals("trece")||num.equals("13"))
            return 13;
        if (num.equals("catorce")||num.equals("14"))
            return 14;
        if (num.equals("quince")||num.equals("15"))
            return 15;
        return 0;
    }

    public static String runInventory(){
        String invent="";
        for (Article a : inventory.getArticles()){
            invent+=a.getName()+" "+a.getColor()+ ": "+a.getQuantity()+"\n";
        }
        return invent;
    }

    public static void initialUsers() {
        Client sebas = new Client("Sebastian", "Rojas", "sebas", "1", "Pital", "123", 1, false); //true admin, false client
        clients.add(sebas);
        Client daya = new Client("Dayana", "Rojas", "daya", "123", "Pital", "123", 1, false); //client
        clients.add(daya);
        Client brian = new Client("Brayan", "Perez", "casa", "123", "Pital", "123", 1, false); //client
        clients.add(brian);
        Client huber = new Client("Huber", "Espinoza", "huberep", "123", "Santa Clara", "123", 1, true); //admin
        clients.add(huber);

        Inventory inventory= Inventory.getInstance();
        ArticleFactory factory = new ArticleFactory();

        factory.maker(1,111, 7000, "blusa", "azul", 10, "cuello alto");
        factory.maker(1,222, 8000, "blusa", "blanco", 10, "cuello alto");
        factory.maker(1,333, 8000, "blusa", "negro", 10, "cuello alto");

        factory.maker(2,444,2500, "gorra", "azul", 10, "deportiva");
        factory.maker(2,555,2000, "gorra", "blanco", 10, "deportiva");
        factory.maker(2,666,2500, "gorra", "negro", 10, "deportiva");

        factory.maker(3,777, 4000,"abrigo", "azul", 10, "deportivo");
        factory.maker(3,888, 4000,"abrigo", "blanco", 10, "deportivo");
        factory.maker(3,999, 4000,"abrigo", "negro", 10, "deportivo");

        factory.maker(4, 101, 7000, "vestido", "azul", 10,"largo");
        factory.maker(4,202, 7000, "vestido", "blanco", 10,"largo");
        factory.maker(4, 303, 7000, "vestido", "negro", 10,"largo");

        factory.maker(5, 404, 10000, "pantalón", "azul", 20, "mezquilla");
        factory.maker(5,505, 10000, "pantalón", "blanco", 20, "mezquilla");
        factory.maker(5,606, 10000, "pantalón", "negro", 20, "mezquilla");

        factory.maker(6,707, 5000, "camisa", "azul", 15, "manga corta");
        factory.maker(6,808, 5000, "camisa", "blanco", 15, "manga corta");
        factory.maker(6,909, 5000, "camisa", "negro", 15, "manga corta");

        factory.maker(7,110, 4000,"short", "azul", 5, "mezquilla");
        factory.maker(7,120, 4000,"short", "blanco", 5, "mezquilla");
        factory.maker(7,130, 4000,"short", "negro", 5, "mezquilla");

        factory.maker(8,140, 1000, "medias", "azul", 10,"cortas");
        factory.maker(8,150, 1000, "medias", "blanco", 10,"cortas");
        factory.maker(8,160, 1000, "medias", "negro", 10,"cortas");

        factory.maker(9,170, 6000, "traje de baño", "azul", 5, "2");
        factory.maker(9,180, 6000, "traje de baño", "blanco", 5, "2");
        factory.maker(9,190, 6000, "traje de baño", "negro", 5, "2");

        factory.maker(10,200, 5000, "camiseta", "azul", 10, "manga corta");
        factory.maker(10,200, 5000, "camiseta", "banco", 10, "manga corta");
        factory.maker(10,200, 5000, "camiseta", "negro", 10, "manga corta");

        inventoryInitial=runInventory();
    }

    public static Client login (String username, String password){
        for (Client c : MainActivity.clients){
            if (c.getUsername().equals(username)){

                if (c.getPasword().equals(password)){
                    return c;
                }
                return null;
            }
        }
        return null;
    }

    public static Article searchArticle (int ID){
        for (Article i : inventory.getArticles()){
            if (i.getID() == ID)
                return i;
        }
        return null;
    }
}
