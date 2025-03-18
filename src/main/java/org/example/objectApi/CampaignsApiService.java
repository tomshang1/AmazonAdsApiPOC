package org.example.objectApi;

import org.example.objectApi.CampaignsApi.CreateCampaignApi;
import org.example.objectApi.CampaignsApi.DeleteCampaignApi;
import org.example.objectApi.CampaignsApi.ListCampaignApi;
import org.example.objectApi.CampaignsApi.UpdateCampaignApi;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CampaignsApi;
import org.openapitools.client.model.CampaignMultiStatusResponseWithPartialErrors;
import org.openapitools.client.model.CampaignSuccessResponse;
import org.openapitools.client.model.CreateCampaignRequest;
import org.openapitools.client.model.DeleteCampaignRequest;
import org.openapitools.client.model.QueryCampaignRequest;
import org.openapitools.client.model.UpdateCampaignRequest;

import java.io.IOException;
import java.util.Map;

public class CampaignsApiService {
    private final CampaignsApi campaignsApi;
    private final Map<String, String> authMap;

    public CampaignsApiService(CampaignsApi campaignsApi, Map<String, String> authMap) {
        this.campaignsApi = campaignsApi;
        this.authMap = authMap;
    }

    public String createCampaign(final CreateCampaignRequest createRequestContent) throws IOException, InterruptedException, ApiException {
        return CreateCampaignApi.createCampaign(campaignsApi, createRequestContent, authMap);
    }

    public CampaignMultiStatusResponseWithPartialErrors updateCampaign(final UpdateCampaignRequest updateRequestContent) throws IOException, InterruptedException, ApiException {
        return UpdateCampaignApi.updateCampaign(campaignsApi, updateRequestContent, authMap);
    }

    public CampaignMultiStatusResponseWithPartialErrors deleteCampaign(final DeleteCampaignRequest deleteRequestContent) throws IOException, InterruptedException, ApiException {
        return DeleteCampaignApi.deleteCampaign(campaignsApi, deleteRequestContent, authMap);
    }

    public CampaignSuccessResponse listCampaign(final QueryCampaignRequest listRequestContent) throws IOException, InterruptedException, ApiException {
        return ListCampaignApi.listCampaign(campaignsApi, listRequestContent, authMap);
    }
}
