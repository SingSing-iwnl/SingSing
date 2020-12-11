package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/product/baseTrademark")
public class BaseTrademarkController {
    @Autowired
    private BaseTrademarkService baseTrademarkService;

    //  http://api.gmall.com/admin/product/baseTrademark/{page}/{limit}
    @GetMapping("{page}/{limit}")
    public Result getBaseTradeMarkList(@PathVariable Long page,
                                       @PathVariable Long limit){

        Page<BaseTrademark> objectPage = new Page<>(page, limit);
        IPage baseTradeMarekList =  baseTrademarkService.getBaseTradeMarekList(objectPage);
        return Result.ok(baseTradeMarekList);
    }
//    http://api.gmall.com/admin/product/baseTrademark/save
    @PostMapping("save")
    public Result save(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }
//    http://api.gmall.com/admin/product/baseTrademark/update
    @PostMapping("update")
    public Result update(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }
//    http://api.gmall.com/admin/product/baseTrademark/remove/{id}
    @DeleteMapping("remove/{id}")
    public Result delete(@PathVariable Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }
//    http://api.gmall.com/admin/product/baseTrademark/get/{id}

    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        BaseTrademark byId = baseTrademarkService.getById(id);
        return Result.ok(byId);
    }
    //    http://api.gmall.com/admin/product/baseTrademark/getTrademarkList

    //获取品牌属性
    @GetMapping("getTrademarkList")
    public Result getTrademarkList(){
        return Result.ok(baseTrademarkService.list(null));
    }

}

