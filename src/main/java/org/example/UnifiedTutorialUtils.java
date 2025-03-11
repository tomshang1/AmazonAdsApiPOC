package org.example;

import org.openapitools.client.model.AdProduct;
import org.openapitools.client.model.CampaignAdProductFilter;
import org.openapitools.client.model.CampaignStateFilter;
import org.openapitools.client.model.QueryCampaignRequest;
import org.openapitools.client.model.State;

import java.util.List;

public class UnifiedTutorialUtils {
    public static QueryCampaignRequest buildTestQueryCampaignRequest() {
        final CampaignStateFilter stateFilter = new CampaignStateFilter();
        stateFilter.setInclude(List.of(State.ENABLED));

        final CampaignAdProductFilter adProductFilter = new CampaignAdProductFilter();
        adProductFilter.setInclude(List.of(AdProduct.SPONSORED_PRODUCTS));

        final QueryCampaignRequest queryCampaignRequest = new QueryCampaignRequest();
        queryCampaignRequest.setStateFilter(stateFilter);
        queryCampaignRequest.setMaxResults(5000);
        queryCampaignRequest.setAdProductFilter(adProductFilter);
        return queryCampaignRequest;
    }
}
