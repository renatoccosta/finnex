// Copyright 2020 Renato Costa
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package br.com.renatoccosta.finnex.parsers;

import org.junit.jupiter.api.Test;

import br.com.renatoccosta.finnex.parsers.impl.BancoDoBrasilCheckingAccountStatementCsvParser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BancoDoBrasilCheckingAccountStatementCsvTest {

    @Test
    void verifySignature() throws Exception {
        BancoDoBrasilCheckingAccountStatementCsvParser parser = new BancoDoBrasilCheckingAccountStatementCsvParser();
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/bb-cc.csv"),
                "windows-1252");

        assertTrue(parser.canParse(reader));
    }

    @Test
    void parse() throws Exception {
        BancoDoBrasilCheckingAccountStatementCsvParser parser = new BancoDoBrasilCheckingAccountStatementCsvParser();

        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/bb-cc.csv"),
                "windows-1252");
        StringWriter actual = new StringWriter();
        parser.parse(reader, actual);

        String expected = Files.readString(Paths.get(this.getClass().getResource("/bb-cc-parsed.csv").toURI()),
                StandardCharsets.UTF_8);

        assertEquals(expected, actual.toString());
    }
}