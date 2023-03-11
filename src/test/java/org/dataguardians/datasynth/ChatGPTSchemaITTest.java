package org.dataguardians.datasynth;

import org.apache.commons.io.IOUtils;
import org.dataguardians.datasynth.schema.Schema;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ChatGPTSchemaITTest {

    private static final String PHONE_REGEX = "\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})";

    @Test
    public void testPhoneNumber() throws IOException {
        final String jsonText = IOUtils.toString(
                this.getClass().getResourceAsStream("/samplers/textMessage.json"),
                "UTF-8"
        );

        Schema schema = Schema.builder().from(jsonText).build();

        SchemaSynthesizer synthesizer = new SchemaSynthesizer(schema);
        var record = synthesizer.generateRecords(1);
        Assert.assertEquals(1, record.size());
    }
}