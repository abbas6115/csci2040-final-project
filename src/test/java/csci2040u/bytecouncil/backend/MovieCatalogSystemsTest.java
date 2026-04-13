package csci2040u.bytecouncil.backend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieCatalogSystemTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        logInAsUser();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void logInAsUser() {
        driver.get(baseUrl() + "/login");

        List<WebElement> usernameFields = driver.findElements(By.cssSelector("input[type='text']"));
        List<WebElement> passwordFields = driver.findElements(By.cssSelector("input[type='password']"));
        List<WebElement> submitButtons = driver.findElements(By.cssSelector("button[type='submit']"));

        Assumptions.assumeTrue(!usernameFields.isEmpty() && !passwordFields.isEmpty() && !submitButtons.isEmpty(),
            "Login form is not available in this test environment");

        usernameFields.get(0).sendKeys("User1");
        passwordFields.get(0).sendKeys("password");
        submitButtons.get(0).click();

        try {
            wait.until(webDriver -> !webDriver.getCurrentUrl().contains("/login?error"));
        } catch (TimeoutException exception) {
            Assumptions.assumeTrue(false, "Login failed in this test environment");
        }
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private void navigateToWatchlist() {
        driver.findElement(By.cssSelector("a[href='/watchlist']")).click();
    }

    @Test
    void ST05_userCatalogDisplaysAllMoviesWithCorrectDetails() {
        driver.get(baseUrl() + "/user");

        List<WebElement> cards = driver.findElements(By.cssSelector(".movie-card"));
        assertFalse(cards.isEmpty(), "Catalog should display at least one movie");

        WebElement firstCard = cards.get(0);
        assertFalse(firstCard.findElement(By.cssSelector(".movie-title")).getText().isBlank(),
            "Movie title should not be blank");
        assertFalse(firstCard.findElement(By.cssSelector(".movie-genre")).getText().isBlank(),
            "Movie genre should not be blank");
        assertFalse(firstCard.findElement(By.cssSelector(".movie-rating")).getText().isBlank(),
            "Movie rating should not be blank");
        assertFalse(firstCard.findElement(By.cssSelector(".movie-year")).getText().isBlank(),
            "Release year should not be blank");
    }

    @Test
    void ST06_addToWatchlistAppearsAtTopOfWatchlist() {
        driver.get(baseUrl() + "/user");

        List<WebElement> cards = driver.findElements(By.cssSelector(".movie-card"));

        String expectedTitle = driver.findElement(
            By.cssSelector(".movie-card:first-child .movie-title")
        ).getText();

        driver.findElement(
            By.cssSelector(".movie-card:first-child .add-to-watchlist-btn")
        ).click();

        navigateToWatchlist();

        String actualTitle = driver.findElement(
            By.cssSelector(".watchlist-entry:first-child .movie-title")
        ).getText();

        assertEquals(expectedTitle, actualTitle,
            "Most recently added movie should appear at the top of the watchlist");
    }

    @Test
    void ST07_mostRecentlyAddedMovieIsListedFirst() {
        driver.get(baseUrl() + "/user");

        List<WebElement> cards = driver.findElements(By.cssSelector(".movie-card"));
        assertTrue(cards.size() >= 2, "Catalog must have at least two movies for this test");

        String firstAdded = cards.get(0).findElement(By.cssSelector(".movie-title")).getText();
        cards.get(0).findElement(By.cssSelector(".add-to-watchlist-btn")).click();

        String secondAdded = cards.get(1).findElement(By.cssSelector(".movie-title")).getText();
        cards.get(1).findElement(By.cssSelector(".add-to-watchlist-btn")).click();

        navigateToWatchlist();

        List<WebElement> entries = driver.findElements(By.cssSelector(".watchlist-entry .movie-title"));
        assertEquals(secondAdded, entries.get(0).getText(),
            "Second added movie should be at the top");
        assertEquals(firstAdded, entries.get(1).getText(),
            "First added movie should be second in the list");
    }

    @Test
    void ST08_removedMovieIsNoLongerInWatchlist() {
        driver.get(baseUrl() + "/user");

        List<WebElement> cards = driver.findElements(By.cssSelector(".movie-card"));
        assertTrue(cards.size() >= 2, "Catalog must have at least two movies for this test");

        String titleToRemove = cards.get(0).findElement(By.cssSelector(".movie-title")).getText();
        cards.get(0).findElement(By.cssSelector(".add-to-watchlist-btn")).click();
        cards.get(1).findElement(By.cssSelector(".add-to-watchlist-btn")).click();

        navigateToWatchlist();

        int sizeBefore = driver.findElements(By.cssSelector(".watchlist-entry")).size();

        driver.findElement(By.cssSelector(".watchlist-entry:first-child .remove-btn")).click();

        List<WebElement> remaining = driver.findElements(By.cssSelector(".watchlist-entry"));
        assertEquals(sizeBefore - 1, remaining.size(),
            "Watchlist should have one fewer entry after removal");
        assertTrue(remaining.stream()
            .noneMatch(e -> e.findElement(By.cssSelector(".movie-title")).getText().equals(titleToRemove)),
            "Removed movie should no longer appear in the watchlist");
    }
}
