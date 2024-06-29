package org.choongang.proxy;

import org.choongang.global.config.MapperProxyHandler;
import org.choongang.member.entities.Member;
import org.choongang.member.mappers.MemberMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class ProxyTest {

    @Test
    void test1() {
        MemberMapper mapper = (MemberMapper) Proxy.newProxyInstance(
                MemberMapper.class.getClassLoader(),
                new Class[] {MemberMapper.class},
                new MapperProxyHandler(MemberMapper.class)
        );

        Member member = mapper.get("user01@test.org");
        System.out.println(member);
    }
}
