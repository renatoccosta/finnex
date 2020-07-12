package br.com.renatoccosta.finnex.parsers;

import br.com.renatoccosta.finnex.Parser;
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
