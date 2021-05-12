package com.pabcubcru.infobooks.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Search {

    private String text;

    private Integer startYear;

    private Integer finishYear;

    private Integer postalCode;

    private String type;
    
}
