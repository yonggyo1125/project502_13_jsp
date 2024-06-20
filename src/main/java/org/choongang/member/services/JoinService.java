package org.choongang.member.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.global.config.annotations.Service;
import org.choongang.member.mapper.MemberMapper;
import org.choongang.member.validators.JoinValidator;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final JoinValidator validator;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final HttpSession session;
    private final MemberMapper mapper;

    public void process() {
        System.out.println(mapper);
    }
}
