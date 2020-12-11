package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ManageService {
    //先查询所有的一级分类数据
    List<BaseCategory1> findAll();

    //根据一级分类Id 查询二级分类数据
    List<BaseCategory2> getCategory2(Long category1Id);

    //根据二级分类Id 查询三级分类数据
    List<BaseCategory3> getCategory3(Long category2Id);

    //根据分类Id 来查询平台属性
    List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id);

    // 大保存 平台属性 +平台属性值
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    //根据平台属性id 获取平台属性集合
    List<BaseAttrValue> getAttrValueList(Long attrId);

    //根据平台属性 id 获取平台属性值
    BaseAttrInfo getBaseAttrInfo(Long attrId);

    //传入查询条件
    IPage getSpuInfoPage(Page<SpuInfo> spuInfoPage, SpuInfo spuInfo);
    //获取销售属性
    List<BaseSaleAttr> getBaseSaleAttrList();
    //Spu属性大保存
    void saveSpuInfo(SpuInfo spuInfo);


    //根据spuId获取图片列表
    List<SpuImage> getspuImageList(Long spuId);
    //根据spuId获取销售属性
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    //sku大保存
    void saveSkuInfo(SkuInfo skuInfo);

//    获取sku分页列表
    IPage<SkuInfo> getSkuInfoList(Page<SkuInfo> skuInfoPage);
    //上架
    void onSale(Long skuId);
    //下架
    void cancelSale(Long skuId);
    //根据id获取商品的基本信息+图片列表
    SkuInfo getSkuInfoById(Long skuId);
    //根据三级id获取 分类名称
    BaseCategoryView getCategoryViewByCategory3Id(Long category3Id);
    //根据skuid获取价格
    BigDecimal getSkuPrice(Long skuId);
    //根据spuId，skuId 查询销售属性集合
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);
    //根据spuId 查询map 集合数据
    Map getSkuValueIdsMap(Long spuId);

}
