package org.example.objectApi.CampaignsApi;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.CampaignsApi;
import org.openapitools.client.model.Campaign;
import org.openapitools.client.model.CampaignMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.CampaignMultiStatusSuccess;
import org.openapitools.client.model.CreateCampaignRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.example.AuthUtils.CLIENT_ID_HEADER_NAME;
import static org.example.AuthUtils.CLIENT_SECRET_HEADER_NAME;
import static org.example.AuthUtils.PROFILE_ID_HEADER_NAME;
import static org.example.AuthUtils.REFRESH_TOKEN_HEADER_NAME;
import static org.example.AuthUtils.getRefreshedToken;
import static org.junit.Assert.assertNotNull;

public class CreateCampaignApi {
    public static String createCampaign(final CampaignsApi campaignsApi,
                                        final CreateCampaignRequest createRequestContent,
                                        final Map<String, String> authMap)
            throws IOException, InterruptedException, ApiException {
        final CampaignMultiStatusResponseWithPartialErrors createResponseContent;
        try {
            campaignsApi.getApiClient().addDefaultHeader("Authorization",
                    getRefreshedToken(authMap.get(REFRESH_TOKEN_HEADER_NAME), authMap.get(CLIENT_ID_HEADER_NAME), authMap.get(CLIENT_SECRET_HEADER_NAME)));
            campaignsApi.getApiClient().addDefaultHeader("Amazon-Advertising-API-ClientId", authMap.get(CLIENT_ID_HEADER_NAME));
            createResponseContent = campaignsApi.createCampaign(
                    authMap.get(CLIENT_ID_HEADER_NAME), "", authMap.get(PROFILE_ID_HEADER_NAME), createRequestContent);
        } catch (final ApiException e) {
            System.out.println("Exception while creating campaign: " + e.getMessage()
                    + "\n Headers: " + e.getResponseHeaders()
                    + "\n Body: " + e.getResponseBody());
            throw e;
        }

        System.out.println("Create Campaigns request: " + createRequestContent + ", response: " + createResponseContent);
        final String campaignId = Optional.ofNullable(createResponseContent.getSuccess())
                .map(list -> list.get(0))
                .map(CampaignMultiStatusSuccess::getCampaign)
                .map(Campaign::getCampaignId)
                .orElse(null);
        assertNotNull(campaignId);

        return campaignId;
    }
}
