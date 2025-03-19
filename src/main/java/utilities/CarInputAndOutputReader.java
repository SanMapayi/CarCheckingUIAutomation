package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CarInputAndOutputReader {
     public static final CarInputAndOutputReader getInstance = new CarInputAndOutputReader();

     private CarInputAndOutputReader() {

     }


    public List<String> getListOfExtractedPlateNumber() {

        List<String> listOfPlateNumbers = new ArrayList<>();

        // Read the content from the car_input.txt file
        Path inputFilePath = Paths.get("src/test/resources/testData/car_input - V6.txt");
        Path userDir = Path.of(System.getProperty("user.dir"));
        Path filePath = userDir.resolve(inputFilePath);
        String carInputContent;

        try {
            carInputContent = Files.readString(filePath);

        // The regex pattern for UK registration plates with or without space in between.
        String regexPlateNum = "[A-Z]{2}\\d{1,2}\\s?[A-Z]{3}";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regexPlateNum);

        // A matcher object to find the patterns in the content
        Matcher matcher = pattern.matcher(carInputContent);

        // Loop through and adding all into a list (vehicle registration plates)
        while (matcher.find()) {
            listOfPlateNumbers.add(matcher.group());

        }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return listOfPlateNumbers;

    }



    public Map<String, List<String>> vehicleRegMakeModelYearFromOutputFile() {

        Map<String, List<String>> mapOfVehicleRegMakeModelAndYear = new HashMap<>();
        String filePath = "src/test/resources/testData/car_output - V6.txt";  // Replace with your file path
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skipping the first line
            br.readLine();

            // Reading the rest of the lines
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitData = line.split(",");
                //using the reg number as a key for the map, and the other values in a list.
                mapOfVehicleRegMakeModelAndYear.put(splitData[0], List.of(splitData[1], splitData[2], splitData[3]));
            }
            return mapOfVehicleRegMakeModelAndYear;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
