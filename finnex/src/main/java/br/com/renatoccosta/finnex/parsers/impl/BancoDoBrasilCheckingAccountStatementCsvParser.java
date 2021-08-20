// Copyright 2020 renat
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
package br.com.renatoccosta.finnex.parsers.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;
import org.springframework.stereotype.Component;

import br.com.renatoccosta.finnex.domain.Statement;
import br.com.renatoccosta.finnex.parsers.Parser;
import io.reactivex.rxjava3.core.Observable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class BancoDoBrasilCheckingAccountStatementCsvParser implements Parser {

    private static final String HEADER = "\"Data\",\"Dependencia Origem\",\"Histórico\",\"Data do Balancete\",\"Número do documento\",\"Valor\",";

    private static final String[] FILTERED_DESCRIPTIONS
            = new String[]{"Saldo Anterior", "S A L D O"};

    private static final String ACCOUNT_NAME = "Banco do Brasil - C/C";

    private static final DateTimeFormatter dateFormatter
            = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DecimalFormat decimalFormat = createFormat();

    private static DecimalFormat createFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        String pattern = "#,##0.0#";

        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        return decimalFormat;
    }

    @Override
    public boolean canParse(Reader input) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(input);
        String header = bufferedReader.readLine();

        return HEADER.equals(header);
    }

    @Override
    public void parse(Reader input, Writer output) throws IOException {
        // CSVWriter csvWriter = new CSVWriter(output);

        // Iterable<String[]> iterable = () -> {
        // try {
        // return new CSVReaderHeaderAware(input).iterator();
        // } catch (IOException e) {
        // throw new RuntimeException(e);
        // }
        // };
        // StreamSupport.stream(iterable.spliterator(), false)
        // .filter(this::filterBalanceLine)
        // .filter(this::filterBlankLine)
        // .map(this::parseLine)
        // .forEach(csvWriter::writeNext);
    }

    @Override
    public Observable<Statement> parse(Reader input) {
        return Observable.<Statement>create(emitter -> {
            try (CSVReader csvReader = new CSVReaderHeaderAware(input)) {
                Iterable<String[]> iterable = () -> {
                    return csvReader.iterator();
                };

                StreamSupport.stream(iterable.spliterator(), false)
                        .filter(this::filterBalanceLine)
                        .filter(this::filterBlankLine)
                        .map(this::lineToStatement)
                        .forEach(emitter::onNext);

                emitter.onComplete();

            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    private boolean filterBlankLine(String[] line) {
        return !(line.length == 1 && (line[0] == null || line[0].trim().equals("")));
    }

    private boolean filterBalanceLine(String[] line) {
        return Stream.of(FILTERED_DESCRIPTIONS).allMatch(s -> !s.equals(line[2]));
    }

    private Statement lineToStatement(String[] line) {
        try {
            Instant date = dateFormatter.parse(line[0], Instant::from);

            return Statement.builder()
                    .dateAsOf(date)
                    .datePosted(date)
                    .account(ACCOUNT_NAME)
                    .description(line[2])
                    .value((BigDecimal) decimalFormat.parse(line[5]))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
