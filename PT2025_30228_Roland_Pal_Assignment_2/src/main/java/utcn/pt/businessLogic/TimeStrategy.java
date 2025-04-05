package utcn.pt.businessLogic;

import utcn.pt.model.Client;
import utcn.pt.model.Server;

import java.util.List;

public class TimeStrategy implements Strategy {

    public void addClient(List<Server> servers, Client c){

        Server min = servers.getFirst();
        for(Server sv : servers){
            if(min.getWaitingPeriod() > sv.getWaitingPeriod()){
                min = sv;
            }
        }

        min.addClients(c);
    }

}
