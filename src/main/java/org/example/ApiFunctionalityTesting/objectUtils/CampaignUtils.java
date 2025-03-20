package org.example.ApiFunctionalityTesting.objectUtils;

import org.openapitools.client.model.AdProduct;
import org.openapitools.client.model.BidStrategy;
import org.openapitools.client.model.BudgetType;
import org.openapitools.client.model.CampaignAdProductFilter;
import org.openapitools.client.model.CampaignCampaignIdFilter;
import org.openapitools.client.model.CampaignCreate;
import org.openapitools.client.model.CampaignStateFilter;
import org.openapitools.client.model.CampaignUpdate;
import org.openapitools.client.model.CostType;
import org.openapitools.client.model.CreateAutoCreationSettings;
import org.openapitools.client.model.CreateBidAdjustments;
import org.openapitools.client.model.CreateBidSettings;
import org.openapitools.client.model.CreateBudget;
import org.openapitools.client.model.CreateBudgetValue;
import org.openapitools.client.model.CreateCampaignOptimizations;
import org.openapitools.client.model.CreateCampaignRequest;
import org.openapitools.client.model.CreateGoalSettings;
import org.openapitools.client.model.CreateMonetaryBudget;
import org.openapitools.client.model.CreateMonetaryBudgetMarketplaceSetting;
import org.openapitools.client.model.CreateMonetaryBudgetValue;
import org.openapitools.client.model.CreatePlacementBidAdjustment;
import org.openapitools.client.model.CreateShopperCohortBidAdjustment;
import org.openapitools.client.model.CreateStates;
import org.openapitools.client.model.DeleteCampaignRequest;
import org.openapitools.client.model.Goal;
import org.openapitools.client.model.Marketplace;
import org.openapitools.client.model.MarketplaceScope;
import org.openapitools.client.model.Placement;
import org.openapitools.client.model.QueryCampaignRequest;
import org.openapitools.client.model.Recurrence;
import org.openapitools.client.model.ShopperCohortType;
import org.openapitools.client.model.State;
import org.openapitools.client.model.UpdateCampaignRequest;
import org.openapitools.client.model.UpdateStates;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.example.CommonUtils.generateName;

public class CampaignUtils {

    public static QueryCampaignRequest buildTestQueryCampaignRequest(final AdProduct adProduct) {
        final CampaignStateFilter stateFilter = new CampaignStateFilter();
        stateFilter.setInclude(List.of(State.ENABLED, State.PAUSED));

        final CampaignAdProductFilter adProductFilter = new CampaignAdProductFilter();
        adProductFilter.setInclude(List.of(adProduct));

        final QueryCampaignRequest queryCampaignRequest = new QueryCampaignRequest();
        queryCampaignRequest.setStateFilter(stateFilter);
        queryCampaignRequest.setMaxResults(5000);
        queryCampaignRequest.setAdProductFilter(adProductFilter);
        return queryCampaignRequest;
    }

    public static QueryCampaignRequest buildTestQueryCampaignByIdRequest(final String campaignId) {
        final CampaignCampaignIdFilter campaignIdFilter = new CampaignCampaignIdFilter();
        campaignIdFilter.setInclude(List.of(campaignId));

        final CampaignAdProductFilter adProductFilter = new CampaignAdProductFilter();
        adProductFilter.setInclude(List.of(AdProduct.SPONSORED_PRODUCTS));

        final QueryCampaignRequest queryCampaignRequest = new QueryCampaignRequest();
        queryCampaignRequest.setMaxResults(5000);
        queryCampaignRequest.setCampaignIdFilter(campaignIdFilter);
        queryCampaignRequest.setAdProductFilter(adProductFilter);
        return queryCampaignRequest;
    }

    public static CreateCampaignRequest buildTestCreateCampaignRequest(final AdProduct adProduct) {
        final CreateCampaignRequest createCampaignRequest = new CreateCampaignRequest();
        createCampaignRequest.setCampaigns(List.of(buildCampaignCreate(adProduct)));
        return createCampaignRequest;
    }

