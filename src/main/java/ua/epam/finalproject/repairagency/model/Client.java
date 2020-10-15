package ua.epam.finalproject.repairagency.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class Client extends User {

    private double walletCount;

    public static Client getWithInitParams(int id, String email, String password, String personName,
                                           Role role, double walletCount, String photoPath, String contactPhone, Locale locale, LocalDate registrationDate)
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
        return client;
    }

    public double getWalletCount() {
        return walletCount;
    }

    public void setWalletCount(double walletCount) {
        this.walletCount = walletCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Double.compare(client.walletCount, walletCount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), walletCount);
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
