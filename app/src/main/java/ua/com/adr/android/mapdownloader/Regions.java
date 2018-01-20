package ua.com.adr.android.mapdownloader;

import java.util.ArrayList;

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

    public Regions() {
        regionsList.add(countriesAfrica);
        regionsList.add(countriesAsia);
        regionsList.add(countriesAustralia);
        regionsList.add(countriesCentrAmerica);
        regionsList.add(countriesEurope);
        regionsList.add(countriesNorthAmerica);
        regionsList.add(countriesRussia);
    }
}
