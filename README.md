# Filmbase

Filmbase is a web application for browsing and viewing a catalog of countless movies and TV shows.

---

## Developer Documentation

### Prerequisites

Before you begin, ensure you have the following:

- Java JDK 21
- Maven (if not using IntelliJ IDEA as your IDE)
- IntelliJ IDEA (recommended IDE for easy building)
- Git

### 1. Clone the repository

Visit our project's [GitHub repository](https://github.com/abbas6115/csci2040-final-project/) and clone it to your machine.

### 2. Open the project in your IDE

Since this project was developed in IntelliJ IDEA, we will use it to demonstrate. Open the cloned project in IntelliJ and give it a few seconds to build the Maven project. IntelliJ should automatically do this, but if not, right click on the `pom.xml` and select *Add as Maven Project*.

To run the webapp while in the project, run the Java class `MovieCatalogApplication`. This will begin hosting a webserver on `localhost:8080`, which you can connect to in your browser.

### 3. Build the project

You can build the project using Maven.

**Using IntelliJ:**

- Open the *Maven* tab on the right-hand side
- Navigate to *Lifecycle*
- Double-click (run):
    - *clean*
    - *package* ... in that order

**Using terminal:**

- Open a terminal window in the project's root directory
- Run the command `mvn clean install`

Using either of these methods should compile and build the project, which will take up to a minute. The packaged application will appear in the `target` directory as a *JAR* file.

### 4. Run the application

Now that you have the built application, you can run it outside of the IDE. **Note: double-clicking the executable JAR file will not run the program, as there is no UI for the server. You must run it in a terminal window**

Open a terminal and navigate to the `target` directory, or wherever your JAR file is situated. Run the command `java -jar <filename>.jar` (<filename>.jar will most likely be similar to `csci2040-final-project-1.0.jar`). Give it a moment to start up the server.

### 5. Access the webapp

In your browser, connect to `localhost:8080` to open the Filmbase webapp. You will be prompted with a login screen and can use either of the two developer logins:

- Admin:
    - Admin1
    - password
- User:
    - User1
    - password

---

## User Documentation

### Prerequisites

Before you begin, ensure you have Java 21 installed on your machine

### 1. Download the latest release

Visit our project's [GitHub repository](https://github.com/abbas6115/csci2040-final-project/) and navigate to the *Releases* page on the right-hand side. Click on the release marked *Latest*. You will see a page with three different downloadable files; click on the `.jar` file to download it (the other files are not required to download). Save it locally in a directory of your choice.

### 2. Run the application

As previously mentioned, double-clicking the executable will not run the program. Open a terminal window in the directory where your JAR file is saved and run `java -jar <filename>.jar`. Give it a moment to start up.

### Access the webapp

Once the application is fully up and running, you may access it from your browser. Connect to the URL `localhost:8080`, again, the page will prompt you with the login.

---

# The Byte Council

- Project Manager: Cai Penfold
- Technical Manager: Abbas Syed
- Front-End Lead: Shan Jeofry
- Back-End Lead: Muhammad Nabeel Khan
- Software Quality Lead: Ethan Jallim