package ua.epam.finalproject.repairagency.model;

public enum Device {

    COMPUTER("Computer"),
    LAPTOP("Laptop"),
    SMARTPHONE("Smartphone"),
    TABLET("Tablet"),
    E_READER("E-reader");

    private String value;

    Device(String value) {
        this.value = value;
    }

    public boolean valueEqualsTo(String name) {
        return value.equals(name);
    }

    public String value() {
        return value;
    }
}
