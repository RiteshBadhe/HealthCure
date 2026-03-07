# Project Documentation: HealthCare Management System

**Author:** [Ritesh Badhe]
**Date:** February 25, 2026
**Course:** [TYBSC CS]


---

### 1. Introduction

This document provides a comprehensive technical overview of the HealthCare Management System, a full-stack web application designed to streamline interactions between patients, doctors, and administrators. The system facilitates appointment booking, management of medical records, and provides emergency SOS features. The frontend is developed using React, offering a dynamic and responsive user interface. The backend is a robust RESTful service built with Java and the Spring Boot framework, ensuring scalability and maintainability.

The primary objective of this documentation is to detail the system's architecture, design, and key workflows in accordance with IEEE standards. It serves as a formal record for academic evaluation and as a reference for future development and maintenance. The analysis herein is based exclusively on the existing codebase, providing an accurate representation of the implemented system.

### 2. System Overview

The HealthCare Management System is a role-based application with three primary actors: Patients, Doctors, and Administrators. Patients can register, book appointments, view their medical history, and trigger SOS alerts. Doctors can manage their profiles, view their appointment schedules, and manage patient medical records. Administrators have overarching permissions to manage doctors, patients, and system-wide data.

The application follows a modern client-server architecture. The React frontend communicates with the Spring Boot backend via a set of RESTful API endpoints. Authentication is managed through a token-based system, and authorization is enforced at the controller level using custom annotations to restrict access based on user roles. Data persistence is handled by a relational database, with Spring Data JPA managing all database interactions.

### 3. System Architecture

The system is designed as a multi-tiered application, promoting a clear separation of concerns between the presentation, business logic, and data access layers. This architecture enhances modularity and simplifies development and testing. The frontend, built with React, is completely decoupled from the backend, which allows for independent development cycles and deployment.

Communication between the frontend and backend is facilitated by a REST API. An API Gateway or Load Balancer can be placed in front of the backend services in a production environment to manage traffic, enhance security, and provide a single entry point for all API requests. The Spring Boot backend itself is structured into a controller layer for handling HTTP requests, a service layer for encapsulating business logic, and a repository layer for data abstraction, all built upon a robust domain model of JPA entities.

![System Architecture Diagram](HealthCure%20projec%20diagrams/architecture.png)

### 4. ER Diagram

The Entity-Relationship (ER) diagram below illustrates the logical structure of the system's database. It defines the entities (tables), their attributes (columns), and the relationships between them. Primary keys are denoted by 'PK' and foreign keys by 'FK', which establish the links between associated tables, such as the one-to-many relationship between a `DOCTOR` and their `APPOINTMENTS`.

The schema is designed to be normalized, reducing data redundancy and improving data integrity. For instance, user information is centralized in the `USERS` table, while role-specific data is maintained in separate `DOCTORS` and `PATIENTS` tables. Key entities include `USERS`, `DOCTORS`, `PATIENTS`, `APPOINTMENTS`, and `MEDICAL_RECORDS`, which form the core of the application's data model.

![ER Diagram](HealthCure%20projec%20diagrams/er.png)

### 5. UML Class Diagram

The UML Class Diagram provides a static view of the backend system's structure. It visualizes the main classes, including their attributes, methods, and the relationships among them. The diagram is organized by layer: Controllers, Services, Repositories, and the Model (Entities), reflecting the application's package structure.

This diagram highlights key design patterns, such as the use of the Repository pattern for data access, where interfaces like `UserRepository` extend `JpaRepository` to abstract database operations. Dependency injection is evident, as controllers depend on repository interfaces to function. The relationships between entity classes, such as the composition between `Doctor` and `DoctorAvailability`, are also clearly depicted, providing a detailed blueprint of the backend codebase.

![UML Class Diagram](HealthCure%20projec%20diagrams/class.png)

### 6. Use Case Diagram

The Use Case diagram defines the functional requirements of the system from the perspective of its users (actors). It identifies the primary actors—Admin, Doctor, Patient, and an Unauthenticated User—and maps out the actions they are permitted to perform. This provides a clear overview of the system's features and the scope of access for each role.

The diagram illustrates that certain use cases, like "View Doctors," are available to multiple actors, while others, such as "Add New Doctor," are restricted to the Admin. Relationships like `<<include>>` and `<<extend>>` are used to show dependencies between use cases. For example, booking an appointment may include the step of viewing available doctors. This diagram is crucial for understanding the system's behavior and ensuring that all functional requirements are met.

![Use Case Diagram](HealthCure%20projec%20diagrams/usecase.png)

### 7. Activity Diagrams

The following activity diagrams illustrate the dynamic behavior of key business workflows within the system. They model the step-by-step flow of activities, including decision points, parallel processes, and error handling, providing a detailed view of the operational logic.

**User Authentication Workflow:** This diagram shows the process for user registration and login. It includes validation checks for user input, verification of existing credentials, and the conditional logic for creating a doctor-specific profile upon registration.

![Authentication Activity Diagram](HealthCure%20projec%20diagrams/activity_auth.png)

**Appointment Booking Workflow:** This diagram details the sequence of actions a patient takes to book an appointment. It highlights the validation of appointment data, the persistence of the appointment record, and the subsequent creation of a notification to confirm the booking with the patient.

![Appointment Booking Activity Diagram](HealthCure%20projec%20diagrams/activity_booking.png)

### 8. Sequence Diagrams

The sequence diagram below models the interactions between different components of the system over time for several key scenarios. It provides a detailed, step-by-step visualization of how messages are passed between the frontend, backend services, and the database to accomplish a specific task.

The diagram consolidates four critical flows: User Registration, Login Authentication, Creating a New Appointment, and a Role-Based Authorization Check. It clearly shows the sequence of API calls, internal method invocations, and database queries. For instance, the authorization flow demonstrates how a security filter intercepts an incoming request to a protected endpoint, validates the user's role from the request header, and either grants or denies access before the controller method is even executed. This level of detail is essential for understanding the system's runtime behavior and debugging potential issues.

![Sequence Diagram](HealthCure%20projec%20diagrams/sequence.png)

### 9. Data Flow Diagram (DFD)

The Data Flow Diagram (DFD) illustrates how data moves through the system. The Level 0 DFD provides a context view, showing the entire system as a single process interacting with external entities like the User and Emergency Contacts. The Level 1 DFD breaks this down further, exposing the main sub-processes within the system, such as "Manage User Accounts" and "Manage Appointments."

The Level 1 diagram shows the flow of data between these processes and the data stores (database tables) they interact with. For example, an "Appointment Request" from a User flows into the "Manage Appointments" process, which in turn writes "Appointment Data" to the `Appointments` data store and sends "Appointment Notification" data to the "Manage Notifications" process. This visualization is critical for understanding how information is processed and stored throughout the application.

![Data Flow Diagram](HealthCure%20projec%20diagrams/dfd.png)

### 10. Conclusion

This document has presented a thorough analysis of the HealthCare Management System, detailing its architecture, database schema, and key operational workflows through a series of standardized diagrams. The system is built on a modern, decoupled architecture that promotes scalability and maintainability. The role-based access control and clear separation of concerns provide a secure and robust foundation for managing sensitive healthcare data.

The diagrams and explanations provided herein offer a comprehensive and accurate representation of the implemented system, derived directly from its source code. This documentation fulfills its purpose as a formal academic record and can effectively guide any future efforts in maintaining, extending, or evaluating the application. The project successfully meets its objectives of providing a functional and well-structured platform for healthcare management.

---
