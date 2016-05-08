package com.isharipov.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Илья on 22.04.2016.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "response")
public class Response implements Serializable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @JsonIgnore
    private String params;
    private String lat;
    private String lng;
    private String provider;
    private String error;
    @JsonIgnore
    private Float accuracy;
}
