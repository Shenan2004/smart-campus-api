/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1.exception;

/**
 *
 * @author shenandim
 */
public class LinkedResourceNotFoundException extends RuntimeException{  // eception occurs when tried to create a sensors with invalid room links
    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}
