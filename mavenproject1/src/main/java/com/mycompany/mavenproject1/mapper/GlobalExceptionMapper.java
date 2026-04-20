/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.mapper;

import com.mycompany.mavenproject1.model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author shenandim
 */
@Provider // register the class as an exception
public class GlobalExceptionMapper implements ExceptionMapper<Throwable>{
    
    @Override
    public Response toResponse(Throwable ex) {
        ErrorResponse error = new ErrorResponse(
                500,
                "Internal Server Error",
                "Sometimes went wrong. Please contact the administrator."
        );
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
