package org.choongang.pokemon.entities.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ability {
    private Item ability;
    private boolean is_hidden;
    private int slot;
}
