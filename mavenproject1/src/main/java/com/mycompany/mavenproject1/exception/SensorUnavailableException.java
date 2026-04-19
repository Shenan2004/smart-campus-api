/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.exception;

/**
 *
 * @author shenandim
 */
public class SensorUnavailableException extends RuntimeException{  // postin reading to unavailable sensors
    public SensorUnavailableException(String message){
        super(message);
    }
}
