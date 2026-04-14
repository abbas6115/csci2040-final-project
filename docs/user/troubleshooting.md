# 🔧 Troubleshooting

---

### Navigation

- [🏠 Main Readme](../../README.md)
- [👋 Introduction](introduction.md)
- [💾 Installation Guide](installation-guide.md)
- [✨ Features and Usage](features-usage.md)
- [❓ FAQ](faq.md)
- **[🔧 Troubleshooting](troubleshooting.md)**

---

## Webserver failed to start

The most common fix to this issue is that the port that Filmbase is attempting to use (`8080`) is busy, i.e. in use by a different program or app.

Ensure the computer is not already running an instance of Filmbase in another window, and if it is, close it. Or, ensure that the computer is not hosting a different application on the same port, such as a different webserver.