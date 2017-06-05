package com.assignment.rapido.domain;

import com.google.gson.annotations.SerializedName;

public class Step
{
    @SerializedName("start_location")
    Location StartLocation;
    @SerializedName("end_location")
    Location EndLocation;

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    String html_instructions;

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
