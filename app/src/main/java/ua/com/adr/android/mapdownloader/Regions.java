package ua.com.adr.android.mapdownloader;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Andy on 20.01.2018.
 */

public class Regions {

    int[] allRegions = {R.string.Africa, R.string.Asia, R.string.Australia, R.string.Central_America,
            R.string.Europe, R.string.North_America, R.string.Russia};
    int[] countriesAfrica = {};
    int[] countriesAsia = {};
    int[] countriesAustralia = {};
    int[] countriesCentrAmerica = {};
    int[] countriesEurope = {R.string.Albania, R.string.Austria, R.string.Belarus, R.string.Cyprus,
            R.string.Denmark, R.string.Estonia, R.string.Finland, R.string.France, R.string.Germany,
            R.string.Hungary, R.string.Iceland, R.string.Italy};
    int[] countriesNorthAmerica = {};
    int[] countriesRussia = {};
    ArrayList<int[]> regionsList = new ArrayList<>();

    int[] franceAreas = {};
    int[] germanyAreas = {R.string.Bavaria, R.string.Berlin, R.string.Hamburg, R.string.Hesse, R.string.Saarland, R.string.Saxony, R.string.Thuringia};
    String[] germanyLinks = {"http", "http://download.osmand.net/download.php?standard=yes&file=Germany_berlin_europe_2.obf.zip", "http", "http", "http", "http", "http"};
    int[] italyAreas = {};
    ArrayList<int[]> europeCountryList;

    public Regions() {
        regionsList.add(countriesAfrica);
        regionsList.add(countriesAsia);
        regionsList.add(countriesAustralia);
        regionsList.add(countriesCentrAmerica);
        regionsList.add(countriesEurope);
        regionsList.add(countriesNorthAmerica);
        regionsList.add(countriesRussia);

        europeCountryList = new ArrayList<>(Collections.<int[]>nCopies(countriesEurope.length, null));
        europeCountryList.set(7, franceAreas);
        europeCountryList.set(8, germanyAreas);
        europeCountryList.set(11, italyAreas);
    }
}
