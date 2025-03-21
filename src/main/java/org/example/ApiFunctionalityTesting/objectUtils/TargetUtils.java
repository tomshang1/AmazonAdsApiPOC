package org.example.ApiFunctionalityTesting.objectUtils;

import org.openapitools.client.model.AdProduct;
import org.openapitools.client.model.CreateProductCategoryRefinement;
import org.openapitools.client.model.CreateProductCategoryRefinementValue;
import org.openapitools.client.model.CreateProductCategoryTarget;
import org.openapitools.client.model.CreateStates;
import org.openapitools.client.model.CreateTargetBid;
import org.openapitools.client.model.CreateTargetBidValue;
import org.openapitools.client.model.CreateTargetDetails;
import org.openapitools.client.model.CreateTargetRequest;
import org.openapitools.client.model.DeleteTargetRequest;
import org.openapitools.client.model.Marketplace;
import org.openapitools.client.model.MarketplaceScope;
import org.openapitools.client.model.QueryTargetRequest;
import org.openapitools.client.model.State;
import org.openapitools.client.model.TargetCreate;
import org.openapitools.client.model.TargetTargetIdFilter;
import org.openapitools.client.model.TargetType;
import org.openapitools.client.model.TargetUpdate;
import org.openapitools.client.model.UpdateStates;
import org.openapitools.client.model.UpdateTargetRequest;

import java.util.List;
import java.util.Objects;

public class TargetUtils {
    public static CreateTargetRequest buildCreateTargetRequestContent(final String campaignId, final String adGroupId, final AdProduct adProduct) {
        final CreateTargetRequest requestContent = new CreateTargetRequest();
        requestContent.addTargetsItem(buildTargetCreate(campaignId, adGroupId, adProduct));
        return requestContent;
    }

    private static TargetCreate buildTargetCreate(final String campaignId, final String adGroupId, final AdProduct adProduct) {
        final TargetCreate targetCreate = new TargetCreate();

        if (Objects.equals(AdProduct.SPONSORED_PRODUCTS, adProduct)) {
            targetCreate.setAdProduct(adProduct);
            targetCreate.setTargetType(TargetType.PRODUCT);
            targetCreate.setNegative(false);
            targetCreate.setBid(buildCreateTargetBidValue());
            targetCreate.setTargetDetails(buildCreateTargetDetails());
        } else if (Objects.equals(AdProduct.SPONSORED_BRANDS, adProduct)) {
            //  TODO: Add SB Target Create implementation, for now, copying SP for more accurate LOC comparison
        } else if (Objects.equals(AdProduct.AMAZON_DSP, adProduct)) {
            //  TODO: Add ADSP Target Create implementation, for now, copying SP for more accurate LOC comparison
        }

        return targetCreate;
    }

    private static void addTargetCreateCommonFields(final TargetCreate targetCreate, final String campaignId, final String adGroupId) {
        final CreateStates createStates = new CreateStates();
        createStates.setState(State.PAUSED);

        targetCreate.setMarketplaces(List.of(Marketplace.US));
        targetCreate.setCampaignId(campaignId);
        targetCreate.setAdGroupId(adGroupId);
        targetCreate.setState(createStates);
        targetCreate.setMarketplaceScope(MarketplaceScope.SINGLE_MARKETPLACE);
    }

    private static CreateTargetBidValue buildCreateTargetBidValue() {
        final CreateTargetBid createTargetBid = new CreateTargetBid();
        createTargetBid.setBid(100d);

        final CreateTargetBidValue createTargetBidValue = new CreateTargetBidValue();
        createTargetBidValue.setBid(createTargetBid);
        return createTargetBidValue;
    }

