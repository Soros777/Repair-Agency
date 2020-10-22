package ua.epam.finalproject.repairagency.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class Client extends User {

    private double walletCount;
    private boolean status;

    public static Client getWithInitParams(int id, String email, String password, String personName,
                                           Role role, double walletCount, String photoPath, String contactPhone, Locale locale, LocalDate registrationDate, boolean status)
    {
        Client client = new Client();
        client.id = id;
        client.email = email;
        client.password = password;
        client.personName = personName;
        client.role = role;
        client.walletCount = walletCount;
        client.photoPath = photoPath;
        client.contactPhone = contactPhone;
        client.locale = locale;
        client.registrationDate = registrationDate;
        client.status = status;
        return client;
    }

    public static Client getWithInitParams(int id, String email, String password, String personName,
                                           Role role, String photoPath, String contactPhone, Locale locale, String registrationDate)
    {
        Client client = new Client();
        client.id = id;
        client.email = email;
        client.password = password;
        client.personName = personName;
        client.role = role;
        client.photoPath = photoPath;
        client.contactPhone = contactPhone;
        client.locale = locale;
        client.registrationDate = LocalDate.parse(registrationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return client;
    }

    public double getWalletCount() {
        return walletCount;
    }

    public void setWalletCount(double walletCount) {
        this.walletCount = walletCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Double.compare(client.walletCount, walletCount) == 0 &&
                status == client.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), walletCount, status);
    }

    @Override
    public String toString() {
        return "Client{" +
                "walletCount=" + walletCount +
                ", status=" + status +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", personName='" + personName + '\'' +
                ", role=" + role +
                ", photoPath='" + photoPath + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", locale=" + locale +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
