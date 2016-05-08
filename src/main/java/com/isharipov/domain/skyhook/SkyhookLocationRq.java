package com.isharipov.domain.skyhook;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@XmlRootElement(name = "LocationRQ", namespace = "http://skyhookwireless.com/wps/2005")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkyhookLocationRq {
    @XmlElement
    private AuthenticationParameters authentication;

    @XmlElements(value = {
            @XmlElement(name = "access-point",
                    type = AccessPoint.class)
    })
    private AccessPoint[] accessPoint;

    @XmlAttribute
    private String version;
}
