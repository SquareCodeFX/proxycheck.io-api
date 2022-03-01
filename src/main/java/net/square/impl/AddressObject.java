package net.square.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
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
