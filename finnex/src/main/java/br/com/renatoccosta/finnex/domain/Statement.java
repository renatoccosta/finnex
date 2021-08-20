package br.com.renatoccosta.finnex.domain;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data @Builder public class Statement {
    private String account;
    private Instant datePosted;
    private Instant dateAsOf;
    private String description;
    private String category;
    private String annotations;
    private BigDecimal value;
    private Double currencyRate;
    private String currencySymbol;
    private String originalId;
}