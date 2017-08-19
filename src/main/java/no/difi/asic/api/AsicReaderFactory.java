package no.difi.asic.api;

import no.difi.asic.builder.Builder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author erlend
 */
public interface AsicReaderFactory {

    Builder<AsicReader> openContainer(InputStream inputStream) throws IOException;

    void verifyContainer(InputStream inputStream) throws IOException;
}
