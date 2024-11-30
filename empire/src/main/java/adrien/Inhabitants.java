package adrien;

public class Inhabitants {
    private static int number_inhabitants;
    private static int number_workers;

    private static Inhabitants instance;

    // Constructeur privé pour empêcher l'instanciation directe
    private Inhabitants(int initialInhabitants) {
        number_inhabitants = initialInhabitants;
    }

    // Méthode publique pour obtenir l'instance unique
    public static Inhabitants getInstance() {
        if (instance == null) {
            instance = new Inhabitants(0);
        }
        return instance;
    }

    public static int getNumberInhabitants() {
        return number_inhabitants;
    }

    public static void addInhabitants(int number) {
        number_inhabitants += number;
        System.out.println("number" + number_inhabitants);
    }

    public static void removeInhabitants(int number) {
        number_inhabitants -= number;
        if (number_inhabitants < 0) {
            number_inhabitants = 0;
        }
    }

    public static int getNumberWorkers() {
        return number_workers;
    }

    public static boolean addWorkers(int number) {
        System.out.println("number_workers: " + number_workers);
        
        if(number_workers + number > number_inhabitants){
            return false;
        }
        number_workers += number;
        return true;
    }

    public static boolean removeWorkers(int number) {
        if (number_workers - number < 0) {
            return false;   
        }
        number_workers -= number;
        return true;
    }
}