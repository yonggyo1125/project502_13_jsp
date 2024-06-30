package org.choongang.pokemon.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;
import org.choongang.pokemon.entities.PokemonDetail;
import org.choongang.pokemon.entities.api.Pokemon;
import org.choongang.pokemon.mappers.PokemonMapper;

@Service
@RequiredArgsConstructor
public class PokemonSaveService {
    private final PokemonMapper mapper;

    public boolean save(Pokemon data) {

        PokemonDetail detail = PokemonDetail.builder()
                .seq(data.getId())
                .name(data.getName())
                .height(data.getHeight())
                .weight(data.getWeight())
                .baseExperience(data.getBase_experience())
                .frontImage(data.getSprites().getOther().getOfficial_artwork().getFront_default())
                .backImage(data.getSprites().getBack_default())
                .rawData(data.getRawData())
                .nameKr(data.getNameKr())
                .description(data.getDescription())
                .build();
        int result = mapper.register(detail);

        return result > 0;
    }
}
