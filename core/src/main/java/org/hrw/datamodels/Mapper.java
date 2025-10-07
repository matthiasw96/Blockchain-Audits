package org.hrw.datamodels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Mapper {
    public static Map<String, String> createColumnMap() {
        try (InputStream in = Mapper.class.getResourceAsStream("/map.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            Map<String, String> map = readLines(reader);

            System.out.println("Loaded " + map.size() + " entries");

            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Map<String, String> readLines(BufferedReader reader) throws IOException {
        Map<String, String> map = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            map.put(parts[0].trim(), parts[1].trim());
        }
        return map;
    }
}
