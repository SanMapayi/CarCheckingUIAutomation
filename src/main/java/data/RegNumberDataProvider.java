package data;

import org.testng.annotations.DataProvider;
import utilities.CarInputAndOutputReader;

import java.util.List;

public class RegNumberDataProvider {

    private final List<String> listOfExtractedRegNumbers = CarInputAndOutputReader.getInstance.getListOfExtractedPlateNumber();

    /**
     * DataProvider supplies each registration number as a separate test case.
     */
    @DataProvider(name = "carRegNumbersProvider")
    public Object[][] provideCarRegNumbers() {
        return listOfExtractedRegNumbers.stream()
                .map(regNumber -> new Object[]{regNumber})  // Convert list to Object array
                .toArray(Object[][]::new);
    }
}
