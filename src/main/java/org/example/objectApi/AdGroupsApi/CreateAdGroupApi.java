package org.example.objectApi.AdGroupsApi;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.AdGroupsApi;
import org.openapitools.client.model.AdGroup;
import org.openapitools.client.model.AdGroupMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.AdGroupMultiStatusSuccess;
import org.openapitools.client.model.CreateAdGroupRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.example.AuthUtils.CLIENT_ID_HEADER_NAME;
import static org.example.AuthUtils.CLIENT_SECRET_HEADER_NAME;
import static org.example.AuthUtils.PROFILE_ID_HEADER_NAME;
import static org.example.AuthUtils.REFRESH_TOKEN_HEADER_NAME;
import static org.example.AuthUtils.getRefreshedToken;
import static org.junit.Assert.assertNotNull;

public class CreateAdGroupApi {
    public static String createAdGroup(final AdGroupsApi adGroupsApi,
                                       final CreateAdGroupRequest createRequestContent,
                                       final Map<String, String> authMap)
            throws IOException, InterruptedException, ApiException {
        final AdGroupMultiStatusResponseWithPartialErrors createResponseContent;
        try {
            adGroupsApi.getApiClient().addDefaultHeader("Authorization",
                    getRefreshedToken(authMap.get(REFRESH_TOKEN_HEADER_NAME), authMap.get(CLIENT_ID_HEADER_NAME), authMap.get(CLIENT_SECRET_HEADER_NAME)));
            adGroupsApi.getApiClient().addDefaultHeader("Amazon-Advertising-API-ClientId", authMap.get(CLIENT_ID_HEADER_NAME));
            createResponseContent = adGroupsApi.createAdGroup(
                    authMap.get(CLIENT_ID_HEADER_NAME), "", authMap.get(PROFILE_ID_HEADER_NAME), createRequestContent);
        } catch (final ApiException e) {
            System.out.println("Exception while creating adGroup: " + e.getMessage()
                    + "\n Headers: " + e.getResponseHeaders()
                    + "\n Body: " + e.getResponseBody());
            throw e;
        }

        System.out.println("Create adGroup request: " + createRequestContent + ", response: " + createResponseContent);
        final String adGroupId = Optional.ofNullable(createResponseContent.getSuccess())
                .map(list -> list.get(0))
                .map(AdGroupMultiStatusSuccess::getAdGroup)
                .map(AdGroup::getAdGroupId)
                .orElse(null);
        assertNotNull(adGroupId);

        return adGroupId;
    }
}
