package com.example.joem.jsonparsing;

/**
 * Created by JoeM on 10/9/2017.
 */

public class Address {
    String line1, city, state, zip;

    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "line1='" + line1 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
