package com.alinesno.infra.data.mdm.vo;


/**
 * @author lgb
 * @data 2023/05/19 下午 4:39
 */
public class DashBoardDataVo {



    /**
     * 接口数
     */
    private DashBoardDataItemVo ApiCount;

    /**
     * 启用数
     */

    private DashBoardDataItemVo ApiStatusCount;

    /**
     * 请求数
     */

    private DashBoardDataItemVo ApiUsedCount;

    /**
     * 请求次数
     */
    private DashBoardDataItemVo requestTotalCount;

    /**
     * 成功次数
     */
    private DashBoardDataItemVo  requestSucCount;

    /**
     * 异常次数
     */
    private DashBoardDataItemVo requestFaiCount;

    public DashBoardDataItemVo getApiCount() {
        return ApiCount;
    }

    public void setApiCount(DashBoardDataItemVo apiCount) {
        ApiCount = apiCount;
    }

    public DashBoardDataItemVo getApiStatusCount() {
        return ApiStatusCount;
    }

    public void setApiStatusCount(DashBoardDataItemVo apiStatusCount) {
        ApiStatusCount = apiStatusCount;
    }

    public DashBoardDataItemVo getApiUsedCount() {
        return ApiUsedCount;
    }

    public void setApiUsedCount(DashBoardDataItemVo apiUsedCount) {
        ApiUsedCount = apiUsedCount;
    }

    public DashBoardDataItemVo getRequestTotalCount() {
        return requestTotalCount;
    }

    public void setRequestTotalCount(DashBoardDataItemVo requestTotalCount) {
        this.requestTotalCount = requestTotalCount;
    }

    public DashBoardDataItemVo getRequestSucCount() {
        return requestSucCount;
    }

    public void setRequestSucCount(DashBoardDataItemVo requestSucCount) {
        this.requestSucCount = requestSucCount;
    }

    public DashBoardDataItemVo getRequestFaiCount() {
        return requestFaiCount;
    }

    public void setRequestFaiCount(DashBoardDataItemVo requestFaiCount) {
        this.requestFaiCount = requestFaiCount;
    }
}
