package com.belicoffee.Model;

public class UserLocation {
    String id;
    String username;
    Double latitude;
    Double longitude;

    public UserLocation() {
    }

    public UserLocation(String id, String username, Double latitude, Double longitude) {
        this.id = id;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
