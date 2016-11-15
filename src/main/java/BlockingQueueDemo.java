import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by pankaj on 11/14/16.
 *
 * This is simple implementation of Blocking Queue
 * In this example, I have implemented printer application
 * Use will be able to send the print job to printer and Printer will execute the jobs
 *
 *
 */

class User implements Runnable{

    private String user_id;

    private BlockingQueue<PrintJob> queue=null;

    public User(BlockingQueue<PrintJob> queue){
        this.queue=queue;

    }

    void sendPrint(){


    }

    public void run() {

        try {

            while(true) {

                Thread.sleep(1000);
                queue.put(new PrintJob("User : CMPE 277 "+ new Date().getTime()));
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class PrintJob{

    private String print_text;

    public PrintJob(String print_text){

        this.print_text=print_text;

    }

    void start_printing(){

        System.out.println("Printer "+this.print_text);
    }

}

class Printer implements Runnable{

    private PrintJob printJob;
    BlockingQueue<PrintJob> queue=null;

    public Printer(BlockingQueue<PrintJob> queue){

        this.queue=queue;

    }

    void print(PrintJob printJob){

        if(printJob!=null)
        printJob.start_printing();

    }

    public void run() {

        try {

           while(true) {
               System.out.print("Printer ");

               queue.take().start_printing();
           }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

public class BlockingQueueDemo {

    public static void main(String[] args){

        BlockingQueue<PrintJob> queue=new ArrayBlockingQueue<PrintJob>(2);

        User user=new User(queue);
        Printer printer=new Printer(queue);

        new Thread(user).start();
        new Thread(printer).start();



    }
}
