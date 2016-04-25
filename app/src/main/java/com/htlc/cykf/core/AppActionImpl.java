
package com.htlc.cykf.core;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.htlc.cykf.R;
import com.htlc.cykf.api.Api;
import com.htlc.cykf.api.ApiImpl;
import com.htlc.cykf.api.ApiResponse;
import com.htlc.cykf.api.net.okhttp.callback.ResultCallback;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.db.DbManager;
import com.htlc.cykf.app.util.CommonUtil;
import com.htlc.cykf.app.util.Constant;
import com.htlc.cykf.app.util.JsonUtil;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.RegExUtil;
import com.htlc.cykf.app.util.SharedPreferenceUtil;
import com.htlc.cykf.model.AuthorityBean;
import com.htlc.cykf.model.BindNumberBean;
import com.htlc.cykf.model.ContactBean;
import com.htlc.cykf.model.ContactGroupBean;
import com.htlc.cykf.model.DepartmentBean;
import com.htlc.cykf.model.DischargeSummaryBean;
import com.htlc.cykf.model.DoctorBean;
import com.htlc.cykf.model.DrugBean;
import com.htlc.cykf.model.IllnessBean;
import com.htlc.cykf.model.MessageBean;
import com.htlc.cykf.model.NetworkCityBean;
import com.htlc.cykf.model.PatientBean;
import com.htlc.cykf.model.PriceBean;
import com.htlc.cykf.model.TotalMoneyBean;
import com.htlc.cykf.model.UpdateCityBean;
import com.htlc.cykf.model.UserBean;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppActionImpl implements AppAction {

    private static final String df = "vnv";
    private final static int PAGE_SIZE = 20; // 默认每页20条

    private Context context;
    private Api api;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void sendSmsCode(final String username, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        // 请求Api
        api.sendSmsCode(username, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });

    }

    @Override
    public void register(final String username, final String code, final String password, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        // 请求Api
        api.register(username, password, code, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });

    }

    @Override
    public void login(final String username, final String password, final ActionCallbackListener<UserBean> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "登录名不能为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            }
            return;
        }
        // 请求Api
        api.login(username, password, new ResultCallback<ApiResponse<UserBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<UserBean> response) {
                if ("1".equals(response.code)) {
                    LogUtil.e(AppActionImpl.this, "注册状态：" + response.data.flag);
                    listener.onSuccess(response.data);
                    App.app.setUserBean(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void forget(String username, String code, String password, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "验证码不能为空");
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码不能为空");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(username)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        // 请求Api
        api.forget(username, password, code, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void postPersonInfo(String userId, String phone, String name, String sex, String age, String department, String hospital, String special, String experience, File photo, File certification, File level, File honor, final ActionCallbackListener<Void> listener) {
        LogUtil.e(this,name+"");
        if (TextUtils.isEmpty(name)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入姓名");
            }
            return;
        }
        if (TextUtils.isEmpty(age)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入年龄");
            }
            return;
        }
        if (TextUtils.isEmpty(department)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入科室");
            }
            return;
        }
        if (TextUtils.isEmpty(hospital)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入医院");
            }
            return;
        }
        if (certification == null) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请选择执业医师资格证");
            }
            return;
        }
        api.postPersonInfo(userId, phone, name, sex, age,
                department, hospital, special, experience,
                photo, certification, level, honor, new ResultCallback<ApiResponse<Void>>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();

                        listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
                    }

                    @Override
                    public void onResponse(ApiResponse<Void> response) {
                        if ("1".equals(response.code)) {
                            listener.onSuccess(null);
                        } else {
                            listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                        }
                    }
                });
    }

    @Override
    public void changePersonInfo(String phone, String name, String sex, String age, String department, String hospital, String special, String experience, File photo, File certification, File level, File honor, final ActionCallbackListener<Void> listener) {
        api.postPersonInfo("", phone, name, sex, age, department, hospital, special, experience, photo, certification, level, honor, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void changeUsername(String phone, final ActionCallbackListener<Void> listener) {
        // 参数检查
        if (TextUtils.isEmpty(phone)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(phone)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        api.postPersonInfo("", phone, "", "", "", "", "", "", "", null, null, null, null, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();

                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(null);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }


    @Override
    public void drugsList(String drugName, final ActionCallbackListener<ArrayList<DrugBean>> listener) {
        api.drugsList(drugName, new ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(String response) {
                LogUtil.e(AppActionImpl.this, response);
                try {
                    JSONObject responseObj = new JSONObject(response);
                    String code = responseObj.getString("code");
                    if (listener != null) {
                        if (code.equals(Constant.REQUEST_ERROR)) {
                            String msg = responseObj.getString("msg");
                            listener.onFailure(ErrorEvent.RESULT_ILLEGAL, msg);
                        } else if (code.equals(Constant.REQUEST_SUCCESS)) {
                            ArrayList<DrugBean> list = new ArrayList<>();
                            String data = responseObj.getString("data");
                            JSONObject jsonObject = new JSONObject(data);
                            Iterator<String> iterator = jsonObject.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                String value = jsonObject.getString(key);
                                List<DrugBean> temp = (List<DrugBean>) JsonUtil.parseJsonToList(value, new TypeToken<List<DrugBean>>() {
                                }.getType());
                                for (int i = 0; i < temp.size(); i++) {
                                    temp.get(i).group = key;
                                }
                                list.addAll(temp);
                            }
                            Collections.sort(list);
                            listener.onSuccess(list);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
                }

            }
        });
    }

    @Override
    public void messageList(String userId, int page, final ActionCallbackListener<ArrayList<MessageBean>> listener) {
        api.messageList(userId, page + "", new ResultCallback<ApiResponse<MessageBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<MessageBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void myCenter(String userId, final ActionCallbackListener<UserBean> listener) {
        api.myCenter(userId, new ResultCallback<ApiResponse<UserBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<UserBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void departmentList(final ActionCallbackListener<ArrayList<DepartmentBean>> listener) {
        api.departmentList(new ResultCallback<ApiResponse<DepartmentBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<DepartmentBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void illnessList(final ActionCallbackListener<ArrayList<IllnessBean>> listener) {
        api.illnessList(new ResultCallback<ApiResponse<IllnessBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<IllnessBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void personInfo(final ActionCallbackListener<DoctorBean> listener) {
        api.personInfo(new ResultCallback<ApiResponse<DoctorBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<DoctorBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void generateBindNumber(String department, String illness, String phone, final ActionCallbackListener<BindNumberBean> listener) {
        if(TextUtils.isEmpty(illness)){
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请选择疾病");
            }
            return;
        }
        if(TextUtils.isEmpty(phone)){
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入患者手机号");
            }
            return;
        }
        if (!RegExUtil.matcherPhoneNumber(phone)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        api.generateBindNumber(department, illness, phone, new ResultCallback<ApiResponse<BindNumberBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<BindNumberBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void sendBindNumber(String phone, final ActionCallbackListener<Void> listener) {
        api.sendBindNumber(phone, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void myPatients(int page, final ActionCallbackListener<ArrayList<PatientBean>> listener) {
        api.myPatients(page + "", new ResultCallback<ApiResponse<PatientBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<PatientBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getAuthorityStatus(final ActionCallbackListener<AuthorityBean> listener) {
        api.getAuthorityStatus(new ResultCallback<ApiResponse<AuthorityBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();

                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<AuthorityBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void setAuthorityStatus(String authority, final ActionCallbackListener<Void> listener) {
        api.setAuthorityStatus(authority, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();

                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getPriceList(final ActionCallbackListener<ArrayList<PriceBean>> listener) {
        api.getPriceList(new ResultCallback<ApiResponse<PriceBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<PriceBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void setPriceList(String one, String three, String six, String twelve, final ActionCallbackListener<Void> listener) {
        api.setPriceList(one, three, six, twelve, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getTotalMoney(final ActionCallbackListener<TotalMoneyBean> listener) {
        api.getTotalMoney(new ResultCallback<ApiResponse<TotalMoneyBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<TotalMoneyBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void withdraw(final ActionCallbackListener<Void> listener) {
        api.withdraw(new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void contactListByType(String type, final ActionCallbackListener<ArrayList<ContactBean>> listener) {
        api.contactListByType(type, new ResultCallback<ApiResponse<ContactBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<ContactBean> response) {
                Collections.sort(response.dataArray);
                listener.onSuccess(response.dataArray);
            }
        });
    }

    @Override
    public void contactList(final ActionCallbackListener<ArrayList<ContactBean>> listener) {
        api.contactList(new ResultCallback<ApiResponse<ContactBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                LogUtil.e(this, request.body().toString());
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<ContactBean> response) {
                listener.onSuccess(response.dataArray);
            }
        });
    }

    @Override
    public void groupOption(String patientId, String flag, String groupId, final ActionCallbackListener<Void> listener) {
        api.groupOption(patientId, flag, groupId, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();

                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void groupExperienceOption(String patientId, String flag, final ActionCallbackListener<Void> listener) {
        api.groupExperienceOption(patientId, flag, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void addGroup(String groupName, final ActionCallbackListener<Void> listener) {
        if(TextUtils.isEmpty(groupName)){
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "请输入分组名");
            }
            return;
        }
        api.addGroup(groupName, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();

                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void queryGroup(final ActionCallbackListener<ArrayList<ContactGroupBean>> listener) {
        api.queryGroup(new ResultCallback<ApiResponse<ContactGroupBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();

                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<ContactGroupBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.dataArray);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void deleteGroup(String groupId, final ActionCallbackListener<Void> listener) {
        api.deleteGroup(groupId, new ResultCallback<ApiResponse<Void>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<Void> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void conversationDetail(String patientId, final ActionCallbackListener<DischargeSummaryBean> listener) {
        api.conversationDetail(patientId, new ResultCallback<ApiResponse<DischargeSummaryBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<DischargeSummaryBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getUserById(String userId, final ActionCallbackListener<ContactBean> listener) {
        api.getContactById(userId, new ResultCallback<ApiResponse<ContactBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<ContactBean> response) {
                if ("1".equals(response.code)) {
                    listener.onSuccess(response.data);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }

    @Override
    public void getAllCity(final ActionCallbackListener<ArrayList<NetworkCityBean>> listener) {
        String lastModifyDate = SharedPreferenceUtil.getString(App.app, DbManager.DATABASE_LAST_MODIFY, "0");
        LogUtil.e(this,lastModifyDate);
        api.getAllCity(lastModifyDate, new ResultCallback<ApiResponse<UpdateCityBean>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                listener.onFailure(ErrorEvent.NETWORK_ERROR, CommonUtil.getResourceString(R.string.common_network_error));
            }

            @Override
            public void onResponse(ApiResponse<UpdateCityBean> response) {
                if ("1".equals(response.code)) {
                    SharedPreferenceUtil.putString(App.app, DbManager.DATABASE_LAST_MODIFY, response.data.update);
                    listener.onSuccess(response.data.city);
                } else {
                    listener.onFailure(ErrorEvent.RESULT_ILLEGAL, response.msg);
                }
            }
        });
    }
}
