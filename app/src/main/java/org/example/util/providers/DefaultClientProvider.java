package org.example.util.providers;

import org.example.dao.ClientDao;
import org.example.models.Client;
import org.example.util.exceptions.ModelProviderException;
import org.example.util.interfaces.ClientProvider;

import java.sql.SQLException;

/**
 * Default client provider
 */
public class DefaultClientProvider implements ClientProvider {
    private final ClientDao clientDao;

    /**
     * Create a new DefaultClientProvider with the given client DAO
     *
     * @param clientDao Client DAO
     * @see ClientDao
     */
    public DefaultClientProvider(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    /**
     * @throws ModelProviderException If an {@link SQLException} occurs while fetching the client
     */
    @Override
    public Client getClient(String numTel) throws ModelProviderException {
        try {
            return clientDao.getClient(numTel);
        } catch (SQLException e) {
            throw new ModelProviderException("Error while fetching client", e);
        }
    }
}