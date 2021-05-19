package com.example.finalproject.covid19cases;


/**
 * CovidCase is a class that represents CovidCases.
 *
 * @author Wilker Fernandes de Sousa
 * @version 1.0
 */
class CovidCase {

    /**
     * Field that stores the id of the cases in to the database.
     */
    private long id;

    /**
     * Fields that stores the names, country code, province name,
     * latitude and longitude of country values.
     */
    private String country, countryCode, province, lat, lon;

    /**
     * Field that stores the cases that are number of cases per
     * countries or state/providence
     */
    private int cases;

    /**
     * Field that stores the status of the cases per countries
     * or state/providence
     */
    private String status;

    /**
     * Field that stores the cases Date
     */
    private String date;

    /**
     * Constructor for Covid
     *
     * @param id          id represents the number of choice of country entries present in the database..
     * @param country     represents the country name of covid cases.
     * @param countryCode represents the country code name of covid cases.
     * @param province    represents the providence/state depending on the country name of covid cases.
     * @param lat         represents the country latitude of covid cases.
     * @param lon         represents the country longitude of covid cases.
     * @param cases       represents the number of cases of covid cases.
     * @param status      represents the status cases with "Confirmed" of covid cases.
     * @param date        represents the date of the cases.
     */
    public CovidCase(long id, String country, String countryCode, String province, String lat, String lon, int cases, String status, String date) {
        this.id = id;
        this.country = country;
        this.countryCode = countryCode;
        this.province = province;
        this.lat = lat;
        this.lon = lon;
        this.cases = cases;
        this.status = status;
        this.date = date;
    }

    /**
     * This constructor is been used in the CovidCasesListActivity to loop through as a JSON object
     *
     * @param country     represents the country name of covid cases.
     * @param countryCode represents the country code name of covid cases.
     * @param province    represents the providence/state depending on the country name of covid cases.
     * @param lat         represents the country latitude of covid cases.
     * @param lon         represents the country longitude of covid cases.
     * @param cases       represents the number of cases of covid cases.
     * @param status      represents the status cases with "Confirmed" of covid cases.
     * @param date        represents the date of the cases.
     */
    public CovidCase(String country, String countryCode, String province, String lat, String lon, int cases, String status, String date) {
        this.country = country;
        this.countryCode = countryCode;
        this.province = province;
        this.lat = lat;
        this.lon = lon;
        this.cases = cases;
        this.status = status;
        this.date = date;
    }

    /**
     * Gets the id
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the field that stores the id
     *
     * @param id takes a long parameter of itself
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the country name
     *
     * @return the country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the field that stores the country name
     *
     * @param country takes a String parameter of itself
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the country code
     *
     * @return the country code
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the field that stores the country code
     *
     * @param countryCode takes a String parameter of itself
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Gets the province
     *
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets the field that stores province
     *
     * @param province takes a String parameter of itself
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Gets the latitude
     *
     * @return the latitude
     */
    public String getLat() {
        return lat;
    }

    /**
     * Sets the field that stores latitude
     *
     * @param lat takes a String parameter of itself
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * Gets the longitude
     *
     * @return the longitude
     */
    public String getLon() {
        return lon;
    }

    /**
     * Sets the field that stores longitude
     *
     * @param lon takes a String parameter of itself
     */
    public void setLon(String lon) {
        this.lon = lon;
    }

    /**
     * Gets the number of cases
     *
     * @return the cases
     */
    public int getCases() {
        return cases;
    }

    /**
     * Sets the field that stores the number of cases as Integers
     *
     * @param cases takes number of cases as Integers of itself
     */
    public void setCases(int cases) {
        this.cases = cases;
    }

    /**
     * Gets the Status of the cases "Confirmed"
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the field that stores status
     *
     * @param status takes a String parameter of itself
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the date
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the field that stores date
     *
     * @param date takes a String parameter of itself
     */
    public void setDate(String date) {
        this.date = date;
    }
}