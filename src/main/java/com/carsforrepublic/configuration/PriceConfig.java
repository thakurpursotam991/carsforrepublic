package com.carsforrepublic.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Car Properties to calculate final price ,
 * it's loading all the parameters from application.yml
 * to calculate final price dynamically
 */
@Component
@ConfigurationProperties("car.properties")
@Data
public class PriceConfig {

    private double basepriceminvalue;
    private double basepricemultiplier;
    private List<String> colourlist;
    private double colourincludesvalue;
    private double colourexcludesvalue;
    private int offroadnowheeldrive;
    private  boolean offroadamphibious;
    private double offroadincludesvalue;
    private double offroadexcludesvalue;
    private String expression;

}
