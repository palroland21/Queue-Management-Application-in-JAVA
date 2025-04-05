package utcn.pt.businessLogic;

import utcn.pt.model.Client;
import utcn.pt.model.Server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Scheduler {

    private List<Server> servers;
    private int nrOfServers;
    private int nrOfClients;
    private Strategy strategy;

    public Scheduler(int nrOfServers, int nrOfClients){
        this.nrOfServers = nrOfServers;
        this.nrOfClients = nrOfClients;
        this.servers = new ArrayList<>();

        for (int i = 0; i < nrOfServers; i++) {
            Server server = new Server();
            servers.add(server);

            Thread thread = new Thread(server);
            thread.start();
        }

    }

    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            this.strategy = new QueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            this.strategy = new TimeStrategy();
        }
    }

    public void dispatchClient(Client client) {
        strategy.addClient(servers, client);
    }

    public List<Server> getServers() {
        return servers;
    }

}
