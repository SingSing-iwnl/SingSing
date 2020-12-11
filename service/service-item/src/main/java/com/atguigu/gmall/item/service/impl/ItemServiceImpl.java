package com.atguigu.gmall.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ProductFeignClient productFeignClient;
    @Override
    public Map<String, Object> getBySkuId(Long skuId) {
        //map.put ("分类数据的key",分类数据的值)
        Map<String, Object> result = new HashMap<>();
        //获取数据
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (skuInfo!=null) {
            //获取分类数据
            BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
            //获取价格
            BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
            //获取销售属性 销售属性值锁定并回显
            List<SpuSaleAttr> spuSaleAttrListCheckBySku = productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());
            //获取切换数据
            Map skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
            //skuValueIdsMap 转换为Json字符串
            String mapString = JSON.toJSONString(skuValueIdsMap);
            //保存分类数据
            result.put("categoryView",categoryView);
            //保存价格
            result.put("price",skuPrice);
            // 保存valuesSkuJson
            result.put("valuesSkuJson",mapString);
            // 保存数据
            result.put("spuSaleAttrList",spuSaleAttrListCheckBySku);
            // 保存skuInfo
            result.put("skuInfo",skuInfo);

        }
        return result;
    }
}