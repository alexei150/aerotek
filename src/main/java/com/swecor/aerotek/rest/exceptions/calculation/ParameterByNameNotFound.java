package com.swecor.aerotek.rest.exceptions.calculation;

public class ParameterByNameNotFound extends RuntimeException{

    public ParameterByNameNotFound(String paramName){
        super(paramName);
    }

}
