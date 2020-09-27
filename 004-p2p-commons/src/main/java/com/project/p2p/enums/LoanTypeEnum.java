package com.project.p2p.enums;

public enum LoanTypeEnum {
    /**
     *新手宝
     */
    NOVICE(0),
    /**
     * 优选产品
     */
    OPTIMIZATION(1),
    /**
     *散标产品
     */
    SCATTER(2);

    private Integer type;

    LoanTypeEnum(Integer type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public static LoanTypeEnum createLoanTypeEnum(Integer val){
        switch (val){
            case 0:
                return LoanTypeEnum.NOVICE;
            case 1:
                return LoanTypeEnum.OPTIMIZATION;
            case 2:
                return LoanTypeEnum.SCATTER;
                default:
                    return null;
        }
    }
}
