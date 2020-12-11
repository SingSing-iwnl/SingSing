package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.atguigu.gmall.product.service.ManageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ManageServiceImpl implements ManageService {

    //注入Mapper
    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;
    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;
    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;


    //spu_info
    //spu_image
    //spu_sale_attr
    //spu_sale_attr_value
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
//    skuInfo
//    skuAttrValue : sku 与平台属性值的关系！
//    skuSaleAttrValue : sku 与销售属性的关系！
//    skuImage : 库存图片表！
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private BaseCategoryViewMapper baseCategoryViewMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<BaseCategory1> findAll() {
        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        //select * from base_category2 where category1_id =category1Id

        return baseCategory2Mapper.selectList(new QueryWrapper<BaseCategory2>().eq("category1_id", category1Id));
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        return baseCategory3Mapper.selectList(new QueryWrapper<BaseCategory3>().eq("category2_id", category2Id));
    }

    //根据分类Id去查询 平台属性
    //后续会有业务 即查询 平台属性 有查询平台属性值
    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        // 单表查询select  * from base_attr_info where category_id=61 and category_level=3;
        // 多表联查
        // select *
        //from base_attr_info bai inner join base_attr_value bav
        //    on bai.id=bav.attr_id
        //    where bai.category_id=61 and  bai.category_level=3;


        return baseAttrInfoMapper.selectBaseAttrInfoList(category1Id,category2Id,category3Id);

    }
//  baseAttrInfo, baseAttrValue
    @Override
    @Transactional
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if (baseAttrInfo.getId()==null){
            //保存
            //baseAttrinfo
            baseAttrInfoMapper.insert(baseAttrInfo);
        }else{
            baseAttrInfoMapper.updateById(baseAttrInfo);
        }

