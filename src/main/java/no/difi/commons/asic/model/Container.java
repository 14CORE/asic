package no.difi.commons.asic.model;

import no.difi.commons.asic.api.MessageDigestAlgorithm;
import no.difi.commons.asic.lang.AsicException;
import no.difi.commons.asic.lang.ChecksumException;
import no.difi.commons.asic.security.MultiMessageDigest;

import java.io.Serializable;
import java.util.*;

/**
 * @author erlend
 */
public class Container implements Serializable {

    private static final long serialVersionUID = -5478541041467973689L;

    private final Mode mode;

    private final Map<String, DataObject> dataObjects = new HashMap<>();

    private final List<Signer> signers = new ArrayList<>();

    private String rootFile;

    public Container(Mode mode) {
        this.mode = mode;
    }

    public String getRootFile() {
        return rootFile;
    }

    public void setRootFile(String rootFile) throws AsicException {
        if (this.rootFile != null)
            throw new AsicException("Root file is already set.");

        if (!dataObjects.containsKey(rootFile))
            throw new AsicException(String.format("File '%s' is not known.", rootFile));

        this.rootFile = rootFile;
    }

    public void update(String filename, DataObject.Type type, MimeType mimeType) {
        DataObject dataObject = getDataObject(filename);
        dataObject.setType(type);
        dataObject.setMimeType(mimeType);
    }

    public void update(String filename, MultiMessageDigest messageDigest) throws AsicException {
        DataObject dataObject = getDataObject(filename);

        if (!dataObject.getHash().update(messageDigest))
            throw new ChecksumException(String.format("Invalid checksum for '%s'.", filename));
    }

    public void verify(Signer signer, String filename, MessageDigestAlgorithm algorithm, byte[] digest)
            throws AsicException {
        if (mode == Mode.WRITER)
            throw new IllegalStateException("Verification of content is performed when reading a container.");

        if (!dataObjects.get(filename).verify(signer, algorithm, digest))
            throw new ChecksumException(String.format("Unable to verify digest for file '%s'.", filename));

        if (!signers.contains(signer))
            signers.add(signer);
    }

    public Collection<DataObject> getDataObjects() {
        return Collections.unmodifiableCollection(dataObjects.values());
    }

    private DataObject getDataObject(String filename) {
        if (dataObjects.containsKey(filename))
            return dataObjects.get(filename);

        DataObject dataObject = new DataObject(filename);
        dataObjects.put(filename, dataObject);
        return dataObject;
    }

    public void finish() throws AsicException {

    }

    public Manifest toManifest() {
        return new Manifest(rootFile,
                Collections.unmodifiableCollection(dataObjects.values()),
                Collections.unmodifiableList(signers));
    }

    public enum Mode {
        READER,
        WRITER
    }
}