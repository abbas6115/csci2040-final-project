# 🔌 External API Integration (TMDB)

---

### Navigation

- [🏠 Main Readme](../../README.md)
- [📌 Project Overview](overview.md)
- [🚀 Getting Started](getting-started.md)
- [🧱 Project Structure](structure.md)
- [🛠️ Build and Dependency Management](build-and-dependencies.md)
- [▶️ Running the Application](running.md)
- [📘 Code Standards and Conventions](code-standards.md)
- [🧪 Testing](testing.md)
- **[🔌 External API Integration](api.md)**

---

Filmbase integrates with TMDB's (The Movie Database) API to retrieve additional movie data such as:

- Trailers
- Streaming availability
- Streaming service logos

All API request-related logic is encapsulated in the `TMDBRequest` class.

#### Location

```
csci2040-final-project/
├──src/
│   └──main/
│       └──java/
│           └──csci2040u.bytecouncil/
│               └──backend/
│                   └──TMDBRequest.java
```

#### API Key Configuration

The TMDB API key is injected using Spring Framework:

```
@Value("${api.key}")
private String apiKey;
```

The key is then stored in `resources/application.properties` as the value for `api.key`.

To use your own TMDB API key, create a `.env` file and store the key with the variable name `API_KEY`, for example:

```
# .env
API_KEY=<your api key here>
```