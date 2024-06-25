package org.choongang.pokemon.entities.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeldItem {
    private Item item;
    private List<Version> version_details;
}
