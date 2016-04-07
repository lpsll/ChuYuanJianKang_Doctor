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
import com.htlc.cykf.model.DoctorBean;
import com.htlc.cykf.model.UserBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sks on 2016/2/15.
 */
public class PersonActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextLeft, mTextRight;
    private View mImageBack;
    private ImageView mImageHead, mImageCertification, mImageLevel, mImageHonor;
    private EditText mEditGender, mEditDepartment, mEditHospital, mEditUsername, mEditSpeciality, mEditExperience;
    private EditText mEditName, mEditAge;
    private boolean isEditable = false;

    private PickPhotoDialog mPickPhotoDialog;
    private OptionsPickerView mPickViePwOptions;
    private ArrayList<String> genders = new ArrayList<>();
    private DoctorBean mPersonBean;

    private File mImageHeadFile;
    private ImageView currentImageView;
    private File mImageCertificationFile;
    private File mImageLevelFile;
    private File mImageHonorFile;
    private boolean isFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        setupView();
    }

    private void setupView() {
        mImageBack = findViewById(R.id.imageBack);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextLeft = (TextView) findViewById(R.id.textLeft);
        mTextLeft.setOnClickListener(this);
        mTextRight = (TextView) findViewById(R.id.textRight);
        mTextRight.setOnClickListener(this);
        mImageHead = (ImageView) findViewById(R.id.imageHead);
        mImageHead.setOnClickListener(this);

        mEditName = (EditText) findViewById(R.id.editName);
        mEditAge = (EditText) findViewById(R.id.editAge);

        mEditDepartment = (EditText) findViewById(R.id.editDepartment);
        mEditHospital = (EditText) findViewById(R.id.editHospital);
        mEditSpeciality = (EditText) findViewById(R.id.editSpeciality);
        mEditExperience = (EditText) findViewById(R.id.editExperience);
        mImageCertification = (ImageView) findViewById(R.id.imageCertification);
        mImageLevel = (ImageView) findViewById(R.id.imageLevel);
        mImageHonor = (ImageView) findViewById(R.id.imageHonor);

        mImageCertification.setOnClickListener(this);
        mImageLevel.setOnClickListener(this);
        mImageHonor.setOnClickListener(this);
        mEditGender = (EditText) findViewById(R.id.editGender);
        mEditGender.setOnClickListener(this);
        mEditUsername = (EditText) findViewById(R.id.editUsername);
        mEditUsername.setOnClickListener(this);


        refreshView();
        getPersonInfo();
    }

    private void refreshView() {
        UserBean bean = application.getUserBean();
        ImageLoader.getInstance().displayImage(bean.photo, mImageHead);
        mEditName.setText(bean.name);
        mEditUsername.setText(bean.username);
        if (mPersonBean != null) {
            mEditName.setText(mPersonBean.name);
            if ("1".equals(mPersonBean.sex)) {
                mEditGender.setText("女");
            } else {
                mEditGender.setText("男");
            }
            mEditAge.setText(mPersonBean.age);
            mEditDepartment.setText(mPersonBean.department);
            mEditHospital.setText(mPersonBean.hospital);
            mEditSpeciality.setText(mPersonBean.field);
            mEditExperience.setText(mPersonBean.seniority);
            ImageLoader.getInstance().displayImage(mPersonBean.imgone, mImageCertification);
            ImageLoader.getInstance().displayImage(mPersonBean.imgtwo, mImageLevel);
            ImageLoader.getInstance().displayImage(mPersonBean.imgthree, mImageHonor);
        }
    }

    @Override
    public void onClick(View v) {
        LogUtil.e(this, v.getId() + ";");
        switch (v.getId()) {
            case R.id.textLeft:
                changeEditStatus();
                break;
            case R.id.textRight:
                if(isEditable){
                    postPersonInfo();
                }else {
                    changeEditStatus();
                }
                break;
            case R.id.imageHead:
                currentImageView = mImageHead;
                showPickPhotoDialog();
                break;
            case R.id.imageCertification:
//                currentImageView = mImageCertification;
//                showPickPhotoDialog();
                break;
            case R.id.imageLevel:
                currentImageView = mImageLevel;
                showPickPhotoDialog();
                break;
            case R.id.imageHonor:
                currentImageView = mImageHonor;
                showPickPhotoDialog();
                break;
            case R.id.editGender:
                selectGender();
                break;
            case R.id.editUsername:
                goChangeUsername();
                break;
        }
    }

    private void goChangeUsername() {
        Intent intent = new Intent(this, ChangeUsernameActivity.class);
        startActivity(intent);
    }


    private void selectGender() {
        if (!isEditable) return;
        //选项选择器
        mPickViePwOptions = new OptionsPickerView(this);
        genders.clear();
        String[] genderArray = {"男", "女"};
        for (int i = 0; i < genderArray.length; i++) {
            genders.add(genderArray[i]);
        }
        //三级不联动效果  false
        mPickViePwOptions.setPicker(genders);
        mPickViePwOptions.setCyclic(false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        mPickViePwOptions.setSelectOptions(0);
        mPickViePwOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                String genderStr = genders.get(options1);
                mEditGender.setText(genderStr);
                if (genderStr.equals("男")) {
                    isFemale = false;
                } else {
                    isFemale = true;
                }
            }
        });
        mPickViePwOptions.show();

    }

    private void changeEditStatus() {
        if (isEditable) {
            mEditName.setEnabled(false);
            mEditAge.setEnabled(false);
            mEditExperience.setEnabled(false);
            mEditSpeciality.setEnabled(false);

            mTextRight.setText("修改");
            mTextLeft.setVisibility(View.GONE);
            mImageBack.setVisibility(View.VISIBLE);

            mImageCertificationFile = null;
            mImageLevelFile = null;
            mImageHonorFile = null;
            refreshView();
        } else {
            mEditSpeciality.setEnabled(true);
            mEditSpeciality.setFocusable(true);
            mEditSpeciality.setFocusableInTouchMode(true);
            mEditSpeciality.requestFocus();

            mEditExperience.setEnabled(true);
            mEditExperience.setFocusable(true);
            mEditExperience.setFocusableInTouchMode(true);
            mEditExperience.requestFocus();

            mEditAge.setEnabled(true);
            mEditAge.setFocusable(true);
            mEditAge.setFocusableInTouchMode(true);
            mEditAge.requestFocus();

            mEditName.setEnabled(true);
            mEditName.setFocusable(true);
            mEditName.setFocusableInTouchMode(true);
            mEditName.requestFocus();

            mTextRight.setText("保存");
            mTextLeft.setVisibility(View.VISIBLE);
            mImageBack.setVisibility(View.GONE);
        }
        isEditable = !isEditable;
    }

    private void postPersonInfo() {
        String name = mEditName.getText().toString().trim();
        String age = mEditAge.getText().toString();
        String special = mEditSpeciality.getText().toString().trim();
        String experience = mEditExperience.getText().toString().trim();

        appAction.changePersonInfo("", name, isFemale ? "1" : "0", age, "", "", special, experience, mImageHeadFile, mImageCertificationFile, mImageLevelFile, mImageHonorFile, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                getPersonInfo();
                changeEditStatus();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
            }
        });
    }

    public void getPersonInfo() {
        appAction.personInfo(new ActionCallbackListener<DoctorBean>() {
            @Override
            public void onSuccess(DoctorBean data) {
                mPersonBean = data;
                refreshView();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if(handleNetworkOnFailure(errorEvent, message)) return;
                ToastUtil.showToast(App.app, message);
            }
        });
    }


    /**
     * 选择图片对话框
     */
    private void showPickPhotoDialog() {
        if (!isEditable) return;
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
        if (currentImageView == mImageCertification) {
            mImageCertificationFile = imageFile;
        } else if (currentImageView == mImageLevel) {
            mImageLevelFile = imageFile;
        } else if (currentImageView == mImageHonor) {
            mImageHonorFile = imageFile;
        } else if (currentImageView == mImageHead) {
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
                if (currentImageView == mImageHead) {
                    startPhotoZoom(data.getData());
                } else {
                    String imagePath = getRealImagePath(data);
                    saveSmallImage(imagePath);
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                if (Activity.RESULT_OK != resultCode) return;
                if (currentImageView == mImageHead) {
                    startPhotoZoom(Uri.fromFile(mImageHeadFile));
                } else if (currentImageView == mImageCertification) {
                    saveSmallImage(mImageCertificationFile.getAbsolutePath());
                } else if (currentImageView == mImageLevel) {
                    saveSmallImage(mImageLevelFile.getAbsolutePath());
                } else if (currentImageView == mImageHonor) {
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
                if (currentImageView == mImageCertification) {
                    mImageCertificationFile = imageFile;
                    mImageCertification.setImageBitmap(bm);
                } else if (currentImageView == mImageLevel) {
                    mImageLevelFile = imageFile;
                    mImageLevel.setImageBitmap(bm);
                } else if (currentImageView == mImageHonor) {
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
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

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
