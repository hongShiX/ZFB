package com.hh.front.controller;

import com.hh.api.model.ProductInfo;
import com.hh.api.pojo.BidInfoProduct;
import com.hh.api.pojo.MutiProduct;
import com.hh.common.enums.RCode;
import com.hh.common.util.CommonUtil;
import com.hh.front.view.PageInfo;
import com.hh.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "理财产品功能")
@RestController
@RequestMapping("/v1")
public class ProductController extends BaseController{
    @ApiOperation(value = "首页三类产品", notes = "一个新手宝，三个优选，三个散标产品")
    @GetMapping("/product/index")
    public RespResult queryProductIndex() {
        RespResult result = RespResult.ok();
        MutiProduct mutiProduct = productService.queryIndexPageProducts();
        result.setData(mutiProduct);
        return result;
    }

    /*
        按产品类型分页查询
     */
    @ApiOperation(value="产品分页查询", notes="按产品类型分页查询")
    @GetMapping("/product/list")
    public RespResult queryProductByType(@RequestParam("ptype") Integer pType,
                                                                    @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
                                                                     @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (pType != null && (pType == 0 || pType == 1 || pType == 2)) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);

            // 分页处理，记录总数
            Integer count = productService.queryRecordNumsByType(pType);
            if (count > 0) {
                // 产品集合
                List<ProductInfo> productInfos = productService.queryByTypeLimit(pType, pageNo, pageSize);
                result = RespResult.ok();
                result.setList(productInfos);

                /*构建PageInfo*/
                PageInfo page = new PageInfo(pageNo, pageSize, count);
                result.setPage(page);
            }
        } else {
            /*请求参数有误*/
            result.setRCode(RCode.REQUEST_PRODUCT_TYPE_ERR);
        }
        return result;
    }

    /*查询某个产品的详情和投资记录*/
    @ApiOperation(value = "产品详情", notes = "查询某个产品的详细信息和投资五条记录")
    @GetMapping("/product/info")
    public RespResult queryProductDetail(@RequestParam("productId") Integer id) {
        RespResult result = RespResult.fail();
        if (id != null && id > 0) {
            result = RespResult.ok();
            // 查询产品信息
            ProductInfo productInfo = productService.queryById(id);

            if (productInfo != null) {
                // 如果产品信息不为空，查询投资记录
                List<BidInfoProduct> bidInfoList = investService.queryBidListByproductId(id, 1, 5);

                result = RespResult.ok();
                result.setData(productInfo);
                result.setList(bidInfoList);
            } else {
                result.setRCode(RCode.PRODUCT_OFFLINE);
            }
        }
        return result;
    }
}
