package com.assignment.rapido.domain;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Route
{
    @SerializedName("legs")
    List<Leg> legs;
    OverviewPolyline overview_polyline;

    public OverviewPolyline getOverview_polyline()
    {
        return overview_polyline;
    }

    public void setOverview_polyline(OverviewPolyline overview_polyline)
    {
        this.overview_polyline = overview_polyline;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public ArrayList<LatLng> DecodePoly()
    {
        Log.i("Location", "String received: "+overview_polyline.points);
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = overview_polyline.points.length();
        int lat = 0, lng = 0;

        while (index < len)
        {
            int b, shift = 0, result = 0;
            do
            {
                b = overview_polyline.points.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do
            {
                b = overview_polyline.points.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            //LatLng p = new LatLng((int) (((double) lat /1E5)* 1E6), (int) (((double) lng/1E5   * 1E6)));

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
