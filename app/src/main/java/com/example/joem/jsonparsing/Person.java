package com.example.joem.jsonparsing;

/**
 * Created by JoeM on 10/9/2017.
 */

public class Person {
    String name;
    long id;
    int age;
    Address address;

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}
