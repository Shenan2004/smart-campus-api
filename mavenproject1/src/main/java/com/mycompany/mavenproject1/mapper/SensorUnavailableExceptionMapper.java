/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.mapper;

import com.mycompany.mavenproject1.exception.SensorUnavailableException;
import com.mycompany.mavenproject1.model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author shenandim
 */

// exception class for the occurance if the sensor was in maintenance
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException>{
    
    @Override
    public Response toResponse(SensorUnavailableException ex) {
        ErrorResponse error = new ErrorResponse(
                403,
                "Forbidden",
                ex.getMessage()
        );
        
        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
