package org.choongang.global.config.containers;

import org.junit.jupiter.api.Test;

public class BeanContainerTest {
    @Test
    void beanLoadTest() {
        BeanContainer bc = BeanContainer.getInstance();
        bc.loadBeans();



    }
}
