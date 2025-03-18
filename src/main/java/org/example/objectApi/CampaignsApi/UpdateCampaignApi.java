package org.example.objectApi.CampaignsApi;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.CampaignsApi;
import org.openapitools.client.model.CampaignMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.UpdateCampaignRequest;

import java.io.IOException;
import java.util.Map;

import static org.example.AuthUtils.CLIENT_ID_HEADER_NAME;
import static org.example.AuthUtils.CLIENT_SECRET_HEADER_NAME;
import static org.example.AuthUtils.PROFILE_ID_HEADER_NAME;
import static org.example.AuthUtils.REFRESH_TOKEN_HEADER_NAME;
import static org.example.AuthUtils.getRefreshedToken;

public class UpdateCampaignApi {
    public static CampaignMultiStatusResponseWithPartialErrors updateCampaign(
            final CampaignsApi campaignsApi,
            final UpdateCampaignRequest updateRequestContent,
            final Map<String, String> authMap)
            throws IOException, InterruptedException, ApiException {
        final CampaignMultiStatusResponseWithPartialErrors updateResponseContent;
        try {
            campaignsApi.getApiClient().addDefaultHeader("Authorization",
                    getRefreshedToken(authMap.get(REFRESH_TOKEN_HEADER_NAME), authMap.get(CLIENT_ID_HEADER_NAME), authMap.get(CLIENT_SECRET_HEADER_NAME)));
            campaignsApi.getApiClient().addDefaultHeader("Amazon-Advertising-API-ClientId", authMap.get(CLIENT_ID_HEADER_NAME));
            updateResponseContent = campaignsApi.updateCampaign(
                    authMap.get(CLIENT_ID_HEADER_NAME), "", authMap.get(PROFILE_ID_HEADER_NAME), updateRequestContent);
        } catch (final ApiException e) {
            System.out.println("Exception while updating campaign: " + e.getMessage()
                    + "\n Headers: " + e.getResponseHeaders()
                    + "\n Body: " + e.getResponseBody());
            throw e;
        }

        System.out.println("Update Campaigns request: " + updateRequestContent + ", response: " + updateResponseContent);

        return updateResponseContent;
    }
}
