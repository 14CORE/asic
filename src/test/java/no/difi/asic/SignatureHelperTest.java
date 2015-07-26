package no.difi.asic;

import static org.testng.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class SignatureHelperTest {

    private static Logger log = LoggerFactory.getLogger(SignatureHelperTest.class);

    @Test
    public void loadNoProblems() {
        new SignatureHelper(getClass().getResourceAsStream("/kontaktinfo-client-test.jks"), "changeit", null, "changeit");
    }


    @Test
    public void wrongKeystorePassword() {
        try {
            new SignatureHelper(getClass().getResourceAsStream("/kontaktinfo-client-test.jks"), "changed?", null, "changeit");
            fail("Exception expected.");
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void wrongKeyPassword() {
        try {
            new SignatureHelper(getClass().getResourceAsStream("/kontaktinfo-client-test.jks"), "changeit", null, "changed?");
            fail("Exception expected.");
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void wrongKeyAlias() {
        try {
            new SignatureHelper(getClass().getResourceAsStream("/kontaktinfo-client-test.jks"), "changeit", "asic", "changeit");
            fail("Exception expected.");
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
        }
    }
}
