package br.com.renatoccosta.finnex.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;

import org.springframework.stereotype.Component;

import br.com.renatoccosta.finnex.Parser;

@Component
public class BancoDoBrasilSavingsAccountCsv implements Parser {
    private static final String HEADER = "\"Data\",\"Histórico\",\"Valor\",";

    private static final String ACCOUNT_NAME = "Banco do Brasil - C/P";

    private static final String DEBIT = "D";

    private static final String CREDIT = "C";

    @Override
    public boolean verifySignature(Reader input) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(input);
        String header = bufferedReader.readLine();

        return HEADER.equals(header);
    }

    @Override
    public void parse(Reader input, Writer output) throws IOException {
        CSVWriter csvWriter = new CSVWriter(output);

        Iterable<String[]> iterable = () -> {
            try {
                return new CSVReaderHeaderAware(input).iterator();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        StreamSupport.stream(iterable.spliterator(), false)
                .filter(this::filterBlankLine)
                .map(this::parseLine)
                .forEach(csvWriter::writeNext);
    }

    private boolean filterBlankLine(String[] line) {
        return !(line.length == 1 && (line[0] == null || line[0].trim().equals("")));
    }

    private String[] parseLine(String[] line) {
        String date = line[0];
        String description = line[1];
        String value = fixValue(line[2]);

        return new String[]{date, description, ACCOUNT_NAME, "", value};
    }

    private String fixValue(String value) {
        Pattern p = Pattern.compile("(\\d+\\,\\d\\d) ([DC])");
        Matcher m = p.matcher(value);

        if (m.matches()) {
            if (DEBIT.equals(m.group(2))) {
                return m.replaceAll("-$1");
            } else {
                return m.replaceAll("$1");
            }
        }

        return value;
    }

}
