package org.example.ApiFunctionalityTesting.objectUtils;

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
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.example.CommonUtils.generateName;

public class AdGroupUtils {
    public static CreateAdGroupRequest buildCreateAdGroupRequest(final String campaignId, final AdProduct adProduct) {
        final CreateAdGroupRequest createCampaignRequest = new CreateAdGroupRequest();
        createCampaignRequest.setAdGroups(List.of(buildAdGroupCreate(campaignId, adProduct)));
        return createCampaignRequest;
    }

    private static AdGroupCreate buildAdGroupCreate(final String campaignId, final AdProduct adProduct) {
        final AdGroupCreate adGroupCreate = new AdGroupCreate();
        addAdGroupCreateCommonFields(adGroupCreate, campaignId);

        // TODO: Add AdGroup common fields addition
        if (Objects.equals(AdProduct.SPONSORED_PRODUCTS, adProduct)) {
            adGroupCreate.setInventoryType(InventoryType.DISPLAY);
            adGroupCreate.setAdProduct(adProduct);
            adGroupCreate.setCreativeRotationType(CreativeRotationType.RANDOM);
            adGroupCreate.setFrequencies(List.of());
            adGroupCreate.setFees(List.of());
            adGroupCreate.setOptimization(buildCreateOptimization());
            adGroupCreate.setTargetingSettings(buildCreateTargetingSettings());
            adGroupCreate.setPacing(buildCreatePacing());
            adGroupCreate.setBid(buildCreateAdGroupBidValue());
        } else if (Objects.equals(AdProduct.SPONSORED_BRANDS, adProduct)) {
            //  TODO: Add SB AdGroup Create implementation, for now, copying SP for more accurate LOC comparison
//            adGroupCreate.setInventoryType(InventoryType.DISPLAY);
//            adGroupCreate.setAdProduct(adProduct);
//            adGroupCreate.setCreativeRotationType(CreativeRotationType.RANDOM);
//            adGroupCreate.setFrequencies(List.of());
//            adGroupCreate.setFees(List.of());
//            adGroupCreate.setOptimization(buildCreateOptimization());
//            adGroupCreate.setTargetingSettings(buildCreateTargetingSettings());
//            adGroupCreate.setPacing(buildCreatePacing());
//            adGroupCreate.setBid(buildCreateAdGroupBidValue());
        } else if (Objects.equals(AdProduct.AMAZON_DSP, adProduct)) {
            //  TODO: Add ADSP AdGroup Create implementation, for now, copying SP for more accurate LOC comparison
//            adGroupCreate.setInventoryType(InventoryType.DISPLAY);
//            adGroupCreate.setAdProduct(adProduct);
//            adGroupCreate.setCreativeRotationType(CreativeRotationType.RANDOM);
//            adGroupCreate.setFrequencies(List.of());
//            adGroupCreate.setFees(List.of());
//            adGroupCreate.setOptimization(buildCreateOptimization());
//            adGroupCreate.setTargetingSettings(buildCreateTargetingSettings());
//            adGroupCreate.setPacing(buildCreatePacing());
//            adGroupCreate.setBid(buildCreateAdGroupBidValue());
        }

        return adGroupCreate;
    }

    private static void addAdGroupCreateCommonFields(final AdGroupCreate adGroupCreate, final String campaignId) {
        final CreateStates createStates = new CreateStates();
        createStates.setState(State.PAUSED);

        adGroupCreate.setMarketplaces(List.of(Marketplace.US));
        adGroupCreate.setCampaignId(campaignId);
        adGroupCreate.setStartDateTime(
                Date.from(Instant.now(Clock.system(ZoneId.of(String.valueOf(ZoneOffset.UTC))))
                        .truncatedTo(ChronoUnit.MINUTES)
                        .plusSeconds(100000)
                )
        );
        adGroupCreate.setState(createStates);
        adGroupCreate.setName(generateName("UNIFIED_ADGroup"));
        adGroupCreate.setMarketplaceScope(MarketplaceScope.SINGLE_MARKETPLACE);
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
