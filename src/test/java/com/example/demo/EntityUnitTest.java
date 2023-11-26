package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

	private Doctor d1;

	private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;


    @Test
    void testDoctorEntityPersistenceWithoutAdditionalInfo() throws Exception{
        d1 = new Doctor();

        entityManager.persistAndFlush(d1);

        Doctor savedDoctor = entityManager.find(Doctor.class, d1.getId());

        assertThat(savedDoctor).isNotNull();
        assertThat(savedDoctor.getId()).isEqualTo(d1.getId());
        assertThat(savedDoctor.getFirstName()).isNull();
        assertThat(savedDoctor.getLastName()).isNull();
        assertThat(savedDoctor.getAge()).isEqualTo(0);
        assertThat(savedDoctor.getEmail()).isNull();
    }

    @Test
    void testDoctorEntityPersistenceWithAdditionalInfo() throws Exception{
        d1 = new Doctor("John", "Doe", 35, "john.doe@example.com");
        entityManager.persistAndFlush(d1);

        Doctor savedDoctor = entityManager.find(Doctor.class, d1.getId());

        assertThat(savedDoctor).isNotNull();
        assertThat(savedDoctor.getId()).isEqualTo(d1.getId());
        assertThat(savedDoctor.getFirstName()).isEqualTo(d1.getFirstName());
        assertThat(savedDoctor.getLastName()).isEqualTo(d1.getLastName());
        assertThat(savedDoctor.getAge()).isEqualTo(d1.getAge());
        assertThat(savedDoctor.getEmail()).isEqualTo(d1.getEmail());
    }

    @Test
    void testDoctorEntityPersistenceRemove() throws Exception{
        d1 = new Doctor("John", "Doe", 35, "john.doe@example.com");

        entityManager.persistAndFlush(d1);

        Doctor savedDoctor = entityManager.find(Doctor.class, d1.getId());

        entityManager.remove(savedDoctor);

        Doctor removedDoctor = entityManager.find(Doctor.class, d1.getId());

        assertThat(removedDoctor).isNull();
    }

    @Test
    void testPatientEntityPersistenceWithoutAdditionalInfo() throws Exception{
        p1 = new Patient();

        entityManager.persistAndFlush(p1);

        Patient savedPatient = entityManager.find(Patient.class, p1.getId());

        assertThat(savedPatient).isNotNull();
        assertThat(savedPatient.getId()).isEqualTo(p1.getId());
        assertThat(savedPatient.getFirstName()).isNull();
        assertThat(savedPatient.getLastName()).isNull();
        assertThat(savedPatient.getAge()).isEqualTo(0);
        assertThat(savedPatient.getEmail()).isNull();
    }

    @Test
    void testPatientEntityPersistenceWithAdditionalInfo() throws Exception{
        p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
    
        entityManager.persistAndFlush(p1);

        Patient savedPatient = entityManager.find(Patient.class, p1.getId());   

        assertThat(savedPatient).isNotNull();
        assertThat(savedPatient.getId()).isEqualTo(p1.getId());
        assertThat(savedPatient.getFirstName()).isEqualTo(p1.getFirstName());
        assertThat(savedPatient.getLastName()).isEqualTo(p1.getLastName());
        assertThat(savedPatient.getAge()).isEqualTo(p1.getAge());
        assertThat(savedPatient.getEmail()).isEqualTo(p1.getEmail());
    }

    @Test
    void testPatientEntityPersistenceRemove() throws Exception{
        p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        entityManager.persistAndFlush(p1);

        Patient savedPatient = entityManager.find(Patient.class, p1.getId());

        entityManager.remove(savedPatient);

        Patient removedPatient = entityManager.find(Patient.class, p1.getId());

        assertThat(removedPatient).isNull();
    }

    @Test
    void testRoomEntityPersistence() throws Exception{
        r1 = new Room("Room 1");
    
        entityManager.persistAndFlush(r1);
    
        Room savedRoom = entityManager.find(Room.class, r1.getRoomName());
    
        assertThat(savedRoom).isNotNull();
        assertThat(savedRoom.getRoomName()).isEqualTo(r1.getRoomName());
    }
    
    @Test
    void testRoomEntityPersistenceRemove() throws Exception{
        r1 = new Room("Room 1");
    
        entityManager.persistAndFlush(r1);
    
        Room savedRoom = entityManager.find(Room.class, r1.getRoomName());
    
        entityManager.remove(savedRoom);
    
        Room removedRoom = entityManager.find(Room.class, r1.getRoomName());
    
        assertNull(removedRoom);
    }

    @Test
    void testAppointmentEntityPersistenceWithoutAdditionalInfo() throws Exception {
        a1 = new Appointment();
        entityManager.persistAndFlush(a1);

        Appointment savedAppointment = entityManager.find(Appointment.class, a1.getId());

        assertThat(savedAppointment).isNotNull();
        assertThat(savedAppointment.getId()).isEqualTo(a1.getId());
        assertThat(savedAppointment.getPatient()).isNull();
        assertThat(savedAppointment.getDoctor()).isNull();
        assertThat(savedAppointment.getRoom()).isNull();
        assertThat(savedAppointment.getStartsAt()).isNull();
        assertThat(savedAppointment.getFinishesAt()).isNull();
    }

    @Test
    void testAppointmentEntityPersistence() throws Exception {
        p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        d1 = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        r1 = new Room("Room 1");

        entityManager.persistAndFlush(p1);
        entityManager.persistAndFlush(d1);
        entityManager.persistAndFlush(r1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        a1 = new Appointment(p1, d1, r1, startsAt, finishesAt);

        entityManager.persistAndFlush(a1);

        Appointment savedAppointment = entityManager.find(Appointment.class, a1.getId());

        assertThat(savedAppointment).isNotNull();
        assertThat(savedAppointment.getId()).isEqualTo(a1.getId());
        assertThat(savedAppointment.getPatient()).isEqualTo(a1.getPatient());
        assertThat(savedAppointment.getDoctor()).isEqualTo(a1.getDoctor());
        assertThat(savedAppointment.getRoom()).isEqualTo(a1.getRoom());
        assertThat(savedAppointment.getStartsAt()).isEqualTo(a1.getStartsAt());
        assertThat(savedAppointment.getFinishesAt()).isEqualTo(a1.getFinishesAt());
    }

    @Test
    void testAppointmentEntityPersistenceRemove() throws Exception {
        p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        d1 = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        r1 = new Room("Room 1");

        entityManager.persistAndFlush(p1);
        entityManager.persistAndFlush(d1);
        entityManager.persistAndFlush(r1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        a1 = new Appointment(p1, d1, r1, startsAt, finishesAt);

        entityManager.persistAndFlush(a1);

        Appointment savedAppointment = entityManager.find(Appointment.class, a1.getId());

        entityManager.remove(savedAppointment);

        Appointment removedAppointment = entityManager.find(Appointment.class, a1.getId());

        assertThat(removedAppointment).isNull();
    }

    @Test
    void testAppointmentEntityOverlaps() throws Exception {
        p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        d1 = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        r1 = new Room("Room 1");

        entityManager.persistAndFlush(p1);
        entityManager.persistAndFlush(d1);
        entityManager.persistAndFlush(r1);

        // For appointment a1
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:00 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:00 24/04/2023", formatter);

        // For appointment a2
        LocalDateTime a2startsAt= LocalDateTime.parse("18:30 24/04/2023", formatter);
        LocalDateTime a2finishesAt = LocalDateTime.parse("19:30 24/04/2023", formatter);

        // For appointment a3
        LocalDateTime a3startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime a3finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        a1 = new Appointment(p1, d1, r1, startsAt, finishesAt);
        a2 = new Appointment(p1, d1, r1, a2startsAt, a2finishesAt);
        a3 = new Appointment(p1, d1, r1, a3startsAt, a3finishesAt);

        entityManager.persistAndFlush(a1);
        entityManager.persistAndFlush(a2);
        entityManager.persistAndFlush(a3);

        assertThat(a1.overlaps(a2)).isTrue();
        assertThat(a1.overlaps(a3)).isTrue();
        assertThat(a2.overlaps(a3)).isFalse();

    }
}
