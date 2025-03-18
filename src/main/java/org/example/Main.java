package org.example;

import org.apache.commons.lang3.StringUtils;
import org.example.objectApi.AdGroupsApiService;
import org.example.objectApi.CampaignsApiService;
import org.example.objectApi.TargetsApiService;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CampaignMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.CampaignSuccessResponse;
import org.openapitools.client.model.State;
import org.openapitools.client.model.TargetMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.TargetSuccessResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static org.example.AuthUtils.CLIENT_ID_HEADER_NAME;
import static org.example.AuthUtils.CLIENT_SECRET_HEADER_NAME;
import static org.example.AuthUtils.PROFILE_ID_HEADER_NAME;
import static org.example.AuthUtils.REFRESH_TOKEN_HEADER_NAME;
import static org.example.AuthUtils.getAccessTokenMap;
import static org.example.AuthUtils.getAdGroupsApi;
import static org.example.AuthUtils.getCampaignsApi;
import static org.example.AuthUtils.getRefreshedToken;
import static org.example.AuthUtils.getTargetsApi;
import static org.example.objectUtils.SPAdGroupUtils.buildCreateAdGroupRequest;
import static org.example.objectUtils.SPCampaignUtils.buildDeleteCampaignsRequestContent;
import static org.example.objectUtils.SPCampaignUtils.buildTestCreateCampaignRequest;
import static org.example.objectUtils.SPCampaignUtils.buildTestQueryCampaignByIdRequest;
import static org.example.objectUtils.SPCampaignUtils.buildUpdateCampaignsRequestContext;
import static org.example.objectUtils.SPTargetUtils.buildCreateTargetRequestContent;
import static org.example.objectUtils.SPTargetUtils.buildDeleteTargetRequest;
import static org.example.objectUtils.SPTargetUtils.buildTestQueryTargetIdRequest;
import static org.example.objectUtils.SPTargetUtils.buildUpdateTargetRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    private static final CampaignsApiService campaignsApiService = new CampaignsApiService(getCampaignsApi(), AUTH_MAP);
    private static final AdGroupsApiService adGroupsApiService = new AdGroupsApiService(getAdGroupsApi(), AUTH_MAP);
    private static final TargetsApiService targetsApiService = new TargetsApiService(getTargetsApi(), AUTH_MAP);

    private static void testSPCampaignApiFunctionality() throws IOException, InterruptedException, ApiException {
        // Create Campaign with paused state
        final String campaignId = campaignsApiService.createCampaign(buildTestCreateCampaignRequest());

        // Query Campaign
        final CampaignSuccessResponse spListCreateResponseContent
                = campaignsApiService.listCampaign(buildTestQueryCampaignByIdRequest(campaignId));
        assertEquals(1, Objects.requireNonNull(spListCreateResponseContent.getCampaigns()).size());
        assertEquals(State.PAUSED, Objects.requireNonNull(spListCreateResponseContent.getCampaigns()).get(0).getState().getState());

        // Update Campaign with enabled state
        final CampaignMultiStatusResponseWithPartialErrors updateCampaignResponse = campaignsApiService.updateCampaign(buildUpdateCampaignsRequestContext(campaignId));
        assertNotNull(updateCampaignResponse.getSuccess());
        assertEquals(1, updateCampaignResponse.getSuccess().size());

        // Query Campaign
        final CampaignSuccessResponse spListUpdateResponseContent
                = campaignsApiService.listCampaign(buildTestQueryCampaignByIdRequest(campaignId));
        assertEquals(1, Objects.requireNonNull(spListUpdateResponseContent.getCampaigns()).size());
        assertEquals(State.ENABLED, Objects.requireNonNull(spListUpdateResponseContent.getCampaigns()).get(0).getState().getState());

        // Delete Campaign with archived state
        final CampaignMultiStatusResponseWithPartialErrors deleteCampaignResponse
                = campaignsApiService.deleteCampaign(buildDeleteCampaignsRequestContent(campaignId));
        assertNotNull(deleteCampaignResponse.getSuccess());
        assertEquals(1, deleteCampaignResponse.getSuccess().size());

        // Query Campaign
        final CampaignSuccessResponse spListDeleteResponseContent
                = campaignsApiService.listCampaign(buildTestQueryCampaignByIdRequest(campaignId));
        assertEquals(1, Objects.requireNonNull(spListDeleteResponseContent.getCampaigns()).size());
        assertEquals(State.ARCHIVED, Objects.requireNonNull(spListDeleteResponseContent.getCampaigns()).get(0).getState().getState());
    }

    private static void testSPTargetApiFunctionality() throws IOException, InterruptedException, ApiException {
        // Create Parent Campaign
        final String campaignId = campaignsApiService.createCampaign(buildTestCreateCampaignRequest());

        // Create Parent AdGroup using Parent Campaign
        final String adGroupId = adGroupsApiService.createAdGroup(buildCreateAdGroupRequest(campaignId));

        // Create Target with paused state
        final String targetId = targetsApiService.createTarget(buildCreateTargetRequestContent(campaignId, adGroupId));

        // Query Target
        final TargetSuccessResponse spListCreateResponseContent
                = targetsApiService.listTarget(buildTestQueryTargetIdRequest(targetId));
        assertEquals(1, Objects.requireNonNull(spListCreateResponseContent.getTargets()).size());
        assertEquals(State.PAUSED, Objects.requireNonNull(spListCreateResponseContent.getTargets()).get(0).getState().getState());

        // Update Target with enabled state
        final TargetMultiStatusResponseWithPartialErrors updateTargetResponse = targetsApiService.updateTarget(buildUpdateTargetRequest(targetId));
        assertNotNull(updateTargetResponse.getSuccess());
        assertEquals(1, updateTargetResponse.getSuccess().size());

        // Query Target
        final TargetSuccessResponse spListUpdateResponseContent
                = targetsApiService.listTarget(buildTestQueryTargetIdRequest(targetId));
        assertEquals(1, Objects.requireNonNull(spListUpdateResponseContent.getTargets()).size());
        assertEquals(State.ENABLED, Objects.requireNonNull(spListUpdateResponseContent.getTargets()).get(0).getState().getState());

        // Delete Target with archived state
        final TargetMultiStatusResponseWithPartialErrors deleteTargetResponse
                = targetsApiService.deleteTarget(buildDeleteTargetRequest(targetId));
        assertNotNull(deleteTargetResponse.getSuccess());
        assertEquals(1, deleteTargetResponse.getSuccess().size());

        // Query Target
        final TargetSuccessResponse spListDeleteResponseContent
                = targetsApiService.listTarget(buildTestQueryTargetIdRequest(targetId));
        assertEquals(1, Objects.requireNonNull(spListDeleteResponseContent.getTargets()).size());
        assertEquals(State.ARCHIVED, Objects.requireNonNull(spListDeleteResponseContent.getTargets()).get(0).getState().getState());

        // Delete Campaign with archived state
        final CampaignMultiStatusResponseWithPartialErrors deleteCampaignResponse
                = campaignsApiService.deleteCampaign(buildDeleteCampaignsRequestContent(campaignId));
        assertNotNull(deleteCampaignResponse.getSuccess());
        assertEquals(1, deleteCampaignResponse.getSuccess().size());
    }

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

        testSPCampaignApiFunctionality();

        testSPTargetApiFunctionality();
    }
}