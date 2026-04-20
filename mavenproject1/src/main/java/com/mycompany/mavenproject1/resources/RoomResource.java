/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.resources;
import com.mycompany.mavenproject1.exception.RoomNotEmptyException;
import com.mycompany.mavenproject1.model.Room;
import com.mycompany.mavenproject1.repository.DataStore;
import java.net.URI;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 *
 * @author shenandim
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Collection<Room> getAllRooms() {
        return DataStore.rooms.values();
    }

    @POST
    public Response createRoom(Room room) {
        DataStore.rooms.put(room.getId(), room);
        return Response.created(URI.create("/api/v1/rooms/" + room.getId()))
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room with ID " + roomId + " not found.");
        }
        return room;
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            throw new NotFoundException("Room with ID " + roomId + " not found.");
        }

        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete room because it still has assigned sensors.");
        }

        DataStore.rooms.remove(roomId);
        return Response.ok()
                .entity("Room deleted successfully.")
                .build();
    }
}
