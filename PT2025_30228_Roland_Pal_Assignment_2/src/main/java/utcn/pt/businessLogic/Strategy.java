package utcn.pt.businessLogic;

import utcn.pt.model.Client;
import utcn.pt.model.Server;

import java.util.List;

public interface Strategy {
    void addClient(List<Server>servers, Client c);
}
