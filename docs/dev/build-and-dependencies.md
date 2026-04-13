# Build and Dependency Management

---

### Navigation

- [🏠 Main Readme](../../README.md)
- [📌 Project Overview](overview.md)
- [🚀 Getting Started](getting-started.md)
- [🧱 Project Structure](structure.md)
- **[🛠️ Build and Dependency Management](build-and-dependencies.md)**
- [▶️ Running the Application](running.md)
- [📘 Code Standards and Conventions](code-standards.md)
- [🧪 Testing](testing.md)
- [🔌 API Information](api.md)

---

Filmbase was developed as a Maven project for ease of building/packaging along with utilizing dependencies and plugins.

The configuration file for Maven is `pom.xml`. In it, you can view the project's dependencies and their versions. If adding a dependency of your own, ensure it is on the proper version compatible with **Java 21** (this may not always be the latest version of the dependency itself).

**Using IntelliJ IDEA**: Maven commands are situated on the right-hand bar labeled with the small italic "m" logo.

**Using an IDE without Maven implementation:** A terminal CLI running within the project's root directory will be needed to run Maven commands.

#### Build Lifecycle

- Clean/blank state

The initial state from when you first clone the project, or can be achieved by running the *clean* command under *Lifecycle* or running `mvn clean` in the terminal. It's recommended to run this every time just before packaging/re-packaging the project.

- "Run" state

After the project has been compiled and run in-IDE at least once. Some `target/` files will be generated but no packaged, standalone executable will exist.

- Packaged state

To package the project, run the `package` command under `Lifecycle` in the Maven tab, or run `mvn package` in your terminal. This can take up to a minute.

If for whatever reason the basic package command fails, in the terminal, run the command `mvn package -Pproduction`.

#### Build Output

- Generated in `target/`
- Main artifact (executable) name: `csci2040-final-project-<version>.jar`