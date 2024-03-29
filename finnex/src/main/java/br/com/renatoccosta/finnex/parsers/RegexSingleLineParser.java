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
package br.com.renatoccosta.finnex.parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexSingleLineParser implements Parser {
    
    @Override
    public boolean canParse(Reader input) throws IOException {
        BufferedReader inputReader = new BufferedReader(input);
        String line;
        while ((line = inputReader.readLine()) != null) {
            Matcher m = getSignature().matcher(line);
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
            boolean found = false;
            for (int i = 0; i < getParsePatterns().length; i++) {
                Matcher m = getParsePatterns()[i].matcher(line);
                if (m.matches()) {
                    found = true;
                    line = m.replaceAll(getReplaceStrings()[i]);
                }
            }
            if (found) {
                writer.append(line);
                writer.newLine();
            }
        }
        writer.flush();
    }
    
    protected abstract Pattern getSignature();
    
    protected abstract Pattern[] getParsePatterns();
    
    protected abstract String[] getReplaceStrings();
    
}
