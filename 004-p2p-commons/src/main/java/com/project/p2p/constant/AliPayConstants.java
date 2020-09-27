package com.project.p2p.constant;

public class AliPayConstants {
    /**
     * 商户appid
      */
    public static final String APPID = "2021000116678413";
    /**
     * 私钥 pkcs8格式的
     */
    public static final String RSA_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCb/mOYVYgwkw2nacsHF3Hr+4VfF8Su4swMoipFjv8473J0+N66bxRdCkhEsm+aRBH9Ewmd1F2ZXEtDFAnE9dVdxNTqDSLNMWnNEQEPiEGQsbY4NYgng3JOCi0KneVoWqJnE/+UT1b8xsB6urPDyVrmC32AjmlwkF2UMBO1NuBZCsxon0ROOvuao8N1i/HLDCEnDwhlJzEg8u28FjVd97dwgEDUkkXyJqrDH7mwSXZKP7Q4b3eceH33pIQAwP28QmeOjlH6ECjYS5h/wGjzkCMrpg+45wMFePGNrX2zDrWBpQ2rGwjirCXM4N7oq0gq3ayN4+StZgdQMPUIvXINOjsFAgMBAAECggEAHRzZ64ky9JXNO4xT9svaWAdF+dxOubDcEhOFvhuU6ERP4SK9NqAadc7AdAm1KF5B2TBfYBRNL5z48tjfheG3Y1ujvNacwxzQuRPOVd7BFccE0fKzqWQvxf7CLzQOp24a1w/1Z/Fys5RIQd9sQ0Ll8v77+i0Z4oce39QlKXHHZvaAtJXVzpRnKt8po1iMsHLgW3jLdHiemUv/C6Mf2ZJAoa2Ph/vSbi/03NHI/NLmNX8BO3O+p89sCP4X4vle+x8PnmXuYaiObxyUcJbQH1PtKtbCRMeh2w84XqAt80q2rdI+kZJIY5CvHPkzPeWcSdHbsxGook0LqLL1PmGE8dyOgQKBgQDkUXRWQDro3hruNglAGwNIbdbzPOiwDXogOl2j21BQu0jpbRMymV1Ztpk4zhAwyz+ddqglaL2Xsw9oz4eFp+29J6ENkLpjM8hMgqoKkMoZQjiyWaNPkmnpP79ujJiT34dPl7bBET5bxZD1z2Nh/NP+NltPSjtF/A7Y+68r9E5EYQKBgQCu6CAvM9Exv+W7g5KESiInz/JCoimNJ5gw9G/jRV4T4zz3ot5c9Xv/ol+pHsHCENFwz500814OLG0RoCvYI8bIowxlN9ChtkhTBIiHfYAu/AkwgcfiXGkQ7lRFdG8u3vvDpagYj3RjlNDxLQV2TDdnM3ElzuG1d2i1htV7lmj5JQKBgQCK1GSzFXc8peEmO8FAM6y5W+C6Iq0yb990vUi3G+IoVNku5t4nhE3ZAUkF23bI1K2n58Cyjo/KMF0MVDSJtuS+gdxSTVCohZIjw8f1iLj6xuOGvy6ocdTouZSDegZE1ESIxy2fFAcg8m8EqMg7iuq0xwSan/6OvYehtNh9F56HwQKBgQCfmd1UdToc5XiiZOsbn/IO1ifAOe0kis7LTSGUB0ZTa3XEmm/B75eYqvgTgKdZiLM0954CJVjcIhncjYW0kuDfUDkNW6MbDRRt/KMAz7paJU3VX3efzKdq9a8cOaUC5iJiYxVCZBTWkYMfr+9eBSpBF6XV1DQNvMplaoX/Yj6jqQKBgQCViUfrzoB2T6FQ1PtO/22zhsn8QuktWisFJFAjUjPNklO0cQqqAmH1I2BSOESNED5sWKe45Sp/tkJqaT+ObVk7upy4hWuT6wD04zKj+r985zofeKKzxPLFLRX6Wl/UP7NJuw4mt9Swmf4SzAalow73melMkW+saE9mx4AKOBtvEw==";
    /**
     * 服务器异步通知页面路径
     */
    public static final String notify_url = "http://www.p2p.com/005-p2p-web/transaction/alipayNotifyNotice";
    /**
     * 页面跳转同步通知页面路径
     */
    public static final String return_url = "http://www.p2p.com/005-p2p-web/transaction/alipayReturnNotice";
    /**
     * 请求网关地址
     */
    public static final String URL = "https://openapi.alipaydev.com/gateway.do";
    /**
     * 编码
     */
    public static final String CHARSET = "utf-8";
    /**
     * 返回格式
     */
    public static final String FORMAT = "json";
    /**
     * 支付宝公钥
     */
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjxp+HOEtuj4iFK6+fm+Hc/xLAkNtr8E6smjU3OhfS0sPnIi6I9Cz/XkXRj6xMclgLXHMxh3eDRbWJo1OESGu6L3y1Jo8hPgC52LVEzuxehtITHi0PxEpsPeRWix3Txz2nwkdzXT8hg8oDCSWAodwDhm/WyOzvLKUC2XQL4bgwwlVQFeSjEzNsmwaq8AhDudbnOB0RZxP9w3Mlbxxm1dzO2AEbL0CMKUBDJr7fAn76QEiaUMC29jRtFu5U2wCVmsN2AM4Mks/I+bLWS+Szhgg6pxhgKEhOZiUDpDJMchyfScEmk9spGEU/rewSrr+BWHXEOBCNnCchZbWYqbtT7PHwwIDAQAB";
    /**
     * RSA2
     */
    public static final String SIGNTYPE = "RSA2";
}