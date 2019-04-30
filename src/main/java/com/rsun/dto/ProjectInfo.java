package com.rsun.dto;

public class ProjectInfo {
    private String idx;
    private String pjname;
    private String certno;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getPjname() {
        return pjname;
    }

    public void setPjname(String pjname) {
        this.pjname = pjname;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String[] toStringArray() {
        return new String[]{idx, certno, pjname};
    }

    public ProjectInfo() {
    }

    public ProjectInfo(String[] arr) {
        setIdx(arr[0]);
        setCertno(arr[1]);
        setPjname(arr[2]);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            return this.getIdx().equals(((ProjectInfo) obj).getIdx());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return idx.hashCode() + certno.hashCode() + pjname.hashCode();
    }
}
