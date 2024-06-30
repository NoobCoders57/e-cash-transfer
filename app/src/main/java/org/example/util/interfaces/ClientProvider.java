package org.example.util.interfaces;

import org.example.models.Client;

import java.sql.SQLException;

public interface ClientProvider {
    Client getClient(String numtel) throws SQLException;
}
