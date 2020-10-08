package ua.epam.finalproject.repairagency.to;

import ua.epam.finalproject.repairagency.model.Client;

import java.time.LocalDate;
import java.util.Locale;

public class ClientTo {
    private int id;
    private String email;
    private String clientName;
    private double walletCount;
    private String photoPath;
    private String contactPhone;
    private Locale locale;
    private LocalDate registrationDate;

    /**
     * Creates Client object with initiated specific fields without password
     *
     * @param id
     * @param email
     * @param clientName
     * @param walletCount
     * @param contactPhone
     * @param locale
     * @param registrationDate
     * @return Client object with fully settings
     */
    public static ClientTo getClientToWithInitParams(
            int id, String email, String clientName, double walletCount, String photoPath,
            String contactPhone, Locale locale, LocalDate registrationDate)
    {
        ClientTo clientTo = new ClientTo();
        clientTo.id = id;
        clientTo.email = email;
        clientTo.clientName = clientName;
        clientTo.walletCount = walletCount;
        clientTo.photoPath = photoPath;
        clientTo.contactPhone = contactPhone;
        clientTo.locale = locale;
        clientTo.registrationDate = registrationDate;
        return clientTo;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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
        return "ClientTo{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", clientName='" + clientName + '\'' +
                ", walletCount=" + walletCount +
                ", photoPath='" + photoPath + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", locale=" + locale +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
