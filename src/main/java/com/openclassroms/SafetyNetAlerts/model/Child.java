package com.openclassroms.SafetyNetAlerts.model;

import lombok.*;

import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Child {
        String firstName;
        @Id
        String lastName;
        long age;
        List<Person> houseMembers;

        public Child() {
        }

        public Child(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
        }

        public Child(String firstName, String lastName, long age) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.age = age;
        }

        public Child(String firstName, String lastName, long age, List<Person> houseMembers) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.age = age;
                this.houseMembers = houseMembers;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Child child = (Child) o;
                return Objects.equals(firstName, child.firstName) &&
                        Objects.equals(lastName, child.lastName);
        }

        @Override
        public int hashCode() {
                return Objects.hash(firstName, lastName);
        }
}
