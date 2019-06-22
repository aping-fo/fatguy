package com.game.sdk.proto;

/**
 * Created by lucky on 2018/10/11.
 */
public class GetLuckdrawPlayerReq {
    private int pageIdx; //第几页
    private int pageSize; //每页数量

    public int getPageIdx() {
        return pageIdx;
    }

    public void setPageIdx(int pageIdx) {
        this.pageIdx = pageIdx;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