    private static CampaignCreate buildCampaignCreate(final AdProduct adProduct) {
        final CampaignCreate campaignCreate = new CampaignCreate();

        addCampaignCreateCommonFields(campaignCreate);

        if (Objects.equals(AdProduct.SPONSORED_PRODUCTS, adProduct)) {
            final CreateAutoCreationSettings createAutoCreationSettings = new CreateAutoCreationSettings();
            createAutoCreationSettings.setAutoCreateTargets(false);
            campaignCreate.setAutoCreationSettings(createAutoCreationSettings);
            campaignCreate.setAdProduct(adProduct);
            campaignCreate.setOptimizations(buildCreateCampaignOptimizationsSP());
            campaignCreate.setBudgets(List.of(buildCreateBudget()));
        } else if (Objects.equals(AdProduct.SPONSORED_BRANDS, adProduct)) {
            // TODO: Find mapping for Unified productLocation field
            // TODO: Find mapping for Unified smartDefault field
            campaignCreate.setBrandId("TEST_BRAND_ID"); // TODO: get valid brandId
            campaignCreate.setPortfolioId("TEST_PORTFOLIO_ID"); // TODO: get valid portfolioId
            campaignCreate.setOptimizations(buildCreateCampaignOptimizationsSB());
            campaignCreate.setCostType(CostType.CPC);
            campaignCreate.setAdProduct(adProduct);
            campaignCreate.setBudgets(List.of(buildCreateBudget())); // Not sure if SB different
            // TODO: Check if below fields are required for SB Campaigns
            final CreateAutoCreationSettings createAutoCreationSettings = new CreateAutoCreationSettings();
            createAutoCreationSettings.setAutoCreateTargets(false);
            campaignCreate.setAutoCreationSettings(createAutoCreationSettings);
        } else if (Objects.equals(AdProduct.AMAZON_DSP, adProduct)) {
            // TODO: Ad ADSP specific Campaign create fields, for now, copying SB for more accurate LOC comparison
//            // TODO: Find mapping for Unified productLocation field
//            // TODO: Find mapping for Unified smartDefault field
//            campaignCreate.setBrandId("TEST_BRAND_ID"); // TODO: get valid brandId
//            campaignCreate.setPortfolioId("TEST_PORTFOLIO_ID"); // TODO: get valid portfolioId
//            campaignCreate.setOptimizations(buildCreateCampaignOptimizationsSB());
//            campaignCreate.setCostType(CostType.CPC);
//            campaignCreate.setAdProduct(adProduct);
//            campaignCreate.setBudgets(List.of(buildCreateBudget())); // Not sure if SB different
//            // TODO: Check if below fields are required for SB Campaigns
//            final CreateAutoCreationSettings createAutoCreationSettings = new CreateAutoCreationSettings();
//            createAutoCreationSettings.setAutoCreateTargets(false);
//            campaignCreate.setAutoCreationSettings(createAutoCreationSettings);
        }

        return campaignCreate;
    }

    private static void addCampaignCreateCommonFields(final CampaignCreate campaignCreate) {
        final CreateStates createStates = new CreateStates();
        createStates.setState(State.PAUSED);

        campaignCreate.setState(createStates);
        campaignCreate.setTags(List.of());
        campaignCreate.setName(generateName("UNIFIED_CAMPAIGN"));
        campaignCreate.setMarketplaces(List.of(Marketplace.US));
        campaignCreate.setMarketplaceScope(MarketplaceScope.SINGLE_MARKETPLACE);
        campaignCreate.setStartDateTime(
                Date.from(Instant.now(Clock.system(ZoneId.of(String.valueOf(ZoneOffset.UTC))))
                        .truncatedTo(ChronoUnit.MINUTES)
                        .plusSeconds(100000)
                )
        );
    }

    private static CreateCampaignOptimizations  buildCreateCampaignOptimizationsSB() {
        final CreateBidAdjustments createBidAdjustments = new CreateBidAdjustments();
        createBidAdjustments.setPlacementBidAdjustments(buildCreatePlacementBidAdjustment());
        // caused an issue with audienceId
        // ADDING SHOPPER COHORT FEATURE
        // Commenting for now as it may cause issues in beta
//        createBidAdjustments.setShopperCohortBidAdjustments(buildShopperCohortBidAdjustments());

        final CreateBidSettings bidSettings = new CreateBidSettings();
        bidSettings.bidAdjustments(createBidAdjustments);
        bidSettings.setBidStrategy(BidStrategy.SALES_DOWN_ONLY);

        final CreateGoalSettings createGoalSettings = new CreateGoalSettings();
        createGoalSettings.setGoal(Goal.CONSIDERATION);
        createGoalSettings.setKpiValue(100d);

        final CreateCampaignOptimizations campaignOptimizations = new CreateCampaignOptimizations();
        campaignOptimizations.bidSettings(bidSettings);
        campaignOptimizations.setGoalSettings(createGoalSettings);
        return campaignOptimizations;
    }

    //  TODO: Add ADSP Campaign Optimization implementation, for now, copying SP for more accurate LOC comparison
//    private static CreateCampaignOptimizations buildCreateCampaignOptimizationsADSP() {
//        final CreateBidAdjustments createBidAdjustments = new CreateBidAdjustments();
//        createBidAdjustments.setPlacementBidAdjustments(buildCreatePlacementBidAdjustment());
//        // caused an issue with audienceId
//        // ADDING SHOPPER COHORT FEATURE
//        // Commenting for now as it may cause issues in beta
////        createBidAdjustments.setShopperCohortBidAdjustments(buildShopperCohortBidAdjustments());
//
//        final CreateBidSettings bidSettings = new CreateBidSettings();
//        bidSettings.bidAdjustments(createBidAdjustments);
//        bidSettings.setBidStrategy(BidStrategy.SALES_DOWN_ONLY);
//
//        final CreateCampaignOptimizations campaignOptimizations = new CreateCampaignOptimizations();
//        campaignOptimizations.bidSettings(bidSettings);
//        return campaignOptimizations;
//    }

