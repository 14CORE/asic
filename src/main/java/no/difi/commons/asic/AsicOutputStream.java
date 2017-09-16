package no.difi.commons.asic;

import com.google.common.io.ByteStreams;
import no.difi.commons.asic.model.MimeType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Simple encapsulation of ZipOutputStream.
 *
 * @author erlend
 */
class AsicOutputStream extends OutputStream {

    private final ZipOutputStream zipOutputStream;

    public AsicOutputStream(OutputStream outputStream) throws IOException {
        // Initiate ZIP stream
        zipOutputStream = new ZipOutputStream(outputStream);

        // Write comment
        zipOutputStream.setComment("mimetype=" + MimeType.APPLICATION_ASICE.getValue());

        // Write "mimetype" file
        zipOutputStream.putNextEntry(new ZipEntry("mimetype"));
        ByteStreams.copy(new ByteArrayInputStream(MimeType.APPLICATION_ASICE.getValue().getBytes()), zipOutputStream);
        zipOutputStream.closeEntry();
    }

    public void nextEntry(String filename) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filename);

        zipOutputStream.putNextEntry(zipEntry);
    }

    public void closeEntry() throws IOException {
        zipOutputStream.closeEntry();
    }

    @Override
    public void write(int b) throws IOException {
        zipOutputStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        zipOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        zipOutputStream.close();
    }
}