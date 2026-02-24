package healthcare_backend.com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor")  // ✅ Your DB table name
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String photo;
    @Column(name = "doctor_name")
    private String name;
    private String specialization;
    private String email;
    private String phone;

    // Default constructor
    public Doctor() {}

    // 4-parameter constructor (for tests)
    public Doctor(String name, String specialization, String email, String phone) {
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
    }

    // ✅ ALL GETTERS (TESTS NEED THESE!)
    public Long getId() { return id; }
    public String getPhoto() { return photo; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // ✅ ALL SETTERS (JPA NEEDS THESE!)
    public void setId(Long id) { this.id = id; }
    public void setPhoto(String photo) { this.photo = photo; }
    public void setName(String name) { this.name = name; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}
