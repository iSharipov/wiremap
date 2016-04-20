package com.isharipov.domain.skyhook;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.IOException;


/**
 * Created by Илья on 17.04.2016.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthenticationParameters {
    private static int x;
    @XmlElement
    private Key key;

    @XmlAttribute
    private String version;
}
