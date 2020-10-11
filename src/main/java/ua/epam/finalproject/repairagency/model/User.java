package ua.epam.finalproject.repairagency.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class User {
    protected int id;
    protected String email;
    protected String password;
    protected String personName;
    protected Role role;
    protected String photoPath;
    protected String contactPhone;
    protected Locale locale;
    protected LocalDate registrationDate;

    public static User getUserWithInitParams(int id, String email, String password, String personName,
                           Role role, String photoPath, String contactPhone, Locale locale, LocalDate registrationDate)
    {
        User user = new User();
        user.id = id;
        user.email = email;
        user.password = password;
        user.personName = personName;
        user.role = role;
        user.photoPath = photoPath;
        user.contactPhone = contactPhone;
        user.locale = locale;
        user.registrationDate = registrationDate;
        return user;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(personName, user.personName) &&
                role == user.role &&
                Objects.equals(photoPath, user.photoPath) &&
                Objects.equals(contactPhone, user.contactPhone) &&
                Objects.equals(locale, user.locale) &&
                Objects.equals(registrationDate, user.registrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, personName, role, photoPath, contactPhone, locale, registrationDate);
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
                ", locale=" + locale +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
