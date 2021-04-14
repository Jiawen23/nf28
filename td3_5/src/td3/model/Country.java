package td3.model;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Country {
    public Country() {
    }

    public static List<String> getCountryNames() {
        String[] locales = Locale.getISOCountries();
        List<String> countryNames = (List) Arrays.stream(locales).map((countryCode) -> {
            return (new Locale("", countryCode)).getDisplayCountry();
        }).sorted().collect(Collectors.toList());
        return countryNames;
    }
}
