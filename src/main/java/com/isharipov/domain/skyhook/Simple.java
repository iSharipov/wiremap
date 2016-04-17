package com.isharipov.domain.skyhook;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Created by Илья on 17.04.2016.
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Simple {
    private String username;
    private String realm;
}
