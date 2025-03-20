package org.example.ApiFunctionalityTesting;

import org.example.objectApi.AdGroupsApiService;
import org.example.objectApi.CampaignsApiService;
import org.example.objectApi.TargetsApiService;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.AdProduct;
import org.openapitools.client.model.CampaignMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.CampaignSuccessResponse;
import org.openapitools.client.model.State;
import org.openapitools.client.model.TargetMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.TargetSuccessResponse;

import java.io.IOException;
import java.util.Objects;

import static org.example.ApiFunctionalityTesting.objectUtils.AdGroupUtils.buildCreateAdGroupRequest;
import static org.example.ApiFunctionalityTesting.objectUtils.CampaignUtils.buildDeleteCampaignsRequestContent;
import static org.example.ApiFunctionalityTesting.objectUtils.CampaignUtils.buildTestCreateCampaignRequest;
import static org.example.ApiFunctionalityTesting.objectUtils.CampaignUtils.buildTestQueryCampaignByIdRequest;
import static org.example.ApiFunctionalityTesting.objectUtils.CampaignUtils.buildUpdateCampaignsRequestContext;
import static org.example.ApiFunctionalityTesting.objectUtils.TargetUtils.buildCreateTargetRequestContent;
import static org.example.ApiFunctionalityTesting.objectUtils.TargetUtils.buildDeleteTargetRequest;
import static org.example.ApiFunctionalityTesting.objectUtils.TargetUtils.buildTestQueryTargetIdRequest;
import static org.example.ApiFunctionalityTesting.objectUtils.TargetUtils.buildUpdateTargetRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestApiFunctionality {
    public static void testCampaignApiFunctionality(final CampaignsApiService CampaignsApiService,
                                                    final AdProduct adProduct)
            throws IOException, InterruptedException, ApiException {
        // Create Campaign with paused state
        final String campaignId = CampaignsApiService.createCampaign(buildTestCreateCampaignRequest(adProduct));

        // Query Campaign
        final CampaignSuccessResponse spListCreateResponseContent
                = CampaignsApiService.listCampaign(buildTestQueryCampaignByIdRequest(campaignId));
        assertEquals(1, Objects.requireNonNull(spListCreateResponseContent.getCampaigns()).size());
        assertEquals(State.PAUSED, Objects.requireNonNull(spListCreateResponseContent.getCampaigns()).get(0).getState().getState());

        // Update Campaign with enabled state
        final CampaignMultiStatusResponseWithPartialErrors updateCampaignResponse = CampaignsApiService.updateCampaign(buildUpdateCampaignsRequestContext(campaignId));
        assertNotNull(updateCampaignResponse.getSuccess());
        assertEquals(1, updateCampaignResponse.getSuccess().size());

        // Query Campaign
        final CampaignSuccessResponse spListUpdateResponseContent
                = CampaignsApiService.listCampaign(buildTestQueryCampaignByIdRequest(campaignId));
        assertEquals(1, Objects.requireNonNull(spListUpdateResponseContent.getCampaigns()).size());
        assertEquals(State.ENABLED, Objects.requireNonNull(spListUpdateResponseContent.getCampaigns()).get(0).getState().getState());

        // Delete Campaign with archived state
        final CampaignMultiStatusResponseWithPartialErrors deleteCampaignResponse
                = CampaignsApiService.deleteCampaign(buildDeleteCampaignsRequestContent(campaignId));
        assertNotNull(deleteCampaignResponse.getSuccess());
        assertEquals(1, deleteCampaignResponse.getSuccess().size());

        // Query Campaign
        final CampaignSuccessResponse spListDeleteResponseContent
                = CampaignsApiService.listCampaign(buildTestQueryCampaignByIdRequest(campaignId));
        assertEquals(1, Objects.requireNonNull(spListDeleteResponseContent.getCampaigns()).size());
        assertEquals(State.ARCHIVED, Objects.requireNonNull(spListDeleteResponseContent.getCampaigns()).get(0).getState().getState());
    }

    public static void testTargetApiFunctionality(
            final CampaignsApiService CampaignsApiService,
            final AdGroupsApiService AdGroupsApiService,
            final TargetsApiService TargetsApiService,
            final AdProduct adProduct) throws IOException, InterruptedException, ApiException {
        // Create Parent Campaign
        final String campaignId = CampaignsApiService.createCampaign(buildTestCreateCampaignRequest(adProduct));

        // Create Parent AdGroup using Parent Campaign
        final String adGroupId = AdGroupsApiService.createAdGroup(buildCreateAdGroupRequest(campaignId, adProduct));

        // Create Target with paused state
        final String targetId = TargetsApiService.createTarget(buildCreateTargetRequestContent(campaignId, adGroupId, adProduct));

        // Query Target
        final TargetSuccessResponse spListCreateResponseContent
                = TargetsApiService.listTarget(buildTestQueryTargetIdRequest(targetId));
        assertEquals(1, Objects.requireNonNull(spListCreateResponseContent.getTargets()).size());
        assertEquals(State.PAUSED, Objects.requireNonNull(spListCreateResponseContent.getTargets()).get(0).getState().getState());

        // Update Target with enabled state
        final TargetMultiStatusResponseWithPartialErrors updateTargetResponse = TargetsApiService.updateTarget(buildUpdateTargetRequest(targetId));
        assertNotNull(updateTargetResponse.getSuccess());
        assertEquals(1, updateTargetResponse.getSuccess().size());

        // Query Target
        final TargetSuccessResponse spListUpdateResponseContent
                = TargetsApiService.listTarget(buildTestQueryTargetIdRequest(targetId));
        assertEquals(1, Objects.requireNonNull(spListUpdateResponseContent.getTargets()).size());
        assertEquals(State.ENABLED, Objects.requireNonNull(spListUpdateResponseContent.getTargets()).get(0).getState().getState());

        // Delete Target with archived state
        final TargetMultiStatusResponseWithPartialErrors deleteTargetResponse
                = TargetsApiService.deleteTarget(buildDeleteTargetRequest(targetId));
        assertNotNull(deleteTargetResponse.getSuccess());
        assertEquals(1, deleteTargetResponse.getSuccess().size());

        // Query Target
        final TargetSuccessResponse spListDeleteResponseContent
                = TargetsApiService.listTarget(buildTestQueryTargetIdRequest(targetId));
        assertEquals(1, Objects.requireNonNull(spListDeleteResponseContent.getTargets()).size());
        assertEquals(State.ARCHIVED, Objects.requireNonNull(spListDeleteResponseContent.getTargets()).get(0).getState().getState());

        // Delete Campaign with archived state
        final CampaignMultiStatusResponseWithPartialErrors deleteCampaignResponse
                = CampaignsApiService.deleteCampaign(buildDeleteCampaignsRequestContent(campaignId));
        assertNotNull(deleteCampaignResponse.getSuccess());
        assertEquals(1, deleteCampaignResponse.getSuccess().size());
    }
}
