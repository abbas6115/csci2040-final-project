package csci2040u.bytecouncil.backend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;

/*
Wrapper Class for requesting trailers, and streaming services
 */

public class TMBDRequest {
    private final HttpClient client;
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final String apiKey="fd51fcbdf9ac457fcea35d70bf437a1b";

    public TMBDRequest() {

        this.client = HttpClient.newHttpClient();
    }

    public CompletableFuture<String> getFirstTrailer(String movieId) {
        return getAllVideos(movieId).thenApply(videoList -> {
            if (!videoList.isEmpty()) {
                return videoList.get(0); // Return the first one built
            }
            return "No trailer found";
        });
    }

    public CompletableFuture<List<String>> getStreamingServices(String movieId, String region) {
        String url = BASE_URL + movieId + "/watch/providers?api_key=" + apiKey;
        return makeRequest(url).thenApply(json -> {
            List<String> providers = new ArrayList<>();
            try {
                if (json.has("results")) {
                    JSONObject allResults = json.getJSONObject("results");

                    if (allResults.has(region)) {
                        JSONObject regionData = allResults.getJSONObject(region);

                        // Check if 'flatrate' (streaming) exists for this region
                        if (regionData.has("flatrate")) {
                            JSONArray flatrate = regionData.getJSONArray("flatrate");
                            for (int i = 0; i < flatrate.length(); i++) {
                                providers.add(flatrate.getJSONObject(i).optString("provider_name"));
                            }
                        } else {
                            System.out.println("No subscription streaming found for " + region + " (may only be for rent/buy).");
                        }
                    } else {
                        System.out.println("Region " + region + " is not listed in the watch results.");
                    }
                }
            } catch (JSONException e) {
                System.err.println("Error parsing providers: " + e.getMessage());
            }
            return providers;
        });
    }

    public CompletableFuture<List<String>> getAllVideos(String movieId) {
        String url = BASE_URL + movieId + "/videos?api_key=" + apiKey;

        return makeRequest(url).thenApply(json -> {
            List<String> videoLinks = new ArrayList<>();
            try {
                if (json.has("results")) {
                    JSONArray results = json.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject video = results.getJSONObject(i);

                        // Only build links for YouTube videos
                        if ("YouTube".equalsIgnoreCase(video.optString("site"))) {
                            String key = video.optString("key");
                            videoLinks.add("https://youtube.com/watch?v=" + key);
                        }
                    }
                }
            } catch (JSONException e) {
                System.err.println("Error building video links: " + e.getMessage());
            }
            return videoLinks;
        });
    }

    private CompletableFuture<JSONObject> makeRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .handle((response, ex) -> {
                    if (ex != null) {
                        System.err.println("Connection Error: " + ex.getMessage());
                        return new JSONObject();
                    }
                    if (response.statusCode() != 200) {
                        System.err.println("API Error: Status " + response.statusCode() + " for URL: " + url);
                        return new JSONObject();
                    }
                    try {
                        return new JSONObject(response.body());
                    } catch (JSONException e) {
                        System.err.println("Malformed JSON: " + e.getMessage());
                        return new JSONObject();
                    }
                });
    }

    public CompletableFuture<List<String>> getStreamingLogos(String movieId, String region) {
        String url = BASE_URL + movieId + "/watch/providers?api_key=" + apiKey;
        String imageBase = "https://image.tmdb.org/t/p/original";

        return makeRequest(url).thenApply(json -> {
            List<String> logoUrls = new ArrayList<>();
            // Use a Set to track unique logo paths (the actual image files)
            java.util.Set<String> seenLogos = new java.util.HashSet<>();

            try {
                if (json.has("results")) {
                    JSONObject allResults = json.getJSONObject("results");
                    if (allResults.has(region)) {
                        JSONObject regionData = allResults.getJSONObject(region);

                        if (regionData.has("flatrate")) {
                            JSONArray flatrate = regionData.getJSONArray("flatrate");
                            for (int i = 0; i < flatrate.length(); i++) {
                                JSONObject provider = flatrate.getJSONObject(i);
                                String path = provider.optString("logo_path");

                                // If we haven't seen this specific LOGO before, add it.
                                // This merges "Paramount+" and "Paramount+ Essential"
                                // because they typically share the same logo file.
                                if (!path.isEmpty() && !seenLogos.contains(path)) {
                                    logoUrls.add(imageBase + path);
                                    seenLogos.add(path);
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                System.err.println("Logo parsing error: " + e.getMessage());
            }
            return logoUrls;
        });
    }

//    testing
//    public static void main(String[] args) {
//        TMBDRequest tmdb = new TMBDRequest();
//
//        // Test ID: Insurgent (262500) or Fight Club (550)
//        String movieId = "262500";
//        String region = "US";
//
//        System.out.println("--- TMDB DATA FETCH FOR ID: " + movieId + " ---");
//
//        // 1. Print the First Trailer Link
//        tmdb.getFirstTrailer(movieId).thenAccept(url -> {
//            System.out.println("Trailer: " + url);
//        }).join();
//
//        // 2. Print Streaming Service Names (e.g., [Netflix, Paramount Plus])
//        tmdb.getStreamingServices(movieId, region).thenAccept(services -> {
//            System.out.println("Available on: " + services);
//        }).join();
//
//        // 3. Print Streaming Logos (Direct image links)
//        // Note: This uses the logic I provided in the previous turn
//        tmdb.getStreamingLogos(movieId, region).thenAccept(logos -> {
//            System.out.println("Service Logos: " + logos);
//        }).join();
//
//        tmdb.getAllVideos(movieId).thenAccept(videoList -> {
//            if (videoList.isEmpty()) {
//                System.out.println("No videos found.");
//            } else {
//                System.out.println("Total videos found: " + videoList.size());
//                videoList.forEach(url -> System.out.println(" - " + url));
//            }
//        }).join();
//
//        System.out.println("--- FETCH COMPLETE ---");
//    }
}
