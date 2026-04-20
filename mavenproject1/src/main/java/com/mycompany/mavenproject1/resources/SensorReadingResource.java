/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.resources;

import com.mycompany.mavenproject1.exception.SensorUnavailableException;
import com.mycompany.mavenproject1.model.Sensor;
import com.mycompany.mavenproject1.model.SensorReading;
import com.mycompany.mavenproject1.repository.DataStore;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author shenandim
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getAllReadings() {
        return DataStore.readings.get(sensorId);
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor.getStatus() != null && sensor.getStatus().equalsIgnoreCase("MAINTENANCE")) {
            throw new SensorUnavailableException(
                    "Cannot add reading. Sensor " + sensorId + " is currently in MAINTENANCE mode."
            );
        }

        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }

        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        DataStore.readings.get(sensorId).add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.created(URI.create("/api/v1/sensors/" + sensorId + "/readings/" + reading.getId()))
                .entity(reading)
                .build();
    }
}
