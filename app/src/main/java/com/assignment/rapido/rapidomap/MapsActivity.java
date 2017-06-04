package com.assignment.rapido.rapidomap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.assignment.rapido.domain.DirectionResponse;
import com.assignment.rapido.util.RapidoMapsClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, android.location.LocationListener {

    private static GoogleMap mMap;
    private Place Source;
    private Place Destination;
    //private PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
    //        getFragmentManager().findFragmentById(R.id.auto);

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {

            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED)
//        {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        //mMap.setMyLocationEnabled(true);


//        mMap.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20));

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        CenterMapToCurrentLocation();

        //       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney , 13));
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(-34 , 151)).add(new LatLng(-34 , 152)));

        Button Source = (Button) findViewById(R.id.source);
        Button Destination = (Button) findViewById(R.id.destination);

        Source.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


//                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
//                {
//                    @Override
//                    public void onPlaceSelected(Place place)
//                    {
//                        // TODO: Get info about the selected place.
//                        Log.i("TAG", "Place: " + place.getName());
//
//                        String placeDetailsStr = place.getName() + "\n"
//                                + place.getId() + "\n"
//                                + place.getLatLng().toString() + "\n"
//                                + place.getAddress() + "\n"
//                                + place.getAttributions();
//                        autocompleteFragment.setText(placeDetailsStr);
//                    }
//
//                    @Override
//                    public void onError(Status status) {
//                        // TODO: Handle the error.
//                        Log.i("TAG", "An error occurred: " + status);
//                    }
//                });

                startActivityForResult(intent, 1);


            }
        });

        Destination.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                        getFragmentManager().findFragmentById(R.id.place_autocomplete_powered_by_google);

                //              autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
//                {
//                    @Override
//                    public void onPlaceSelected(Place place)
//                    {
//                        // TODO: Get info about the selected place.
//                        Log.i("TAG", "Place: " + place.getName());
//
//                        String placeDetailsStr = place.getName() + "\n"
//                                + place.getId() + "\n"
//                                + place.getLatLng().toString() + "\n"
//                                + place.getAddress() + "\n"
//                                + place.getAttributions();
//                        autocompleteFragment.setText(placeDetailsStr);
//                    }
//
//                    @Override
//                    public void onError(Status status) {
//                        // TODO: Handle the error.
//                        Log.i("TAG", "An error occurred: " + status);
//                    }
//                });

                startActivityForResult(intent, 2);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Source = PlaceAutocomplete.getPlace(this, data);
                Log.i("TAG", "Place: " + Source.getName());
                LatLng LatLong = Source.getLatLng();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLong, 13));
                mMap.addMarker(new MarkerOptions().position(LatLong).title("MyLocation").draggable(true));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Destination = PlaceAutocomplete.getPlace(this, data);
                Log.i("TAG", "Place: " + Destination.getName());
                LatLng LatLong = Destination.getLatLng();
                mMap.addMarker(new MarkerOptions().position(LatLong).title("Destination").draggable(true));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (Source != null && Destination != null) {
            RapidoMapsClient Client = new RapidoMapsClient();
            Client.GetJsonResponse((String) Source.getAddress(), (String) Destination.getAddress(), getString(R.string.Api_Key1));
        }
    }

    private void CenterMapToCurrentLocation() {
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria mCriteria = new Criteria();
        String bestProvider = manager.getBestProvider(mCriteria, true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location mLocation = manager.getLastKnownLocation(bestProvider);
            if (mLocation != null)
            {
                Log.e("TAG", "GPS is on");
                final double currentLatitude = mLocation.getLatitude();
                final double currentLongitude = mLocation.getLongitude();
                LatLng loc1 = new LatLng(currentLatitude, currentLongitude);
                mMap.addMarker(new MarkerOptions().position(loc1).title("Your Current Location").draggable(true));
                mMap.setMyLocationEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 13));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);
            }
            else if (bestProvider == null)
            {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public static void DrawPolyLines(DirectionResponse directionResponse)
    {
        int Colour = 1;
        for (int j = directionResponse.getRoutes().size() - 1; j >= 0; j--) {
            PolylineOptions PolyLine = new PolylineOptions().geodesic(true);

            PolyLine.clickable(true);
            ArrayList<LatLng> PointsList = directionResponse.getRoutes().get(j).DecodePoly();
            if (j == 0) {
                PolyLine.color(Color.BLUE);
                PolyLine.width(15);
            } else {
                PolyLine.color(Color.GRAY);
                PolyLine.width(10);
            }
            for (int i = 0; i < PointsList.size(); i++) {
                PolyLine.add(PointsList.get(i));

            }

            mMap.addPolyline(PolyLine);
            Colour++;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLocation = manager.getLastKnownLocation(provider);
        if (mLocation != null)
        {
            Log.e("TAG", "GPS is on");
            final double currentLatitude = mLocation.getLatitude();
            final double currentLongitude = mLocation.getLongitude();
            LatLng loc1 = new LatLng(currentLatitude, currentLongitude);
            mMap.addMarker(new MarkerOptions().position(loc1).title("Your Current Location"));
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 13));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);
        }
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }
}
