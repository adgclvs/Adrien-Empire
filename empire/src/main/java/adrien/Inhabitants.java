package adrien;

public class Inhabitants {
    private static int number_inhabitants;

    // Instance unique de la classe
    private static Inhabitants instance;

    // Constructeur privé pour empêcher l'instanciation directe
    private Inhabitants(int initialInhabitants) {
        number_inhabitants = initialInhabitants;
    }

    // Méthode publique pour obtenir l'instance unique
    public static Inhabitants getInstance(int initialInhabitants) {
        if (instance == null) {
            instance = new Inhabitants(initialInhabitants);
        }
        return instance;
    }

    public static int getNumberInhabitants() {
        return number_inhabitants;
    }

    public static void addInhabitants(int number) {
        number_inhabitants += number;
    }

    public static void removeInhabitants(int number) {
        number_inhabitants -= number;
        if (number_inhabitants < 0) {
            number_inhabitants = 0;
        }
    }
}