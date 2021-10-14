package com.lr.myoffice.dataservice.config;

public class EventConfig {
    private String endpoint;
    private String accessKey;
    private String dataUpdate;

    public String getEndpoint() {
        return this.endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getDataUpdate() {
        return this.dataUpdate;
    }

    public void setDataUpdate(String dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public String getDataUpdateTopicEndpoint() {
        return this.endpoint + this.dataUpdate;
    }
}
