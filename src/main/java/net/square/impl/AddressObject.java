package net.square.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.square.ProxyAPI;

/**
 * An AddressObject is an object in which all parts of an IP address are united.
 * In addition to the ASN number, the country of origin or the city of the IP address can also be found here.
 * This information is provided by https://proxycheck.io, and I combine them via the interface in {@link ProxyAPI}.
 */
@Getter @AllArgsConstructor @EqualsAndHashCode
public class AddressObject {

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
