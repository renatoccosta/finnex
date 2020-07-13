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

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

@Component
public class SimpleOFXParser implements Parser {

    @Override
    public boolean verifySignature(Reader input) throws IOException {
        return false;
    }

    @Override
    public void parse(Reader input, Writer output) throws IOException {

    }
}
