package adrien;

import java.util.ArrayList;
import java.util.List;

public class GameTimer {
    private int currentTick;
    private int tickDuration;
    private boolean running;
    private List<Runnable> tickListeners;

    public GameTimer(int tickDuration) {
        this.tickDuration = tickDuration;
        this.currentTick = 0;
        this.running = false;
        this.tickListeners = new ArrayList<>();
    }

    /**
     * Ajouter un listener à exécuter à chaque tick
     * @param listener Action à exécuter
     */
    public void addTickListener(Runnable listener) {
        tickListeners.add(listener);
    }

    /**
     * Démarrer le timer
     */
    public void start() {
        running = true;
        Thread timerThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(tickDuration); // Attendre la durée d'un tick
                    currentTick++;
                    System.out.println("Tick: " + currentTick);

                    // Notifier tous les listeners
                    for (Runnable listener : tickListeners) {
                        listener.run();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Timer interrupted.");
                }
            }
        });
        timerThread.start();
    }

    /**
     * Arrêter le timer
     */
    public void stop() {
        running = false;
    }

    /**
     * Obtenir le tick actuel
     * @return Tick actuel
     */
    public int getCurrentTick() {
        return currentTick;
    }
}
