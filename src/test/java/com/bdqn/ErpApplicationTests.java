package com.bdqn;

import com.bdqn.sys.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ErpApplicationTests {

    @Test
    void contextLoads() {
        Integer age = null;
        String name = "   ";
        System.out.println(CommonUtils.isBlank(name));
        System.out.println(CommonUtils.isNotBlank(name));
        System.out.println(CommonUtils.isEmpty(age));
        System.out.println(CommonUtils.isNotEmpty(age));
    }

}
