package com.htlc.cykf.model;

/**
 * Created by sks on 2016/3/4.
 */
public class DischargeSummaryBean {
    public String subjectName ;
    public String photo ;
    public String profession  ;
    public String checkNum  ;
    public String advice  ;
    public String sex  ;
    public String inDiagnose  ;
    public String inDay  ;
    public String inInfo  ;
    public String result  ;
    public String outDiagnose  ;
    public String discharged  ;
    public String hospitalize  ;
    public String name  ;
    public String region  ;
    public String onIfo  ;
    public String outInfo  ;
    public String age  ;

    @Override
    public String toString() {
        return "DischargeSummaryBean{" +
                "subjectName='" + subjectName + '\'' +
                ", photo='" + photo + '\'' +
                ", profession='" + profession + '\'' +
                ", checkNum='" + checkNum + '\'' +
                ", advice='" + advice + '\'' +
                ", sex='" + sex + '\'' +
                ", inDiagnose='" + inDiagnose + '\'' +
                ", inDay='" + inDay + '\'' +
                ", inInfo='" + inInfo + '\'' +
                ", result='" + result + '\'' +
                ", outDiagnose='" + outDiagnose + '\'' +
                ", discharged='" + discharged + '\'' +
                ", hospitalize='" + hospitalize + '\'' +
                ", name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", onIfo='" + onIfo + '\'' +
                ", outInfo='" + outInfo + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
