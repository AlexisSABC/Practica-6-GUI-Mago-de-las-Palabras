package homework;

import java.util.*;

public class SetdeLetras {
    //Guardar set de letras
    private HashSet<String> letterSet;

    //Inicializar HasSet
    public SetdeLetras(){
        letterSet = new HashSet<>();
    }

    //Generar y devolver Set de Letras
    public HashSet<String> generateSet(int mount){
        //Inicializar elementos
        Random generator = new Random();

        //Cantidad de vocales segun la dificultad
        int vowelAmount = 0;
        if(mount == 10){
            vowelAmount = 3;
        }else{
            vowelAmount = 2;
        }

        //Obtener ArrayList de claves
        BancodePalabras keyBank = new BancodePalabras();
        HashMap<String, Integer> bank = keyBank.generateBank();

        Set<String> setKeys = bank.keySet();
        ArrayList<String> keys = new ArrayList<>(setKeys);
        int wordsAmont = keys.size();

        //Algoritmo de generacion de letras
        // Crear listas para separar vocales y consonantes
        int switchRepeat = 1;
        do{
            String randomWord = keys.get(generator.nextInt(wordsAmont));
            String[] randomWordArray = randomWord.split("");
            switchRepeat = 1;

            //Filtrar palabras de longitud corta
            if(randomWordArray.length <= 5){
                while(switchRepeat <= 2){

                    if(switchRepeat == 1){
                        String[] vowels = {"a", "e", "i", "o", "u"};
                        while(letterSet.size() < vowelAmount){
                            letterSet.add(vowels[generator.nextInt(vowels.length)]);
                        }
                    }else{
                        for(int j = 0; j < randomWordArray.length; j++){
                            if((randomWordArray[j].equals("a") == false) && (randomWordArray[j].equals("e") == false) && (randomWordArray[j].equals("i") == false) && (randomWordArray[j].equals("o") == false) && (randomWordArray[j].equals("u") == false)){
                                if(letterSet.size() < mount){
                                    letterSet.add(randomWordArray[j]);
                                }
                            }
                        }
                    }

                    switchRepeat++;
                }
            }

        }while(letterSet.size() < mount);

        return letterSet;
    }
}