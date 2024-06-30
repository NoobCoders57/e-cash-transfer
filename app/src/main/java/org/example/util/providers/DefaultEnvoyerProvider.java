package org.example.util.providers;

import org.example.dao.EnvoyerDao;
import org.example.models.Envoyer;
import org.example.util.exceptions.ModelProviderException;
import org.example.util.interfaces.EnvoyerProvider;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DefaultEnvoyerProvider implements EnvoyerProvider {
    private final EnvoyerDao envoyerDao;

    public DefaultEnvoyerProvider(EnvoyerDao envoyerDao) {
        this.envoyerDao = envoyerDao;
    }

    @Override
    public List<Envoyer> allTransactions(String clientNumTel, Date start, Date end) throws ModelProviderException {
        try {
            return envoyerDao.allTransactions(clientNumTel, start, end);
        } catch (SQLException e) {
            throw new ModelProviderException("Error while fetching transactions", e);
        }
    }

    @Override
    public List<Envoyer> receivedTransactions(String clientNumTel, Date start, Date end) throws ModelProviderException {
        try {
            return envoyerDao.receivedTransactions(clientNumTel, start, end);
        } catch (SQLException e) {
            throw new ModelProviderException("Error while fetching transactions", e);
        }
    }

    @Override
    public List<Envoyer> sentTransactions(String clientNumTel, Date start, Date end) throws ModelProviderException {
        try {
            return envoyerDao.sentTransactions(clientNumTel, start, end);
        } catch (SQLException e) {
            throw new ModelProviderException("Error while fetching transactions", e);
        }
    }
}
