package org.example.objectUtils;

import org.apache.commons.lang3.StringUtils;
import org.example.objectApi.CampaignsApiService;
import org.openapitools.client.model.AdProduct;
import org.openapitools.client.model.AudienceSegmentType;
import org.openapitools.client.model.Campaign;
import org.openapitools.client.model.CampaignMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.CampaignSuccessResponse;
import org.openapitools.client.model.CampaignUpdate;
import org.openapitools.client.model.ShopperCohortType;
import org.openapitools.client.model.UpdateAudienceSegment;
import org.openapitools.client.model.UpdateBidAdjustments;
import org.openapitools.client.model.UpdateBidSettings;
import org.openapitools.client.model.UpdateCampaignOptimizations;
import org.openapitools.client.model.UpdateCampaignRequest;
import org.openapitools.client.model.UpdateShopperCohortBidAdjustment;

import java.util.List;
import java.util.Objects;

import static org.example.AuthUtils.getCampaignsApi;
import static org.example.Main.AUTH_MAP;
import static org.example.objectUtils.SPCampaignUtils.buildTestQueryCampaignRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SPCampaignAudienceIdMitigationUtils {
    private static final CampaignsApiService campaignsApiService = new CampaignsApiService(getCampaignsApi(), AUTH_MAP);

    public static UpdateCampaignRequest buildUpdateCampaignsRequestContextAudienceId(final String campaignId) {
        final UpdateCampaignRequest requestContent = new UpdateCampaignRequest();
        requestContent.addCampaignsItem(buildCampaignUpdateAudienceId(campaignId));
        return requestContent;
    }

    public static UpdateCampaignRequest buildUpdateCampaignsRequestContextAudienceIds(final List<String> campaignIds) {
        final UpdateCampaignRequest requestContent = new UpdateCampaignRequest();
        campaignIds.forEach(campaignId -> requestContent.addCampaignsItem(buildCampaignUpdateAudienceId(campaignId)));
        return requestContent;
    }

    private static CampaignUpdate buildCampaignUpdateAudienceId(final String campaignId) {
        final CampaignUpdate campaignUpdate = new CampaignUpdate();
        campaignUpdate.setCampaignId(campaignId);
        campaignUpdate.setOptimizations(buildUpdateCampaignOptimizationsAudienceId());
        return campaignUpdate;
    }

    private static UpdateCampaignOptimizations buildUpdateCampaignOptimizationsAudienceId() {
        final UpdateAudienceSegment updateAudienceSegment = new UpdateAudienceSegment();
        updateAudienceSegment.setAudienceId("407353587171002826");
        updateAudienceSegment.setAudienceSegmentType(AudienceSegmentType.SPONSORED_ADS_AMC);

        final UpdateShopperCohortBidAdjustment updateShopperCohortBidAdjustment = new UpdateShopperCohortBidAdjustment();
        updateShopperCohortBidAdjustment.setPercentage(10);
        updateShopperCohortBidAdjustment.setShopperCohortType(ShopperCohortType.AUDIENCE_SEGMENT);
        updateShopperCohortBidAdjustment.setAudienceSegments(List.of(updateAudienceSegment));

        final UpdateBidAdjustments updateBidAdjustments = new UpdateBidAdjustments();
        updateBidAdjustments.addShopperCohortBidAdjustmentsItem(updateShopperCohortBidAdjustment);

        final UpdateBidSettings updateBidSettings = new UpdateBidSettings();
        updateBidSettings.bidAdjustments(updateBidAdjustments);

        final UpdateCampaignOptimizations updateCampaignOptimizations = new UpdateCampaignOptimizations();
        updateCampaignOptimizations.setBidSettings(updateBidSettings);

        return updateCampaignOptimizations;
    }

    private static boolean isNumericValue(final String s) {
        boolean isNumeric = true;
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            isNumeric = false;
        }
        return isNumeric;
    }

    private static List<String> getAffectedCampaignIds(final CampaignSuccessResponse campaignSuccessResponse) {
        return campaignSuccessResponse.getCampaigns().stream()
                .filter(campaign -> Objects.nonNull(campaign.getOptimizations().getBidSettings().getBidAdjustments().getShopperCohortBidAdjustments()))
                .filter(campaign -> Objects.nonNull(campaign.getOptimizations().getBidSettings().getBidAdjustments().getShopperCohortBidAdjustments().get(0).getAudienceSegments()))
                .filter(campaign -> StringUtils.isNotEmpty(campaign.getOptimizations().getBidSettings().getBidAdjustments().getShopperCohortBidAdjustments().get(0).getAudienceSegments().get(0).getAudienceId()))
                //.filter(campaign -> !isNumericValue(campaign.getOptimizations().getBidSettings().getBidAdjustments().getShopperCohortBidAdjustments().get(0).getAudienceSegments().get(0).getAudienceId()))
                .map(Campaign::getCampaignId)
                .toList();
    }

    public static void main(String[] args) throws Exception {
        // Query Campaign
        final CampaignSuccessResponse spListUpdateResponseContent
                = campaignsApiService.listCampaign(buildTestQueryCampaignRequest(AdProduct.SPONSORED_PRODUCTS));

        final List<String> affectedCampaignIds = getAffectedCampaignIds(spListUpdateResponseContent);

        System.out.println("CampaignId List size: " + affectedCampaignIds.size() + "CAMPAIGNID's: " + affectedCampaignIds);

        // Update Campaign with enabled state
        final CampaignMultiStatusResponseWithPartialErrors updateCampaignResponse = campaignsApiService.updateCampaign(buildUpdateCampaignsRequestContextAudienceId(affectedCampaignIds.get(0)));
        assertNotNull(updateCampaignResponse.getSuccess());
        assertEquals(1, updateCampaignResponse.getSuccess().size());

        System.out.println("Update response: " + updateCampaignResponse);

//        // Update Campaigns with enabled state
//        final CampaignMultiStatusResponseWithPartialErrors updateCampaignResponse = campaignsApiService.updateCampaign(buildUpdateCampaignsRequestContextAudienceIds(affectedCampaignIds));
//        System.out.println("Update response: " + updateCampaignResponse);
//        assertNotNull(updateCampaignResponse.getSuccess());
//        assertEquals(16, updateCampaignResponse.getSuccess().size());
    }
}
