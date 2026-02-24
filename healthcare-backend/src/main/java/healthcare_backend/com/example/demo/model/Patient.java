package healthcare_backend.com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String disease;

    // ---- GETTERS ----

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDisease() {
        return disease;
    }

    // ---- SETTERS ----

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }
}
