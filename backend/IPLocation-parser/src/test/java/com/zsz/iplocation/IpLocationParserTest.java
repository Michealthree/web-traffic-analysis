package com.zsz.iplocation;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class IpLocationParserTest {

    @Test
    public void parse() {
        IpLocation ipLocation = IpLocationParser.parse("58.210.35.226");
        Assert.assertNotNull(ipLocation);
        assertEquals("CN", ipLocation.getCountry());
        assertEquals("Jiangsu", ipLocation.getRegion());
        assertEquals("Suzhou", ipLocation.getCity());
        assertEquals("31.3041", ipLocation.getLatitude());
        assertEquals("120.5954", ipLocation.getLongitude());
    }
}