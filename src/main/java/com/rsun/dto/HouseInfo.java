package com.rsun.dto;

public class HouseInfo {

    private String idx;
    private String certidx;
    private String certno;
    private String pjname;
    private String buildno;
    private String unitno;
    private String roomno;
    private String floor;
    private String lstusage;
    private String lstarea;
    private String status;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getCertidx() {
        return certidx;
    }

    public void setCertidx(String certidx) {
        this.certidx = certidx;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getPjname() {
        return pjname;
    }

    public void setPjname(String pjname) {
        this.pjname = pjname;
    }

    public String getBuildno() {
        return buildno;
    }

    public void setBuildno(String buildno) {
        this.buildno = buildno;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getLstusage() {
        return lstusage;
    }

    public void setLstusage(String lstusage) {
        this.lstusage = lstusage;
    }

    public String getLstarea() {
        return lstarea;
    }

    public void setLstarea(String lstarea) {
        this.lstarea = lstarea;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HouseInfo{" +
                "idx='" + idx + '\'' +
                ", certidx='" + certidx + '\'' +
                ", certno='" + certno + '\'' +
                ", pjname='" + pjname + '\'' +
                ", buildno='" + buildno + '\'' +
                ", unitno='" + unitno + '\'' +
                ", roomno='" + roomno + '\'' +
                ", floor='" + floor + '\'' +
                ", lstusage='" + lstusage + '\'' +
                ", lstarea='" + lstarea + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String[] toStringArray() {
        return new String[]{idx, certidx, certno, pjname, buildno, unitno, roomno, floor, lstusage, lstarea, status};
    }

    public HouseInfo() {
    }

    public HouseInfo(String[] arr) {
        setIdx(arr[0]);
        setCertidx(arr[1]);
        setCertno(arr[2]);
        setPjname(arr[3]);
        setBuildno(arr[4]);
        setUnitno(arr[5]);
        setRoomno(arr[6]);
        setFloor(arr[7]);
        setLstusage(arr[8]);
        setLstarea(arr[9]);
        setStatus(arr[10]);
    }
}
