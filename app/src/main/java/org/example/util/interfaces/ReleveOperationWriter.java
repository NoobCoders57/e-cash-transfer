package org.example.util.interfaces;

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
     * @param numClient the client number for which the "relevé d'opération" is written
     * @param month the month for which the "relevé d'opération" is written
     * @param out    the output stream to write the "relevé d'opération" to
     */
    void writeReleveOperation(String numClient, YearMonth month, OutputStream out) throws ModelProviderException;
}
