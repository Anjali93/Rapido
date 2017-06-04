package com.assignment.rapido.domain;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionResponse
{
    @SerializedName("routes")
    List<Route>  routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
