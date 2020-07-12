package br.com.renatoccosta.finnex.parsers;

import br.com.renatoccosta.finnex.Parser;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class BancoDoBrasilInvestmentFundsTxt implements Parser {
    private static final String HEADER = "EXTRATO DE FUNDOS DE INVESTIMENTOS";

    private static final String[] FILTERED_DESCRIPTIONS = new String[]{"SALDO"};

    private static final Pattern FUND_NAME_CNPJ = Pattern.compile(
            "(.+?)\\s+\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}\\s*");

    private static final Pattern ENTRY = Pattern.compile(
            "\\s+(\\d{2}\\/\\d{2}\\/\\d{4})\\s+(.+?)\\s+([\\d\\.]+\\,\\d{2})\\s+");

    @Override
    public boolean verifySignature(Reader input) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(input);

        return bufferedReader.lines()
                .filter(s -> HEADER.equals(s.trim()))
                .findFirst()
                .isEmpty();
    }

    @Override
    public void parse(Reader input, Writer output) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(input);

        String accountName = "";
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Matcher matcher = FUND_NAME_CNPJ.matcher(line);
            if (matcher.matches()) {
                accountName = matcher.replaceAll("$1");
                break;
            }
        }

        CSVWriter csvWriter = new CSVWriter(output);

        bufferedReader.lines()
                .filter(this::removeBlankLine)
                .filter(this::removeBalanceLine)
                .filter(this::entryLine)
                .map(this.lineParser(accountName))
                .forEach(csvWriter::writeNext);
    }

    private boolean removeBlankLine(String s) {
        return !"".equals(s.trim());
    }

    private boolean removeBalanceLine(String line) {
        return Stream.of(FILTERED_DESCRIPTIONS).allMatch(s -> !line.contains(s));
    }

    private boolean entryLine(String line) {
        return ENTRY.matcher(line).find();
    }

    private Function<String, String[]> lineParser(final String accountName) {
        return s -> {
            Matcher matcher = ENTRY.matcher(s);
            matcher.find();

            String date = matcher.group(1);
            String description = matcher.group(2);
            String value = matcher.group(3);

            return new String[]{date, description, accountName, "", value};
        };
    }

}
