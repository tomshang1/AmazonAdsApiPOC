package org.example.objectApi.TargetsApi;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.TargetsApi;
import org.openapitools.client.model.DeleteTargetRequest;
import org.openapitools.client.model.TargetMultiStatusResponseWithPartialErrors;

import java.io.IOException;
import java.util.Map;

import static org.example.AuthUtils.CLIENT_ID_HEADER_NAME;
import static org.example.AuthUtils.CLIENT_SECRET_HEADER_NAME;
import static org.example.AuthUtils.PROFILE_ID_HEADER_NAME;
import static org.example.AuthUtils.REFRESH_TOKEN_HEADER_NAME;
import static org.example.AuthUtils.getRefreshedToken;

public class DeleteTargetApi {
    public static TargetMultiStatusResponseWithPartialErrors deleteTarget(
            final TargetsApi targetsApi,
            final DeleteTargetRequest deleteRequestContent,
            final Map<String, String> authMap)
            throws IOException, InterruptedException, ApiException {
        final TargetMultiStatusResponseWithPartialErrors deleteResponseContent;
        try {
            targetsApi.getApiClient().addDefaultHeader("Authorization",
                    getRefreshedToken(authMap.get(REFRESH_TOKEN_HEADER_NAME), authMap.get(CLIENT_ID_HEADER_NAME), authMap.get(CLIENT_SECRET_HEADER_NAME)));
            targetsApi.getApiClient().addDefaultHeader("Amazon-Advertising-API-ClientId", authMap.get(CLIENT_ID_HEADER_NAME));
            deleteResponseContent = targetsApi.deleteTarget(
                    authMap.get(CLIENT_ID_HEADER_NAME), "", authMap.get(PROFILE_ID_HEADER_NAME), deleteRequestContent);
        } catch (final ApiException e) {
            System.out.println("Exception while deleting target: " + e.getMessage()
                    + "\n Headers: " + e.getResponseHeaders()
                    + "\n Body: " + e.getResponseBody());
            throw e;
        }

        System.out.println("Delete targets request: " + deleteRequestContent + ", response: " + deleteResponseContent);

        return deleteResponseContent;
    }
}
