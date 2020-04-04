package towerdefense;

import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import states.MenuState;
import states.StateManager;

public class Game implements Runnable {

    private boolean running = false;
    private Thread gameThread;
    private StateManager stateManager;
    private Canvas canvas;

    public Game() {
        stateManager = new StateManager();
        canvas = new Canvas();
    }

    @Override
    public void run() {

        MenuState menu = new MenuState(stateManager);
        stateManager.addState(menu);

        int fps = 60;
        double tickTime = (double) 1000000000 / fps;
        long now;
        double delta = 0;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while (running) {

            now = System.nanoTime();
            delta += (now - lastTime) / tickTime;
            timer += (now - lastTime);
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                delta = 0;
                ticks += 1;
            }

            if (timer >= 1000000000) {
                //System.out.println(ticks);
                ticks = 0;
                timer = 0;
            }

        }
        stop();
    }

    private void tick() {
        stateManager.tick();
    }

    private void render() {
        stateManager.render(canvas.getGraphicsContext2D());
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            gameThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
