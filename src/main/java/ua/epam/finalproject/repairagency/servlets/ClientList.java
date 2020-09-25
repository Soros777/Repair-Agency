package ua.epam.finalproject.repairagency.servlets;

import ua.epam.finalproject.repairagency.model.Client;

import java.util.HashMap;
import java.util.Map;

public class ClientList {

    private static Map<String, Client> clients = new HashMap<>();

    public static Client findClient(String clientEmail) {
        return clients.get(clientEmail);
    }

    public static boolean addClient(Client client) {
        boolean result = false;
        if(!clients.containsKey(client.getEmail())
                && client.getPassword() != null
                && !"".equals(client.getPassword()))
        {
            clients.put(client.getEmail(), client);
            result = true;
        }
        return result;
    }
}
