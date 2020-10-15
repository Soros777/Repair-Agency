package ua.epam.finalproject.repairagency.model;

import java.time.LocalDate;

public class Order {
    private int id;
    private Client createdByClient;
    private Device device;
    private String description;
    private User master;
    private User manager;
    private double cost;
    private Status status;
    private LocalDate createdDate;

    public Order() {}

    public Order(int id, Client createdByClient, Device device, String description, User master, User manager, double cost, Status status, LocalDate createdDate) {
        this.id = id;
        this.createdByClient = createdByClient;
        this.device = device;
        this.description = description;
        this.master = master;
        this.manager = manager;
        this.cost = cost;
        this.status = status;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getCreatedByClient() {
        return createdByClient;
    }

    public void setCreatedByClient(Client createdByClient) {
        this.createdByClient = createdByClient;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createdByClient=" + createdByClient +
                ", device=" + device +
                ", description='" + description + '\'' +
                ", master=" + master +
                ", manager=" + manager +
                ", cost=" + cost +
                ", status=" + status +
                ", createdDate=" + createdDate +
                '}';
    }
}
