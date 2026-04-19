/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.exception;

/**
 *
 * @author shenandim
 */
public class RoomNotEmptyException extends RuntimeException{  // exception that occurs when someone tried to delete a roon that was not there
    public RoomNotEmptyException(String message) {
     super(message);   
    }
}
