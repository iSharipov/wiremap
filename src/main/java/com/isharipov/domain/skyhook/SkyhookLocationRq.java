package com.isharipov.domain.skyhook;

import lombok.Data;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@XmlRootElement(name = "LocationRQ", namespace = "http://skyhookwireless.com/wps/2005")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkyhookLocationRq {
    @XmlElement
    private AuthenticationParameters authentication;

    @XmlElement(name="access-point")
    private AccessPoint accessPoint;

    @XmlAttribute
    private String version;

    @XmlAttribute(name="street-address-lookup")
    private String streetAddressLookup;
}
