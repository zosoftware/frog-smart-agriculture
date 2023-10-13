package com.frog.iot.service.impl;

import com.frog.common.core.domain.AjaxResult;
import com.frog.common.core.domain.entity.SysRole;
import com.frog.common.core.domain.entity.SysUser;
import com.frog.common.core.redis.RedisCache;
import com.frog.common.utils.DateUtils;
import com.frog.iot.domain.Product;
import com.frog.iot.mapper.DeviceMapper;
import com.frog.iot.mapper.ProductAuthorizeMapper;
import com.frog.iot.mapper.ProductMapper;
import com.frog.iot.model.ChangeProductStatusModel;
import com.frog.iot.model.IdAndName;
import com.frog.iot.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.frog.common.utils.SecurityUtils.getLoginUser;

/**
 * 产品Service业务层处理
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
@Service
public class ProductServiceImpl implements IProductService 
{
    private String tslPreKey ="TSL:";

    // 物模型值命名空间：Key：TSLV:{productId}_{deviceNumber}   HKey:{identity#V/identity#S/identity#M/identity#N}
    private String devicePreKey = "TSLV:";

    @Autowired
    private ThingsModelServiceImpl thingsModelService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductAuthorizeMapper productAuthorizeMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ToolServiceImpl toolService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    @Lazy
    private DeviceServiceImpl deviceService;

    /**
     * 查询产品
     * 
     * @param productId 产品主键
     * @return 产品
     */
    @Override
    public Product selectProductByProductId(Long productId)
    {
        return productMapper.selectProductByProductId(productId);
    }

    /**
     * 查询产品列表
     * 
     * @param product 产品
     * @return 产品
     */
    @Override
    public List<Product> selectProductList(Product product)
    {
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles=user.getRoles();
        // 租户
        if(roles.stream().anyMatch(a->a.getRoleKey().equals("tenant"))){
            product.setTenantId(user.getUserId());
        }
        return productMapper.selectProductList(product);
    }

    /**
     * 查询产品简短列表
     *
     * @return 产品
     */
    @Override
    public List<IdAndName> selectProductShortList()
    {
        Product product =new Product();
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles=user.getRoles();
        // 租户
        if(roles.stream().anyMatch(a->a.getRoleKey().equals("tenant"))){
            product.setTenantId(user.getUserId());
        }
        return productMapper.selectProductShortList(product);
    }

    /**
     * 新增产品
     * 
     * @param product 产品
     * @return 结果
     */
    @Override
    public Product insertProduct(Product product)
    {
        // 判断是否为管理员
        product.setIsSys(1);
        SysUser user = getLoginUser().getUser();
        List<SysRole> roles=user.getRoles();
        for(int i=0;i<roles.size();i++){
            if(roles.get(i).getRoleKey().equals("tenant") || roles.get(i).getRoleKey().equals("general")){
                product.setIsSys(0);
                break;
            }
        }
        // mqtt账号密码
        if(product.getMqttAccount()==null || product.getMqttAccount().equals("")){
            product.setMqttAccount("FastBee");
        }
        if(product.getMqttPassword()==null || product.getMqttPassword().equals("")){
            product.setMqttPassword("P"+toolService.getStringRandom(15));
        }
        if(product.getMqttSecret()==null || product.getMqttSecret().equals("")){
            product.setMqttSecret("K"+toolService.getStringRandom(15));
        }
        product.setStatus(product.getStatus()==null?1:product.getStatus());
        product.setTenantId(user.getUserId());
        product.setTenantName(user.getUserName());
        product.setCreateTime(DateUtils.getNowDate());
        productMapper.insertProduct(product);
        return product;
    }

    /**
     * 修改产品
     * 
     * @param product 产品
     * @return 结果
     */
    @Override
    public int updateProduct(Product product)
    {
        product.setUpdateTime(DateUtils.getNowDate());
        return productMapper.updateProduct(product);
    }

    /**
     * 获取产品下面的设备数量
     *
     * @param productId 产品ID
     * @return 结果
     */
    @Override
    public int selectDeviceCountByProductId(Long productId)
    {
        return deviceMapper.selectDeviceCountByProductId(productId);
    }

    /**
     * 更新产品状态,1-未发布，2-已发布
     *
     * @param model
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult changeProductStatus(ChangeProductStatusModel model)
    {
        if(model.getStatus()!=1 && model.getStatus()!=2){
            return AjaxResult.error("状态更新失败,状态值有误");
        }
        if(model.getStatus()==2 && model.getDeviceType()!=3){
            // 产品下必须包含物模型(不包含监控产品,deviceType==3)
            int thingsCount=productMapper.thingsCountInProduct(model.getProductId());
            if(thingsCount==0){
                return AjaxResult.error("发布失败，请先添加产品的物模型");
            }
            // 产品下物模型的标识符必须唯一
            int repeatCount=productMapper.thingsRepeatCountInProduct(model.getProductId());
            if(repeatCount>1){
                return AjaxResult.error("发布失败，产品物模型的标识符必须唯一");
            }
            // 批量更新产品下所有设备的物模型值
            updateDeviceStatusByProductIdAsync(model.getProductId());
        }
        if(productMapper.changeProductStatus(model)>0){
            return AjaxResult.success("操作成功");
        }
        return AjaxResult.error("状态更新失败");
    }

    /***
     * 更新产品下所有设备的物模型值
     * @param productId
     */
    @Async
    public void updateDeviceStatusByProductIdAsync(Long productId){
        List<String> deviceNumbers=deviceMapper.selectSerialNumberByProductId(productId);
        deviceNumbers.forEach(x->{
            // 缓存新的物模型值
            deviceService.cacheDeviceStatus(productId,x);
        });
    }

    /**
     * 批量删除产品
     * 
     * @param productIds 需要删除的产品主键
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult deleteProductByProductIds(Long[] productIds)
    {
        // 删除物模型JSON缓存
        for(int i=0;i<productIds.length;i++){
            redisCache.deleteObject(tslPreKey+productIds[i]);
        }
        // 产品下不能有固件
        int firmwareCount=productMapper.firmwareCountInProducts(productIds);
        if(firmwareCount>0){
            return AjaxResult.error("删除失败，请先删除对应产品下的固件");
        }
        // 产品下不能有设备
        int deviceCount=productMapper.deviceCountInProducts(productIds);
        if(deviceCount>0){
            return AjaxResult.error("删除失败，请先删除对应产品下的设备");
        }
        // 删除产品物模型
        productMapper.deleteProductThingsModelByProductIds(productIds);
        // 删除产品的授权码
        productAuthorizeMapper.deleteProductAuthorizeByProductIds(productIds);
        // 删除产品
        if(productMapper.deleteProductByProductIds(productIds)>0){
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }


    /**
     * 删除产品信息
     * 
     * @param productId 产品主键
     * @return 结果
     */
    @Override
    public int deleteProductByProductId(Long productId)
    {
        // 删除物模型JSON缓存
        redisCache.deleteObject(tslPreKey+productId);
        return productMapper.deleteProductByProductId(productId);
    }
}
