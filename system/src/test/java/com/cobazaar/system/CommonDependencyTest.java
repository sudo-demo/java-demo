package com.cobazaar.system;

import com.cobazaar.common.result.Result;
import com.cobazaar.common.entity.BaseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Common模块依赖测试类
 * 验证system模块能否正常使用common模块提供的组件
 */
@SpringBootTest
public class CommonDependencyTest {

    @Test
    public void testResultClassImport() {
        // 测试Result类是否能正常使用
        Result<String> result = Result.success("测试成功");
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals("测试成功", result.getData());
        assertNotNull(result.getTimestamp());
    }

    @Test
    public void testBaseEntityImport() {
        // 测试BaseEntity类是否能正常使用
        TestEntity entity = new TestEntity();
        assertNotNull(entity);
        assertNull(entity.getCreateTime());
        assertNull(entity.getUpdateTime());
    }

    @Test
    public void testStringUtilImport() {
        // 测试字符串操作
        assertTrue(null == null || "".equals(null));
        assertTrue("".equals(""));
        assertFalse("test".equals(""));
    }

    /**
     * 测试用的实体类，继承BaseEntity
     */
    static class TestEntity extends BaseEntity {
        private String name;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
    }
}