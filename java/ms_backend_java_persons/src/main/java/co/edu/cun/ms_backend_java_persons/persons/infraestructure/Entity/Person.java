package co.edu.cun.ms_backend_java_persons.persons.infraestructure.Entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "persons")
@Data
public class Person {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "updated_at", nullable = false)
    private String updatedAt;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @PrePersist
    void onCreate() {
        final String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        if (createdAt == null || createdAt.isBlank()) {
            createdAt = now;
        }
        if (updatedAt == null || updatedAt.isBlank()) {
            updatedAt = now;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
}
