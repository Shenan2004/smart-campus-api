/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.resources;

import com.mycompany.mavenproject1.exception.LinkedResourceNotFoundException;
import com.mycompany.mavenproject1.model.Sensor;
import com.mycompany.mavenproject1.model.Room;
import com.mycompany.mavenproject1.repository.DataStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author shenandim
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Collection<Sensor> getAllSensors(@QueryParam("type") String type) {
        if (type == null || type.trim().isEmpty()) {
            return DataStore.sensors.values();
        }

        List<Sensor> filteredSensors = new ArrayList<>();
        for (Sensor sensor : DataStore.sensors.values()) {
            if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }
        return filteredSensors;
    }

    @POST
    public Response createSensor(Sensor sensor) {
        // Generate ID if missing
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            sensor.setId(java.util.UUID.randomUUID().toString());
        }

        Room room = DataStore.rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException(
                    "Cannot create sensor. Room with ID " + sensor.getRoomId() + " does not exist."
            );
        }

        DataStore.sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        DataStore.readings.put(sensor.getId(), new ArrayList<>());

        return Response.created(URI.create("/api/v1/sensors/" + sensor.getId()))
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            throw new NotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        return sensor;
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            throw new NotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        return new SensorReadingResource(sensorId);
    }
}
