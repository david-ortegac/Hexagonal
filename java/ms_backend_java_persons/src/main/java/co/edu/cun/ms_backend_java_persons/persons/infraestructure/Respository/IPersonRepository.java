package co.edu.cun.ms_backend_java_persons.persons.infraestructure.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.cun.ms_backend_java_persons.persons.infraestructure.Entity.Person;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Integer> {
    
}
