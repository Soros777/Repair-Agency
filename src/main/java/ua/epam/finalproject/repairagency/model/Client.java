package ua.epam.finalproject.repairagency.model;

import java.time.LocalDate;
import java.util.Locale;

public class Client {

    private int id;
    private String email;
    private String password;
    private String clientName;
    private double walletCount;
    private String contactPhone;
    private Locale locale;
    private LocalDate registrationDate;

    public static Client getClientWithInitParams(
            int id, String email, String password,
            String clientName, double walletCount,
            String contactPhone, Locale locale,
            LocalDate registrationDate)
    {
        Client client = new Client();
        client.id = id;
        client.email = email;
        client.password = password;
        client.clientName = clientName;
        client.walletCount = walletCount;
        client.contactPhone = contactPhone;
        client.locale = locale;
        client.registrationDate = registrationDate;
        return client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public double getWalletCount() {
        return walletCount;
    }

    public void setWalletCount(double walletCount) {
        this.walletCount = walletCount;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", clientName='" + clientName + '\'' +
                ", walletCount=" + walletCount +
                ", contactPhone='" + contactPhone + '\'' +
                ", locale=" + locale +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
