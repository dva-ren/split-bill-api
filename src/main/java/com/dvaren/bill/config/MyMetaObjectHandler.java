package com.dvaren.bill.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 自动生成时间字段配置
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = this.getFieldValByName("createTime", metaObject);
        if(createTime == null){
            this.setFieldValByName("createTime",new Timestamp(System.currentTimeMillis()), metaObject);
        }
//        this.setFieldValByName("updateTime",new Timestamp(System.currentTimeMillis()),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",new Timestamp(System.currentTimeMillis()),metaObject);
    }
}
