package org.example;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.AdGroupsApi;
import org.openapitools.client.api.CampaignsApi;
import org.openapitools.client.api.TargetsApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthUtils {
    private static final String BASE_URL = "https://advertising-api.amazon.com";
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String GET_ACCESS_TOKEN_BODY = "{" +
            "\"grant_type\": \"authorization_code\"," +
            "\"code\": \"%s\"," +
            "\"redirect_uri\": \"https://www.example.com/login.php\"," +
            "\"client_id\": \"%s\"," +
            "\"client_secret\": \"%s\"" +
            "}";
    private static final String GET_REFRESH_TOKEN_BODY = "{" +
            "\"grant_type\": \"refresh_token\"," +
            "\"refresh_token\": \"%s\"," +
            "\"client_id\": \"%s\"," +
            "\"client_secret\": \"%s\"" +
            "}";

    public static final String CLIENT_ID_HEADER_NAME = "Amazon-Advertising-API-ClientId";
    public static final String CLIENT_SECRET_HEADER_NAME = "Amazon-Advertising-API-ClientSecret";
    public static final String PROFILE_ID_HEADER_NAME = "profile_id";
    public static final String ACCESS_TOKEN_HEADER_NAME = "access_token";
    public static final String REFRESH_TOKEN_HEADER_NAME = "refresh_token";

    public static CampaignsApi getCampaignsApi() {
        return new CampaignsApi(getApiClient());
    }

    public static AdGroupsApi getAdGroupsApi() {
        return new AdGroupsApi(getApiClient());
    }

    public static TargetsApi getTargetsApi() {
        return new TargetsApi(getApiClient());
    }

    public static ApiClient getApiClient() {
        log.info("Start building the ads api client");

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(BASE_URL);
        log.info("Completed building the ads api client");
        return apiClient;
    }

    public static Map<String, String> getAccessTokenMap(final String authCode,
                                                        final String clientId,
                                                        final String clientSecret)
            throws IOException, InterruptedException {
        final Map<String, String> tokenMap = new HashMap<>();
        // Create an HttpClient instance
        final HttpClient client = HttpClient.newHttpClient();

        final String requestBody = String.format(
                GET_ACCESS_TOKEN_BODY, authCode, clientId, clientSecret);

        // Create a HttpRequest
        System.out.println("Get access token request body: " + requestBody);
        final HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.amazon.com/auth/o2/token"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        // Send the request and get the response
        final HttpResponse<String> authResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Get auth token response status code: " + authResponse.statusCode()
                + " AND Response body: " + authResponse.body());

        final Map<String, Object> authResponseMap = (new JSONObject(authResponse.body())).toMap();

        if (authResponseMap.containsKey("error") && authResponseMap.get("error").equals("invalid_grant")) {
            throw new RuntimeException("Invalid grant, please get new grant manually: " + authResponseMap.get("error_description"));
        }

        final String authToken = "Bearer " + authResponseMap.get(ACCESS_TOKEN_HEADER_NAME);
        tokenMap.put(ACCESS_TOKEN_HEADER_NAME, authToken);
        System.out.println("Retrieved auth token: " + authToken);

        final String refreshToken = (String) authResponseMap.get(REFRESH_TOKEN_HEADER_NAME);
        tokenMap.put(REFRESH_TOKEN_HEADER_NAME, refreshToken);
        System.out.println("Retrieved refresh token: " + refreshToken);

        return tokenMap;
    }

    public static String getRefreshedToken(final String refreshToken,
                                           final String clientId,
                                           final String clientSecret) throws IOException, InterruptedException {
        // Create an HttpClient instance
        final HttpClient client = HttpClient.newHttpClient();

        final String requestBody = String.format(GET_REFRESH_TOKEN_BODY, refreshToken, clientId, clientSecret);

        // Create a HttpRequest
        System.out.println("Get refresh token request body: " + requestBody);
        final HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.amazon.com/auth/o2/token"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        // Send the request and get the response
        final HttpResponse<String> authResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Get refresh token response status code: " + authResponse.statusCode()
                + " AND Response body: " + authResponse.body());

        final Map<String, Object> authResponseMap = (new JSONObject(authResponse.body())).toMap();
        final String authToken = "Bearer " + authResponseMap.get("access_token");
        System.out.println("Retrieved refreshed auth token: " + authToken);

        return authToken;
    }

    public static String getProfileId(final String accessToken, final String clientId) throws IOException, InterruptedException {
        // Create an HttpClient instance
        final HttpClient client = HttpClient.newHttpClient();

        // Create a HttpRequest
        System.out.println("Get profile request with no body");
        HttpRequest profileRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://advertising-api.amazon.com/v2/profiles"))
                .GET()
                .header(AUTHORIZATION_HEADER_NAME, accessToken)
                .header(CLIENT_ID_HEADER_NAME, clientId)
                .build();

        // Send the request and get the response
        final HttpResponse<String> profilesResponse = client.send(profileRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Get profiles response status code: " + profilesResponse.statusCode()
                + " AND Response body: " + profilesResponse.body());

        final JSONObject profileResponseObject = (JSONObject) (new JSONArray(profilesResponse.body())).get(0);

        return String.valueOf(profileResponseObject.getLong("profileId"));
    }
}
