/*
 * Copyright 2016 Renato Costa <renatoccosta@gmail.com>.
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
package br.com.renatoccosta.finnex.parsers.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import br.com.renatoccosta.finnex.parsers.Parser;

@Component
public class ItaucardParser implements Parser {

    private final Pattern signature = Pattern.compile("Itaucard");

    private final Pattern parsePattern = Pattern.compile("(\\d\\d/\\d\\d) (.+?) ((- )?\\d+,\\d\\d)");

    private final String replace = "$1\t$2\t$3";

    @Override
    public boolean verifySignature(Reader input) throws IOException {
        BufferedReader inputReader = new BufferedReader(input);
        
        String line;
        while ((line = inputReader.readLine()) != null) {
            Matcher m = signature.matcher(line);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void parse(Reader input, Writer output) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        BufferedWriter writer = new BufferedWriter(output);
        
        String line;
        while ((line = reader.readLine()) != null) {
            Matcher m = parsePattern.matcher(line);
            while (m.find()) {
                line = line.substring(m.start(), m.end());
                line = parsePattern.matcher(line).replaceAll(replace);
                writer.append(line);
                writer.newLine();
            }
        }
        writer.flush();
    }

}
