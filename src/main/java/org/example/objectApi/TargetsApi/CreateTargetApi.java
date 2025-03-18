package org.example.objectApi.TargetsApi;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.TargetsApi;
import org.openapitools.client.model.CreateTargetRequest;
import org.openapitools.client.model.Target;
import org.openapitools.client.model.TargetMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.TargetMultiStatusSuccess;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.example.AuthUtils.CLIENT_ID_HEADER_NAME;
import static org.example.AuthUtils.CLIENT_SECRET_HEADER_NAME;
import static org.example.AuthUtils.PROFILE_ID_HEADER_NAME;
import static org.example.AuthUtils.REFRESH_TOKEN_HEADER_NAME;
import static org.example.AuthUtils.getRefreshedToken;
import static org.junit.Assert.assertNotNull;

public class CreateTargetApi {
    public static String createTarget(final TargetsApi targetsApi,
                                      final CreateTargetRequest createRequestContent,
                                      final Map<String, String> authMap)
            throws IOException, InterruptedException, ApiException {
        final TargetMultiStatusResponseWithPartialErrors createResponseContent;
        try {
            targetsApi.getApiClient().addDefaultHeader("Authorization",
                    getRefreshedToken(authMap.get(REFRESH_TOKEN_HEADER_NAME), authMap.get(CLIENT_ID_HEADER_NAME), authMap.get(CLIENT_SECRET_HEADER_NAME)));
            targetsApi.getApiClient().addDefaultHeader("Amazon-Advertising-API-ClientId", authMap.get(CLIENT_ID_HEADER_NAME));
            createResponseContent = targetsApi.createTarget(
                    authMap.get(CLIENT_ID_HEADER_NAME), "", authMap.get(PROFILE_ID_HEADER_NAME), createRequestContent);
        } catch (final ApiException e) {
            System.out.println("Exception while creating target: " + e.getMessage()
                    + "\n Headers: " + e.getResponseHeaders()
                    + "\n Body: " + e.getResponseBody());
            throw e;
        }

        System.out.println("Create target request: " + createRequestContent + ", response: " + createResponseContent);
        final String targetId = Optional.ofNullable(createResponseContent.getSuccess())
                .map(list -> list.get(0))
                .map(TargetMultiStatusSuccess::getTarget)
                .map(Target::getTargetId)
                .orElse(null);
        assertNotNull(targetId);

        return targetId;
    }
}
