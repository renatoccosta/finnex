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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import br.com.renatoccosta.finnex.domain.Statement;
import io.reactivex.rxjava3.core.Observable;

/**
 * Generic parser interface that all parsers needs to implement.
 */
public interface Parser {

    /**
     * Verifies if this parser can understand the document and parse its data
     * 
     * @param input Document stream
     * @return Whether it can or not parse the data
     * @throws IOException If any problems happens during read
     */
    boolean canParse(Reader input) throws IOException;

    void parse(Reader input, Writer output) throws IOException;

    Observable<Statement> parse(Reader input);
}
