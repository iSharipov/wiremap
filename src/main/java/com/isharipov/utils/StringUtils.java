package com.isharipov.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Илья on 23.04.2016.
 */
public class StringUtils {
    public static String getMac(String unformattedMac, String divisionSymbol) {

        return unformattedMac.replaceAll("(.{2})", "$1" + divisionSymbol).substring(0, 17);
    }

    public static List<String> replaceSpecialsSymbolsAndUpperCase(List<String> inputMac) {
        List<String> temp = new ArrayList<>(inputMac.size());
        for (String anInputMac : inputMac) {
            temp.add(anInputMac.replaceAll("[^a-zA-Z0-9]+", "").toUpperCase());
        }
        return temp;
    }

    public static String fileToString(String path, Charset encoding) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, encoding);
    }

    public static List<String> macList(String fileName) {

        String csvFile = "src/main/resources/" + fileName + ".csv";
        CSVParser parser = null;
        List<String> list = new ArrayList<>();
        try {
            parser = CSVParser.parse(StringUtils.fileToString(csvFile, Charset.defaultCharset()), CSVFormat.DEFAULT.withQuote(null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CSVRecord csvRecord : parser) {
            if (csvRecord.toString().contains("BSSID")) {
                int index = csvRecord.toString().indexOf("BSSID");
                list.add(csvRecord.toString().substring(index + 8, index + 25));
            }
        }
        return list;
    }
}
