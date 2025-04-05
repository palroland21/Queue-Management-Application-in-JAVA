package utcn.pt.model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private static int i = 1;
    private int id;
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;

    public Server(){
        this.id = i++;
        this.clients = new LinkedBlockingQueue<Client>();
        this.waitingPeriod = new AtomicInteger(0);
    }

    public int getWaitingPeriod(){
        return waitingPeriod.get();
    }

    private void addWaitingPeriod(int period){
        waitingPeriod.set(this.waitingPeriod.get() + period);
    }

    public int getNumberOfClients(){
        return clients.size();
    }

    public void addClients(Client client){
        this.clients.add(client);
        addWaitingPeriod(client.getServiceTime());
    }

    public void removeClient(Client client){
        this.clients.remove(client);
    }

    public void run(){
        while(true){
            try {
                if (!clients.isEmpty()) {
                    Client currClient = clients.peek(); // get First Client
                    Thread.sleep(1000);

                    currClient.decrementServiceTime();
                    waitingPeriod.decrementAndGet();

                    if(currClient.getServiceTime() == 0){
                        clients.remove(currClient);
                    }
                }
            }catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public ArrayList<Client> getClients(){
        return new ArrayList<>(clients);
    }

    @Override
    public String toString(){
        StringBuilder myString = new StringBuilder();
        myString.append("Queue ").append(id).append(": ");
        if(!clients.isEmpty()){
            for(Client c : clients){
                myString.append(c.toString()).append(" ");
            }
        }else {
            myString.append("closed");
        }
        return myString.toString();
    }
}
