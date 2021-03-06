package com.inext.configuration;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 被继承的Mapper，一般业务Mapper继承它
 */
public interface BaseDao<T> extends Mapper<T>, MySqlMapper<T> {
    // FIXME 特别注意，该接口不能被扫描到，否则会出错
}