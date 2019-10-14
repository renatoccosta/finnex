package br.com.renatoccosta.finnex;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BancoDoBrasilCheckingAccountStatementCsv implements Parser {
    private static final String HEADER =
            "\"Data\",\"Dependencia Origem\",\"Histórico\",\"Data do Balancete\",\"Número do documento\",\"Valor\",";

    private static final String[] FILTERED_DESCRIPTIONS = new String[] {"Saldo Anterior", "S A L D O"};

    private static final String ACCOUNT_NAME = "Banco do Brasil - C/C";

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
                .filter(this::filterBalanceLine)
                .filter(this::filterBlankLine)
                .map(this::parseLine)
                .forEach(csvWriter::writeNext);
    }

    private boolean filterBlankLine(String[] line) {
        return !(line.length == 1 && (line[0] == null || line[0].trim().equals("")));
    }

    private boolean filterBalanceLine(String[] line) {
        return Stream.of(FILTERED_DESCRIPTIONS).allMatch(s -> !s.equals(line[2]));
    }

    private String[] parseLine(String[] line) {
        String date = fixDate(line[0]);
        String description = line[2];
        String value = line[5];

        return new String[] {date, description, ACCOUNT_NAME, "", value};
    }

    private String fixDate(String s) {
        Pattern p = Pattern.compile("(\\d\\d)/(\\d\\d)/(\\d\\d\\d\\d)");
        Matcher m = p.matcher(s);

        return m.replaceAll("$2/$1/$3");
    }
}