package br.com.renatoccosta.finnex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Renato Costa <renatoccosta@petrobras.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input filename as first argument");
        }

        File fileInput = new File(args[0]);
        File fileOutput = new File(fileInput.getPath() + "_");

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            Pattern key = Pattern.compile("OUROCARD");

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = key.matcher(line);
                if (m.find()) {
                    found = true;
                    break;
                }
            }
        }

        if (found) {

            Pattern[] patterns = new Pattern[]{
                Pattern.compile("(\\d\\d/\\d\\d) +(.+?) +([\\d\\.,-]+) +([\\d\\.,-]+)"),
                Pattern.compile("(.+?)\\t(.+?)\\s+PARC (\\d\\d/\\d\\d).+?\\t\\t(.+?)\\t(.+)"),
                Pattern.compile("(.+?)\\t([^\\t]{22,}?)\\s+.+?\\t\\t(.+?)\\t(.+)"),
                Pattern.compile("\\s+\\t")
            };

            String[] replaces = new String[]{
                "$1\t$2\t\t$3\t$4",
                "$1\t$2\t$4\t$3\t$5",
                "$1\t$2\t$3\t\t$4",
                "\t"
            };

            try (BufferedReader reader = new BufferedReader(new FileReader(fileInput));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutput))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    found = false;

                    for (int i = 0; i < patterns.length; i++) {
                        Matcher m = patterns[i].matcher(line);
                        if (m.matches()) {
                            found = true;
                            line = m.replaceAll(replaces[i]);
                        }
                    }

                    if (found) {
                        writer.append(line);
                        writer.newLine();
                    }

                }
            }

        }
    }

}
