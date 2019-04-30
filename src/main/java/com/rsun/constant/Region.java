package com.rsun.constant;

import org.springframework.beans.factory.annotation.Value;

public enum Region {
    /*
    <option value="1">梁溪区(崇安)</option>
    <option value="2">梁溪区(南长)</option>
    <option value="3">梁溪区(北塘)</option>
    <option value="4">新吴区</option>
    <option value="5">滨湖区</option>
    <option value="6">锡山区</option>
    <option value="7">惠山区</option>
     */
    CHONG_AN,
    NAN_CHANG,
    BEI_TANG,
    XIN_WU,
    BIN_HU,
    XI_SHAN,
    HUI_SHAN;

    @Value("${CHONG_AN}")
    private String chongan;
    @Value("${NAN_CHANG}")
    private String nanchang;
    @Value("${BEI_TANG}")
    private String beitang;
    @Value("${XIN_WU}")
    private String xinwu;
    @Value("${BIN_HU}")
    private String binhu;
    @Value("${XI_SHAN}")
    private String xishan;
    @Value("${HUI_SHAN}")
    private String huishan;

    public String Value() {
        switch (this) {
            case CHONG_AN:
                return chongan;
            case NAN_CHANG:
                return nanchang;
            case BEI_TANG:
                return beitang;
            case XIN_WU:
                return xinwu;
            case BIN_HU:
                return binhu;
            case XI_SHAN:
                return xishan;
            case HUI_SHAN:
            default:
                return huishan;
        }
    }
}
