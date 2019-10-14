/*
 * Copyright 2016 Renato Costa <renatoccosta@petrobras.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.renatoccosta.finnex;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Renato Costa <renatoccosta@petrobras.com>
 */
public class Main {

    private static final Parser[] PARSERS = new Parser[]{
        new OurocardParser(),
        new ItaucardParser()
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input filename as first argument");
        }

        File fileInput = new File(args[0]);
        File fileOutput = new File(fileInput.getPath() + "_");

        Parser foundParser = null;

        for (Parser parser : PARSERS) {
            try (Reader reader = new FileReader(args[0])) {
                if (parser.verifySignature(reader)) {
                    foundParser = parser;
                    break;
                }
            }
        }

        if (foundParser != null) {
            try (Reader reader = new FileReader(fileInput);
                    Writer writer = new FileWriter(fileOutput, StandardCharsets.UTF_8)) {
                foundParser.parse(reader, writer);
            }
        }
    }

}
