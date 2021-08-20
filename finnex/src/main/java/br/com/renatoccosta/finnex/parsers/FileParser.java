package br.com.renatoccosta.finnex.parsers;

import br.com.renatoccosta.finnex.domain.Statement;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data @Builder public class FileParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileParser.class);
    
    private File file;

    private Parser parser;

    private Charset charset;
    
    private static final DecimalFormat decimalFormat = createFormat();

    private static DecimalFormat createFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        String pattern = "#.##0,0#";

        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        return decimalFormat;
    }

    public void export() {
        try (Reader reader = new FileReader(file, charset);
                Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
//            parser.parse(reader)
//                    .map();
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }
    
    private String[] statementToArray(Statement statement) {
        return new String[] {
            statement.getAccount(),
            formatDate(statement.getDatePosted()),
            formatDate(statement.getDateAsOf()),
            statement.getDescription(),
            statement.getCategory(),
            statement.getAnnotations(),
            decimalFormat.format(statement.getValue()),
            statement.getCurrencyRate().toString(),
            statement.getCurrencySymbol(),
            statement.getOriginalId()
        };
    }

    private static String formatDate(Instant instant) {
        return DateTimeFormatter.ISO_DATE_TIME.format(instant);
    }

    private String formatValue(BigDecimal value) {
        return null;
    }

}