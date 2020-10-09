package ua.epam.finalproject.repairagency.model;

public class Client extends User {

    private double walletCount;

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
