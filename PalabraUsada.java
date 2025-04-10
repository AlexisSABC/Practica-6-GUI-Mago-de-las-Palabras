package homework;

public class PalabraUsada {
    //Atributos
    private String word;
    private int points;
    private int playerID;

    //Establecer datos de la palabra
    public PalabraUsada(String word, int points, int playerID) {
        this.word = word;
        this.points = points;
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return word + " (" + points + " puntos para Jugador " + playerID + " ).";
    }
}