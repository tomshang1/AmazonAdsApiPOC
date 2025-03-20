package org.example;

import org.apache.commons.lang3.StringUtils;
import org.example.objectApi.AdGroupsApiService;
import org.example.objectApi.CampaignsApiService;
import org.example.objectApi.TargetsApiService;
import org.openapitools.client.model.AdProduct;

import java.util.Map;

import static org.example.AuthUtils.CLIENT_ID_HEADER_NAME;
import static org.example.AuthUtils.CLIENT_SECRET_HEADER_NAME;
import static org.example.AuthUtils.PROFILE_ID_HEADER_NAME;
import static org.example.AuthUtils.REFRESH_TOKEN_HEADER_NAME;
import static org.example.AuthUtils.getAccessTokenMap;
import static org.example.AuthUtils.getAdGroupsApi;
import static org.example.AuthUtils.getCampaignsApi;
import static org.example.AuthUtils.getRefreshedToken;
import static org.example.AuthUtils.getTargetsApi;
import static org.example.ApiFunctionalityTesting.TestApiFunctionality.testCampaignApiFunctionality;
import static org.example.ApiFunctionalityTesting.TestApiFunctionality.testTargetApiFunctionality;

public class Main {
    // TO BE SET BY USER
    private final static String AUTH_CODE = null; // Change with new auth code to fetch profileId or refresh token
    private final static String PROFILE_ID_SCOPE = null; // Set this to fetched profileId! Don't need to refresh this
    public final static String REFRESH_TOKEN = null; // Set this with fetched refresh token! Don't need to refresh this
    private final static String CLIENT_ID = null; // Get from LWA account
    private final static String CLIENT_SECRET = null; // Get from LWA account
    // END


    public static final Map<String, String> AUTH_MAP = Map.of(
            CLIENT_ID_HEADER_NAME, CLIENT_ID,
            CLIENT_SECRET_HEADER_NAME, CLIENT_SECRET,
            PROFILE_ID_HEADER_NAME, PROFILE_ID_SCOPE,
            REFRESH_TOKEN_HEADER_NAME, REFRESH_TOKEN
    );

    private static final CampaignsApiService CAMPAIGNS_API_SERVICE = new CampaignsApiService(getCampaignsApi(), AUTH_MAP);
    private static final AdGroupsApiService AD_GROUPS_API_SERVICE = new AdGroupsApiService(getAdGroupsApi(), AUTH_MAP);
    private static final TargetsApiService TARGETS_API_SERVICE = new TargetsApiService(getTargetsApi(), AUTH_MAP);

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

        // test SP campaigns + targets API functionality
        testCampaignApiFunctionality(CAMPAIGNS_API_SERVICE, AdProduct.SPONSORED_PRODUCTS);
        testTargetApiFunctionality(CAMPAIGNS_API_SERVICE, AD_GROUPS_API_SERVICE, TARGETS_API_SERVICE, AdProduct.SPONSORED_PRODUCTS);

        // TODO: Add SB campaigns + targets API functionality once SB API has been implemented in Unified APIs
//        testCampaignApiFunctionality(CAMPAIGNS_API_SERVICE, AdProduct.SPONSORED_BRANDS);
//        testTargetApiFunctionality(CAMPAIGNS_API_SERVICE, AD_GROUPS_API_SERVICE, TARGETS_API_SERVICE, AdProduct.SPONSORED_BRANDS);
    }
}