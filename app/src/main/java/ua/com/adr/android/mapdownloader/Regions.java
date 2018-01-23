package ua.com.adr.android.mapdownloader;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Andy on 20.01.2018.
 */

public class Regions {

    String[] allRegions = {"Africa", "Asia", "Australia and Oceania", "Central America", "Europe", "North America", "Russia"};
    String[] countriesAfrica = {};
    String[] countriesAsia = {};
    String[] countriesAustralia = {};
    String[] countriesCentrAmerica = {};
    String[] countriesEurope = {"Albania", "Austria", "Belarus", "Cyprus", "Denmark", "Estonia", "Finland", "France", "Germany", "Hungary", "Iceland", "Italy"};
    String[] countriesNorthAmerica = {};
    String[] countriesRussia = {};
    ArrayList<String[]> regionsList = new ArrayList<>();
    ArrayList<String[]> europeCountryList;

    String[] franceAreas = {};
    String[] germanyAreas = {"Bavaria", "Berlin", "Hamburg", "Hesse", "Saarland", "Saxony", "Thuringia"};
    String[] germanyLinks = {"http", "http://download.osmand.net/download.php?standard=yes&file=Germany_berlin_europe_2.obf.zip", "http", "http", "http", "http", "http"};
    String[] italyAreas = {};

    public Regions() {
        regionsList.add(countriesAfrica);
        regionsList.add(countriesAsia);
        regionsList.add(countriesAustralia);
        regionsList.add(countriesCentrAmerica);
        regionsList.add(countriesEurope);
        regionsList.add(countriesNorthAmerica);
        regionsList.add(countriesRussia);

        europeCountryList = new ArrayList<String[]>(Collections.<String[]>nCopies(countriesEurope.length, null));
        europeCountryList.set(7, franceAreas);
        europeCountryList.set(8, germanyAreas);
        europeCountryList.set(11, italyAreas);
    }
}
