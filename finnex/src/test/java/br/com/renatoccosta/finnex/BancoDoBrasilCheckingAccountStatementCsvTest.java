package br.com.renatoccosta.finnex;

import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BancoDoBrasilCheckingAccountStatementCsvTest {

    @Test
    void verifySignature() throws Exception {
        BancoDoBrasilCheckingAccountStatementCsv parser = new BancoDoBrasilCheckingAccountStatementCsv();
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/bb-cc.csv"),
                "windows-1252");

        assertTrue(parser.verifySignature(reader));
    }

    @Test
    void parse() throws Exception {
        BancoDoBrasilCheckingAccountStatementCsv parser = new BancoDoBrasilCheckingAccountStatementCsv();

        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/bb-cc.csv"),
                "windows-1252");
        StringWriter actual = new StringWriter();
        parser.parse(reader, actual);

        String expected = Files.readString(Paths.get(this.getClass().getResource("/bb-cc-parsed.csv").toURI()),
                StandardCharsets.UTF_8);

        assertEquals(expected, actual.toString());
    }
}