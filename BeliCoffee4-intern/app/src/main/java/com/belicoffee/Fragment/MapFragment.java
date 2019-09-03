package com.belicoffee.Fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.belicoffee.Dialog.MapDialog;
import com.belicoffee.Manager.MapManager;
import com.belicoffee.Manager.UserManager;
import com.belicoffee.Model.HistoryLocation;
import com.belicoffee.Model.UserLocation;
import com.belicoffee.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, MapManager.LocationListener, GoogleMap.OnMarkerClickListener, View.OnClickListener, MapDialog.HistoryListener {
    boolean cameraState = true;
    GoogleMap map;
    Button btnHistory;
    MapManager mapManager;
    UserManager userManager;
    String myId;
    String myName;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        btnHistory = view.findViewById(R.id.btn_history_map);
        btnHistory.setVisibility(View.GONE);
        mapManager = MapManager.getInstance();
        userManager = UserManager.getInstance();

        mapManager.setListener(getActivity(), this);
        mapManager.updateLocation();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myId = firebaseUser.getUid();
        myName = userManager.getMyName(myId);

        btnHistory.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mv_map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onStop() {
        map = null;
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            map = googleMap;
            map.setOnMarkerClickListener(this);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    @Override
    public void updateMyLocation(LatLng latLng) {
        if (latLng != null && map != null) {
            Log.i("tag", "updateMyLocation" + latLng.toString());
            map.clear();
            map.addMarker(new MarkerOptions().position(latLng));
            if (cameraState) {
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                cameraState = false;
            }
        }
    }

    @Override
    public void updateOtherLocation(ArrayList<UserLocation> userLocations) {
        if (map != null) {
            for (UserLocation userLocation : userLocations) {
                LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                Log.i("tag", "updateOtherLocation" + latLng.toString());
                Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(userLocation.getUsername())
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getActivity(), R.drawable.ic_location_on_dark_cyan_48dp))));
                marker.setTag(userLocation.getId());
            }
        }
    }

    @Override
    public void showHistoryLocation(ArrayList<HistoryLocation> historyLocations) {
        map.clear();
        for (HistoryLocation historyLocation : historyLocations) {
            LatLng latLng = new LatLng(historyLocation.getLatitude(), historyLocation.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getActivity(), R.drawable.ic_my_location_dark_cyan_24dp))));
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() == null) {
            showMapDialog(myName, myId, null);
        } else {
            String markerId = marker.getTag().toString();
            showMapDialog(myName, myId, markerId);

        }
        return true;
    }

    private void showMapDialog(String myName, String myId, String hisId) {
        FragmentManager fragmentManager = getFragmentManager();
        MapDialog mapDialog = new MapDialog(myName, myId, hisId, this);
        mapDialog.show(fragmentManager, "user_dialog");
    }

    @Override
    public void onClick(View v) {
        mapManager.setHistory(false);
        btnHistory.setVisibility(View.GONE);
        map.clear();
    }

    @Override
    public void showButton() {
        map.clear();
        btnHistory.setVisibility(View.VISIBLE);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}

