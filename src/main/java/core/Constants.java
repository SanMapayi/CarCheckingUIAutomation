package core;

import java.nio.file.Path;

public class Constants {

    public static final Path CARINPUTPATH =Path.of(System.getProperty("user.dir")).resolve( Path.of("src/test/resources/testdata/car_input - V6.txt"));
    public static final Path CAROUTPUTPATH = Path.of(System.getProperty("user.dir")).resolve(Path.of("src/test/resources/testdata/car_output - V6.txt"));

    public static final Path SCREENSHOTSPATH = Path.of(System.getProperty("user.dir")).resolve(Path.of("screenshots"));
    public static final Path TESTREPORTPATH =  Path.of(System.getProperty("user.dir")).resolve(Path.of("test-output"));
    public static final String URL = "https://car-checking.com";
    public static final int NUMBEROFTESTREPORTSTOKEEP = 5;
}
