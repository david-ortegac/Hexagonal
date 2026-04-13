package co.edu.cun.ms_backend_java_persons.persons.infraestructure.Mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.cun.ms_backend_java_persons.persons.domain.DTO.PersonDomain;
import co.edu.cun.ms_backend_java_persons.persons.infraestructure.Entity.Person;

@Mapper(componentModel = "spring")
public interface IPersonMapper {
    PersonDomain toDomain(Person entity);
    Person toEntity(PersonDomain domain);

    List<PersonDomain> toDomainList(List<Person> entities);
}
