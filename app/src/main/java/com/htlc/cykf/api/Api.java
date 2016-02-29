
package com.htlc.cykf.api;


import com.htlc.cykf.api.net.okhttp.callback.ResultCallback;
import com.htlc.cykf.model.InformationBean;
import com.htlc.cykf.model.MedicalHistoryItemBean;
import com.htlc.cykf.model.MessageBean;
import com.htlc.cykf.model.UserBean;

import java.io.File;

public interface Api {
    String Host = "http://192.168.0.103:8100/mysite/outhospital/index.php";
    String SendSmsCode = Host + "/home_user_verifycode_phone_123";
    String Register = Host + "/home_user_enroll_phone_123_pwd_123_type_0_verifycode";
    String Login = Host + "/home_user_login";
    String Forget = Host + "/home_user_resetpwd_phone_123_pwd_1234_type_0";

    String BindDoctor = Host + "/home_sundry_adddoctor";
    String PostPersonInfo = Host + "/home_user_patientinfo";
    String DischargeSummary = Host + "/home_user_outinfo";
    String MedicineHistory = Host + "/home_infowrite_medicinehistory";
    String DrugsList = Host + "/home_infowrite_querymedicine";
    String PostDrugs = Host + "/home_infowrite_takepill";
    String ContactList = Host + "/home_sundry_sessionlist";
    String InformationList = Host + "/home_sundry_magazinelist";
    String InformationDetail = Host + "/home_sundry_magazineinfo";
    String MessageList = Host + "/home_sundry_msglist";
    String MessageDetail = Host + "/home_sundry_getmsginfo";
    String MyCenter = Host + "/home_sundry_getphoto";


    /**
     * 获取验证码
     * @param username 手机号码
     * @return 成功时返回：{ "code": 0, "msg":"success" }
     */
    void sendSmsCode(String username, ResultCallback<ApiResponse<Void>> callback);

    /**
     * 注册
     *
     * @param username
     * @param password
     * @param smsCode
     * @param callback
     */
    void register(String username, String password, String smsCode, ResultCallback<ApiResponse<Void>> callback);

    /**
     * @param username
     * @param password
     * @param callback
     */
    void login(String username, String password, ResultCallback<ApiResponse<UserBean>> callback);

    /**
     * @param username
     * @param password
     * @param smsCode
     * @param callback
     */
    void forget(String username, String password, String smsCode, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param userId
     * @param recommend
     * @param callback
     */
    void bindDoctor(String userId, String recommend, ResultCallback<ApiResponse<Void>> callback);

    /**
     * @param userId
     * @param phone
     * @param name
     * @param sex
     * @param age
     * @param profession
     * @param address
     * @param photo
     * @param callback
     */
    void postPersonInfo(String userId, String phone, String name, String sex, String age, String profession, String address, File photo, ResultCallback<ApiResponse<Void>> callback);

    /**
     * 参数:*userid 用户id
     * hospitalize 入院日期
     * discharged 出院日期
     * inDay 住院天数
     * inDiagnose 入院诊断
     * outDiagnose 出院诊断
     * result 治疗结果
     * checkNum 特殊检查号
     * inInfo 入院情况
     * onIfo 住院情况
     * outInfo 出院情况
     * advice 出院医嘱
     */
    void dischargeSummary(String userId,String startTime,String endTime,String totalTime,
                          String inDiagnose,String outDiagnose,String effect,String specialItem,String inStatus,String atStatus,String outStatus,String outAdvice,ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param userId
     * @param callback
     */
    void medicineHistory(String userId,ResultCallback<ApiResponse<MedicalHistoryItemBean>> callback);

    /**
     *
     * @param drugName
     * @param callback
     */
    void drugsList(String drugName, ResultCallback<String> callback);

    /**
     *
     * @param userId
     * @param date
     * @param drugsJson
     * @param callback
     */
    void postDrugs(String userId, String date, String drugsJson, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param userId
     * @param callback
     */
    void contactList(String userId, ResultCallback<String> callback);

    /**
     *
     * @param username
     * @param page
     * @param callback
     */
    void informationList(String username, String page, ResultCallback<ApiResponse<InformationBean>> callback);

    /**
     *
     * @param userId
     * @param page
     * @param callback
     */
    void messageList(String userId,String page, ResultCallback<ApiResponse<MessageBean>> callback);

    /**
     *
     * @param userId
     * @param callback
     */
    void myCenter(String userId,ResultCallback<ApiResponse<UserBean>> callback);
}
