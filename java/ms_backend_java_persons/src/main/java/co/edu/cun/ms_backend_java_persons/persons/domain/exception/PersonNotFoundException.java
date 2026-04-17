package co.edu.cun.ms_backend_java_persons.persons.domain.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(int id) {
        super("Person with id " + id + " was not found");
    }
}
