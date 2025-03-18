package org.example.objectApi;

import org.example.objectApi.AdGroupsApi.CreateAdGroupApi;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AdGroupsApi;
import org.openapitools.client.model.CreateAdGroupRequest;

import java.io.IOException;
import java.util.Map;

public class AdGroupsApiService {
    private final AdGroupsApi adGroupsApi;
    private final Map<String, String> authMap;

    public AdGroupsApiService(AdGroupsApi adGroupsApi, Map<String, String> authMap) {
        this.adGroupsApi = adGroupsApi;
        this.authMap = authMap;
    }

    public String createAdGroup(final CreateAdGroupRequest createRequestContent) throws IOException, InterruptedException, ApiException {
        return CreateAdGroupApi.createAdGroup(adGroupsApi, createRequestContent, authMap);
    }
}
