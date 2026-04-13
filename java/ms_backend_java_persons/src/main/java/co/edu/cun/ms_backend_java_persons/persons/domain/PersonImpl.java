package co.edu.cun.ms_backend_java_persons.persons.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.cun.ms_backend_java_persons.persons.application.IPerson;
import co.edu.cun.ms_backend_java_persons.persons.domain.DTO.PersonDomain;
import co.edu.cun.ms_backend_java_persons.persons.infraestructure.Entity.Person;
import co.edu.cun.ms_backend_java_persons.persons.infraestructure.Mapper.IPersonMapper;
import co.edu.cun.ms_backend_java_persons.persons.infraestructure.Respository.IPersonRepository;
import jakarta.transaction.Transactional;

@Service
public class PersonImpl implements IPerson {

    private final IPersonRepository repository;
    private final IPersonMapper mapper;

    public PersonImpl(IPersonRepository repository, IPersonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    
    @Override
    public List<PersonDomain> getAllPersons() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    public PersonDomain getPersonById(int id) {
        return mapper.toDomain(repository.findById(id).orElseThrow(() -> new RuntimeException("Person not found")));
    }

    @Override
    @Transactional
    public PersonDomain createPerson(PersonDomain person) {
       return mapper.toDomain(repository.save(mapper.toEntity(person)));
    }

    @Override
    @Transactional
    public PersonDomain updatePerson(PersonDomain person, int id) {
        PersonDomain personDomain = getPersonById(id);
        personDomain.setName(person.getName());
        personDomain.setEmail(person.getEmail());
        personDomain.setPhone(person.getPhone());
        personDomain.setAddress(person.getAddress());
        personDomain.setCity(person.getCity());
        Person personEntity = mapper.toEntity(personDomain);
        personEntity.setId(id);
        return mapper.toDomain(repository.save(personEntity));
    }

    @Override
    @Transactional
    public String deletePerson(int id) {
        Person person = repository.findById(id).orElseThrow(() -> new RuntimeException("Person not found"));
        repository.delete(person);
        return "Person deleted successfully";
    }
    
}
