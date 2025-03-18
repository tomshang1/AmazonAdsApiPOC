package org.example.objectApi;

import org.example.objectApi.TargetsApi.CreateTargetApi;
import org.example.objectApi.TargetsApi.DeleteTargetApi;
import org.example.objectApi.TargetsApi.ListTargetApi;
import org.example.objectApi.TargetsApi.UpdateTargetApi;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.TargetsApi;
import org.openapitools.client.model.CreateTargetRequest;
import org.openapitools.client.model.DeleteTargetRequest;
import org.openapitools.client.model.QueryTargetRequest;
import org.openapitools.client.model.TargetMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.TargetSuccessResponse;
import org.openapitools.client.model.UpdateTargetRequest;

import java.io.IOException;
import java.util.Map;

public class TargetsApiService {
    private final TargetsApi targetsApi;
    private final Map<String, String> authMap;

    public TargetsApiService(TargetsApi targetsApi, Map<String, String> authMap) {
        this.targetsApi = targetsApi;
        this.authMap = authMap;
    }

    public String createTarget(final CreateTargetRequest createRequestContent) throws IOException, InterruptedException, ApiException {
        return CreateTargetApi.createTarget(targetsApi, createRequestContent, authMap);
    }

    public TargetMultiStatusResponseWithPartialErrors updateTarget(final UpdateTargetRequest updateRequestContent) throws IOException, InterruptedException, ApiException {
        return UpdateTargetApi.updateTarget(targetsApi, updateRequestContent, authMap);
    }

    public TargetMultiStatusResponseWithPartialErrors deleteTarget(final DeleteTargetRequest deleteRequestContent) throws IOException, InterruptedException, ApiException {
        return DeleteTargetApi.deleteTarget(targetsApi, deleteRequestContent, authMap);
    }

    public TargetSuccessResponse listTarget(final QueryTargetRequest listRequestContent) throws IOException, InterruptedException, ApiException {
        return ListTargetApi.listTarget(targetsApi, listRequestContent, authMap);
    }
}
