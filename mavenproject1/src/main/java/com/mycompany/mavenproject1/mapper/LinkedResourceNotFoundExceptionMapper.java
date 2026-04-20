/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.mapper;

import com.mycompany.mavenproject1.exception.LinkedResourceNotFoundException;
import com.mycompany.mavenproject1.model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author shenandim
 */

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException>{
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            422,
            "Unprocessble Entity",
            ex.getMessage()
        );
        
        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
