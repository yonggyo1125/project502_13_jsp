package org.choongang.global.config;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * 마이바티스 DB 연결 테스트
 * 
 */
public class DBConnTest {
    
    @Test
    @DisplayName("마이바티스 설정에 따라 SqlSession 객체가 정상 생성되는지 테스트")
    void dbConnectionTest() {
        assertDoesNotThrow(() -> {
            SqlSession session = DBConn.getSession();
            System.out.println(session);
        });
    }
}
