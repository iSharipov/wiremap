package com.isharipov.domain.skyhook;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by Илья on 19.04.2016.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Key {
    @XmlAttribute
    private String key;

    @XmlAttribute
    private String username;
}
