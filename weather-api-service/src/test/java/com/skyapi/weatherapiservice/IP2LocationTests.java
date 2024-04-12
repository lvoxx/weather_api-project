package com.skyapi.weatherapiservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

class IP2LocationTests {
    final String BIN_FILE="ip2locdb/IP2LOCATION-LITE-DB3.BIN";

    @Test
    void testInvalidIP() throws IOException{
        IP2Location ip2Location = new IP2Location();

        ip2Location.Open(BIN_FILE);
        String ipAddress = "abc";

        IPResult res = ip2Location.IPQuery(ipAddress);

        assertThat(res.getStatus()).isEqualTo("INVALID_IP_ADDRESS");
        System.out.println(res);
    }

    @Test
    void testValidIP1() throws IOException{
        IP2Location ip2Location = new IP2Location();

        ip2Location.Open(BIN_FILE);
        String ipAddress = "108.30.178.78"; // New York

        IPResult res = ip2Location.IPQuery(ipAddress);

        assertThat(res.getStatus()).isEqualTo("OK");
        assertThat(res.getCity()).isEqualTo("New York City");
        System.out.println(res);
    }

    @Test
    void testValidIP2() throws IOException{
        IP2Location ip2Location = new IP2Location();

        ip2Location.Open(BIN_FILE);
        String ipAddress = "103.48.198.14"; // Delhi

        IPResult res = ip2Location.IPQuery(ipAddress);

        assertThat(res.getStatus()).isEqualTo("OK");
        assertThat(res.getCity()).isEqualTo("Delhi");
        System.out.println(res);
    }
}
