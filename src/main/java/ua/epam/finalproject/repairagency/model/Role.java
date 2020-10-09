package ua.epam.finalproject.repairagency.model;

public enum Role {

    DIRECTOR("Director"),
    CLIENT("Client"),
    ADMINISTRATOR("Administrator"),
    MANAGER("Manager"),
    MASTER("Master");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public boolean valueEqualsTo(String name) {
        return value.equals(name);
    }

    public String value() {
        return value;
    }
}
