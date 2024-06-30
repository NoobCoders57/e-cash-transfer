package org.example.util.interfaces;

import org.example.models.Envoyer;
import org.example.util.exceptions.ModelProviderException;

import java.util.Date;
import java.util.List;

/**
 * Interface that provides the method to get all transactions of a client
 */
public interface EnvoyerProvider {
    /**
     * Get all transactions of a client
     * @param clientNumTel the client's phone number
     * @param start the start date
     * @param end the end date
     * @return a list of transactions
     * @see Envoyer
     */
    List<Envoyer> allTransactions(String clientNumTel, Date start, Date end) throws ModelProviderException;

    /**
     * Get all transactions where the client is the cash receiver
     * @param clientNumTel the client's phone number
     * @param start the start date
     * @param end the end date
     * @return a list of transactions
     * @see Envoyer
     */
    List<Envoyer> receivedTransactions(String clientNumTel, Date start, Date end) throws ModelProviderException;

    /**
     * Get all transactions where the client is the cash sender
     * @param clientNumTel the client's phone number
     * @param start the start date
     * @param end the end date
     * @return a list of transactions
     * @see Envoyer
     */
    List<Envoyer> sentTransactions(String clientNumTel, Date start, Date end) throws ModelProviderException;
}
