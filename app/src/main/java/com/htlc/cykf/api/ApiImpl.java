
package com.htlc.cykf.api;


import android.text.TextUtils;
import android.util.Pair;

import com.htlc.cykf.api.net.okhttp.callback.ResultCallback;
import com.htlc.cykf.api.net.okhttp.request.OkHttpRequest;
import com.htlc.cykf.api.utils.EncryptUtil;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.util.Constant;
import com.htlc.cykf.app.util.LogUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApiImpl implements Api {


    @Override
    public void sendSmsCode(String username, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", username);
        String url = Api.SendSmsCode;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void register(String username, String password, String smsCode, ResultCallback<ApiResponse<Void>> callback) {
        password = EncryptUtil.makeMD5(password);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", username);
        params.put("pwd", password);
        params.put("verifycode", smsCode);
        params.put("type", "1");
        String url = Api.Register;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void login(String username, String password, ResultCallback<ApiResponse<UserBean>> callback) {
        password = EncryptUtil.makeMD5(password);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", username);
        params.put("pwd", password);
        params.put("type", "1");
        String url = Api.Login;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void forget(String username, String password, String smsCode, ResultCallback<ApiResponse<Void>> callback) {
        password = EncryptUtil.makeMD5(password);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", username);
        params.put("pwd", password);
        params.put("verifycode", smsCode);
        params.put("type", "1");
        String url = Api.Forget;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }


    @Override
    public void postPersonInfo(String userId, String phone, String name, String sex, String age,
                               String department, String hospital, String special, String experience,
                               File photo, File certification, File level, File honor,
                               ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        if (!TextUtils.isEmpty(phone)) {
            params.put("phone", phone);
        }
        if (!TextUtils.isEmpty(name)) {
            params.put("name", name);
        }
        LogUtil.e(this, "sex" + sex);
        if (!TextUtils.isEmpty(sex)) {
            params.put("sex", sex);
        }
        if (!TextUtils.isEmpty(age)) {
            params.put("age", age);
        }
        if (!TextUtils.isEmpty(department)) {
            params.put("department", department);
        }
        if (!TextUtils.isEmpty(hospital)) {
            params.put("hospital", hospital);
        }
        if (!TextUtils.isEmpty(special)) {
            params.put("field", special);
        }
        if (!TextUtils.isEmpty(experience)) {
            params.put("seniority", experience);
        }
        String url = Api.PostPersonInfo;
        LogUtil.e(this, url);
        OkHttpRequest.Builder builder = new OkHttpRequest.Builder().url(url).params(params);
        ArrayList<Pair<String, File>> filesList = new ArrayList<>();
        if (photo != null) {
            filesList.add(new Pair<String, File>("userphoto", photo));
        }
        if (certification != null) {
            filesList.add(new Pair<String, File>("imgone", certification));
        }
        if (level != null) {
            filesList.add(new Pair<String, File>("imgtwo", level));
        }
        if (honor != null) {
            filesList.add(new Pair<String, File>("imgthree", honor));
        }
        Pair<String, File>[] filesArray = new Pair[filesList.size()];
        for(int i=0; i<filesArray.length; i++){
            filesArray[i] = filesList.get(i);
        }
        builder.files(filesArray);
        builder.upload(callback);
    }

    /**
     * 获取药品列表，或药品搜索
     *
     * @param drugName
     * @param callback
     */
    @Override
    public void drugsList(String drugName, ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        if (!TextUtils.isEmpty(drugName)) {
            params.put("name", drugName);
        }
        String url = Api.DrugsList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void messageList(String userId, String page, ResultCallback<ApiResponse<MessageBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);

        params.put("userid", userId);
        params.put("flag", "1");
        params.put("num", page);
        String url = Api.MessageList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void myCenter(String userId, ResultCallback<ApiResponse<UserBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);

        params.put("userid", userId);
        String url = Api.MyCenter;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void departmentList(ResultCallback<ApiResponse<DepartmentBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.DepartmentList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void illnessList(ResultCallback<ApiResponse<IllnessBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.IllnessList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void personInfo(ResultCallback<ApiResponse<DoctorBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.PersonInfo;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void generateBindNumber(String department, String illness, String phone,ResultCallback<ApiResponse<BindNumberBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("subject", department);
        params.put("disease", illness);
        params.put("phone", phone);
        String url = Api.GenerateBindNumber;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void sendBindNumber(String phone, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("phone", phone);
        String url = Api.SendBindNumber;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void myPatients(String page,ResultCallback<ApiResponse<PatientBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("num", page);
        String url = Api.MyPatients;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getAuthorityStatus(ResultCallback<ApiResponse<AuthorityBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.GetAuthorityStatus;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void setAuthorityStatus(String authority, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        LogUtil.e(this, "authority:" + authority);
        params.put("flag", authority);
        String url = Api.SetAuthorityStatus;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getPriceList(ResultCallback<ApiResponse<PriceBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("doctorid", TextUtils.isEmpty(token) ? "" : userId);
        String url = Api.GetPriceList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void setPriceList(String one, String three, String six, String twelve, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("one", one);
        params.put("three", three);
        params.put("six", six);
        params.put("ten", twelve);
        String url = Api.SetPriceList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getTotalMoney(ResultCallback<ApiResponse<TotalMoneyBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.GetTotalMoney;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void withdraw(ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token) ? "" : token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.Withdraw;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void contactListByType(String type, ResultCallback<ApiResponse<ContactBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("flag", "1");
        params.put("type", type);
        String url = Api.ContactListByType;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void contactList(ResultCallback<ApiResponse<ContactBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.ContactList;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void groupOption(String patientId, String flag, String groupId, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("flag", flag);
        params.put("patient", patientId);
        params.put("groupid", groupId);
        String url = Api.GroupOption;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void groupExperienceOption(String patientId, String flag, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("flag", flag);
        params.put("patient", patientId);
        String url = Api.GroupExperienceOption;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void addGroup(String groupName, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("name", groupName);
        String url = Api.AddGroup;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void queryGroup(ResultCallback<ApiResponse<ContactGroupBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        String url = Api.QueryGroup;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void deleteGroup(String groupId, ResultCallback<ApiResponse<Void>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("groupid", groupId);
        String url = Api.DeleteGroup;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void conversationDetail(String patientId, ResultCallback<ApiResponse<DischargeSummaryBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("patient", patientId);
        String url = Api.ConversationDetail;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getContactById(String contactId, ResultCallback<ApiResponse<ContactBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("id", contactId);
        String url = Api.GetContactById;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getContactPayStatus(String contactId, ResultCallback<ApiResponse<PayStatusBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();

        String token = App.app.getUserBean().token;
        params.put("token", TextUtils.isEmpty(token)?"":token);
        String userId = App.app.getUserBean().userid;
        params.put("userid", TextUtils.isEmpty(token) ? "" : userId);

        params.put("patient", contactId);
        String url = Api.GetContactPayStatus;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void getAllCity(String lastModifyData, ResultCallback<ApiResponse<UpdateCityBean>> callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", lastModifyData);
        String url = Api.GetAllCity;
        LogUtil.e(this, url);
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void checkUpdate(ResultCallback<String> callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("shortcut", Constant.PGY_SHORT_URL);
        params.put("_api_key", Constant.PGY_API_KEY);
        String url = Api.CheckUpdate;
        new OkHttpRequest.Builder().url(url).params(params).post(callback);
    }

    @Override
    public void downloadApk(String url, String dir, String fileName, ResultCallback<String> callback) {
        new OkHttpRequest.Builder().url(url).destFileDir(dir).destFileName(fileName).download(callback);
    }
}
