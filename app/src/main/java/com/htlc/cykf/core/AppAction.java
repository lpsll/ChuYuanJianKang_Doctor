
package com.htlc.cykf.core;

import com.htlc.cykf.api.net.okhttp.callback.ResultCallback;
import com.htlc.cykf.model.AppVersionBean;
import com.htlc.cykf.model.AuthorityBean;
import com.htlc.cykf.model.BindNumberBean;
import com.htlc.cykf.model.ContactBean;
import com.htlc.cykf.model.ContactGroupBean;
import com.htlc.cykf.model.DepartmentBean;
import com.htlc.cykf.model.DischargeSummaryBean;
import com.htlc.cykf.model.DoctorBean;
import com.htlc.cykf.model.DrugBean;
import com.htlc.cykf.model.IllnessBean;
import com.htlc.cykf.model.InformationBean;
import com.htlc.cykf.model.MedicalHistoryItemBean;
import com.htlc.cykf.model.MessageBean;
import com.htlc.cykf.model.NetworkCityBean;
import com.htlc.cykf.model.PatientBean;
import com.htlc.cykf.model.PayStatusBean;
import com.htlc.cykf.model.PriceBean;
import com.htlc.cykf.model.TotalMoneyBean;
import com.htlc.cykf.model.UserBean;

import java.io.File;
import java.util.ArrayList;

public interface AppAction {
    /**
     * 发送验证码
     *
     * @param username 手机号
     * @param listener 回调监听器
     */
    public void sendSmsCode(String username, ActionCallbackListener<Void> listener);

    /**
     * 注册
     *
     * @param username 手机号
     * @param code     验证码
     * @param password 密码
     * @param listener 回调监听器
     */
    public void register(String username, String code, String password, ActionCallbackListener<Void> listener);

    /**
     * 登录
     *
     * @param username 登录名
     * @param password 密码
     * @param listener 回调监听器
     */
    public void login(String username, String password, ActionCallbackListener<UserBean> listener);

    /**
     * @param username 手机号
     * @param code     验证码
     * @param password 密码
     * @param listener 回调监听器
     */
    public void forget(String username, String code, String password, ActionCallbackListener<Void> listener);


    /**
     * @param userId
     * @param phone
     * @param name
     * @param sex
     * @param age
     * @param photo
     * @param listener
     */
    public void postPersonInfo(String userId, String phone, String name, String sex, String age,
                               String department, String hospital, String special, String experience,
                               File photo, File certification, File level, File honor, ActionCallbackListener<Void> listener);

    /**
     * @param phone
     * @param name
     * @param sex
     * @param age
     * @param department
     * @param hospital
     * @param special
     * @param experience
     * @param photo
     * @param certification
     * @param level
     * @param honor
     * @param listener
     */
    void changePersonInfo(String phone, String name, String sex, String age,
                          String department, String hospital, String special, String experience,
                          File photo, File certification, File level, File honor, ActionCallbackListener<Void> listener);

    void changeUsername(String phone, ActionCallbackListener<Void> listener);

    /**
     * @param drugName
     * @param listener
     */
    void drugsList(String drugName, ActionCallbackListener<ArrayList<DrugBean>> listener);


    /**
     * @param userId
     * @param page
     * @param listener
     */
    void messageList(String userId, int page, ActionCallbackListener<ArrayList<MessageBean>> listener);

    /**
     * @param userId
     * @param listener
     */
    void myCenter(String userId, ActionCallbackListener<UserBean> listener);

    /**
     * @param listener
     */
    void departmentList(ActionCallbackListener<ArrayList<DepartmentBean>> listener);

    /**
     * @param listener
     */
    void illnessList(ActionCallbackListener<ArrayList<IllnessBean>> listener);

    /**
     * @param listener
     */
    void personInfo(ActionCallbackListener<DoctorBean> listener);

    /**
     * @param departmentId
     * @param illnessId
     * @param phone
     * @param listener
     */
    void generateBindNumber(String departmentId, String illnessId, String phone, ActionCallbackListener<BindNumberBean> listener);

    /**
     * @param phone
     * @param listener
     */
    void sendBindNumber(String phone, ActionCallbackListener<Void> listener);

    /**
     * @param listener
     */
    void myPatients(int page, ActionCallbackListener<ArrayList<PatientBean>> listener);

    /**
     * @param listener
     */
    void getAuthorityStatus(ActionCallbackListener<AuthorityBean> listener);

    /**
     * @param authority 1,可以聊；2，不可以
     * @param listener
     */
    void setAuthorityStatus(String authority, ActionCallbackListener<Void> listener);

    /**
     * @param listener
     */
    void getPriceList(ActionCallbackListener<ArrayList<PriceBean>> listener);

    /**
     * @param one
     * @param three
     * @param six
     * @param twelve
     * @param listener
     */
    void setPriceList(String one, String three, String six, String twelve, ActionCallbackListener<Void> listener);

    /**
     *
     * @param listener
     */
    void getTotalMoney(ActionCallbackListener<TotalMoneyBean> listener);

    /**
     *
     * @param listener
     */
    void withdraw(ActionCallbackListener<Void> listener);

    /**
     * @param type
     * @param listener
     */
    void contactListByType(String type, ActionCallbackListener<ArrayList<ContactBean>> listener);

    /**
     * @param listener
     */
    void contactList(ActionCallbackListener<ArrayList<ContactBean>> listener);

    /**
     * @param patientId
     * @param flag      0,移入；1，移出；
     * @param groupId
     * @param listener
     */
    void groupOption(String patientId, String flag, String groupId, ActionCallbackListener<Void> listener);

    /**
     * @param patientId
     * @param flag      0,移出；3，移入
     * @param listener
     */
    void groupExperienceOption(String patientId, String flag, ActionCallbackListener<Void> listener);

    /**
     * @param groupName
     * @param listener
     */
    void addGroup(String groupName, ActionCallbackListener<Void> listener);

    /**
     * @param listener
     */
    void queryGroup(ActionCallbackListener<ArrayList<ContactGroupBean>> listener);

    /**
     * @param groupId
     * @param listener
     */
    void deleteGroup(String groupId, ActionCallbackListener<Void> listener);

    /**
     * @param patientId
     * @param listener
     */
    void conversationDetail(String patientId, ActionCallbackListener<DischargeSummaryBean> listener);

    void getUserById(String userId, ActionCallbackListener<ContactBean> listener);
    void getContactPayStatus(String contactId, ActionCallbackListener<PayStatusBean> listener);
    void getAllCity(ActionCallbackListener<ArrayList<NetworkCityBean>> listener);

    void checkUpdate(ActionCallbackListener<AppVersionBean> listener);
    void downloadApk(String url, String fileName, ResultCallback<String> callback);
}
