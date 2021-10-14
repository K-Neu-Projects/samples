package com.lr.myoffice.dataservice.services;

import org.springframework.stereotype.Service;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.models.CloudEvent;
import com.azure.core.models.CloudEventDataFormat;
import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;
import com.lr.myoffice.dataservice.config.EventConfig;
import com.lr.myoffice.dataservice.config.MyOfficeConfig;
import com.lr.myoffice.dataservice.model.CustomerUpdateResult;
import com.lr.myoffice.dataservice.model.EventGroup;
import com.lr.myoffice.dataservice.model.EventTypes;
import com.lr.myoffice.dataservice.util.Constants;

@Service
public class EventService {

    private final EventConfig eventConfig;

    public EventService(MyOfficeConfig myOfficeConfig) {
        this.eventConfig = myOfficeConfig.getNotificationQueue();
    }

    public String startCustomerUpdate(int updateCount, String description) {
        var eventGroup = new EventGroup();
        eventGroup.setEventCount(updateCount);
        eventGroup.setDescription(description);

        var cloudEvent = this.buildCloudEvent(EventTypes.START_CUSTOMER_UPDATE, eventGroup);
        this.sendEvent(cloudEvent);

        return eventGroup.getId();
    }

    public void sendCustomerUpateResult(CustomerUpdateResult event) {
        this.sendEvent(this.buildCloudEvent(EventTypes.CUSTOMER_UPDATE_RESULT, event));
    }

    private EventGridPublisherClient<CloudEvent> createPublisherClient() {
        return new EventGridPublisherClientBuilder()
                .endpoint(this.eventConfig.getDataUpdateTopicEndpoint())
                .credential(new AzureKeyCredential(this.eventConfig.getAccessKey()))
                .buildCloudEventPublisherClient();
    }

    private void sendEvent(CloudEvent event) {
        var client = this.createPublisherClient();
        client.sendEvent(event);
    }

    private <T> CloudEvent buildCloudEvent(EventTypes type, T data) {
        return new CloudEvent(Constants.SERVICE_NAME, type.getName(), BinaryData.fromObject(data),
                CloudEventDataFormat.JSON, Constants.APPLICATION_JSON);
    }
}
