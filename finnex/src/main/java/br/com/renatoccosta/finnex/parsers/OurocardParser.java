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
package br.com.renatoccosta.finnex.parsers;

import br.com.renatoccosta.finnex.RegexSingleLineParser;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 *
 * @author Renato Costa <renatoccosta@petrobras.com>
 */
@Component
public class OurocardParser extends RegexSingleLineParser {

    private final Pattern signature = Pattern.compile("OUROCARD");

    private final Pattern[] parsePatterns = new Pattern[]{
        Pattern.compile("(\\d\\d/\\d\\d) +(.+?) +([\\d\\.,-]+) +([\\d\\.,-]+)"),
        Pattern.compile("(.+?)\\t(.+?)\\s+PARC (\\d\\d/\\d\\d).+?\\t\\t(.+?)\\t(.+)"),
        Pattern.compile("(.+?)\\t([^\\t]{22,}?)\\s+.+?\\t\\t(.+?)\\t(.+)"),
        Pattern.compile("\\s+\\t")
    };

    private final String[] replaces = new String[]{
        "$1\t$2\t\t$3\t$4",
        "$1\t$2\t$4\t$3\t$5",
        "$1\t$2\t$3\t\t$4",
        "\t"
    };

    @Override
    protected Pattern getSignature() {
        return signature;
    }

    @Override
    protected Pattern[] getParsePatterns() {
        return parsePatterns;
    }

    @Override
    protected String[] getReplaceStrings() {
        return replaces;
    }

}
