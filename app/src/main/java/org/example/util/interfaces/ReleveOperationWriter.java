package org.example.util.interfaces;

import org.example.models.Client;
import org.example.util.exceptions.ModelProviderException;

import java.io.OutputStream;
import java.time.YearMonth;

/**
 * Interface for writing a "relevé d'opération to an output stream
 */
public interface ReleveOperationWriter {
    /**
     * Writes a "relevé d'opération" to an output stream
     *
     * @param client the client for which the "relevé d'opération" is written
     * @param month the month for which the "relevé d'opération" is written
     * @param out    the output stream to write the "relevé d'opération" to
     */
    void writeReleveOperation(Client client, YearMonth month, OutputStream out) throws ModelProviderException;
}
