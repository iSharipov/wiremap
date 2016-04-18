package com.isharipov.domain.skyhook;

import com.isharipov.domain.common.CommonRq;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class AccessPoint extends CommonRq {
    @XmlElement(name = "signal-strength")
    private String signalStrength;
}
