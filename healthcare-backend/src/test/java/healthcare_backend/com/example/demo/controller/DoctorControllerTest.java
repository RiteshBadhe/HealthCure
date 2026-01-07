package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.model.Doctor;
import healthcare_backend.com.example.demo.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorController doctorController;

    @Test
    void getAllDoctorsReturnsList() {
        Doctor d = new Doctor("Alice","Cardiology","alice@example.com","12345");
        when(doctorRepository.findAll()).thenReturn(List.of(d));

        List<Doctor> result = doctorController.getAllDoctors();

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
    }
}