    private static CreateCampaignOptimizations buildCreateCampaignOptimizationsSP() {
        final CreateBidAdjustments createBidAdjustments = new CreateBidAdjustments();
        createBidAdjustments.setPlacementBidAdjustments(buildCreatePlacementBidAdjustment());
        // caused an issue with audienceId
        // ADDING SHOPPER COHORT FEATURE
        // Commenting for now as it may cause issues in beta
//        createBidAdjustments.setShopperCohortBidAdjustments(buildShopperCohortBidAdjustments());

        final CreateBidSettings bidSettings = new CreateBidSettings();
        bidSettings.bidAdjustments(createBidAdjustments);
        bidSettings.setBidStrategy(BidStrategy.SALES_DOWN_ONLY);

        final CreateCampaignOptimizations campaignOptimizations = new CreateCampaignOptimizations();
        campaignOptimizations.bidSettings(bidSettings);
        return campaignOptimizations;
    }

    private static List<CreatePlacementBidAdjustment> buildCreatePlacementBidAdjustment() {
        final CreatePlacementBidAdjustment homePagePlacementBidAdjustment = new CreatePlacementBidAdjustment();
        homePagePlacementBidAdjustment.setPlacement(Placement.TOP_OF_SEARCH);
        homePagePlacementBidAdjustment.setPercentage(10);

        final CreatePlacementBidAdjustment restOfSearchPlacementBidAdjustment = new CreatePlacementBidAdjustment();
        restOfSearchPlacementBidAdjustment.setPlacement(Placement.REST_OF_SEARCH);
        restOfSearchPlacementBidAdjustment.setPercentage(20);

        final CreatePlacementBidAdjustment productPagePlacementBidAdjustment = new CreatePlacementBidAdjustment();
        productPagePlacementBidAdjustment.setPlacement(Placement.PRODUCT_PAGE);
        productPagePlacementBidAdjustment.setPercentage(30);

        return List.of(
                homePagePlacementBidAdjustment,
                restOfSearchPlacementBidAdjustment,
                productPagePlacementBidAdjustment
        );
    }

    public static List<CreateShopperCohortBidAdjustment> buildShopperCohortBidAdjustments() {
//        final CreateAudienceSegment audienceSegment = new CreateAudienceSegment();
//        audienceSegment.audienceId("123");
//        audienceSegment.audienceSegmentType(AudienceSegmentType.SPONSORED_ADS_AMC);

        final CreateShopperCohortBidAdjustment shopperCohortBidAdjustment = new CreateShopperCohortBidAdjustment();
        shopperCohortBidAdjustment.setShopperCohortType(ShopperCohortType.AUDIENCE_SEGMENT);
        shopperCohortBidAdjustment.setPercentage(10);
        //shopperCohortBidAdjustment.addAudienceSegmentsItem(audienceSegment);

        return List.of(shopperCohortBidAdjustment);
    }

    private static CreateBudget buildCreateBudget() {
        final CreateMonetaryBudget createMonetaryBudget = new CreateMonetaryBudget();
        createMonetaryBudget.setValue(100d);

        final CreateMonetaryBudgetMarketplaceSetting createMonetaryBudgetMarketplaceSetting
                = new CreateMonetaryBudgetMarketplaceSetting();
        createMonetaryBudgetMarketplaceSetting.setMarketplace(Marketplace.US);
        createMonetaryBudgetMarketplaceSetting.setMonetaryBudget(createMonetaryBudget);

        final CreateMonetaryBudgetValue createMonetaryBudgetValue = new CreateMonetaryBudgetValue();
        createMonetaryBudgetValue.setMonetaryBudget(createMonetaryBudget);
        createMonetaryBudgetValue.setMarketplaceSettings(List.of(createMonetaryBudgetMarketplaceSetting));

        final CreateBudgetValue createBudgetValue = new CreateBudgetValue();
        createBudgetValue.setMonetaryBudgetValue(createMonetaryBudgetValue);

        final CreateBudget createBudget = new CreateBudget();
        createBudget.setBudgetValue(createBudgetValue);
        createBudget.setBudgetType(BudgetType.MONETARY);
        createBudget.setRecurrenceTimePeriod(Recurrence.DAILY);

        return createBudget;
    }

    public static UpdateCampaignRequest buildUpdateCampaignsRequestContext(final String campaignId) {
        final UpdateCampaignRequest requestContent = new UpdateCampaignRequest();
        requestContent.addCampaignsItem(buildCampaignUpdate(campaignId));
        return requestContent;
    }

    private static CampaignUpdate buildCampaignUpdate(final String campaignId) {
        final UpdateStates updateStates = new UpdateStates();
        updateStates.setState(State.ENABLED);

        final CampaignUpdate campaignUpdate = new CampaignUpdate();
        campaignUpdate.setCampaignId(campaignId);
        campaignUpdate.setState(updateStates);
        return campaignUpdate;
    }


    public static DeleteCampaignRequest buildDeleteCampaignsRequestContent(final String campaignId) {
        final DeleteCampaignRequest requestContent = new DeleteCampaignRequest();
        requestContent.addCampaignIdsItem(campaignId);
        return requestContent;
    }
}
