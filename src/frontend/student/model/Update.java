package frontend.student.model;


import common.Utils;

/**
 * The update class is used for any views that needs to be updated regularly.
 *
 *
 * @author Melisha Trout
 * @version 16/03/208
 */


public class Update implements Runnable, UpdateHelper {

    private UpdateHelper updateHelper;
    private double delay;

    public Update (UpdateHelper obj, double delay) {
        this.updateHelper = obj;
        this.delay = delay;
    }

    public void run() {
        long lastUpdate = System.nanoTime();
        double t = 0;
        while (updateHelper.stillRunning()) {
            long now = System.nanoTime();
            t = (now - lastUpdate) / 1000000000;
            if (t >= delay - 0.1) {
                updateHelper.update();
                lastUpdate = now;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Utils.logException("InterruptedException: ", e);
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public boolean stillRunning() {
        return false;
    }
}