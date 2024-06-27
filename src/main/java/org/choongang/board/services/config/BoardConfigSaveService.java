package org.choongang.board.services.config;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.controllers.RequestBoard;
import org.choongang.board.mappers.BoardMapper;
import org.choongang.global.config.annotations.Service;

@Service
@RequiredArgsConstructor
public class BoardConfigSaveService {
    private final BoardMapper mapper;

    public void process(RequestBoard form) {

    }
}
