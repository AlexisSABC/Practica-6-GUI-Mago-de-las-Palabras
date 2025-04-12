package homework;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MagodelasPalabras {
    //Atributos, listas y objetos graficos
    private int playerTurn;
    private int playersAmount;
    private int lettersAmount;
    private int skipCounter;
    private int roundCounter;
    private String unsavedWord;

    private HashSet<String> foundWords; //Guarda las palabras ya usadas
    private HashSet<String> lettersSet; //Set de letras usables por ronda
    private ArrayList<PalabraUsada> foundWordsData; //Guardar palabra, puntaje y jugador que lo ingreso
    private HashMap<String, Integer> wordBank; //Banco con todas las palabras disponibles
    private HashMap<Integer, Integer> playerPoints; //Puntos de los 2 a 4 jugadores

    private JLabel mainSign;
    private JLabel roundLabel;
    private JLabel playerTurnLabel;
    private JButton skipTurnButton;
    private JButton setWordButton;
    private JButton addToDictionary;
    private JButton eraseLetterButton;
    private JLabel showWord;
    private JLabel usedWordsTitle;
    private JLabel usedWordList;

    private ArrayList<JButton> lettersButtons;
    private ArrayList<JLabel> playerPointsLabels;

    private SetdeLetras objectSet;
    private BancodePalabras wordBankObject;

    //Inicializar todo el juego
    public MagodelasPalabras(){
        //Inicializar atributos
        playerTurn = 1;
        playersAmount = 2;
        lettersAmount = 0;
        roundCounter = 1;

        playerPoints = new HashMap<>();
        foundWords = new HashSet<>();
        lettersSet = new HashSet<>();
        foundWordsData = new ArrayList<>();
        lettersButtons = new ArrayList<>();
        playerPointsLabels = new ArrayList<>();

        //Crear objeto de set de letras
        objectSet = new SetdeLetras();

        //Crear banco de palabras
        wordBankObject = new BancodePalabras();
        wordBank = wordBankObject.generateBank();

        //Iniciar juego
        firstWindow();
    }

    //Pedir numero de jugadores y dificultad
    public void firstWindow(){
        JFrame startWindow = new JFrame("El mago de las palabras"); //crea la ventana
        startWindow.setSize(720,500); //poner el tamaño de la ventana
        startWindow.setLayout(null); //deja acomodar los objetos libremente en la ventana
        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //termina la ejecucion de la ventana al cerrarlo


        Container contentPane = startWindow.getContentPane(); //mueve libremente los objetos
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
            public void actionPerformed(ActionEvent e){
                rulesWindow();
            }
        });

        //boton de error
        JLabel errorMessage =  new JLabel("No se seleccionó una dificultad");
        errorMessage.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        errorMessage.setBounds((startWindow.getWidth()-errorMessage.getPreferredSize().width)/2,(startWindow.getHeight()/2) + 150,errorMessage.getPreferredSize().width+errorMessage.getPreferredSize().width, errorMessage.getPreferredSize().height);
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
    public void rulesWindow(){
        JFrame rulesWindow = new JFrame("Reglas del juego");
        rulesWindow.setSize(1000,500);
        rulesWindow.setLayout(null);

        Container container = rulesWindow.getContentPane();
        container.setBackground(Color.WHITE);

        //Titulo
        JLabel rulesTitleLabel = new JLabel("Reglas: ");
        rulesTitleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        rulesTitleLabel.setBounds((1000 - rulesTitleLabel.getPreferredSize().width) / 2, 10, rulesTitleLabel.getPreferredSize().width + 20, rulesTitleLabel.getPreferredSize().height + 20);
        rulesTitleLabel.setVisible(true);
        rulesWindow.add(rulesTitleLabel);

        //Reglas
        JLabel rulesLabel = new JLabel();
        rulesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        rulesLabel.setBounds(10, 60, 960, 365);
        rulesLabel.setVisible(true);
        rulesWindow.add(rulesLabel);

        rulesLabel.setText("<html> +) Reglas de puntuación: <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; -) Las vocales otorgan 5 puntos. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; -) Las consonantes otorgan 3 puntos. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; -) Se agregan los puntos si la palabra ingresada es una palabra valida y cumple con las letras ofrecidas. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; -) Por su contraparte, el jugador pierde 5 puntos al ingresar una palabra no valida. <br>" +
                "<br>" +
                "+) Modos de juego: <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; -) Regular: Los jugadores podran ingresar tantas palabras como quieran  en un turno. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Ellos mismos pasan turno. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; A cada jugador se le da un set de 10 palabras. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Hay 3 vocales garantizadas. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp; -) Experto: Los jugadores solo podran poner una palabra en su turno. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Cada jugador contara con un set de 7 letras para crear la palabra. <br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Hay 2 vocales garantizadas. <br></html>");

        rulesWindow.setResizable(false);
        rulesWindow.setVisible(true);
    }

    //Ventana de juego total
    public void gameWindow(boolean difficultGame, int amountPlayer){
        this.playersAmount = amountPlayer;

        //Obtener letras a generar
        if(difficultGame){
            lettersAmount = 10;
        }else{
            lettersAmount = 7;
        }

        //Crear jugadores
        for(int i = 1; i <= playersAmount; i++){
            playerPoints.put(i, 0);
        }

        JFrame gameWindow = new JFrame("Practica 6 - GUI mago de las palabras");
        gameWindow.setSize(1080,720);
        gameWindow.setLayout(null);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = gameWindow.getContentPane();
        container.setBackground(Color.WHITE);

        //letrero de ronda
        roundLabel = new  JLabel("Ronda " + roundCounter);
        roundLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        roundLabel.setBounds((gameWindow.getWidth())/50,(gameWindow.getHeight())/55,roundLabel.getPreferredSize().width,roundLabel.getPreferredSize().height);
        roundLabel.setVisible(true);
        gameWindow.add(roundLabel);

        //crear etiquetas de puntos
        for(int i = 0; i < amountPlayer; i++){
            //listado de jugadores con sus puntos
            JLabel playerLabel = new  JLabel("Jugador  " +(i+1) +"  Puntos: 0");
            playerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            playerLabel.setBounds((gameWindow.getWidth())/50, (gameWindow.getHeight()/45) + ((i+1)*40),playerLabel.getPreferredSize().width + 200,playerLabel.getPreferredSize().height);
            gameWindow.add(playerLabel);
            playerLabel.setVisible(true);

            playerPointsLabels.add(playerLabel);
        }

        //crear botones de letras
        for(int i =0; i< lettersAmount; i++){
            JButton letterButton = new JButton("A");
            letterButton.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            letterButton.setBackground(Color.LIGHT_GRAY);
            letterButton.setBorder(new LineBorder(Color.BLACK, 3));
            letterButton.setBounds((gameWindow.getWidth())/3 + ((i+1) * 60),(gameWindow.getHeight())/2+(gameWindow.getHeight())/6,letterButton.getPreferredSize().width+letterButton.getPreferredSize().width/4,letterButton.getPreferredSize().height);
            gameWindow.add(letterButton);
            letterButton.setVisible(true);

            letterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    writeLetter(letterButton.getText());
                    mainSign.setText("");
                }
            });

            lettersButtons.add(letterButton);
        }

        //Mostrar set de letras inicial en letras
        showButtonLetters();

        boolean gameMode[] = {difficultGame};
        //boton para pasar turno
        skipTurnButton = new JButton("Pasar turno");
        skipTurnButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        skipTurnButton.setBackground(Color.LIGHT_GRAY);
        skipTurnButton.setBorder(new LineBorder(Color.BLACK, 3));
        if(difficultGame) {
            skipTurnButton.setBounds((gameWindow.getWidth() + skipTurnButton.getPreferredSize().width * 24/5) / 2, (gameWindow.getHeight()) / 2 + (gameWindow.getHeight()) / 3, skipTurnButton.getPreferredSize().width, skipTurnButton.getPreferredSize().height);
        }else{
            skipTurnButton.setBounds((gameWindow.getWidth() + skipTurnButton.getPreferredSize().width *25/9 ) / 2, (gameWindow.getHeight()) / 2 + (gameWindow.getHeight()) / 3, skipTurnButton.getPreferredSize().width, skipTurnButton.getPreferredSize().height);

        }
        skipTurnButton.setVisible(true);
        gameWindow.add(skipTurnButton);

        skipTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                skipTurnFunctions(true, gameMode[0]);
            }
        });

        if(difficultGame){
            skipTurnButton.setVisible(true);
        }else{
            skipTurnButton.setVisible(false);
        }

        //boton para ingresar palabra
        setWordButton = new JButton("Ingresar palabra");
        setWordButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        setWordButton.setBackground(Color.LIGHT_GRAY);
        setWordButton.setBorder(new LineBorder(Color.BLACK, 3));
        if(difficultGame) {
            setWordButton.setBounds((gameWindow.getWidth() - skipTurnButton.getPreferredSize().width) / 2, (gameWindow.getHeight()) / 2 + (gameWindow.getHeight()) / 3, setWordButton.getPreferredSize().width, setWordButton.getPreferredSize().height);
        }else{
            setWordButton.setBounds((gameWindow.getWidth()- skipTurnButton.getPreferredSize().width*2)/2,(gameWindow.getHeight())/2+(gameWindow.getHeight())/3,setWordButton.getPreferredSize().width,setWordButton.getPreferredSize().height);
        }
        setWordButton.setVisible(true);
        gameWindow.add(setWordButton);

        setWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(showWord.getText().isEmpty()){
                    mainSign.setText("No se ingreso una palabra");

                }else{
                    analyzeWord(showWord.getText(), playerTurn);
                    skipTurnFunctions(false, gameMode[0]);
                }

                showWord.setText("");
            }
        });

        //boton para agregar al diccionario
        addToDictionary = new JButton("Agregar al diccionario");
        addToDictionary.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        addToDictionary.setBackground(Color.LIGHT_GRAY);
        addToDictionary.setBorder(new LineBorder(Color.BLACK, 3));
        addToDictionary.setBounds((gameWindow.getWidth()/2 + gameWindow.getWidth()/4),(gameWindow.getHeight()-addToDictionary.getPreferredSize().height)/2,addToDictionary.getPreferredSize().width,addToDictionary.getPreferredSize().height);
        addToDictionary.setVisible(false);
        gameWindow.add(addToDictionary);

        addToDictionary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addtoDictionary(unsavedWord);
            }
        });

        //Boton para eliminar letras
        eraseLetterButton = new JButton("Borrar letra");
        eraseLetterButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        eraseLetterButton.setBackground(Color.LIGHT_GRAY);
        eraseLetterButton.setBorder(new LineBorder(Color.BLACK, 3));
        eraseLetterButton.setBounds((gameWindow.getWidth()/2 + gameWindow.getWidth()/4),(gameWindow.getHeight()-eraseLetterButton.getPreferredSize().height)/2 + 50,eraseLetterButton.getPreferredSize().width,eraseLetterButton.getPreferredSize().height);
        eraseLetterButton.setVisible(true);
        gameWindow.add(eraseLetterButton);

        eraseLetterButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e){
               eraseLetter();
           }
        });

        //Mostrar la palabra que se esta escribiendo
        showWord = new JLabel("palabra");
        showWord.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        showWord.setBackground(Color.LIGHT_GRAY);
        showWord.setBorder(new LineBorder(Color.BLACK, 3));
        showWord.setBounds(((gameWindow.getWidth()-showWord.getPreferredSize().width*3/2)/2) + 15, (gameWindow.getHeight()/2-gameWindow.getHeight()/3), showWord.getPreferredSize().width+showWord.getPreferredSize().width+150,showWord.getPreferredSize().height);
        showWord.setVisible(true);
        showWord.setHorizontalAlignment(SwingConstants.CENTER);
        showWord.setVerticalAlignment(SwingConstants.CENTER);
        showWord.setText("");
        gameWindow.add(showWord);

        //Letrero central
        mainSign = new JLabel("El jugador N fue el ganador!!!!");
        mainSign.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        mainSign.setBounds((gameWindow.getWidth()-mainSign.getPreferredSize().width*3/8)/2,(gameWindow.getHeight()*5)/16,mainSign.getPreferredSize().width+100,mainSign.getPreferredSize().height);
        mainSign.setVisible(true);
        mainSign.setText("");
        gameWindow.add(mainSign);

        //Letrero turno del jugador N
        playerTurnLabel = new JLabel("Es turno del jugador " + playerTurn);
        playerTurnLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        playerTurnLabel.setBounds(((gameWindow.getWidth()-playerTurnLabel.getPreferredSize().width/4)/2) + 15, (gameWindow.getHeight()/15),playerTurnLabel.getPreferredSize().width,playerTurnLabel.getPreferredSize().height);
        playerTurnLabel.setVisible(true);
        gameWindow.add(playerTurnLabel);

        //Contenedor para palabras usadas
        usedWordList = new JLabel("");
        usedWordList.setBackground(Color.WHITE);
        usedWordList.setOpaque(true);
        usedWordList.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        usedWordList.setVerticalAlignment(SwingConstants.TOP);

        // Envolver el JLabel en un JScrollPane
        JScrollPane scrollPane = new JScrollPane(usedWordList);
        scrollPane.setBounds(10, ((gameWindow.getHeight() / 5) * 2) - 20, 390, 400);

        // Agregar el JScrollPane en lugar del JLabel directamente
        gameWindow.add(scrollPane);

        //Titulo de palabras usadas
        usedWordsTitle = new JLabel("Palabras usadas");
        usedWordsTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        usedWordsTitle.setSize(usedWordsTitle.getPreferredSize().width + 20, usedWordsTitle.getPreferredSize().height);
        usedWordsTitle.setLocation(((scrollPane.getWidth() - usedWordsTitle.getWidth()) / 2) + 10, (gameWindow.getHeight() / 5) + 75);
        usedWordsTitle.setVisible(true);
        gameWindow.add(usedWordsTitle);

        gameWindow.setResizable(false);
        gameWindow.setVisible(true);
    }

    //Mostrar informacion en dados
    public void showButtonLetters(){
        //Crear set de letras inicial
        lettersSet.clear();
        lettersSet = objectSet.generateSet(lettersAmount);

        Iterator<String> lettersSetIterator = lettersSet.iterator();
        int buttonCounter = 0;

        while(lettersSetIterator.hasNext()){
            lettersButtons.get(buttonCounter).setText(lettersSetIterator.next());
            buttonCounter++;
        }
    }

    //Funcion de paso de turno
    public void skipTurnFunctions(boolean leaptTurn, boolean gameMode){
        //Controlar rondas y turnos
        if(leaptTurn){ //Para saltar ronda
            skipCounter++;
            if(skipCounter == playersAmount){ //Paso de rona
                roundCounter++;
                skipCounter = 0;
                playerTurn = 1;

                mainSign.setText("");
                foundWords.clear();
                foundWordsData.clear();
                showWord.setText("");
                showButtonLetters();
                showUsedWords(true);

            }else{
                if(playerTurn < playersAmount){
                    playerTurn++;
                }else{
                    playerTurn = 1;
                }

                mainSign.setText("");
            }

        }else{ //Para ingresar palabra
            if(gameMode){
                skipCounter = 0;
                if(playerTurn < playersAmount){
                    playerTurn++;
                }else{
                    playerTurn = 1;
                }

            }else{
                skipCounter++;
                if(skipCounter == playersAmount){ //Paso de rona
                    roundCounter++;
                    skipCounter = 0;
                    playerTurn = 1;

                    foundWords.clear();
                    foundWordsData.clear();
                    mainSign.setText("");
                    showButtonLetters();
                    showUsedWords(true);

                }else{
                    if(playerTurn < playersAmount){
                        playerTurn++;
                    }else{
                        playerTurn = 1;
                    }

                    mainSign.setText("");
                }
            }
        }

        //Actualizar etiquetas
        roundLabel.setText("Ronda " + roundCounter);
        playerTurnLabel.setText("Es turno del jugador " + playerTurn);

        //Verificar si ya se completaron los turnos y modificar ventana
        if(roundCounter > 3){
            setWordButton.setVisible(false);
            skipTurnButton.setVisible(false);
            addToDictionary.setVisible(false);
            roundLabel.setVisible(false);
            eraseLetterButton.setVisible(false);
            usedWordsTitle.setVisible(false);
            playerTurnLabel.setText("¡¡¡ FIN DEL JUEGO !!!");

            Iterator<JButton> buttonIterator = lettersButtons.iterator();
            while(buttonIterator.hasNext()){
                buttonIterator.next().setVisible(false);
            }

            showUsedWords(true);
            generateGameResults();
        }
    }

    //Borrar letra reciente
    public void eraseLetter(){
        String text = showWord.getText();
        addToDictionary.setVisible(false);

        if(text.isEmpty() == false){
            String[] allText = text.split("");
            String[] newText = new String[allText.length - 1];
            int [] i = {0};

            Arrays.asList(allText).forEach(w -> {
                if(i[0] < newText.length){
                    newText[i[0]] = w;
                }

                i[0]++;
            });

            showWord.setText("");
            for(String s : newText){
                showWord.setText(showWord.getText() + s);
            }
        }

        addToDictionary.setVisible(false);
    }

    //Escribir letra reciente
    public void writeLetter(String letter){
        showWord.setText(showWord.getText() + letter);
        addToDictionary.setVisible(false);
    }

    //Analizar palabras ingrezada
    public void analyzeWord(String word, int playerID){
        //Verificar que la palabra ingresada tenga las palabras del HashSet
        if(verifyLetters(word)){
            //Encontrar y dar los puntos del valor de la palabra al jugador si no ha sido colocado
            findWord(word, playerID);
        }else{
            //Mensaje de letras no permitidad y Eliminar 5 puntos
            mainSign.setText("Palabra no tiene letras no asignadas, -5 Puntos");
            reduceFivePoints(playerID);
        }
    }

    //Metodo para darle los puntos de la palabra al jugador
    public void findWord(String word, int playerID){
        //Verificar si ya ha sido ingresado antes
        if(foundWords.contains(word)){
            mainSign.setText(word + " ya ha sido usada antes");

        }else{
            if(wordBank.containsKey(word)) { //si el banco de palabras tiene la palabra entonces obtiene su puntuacion y la suma al jugador
                //Guardar puntos
                Integer points = wordBank.get(word);

                //Actualizar los puntos
                playerPoints.put(playerID, playerPoints.get(playerID) + points);
                playerPointsLabels.get(playerID - 1).setText("Jugador  " +(playerID) +"  Puntos: " + playerPoints.get(playerID));

                //Mensaje
                mainSign.setText("Se obtuvieron: " + points + " puntos.");
                addToDictionary.setVisible(false);

                //Agregar datos de la palabra
                PalabraUsada usedWord = new PalabraUsada(word, points, playerID);
                foundWordsData.add(usedWord);
                showUsedWords(false);
            }else{
                //Eliminar 5 puntos
                reduceFivePoints(playerTurn);

                //Mensaje
                mainSign.setText(word + " no existe, -5 puntos");
                addToDictionary.setVisible(true);
                unsavedWord = word;

                //Agregar palabra usada
                PalabraUsada usedWord = new PalabraUsada(word, -5, playerTurn);
                foundWordsData.add(usedWord);
                showUsedWords(false);
            }
        }

        //Establecer que la palabra ya se uso
        foundWords.add(word);
        showWord.setText("");
    }

    //Eliminar 5 puntos de jugador
    public void reduceFivePoints(int playerID){
        for(int i = 1; i <= 5; i++){
            if(playerPoints.get(playerID) != 0){
                playerPoints.put(playerID, playerPoints.get(playerID) - 1);
            }
        }
        playerPointsLabels.get(playerID - 1).setText("Jugador  " +(playerID) +"  Puntos: " + playerPoints.get(playerID));
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
    public void generateGameResults(){
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
                mainSign.setText("¡¡¡ EMPATE !!!");
                mainSign.setLocation(mainSign.getX() + 60, mainSign.getY());
            }
        });

        //Mostrar resultados
        playerPoints.forEach((k, v) -> {
            if(v == highestPoints[0]){
                if(tiePlayers[0] > 1){
                    playerPointsLabels.get(k - 1).setText(playerPointsLabels.get(k - 1) .getText() + " (EMPATE)");

                }else{
                    playerPointsLabels.get(k - 1).setText(playerPointsLabels.get(k - 1).getText());
                    mainSign.setText("¡¡¡ Jugador " + k + " gana la partida !!!");
                    mainSign.setLocation(mainSign.getX() - 50, mainSign.getY());
                }
            }else{
                playerPointsLabels.get(k - 1).setText(playerPointsLabels.get(k - 1).getText());
            }
        });
    }

    //Mostrar etiquetas de palabras usadas
    public void showUsedWords(boolean eraseLabels){

        if(eraseLabels){ //Eliminar la lista
            usedWordList.setText("");

        }else{ //Actualizar la lista
            String allWordsList = "";
            for(int i = 0; i < foundWordsData.size(); i++){
                String addDetail = "+) " + foundWordsData.get(i);
                allWordsList = allWordsList + addDetail + "<br>";
            }

            allWordsList = "<html>" + allWordsList + "</html>";

            usedWordList.setText(allWordsList);
        }
    }

    //metodo para agregar la palabra al diccionario
    public void addtoDictionary(String word){
        try{
            FileWriter modifyFile = new FileWriter("src/homework/Banco_de_Palabras.txt", true);
            BufferedWriter modifyBuffer = new BufferedWriter(modifyFile);
            modifyBuffer.write("\n" + word);
            modifyBuffer.flush();
            modifyBuffer.close();

        }catch(IOException e){
            System.err.println("Error al manejar el archivo.");
        }

        addToDictionary.setVisible(false);
        mainSign.setText("Palabra agregada con exito");
        wordBank.clear();
        wordBank = wordBankObject.generateBank();
    }
}
