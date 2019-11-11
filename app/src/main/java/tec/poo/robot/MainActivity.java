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

import Exceptions.AndroidException;

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
    public static String edited = "Artículos editados: \n";
    public static String deleted = "Artículos eliminados: \n";
    public static String reservated = "Reservaciones \n";
    public static String buyed = "Compras \n";


    String action;               //Si va a comprar, apartar, retitar un articulo
    String paymentMethod;        //Si paga en efectivo o con tarjeta
    String color;                //Guarda el color del articulo
    String nameArticle;          //Guarda el nombre del articulo
    String characteristic;       //Guarda la caracteristica del articulo
    String delivery;             //Guarda si por encomienda o si se lo lleva
    String chanceCardNumber;     //Guarda si el cliente desea cambiar la tarjeta
    String direcction = "";                      //Guarda la nueva dirección del cliente
    String zipCode;                        //Guarda el nuevo código postal del cliente
    int priceArt;                //Guarda el precio del articulo
    int cardNumber;              //Guarda el numero de tarjeta
    double totalPay;                //Guarda el monto total a pagar
    int cantArticles = 1;            //Guarda la cantidad de articulos que desea la parsona
    int payColones;              //Guarda la cantidad de dinero con el que se va a pagar
    Article art;
    int switchChange;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.date = new Date();
        this.email = "Artículos comprados \n";
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
        if((speechText.contains("apartar")) || (speechText.contains("Apartar"))){  //Apartar articulos
            action = partsSpeechText[partsSpeechText.length-3];
            nameArticle = partsSpeechText[partsSpeechText.length-1];
            return 1;
        }
        if((speechText.contains("retirar")) || (speechText.contains("Retirar"))){  //Retirar articulos
            action = partsSpeechText[partsSpeechText.length-3];
            nameArticle = partsSpeechText[partsSpeechText.length-1];
            return 2;
        }
        if((speechText.contains("consultar")) || (speechText.contains("Consultar"))){  //Consultar articulos apartados

            return 13;
        }
        if((speechText.contains("color")) || (speechText.contains("Color"))){  //Consultar articulos apartados
            color = partsSpeechText[partsSpeechText.length-1];
            return 3;
        }
        if((speechText.contains("Sí")) || (speechText.contains("sí"))||
                (speechText.contains("Si")) || (speechText.contains("si"))){
            if (switchChange == 1){
                return 4;
            }
            if (switchChange == 2){
                return 4;
            }
            if (switchChange == 3){
                chanceCardNumber =  partsSpeechText[partsSpeechText.length-1];
                return 8;
            }
            if (switchChange == 4){
                return 9;
            }
            if (switchChange == 5){
                return 9;
            }
            if (switchChange == 6){
                return 11;
            }

        }
        if((speechText.contains("No")) || (speechText.contains("no"))){
            if (switchChange == 1){
                return 11;
            }
            if (switchChange == 2){
                return 11;
            }
            if (switchChange == 3){
                cardNumber = clientActually.getCardNumber();
                chanceCardNumber =  partsSpeechText[partsSpeechText.length-1];
                return 8;
            }
            if (switchChange == 4){
                switchChange= 5;
                return 7;
            }
            if (switchChange == 5){
                direcction = clientActually.getDirection();
                return 10;
            }
            if (switchChange == 6){
                return 12;
            }

        }
        if((speechText.contains("quiero")) || (speechText.contains("Quiero"))){  //cantidad de articulos
            cantArticles = entero(partsSpeechText[partsSpeechText.length-1]);
            return 4;
        }
        if((speechText.contains("efectivo")) || (speechText.contains("Efectivo"))||
                (speechText.contains("tarjeta")) || (speechText.contains("Tarjeta"))){  //pagar con efectivo o con tarjeta
            paymentMethod = partsSpeechText[partsSpeechText.length-1];
            return 5;
        }
        if((speechText.contains("colones")) || (speechText.contains("Colones"))){  //cantidad de dinero con el que piensa pagar
            payColones =  Integer.parseInt(partsSpeechText[partsSpeechText.length-2]);
            return 6;
        }
        if((speechText.contains("llevo")) || (speechText.contains("Llevo"))||
                (speechText.contains("encomienda")) || (speechText.contains("Encomienda"))||
                (speechText.contains("llevar")) || (speechText.contains("Llevar"))){  //cantidad de dinero con el que piensa pagar
            delivery =  partsSpeechText[partsSpeechText.length-1];
            return 7;
        }
        if((speechText.contains("número")) || (speechText.contains("Número"))){  //cantidad de dinero con el que piensa pagar
            payColones =  Integer.parseInt(partsSpeechText[partsSpeechText.length-2]);
            return 6;
        }
        if((speechText.contains("código")) || (speechText.contains("Codigo"))) {  //Cambiar el código postal
            zipCode = partsSpeechText[partsSpeechText.length-1];
            switchChange = 5;
            return 7;
        }
        if((speechText.contains("dirección")) || (speechText.contains("Dirección"))) {  //Cambiar la dirección
            for (int i = 1; i < partsSpeechText.length; ++i) {
                direcction += partsSpeechText[i]+ " ";
            }
            switchChange = 0;
            return 10;
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
                conversation += "Hola! ¿En qué le puedo ayudar?";
                speak("Hola! ¿En qué le puedo ayudar?"); //Text to speech, robot speaking
                while (textToSpeech.isSpeaking()){

                }
                recordSpeech();   //Speech to text: Client answer
                break;
            case 1:     //Buy a article command
                if (nameArticle.equalsIgnoreCase("Blusa")) {
                    characteristic = "cuello alto";
                    conversation +="Contamos con blusas de cuello alto, de color blanco, negro y azul. ¿De qué color la quiere?";
                    speak("Contamos con blusas de cuello alto, de color blanco, negro y azul. ¿De qué color la quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Gorra")) {
                    characteristic = "deportiva";
                    conversation +="Contamos con gorras deportivas de color blanco, negro y azul. ¿De qué color la quiere?";
                    speak("Contamos con gorras deportivas de color blanco, negro y azul. ¿De qué color la quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Abrigo")) {
                    characteristic = "deportivo";
                    conversation +="Contamos con abrigos deportivos de color blanco, negro y azul. ¿De qué color lo quiere?";
                    speak("Contamos con abrigos deportivos de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Vestido")) {
                    characteristic = "largo";
                    conversation +="Contamos con vestidos largos de color blanco, negro y azul. ¿De qué color lo quiere?";
                    speak("Contamos con vestidos largos de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Pantalón")) {
                    characteristic = "mezclilla";
                    conversation +="Contamos con pantalones de mezclilla de color blanco, negro y azul. ¿De qué color lo quiere?";
                    speak("Contamos con pantalones de mezclilla de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Camisa")) {
                    characteristic = "manga corta";
                    conversation +="Contamos con camisas de manga corta de color blanco, negro y azul. ¿De qué color la quiere?";
                    speak("Contamos con camisas de manga corta de color blanco, negro y azul. ¿De qué color la quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Short")) {
                    characteristic = "mezclilla";
                    conversation +="Contamos con shorts de mezclilla de color blanco, negro y azul. ¿De qué color lo quiere?";
                    speak("Contamos con shorts de mezclilla de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Medias")) {
                    characteristic = "cortas";
                    conversation +="Contamos con medias cortas de color blanco, negro y azul. ¿De qué color las quiere?";
                    speak("Contamos con medias cortas de color blanco, negro y azul. ¿De qué color las quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Traje de baño")) {
                    characteristic = "2";
                    conversation +="Contamos con trajes de baño de dos piezas de color blanco, negro y azul. ¿De qué color lo quiere?";
                    speak("Contamos con trajes de baño de dos piezas de color blanco, negro y azul. ¿De qué color lo quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                if (nameArticle.equalsIgnoreCase("Camiseta")) {
                    characteristic = "cuello v";
                    conversation +="Contamos con camisetas de cuello V de color blanco, negro y azul. ¿De qué color la quiere?";
                    speak("Contamos con camisetas de cuello V de color blanco, negro y azul. ¿De qué color la quiere?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }

                recordSpeech();   //Speech to text: Client answer
                break;
            case 2:     //Retirar artículo apartado
                if (nameArticle.equalsIgnoreCase("Blusa")) {
                    characteristic = "cuello alto";
                }
                if (nameArticle.equalsIgnoreCase("Gorra")) {
                    characteristic = "deportiva";
                }
                if (nameArticle.equalsIgnoreCase("Abrigo")) {
                    characteristic = "deportivo";
                }
                if (nameArticle.equalsIgnoreCase("Vestido")) {
                    characteristic = "largo";
                }
                if (nameArticle.equalsIgnoreCase("Pantalón")) {
                    characteristic = "mezclilla";
                }
                if (nameArticle.equalsIgnoreCase("Camisa")) {
                    characteristic = "manga corta";
                }
                if (nameArticle.equalsIgnoreCase("Short")) {
                    characteristic = "mezclilla";
                }
                if (nameArticle.equalsIgnoreCase("Medias")) {
                    characteristic = "cortas";
                }
                if (nameArticle.equalsIgnoreCase("Traje de baño")) {
                    characteristic = "2";
                }
                if (nameArticle.equalsIgnoreCase("Camiseta")) {
                    characteristic = "cuello v";
                }
                conversation+= "De qué color es el artículo apartado?";
                speak("De qué color es el artículo apartado?");
                while (textToSpeech.isSpeaking()){

                }
                recordSpeech();   //Speech to text: Client answer
                break;

            case 3:     //Ofrece articulo con las especificaciones y pregunta cuantos desea
                art = searchArticle(nameArticle,color,characteristic);

                if(action.equalsIgnoreCase("comprar")){
                    priceArt = art.getPrice();
                    conversation+= "Cada"+ nameArticle +"tiene un precio de" + art.getPrice()+ "contamos con"+art.getQuantity()+
                            "artículos en el inventario. Cuantós desea?";
                    speak("Cada"+ nameArticle +"tiene un precio de" + art.getPrice()+ "contamos con"+art.getQuantity()+
                            "artículos en el inventario. Cuantós desea?");
                    while (textToSpeech.isSpeaking()){

                    }
                }else if (action.equalsIgnoreCase("apartar")){
                    priceArt = art.getPrice();
                    switchChange = 1;
                    conversation+= "Cada"+ nameArticle +"tiene un precio de" + art.getPrice()+ "contamos con"+art.getQuantity()+
                            "artículos en el inventario. Desea apartarlo?";
                    speak("Cada"+ nameArticle +"tiene un precio de" + art.getPrice()+ "contamos con"+art.getQuantity()+
                            "artículos en el inventario. Desea apartarlo?");
                    while (textToSpeech.isSpeaking()){

                    }
                }else{
                    if (searchCustomerItem(art) == true){
                        priceArt = art.getPrice();
                        switchChange = 2;
                        conversation+= "Desea retirar el artíulo?";
                        speak("Desea retirar el artíulo?");
                        while (textToSpeech.isSpeaking()){

                        }
                    }else{
                        switchChange = 2;
                        conversation+= "No cuenta con artículos con las especificaciones brindadas. Desea realizar algo más?";
                        speak("No cuenta con artículos con las especificaciones brindadas. Desea realizar algo más?");
                        while (textToSpeech.isSpeaking()){

                        }
                    }
                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 4:
                if(art.getQuantity() < cantArticles){
                    conversation+= "No contamos con los artículos suficientes. Intente de nuevo";
                    speak("No contamos con los artículos suficientes. Intente de nuevo");
                    while (textToSpeech.isSpeaking()){

                    }
                }else{
                    if (action.equalsIgnoreCase("comprar")){
                        totalPay = cantArticles * priceArt;
                        conversation+= "Su total a pagar es de" + totalPay + "colones. Paga con efectivo o con tarjeta";
                        speak("Su total a pagar es de" + totalPay + "colones. Paga con efectivo o con tarjeta");
                        while (textToSpeech.isSpeaking()){

                        }
                    }if (action.equalsIgnoreCase("apartar")){
                        totalPay =  priceArt *0.15;
                        conversation+= "Para poder apartarlo tiene que pagar el 15% del valor del artículo, " +
                                "en su caso serían"+ totalPay +" colones ¿Paga en efectivo o tarjeta?";
                        speak("Para poder apartarlo tiene que pagar el 15% del valor del artículo, " +
                                "en su caso serían"+ totalPay +" colones ¿Paga en efectivo o tarjeta?");
                        while (textToSpeech.isSpeaking()){

                        }
                    }if (action.equalsIgnoreCase("retirar")) {
                        totalPay = priceArt*0.85;
                        conversation+= "Su total a pagar es de" + totalPay + "colones. Paga con efectivo o con tarjeta";
                        speak("Su total a pagar es de" + totalPay + "colones. Paga con efectivo o con tarjeta");
                        while (textToSpeech.isSpeaking()) {

                        }
                    }
                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 5:     //Pregunta con cuanto dinero va a pagar o si desea pagar con la tarjeta registrada
                if(paymentMethod.equalsIgnoreCase("efectivo")) {
                    conversation+= "Con cuánto dinero va a pagar?";
                    speak("Con cuánto dinero va a pagar?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }else{
                    switchChange = 3;
                    conversation+= "Desea pagar con la tarjeta"+ clientActually.getCardNumber()+" o la desea cambiar";
                    speak("Desea pagar con la tarjeta"+ clientActually.getCardNumber()+" o la desea cambiar");
                    while (textToSpeech.isSpeaking()) {

                    }
                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 6:     //Paga en efectivo, y pregunta si se lo lleva o se le envía por encomienda

                double total = payColones - totalPay;
                if(action.equals("comprar")||action.equalsIgnoreCase("retirar")) {

                    if (totalPay > payColones) {
                        conversation+= "El dinero no es suficiente. Intente de nuevo";
                        speak("El dinero no es suficiente. Intente de nuevo");
                        while (textToSpeech.isSpeaking()) {

                        }
                    } else {
                        if (action.equalsIgnoreCase("retirar")){
                            takeOutCustomerItem(art);
                        }
                        art.payWithCash(art, cantArticles, totalPay);
                        conversation+= "Su vuelto es de " + total + "colones. Se lo lleva o se le envía por encomienda";
                        speak("Su vuelto es de " + total + "colones. Se lo lleva o se le envía por encomienda");
                        while (textToSpeech.isSpeaking()) {

                        }
                    }
                }else{
                    if (totalPay > payColones) {
                        conversation+= "El dinero no es suficiente. Intente de nuevo";
                        speak("El dinero no es suficiente. Intente de nuevo");
                        while (textToSpeech.isSpeaking()) {

                        }
                    } else {
                        art.reserveArticleWithCash(art, cantArticles,priceArt);
                        clientActually.setArticles(art);
                        conversation+= "Su vuelto es de " + total + "colones. muchas gracias por su visita, estamos para servirle, esperamos verle pronto ";
                        speak("Su vuelto es de " + total + "colones. muchas gracias por su visita, estamos para servirle, esperamos verle pronto ");
                        while (textToSpeech.isSpeaking()) {

                        }
                    }
                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 7:   //Enviar por encomienda o llevárselo
                if(action.equals("comprar")||action.equalsIgnoreCase("retirar")) {
                    if (delivery.equalsIgnoreCase("llevar") || delivery.equalsIgnoreCase("llevo")) {
                        conversation+= "Pondremos su artículo en una bolsa de papel, muchas gracias por su visita, estamos para servirle, esperamos verle pronto";
                        speak("Pondremos su artículo en una bolsa de papel, muchas gracias por su visita, estamos para servirle, esperamos verle pronto");
                        while (textToSpeech.isSpeaking()) {

                        }
                    } else {
                        if (switchChange == 5){
                            conversation+= "Desea cambiar su dirección" + clientActually.getDirection();
                            speak("Desea cambiar su dirección" + clientActually.getDirection());
                            while (textToSpeech.isSpeaking()) {

                            }
                        }else{
                            switchChange = 4;
                            conversation+= "Desea cambiar su código postal" + clientActually.getPostalApart();
                            speak("Desea cambiar su código postal" + clientActually.getPostalApart());
                            while (textToSpeech.isSpeaking()) {

                            }
                        }
                    }
                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 8:
                if (action.equalsIgnoreCase("apartar")){
                    conversation+= "muchas gracias por su visita, estamos para servirle, esperamos verle pronto";
                    speak("muchas gracias por su visita, estamos para servirle, esperamos verle pronto");
                    while (textToSpeech.isSpeaking()) {

                    }
                }else{
                    if (chanceCardNumber.equalsIgnoreCase("no")){
                        art.payWithCard(art,cantArticles,cardNumber,clientActually.getName(),clientActually.getLastName());
                        clientActually.setArticles(art);
                        conversation+= "Para llevar o se le envía por encomienda?";
                        speak("Para llevar o se le envía por encomienda?");
                        while (textToSpeech.isSpeaking()) {

                        }
                    }else{
                        chanceCardNumber = "no";
                        conversation+= "Cuál es su nuevo número de tarjeta?";
                        speak("Cuál es su nuevo número de tarjeta?");
                        while (textToSpeech.isSpeaking()) {

                        }
                    }
                }

                recordSpeech();  //Speech to text: Client answer
                break;
            case 9:
                if (switchChange == 4){
                    conversation+= "Cuál es su nuevo código postal?";
                    speak("Cuál es su nuevo código postal?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }else{
                    conversation+= "Cuál es su nueva dirección?";
                    speak("Cuál es su nueva dirección?");
                    while (textToSpeech.isSpeaking()) {

                    }
                }

                recordSpeech();  //Speech to text: Client answer
                break;

            case 10:
                conversation+= "Su paquete será enviado a su dirección"+direcction+" Muchas gracias por su visita. Si desea comprar otro artículo, estamos para servirle";
                speak("Su paquete será enviado a su dirección"+direcction+" Muchas gracias por su visita. Si desea comprar otro artículo, estamos para servirle");
                while (textToSpeech.isSpeaking()) {

                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 11:
                conversation+= "Qué más desea realizar?";
                speak("Qué más desea realizar?");
                while (textToSpeech.isSpeaking()) {

                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 12:
                conversation+= "Hasta la próxima";
                speak("Hasta la próxima");
                while (textToSpeech.isSpeaking()) {

                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 13:
                conversation+= "Tiene los siguientes artículos apartados";
                speak("Tiene los siguientes artículos apartados");
                while (textToSpeech.isSpeaking()) {

                }
                for(Client c : MainActivity.clients){
                    if(c.getUsername().equals(clientActually.getUsername())) {
                        for (Article a : c.getArticles()) {
                            conversation+= a.getName()+"de color"+a.getColor();
                            speak(a.getName()+"de color"+a.getColor());
                            while (textToSpeech.isSpeaking()) {

                            }
                        }
                    }
                }
                switchChange = 6;
                conversation+= "Desea realizar algo más";
                speak("Desea realizar algo más");
                while (textToSpeech.isSpeaking()) {

                }
                recordSpeech();  //Speech to text: Client answer
                break;

            case 100:   //Saying good bye command
                conversation+= "Adiós, fue un placer ayudarte";
                speak("Adiós, fue un placer ayudarte");
                while (textToSpeech.isSpeaking()){

                }
                inventoryFinal+=runInventory();
                email += inventoryInitial + conversation + buyed + reservated + edited+ deleted + inventoryFinal;

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
                conversation+= "No he logrado entender, por favor, me puede explicar de nuevo";
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
    public boolean searchCustomerItem(Article article){
        for(Client c : MainActivity.clients){
            if(c.getUsername().equals(clientActually.getUsername())){
                for(Article a : c.getArticles()){
                    if(a == article){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean takeOutCustomerItem(Article article){
        for(Client c : MainActivity.clients){
            if(c.getUsername().equals(clientActually.getUsername())){
                for(Article a : c.getArticles()){
                    if(a == article){
                        c.getArticles().remove(a);
                        return true;
                    }
                }
            }
        }
        return false;
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
        return 1;
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

        try {
            factory.maker(1, 111, 7000, "blusa", "azul", 10, "cuello alto");
            factory.maker(1, 222, 8000, "blusa", "blanco", 10, "cuello alto");
            factory.maker(1, 333, 8000, "blusa", "negro", 10, "cuello alto");

            factory.maker(2, 444, 2500, "gorra", "azul", 10, "deportiva");
            factory.maker(2, 555, 2000, "gorra", "blanco", 10, "deportiva");
            factory.maker(2, 666, 2500, "gorra", "negro", 10, "deportiva");

            factory.maker(3, 777, 4000, "abrigo", "azul", 10, "deportivo");
            factory.maker(3, 888, 4000, "abrigo", "blanco", 10, "deportivo");
            factory.maker(3, 999, 4000, "abrigo", "negro", 10, "deportivo");

            factory.maker(4, 101, 7000, "vestido", "azul", 10, "largo");
            factory.maker(4, 202, 7000, "vestido", "blanco", 10, "largo");
            factory.maker(4, 303, 7000, "vestido", "negro", 10, "largo");

            factory.maker(5, 404, 10000, "pantalón", "azul", 20, "mezquilla");
            factory.maker(5, 505, 10000, "pantalón", "blanco", 20, "mezquilla");
            factory.maker(5, 606, 10000, "pantalón", "negro", 20, "mezquilla");

            factory.maker(6, 707, 5000, "camisa", "azul", 15, "manga corta");
            factory.maker(6, 808, 5000, "camisa", "blanco", 15, "manga corta");
            factory.maker(6, 909, 5000, "camisa", "negro", 15, "manga corta");

            factory.maker(7, 110, 4000, "short", "azul", 5, "mezquilla");
            factory.maker(7, 120, 4000, "short", "blanco", 5, "mezquilla");
            factory.maker(7, 130, 4000, "short", "negro", 5, "mezquilla");

            factory.maker(8, 140, 1000, "medias", "azul", 10, "cortas");
            factory.maker(8, 150, 1000, "medias", "blanco", 10, "cortas");
            factory.maker(8, 160, 1000, "medias", "negro", 10, "cortas");

            factory.maker(9, 170, 6000, "traje de baño", "azul", 5, "2");
            factory.maker(9, 180, 6000, "traje de baño", "blanco", 5, "2");
            factory.maker(9, 190, 6000, "traje de baño", "negro", 5, "2");

            factory.maker(10, 200, 5000, "camiseta", "azul", 10, "manga corta");
            factory.maker(10, 200, 5000, "camiseta", "banco", 10, "manga corta");
            factory.maker(10, 200, 5000, "camiseta", "negro", 10, "manga corta");

        }catch (AndroidException ex){
            //no se puede hacer el toast aquí
        }
        inventoryInitial+=runInventory();
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
