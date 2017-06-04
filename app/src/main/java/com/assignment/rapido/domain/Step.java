package com.assignment.rapido.domain;

import com.google.gson.annotations.SerializedName;

public class Step
{
    @SerializedName("start_location")
    Location StartLocation;
    @SerializedName("end_location")
    Location EndLocation;

    public Location getEndLocation() {
        return EndLocation;
    }

    public void setEndLocation(Location endLocation) {
        EndLocation = endLocation;
    }

    public Location getStartLocation() {
        return StartLocation;
    }

    public void setStartLocation(Location startLocation) {
        StartLocation = startLocation;
    }
}
