/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.repository;

/**
 *
 * @author shenandim
 */
import com.mycompany.mavenproject1.model.Room;
import com.mycompany.mavenproject1.model.Sensor;
import com.mycompany.mavenproject1.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {  // the database 
    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();

    static {
        // creating an empty reading list for creating rooms and sensors
        Room room1 = new Room("LIB-301", "Library Quiet Study", 40);
        rooms.put(room1.getId(), room1);

        Sensor sensor1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 25.5, "LIB-301");
        sensors.put(sensor1.getId(), sensor1);
        room1.getSensorIds().add(sensor1.getId());

        readings.put(sensor1.getId(), new ArrayList<>());
    }

    private DataStore() {
    }
}
