package co.edu.cun.ms_backend_java_persons.persons.application;

import java.util.List;

import co.edu.cun.ms_backend_java_persons.persons.domain.DTO.PersonDomain;

public interface IPerson {
    public List<PersonDomain> getAllPersons();
    public PersonDomain getPersonById(int id);
    public PersonDomain createPerson(PersonDomain person);
    public PersonDomain updatePerson(PersonDomain person, int id);
    public String deletePerson(int id);
}
