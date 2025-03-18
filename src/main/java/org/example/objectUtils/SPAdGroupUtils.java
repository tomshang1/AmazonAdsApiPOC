package org.example.objectUtils;

import org.openapitools.client.model.AdGroupCreate;
import org.openapitools.client.model.AdProduct;
import org.openapitools.client.model.BidStrategy;
import org.openapitools.client.model.BudgetAllocation;
import org.openapitools.client.model.CreateAdGroupBid;
import org.openapitools.client.model.CreateAdGroupBidValue;
import org.openapitools.client.model.CreateAdGroupBudgetSettings;
import org.openapitools.client.model.CreateAdGroupRequest;
import org.openapitools.client.model.CreateOptimization;
import org.openapitools.client.model.CreatePacing;
import org.openapitools.client.model.CreateStates;
import org.openapitools.client.model.CreateTargetingSettings;
import org.openapitools.client.model.CreativeRotationType;
import org.openapitools.client.model.DeliveryProfile;
import org.openapitools.client.model.InventoryType;
import org.openapitools.client.model.Marketplace;
import org.openapitools.client.model.MarketplaceScope;
import org.openapitools.client.model.State;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;

import java.time.ZoneOffset;
import java.util.List;

import static org.example.CommonUtils.generateName;

public class SPAdGroupUtils {
    public static CreateAdGroupRequest buildCreateAdGroupRequest(final String campaignId) {
        final CreateAdGroupRequest createCampaignRequest = new CreateAdGroupRequest();
        createCampaignRequest.setAdGroups(List.of(buildAdGroupCreate(campaignId)));
        return createCampaignRequest;
    }

    private static AdGroupCreate buildAdGroupCreate(final String campaignId) {
        final CreateStates createStates = new CreateStates();
        createStates.setState(State.PAUSED);

        final AdGroupCreate adGroupCreate = new AdGroupCreate();
        adGroupCreate.setInventoryType(InventoryType.DISPLAY);
        adGroupCreate.setAdProduct(AdProduct.SPONSORED_PRODUCTS);
        adGroupCreate.setMarketplaces(List.of(Marketplace.US));
        adGroupCreate.setCampaignId(campaignId);
        adGroupCreate.setCreativeRotationType(CreativeRotationType.RANDOM);
        adGroupCreate.setFrequencies(List.of());
        adGroupCreate.setFees(List.of());
        adGroupCreate.setOptimization(buildCreateOptimization());
        adGroupCreate.setTargetingSettings(buildCreateTargetingSettings());
        adGroupCreate.setStartDateTime(OffsetDateTime
                .now(ZoneId.of(String.valueOf(ZoneOffset.UTC)))
                .truncatedTo(ChronoUnit.MINUTES)
                .plusMinutes(1000));
        adGroupCreate.setState(createStates);
        adGroupCreate.setName(generateName("UNIFIED_ADGroup"));
        adGroupCreate.setPacing(buildCreatePacing());
        adGroupCreate.setMarketplaceScope(MarketplaceScope.SINGLE_MARKETPLACE);
        adGroupCreate.setBid(buildCreateAdGroupBidValue());

        return adGroupCreate;
    }

    private static CreateOptimization buildCreateOptimization() {
        final CreateOptimization createOptimization = new CreateOptimization();
        createOptimization.setBidStrategy(BidStrategy.MANUAL);
        createOptimization.setBudgetSettings(createAdGroupBudgetSettings());
        return createOptimization;
    }

    private static CreateAdGroupBudgetSettings createAdGroupBudgetSettings() {
        final CreateAdGroupBudgetSettings createAdGroupBudgetSettings = new CreateAdGroupBudgetSettings();
        createAdGroupBudgetSettings.setBudgetAllocation(BudgetAllocation.AUTO);
        createAdGroupBudgetSettings.setBudgetAllocation(BudgetAllocation.AUTO);
        return createAdGroupBudgetSettings;
    }

    private static CreateTargetingSettings buildCreateTargetingSettings() {
        final CreateTargetingSettings createTargetingSettings = new CreateTargetingSettings();

        return createTargetingSettings;
    }

    private static CreatePacing buildCreatePacing() {
        final CreatePacing createPacing = new CreatePacing();
        createPacing.setCatchUpBoostPercentage(0);
        createPacing.setDeliveryProfile(DeliveryProfile.EVEN);
        return createPacing;
    }

    private static CreateAdGroupBidValue buildCreateAdGroupBidValue() {
        final CreateAdGroupBid createAdGroupBid = new CreateAdGroupBid();
        createAdGroupBid.setDefaultBid(100d);

        final CreateAdGroupBidValue createAdGroupBidValue = new CreateAdGroupBidValue();
        createAdGroupBidValue.setBid(createAdGroupBid);
        return createAdGroupBidValue;
    }
}
