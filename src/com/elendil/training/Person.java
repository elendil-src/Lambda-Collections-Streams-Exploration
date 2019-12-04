package com.elendil.training;

import java.util.Objects;

public class Person {


    private String firstName;
    private String lastName;
    private int age;

    Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    String getFirstName() { return firstName; }
    String getLastName() {
        return lastName;
    }

    int getAge() {
        return age;
    }
    void setAge(int age) { this.age = age; }

    void setFirstName(String firstName) { this.firstName = firstName;}
    void setLastName(String lastName) { this.lastName = lastName; }

    @Override
    public String toString() {
        return "Person[" + "firstName=" + firstName + "; lastName=" + lastName + "; age=" + age +"]";
    }

    @Override
    public boolean equals(Object o ) {
        if (o == null) return false;
        if ( o instanceof Person )
        {
            Person c = (Person)o;
            return firstName.equalsIgnoreCase(c.getFirstName())
                    && this.getLastName().equalsIgnoreCase(c.getLastName())
                    && this.getAge() == c.getAge();
        }
        return  false;
    }
    @Override
    public int hashCode()  {
        return Objects.hash(getFirstName(), getLastName(), getAge());
    }

}
