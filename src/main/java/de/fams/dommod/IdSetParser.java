package de.fams.dommod;

import java.awt.image.ImagingOpException;
import java.io.*;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdSetParser {

    public static Pattern TERM_EXP = Pattern.compile("(\\w+)\\((-?\\d+)\\)");

    public UsageSet parse(String fName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fName));
            return parse(reader);
        } catch (Exception e) {
            throw new RuntimeException("Error reading usage file.", e);
        }
    }

    public UsageSet parse(BufferedReader reader) throws IOException {
        UsageSet result = new UsageSet();
        String line = reader.readLine();
        while (line != null) {
            processLine(line, result);
            line = reader.readLine();
        }
        return result;
    }

    private void processLine(String line, UsageSet result) {
        if (line.contains("--")) {
            line = line.substring(0, line.indexOf("--"));
        }
        line = line.trim();
        if (line.isEmpty()) {
            return;
        }
        Matcher matcher = TERM_EXP.matcher(line);
        if (!matcher.matches()) {
            throw new RuntimeException("Error processing: " + line);
        }
        String typeString = matcher.group(1).toUpperCase();
        int id = Integer.parseInt(matcher.group(2));
        if (typeString.equals("MONTAG")) {
            result.addMontag(id);
        } else {
            EntityType type = EntityType.valueOf(typeString);
            if (type == null) {
                throw new RuntimeException("Unknown type " + typeString);
            }
            NumericReference ref = new NumericReference(type, id);
            result.add(ref);
        }
    }

}
