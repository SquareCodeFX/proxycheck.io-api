package net.square.address;

/**
 * This object contains information about one specific IPv4 address, which is retrieved by sending an HTTP GET request
 * to the API of <a href="https://proxycheck.io">https://proxycheck.io</a>.
 * It will represent the response body of the request that has been sent.
 */
public record AddressData(
    String asn,
    String provider,
    String organisation,
    String continent,
    String country,
    String isocode,
    String region,
    String regioncode,
    String city,
    double latitude,
    double longitude,
    String proxy,
    String type
) {}