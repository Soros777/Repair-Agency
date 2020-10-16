package ua.epam.finalproject.repairagency.model;

public enum Status {

    NEW("New"),
    WAIT_FOR_PAY("Wait_For_Pay"),
    PAYED("Payed"),
    CANCELED("Canceled"),
    WORKING("Working"),
    MADE("Made");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public boolean valueEqualsTo(String name) {
        return value.equals(name);
    }

    public String getValue() {
        return value;
    }

    public static Status fromString(String roleStr) {
        for (Status status : values()) {
            if(status.value.equalsIgnoreCase(roleStr)) {
                return status;
            }
        }
        return null;
    }
}
