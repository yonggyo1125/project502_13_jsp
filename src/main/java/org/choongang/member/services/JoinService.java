package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.mappers.MemberMapper;
import org.choongang.member.validators.JoinValidator;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final JoinValidator validator;
    private final MemberMapper mapper;

    public void process(RequestJoin form) {
        validator.check(form);


    }
}
