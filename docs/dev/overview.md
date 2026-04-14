# Project Overview

---

### Navigation

- [🏠 Main Readme](../../README.md)
- **[📌 Project Overview](overview.md)**
- [🚀 Getting Started](getting-started.md)
- [🧱 Project Structure](structure.md)
- [🛠️ Build and Dependency Management](build-and-dependencies.md)
- [▶️ Running the Application](running.md)
- [📘 Code Standards and Conventions](code-standards.md)
- [🧪 Testing](testing.md)
- [🔌 External API Integration](api.md)

---

The Filmbase project is a web-application project whose main purpose is to act as a "catalog" for film of all kind, including movies and TV shows. It serves as a central hub providing information for this type of media regardless of what streaming platform(s), languages, regions, etc. the film might be constrained to.

#### Key Features

Filmbase features (but is not limited to):

- A catalog-style database of movies and shows for the user to browse
- User and admin views
- Searching and filtering
- Importing movies from an external source (TMDB)
- Ability to read from and write to CSV files about movie data

#### Tech Stack

| Layer                  | Technology                         |
|------------------------|------------------------------------|
| Language               | Java                               |
| Build Tool             | Maven                              |
| Framework              | Spring Boot, Vaadin                |
| Front-End              | Vaadin*                            |
| Data Modeling Approach | CSV file, in-memory JPA repository |
| Testing                | JUnit 5, Mockito                   |

\* includes auto-generated JavaScript and HTML

#### Deployment Model

The packaged application (a JAR file) hosts the web-application when run, so it is designed to be deployed on a cloud-based hosting service such as AWS.