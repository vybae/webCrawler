package com.rsun.dto.http;

public class HttpResponseWrapper<T> {
    private boolean success;
    private volatile AnalysisResponseType status;
    private String message;
    private T responseContent;
    private int page;
    private int total;
    private volatile int currentReqCount;
    private int limitReqCount;

    public HttpResponseWrapper() {
    }

    public boolean isSuccess() {
        return success;
    }

    public AnalysisResponseType getStatus() {
        return status;
    }

    public void setStatus(AnalysisResponseType status) {
        this.status = status;
        if (AnalysisResponseType.SUCCESS.equals(status)) {
            this.success = true;
        } else {
            this.success = false;
        }
        if (status != null)
            this.message = status.toString();
    }

    public void setStatusWhenSuccess(AnalysisResponseType status) {
        if (AnalysisResponseType.SUCCESS.equals(status)) {
            setStatus(status);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(T responseContent) {
        this.responseContent = responseContent;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentReqCount() {
        return currentReqCount;
    }

    public void setCurrentReqCount(int currentReqCount) {
        if (currentReqCount >= this.currentReqCount) {
            this.currentReqCount = currentReqCount;
        }
    }

    public int getLimitReqCount() {
        return limitReqCount;
    }

    public void setLimitReqCount(int limitReqCount) {
        this.limitReqCount = limitReqCount;
    }

    public <C> HttpResponseWrapper<C> copyWithoutContent() {
        HttpResponseWrapper<C> target = new HttpResponseWrapper<>();
        target.setStatus(this.status);
        target.setMessage(this.message);
        target.setPage(this.page);
        target.setTotal(this.total);
        target.setCurrentReqCount(this.currentReqCount);
        target.setLimitReqCount(this.limitReqCount);
        return target;
    }
}
