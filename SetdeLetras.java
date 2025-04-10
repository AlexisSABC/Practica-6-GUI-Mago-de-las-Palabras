package homework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class SetdeLetras {
    //Guardar set de letras
    private HashSet<String> letterSet;
    private ArrayList<String> wordsBank;

    //Inicializar HasSet
    public SetdeLetras(){
        letterSet = new HashSet<>();
    }

    //Generar y devolver Set de Letras
    public HashSet<String> generateSet(int mount){
        //Inicializar elementos
        String vowels = "aeiou";
        String consonants = "bcdfghjklmnñpqrstvwxyz";

        int letterIndex = 0;
        String letter;
        Random generator = new Random();

        //Agregar vocales
        int vowelAmount = 0;
        if(mount == 10){
            vowelAmount = 3;
        }else{
            vowelAmount = 2;
        }

        while(letterSet.size() != vowelAmount){
            letterIndex = generator.nextInt(vowels.length());
            letter = String.valueOf(vowels.charAt(letterIndex));
            letterSet.add(letter);
        }

        //Agregar consonantes
        while(letterSet.size() != mount){
            letterIndex = generator.nextInt(consonants.length());
            letter = String.valueOf(consonants.charAt(letterIndex));
            letterSet.add(letter);
        }

        return letterSet;
    }
}