package utcn.pt.model;

public class Client {
    private int id;
    private int arrivalTime;
    private int serviceTime;

    public Client(int id, int arrivalTime, int serviceTime){
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int decrementServiceTime(){
        this.serviceTime--;
        return this.serviceTime;
    }

    @Override
    public String toString() {
        return "(" + id + ", " + arrivalTime + ", " + serviceTime + ")";
    }
}
