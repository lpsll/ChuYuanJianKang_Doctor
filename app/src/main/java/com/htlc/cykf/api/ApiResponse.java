
package com.htlc.cykf.api;

import java.util.ArrayList;

public class ApiResponse<T> {
    public String code;    // 返回码，0为成功
    public String msg;      // 返回信息
    public T data;           // 单个对象
    public ArrayList<T> dataArray;       // 数组对象
//    public int currentPage; // 当前页数
//    public int pageSize;    // 每页显示数量
//    public int maxCount;    // 总条数
//    public int maxPage;     // 总页数
    
}
