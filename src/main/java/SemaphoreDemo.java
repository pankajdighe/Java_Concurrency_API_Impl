import javafx.scene.chart.PieChart;

import java.util.concurrent.Semaphore;

/**
 * Created by pankaj on 11/8/16.
 *
 * This is simple implementation of the Java Semaphore.
 * Idea of this project it to distribute limited objects of DataConnection class.
 * DataConnectionPool class is used to maintain the pool of DataConnection objects
 */

class DataConnection{

    private int connection_id;
    private String connection_string;

    public DataConnection(int connection_id,String connection_string){
        this.connection_id=connection_id;
        this.connection_string=connection_string;

    }

    public void useDataConnection(){

        System.out.println("Using data Connection "+this.connection_id);
    }

    public String toString(){

        return this.connection_string;
    }

}

class DataConnectionPool{

    private int MAX_AVAILABLE_DATA_CONNECTIONS=0;
    private final Semaphore available;
    private boolean[] used;
    private DataConnection[] dataconnectionItems;

    public DataConnectionPool(int MAX_AVAILABLE_DATA_CONNECTIONS){


        this.MAX_AVAILABLE_DATA_CONNECTIONS=MAX_AVAILABLE_DATA_CONNECTIONS;
        this.available = new Semaphore(MAX_AVAILABLE_DATA_CONNECTIONS, true);
        this.used=new boolean[MAX_AVAILABLE_DATA_CONNECTIONS];
        this.dataconnectionItems=new DataConnection[MAX_AVAILABLE_DATA_CONNECTIONS];
        this.intitDataConnections();

    }

    private void intitDataConnections() {

      //  System.out.println("Connections "+MAX_AVAILABLE_DATA_CONNECTIONS);
        for(int i=0;i<MAX_AVAILABLE_DATA_CONNECTIONS;i++){



            DataConnection dataConnection=new DataConnection(i,"This is conn string "+i);
            dataconnectionItems[i]=dataConnection;
            //used[i]=false;

        }
    }


    public DataConnection getDataConnection() throws InterruptedException {
        available.acquire();
        return getNextAvailableDataConnection();
    }

    private DataConnection getNextAvailableDataConnection() {

        for (int i = 0; i < MAX_AVAILABLE_DATA_CONNECTIONS; ++i) {
            if (!used[i]) {
                used[i] = true;
                return dataconnectionItems[i];
            }
        }
        return null;
    }

    private synchronized boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE_DATA_CONNECTIONS; ++i) {
            if (item == dataconnectionItems[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }

    public void putItem(DataConnection dataConnection) {
        System.out.println("Putting back DataConnection "+dataConnection.toString());
        if (markAsUnused(dataConnection))
            available.release();
    }


}

public class SemaphoreDemo {


    public static void main(String args[]) {

        final DataConnectionPool dataConnectionPool=new DataConnectionPool(100);

        Thread t1 = new Thread() {

            @Override
            public void run() {


                while (true) {
                    //System.out.print("no data");
                    try {

                        DataConnection dataConnection=dataConnectionPool.getDataConnection();
                      //  if(dataConnection!=null) {
                            dataConnection.useDataConnection();
                            Thread.sleep(100);
                            dataConnectionPool.putItem(dataConnection);
                      //  }
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
                        DataConnection dataConnection=dataConnectionPool.getDataConnection();
                        if(dataConnection!=null) {
                        dataConnection.useDataConnection();
                        Thread.sleep(100);
                        dataConnectionPool.putItem(dataConnection);


                        }else {

                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        };

        Thread t3 = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        DataConnection dataConnection=dataConnectionPool.getDataConnection();
                        if(dataConnection!=null) {
                        dataConnection.useDataConnection();
                        Thread.sleep(100);
                        dataConnectionPool.putItem(dataConnection);

                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        };


        t1.start();
        t2.start();
        t3.start();

      /*  try {
            DataConnection dataConnection=dataConnectionPool.getDataConnection();
            if(dataConnection!=null) {
                dataConnection.useDataConnection();

            }else{

                System.out.println("No Connection found");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }
}