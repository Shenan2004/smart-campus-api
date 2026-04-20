package com.mycompany.mavenproject1.resources;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Map<String, Object> getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("version", "v1");
        response.put("adminContact", "admin@smartcampus.local");

        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        response.put("resources", resources);
        return response;
    }
}