package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.ManageService;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/product")
@Api(tags = "spu 数据接口")
public class SpuManageController {
//    http://api.gmall.com/admin/product/ {page}/{limit}?category3Id=61
    @Autowired
    private ManageService manageService;
    // spu 分页
    @GetMapping("{page}/{limit}")
    public Result getSpuList(@PathVariable Long page,
                             @PathVariable Long limit,
                             SpuInfo spuInfo){
        //处理的分页标准
//        IPage
        Page<SpuInfo> spuInfoPage = new Page<>(page,limit);
        //服务层方法调用
        IPage spuInfoPageList = manageService.getSpuInfoPage(spuInfoPage, spuInfo);
        return Result.ok(spuInfoPageList);
    }
    //    http://api.gmall.com/admin/product/baseSaleAttrList
    //获取销售属性
    @GetMapping("baseSaleAttrList")
    public Result getBaseSaleAttrList(){
        List<BaseSaleAttr> baseSaleAttrList  =   manageService.getBaseSaleAttrList();
        return Result.ok(baseSaleAttrList);
    }
    //    http://api.gmall.com/admin/product/saveSpuInfo
    //接受前台传递参数 json-->java
    //Spu属性大保存
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
        manageService.saveSpuInfo(spuInfo);
        return Result.ok();
    }
//    http://api.gmall.com/admin/product/spuImageList/{spuId}
//根据spuId获取图片列表
    @GetMapping("spuImageList/{spuId}")
    public Result getspuImageList(@PathVariable Long spuId){
        List<SpuImage>spuImageList = manageService.getspuImageList(spuId);
        return Result.ok(spuImageList);
    }
}
