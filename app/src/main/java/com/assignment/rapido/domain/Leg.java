package com.assignment.rapido.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Leg
{
    @SerializedName("steps")
    List<Step>  steps;

    Location start_location;
    Location end_location;

    public Location getStart_location() {
        return start_location;
    }

    public void setStart_location(Location start_location) {
        this.start_location = start_location;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public void setEnd_location(Location end_location) {
        this.end_location = end_location;
    }

    public List<Step>  getSteps()
    {
        return steps;
    }

    public void setSteps(List<Step>  steps)
    {
        this.steps = steps;
    }
}
