package com.skyapi.weatherapiservice.helper.string;

public enum Ip2LocStatus {
    OK("OK"),
    EMPTY_IP_ADDRESS("EMPTY_IP_ADDRESS"),
    INVALID_IP_ADDRESS("INVALID_IP_ADDRESS"),
    MISSING_FILE("MISSING_FILE"),
    IPV6_NOT_SUPPORTED("IPV6_NOT_SUPPORTED"),
    UNKNOWN_ERROR("UNKNOWN_ERROR");

    private final String status;

    Ip2LocStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
