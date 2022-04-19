package net.square.address;

import lombok.Data;

/**
 * This object contains information about one specific IPv4 address, which is retrieved by sending an HTTP GET request
 * to the API of <a href="https://proxycheck.io">https://proxycheck.io</a>.
 * It will represent the response body of the request that has been sent.
 */
@Data
public class AddressData {
    private String asn;
    private String provider;
    private String organisation;
    private String continent;
    private String country;
    private String isocode;
    private String region;
    private String regioncode;
    private String city;
    private double latitude;
    private double longitude;
    private String proxy;
    private String type;
}