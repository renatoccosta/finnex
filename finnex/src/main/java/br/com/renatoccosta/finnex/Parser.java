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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 *
 * @author Renato Costa <renatoccosta@petrobras.com>
 */
public interface Parser {
    
    boolean verifySignature(Reader input) throws IOException;
    
    void parse(Reader input, Writer output) throws IOException;
    
}