    private static CreateTargetDetails buildCreateTargetDetails() {
        final CreateProductCategoryRefinement createProductCategoryRefinement = new CreateProductCategoryRefinement();
        createProductCategoryRefinement.setProductCategoryId("12345");

        final CreateProductCategoryRefinementValue createProductCategoryRefinementValue = new CreateProductCategoryRefinementValue();
        createProductCategoryRefinementValue.setProductCategoryRefinement(createProductCategoryRefinement);

        final CreateProductCategoryTarget createContentCategoryTarget = new CreateProductCategoryTarget();
        createContentCategoryTarget.setProductCategoryRefinement(createProductCategoryRefinementValue);

        final CreateTargetDetails createTargetDetails = new CreateTargetDetails();
        createTargetDetails.setProductCategoryTarget(createContentCategoryTarget);
        return createTargetDetails;
    }

    //  TODO: Add SB Target Details implementation, for now, copying SP for more accurate LOC comparison
//    private static CreateTargetDetails buildSBCreateTargetDetails() {
//        final CreateProductCategoryRefinement createProductCategoryRefinement = new CreateProductCategoryRefinement();
//        createProductCategoryRefinement.setProductCategoryId("12345");
//
//        final CreateProductCategoryRefinementValue createProductCategoryRefinementValue = new CreateProductCategoryRefinementValue();
//        createProductCategoryRefinementValue.setProductCategoryRefinement(createProductCategoryRefinement);
//
//        final CreateProductCategoryTarget createContentCategoryTarget = new CreateProductCategoryTarget();
//        createContentCategoryTarget.setProductCategoryRefinement(createProductCategoryRefinementValue);
//
//        final CreateTargetDetails createTargetDetails = new CreateTargetDetails();
//        createTargetDetails.setProductCategoryTarget(createContentCategoryTarget);
//        return createTargetDetails;
//    }

    //  TODO: Add ADSP Target Details implementation, for now, copying SP for more accurate LOC comparison
//    private static CreateTargetDetails buildADSPCreateTargetDetails() {
//        final CreateProductCategoryRefinement createProductCategoryRefinement = new CreateProductCategoryRefinement();
//        createProductCategoryRefinement.setProductCategoryId("12345");
//
//        final CreateProductCategoryRefinementValue createProductCategoryRefinementValue = new CreateProductCategoryRefinementValue();
//        createProductCategoryRefinementValue.setProductCategoryRefinement(createProductCategoryRefinement);
//
//        final CreateProductCategoryTarget createContentCategoryTarget = new CreateProductCategoryTarget();
//        createContentCategoryTarget.setProductCategoryRefinement(createProductCategoryRefinementValue);
//
//        final CreateTargetDetails createTargetDetails = new CreateTargetDetails();
//        createTargetDetails.setProductCategoryTarget(createContentCategoryTarget);
//        return createTargetDetails;
//    }

    public static QueryTargetRequest buildTestQueryTargetIdRequest(final String targetId) {
        final TargetTargetIdFilter targetIdFilter = new TargetTargetIdFilter();
        targetIdFilter.addIncludeItem(targetId);

        final QueryTargetRequest requestContent = new QueryTargetRequest();
        requestContent.setTargetIdFilter(targetIdFilter);
        return requestContent;
    }

    public static UpdateTargetRequest buildUpdateTargetRequest(final String targetId) {
        final UpdateTargetRequest requestContent = new UpdateTargetRequest();
        requestContent.addTargetsItem(buildTargetUpdate(targetId));
        return requestContent;
    }

    private static TargetUpdate buildTargetUpdate(final String targetId) {
        final UpdateStates updateStates = new UpdateStates();
        updateStates.setState(State.ENABLED);

        final TargetUpdate targetUpdate = new TargetUpdate();
        targetUpdate.setTargetId(targetId);
        targetUpdate.setState(updateStates);
        return targetUpdate;
    }

    public static DeleteTargetRequest buildDeleteTargetRequest(final String targetId) {
        final DeleteTargetRequest requestContent = new DeleteTargetRequest();
        requestContent.addTargetIdsItem(targetId);
        return requestContent;
    }
}
