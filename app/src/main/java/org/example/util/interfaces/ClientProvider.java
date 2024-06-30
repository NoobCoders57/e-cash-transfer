package org.example.util.interfaces;

import org.example.models.Client;
import org.example.util.exceptions.ModelProviderException;

/**
 * Interface that provides the method to get a client
 */
public interface ClientProvider {
    /**
     * Get a client by its phone number
     * @param numtel the client's phone number
     * @return the client
     * @throws ModelProviderException if an error occurs while fetching the client
     * @see Client
     */
    Client getClient(String numtel) throws ModelProviderException;
}
