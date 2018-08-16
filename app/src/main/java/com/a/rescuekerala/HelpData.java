package com.a.rescuekerala;

public class HelpData {

    double lat, log;
    String name, phone, message;

    public HelpData(double lat, double log, String name, String phone, String message) {
        this.lat = lat;
        this.log = log;
        this.name = name;
        this.phone = phone;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }
}
