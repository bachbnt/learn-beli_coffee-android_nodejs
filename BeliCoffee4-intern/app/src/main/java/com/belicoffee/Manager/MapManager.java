package com.belicoffee.Manager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.belicoffee.Model.HistoryLocation;
import com.belicoffee.Model.UserLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapManager {
    static MapManager mapManager;
    private long UPDATE_INTERVAL = 10000;
    private long FASTEST_INTERVAL = 3000;
    static String myId;
    LatLng latLng;

    ArrayList<UserLocation> userLocations;
    ArrayList<HistoryLocation> historyLocations;
    UserManager userManager;

    Activity activity;
    LocationListener locationListener;
    ArrayList<HistoryLocation> myHistoryLocations;
    boolean fisrtUpdate = true;
    boolean historyState = false;
    int count;

    private MapManager() {
        userManager = UserManager.getInstance();
        userLocations = new ArrayList<>();
        myHistoryLocations = new ArrayList<>();
        historyLocations = new ArrayList<>();
    }

    public static MapManager getInstance() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = firebaseUser.getUid();
        if (mapManager == null)
            mapManager = new MapManager();
        return mapManager;
    }

    public void setListener(Activity activity, LocationListener locationListener) {
        this.activity = activity;
        this.locationListener = locationListener;
    }

    public void setHistory(boolean historyState) {
        this.historyState = historyState;
        Log.i("tag", "historystate " + historyState);
    }

    public void updateLocation() {
        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(activity);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                        fisrtUpdate = false;
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        sendLocation();
        saveHistory();
        if (!historyState) {
            locationListener.updateMyLocation(latLng);
            loadLocationList();
            Log.i("tag", "updateOtherLocation onLocationChanged");
        }
    }

    public void sendLocation() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Locations").child(myId);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", myId);
        hashMap.put("username", userManager.getMyName(myId));
        hashMap.put("latitude", latLng.latitude);
        hashMap.put("longitude", latLng.longitude);
        reference.setValue(hashMap);
    }

    public void loadLocationList() {
        Log.i("tag", "updateOtherLocation loadLocationList");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Locations");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userLocations.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserLocation userLocation = snapshot.getValue(UserLocation.class);
                    if (!userLocation.getId().equals(myId)) {
                        userLocations.add(userLocation);
                        if (!historyState)
                            locationListener.updateOtherLocation(userLocations);
                    }
                }
                Log.i("tag", "loadLocationList" + userLocations.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.i("tag", "loadLocationList" + userLocations.size());
    }


    public void saveHistory() {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        if (fisrtUpdate) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("History").child(myId);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("latitude", latLng.latitude);
            hashMap.put("longitude", latLng.longitude);
            hashMap.put("date", date);
            reference.push().setValue(hashMap);
            Log.i("tag", "check location first");
        } else {
            count = 0;
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("History/" + myId);
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    myHistoryLocations.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HistoryLocation historyLocation = snapshot.getValue(HistoryLocation.class);
                        myHistoryLocations.add(historyLocation);
                        Log.i("tag", "check location add");
                    }
                    Log.i("tag", "check location datachange" + myHistoryLocations.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Log.i("tag", "check location datachange after" + myHistoryLocations.size());
            for (HistoryLocation historyLocation : myHistoryLocations) {
                Log.i("tag", "check location history " + myHistoryLocations.size());
                LatLng originLatLng = new LatLng(historyLocation.getLatitude(), historyLocation.getLongitude());
                if (caculateDistance(originLatLng, latLng) > 40) {
                    count++;
                    Log.i("tag", "check location count " + count);
                }
            }
            if (count == myHistoryLocations.size() && count != 0) {
                Log.i("tag", "check location push count " + count);
                Log.i("tag", "check location push history " + myHistoryLocations.size());
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("History").child(myId);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("latitude", latLng.latitude);
                hashMap.put("longitude", latLng.longitude);
                hashMap.put("date", date);
                reference2.push().setValue(hashMap);
                Log.i("tag", "check location push");
            }
        }
    }

    public void loadAllHistory(String id, final int numOfDays) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("History/" + id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyLocations.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HistoryLocation historyLocation = snapshot.getValue(HistoryLocation.class);
                    if (caculateDay(historyLocation.getDate()) <= numOfDays) {
                        Log.i("tag", "caculateDay numdays" + numOfDays);
                        historyLocations.add(historyLocation);
                        locationListener.showHistoryLocation(historyLocations);
                    }
                }
                Log.i("tag", "loadAllHistory update" + historyLocations.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public double caculateDistance(LatLng latLng1, LatLng latLng2) {
        double distance = 0;
        Location location1 = new Location("1");
        location1.setLatitude(latLng1.latitude);
        location1.setLongitude(latLng1.longitude);
        Location location2 = new Location("2");
        location2.setLatitude(latLng2.latitude);
        location2.setLongitude(latLng2.longitude);
        distance = location1.distanceTo(location2) / 1000;//meters to kilometers
        Log.i("tag", "distance " + distance);
        return distance;
    }

    public long caculateDay(String date) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        try {
            Date beforeDate = format.parse(date);
            long time = currentDate.getTime() - beforeDate.getTime();
            day = time / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("tag", "caculateDay " + day);
        return day;
    }

    public interface LocationListener {
        void updateMyLocation(LatLng latLng);

        void updateOtherLocation(ArrayList<UserLocation> userLocations);

        void showHistoryLocation(ArrayList<HistoryLocation> historyLocations);
    }
}
