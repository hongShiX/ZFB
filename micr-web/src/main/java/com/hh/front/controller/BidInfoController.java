package com.hh.front.controller;

import com.hh.api.model.BidInfo;
import com.hh.front.view.RespResult;
import com.hh.front.view.recharge.BidResultView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "投资业务")
public class BidInfoController extends BaseController{
    @ApiOperation("查询某个用户的投资记录")
    @GetMapping
    public RespResult queryBidInfo(@RequestHeader("uid") Integer uid,
                                   @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                   @RequestParam(required = false, defaultValue = "6") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (uid != null && uid > 0) {
            List<BidInfo> bidInfos = bidInfoService.queryByUid(uid, pageNo, pageSize);
            result = RespResult.ok();
            result.setList(toView(bidInfos));
            // 暂时没做分页
        }
        return result;
    }

    public List<BidResultView> toView (List<BidInfo> bidInfos) {
        return null;
    }
}
