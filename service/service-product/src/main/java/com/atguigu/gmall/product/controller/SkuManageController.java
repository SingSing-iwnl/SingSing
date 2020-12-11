package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/product")
@Api(tags = "sku接口")
public class SkuManageController {

    @Autowired
    private ManageService manageService;
//    http://api.gmall.com/admin/product/spuSaleAttrList/{spuId}
    //根据spuID获取销售属性+销售属性值
    @GetMapping("spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable Long spuId){
        List<SpuSaleAttr> spuSaleAttrList= manageService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }
//    spuInfo;
//    spuImage;
//    spuSaleAttr;
//    spuSaleAttrValue;
//    http://api.gmall.com/admin/product/saveSkuInfo
    //saveSkuInfo sku保存
    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        manageService.saveSkuInfo(skuInfo);
        return Result.ok();
    }
//    http://api.gmall.com/admin/product/list/{page}/{limit}
    //获取sku分页列表
    @GetMapping("list/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit){
        Page<SkuInfo> skuInfoPage = new Page<>(page,limit);

        IPage<SkuInfo> skuInfoIPage = manageService.getSkuInfoList(skuInfoPage);
        return Result.ok(skuInfoIPage);

    }
//    http://api.gmall.com/admin/product/onSale/{skuId}
    //上架
    @GetMapping("onSale/{skuId}")
    public Result onSale(@PathVariable Long skuId){
        manageService.onSale(skuId);
        return Result.ok();
    }
//    http://api.gmall.com/admin/product/cancelSale/{skuId}
    //下架
    @GetMapping("cancelSale/{skuId}")
    public Result cancelSale(@PathVariable Long skuId){
        manageService.cancelSale(skuId);
        return Result.ok();
    }
}
