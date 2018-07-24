package com.minlia.module.pooul.body.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 支付参数返回体
 * Created by garen on 2018/07/17.
 */
@Data
public class PooulPayData {

//    {
//        "prepay_id":"wx1918001472767249894ddfdc3284524819",
//            "trade_id":"5b50612e01c9111b33aa837a",
//            "mch_trade_id":"MO00002",
//            "merchant_id":"2162288807443437",
//            "pay_info":"{
//                "appId":"wx469ffdb81de47e4d",
//                "timeStamp":1531994414,
//                "nonceStr":"5b50612e01c9111b33aa837c",
//                "package":"prepay_id=wx1918001472767249894ddfdc3284524819",
//                "signType":"MD5",
//                "paySign":"6132d1c4860d639abfd78476fbc6317c"
//            }"
//    }

    @JsonProperty("prepay_id")
    private String prepay_id;

    private String trade_id;

    private String mch_trade_id;

    private String merchant_id;

    private String pay_info;

}

