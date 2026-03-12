package app.utakata.ac.application;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VoicePartAssetsTest {
    @Test
    void shouldUseUniqueAsciiFileNames() {
        HashSet<String> fileNames = new HashSet<>();

        for (String part : VoicePartAssets.supportedParts()) {
            String fileName = VoicePartAssets.fileNameFor(part);
            assertTrue(fileName.chars().allMatch(ch -> ch < 128), "non-ascii file name: " + fileName);
            assertTrue(fileNames.add(fileName), "duplicate file name: " + fileName);
        }

        assertEquals(VoicePartAssets.supportedParts().size(), fileNames.size());
    }
}
