package com.assignment.rapido.rapidomap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.assignment.rapido.domain.DirectionResponse;
import com.assignment.rapido.util.RapidoMapsClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends android.support.v4.app.FragmentActivity implements OnMapReadyCallback, android.location.LocationListener {

    private static GoogleMap mMap;
    private static Place Source;
    private static Place Destination;
    LinearLayout   DirectionsInput;
    TextView SourceText ;
    TextView DestinationText;
    Intent intent;
    static List<PolylineOptions> PolylinesList;
    PolylineOptions       SelectedRoute;
    static int PolyIndex=-1;
    int     SelectedPolyIndex;
    ListView    ListOfDirections;
    LinearLayout  layout;
    LinearLayout  ShowRoute;
    static DirectionResponse  DirectionsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(mapFragment.getView() != null)
        mapFragment.getView().setClickable(true);

        try
        {
            intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
        {
            // TODO: Handle the error.
        }

        DirectionsInput = (LinearLayout)findViewById(R.id.dir);
        layout = (LinearLayout)findViewById(R.id.Rout);
        layout.setVisibility(View.INVISIBLE);

        ShowRoute = (LinearLayout)findViewById(R.id.findroute);
        TextView findRoute = (TextView) findViewById(R.id.findrout);
        findRoute.setClickable(true);

        findRoute.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(Source == null || Destination == null)
                    return;

                LinearLayout  layout = (LinearLayout)findViewById(R.id.Rout);

                ListOfDirections = (ListView)findViewById(R.id.listview1);
                ArrayList<String> list = new ArrayList();

                for(int j =0 ; j<DirectionsResponse.getRoutes().get(PolyIndex).getLegs().size() ; j++)
                    for(int i =0; i < DirectionsResponse.getRoutes().get(PolyIndex).getLegs().get(j).getSteps().size() ; i++)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            list.add(Html.fromHtml(DirectionsResponse.getRoutes().get(PolyIndex).getLegs().get(j).getSteps().get(i).getHtml_instructions(), 1).toString());
                        }
                        else
                            list.add(Html.fromHtml(DirectionsResponse.getRoutes().get(PolyIndex).getLegs().get(j).getSteps().get(i).getHtml_instructions()).toString());
                    }


               ArrayAdapter adapter = new ArrayAdapter(MapsActivity.this , android.R.layout.simple_list_item_activated_1 , list);
                ListOfDirections.setAdapter(adapter);


                ListOfDirections.setScrollbarFadingEnabled(true);
                // Prepare the View for the animation
                layout.setVisibility(View.VISIBLE);
                DirectionsInput.setVisibility(View.INVISIBLE);
                ShowRoute.setVisibility(View.INVISIBLE);

                Animation bottomup = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.anim);
                layout.setAnimation(bottomup);
            }

        });

        PolylinesList = new LinkedList<>();
        SelectedPolyIndex = -1;
        SelectedRoute = new PolylineOptions();
        SelectedRoute.geodesic(true).clickable(true);
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);

        SourceText = (TextView) findViewById(R.id.TextSource);
        DestinationText = (TextView) findViewById(R.id.TextDestination);
        CenterMapToCurrentLocation();

        SourceText.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(intent, 1);

            }
        });

        DestinationText.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(intent, 2);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickCoords)
            {
                SelectedPolyIndex = -1;
                for (PolylineOptions polyline : PolylinesList)
                {
                    SelectedPolyIndex++;
                    boolean found = false;
                    for (LatLng polyCoords : polyline.getPoints())
                    {
                        float[] results = new float[1];
                        Location.distanceBetween(clickCoords.latitude, clickCoords.longitude,
                                polyCoords.latitude, polyCoords.longitude, results);

                        if (results[0] < 100)
                        {
                            // If distance is less than 100 meters, this is your polyline
                            Log.e("TAG", "Found @ "+clickCoords.latitude+" "+clickCoords.longitude);
                            PolyIndex = SelectedPolyIndex;
                            found = true;
                            break;

                        }
                    }
                    if(found)
                    {
                        Draw();
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                Source = PlaceAutocomplete.getPlace(this, data);
                Log.i("TAG", "Place: " + Source.getName());
                SourceText.setText(Source.getAddress());

                Draw();


            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("TAG", status.getStatusMessage());

            }
            else if (resultCode == RESULT_CANCELED)
            {
                // The user canceled the operation.
            }
        }
        else if (requestCode == 2)
        {
            if (resultCode == RESULT_OK)
            {
                Destination = PlaceAutocomplete.getPlace(this, data);
                Log.i("TAG", "Place: " + Destination.getName());
                DestinationText.setText(Destination.getAddress());

                Draw();

            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
            {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("TAG", status.getStatusMessage());

            }
            else if (resultCode == RESULT_CANCELED)
            {
                // The user canceled the operation.
            }
        }

        if (Source != null && Destination != null)
        {
            RapidoMapsClient Client = new RapidoMapsClient();
            Client.GetJsonResponse((String) Source.getAddress(), (String) Destination.getAddress(),
                    getString(R.string.Api_Key1));
        }
    }

    private void CenterMapToCurrentLocation()
    {
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria mCriteria = new Criteria();
        String bestProvider = manager.getBestProvider(mCriteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
        {
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
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public static void setPolyLines(DirectionResponse directionResponse)
    {
        DirectionsResponse = directionResponse;
        PolylinesList.clear();
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (int j = directionResponse.getRoutes().size() - 1; j >= 0; j--)
        {
            PolylineOptions PolyLine = new PolylineOptions().geodesic(true);

            ArrayList<LatLng> PointsList = directionResponse.getRoutes().get(j).DecodePoly();
            for (int i = 0; i < PointsList.size(); i++) {
                PolyLine.add(PointsList.get(i));
                b.include(new LatLng(PointsList.get(i).latitude , PointsList.get(i).longitude));

            }
            PolylinesList.add(PolyLine);
        }
        PolyIndex = directionResponse.getRoutes().size() > 0 ? 0 : -1;

        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds , 1000, 1000 , 5);
        mMap.animateCamera(cu);
        Draw();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        CenterMapToCurrentLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        CenterMapToCurrentLocation();
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        CenterMapToCurrentLocation();
    }

    private static void Draw()
    {
        mMap.clear();
        if(Source!=null){

            if(PolylinesList.size() == 0)
            {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Source.getLatLng(), 13));
            }
            mMap.addMarker(new MarkerOptions().position(Source.getLatLng()).title("MyLocation").draggable(true));
        }
        if(Destination!=null){
            mMap.addMarker(new MarkerOptions().position(Destination.getLatLng()).title("MyLocation").draggable(true));
        }

        for(int i = 0; i < PolylinesList.size(); i++){
            PolylinesList.get(i).color(i==PolyIndex ? Color.BLUE : Color.GRAY);
            PolylinesList.get(i).width(i==PolyIndex ? 15 : 10);
            if(i==PolyIndex)continue;
            mMap.addPolyline(PolylinesList.get(i));
        }
        if(PolyIndex > -1){
            mMap.addPolyline(PolylinesList.get(PolyIndex));
        }
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onBackPressed()
    {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                // do something here
                DirectionsInput.setVisibility(View.VISIBLE);
                ShowRoute.setVisibility(View.VISIBLE);
                Animation bottodown = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.bottom_bottom);
                layout.setAnimation(bottodown);
                layout.setVisibility(View.INVISIBLE);

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
