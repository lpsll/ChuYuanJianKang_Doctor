
package com.htlc.cykf.api;


import com.htlc.cykf.api.net.okhttp.callback.ResultCallback;
import com.htlc.cykf.app.activity.AuthorityActivity;
import com.htlc.cykf.model.AuthorityBean;
import com.htlc.cykf.model.BindNumberBean;
import com.htlc.cykf.model.ContactBean;
import com.htlc.cykf.model.ContactGroupBean;
import com.htlc.cykf.model.DepartmentBean;
import com.htlc.cykf.model.DischargeSummaryBean;
import com.htlc.cykf.model.DoctorBean;
import com.htlc.cykf.model.IllnessBean;
import com.htlc.cykf.model.InformationBean;
import com.htlc.cykf.model.MedicalHistoryItemBean;
import com.htlc.cykf.model.MessageBean;
import com.htlc.cykf.model.PatientBean;
import com.htlc.cykf.model.PayStatusBean;
import com.htlc.cykf.model.PriceBean;
import com.htlc.cykf.model.TotalMoneyBean;
import com.htlc.cykf.model.UpdateCityBean;
import com.htlc.cykf.model.UserBean;

import java.io.File;

public interface Api {
//    String Host = "http://192.168.0.109:8100/mysite/outhospital/index.php/home/";
    String Host = "http://123.56.243.53/index.php/Home/";
    String SendSmsCode = Host + "user_verifycode";
    String Register = Host + "user_enroll";
    String Protocol = Host + "user_getprotocol";
    String Login = Host + "user_login";
    String Forget = Host + "user_resetpwd";

    String PostPersonInfo = Host + "user_doctorinfo";
    String DrugsList = Host + "infowrite_querymedicine";
    String MessageList = Host + "sundry_msglist";
    String MessageDetail = Host + "sundry_getmsginfo";
    String MyCenter = Host + "sundry_getphoto";
    String DepartmentList = Host + "doctor_getsubjectlist";
    String IllnessList = Host + "doctor_getdisease";
    String PersonInfo = Host + "user_getdoctorinfo";
    String GenerateBindNumber = Host + "doctor_createnum";
    String SendBindNumber = Host + "doctor_sendmsg";
    String MyPatients = Host + "doctor_patientlist";
    String GetAuthorityStatus = Host + "doctor_getlimit";
    String SetAuthorityStatus = Host + "doctor_setlimit";
    String GetPriceList = Host + "pay_getdoctorprice";
    String SetPriceList = Host + "pay_setdoctorprice";
    String GetTotalMoney = Host + "pay_getmoney";
    String Withdraw = Host + "pay_getrequired";
    String ContactListByType = Host + "sundry_sessionlist";
    String ContactList = Host + "doctor_getcontactmanall";
    String GroupOption = Host + "doctor_setusergroup";
    String GroupExperienceOption = Host + "doctor_setuserpay";
    String AddGroup = Host + "doctor_addgroup";
    String QueryGroup = Host + "doctor_getgroup";
    String DeleteGroup = Host + "doctor_delgroup";
    String ConversationDetail = Host + "doctor_getdetails";
    String GetContactById = Host + "user_getpni";
    String GetContactPayStatus = Host + "doctor_getpayflag";

    String GetAllCity = Host + "city_getall";

    String CheckUpdate = "http://www.pgyer.com/apiv1/app/getAppKeyByShortcut";
    String DownloadApk = "http://www.pgyer.com/apiv1/app/install?aId=%1$s&_api_key=%2$s";




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
     * @param userId
     * @param phone
     * @param name
     * @param sex
     * @param age
     * @param photo
     * @param callback
     */
    void postPersonInfo(String userId, String phone, String name, String sex, String age,
                        String department, String hospital, String special, String experience,
                        File photo,File certification, File level, File honor,
                        ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param drugName
     * @param callback
     */
    void drugsList(String drugName, ResultCallback<String> callback);

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

    /**
     *
     * @param callback
     */
    void departmentList(ResultCallback<ApiResponse<DepartmentBean>> callback);

    /**
     *
     * @param callback
     */
    void illnessList(ResultCallback<ApiResponse<IllnessBean>> callback);

    /**
     *
     * @param callback
     */
    void personInfo(ResultCallback<ApiResponse<DoctorBean>> callback);

    /**
     *
     * @param callback
     */
    void generateBindNumber(String department, String illness, String phone,ResultCallback<ApiResponse<BindNumberBean>> callback);

    /**
     *
     * @param phone
     * @param callback
     */
    void sendBindNumber(String phone, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param callback
     */
    void myPatients(String page,ResultCallback<ApiResponse<PatientBean>> callback);

    /**
     *
     * @param callback
     */
    void getAuthorityStatus(ResultCallback<ApiResponse<AuthorityBean>> callback);

    /**
     *
     * @param authority  1,为可以聊天；2，不可以聊天
     * @param callback
     */
    void setAuthorityStatus(String authority, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param callback
     */
    void getPriceList(ResultCallback<ApiResponse<PriceBean>> callback);

    /**
     *
     * @param one
     * @param three
     * @param six
     * @param twelve
     * @param callback
     */
    void setPriceList(String one, String three, String six, String twelve, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param callback
     */
    void  getTotalMoney(ResultCallback<ApiResponse<TotalMoneyBean>> callback);
    /**
     *
     * @param callback
     */
    void withdraw(ResultCallback<ApiResponse<Void>> callback);
    /**
     *
     * @param type
     * @param callback
     */
    void contactListByType(String type, ResultCallback<ApiResponse<ContactBean>> callback);

    /**
     *
     * @param callback
     */
    void contactList(ResultCallback<ApiResponse<ContactBean>> callback);

    /**
     *
     * @param patientId
     * @param flag
     * @param groupId
     * @param callback
     */
    void groupOption(String patientId, String flag, String groupId, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param patientId
     * @param flag
     * @param callback
     */
    void groupExperienceOption(String patientId, String flag, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param groupName
     * @param callback
     */
    void addGroup(String groupName, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param callback
     */
    void queryGroup(ResultCallback<ApiResponse<ContactGroupBean>> callback);

    /**
     *
     * @param groupId
     * @param callback
     */
    void deleteGroup(String groupId, ResultCallback<ApiResponse<Void>> callback);

    /**
     *
     * @param patientId
     * @param callback
     */
    void conversationDetail(String patientId, ResultCallback<ApiResponse<DischargeSummaryBean>> callback);

    /**
     *
     * @param contactId
     * @param callback
     */
    void getContactById(String contactId, ResultCallback<ApiResponse<ContactBean>> callback);

    /**
     *
     * @param contactId
     * @param callback
     */
    void getContactPayStatus(String contactId, ResultCallback<ApiResponse<PayStatusBean>> callback);


    void getAllCity(String lastModifyData, ResultCallback<ApiResponse<UpdateCityBean>> callback);

    void checkUpdate(ResultCallback<String> callback);
    void downloadApk(String url, String dir, String fileName, ResultCallback<String> callback);
}
