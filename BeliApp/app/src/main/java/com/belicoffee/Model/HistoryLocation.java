package com.belicoffee.Model;

public class HistoryLocation {
    String date;
    Double latitude;
    Double longitude;

    public HistoryLocation() {
    }

    public HistoryLocation(String date, Double latitude, Double longitude) {
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
