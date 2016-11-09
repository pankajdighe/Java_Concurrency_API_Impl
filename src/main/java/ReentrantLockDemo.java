import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pankaj on 11/8/16.
 *
 * This is simple implementation of ReentrantLock
 * This demo will start three threads, all threads print the specific string on console
 * This program will synchronize the these three threads
 *
 *
 */
public class ReentrantLockDemo {

    private final ReentrantLock lock = new ReentrantLock();


    //This method is using Reetrant Lock
    public void print(String input) {
        lock.lock();
        try {
            System.out.print(input);

        } finally {
            lock.unlock();
        }
    }


    public static void main(String args[]) {

        final ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();

        Thread t1 = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        reentrantLockDemo.print("1");
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();                    }
                }

            }
        };

        Thread t2 = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        reentrantLockDemo.print("2");
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();                    }
                }

            }
        };


        Thread t3 = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        reentrantLockDemo.print("3");
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();                    }
                }

            }
        };


        t1.start();
        t2.start();
        t3.start();



    }
}
