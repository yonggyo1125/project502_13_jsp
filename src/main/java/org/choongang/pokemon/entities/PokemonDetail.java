package org.choongang.pokemon.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokemonDetail {
    private long seq;
    private String name;
    private int weight;
    private int height;
    private int baseExperience;
    private String frontImage;
    private String backImage;
    private String rawData;
}
