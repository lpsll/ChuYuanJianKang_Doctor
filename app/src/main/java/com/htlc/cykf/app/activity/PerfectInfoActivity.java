package com.htlc.cykf.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.htlc.cykf.R;
import com.htlc.cykf.app.App;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.app.util.PictureUtil;
import com.htlc.cykf.app.util.ToastUtil;
import com.htlc.cykf.app.widget.PickPhotoDialog;
import com.htlc.cykf.core.ActionCallbackListener;
import com.htlc.cykf.model.DepartmentBean;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sks on 2016/2/15.
 */
public class PerfectInfoActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View mImageBack;

    private ImageView mImageHead, mImageCertification, mImageLevel, mImageHonor;
    private RadioGroup mRadioGroup;
    private TextView mTextButtonFinish,mTextDepartment;
    private EditText mEditUsername, mEditAge, mEditName, mEditHospital, mEditSpeciality, mEditExperience;
    private PickPhotoDialog mPickPhotoDialog;

    private OptionsPickerView mPickViePwOptions;
    private ArrayList<DepartmentBean> mDepartments = new ArrayList<>();

    private boolean isFemale;
    private File mImageHeadFile;
    private ImageView currentImageView;
    private File mImageCertificationFile;
    private File mImageLevelFile;
    private File mImageHonorFile;
    private String mDepartmentId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.e(this, "onCreate start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_info);
        setupView();
        LogUtil.e(this, "onCreate finish");
    }

    private void setupView() {
        mImageBack = findViewById(R.id.imageBack);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditName = (EditText) findViewById(R.id.editName);
        mEditUsername = (EditText) findViewById(R.id.editUsername);
        mEditUsername.setText(application.getUserBean().username);
        mEditAge = (EditText) findViewById(R.id.editAge);
        mEditHospital = (EditText) findViewById(R.id.editHospital);
        mEditSpeciality = (EditText) findViewById(R.id.editSpeciality);
        mEditExperience = (EditText) findViewById(R.id.editExperience);

        mImageHead = (ImageView) findViewById(R.id.imageHead);
        mImageCertification = (ImageView) findViewById(R.id.imageCertification);
        mImageLevel = (ImageView) findViewById(R.id.imageLevel);
        mImageHonor = (ImageView) findViewById(R.id.imageHonor);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mTextDepartment = (TextView) findViewById(R.id.textDepartment);
        mTextButtonFinish = (TextView) findViewById(R.id.textButtonFinish);


        mImageHead.setOnClickListener(this);
        mImageCertification.setOnClickListener(this);
        mImageLevel.setOnClickListener(this);
        mImageHonor.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        mTextDepartment.setOnClickListener(this);
        mTextButtonFinish.setOnClickListener(this);

        getDepartmentList();
    }

    private void getDepartmentList() {
        appAction.departmentList(new ActionCallbackListener<ArrayList<DepartmentBean>>() {
            @Override
            public void onSuccess(ArrayList<DepartmentBean> data) {
                mDepartments.clear();
                mDepartments.addAll(data);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                LogUtil.e(PerfectInfoActivity.this,message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageHead:
                LogUtil.e(this, "imageHead");
                currentImageView = mImageHead;
                showPickPhotoDialog();
                break;
            case R.id.imageCertification:
                LogUtil.e(this, "imageCertification");
                currentImageView = mImageCertification;
                showPickPhotoDialog();
                break;
            case R.id.imageLevel:
                LogUtil.e(this, "imageLevel");
                currentImageView = mImageLevel;
                showPickPhotoDialog();
                break;
            case R.id.imageHonor:
                LogUtil.e(this, "imageHonor");
                currentImageView = mImageHonor;
                showPickPhotoDialog();
                break;
            case R.id.textDepartment:
                LogUtil.e(this, "textDepartment");
                selectDepartment();
                break;
            case R.id.textButtonFinish:
                LogUtil.e(this, "textButtonFinish");
                commit();
                break;
        }
    }

    /**
     * 提交数据
     */
    private void commit() {
        String name = mEditName.getText().toString().trim();
        String age = mEditAge.getText().toString().trim();
        String userId = application.getUserBean().userid;
        String username = application.getUserBean().username;
        String hospital = mEditHospital.getText().toString().trim();
        String speciality = mEditSpeciality.getText().toString().trim();
        String experience = mEditExperience.getText().toString().trim();
        showProgressHUD();
        LogUtil.e(this,"是女？"+isFemale);
        appAction.postPersonInfo(userId, username, name, isFemale ? "1" : "0", age,
                mDepartmentId, hospital, speciality, experience,
                mImageHeadFile, mImageCertificationFile, mImageLevelFile, mImageHonorFile,
                new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        dismissProgressHUD();
                        LogUtil.e(PerfectInfoActivity.this, "提交成功！");
                        showMessageDialog();
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        dismissProgressHUD();
                        if(handleNetworkOnFailure(errorEvent, message)) return;
                        ToastUtil.showToast(App.app, message);

                    }
                });
    }

    private void showMessageDialog() {
        View view =  View.inflate(this,R.layout.dialog_message,null);
        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText("提交成功\n等待审核");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(PerfectInfoActivity.this,null);
                finish();
            }
        });
        showTipsDialog(view, 300, 150, false);
    }

    private void selectDepartment(){
        if(mDepartments.size()<1){
            appAction.departmentList(new ActionCallbackListener<ArrayList<DepartmentBean>>() {
                @Override
                public void onSuccess(ArrayList<DepartmentBean> data) {
                    mDepartments.clear();
                    mDepartments.addAll(data);
                    selectDepartment();
                }

                @Override
                public void onFailure(String errorEvent, String message) {
                    if(handleNetworkOnFailure(errorEvent, message)) return;
                    LogUtil.e(PerfectInfoActivity.this,message);
                }
            });
        }
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(this);
        //三级不联动效果  false
        mPickViePwOptions.setPicker(mDepartments);
        mPickViePwOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                mTextDepartment.setText(mDepartments.get(options1).subject);
                mDepartmentId = mDepartments.get(options1).id;
            }
        });
        mPickViePwOptions.show();
    }

    private void goMain() {
        MainActivity.start(this,null);
        finish();
    }

    /**
     * 性别选择
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radioButton0) {
            isFemale = false;
        }
        if (checkedId == R.id.radioButton1) {
            isFemale = true;
        }
    }


    /**
     * 选择图片对话框
     */
    private void showPickPhotoDialog() {

        mPickPhotoDialog = new PickPhotoDialog(this, R.style.TransparentAlertDialog);//创建Dialog并设置样式主题
        mPickPhotoDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        mPickPhotoDialog.show();
        mPickPhotoDialog.textAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
                pickPhotoByAlbum();

            }
        });
        mPickPhotoDialog.textCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
                takePhoto();
            }
        });
        mPickPhotoDialog.textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
            }
        });

        Window win = mPickPhotoDialog.getWindow();

        WindowManager.LayoutParams params = win.getAttributes();
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

    }

    private void takePhoto() {

        /**
         * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
         * 文档，you_sdk_path/docs/guide/topics/media/camera.html
         * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
         * 官方文档太长了就不看了，其实是错的
         */
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File imageFile = new File(PictureUtil.getAlbumDir(), "IMG_" + filename + ".jpg");
        if(currentImageView==mImageCertification){
            mImageCertificationFile = imageFile;
        }else if(currentImageView ==mImageLevel){
            mImageLevelFile = imageFile;
        }else if(currentImageView == mImageHonor){
            mImageHonorFile = imageFile;
        }else if (currentImageView == mImageHead){
            mImageHeadFile = imageFile;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(intent, 2);

    }

    private void pickPhotoByAlbum() {
        /**
         * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
         * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
         */
        Intent intent = new Intent(Intent.ACTION_PICK, null);


        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images
         * .Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如
         * ："image/jpeg 、 image/png等的类型"
         */
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (Activity.RESULT_OK != resultCode) return;
                if(currentImageView == mImageHead){
                    startPhotoZoom(data.getData());
                }else {
                   String imagePath = getRealImagePath(data);
                    saveSmallImage(imagePath);
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                if (Activity.RESULT_OK != resultCode) return;
                if(currentImageView == mImageHead){
                    startPhotoZoom(Uri.fromFile(mImageHeadFile));
                }else if(currentImageView == mImageCertification){
                    saveSmallImage(mImageCertificationFile.getAbsolutePath());
                }else if(currentImageView == mImageLevel){
                    saveSmallImage(mImageLevelFile.getAbsolutePath());
                }else if(currentImageView == mImageHonor){
                    saveSmallImage(mImageHonorFile.getAbsolutePath());
                }
                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
                 *
                 */
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveSmallImage(String imagePath) {
        if (imagePath != null) {
            try {
                File f = new File(imagePath);
                Bitmap bm = PictureUtil.getSmallBitmap(imagePath, 1024);
                File imageFile = new File(PictureUtil.getAlbumDir(), "small_" + f.getName());
                if(currentImageView==mImageCertification){
                    mImageCertificationFile = imageFile;
                    mImageCertification.setImageBitmap(bm);
                }else if(currentImageView ==mImageLevel){
                    mImageLevelFile = imageFile;
                    mImageLevel.setImageBitmap(bm);
                }else if(currentImageView == mImageHonor){
                    mImageHonorFile = imageFile;
                    mImageHonor.setImageBitmap(bm);
                }
                FileOutputStream fos = new FileOutputStream(imageFile);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("PerfectInfoActivity", "error", e);
            }
        }
    }

    private String getRealImagePath(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */


    public void startPhotoZoom(Uri uri) {
 /*
 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
 * yourself_sdk_path/docs/reference/android/content/Intent.html
 * 直接在里面Ctrl+F搜：CROP ，之前没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        // 下面这句指定调用相机拍照后的照片存储的路径
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        mImageHeadFile = new File(Environment.getExternalStorageDirectory(), "IMG_" + filename + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageHeadFile));

        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            final Bitmap photo = extras.getParcelable("data");
            mImageHead.setImageBitmap(photo);
        }

    }
}
