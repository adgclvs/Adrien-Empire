package adrien;

public class Inhabitants {
    private static int number_inhabitants;
    private static int number_workers;

    private static Inhabitants instance;

    /**
     * Constructor
     */
    private Inhabitants(int initialInhabitants) {
        number_inhabitants = initialInhabitants;
    }

    /**
     * Get the instance of the Inhabitants
     */
    public static Inhabitants getInstance() {
        if (instance == null) {
            instance = new Inhabitants(0);
        }
        return instance;
    }

/**********************************INHABITANTS********************************************** */

    /**
     * Get the number of inhabitants
     */
    public static int getNumberInhabitants() {
        return number_inhabitants;
    }

    /**
     * Add inhabitants to the city
     * @param number Number of inhabitants to add
     */
    public static void addInhabitants(int number) {
        number_inhabitants += number;
        System.out.println("number" + number_inhabitants);
    }

    /**
     * Remove inhabitants from the city
     * @param number Number of inhabitants to remove
     */
    public static void removeInhabitants(int number) {
        number_inhabitants -= number;
        if (number_inhabitants < 0) {
            number_inhabitants = 0;
        }
    }

/**********************************WORKERS********************************************** */

    /**
     * Get the number of workers
     */
    public static int getNumberWorkers() {
        return number_workers;
    }

    /**
     * Add workers to the city
     * @param number Number of workers to add
     */
    public static boolean addWorkers(int number) {
        System.out.println("number_workers: " + number_workers);
        
        if(number_workers + number > number_inhabitants){
            return false;
        }
        number_workers += number;
        return true;
    }

    /**
     * Remove workers from the city
     * @param number Number of workers to remove
     */
    public static boolean removeWorkers(int number) {
        if (number_workers - number < 0) {
            return false;   
        }
        number_workers -= number;
        return true;
    }
}