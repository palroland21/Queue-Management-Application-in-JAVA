package utcn.pt.businessLogic;

import utcn.pt.model.Client;
import utcn.pt.model.Server;

import java.util.List;

public class QueueStrategy implements Strategy{

    public void addClient(List<Server> servers, Client c){
        Server min = servers.getFirst();

        for(Server sv : servers){
            if( sv.getNumberOfClients() < min.getNumberOfClients()){
                min = sv;
            }
        }

        min.addClients(c);
    }

}
