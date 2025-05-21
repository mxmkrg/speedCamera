package model.person;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
public abstract class Person {
    private String firstName;
    private String lastName;
    private final LocalDate dateOfBirth;
    private char gender;    //'M', 'F', 'O'
    private String address;
    private String email;
    private long phoneNumber;
}
