package no.difi.asic;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class AsicInputStream extends ZipInputStream {

    public static final Logger log = LoggerFactory.getLogger(AsicInputStream.class);

    public AsicInputStream(InputStream in) {
        super(in);
    }

    @Override
    public ZipEntry getNextEntry() throws IOException {
        ZipEntry zipEntry = super.getNextEntry();

        if (zipEntry != null && zipEntry.getName().equals("mimetype")) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(this, baos);

            log.info(String.format("Content of mimetype: %s", baos.toString()));
            if (!AbstractAsicWriter.APPLICATION_VND_ETSI_ASIC_E_ZIP.equals(baos.toString()))
                throw new IllegalStateException("Content is not ASiC-E container.");

            // Fetch next
            zipEntry = super.getNextEntry();
        }

        return zipEntry;
    }
}