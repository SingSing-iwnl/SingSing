package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mqx
 * @date 2020-11-30 10:49:56
 */
@Mapper
public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {
    /**
     * 根据分类Id 查询平台属性+平台属性值。
     * 在mybatis 传递多个的时候，需要添加注解，后台才能够获取到。
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> selectBaseAttrInfoList(@Param("category1Id") Long category1Id,
                                              @Param("category2Id") Long category2Id,
                                              @Param("category3Id") Long category3Id);
}
