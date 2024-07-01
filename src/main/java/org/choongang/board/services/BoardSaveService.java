package org.choongang.board.services;

import lombok.RequiredArgsConstructor;
import org.choongang.board.mappers.BoardDataMapper;
import org.choongang.global.config.annotations.Service;

/**
 * 게시글 추가, 수정
 *
 */
@Service
@RequiredArgsConstructor
public class BoardSaveService {

    private final BoardDataMapper mapper;
}
