package Domain;

import java.util.Arrays;
import java.util.List;

public class BiletConverter {

    // Method to serialize a list of strings into a single string
    public static String serializeTuristi(List<String> turisti) {
        return String.join(", ", turisti); // Assuming comma-separated values
    }

    // Method to deserialize a string into a list of strings
    public static List<String> deserializeTuristi(String serializedTuristi) {
        return Arrays.asList(serializedTuristi.split(", ")); // Assuming comma-separated values
    }
}