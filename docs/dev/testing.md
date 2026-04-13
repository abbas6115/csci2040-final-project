# Testing

---

### Navigation

- [🏠 Main Readme](../../README.md)
- [📌 Project Overview](overview.md)
- [🚀 Getting Started](getting-started.md)
- [🧱 Project Structure](structure.md)
- [🛠️ Build and Dependency Management](build-and-dependencies.md)
- [▶️ Running the Application](running.md)
- [📘 Code Standards and Conventions](code-standards.md)
- **[🧪 Testing](testing.md)**
- [🔌 API Information](api.md)

---

Testing was built using JUnit 5 for our project, along with Spring Boot's test support and Mockito to help with things like isolating dependencies (such as the CSV writer). The tests are mostly backend-focused. Test files are stored in `src/test/`.

Test files are their own Java classes, and each one's purpose is to test its respective object in our code. For example, `MovieCsvWriterTest.java` holds the JUnit tests for the `MovieCsvWriter.java` class. To run any test, simply run the class file.

*"Unit tests form the base layer and are split into two styles: the MovieCsvWriter tests are clear box, directly verifying internal CSV formatting, escaping, and parsing logic against the file system using JUnit's TempDir, while the MovieDatabaseCommands tests use Mockito to mock out the repository and CSV writer, asserting that the correct methods are called in the correct order."*

*"The CustomUser and ViewWatchList tests are opaque box, treating the class purely through its public API without any knowledge of the internal LinkedList implementation. Integration tests sit above that and wire real components together. A raw JPA EntityManagerFactory backed by an H2 in-memory database is constructed manually in setup, giving a real repository without needing Spring Boot, while the CSV writer is mocked so only the database interaction is under test."*

*"System tests sit at the top and we tried Selenium WebDriver in headless Chrome to drive the full running application through a browser, logging in as a real user and asserting, which means they are entirely opaque box and depend on the app being live with seeded data before they run."*

...some words from Cai Penfold, our Project Manager.