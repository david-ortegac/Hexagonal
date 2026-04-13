package co.edu.cun.ms_backend_java_persons.persons.adapter.restfull.v1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.cun.ms_backend_java_persons.persons.adapter.restfull.v1.controller.DTO.PersonAdapter;
import co.edu.cun.ms_backend_java_persons.persons.adapter.restfull.v1.controller.Mappers.IMapperAdapter;
import co.edu.cun.ms_backend_java_persons.persons.application.IPerson;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonaController {

    private final IPerson personService;
    private final IMapperAdapter mapperAdapter;

    public PersonaController(IPerson personService, IMapperAdapter mapperAdapter) {
        this.personService = personService;
        this.mapperAdapter = mapperAdapter;
    }

    @GetMapping("/")
    public ResponseEntity<List<PersonAdapter>> getPersons() {
        return ResponseEntity.ok().body(mapperAdapter.toAdapterList(personService.getAllPersons()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonAdapter> getPersonById(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(mapperAdapter.toAdapter(personService.getPersonById(id)));
    }

    @PostMapping("/")
    public ResponseEntity<PersonAdapter> createPerson(@RequestBody PersonAdapter personAdapter) {
        return ResponseEntity.ok().body(mapperAdapter.toAdapter(personService.createPerson(mapperAdapter.toDomain(personAdapter))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonAdapter> updatePerson(@PathVariable(name = "id") int id, @RequestBody PersonAdapter personAdapter) {
        return ResponseEntity.ok().body(mapperAdapter.toAdapter(personService.updatePerson(mapperAdapter.toDomain(personAdapter), id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(personService.deletePerson(id));
    }
}
