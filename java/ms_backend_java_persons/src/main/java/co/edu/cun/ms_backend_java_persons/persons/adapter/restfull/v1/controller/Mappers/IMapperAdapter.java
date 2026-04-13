package co.edu.cun.ms_backend_java_persons.persons.adapter.restfull.v1.controller.Mappers;

import java.util.List;

import org.mapstruct.Mapper;

import co.edu.cun.ms_backend_java_persons.persons.adapter.restfull.v1.controller.DTO.PersonAdapter;
import co.edu.cun.ms_backend_java_persons.persons.domain.DTO.PersonDomain;

@Mapper(componentModel = "spring")
public interface IMapperAdapter {
    PersonAdapter toAdapter(PersonDomain domain);
    PersonDomain toDomain(PersonAdapter adapter);

    List<PersonAdapter> toAdapterList(List<PersonDomain> domains);
}