//        baseAttrValue
        //先删除在插入数据
        QueryWrapper<BaseAttrValue> baseAttrValueQueryWrapper = new QueryWrapper<>();
        baseAttrValueQueryWrapper.eq("attr_id", baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValueQueryWrapper);

        //删除完成之后 原始id不存在

        //如下操作 保存
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if (!CollectionUtils.isEmpty(attrValueList))
        {
            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }
    //
    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        return baseAttrValueMapper.selectList(new QueryWrapper<BaseAttrValue>().eq("attr_id",attrId));
    }

    @Override
    public BaseAttrInfo getBaseAttrInfo(Long attrId) {
        BaseAttrInfo baseAttrInfo =  baseAttrInfoMapper.selectById(attrId);
        if (baseAttrInfo!=null){
            //获取平台属性值集合,将属性值集合放入该对象
            baseAttrInfo.setAttrValueList(getAttrValueList(attrId));
        }
        return baseAttrInfo;
    }

    @Override
    public IPage getSpuInfoPage(Page<SpuInfo> spuInfoPage, SpuInfo spuInfo) {
        //构件查询条件
        QueryWrapper<SpuInfo> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("category3_id",spuInfo.getCategory3Id());
        //设置排序规则
        spuInfoQueryWrapper.orderByDesc("id");
        return spuInfoMapper.selectPage(spuInfoPage,spuInfoQueryWrapper);
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }

    @Override
    @Transactional//多表联查 事务!
    public void saveSpuInfo(SpuInfo spuInfo) {
        //spu_info
        //spu_image
        //spu_sale_attr
        //spu_sale_attr_value
        spuInfoMapper.insert(spuInfo);
        //获取图片列表
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        //判断不为空
        if (!CollectionUtils.isEmpty(spuImageList)){
            for (SpuImage spuImage : spuImageList) {
                //保存
                spuImage.setSpuId(spuInfo.getId());
                spuImageMapper.insert(spuImage);
            }
        }
        //spuInfo<-spusaleAttr<-SpuSaleAttrValue
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        //判断不为空
        if (!CollectionUtils.isEmpty(spuSaleAttrList)){
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                //保存
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);

                //这个销售属性中获取销售属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (!CollectionUtils.isEmpty(spuSaleAttrValueList)){
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        //前台传递的json中 字符串没有sale_attr_name 这个是?
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    }
                }
            }
        }
    }

    @Override
    public List<SpuImage> getspuImageList(Long spuId) {

        return spuImageMapper.selectList(new QueryWrapper<SpuImage>().eq("spu_Id", spuId));
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {

        //调用mapper mapper中方法定义
        //service 中方法定义 get save
        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);

    }





    @Override
    @Transactional//三标联查 加事务 注解
    public void saveSkuInfo(SkuInfo skuInfo) {
        //    skuInfo;
        skuInfoMapper.insert(skuInfo);
        //    skuSaleAttrValue;
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            }
        }
        //    skuImage;
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (skuImageList != null && skuImageList.size() > 0){
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            }
        }
        //    skuSaleAttr;
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (!CollectionUtils.isEmpty(skuSaleAttrValueList)){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            }
        }

    }

    @Override
    public IPage<SkuInfo> getSkuInfoList(Page<SkuInfo> skuInfoPage) {
        QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
        skuInfoQueryWrapper.orderByDesc("id");
        return skuInfoMapper.selectPage(skuInfoPage, skuInfoQueryWrapper);
    }

    @Override
    public void onSale(Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setIsSale(1);
        skuInfo.setId(skuId);
        skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public void cancelSale(Long skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setIsSale(0);
        skuInfo.setId(skuId);
        skuInfoMapper.updateById(skuInfo);
    }
//
//    skuInfo :	select * from skuInfo where id = skuId;
//    skuImageList : select * from skuImage where skuId = skuId;
    @Override
    public SkuInfo getSkuInfoById(Long skuId) {
//        if (true){
//            //缓存
//        }else {
//
//        }
        //先获取缓存中的数据
        //定义缓存中的key:sku:skuid:info
        //缓存储存数据的时候 我们需要储存的数据是谁? skuinfo, 缓存的那种数据类型的数据? hash
        //hset(key,field.value) field =property
        //采用String 数据类型
        //key=sku:skuid+:info
        String skukey= RedisConst.SKUKEY_PREFIX+skuId+RedisConst.SKUKEY_SUFFIX;
        //获取数据set (key,value) get(key)
        SkuInfo skuInfo = (SkuInfo) redisTemplate.opsForValue().get(skukey);
        //判断
        if (skuInfo==null){
            //缓存中没有数据,就从数据库中获取数据所以此处需要添加分布式锁
            //声明一个带锁的key=sku:skuid+lock
            String skuKeyLock = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKULOCK_SUFFIX;
            //声明一个uuid 防误删
            String uuid= UUID.randomUUID().toString();
            //开始上锁 第一个参数是声明的锁,第二个参数为防止误删的的uuid,
            Boolean flag = redisTemplate.opsForValue().setIfAbsent(skuKeyLock, uuid, RedisConst.SKULOCK_EXPIRE_PX1, TimeUnit.SECONDS);
            //判断是否上锁成功
            if (flag){
                //上锁成功,执行业务

            }
//            getSkuInfoDB()
        }





        return getSkuInfoDB(skuId);
    }

    private SkuInfo getSkuInfoDB(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        QueryWrapper<SkuImage> skuImageQueryWrapper = new QueryWrapper<>();
        skuImageQueryWrapper.eq("sku_id",skuId);
        List<SkuImage> skuImageList = skuImageMapper.selectList(skuImageQueryWrapper);
        skuInfo.setSkuImageList(skuImageList);
        //  返回数据
        return skuInfo;
    }

    @Override
    public BaseCategoryView getCategoryViewByCategory3Id(Long category3Id) {
        return baseCategoryViewMapper.selectById(category3Id);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (null != skuInfo) {
            return skuInfo.getPrice();
        } else
            return new BigDecimal(0);
//        QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
//        skuInfoQueryWrapper.eq("sku_id",skuId );
//        SkuInfo skuInfo1 = skuInfoMapper.selectById(skuInfoQueryWrapper);
//        return skuInfo1.getPrice();
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuId,spuId);
    }

    //根据spuId 查询map 集合数据
    @Override
    public Map getSkuValueIdsMap(Long spuId) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        List<Map> maps = skuSaleAttrValueMapper.selectSkuValueIdsMap(spuId);
        if (!CollectionUtils.isEmpty(maps)){
            for (Map map : maps) {
                hashMap.put(map.get("value_ids"), map.get("sku_id"));
            }
        }
        return  hashMap;
    }



}
