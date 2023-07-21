package com.hh.front.controller;

import com.hh.api.model.User;
import com.hh.common.constants.RedisKey;
import com.hh.common.util.CommonUtil;
import com.hh.front.view.RespResult;
import com.hh.front.view.invest.RankView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 有关投资的功能
 */
@Api(tags = "投资理财产品")
@RestController
public class InvestController extends BaseController {
    /*投资排行榜*/
    @ApiOperation(value = "投资排行榜", notes = "显示投资金额最高的3位用户信息")
    @GetMapping("/v1/invest/rank")
    public RespResult showInvestRank() {
        /*从redis查询数据*/
        Set<ZSetOperations.TypedTuple<String>> sets = stringRedisTemplate
                .boundZSetOps(RedisKey.KEY_INVEST_RANK)
                .reverseRangeWithScores(0, 2);

        List<RankView> rankList = new ArrayList<>();
        /*遍历set集合*/
        sets.forEach(tuple -> {
            rankList.add(new RankView(CommonUtil.tuoMinPhone(tuple.getValue()), tuple.getScore()));
        });

        RespResult result = RespResult.ok();
        result.setList(rankList);
        return result;
    }

    @ApiOperation(value = "投资理财产品")
    @PostMapping("/v1/invest/product")
    /*购买理财产品，更新投资排行榜*/
    public RespResult investProduct(@RequestHeader("uid") Integer uid,
                                    @RequestParam("productId") Integer productId,
                                    @RequestParam("money") BigDecimal money) {
        RespResult result = RespResult.fail();
        // 检查基本参数
        if ((uid != null && uid > 0) && (productId != null && productId > 0)
                && (money != null && money.intValue() % 100 == 0 && money.intValue() >= 100)) {
            Integer investResult = investService.investProduct(uid, productId, money);
            switch (investResult) {
                case 0:
                    result.setMsg("投资数据不正确");
                    break;
                case 1:
                    result = RespResult.ok();
                    modifyInvestRank(uid, money);
                    break;
                case 2:
                    result.setMsg("资金不足");
                    break;
                case 3:
                    result.setMsg("理财产品不存在");
                    break;
            }

        }
        return result;
    }

    /*更新投资排行榜*/
    private void modifyInvestRank(Integer uid, BigDecimal money) {
        User user = userService.queryById(uid);
        if (user != null) {
            // 更新redis中的投资排行榜
            String key = RedisKey.KEY_INVEST_RANK;
            stringRedisTemplate.boundZSetOps(key)
                    .incrementScore(user.getPhone(), money.doubleValue());
        }
    }
}
