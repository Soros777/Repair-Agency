package ua.epam.finalproject.repairagency.resource;

import java.util.ResourceBundle;

public class ConfigurationManager {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.config");
    // класс извлекает информацию из файла config.properties
    private ConfigurationManager() {}

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }


}
