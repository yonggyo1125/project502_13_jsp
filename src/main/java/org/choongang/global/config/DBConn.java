package org.choongang.global.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class DBConn {
    private static SqlSessionFactory factory;

    static {
        try {
            String mode = System.getenv("mode");
            mode = mode == null || !mode.equals("prod") ? "dev":"prod";

            Reader reader = Resources.getResourceAsReader("org/choongang/global/config/mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(reader, mode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSession(boolean autoCommit) {
        return factory.openSession(autoCommit);
    }

    /**
     * 기본 getSession() 메서드를 통해서 SqlSession 객체를 생성하는 경우는
     * 하나의 SQL 쿼리 실행마다 COMMIT을 하도록 autoCommit을 true로 설정합니다.
     *
     * @return
     */
    public static SqlSession getSession() {
        return getSession(true);
    }
}
