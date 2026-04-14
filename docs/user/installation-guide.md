# 💾 Installation Guide

---

### Navigation

- [🏠 Main Readme](../../README.md)
- [👋 Introduction](introduction.md)
- **[💾 Installation Guide](installation-guide.md)**
- [✨ Features and Usage](features-usage.md)
- [❓ FAQ](faq.md)
- [🔧 Troubleshooting](troubleshooting.md)

---

### System Requirements

Before deploying Filmbase onto your host computer, ensure the system has an installation of Java 21.

The application file is less than 200 MB, so storage should not be an issue.

### 1. Download the latest release

From Filmbase's GitHub project repository, navigate to the *Releases* tab on the right-hand side of the page (or click on [this link](https://github.com/abbas6115/csci2040-final-project/releases)). Find the release that is marked with the *Latest* label.

Under *Assets*, find the file that ends in `.jar` (it's name will begin with `csci2040-final-project`) and click on it to download the JAR file. This is your runnable application.

### 2. Run the application

Keep in mind that double-clicking the file you downloaded will **not** start the app. Instead, you must use Java to run the file inside a terminal window.

Open a terminal of your choice and navigate to the folder location in which you have saved the app. Then, run the following command:

```
java -jar csci2040-final-project-<version>.jar
```

This will run the application and begin hosting the website. It will take a couple seconds to start up.

### Accessing the webapp

Once the application is fully up and running, you may access it from your browser. Connect to the URL `https://localhost:8080`. To view the logged-in pages, you can use the following logins:

- Admin:
  - Admin1
  - password
- User:
  - User1
  - password

### Stopping the app

To stop the application and close the webserver, simply go to the terminal where Filmbase is running and press `Ctrl + C`.