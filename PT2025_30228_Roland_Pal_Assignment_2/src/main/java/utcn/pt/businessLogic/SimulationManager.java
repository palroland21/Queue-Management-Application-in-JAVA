package utcn.pt.businessLogic;

import utcn.pt.model.Client;
import utcn.pt.model.Server;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class SimulationManager implements Runnable {
    private int numberOfServers;
    private int numberOfClients;
    private int timeLimit;
    private int minSerTime;
    private int maxSerTime;
    private int minArrTime;
    private int maxArrTime;

    private JTextArea textArea;
    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    private Scheduler scheduler;
    private List<Client> generatedClient;
    private Random random;

    public SimulationManager(int numberOfClients, int numberOfServers, int timeLimit, int minSerTime, int maxSerTime, int minArrTime, int maxArrTime){
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.timeLimit = timeLimit;
        this.minSerTime = minSerTime;
        this.maxSerTime = maxSerTime;
        this.minArrTime = minArrTime;
        this.maxArrTime = maxArrTime;

        this.generatedClient = new ArrayList<Client>();
        this.scheduler = new Scheduler(numberOfServers, numberOfClients);
        this.random = new Random();


        generateNRandomClient();
    }

    public void selectStrategy(String strategy){
        if(strategy.equals("TimeStrategy")){
            selectionPolicy = SelectionPolicy.SHORTEST_TIME;
        } else if(strategy.equals("QueueStrategy")){
            selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
        }

    }

    public void setTextArea(JTextArea textArea){
        this.textArea = textArea;
    }

    public String getTextArea(){
        return textArea.getText();
    }

    private void updateTextArea(String text){
        textArea.append(text + "\n");
        // Autoscroll
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    private void generateNRandomClient(){
        int arrivalTime = 0;
        int serviceTime = 0;
        if(minArrTime > maxArrTime || minSerTime > maxSerTime){
            JOptionPane.showMessageDialog(null, "Minim > Maxim", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for(int i=1; i<=numberOfClients; i++){
            if(maxArrTime - minArrTime == 0){
                arrivalTime = minArrTime;
            } else {
                arrivalTime = minArrTime + random.nextInt((maxArrTime - minArrTime) + 1); // [minArrTime, maxArrTime]
            }

            if(maxSerTime - minSerTime == 0){
                serviceTime = minSerTime;
            } else {
                serviceTime = minSerTime + random.nextInt((maxSerTime - minSerTime) + 1); // [minSerTime, maxSerTime]
            }

            Client newClient = new Client(i, arrivalTime, serviceTime);

            generatedClient.add(newClient);
        }
        Collections.sort(generatedClient, Comparator.comparing(Client::getArrivalTime));
    }

    @Override
    public void run(){
        int currentTime = 0;
        boolean lastIteration = false;
        int totalWaitingTime = 0;
        int totalWaitingTimeCalculations = 0;
        int totalServiceTime = 0;
        int totalServingTimeCalculations = 0;

        int maxClients = 0;
        int peakHour = -1;

        if(generatedClient.size() == 0){
            return;
        }

        while (currentTime <= timeLimit) {
            StringBuilder outputString = new StringBuilder();
            outputString.append("Time: ").append(currentTime).append("\n");
            scheduler.changeStrategy(selectionPolicy);


            Iterator<Client> iterator = generatedClient.iterator();
            while (iterator.hasNext()) {
                Client client = iterator.next();
                if(client.getArrivalTime() == currentTime){
                    scheduler.dispatchClient(client);
                    iterator.remove();
                } else {
                    outputString.append(client).append(" ");
                }
            }
            outputString.append("\n");

            // Display Queues
            List<Server> servers = scheduler.getServers();
            for(Server server : servers){
                outputString.append(server).append("\n");
            }

            // Calculate number of iterations and waiting time for AverageWaitingTime
            for(Server server : servers){
                totalWaitingTime += server.getWaitingPeriod();
                totalWaitingTimeCalculations++;
            }

            // Calculate average service time

            for(Server server : scheduler.getServers()){
                for(Client client : server.getClients()){
                    totalServiceTime += client.getServiceTime();
                    totalServingTimeCalculations++;
                }
            }

            // Calculate the peak period

            for(Server server: servers){
                if(server.getNumberOfClients() > maxClients){
                    maxClients = server.getNumberOfClients();
                    peakHour = currentTime;
                }
            }


            //  ---------------------------------------------------------


            outputString.append("\n");

            updateTextArea(outputString.toString());

            if(lastIteration == true){
                break;
            }
            if(generatedClient.isEmpty() && allServersAreEmpty()){
                lastIteration = true;
            }

            currentTime++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Calculate average service time
        double avgServiceTime = (double) totalServiceTime / totalServingTimeCalculations;
        String avgServiceTimeString = String.format("Average service time: %.2f seconds", avgServiceTime);


        // Calculate average waiting time
        double avgWaitingTime = (double) totalWaitingTime / totalWaitingTimeCalculations;
        String avgWaitingTimeString = String.format("Average waiting time: %.2f seconds", avgWaitingTime);


        // Update textArea
        updateTextArea(avgWaitingTimeString);
        updateTextArea(avgServiceTimeString);
        updateTextArea("Peak hour started at time: " + peakHour + "\n");


        saveStringFile(getTextArea(), "logEvent.txt");
    }

    private boolean allServersAreEmpty(){
        for(Server server : scheduler.getServers()){
            if(server.getNumberOfClients() > 0)
                return false;
        }
        return true;
    }

    public static void saveStringFile(String strToSave, String fileName) {
        try{
            BufferedWriter myFile = new BufferedWriter(new FileWriter(fileName));
            myFile.write(strToSave);
            myFile.flush(); // fortez scrierea imediata a datelor
            myFile.close();
            System.out.println("Data was saved!");
        }catch(IOException e){
            System.out.println("Error by saving data: " + e.getMessage());
        }
    }

}
