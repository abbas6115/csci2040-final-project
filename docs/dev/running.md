# ▶️ Running the Application

---

### Navigation

- [🏠 Main Readme](../../README.md)
- [📌 Project Overview](overview.md)
- [🚀 Getting Started](getting-started.md)
- [🧱 Project Structure](structure.md)
- [🛠️ Build and Dependency Management](build-and-dependencies.md)
- **[▶️ Running the Application](running.md)**
- [📘 Code Standards and Conventions](code-standards.md)
- [🧪 Testing](testing.md)
- [🔌 External API Integration](api.md)

---

#### Running in IDE

As mentioned in [Getting Started](getting-started.md), the project can be run locally within the IDE by running the `MovieCatalogApplication` Java source file. This is the main class of the project.

#### Running the Packaged JAR

Because the server itself does not have a UI implemented, the JAR file cannot be executed via double-click.

Instead, you can execute the program via the terminal using the command `java -jar csci2040-final-project-<version>.jar` (ensure your terminal is in the same working directory as the executable).

#### Accessing the web-app

In your browser, go to the URL `http://localhost:8080` to access the website. You will be prompted with a login screen and can use either of the two developer logins:

- Admin:
    - Admin1
    - password
- User:
    - User1
    - password

#### Stopping the app

To stop the application and close the webserver, simply go to the terminal where Filmbase is running and press `Ctrl + C`.
