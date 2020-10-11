package ua.epam.finalproject.repairagency.model;

import java.time.LocalDate;
import java.util.Locale;

public class Client extends User {

    private double walletCount;

    public static Client getClientWithInitParams(int id, String email, String password, String personName,
                              Role role, double walletCount, String photoPath, String contactPhone, Locale locale, LocalDate registrationDate)
    {
        User user = getUserWithInitParams(id, email, password, personName, role, photoPath, contactPhone, locale, registrationDate);
        Client client = (Client) user;
        client.walletCount = walletCount;
        return client;
    }

    public double getWalletCount() {
        return walletCount;
    }

    public void setWalletCount(double walletCount) {
        this.walletCount = walletCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", personName='" + personName + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", walletCount='" + walletCount + '\'' +
                ", locale=" + locale +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
