package org.choongang.pokemon.entities.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cry {
    private String latest;
    private String legacy;
}
