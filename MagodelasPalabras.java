package homework;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MagodelasPalabras {
    //Atributos
    private HashMap<String, Integer> wordBank; //Banco con todas las palabras disponibles
    private HashSet<String> foundWords; //Guarda las palabras ya usadas
    private HashSet<String> lettersSet; //Set de letras usables por ronda
    private ArrayList<PalabraUsada> foundWordsData; //Guardar palabra, puntaje y jugador que lo ingreso

    //Atributos de lo grafico
    JFrame startWindow;
    JFrame gameWindow;

    private int playersAmount; //Cantidad de jugadores en juego
    private HashMap<Integer, Integer> playerPoints; //Puntos de los 2 a 4 jugadores

    private Scanner reader; //Leer por teclado

    //Iniciarlizar juego
    public MagodelasPalabras(){
        //Inicializar Atributos
        playersAmount = 0;

        playerPoints = new HashMap<>();
        foundWords = new HashSet<>();
        lettersSet = new HashSet<>();
        foundWordsData = new ArrayList<>();

        reader = new Scanner(System.in);

        //Crear banco de palabras
        BancodePalabras bank = new BancodePalabras();
        wordBank = bank.generateBank();

        window();
        /*
        //Pedir numero de jugadores
        playersAmount = setPlayersAmount();
        System.out.println("\n");

        //Crear jugadores
        for(int i = 1; i <= playersAmount; i++){
            playerPoints.put(i, 0);
        }

        //Pedir modo de juego
        boolean gameMode = setGameMode();
        System.out.println("\n");

        //Jugar
        wordsWizardGame(gameMode);

        //Mostrar estadisticas
        showGameResults();

         */
    }

    public void window(){
        startWindow = new JFrame("El mago de las palabras"); //crea la ventana
        startWindow.setSize(720,500); //poner el tamaño de la ventana
        startWindow.setLayout(null); //deja acomodar los objetos libremente en la ventana
        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //termina la ejecucion de la ventana al cerrarlo


        Container contentPane = new Container();
        contentPane=startWindow.getContentPane(); //mueve libremente los objetos
        contentPane.setBackground(Color.WHITE); //Pone el color del fondo

        //Letrero para elegir jugadores
        JLabel selectPlayersLabel = new JLabel("Seleccionar cantidad de jugadores:");
        selectPlayersLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        selectPlayersLabel.setBounds((startWindow.getWidth()-selectPlayersLabel.getPreferredSize().width)/2, (startWindow.getHeight())/9, selectPlayersLabel.getPreferredSize().width, selectPlayersLabel.getPreferredSize().height);
        startWindow.add(selectPlayersLabel);
        selectPlayersLabel.setVisible(true);

        //para el boton de seleccion "Cantidad de jugadores"
        JComboBox playerSelection = new JComboBox<>();
        playerSelection.setBackground(Color.LIGHT_GRAY); //establece el color del fondo
        playerSelection.setBorder(new LineBorder(Color.BLACK, 3)); //establece el contorno del objeto
        playerSelection.setFont(new Font("Comic Sans MS", Font.BOLD, 20)); //establece fuente del texto
        playerSelection.setBounds((startWindow.getWidth()-playerSelection.getPreferredSize().width)/2, startWindow.getHeight()/4, playerSelection.getPreferredSize().width+playerSelection.getPreferredSize().width/4,playerSelection.getPreferredSize().height); //acomoda el objeto en la ventana
        playerSelection.addItem(2);
        playerSelection.addItem(3); //agrega elementos a la seleccion de personaje
        playerSelection.addItem(4);
        playerSelection.setVisible(true); //hace visible el objeto en la ventana
        startWindow.add(playerSelection); //agrega el elemento a la ventana

        //Letrero para elegir dificultad
        JLabel selectDifficultLabel = new JLabel("Selecciona una dificultad:");
        selectDifficultLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        selectDifficultLabel.setBounds((startWindow.getWidth()-selectDifficultLabel.getPreferredSize().width)/2,(startWindow.getHeight())/2,selectDifficultLabel.getPreferredSize().width+selectDifficultLabel.getPreferredSize().width,selectDifficultLabel.getPreferredSize().height);
        startWindow.add(selectDifficultLabel);
        selectDifficultLabel.setVisible(true);

        //Botones para elegir la dificultad
        JRadioButton regularDifficult = new JRadioButton("Dificultad Regular");
        regularDifficult.setVisible(true);
        regularDifficult.setBackground(Color.WHITE);
        startWindow.add(regularDifficult);
        regularDifficult.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        regularDifficult.setBounds((startWindow.getWidth()-regularDifficult.getPreferredSize().width*3)/2,(startWindow.getHeight()/5)*3,regularDifficult.getPreferredSize().width,regularDifficult.getPreferredSize().height);

        JRadioButton expertDifficult = new JRadioButton("Dificultad Experto");
        expertDifficult.setVisible(true);
        expertDifficult.setBackground(Color.WHITE);
        startWindow.add(expertDifficult);
        expertDifficult.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        expertDifficult.setBounds((startWindow.getWidth()+expertDifficult.getPreferredSize().width)/2,(startWindow.getHeight()/5)*3,expertDifficult.getPreferredSize().width,expertDifficult.getPreferredSize().height);

        ButtonGroup difficult = new ButtonGroup();
        difficult.add(regularDifficult);
        difficult.add(expertDifficult);

        //boton para las reglas
        JButton rules = new JButton("Reglas");
        rules.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        rules.setBackground(Color.LIGHT_GRAY);
        rules.setBorder(new LineBorder(Color.BLACK, 3));
        rules.setBounds((startWindow.getWidth()-rules.getPreferredSize().width*7)/2,(startWindow.getHeight()/7)*5,rules.getPreferredSize().width+rules.getPreferredSize().width/5,rules.getPreferredSize().height);
        startWindow.add(rules);
        rules.setVisible(true);
        rules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){rulesWindows();}
        });

        //boton de error
        JLabel errorMessage =  new JLabel("No se seleccionó una dificultad");
        errorMessage.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        errorMessage.setBounds((startWindow.getWidth()-errorMessage.getPreferredSize().width)/2,(startWindow.getHeight()/7)*6,errorMessage.getPreferredSize().width+errorMessage.getPreferredSize().width, errorMessage.getPreferredSize().height);
        startWindow.add(errorMessage);
        errorMessage.setVisible(false);

        //boton de jugar
        JButton play = new JButton("Jugar");
        play.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        play.setBackground(Color.LIGHT_GRAY);
        play.setBorder(new LineBorder(Color.BLACK, 3));
        play.setBounds((startWindow.getWidth()+play.getPreferredSize().width*6)/2,(startWindow.getHeight()/7)*5,play.getPreferredSize().width+play.getPreferredSize().width/5,play.getPreferredSize().height);
        startWindow.add(play);
        play.setVisible(true);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                int amountPlayer = (Integer) playerSelection.getSelectedItem();
                boolean difficult = true;
                if(regularDifficult.isSelected()){
                    difficult = true;
                    gameWindow(difficult, amountPlayer);
                    startWindow.dispose();
                }
                if(expertDifficult.isSelected()){
                    difficult = false;
                    gameWindow(difficult, amountPlayer);
                    startWindow.dispose();
                }
                if(regularDifficult.isSelected() == false && expertDifficult.isSelected() == false){
                    errorMessage.setVisible(true);
                }
            }
        });

        startWindow.setResizable(false);
        startWindow.setVisible(true);
    }

    //mostrar las reglas/instrucciones en ventana
    public void rulesWindows(){
        JFrame rulesWindow = new JFrame("Reglas del juego");
        rulesWindow.setSize(500,500);

        //aqui va el content

        rulesWindow.setResizable(false);
        rulesWindow.setVisible(true);
    }

    public void gameWindow(boolean difficultGame, int  amountPlayer){
        int letters=0;
        if(difficultGame){
            letters=10;
        }else{
            letters=7;
        }

        ArrayList<JLabel> playerPoints;

        gameWindow = new JFrame("Reglas del juego");
        gameWindow.setSize(1080,720);
        gameWindow.setLayout(null);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = new Container();
        container=gameWindow.getContentPane();
        container.setBackground(Color.WHITE);


        //letrero de ronda
        JLabel round = new  JLabel("Ronda n");
        round.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        round.setBounds((gameWindow.getWidth())/50,(gameWindow.getHeight())/55,round.getPreferredSize().width,round.getPreferredSize().height);
        round.setVisible(true);
        gameWindow.add(round);

        //crear numero de puntos
        for(int i = 0; i < amountPlayer; i++){
            //listado de jugadores con sus puntos
            JLabel player = new  JLabel("Player  " +(i+1) +"  Puntos: 0");
            player.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            player.setBounds((gameWindow.getWidth())/50, (gameWindow.getHeight()/45) + ((i+1)*40),player.getPreferredSize().width,player.getPreferredSize().height);
            gameWindow.add(player);
            player.setVisible(true);
        }



        //crear cuadros de letras
        for(int i =0; i< letters; i++){
            JButton letter = new JButton("A");
            letter.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            letter.setBackground(Color.LIGHT_GRAY);
            letter.setBorder(new LineBorder(Color.BLACK, 3));
            letter.setBounds((gameWindow.getWidth())/3 + ((i+1) * 60),(gameWindow.getHeight())/2+(gameWindow.getHeight())/6,letter.getPreferredSize().width+letter.getPreferredSize().width/4,letter.getPreferredSize().height);
            gameWindow.add(letter);
            letter.setVisible(true);
        }

        //boton para pasar turno
        JButton pass = new JButton("Pasar turno");
        pass.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        pass.setBackground(Color.LIGHT_GRAY);
        pass.setBorder(new LineBorder(Color.BLACK, 3));
        if(difficultGame) {
            pass.setBounds((gameWindow.getWidth() + pass.getPreferredSize().width * 24/5) / 2, (gameWindow.getHeight()) / 2 + (gameWindow.getHeight()) / 3, pass.getPreferredSize().width, pass.getPreferredSize().height);
        }else{
            pass.setBounds((gameWindow.getWidth() + pass.getPreferredSize().width *25/9 ) / 2, (gameWindow.getHeight()) / 2 + (gameWindow.getHeight()) / 3, pass.getPreferredSize().width, pass.getPreferredSize().height);

        }
        pass.setVisible(true);
        gameWindow.add(pass);

        //boton para ingresar palabra
        JButton setWord = new JButton("Ingresar palabra");
        setWord.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        setWord.setBackground(Color.LIGHT_GRAY);
        setWord.setBorder(new LineBorder(Color.BLACK, 3));
        if(difficultGame) {
            setWord.setBounds((gameWindow.getWidth() - pass.getPreferredSize().width) / 2, (gameWindow.getHeight()) / 2 + (gameWindow.getHeight()) / 3, setWord.getPreferredSize().width, setWord.getPreferredSize().height);
        }else{
            setWord.setBounds((gameWindow.getWidth()-pass.getPreferredSize().width*2)/2,(gameWindow.getHeight())/2+(gameWindow.getHeight())/3,setWord.getPreferredSize().width,setWord.getPreferredSize().height);

        }
        setWord.setVisible(true);
        gameWindow.add(setWord);

        //boton para agregar al diccionario
        JButton addToDictionary = new JButton("Agregar al diccionario");
        addToDictionary.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        addToDictionary.setBackground(Color.LIGHT_GRAY);
        addToDictionary.setBorder(new LineBorder(Color.BLACK, 3));
        addToDictionary.setBounds((gameWindow.getWidth()/2 + gameWindow.getWidth()/4),(gameWindow.getHeight()-addToDictionary.getPreferredSize().height)/2,addToDictionary.getPreferredSize().width,addToDictionary.getPreferredSize().height);
        addToDictionary.setVisible(true);
        gameWindow.add(addToDictionary);

        //Mostrar la palabra que se esta escribiendo
        JLabel showWord = new JLabel("palabra");
        showWord.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        showWord.setBackground(Color.LIGHT_GRAY);
        showWord.setBorder(new LineBorder(Color.BLACK, 3));
        showWord.setBounds((gameWindow.getWidth()-showWord.getPreferredSize().width*3/2)/2, (gameWindow.getHeight()/2-gameWindow.getHeight()/3), showWord.getPreferredSize().width+showWord.getPreferredSize().width+150,showWord.getPreferredSize().height);
        showWord.setVisible(true);
        gameWindow.add(showWord);

        //Letrero central
        JLabel mainSign = new JLabel("El jugador N fue el ganador!!!!");
        mainSign.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        mainSign.setBounds((gameWindow.getWidth()-mainSign.getPreferredSize().width*3/8)/2,(gameWindow.getHeight()*5)/16,mainSign.getPreferredSize().width+100,mainSign.getPreferredSize().height);
        mainSign.setVisible(true);
        gameWindow.add(mainSign);

        //Letrero turno del jugador N
        JLabel playerTurn = new JLabel("Es turno del jugador N");
        playerTurn.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        playerTurn.setBounds((gameWindow.getWidth()-playerTurn.getPreferredSize().width/4)/2, (gameWindow.getHeight()/15),playerTurn.getPreferredSize().width,playerTurn.getPreferredSize().height);
        playerTurn.setVisible(true);
        gameWindow.add(playerTurn);

        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
    }


    //metodo para agregar la palabra al diccionario
    public void addWordToDictionary(){

    }

    //boton para borrar
    public void deleteButtom(){

    }


    //Pedir numero de jugadores
    public int setPlayersAmount(){
        int players = 0;

        System.out.println("+) Ingresa número de jugadores (entre 2 y 4):");
        do{
            players = reader.nextInt();

            if((players < 2) || (players > 4)){
                System.err.println("Número de jugadores invalido, intenta de nuevo: ");
            }
        }while((players < 2) || (players > 4));

        return players;
    }

    //Pedir modo de juego
    public boolean setGameMode(){
        int mode = 0;

        System.out.println("+) Reglas de puntuación: ");
        System.out.println("   -) Las vocales otorgan 5 puntos.");
        System.out.println("   -) Las consonantes otorgan 3 puntos.");
        System.out.println("   -) Se agregan los puntos si la palabra ingresada es una palabra valida y cumple con las letras ofrecidas.");
        System.out.println("   -) Por su contraparte, el jugador pierde 5 puntos al ingresar una palabra no valida.");
        System.out.println("+) Modos de juego: ");
        System.out.println("   -) Regular: Los jugadores podran ingresar tantas palabras como quieran  en un turno.");
        System.out.println("               Ellos mismos pasan turno.");
        System.out.println("               A cada jugador se le da un set de 10 palabras.");
        System.out.println("               Hay 3 vocales garantizadas.");
        System.out.println("   -) Experto: Los jugadores solo podran poner una palabra en su turno.");
        System.out.println("               Cada jugador contara con un set de 7 letras para crear la palabra.");
        System.out.println("               Hay 2 vocales garantizadas.");

        System.out.println("\n");

        System.out.println("+) Ingresa Modo de juego: ");
        System.out.println("   1. Regular.");
        System.out.println("   2. Experto.");
        do{
            mode = reader.nextInt();

            if((mode != 1) && (mode != 2)){
                System.err.println("Modo de juego invalido, intenta de nuevo.");
            }
        }while((mode != 1) && (mode != 2));

        if(mode == 1){
            return true;
        }else{
            return false;
        }
    }

    //Juego completo
    public void wordsWizardGame(boolean gameMode){
        boolean gameRule = gameMode; //si es falso, es solo una palabra por ronda, si es true son hasta que pase turno
        boolean skipTurn;
        int skipCounter = 0; //Cuenta cuantos jugadores han pasado turno
        int playerTurn = 0;
        int playerAction=1; //guarda la accion que el jugador desea realizar
        String word = ""; //guarda la palabra del jugador

        //Control de rondas
        for(int i = 1; i <= 3; i++){
            //Reiniciar vairables y listas
            playerTurn = 0;
            skipCounter = 0;
            foundWords.clear();
            foundWordsData.clear();

            //Titulo de ronda
            System.out.println("( -------------------- RONDA " + i + " -------------------- )");

            //Generar set de palabras al inicio del turno del jugador
            SetdeLetras set = new SetdeLetras();

            if(gameRule){ //si es modo regular crea un set de 10 letras, si es experto lo crea de 7
                lettersSet = set.generateSet(10);
            }else {
                lettersSet = set.generateSet(7);
            }

            //Control de jugadores
            do{
                //Incrementar control de jugadores
                if(playerTurn < playersAmount){
                    playerTurn++;
                }else{
                    playerTurn = 1;
                }

                    //Mostrar Puntos de jugadores
                    System.out.println("+) Puntos de jugadores: ");
                    playerPoints.forEach((k, v) -> {
                        System.out.println("   -) Jugador " + k + ": " + v + " Puntos.");
                    });

                    //Mostrar letras usadas
                    Iterator<PalabraUsada> foundWordsDataIterator = foundWordsData.iterator();
                    System.out.println("+) Palabras usadas: ");
                    while(foundWordsDataIterator.hasNext()){
                        System.out.println("   -) " + foundWordsDataIterator.next().toString());
                    }
                    System.out.println("\n");

                    //Mostrar turno de jugador
                    System.out.println("+) Turno de Jugador " + playerTurn + " (Puntos = " + playerPoints.get(playerTurn) + "): ");
                    System.out.println("   -) Letras disponibles:");

                    //Mostrar letras de turno
                    Iterator<String> lettersIterator = lettersSet.iterator();
                    while (lettersIterator.hasNext()) {
                        System.out.println(lettersIterator.next());
                    }

                    if(gameRule){ //si es de mas de una ronda, entonces le pregunta al jugador que desea realizar
                        System.out.println("Que desea realizar?");
                        System.out.println("[1] Ingresar una palabra ");
                        System.out.println("[2] Pasar Turno ");
                        do {
                            playerAction = reader.nextInt();

                            if ((playerAction != 1) && (playerAction != 2)) {
                                System.err.println("Modo de juego invalido, intenta de nuevo.");
                            }
                        } while ((playerAction != 1) && (playerAction != 2));
                    }

                    reader = new Scanner(System.in);
                    switch(playerAction){
                        case 1:
                            //Incrementar si esta elegido el modo experto
                            if(gameRule == false){
                                skipCounter++;
                            }else{
                                //Reiniciar contador de turnos
                                skipCounter = 0;
                            }

                            //Ingresar palabras
                            System.out.println("Ingrese la palabra: ");
                            word = reader.nextLine();

                            //Verificar que la palabra ingresada tenga las palabras del HashSet
                            if(verifyLetters(word)){
                                //Encontrar y dar los puntos del valor de la palabra al jugador si no ha sido colocado
                                findWord(word, playerTurn);
                            }else{
                                //Mensaje de letras no permitidad y Eliminar 5 puntos
                                System.out.println("-) La palabra tiene letras no asignadas, -5 Puntos.");
                                reduceFivePoints(playerTurn);
                            }
                            break;
                        case 2:
                            skipCounter++;
                            break;
                        default:
                            break;
                    }

                    System.out.println("\n");

            }while(skipCounter != playersAmount);
        }
    }

    //Metodo para darle los puntos de la palabra al jugador
    public void findWord(String word, int playerTurn){
        //Verificar si ya ha sido ingresado antes
        if(foundWords.contains(word)){
            System.out.println("+) " + word + " ya ha sido usada antes.");

        }else{
            if(wordBank.containsKey(word)) { //si el banco de palabras tiene la palabra entonces obtiene su puntuacion y la suma al jugador
                //Guardar puntos
                Integer points = wordBank.get(word);

                //Actualizar los puntos
                playerPoints.put(playerTurn, playerPoints.get(playerTurn) + points);

                //Mensaje
                System.out.println("Puntos obtenidos por la palabra: " + points);

                //Agregar datos de la palabra
                PalabraUsada usedWord = new PalabraUsada(word, points, playerTurn);
                foundWordsData.add(usedWord);
            }else{
                //Eliminar 5 puntos
                reduceFivePoints(playerTurn);

                //Mensaje
                System.out.println("La palabra no existe, -5 puntos");

                //Agregar palabra usada
                PalabraUsada usedWord = new PalabraUsada(word, -5, playerTurn);
                foundWordsData.add(usedWord);
            }
        }

        //Establecer que la palabra ya se uso
        foundWords.add(word);
    }

    //Eliminar 5 puntos de jugador
    public void reduceFivePoints(int playerTurn){
        for(int i = 1; i <= 5; i++){
            if(playerPoints.get(playerTurn) != 0){
                playerPoints.put(playerTurn, playerPoints.get(playerTurn) - 1);
            }
        }
    }

    //Verifiar que las letras de la palabra ingresada esten en el HashSet
    public boolean verifyLetters(String word){
        boolean existLetters = true;

        //Convertir letra a arreglo
        String[] wordArray = word.split("");
        int booleanCounter[] = {0};

        //Crear arreglo de control
        boolean[] wordsControl = new boolean[wordArray.length];
        Arrays.fill(wordsControl, false);

        //Buscar las letras validas
        Iterator<String> lettersIterator = lettersSet.iterator(); //Iterador
        while(lettersIterator.hasNext()){
            String letter = lettersIterator.next();
            for(int i = 0; i < wordArray.length; i++){
                if(letter.equals(wordArray[i])){
                    wordsControl[i] = true;
                }
            }
        }

        //Lambda para contar las letras validas
        for(int i = 0; i < wordsControl.length; i++){
            if(wordsControl[i]){
                booleanCounter[0]++;
            }
        }

        //Definir si la palabra tiene todas sus letras validas
        if(booleanCounter[0] == wordArray.length){
            existLetters = true;
        }else{
            existLetters = false;
        }

        return existLetters;
    }

    //Mostrar resultados de juego
    public void showGameResults(){
        //Control de puntaje mas alto
        int highestPoints[] = {playerPoints.get(1)};
        int tiePlayers[] = {0};

        //Obtener el puntaje mas alto
        playerPoints.forEach((k, v) -> {
            if(v >= highestPoints[0]){
                highestPoints[0] = v;
            }
        });

        //Determinar si existe empate
        playerPoints.forEach((k, v) -> {
            if(v == highestPoints[0]){
                tiePlayers[0]++;
            }
        });

        //Mostrar resultados
        System.out.println("+) Resultados de los Jugadores: ");
        playerPoints.forEach((k, v) -> {
            if(v == highestPoints[0]){
                if(tiePlayers[0] > 1){
                    System.out.println("   -) Jugador " + k + ": " + v + " Puntos (EMPATE).");
                }else{
                    System.out.println("   -) Jugador " + k + ": " + v + " Puntos (¡¡¡Ganador!!!).");
                }
            }else{
                System.out.println("   -) Jugador " + k + ": " + v + " Puntos.");
            }
        });

    }
}