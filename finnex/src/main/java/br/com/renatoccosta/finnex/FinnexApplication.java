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
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Renato Costa <renatoccosta@petrobras.com>
 */
@SpringBootApplication
public class FinnexApplication {

    private final static Logger LOGGER = Logger.getLogger(FinnexApplication.class.getName());

    private final List<Parser> parsers;

    private final ApplicationArguments args;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FinnexApplication.class, args);
    }

    public FinnexApplication(List<Parser> parsers, ApplicationArguments args) {
        this.parsers = parsers;
        this.args = args;
    }

    @PostConstruct
    private void init() {
        String[] rawArgs = args.getSourceArgs();

        if (rawArgs.length < 1) {
            throw new IllegalArgumentException("Input filenames as arguments");
        }

        Stream.of(rawArgs)
                .map(File::new)
                .forEach(fileInput -> {
                    try {
                        String charset = UniversalDetector.detectCharset(fileInput);
                        parsers.stream()
                                .filter(p -> {
                                    try (Reader reader = new FileReader(fileInput, Charset.forName(charset))) {
                                        return p.verifySignature(reader);
                                    } catch (IOException e) {
                                        LOGGER.warning(e.getMessage());
                                        return false;
                                    }
                                })
                                .findFirst()
                                .ifPresent(p -> {
                                    File fileOutput = new File(fileInput.getPath() + "_");
                                    try (Reader reader = new FileReader(fileInput, Charset.forName(charset));
                                         Writer writer = new FileWriter(fileOutput, StandardCharsets.UTF_8)) {
                                        p.parse(reader, writer);
                                    } catch (IOException e) {
                                        LOGGER.warning(e.getMessage());
                                    }
                                });
                    } catch (IOException e) {
                        LOGGER.warning(e.getMessage());
                    }
                });
    }

}
