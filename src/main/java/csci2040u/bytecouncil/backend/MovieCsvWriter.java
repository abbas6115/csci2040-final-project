package csci2040u.bytecouncil.backend;

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

    public List<Movie> readMovies() {
        if (Files.notExists(csvPath)) {
            return Collections.emptyList();
        }

        try {
            List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            List<Movie> movies = new ArrayList<>();

            for (String line : lines) {
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

                movies.add(movie);
            }

            return movies;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read movies from CSV file", exception);
        }
    }

    private String toCsvRow(Movie movie) {
        return String.join(
                ",",
                escape(movie.getId() == null ? "" : movie.getId().toString()),
                escape(movie.getName()),
                escape(movie.getPosterURL()),
                escape(movie.getActors()),
                escape(movie.getGenre()),
                escape(movie.getRatings()),
                escape(Integer.toString(movie.getReleaseYear()))
        );
    }

    private String escape(String value) {
        String sanitizedValue = value == null ? "" : value;
        String escapedValue = sanitizedValue.replace("\"", "\"\"");
        return '"' + escapedValue + '"';
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);

            if (character == '"') {
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
        return value == null || value.isBlank() ? "N/A" : value;
    }
}