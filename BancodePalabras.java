package homework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BancodePalabras {
    //Guardar todo el banco
    private ArrayList<String> wordsBank = new ArrayList<>();
    private HashMap<String, Integer> bank;

    //Generar Banco de palabras
    public BancodePalabras() {
        bank = new HashMap<>();
    }

    //Proceso de creacion de banco
    public HashMap<String, Integer> generateBank(){
        int wordsSave=0;
        String word; //Almacenar palabra temporal
        int wordPoints[] = {0}; //Calcula los puntos de las palabras

        try (BufferedReader fileData = new BufferedReader(new FileReader("src/homework/Banco_de_Palabras.txt"))) {
            while ((word = fileData.readLine()) != null) {
                //Reiniciar acumulador de puntos
                wordPoints[0] = 0;

                //Convertir palabra a array
                String[] wordArray = word.split("");

                //Lambda para generar los puntos de la palabra
                Arrays.asList(wordArray).forEach(letter -> {
                    if (letter.equals("a") || letter.equals("e") || letter.equals("i") || letter.equals("o") || letter.equals("u")) {
                        wordPoints[0] += 5;
                    } else {
                        wordPoints[0] += 3;
                    }
                });

                //Insertar palabras (Clave) y puntaje de palabras
                bank.put(word.toLowerCase(), wordPoints[0]);
                wordsBank.add(word);
                wordsSave++;

            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo");
        }

        return bank;
    }

    public ArrayList<String> getWordsBank() {
        return wordsBank;
    }
}