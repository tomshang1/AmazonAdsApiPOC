package org.example;

import org.apache.commons.lang3.StringUtils;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CampaignsApi;
import org.openapitools.client.model.CampaignSuccessResponse;
import org.openapitools.client.model.QueryCampaignRequest;

import java.util.Map;

import static org.example.AuthUtils.REFRESH_TOKEN_HEADER_NAME;
import static org.example.AuthUtils.getAccessTokenMap;
import static org.example.AuthUtils.getApiClient;
import static org.example.AuthUtils.getRefreshedToken;
import static org.example.UnifiedTutorialUtils.buildTestQueryCampaignRequest;

public class Main {
    // TO BE SET BY USER
    private final static String AUTH_CODE = null; // Change with new auth code to fetch profileId or refresh token
    private final static String PROFILE_ID_SCOPE = null; // Set this to fetched profileId! Don't need to refresh this
    public final static String REFRESH_TOKEN = null; // Set this with fetched refresh token! Don't need to refresh this
    private final static String CLIENT_ID = null; // Get from LWA account
    private final static String CLIENT_SECRET = null; // Get from LWA account
    // END

    private final static ApiClient apiClient = getApiClient(AUTH_CODE, CLIENT_ID, CLIENT_SECRET);
    private final static CampaignsApi campaignsApi = new CampaignsApi(apiClient);

    public static void main(String[] args) throws Exception {

        if (StringUtils.isBlank(REFRESH_TOKEN)) {
            final Map<String, String> tokenMap = getAccessTokenMap(AUTH_CODE, CLIENT_ID, CLIENT_SECRET);
            System.out.println("PLEASE ADD VALUE TO REFRESH_TOKEN and try again: " + tokenMap.get(REFRESH_TOKEN_HEADER_NAME));
            return; // if refreshToken is not set, fetch it, print it to console, and return (since auth code can only be used once)
        }

        if (StringUtils.isBlank(PROFILE_ID_SCOPE)) {
            final String profileId = AuthUtils.getProfileId(getRefreshedToken(REFRESH_TOKEN, CLIENT_ID, CLIENT_SECRET), CLIENT_ID);
            System.out.println("PLEASE ADD VALUE TO PROFILE_ID_SCOPE and try again: " + profileId);
            return; // if profileId is not set, fetch it, print it to console, and return
        }

        // Query Campaign sample request
        final QueryCampaignRequest queryCampaignRequest = buildTestQueryCampaignRequest();
        final CampaignSuccessResponse campaignSuccessResponse;
        try {
            campaignsApi.getApiClient().addDefaultHeader("Authorization", getRefreshedToken(REFRESH_TOKEN, CLIENT_ID, CLIENT_SECRET));
            campaignSuccessResponse = campaignsApi.queryCampaign(CLIENT_ID, PROFILE_ID_SCOPE, queryCampaignRequest);
        } catch (ApiException e) {
            // Handle exception
            System.out.println(e.getCode());
            System.out.println(e.getResponseHeaders());
            System.out.println(e.getResponseBody());
            throw e;
        }

        System.out.println(campaignSuccessResponse);
    }
}