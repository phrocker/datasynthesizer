package org.dataguardians.datasynth.code.quality.code.comment;

import org.apache.commons.io.IOUtils;
import org.dataguardians.datasynth.code.java.JavaDocParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JavaDocParserTest {

    @Test
    public void testParsing() throws IOException {
        final String txt = IOUtils.toString(this.getClass().getResourceAsStream("/commentParserClass"), "UTF-8");

        String out = JavaDocParser.parseClassJavaDoc("CommentGenerator", txt);
        Assert.assertTrue(out.contains("input"));
    }

    @Test
    public void testFail() throws IOException {
        final String txt = IOUtils.toString(this.getClass().getResourceAsStream("/commentParserClassFail"), "UTF-8");

        String out = JavaDocParser.parseClassJavaDoc("CommentGenerator", txt);
        Assert.assertTrue(out.contains("QueryConfiguration"));
    }
}
