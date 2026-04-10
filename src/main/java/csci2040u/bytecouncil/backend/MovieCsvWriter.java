package csci2040u.bytecouncil.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
// Handles CSV persistence and retrieval for movie data used by catalog/admin views
public class MovieCsvWriter {
    private static final String HEADER = "id,name,posterURL,actors,genre,ratings,releaseYear";

    private final Path csvPath;

    public MovieCsvWriter(@Value("${movie.csv.path:movies.csv}") String csvPath) {
        this.csvPath = Path.of(csvPath);
    }

    public void appendMovie(Movie movie) {
        try {
            Path parent = csvPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            // Ensure the CSV starts with a header when the file is first created
            if (Files.notExists(csvPath)) {
                Files.writeString(
                        csvPath,
                        HEADER + System.lineSeparator(),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                );
            }

            Files.writeString(
                    csvPath,
                    toCsvRow(movie) + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write movie to CSV file", exception);
        }
    }

    public void overwriteMovies(Iterable<Movie> movies) {
        try {
            Path parent = csvPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            StringBuilder content = new StringBuilder();
            content.append(HEADER).append(System.lineSeparator());
            for (Movie movie : movies) {
                content.append(toCsvRow(movie)).append(System.lineSeparator());
            }

            Files.writeString(
                    csvPath,
                    content.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to overwrite movies in CSV file", exception);
        }
    }

    public List<Movie> readMovies() {
        // If no CSV exists yet, return an empty list so the UI can render safely
        if (Files.notExists(csvPath)) {
            return Collections.emptyList();
        }

        try {
            List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            List<Movie> movies = new ArrayList<>();

            for (String line : lines) {
                // Skip blank lines and the header row.
                if (line == null || line.isBlank() || line.startsWith("id,")) {
                    continue;
                }

                List<String> values = parseCsvLine(line);
                Movie movie = new Movie();
                movie.setId(parseId(getValue(values, 0)));
                movie.setName(defaultIfMissing(getValue(values, 1)));
                movie.setPosterURL(defaultIfMissing(getValue(values, 2)));
                movie.setActors(defaultIfMissing(getValue(values, 3)));
                movie.setGenre(defaultIfMissing(getValue(values, 4)));
                movie.setRatings(defaultIfMissing(getValue(values, 5)));
                movie.setReleaseYear(parseReleaseYear(getValue(values, 6)));
                movie.setTmdbID(defaultIfMissing(getValue(values, 7)));

                //allows for the repository to set its own id and

                movie.setId(null);
                movies.add(movie);
            }

            return movies;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read movies from CSV file", exception);
        }
    }

    // Helper method to convert a Movie object to a CSV row string with proper escaping
    private String toCsvRow(Movie movie) {
        return String.join(
                ",",
                escape(movie.getId() == null ? "" : movie.getId().toString()),
                escape(movie.getName()),
                escape(movie.getPosterURL()),
                escape(movie.getActors()),
                escape(movie.getGenre()),
                escape(movie.getRatings()),
                escape(Integer.toString(movie.getReleaseYear())),
                escape(movie.getTmdbID())
        );
    }

    // Helper method to escape special characters in CSV values (e.g., commas, quotes)
    private String escape(String value) {
        String sanitizedValue = value == null ? "" : value;
        String escapedValue = sanitizedValue.replace("\"", "\"\"");
        return '"' + escapedValue + '"';
    }

    // Helper method to parse a CSV line into individual values, handling quoted fields with commas
    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);

            if (character == '"') {
                // Support escaped quotes represented as doubled double-quotes
                if (inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    currentValue.append('"');
                    index++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }

            if (character == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue.setLength(0);
                continue;
            }

            currentValue.append(character);
        }

        values.add(currentValue.toString());
        return values;
    }

    private String getValue(List<String> values, int index) {
        return index < values.size() ? values.get(index) : "";
    }

    private Long parseId(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Long.valueOf(value.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private int parseReleaseYear(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private String defaultIfMissing(String value) {
        // Missing CSV fields are surfaced as N/A in UI cards
        return value == null || value.isBlank() ? "N/A" : value;
    }

    // bulk import movies from another .csv file, assuming the same header structure
    public void importMoviesBulk(File csvFile) {
//        MovieDatabaseCommands mdc = new MovieDatabaseCommands();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            reader.readLine(); // skip the headers
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> parsedLine = parseCsvLine(line);
                Movie movie = new Movie();
                movie.setId(parseId(getValue(parsedLine, 0)));
                movie.setName(defaultIfMissing(getValue(parsedLine, 1)));
                movie.setPosterURL(defaultIfMissing(getValue(parsedLine, 2)));
                movie.setActors(defaultIfMissing(getValue(parsedLine, 3)));
                movie.setGenre(defaultIfMissing(getValue(parsedLine, 4)));
                movie.setRatings(defaultIfMissing(getValue(parsedLine, 5)));
                movie.setReleaseYear(parseReleaseYear(getValue(parsedLine, 6)));
                movie.setTmdbID(defaultIfMissing(getValue(parsedLine, 7)));

                appendMovie(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}