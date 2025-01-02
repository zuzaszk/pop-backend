# Project of Projects

The project is titled **Project of Projects** (acronym PoP), as it is a project designed to serve as a management tool for the assessment of Team Projects (Zespołowe Przedsięwzięcie Inżynierskie—ZPI), which is undoubtedly a critical aspect of engineering education at the Wroclaw University of Science and Technology. The platform addresses inconveniences of current practices (such as file uploading and transparency in feedback) by automating workflows and enhancing collaboration among students, supervisors, reviewers, and event chairs. Key features include role-based access, project management workflows, weighted evaluations, edition-based filtering, and a comprehensive statistics dashboard.

The platform improves efficiency, saves time, and ensures project evaluation and feedback transparency. With modern technologies, PoP delivers a purpose-built solution. Enhancing organization and communication ensures a collaborative environment for a more effective and transparent ZPI management process. Future improvements include a mobile app, a Polish version, and offline access.

---

## Introduction

The Team Project is a pillar of engineering education, encouraging cooperation among stakeholders—students, supervisors, reviewers, event chairs, and spectators. Despite its importance, the existing process of organizing, managing, and evaluating ZPI projects often involves significant manual effort and inefficiency. These difficulties include disjointed communication, time-consuming project management, and a lack of transparency in feedback and assessment.

To address these issues, our team developed an integrated platform that would simplify ZPI management and organization. This system focuses on essential activities that allow students to form teams, submit project files, and receive feedback. Supervisors and reviewers will benefit from automated workflows for project evaluation, while event chairs and spectators will have easy access to view relevant information about the team projects.

The platform uses JWT for state-free secure authentication and authorization. Additionally, the integration of OAuth2 with Google and OAuth 1.0 with USOS ensures access for different types of users, e.g., spectators (the former) and university-related users (the latter, through university credentials). We used Granted Authority to secure API call access.

The primary objectives of our projects are to:

- **Improve Efficiency** through task automation such as project submission, invitation management, and enhanced stakeholder collaboration to reduce administrative burden.
- **Save Time** by organizing all project-related information in a single platform to minimize the time spent searching and managing files.
- **Enhance Collaboration** by providing better interaction with features such as messaging and real-time project status updates.
- **Provide Transparency**, ensuring stakeholders have straightforward and easy access to view relevant information about the team projects, so everything is handled openly and fairly, reducing biases and misunderstandings.

---

## Related Work

### Existing Solutions and Technologies

When developing PoP, we began by exploring existing tools that might address similar challenges. While platforms such as EasyChair, Google Scholar, and ResearchGate offer valuable functionalities in their respective domains, none adequately meet the specific needs of our faculty, particularly in managing student projects. 

- **EasyChair** is widely used for managing academic conferences, offering features like paper submissions, peer reviews, and feedback coordination. However, its design is tailored to conference workflows, making it unsuitable for the detailed and iterative processes involved in supervising and evaluating student projects.
- **Google Scholar** and **ResearchGate** serve as essential platforms for researchers to share their work and build professional profiles. However, they lack the tools necessary for submission management, role-based workflows, and feedback coordination, which are crucial in an educational context.

PoP was built from the ground up to address these gaps, focusing exclusively on our faculty's needs. Designed with input from all stakeholders—students, supervisors, reviewers, and chairs—it integrates all necessary functionalities into a single platform. Unlike general-purpose academic tools, PoP is tailored to manage student projects comprehensively, from submissions and evaluations to feedback and role assignments.

Key features of PoP include:

- **Role-based permissions** that ensure users (e.g., students, supervisors, and reviewers) access only the information relevant to their roles.
- Structured workflows for evaluations and detailed feedback mechanisms, enabling transparency and efficiency.

While platforms like EasyChair or ResearchGate excel in their domains, PoP’s specialized design provides the targeted solutions our faculty requires for managing team projects.

### Technology Choices

#### Database

- **PostgreSQL**
  PostgreSQL was selected for its ability to handle complex queries, joins, and foreign key constraints, ensuring data integrity. Entities such as `users`, `projects`, `edits`, `evaluations`, and `invitations` were identified, requiring a relational database management system.

#### Backend

- **Spring Boot**
  Spring Boot was chosen for its robust features and ease of implementing secure authentication using OAuth2 and OAuth 1.0. Its excellent integration with various data sources, including PostgreSQL, and the "convention over configuration" approach accelerated development.

- **Maven**
  Maven facilitated efficient dependency management and seamless integration of libraries and frameworks like Spring Boot, MyBatis, and PostgreSQL. It also streamlined the application packaging and deployment process.

#### Frontend

- **Svelte**
  Svelte’s lightweight nature and intuitive reactivity enabled the team to build a dynamic and responsive user interface efficiently.

- **Tailwind CSS**
  Tailwind CSS provided a comprehensive set of utility classes, eliminating the need for custom CSS and ensuring a cohesive visual design.

---

## Results

The PoP application was successfully developed to address the unique needs of managing student projects. It integrates functionalities for streamlined workflows, collaboration enhancement, and tools for submissions, evaluations, and feedback.

### Role-Based Functionalities and Data Access

| Role       | Key Features                       | Access Level          |
|------------|------------------------------------|-----------------------|
| Student    | Submit project, receive feedback  | Limited to their team |
| Supervisor | Evaluate projects, manage teams   | Full project access   |
| Chair      | Assign supervisors and reviewers  | Admin access          |
| Reviewer   | Provide evaluations               | Full project access   |
| Spectator  | Browse projects                   | Limited view          |

**Flexibility and scalability:**

- **Edition-role concept**: User roles can change based on project editions.
- **Multiple roles**: A user can hold multiple roles simultaneously.

---

## Conclusion

PoP provides a specialized, efficient, and transparent platform tailored for ZPI project management. Future improvements include mobile app development, a Polish language version, and offline access.
