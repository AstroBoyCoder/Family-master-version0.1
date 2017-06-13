package com.example.astroboy.family_master_version01.View.FamilyManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.astroboy.family.GreenDao.Family;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_userLogin;
import com.example.astroboy.family_master_version01.Util.ImagePickerView.GlideImageLoader;
import com.example.astroboy.family_master_version01.Util.ImagePickerView.ImagePickerAdapter;
import com.example.astroboy.family_master_version01.View.MainPageComponents.Family_Fragment_Switcher_Activity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Family_manage_create extends AppCompatActivity implements View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener,BDLocationListener {

    private final static String TAG = "Family_manage_create";

    private String reCode = "";
    List<Family> families;

    ImageButton createBack;
    LinearLayout sent_essay;
    EditText publishContent;
    Spinner selectFamilySpinner;

    EditText familyName,familyIntro,familyPhone,familyAddress;
    RadioGroup family_type;
    //RadioButton
    LinearLayout create_family_ly;

    private Handler mHandler;
    private LocationClient mLocationClient;

    final Map<ArrayList<ImageItem>, String> map = new HashMap<>();
    private Integer requestCode = 100;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;               //允许选择图片最大数

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_manage_create_activity);

        init();
    }

    private void init() {
        createBack = (ImageButton) findViewById(R.id.createFamily_back);
        familyName = (EditText) findViewById(R.id.create_familyName);
        familyIntro = (EditText) findViewById(R.id.create_familyIntro);
        familyPhone = (EditText) findViewById(R.id.create_familyPhone);
        familyAddress = (EditText) findViewById(R.id.create_familyAddress);
        create_family_ly = (LinearLayout) findViewById(R.id.create_family_ly);
        initLocation();
        create_family_ly.setOnClickListener(this);
        createBack.setOnClickListener(this);
        initImagePicker();
        initWidget();
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        setLocationOption();
        mLocationClient.registerLocationListener(this);
        mLocationClient.start();
    }

    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开gps
        //option.setEnableSimulateGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_family_ly:
                analyzeInfoValidToPost();
                break;
            case R.id.createFamily_back:
                onBackPressed();
                break;
        }
    }

    private void analyzeInfoValidToPost() {
        String name = familyName.getText().toString();
        String intro = familyIntro.getText().toString();
        String phone = familyIntro.getText().toString();
        String address = familyAddress.getText().toString();
        Boolean existImg = selImageList.isEmpty();
        if (name.equals("")||phone.equals("")||address.equals("")){
            if (name.equals("")) familyName.setError("必填");
            if (address.equals("")) familyAddress.setError("必填");
            if (phone.equals("")) familyIntro.setError("必填");
        }else {
            post_family_to_server(Family_Fragment_Switcher_Activity.UID,name,address,phone,intro);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selImageList.addAll(images);
                adapter.setImages(selImageList);
                if (selImageList.isEmpty()) {
                    setImageItemsRequest(100);
                } else {
                    setImageItemsRequest(200);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                setImageItemsRequest(200);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
                if (selImageList.isEmpty()) {
                    setImageItemsRequest(100);
                } else {
                    setImageItemsRequest(200);
                }
            }
        }
    }

    public Integer getImageItemsRequest() {
        return requestCode;
    }

    public void setImageItemsRequest(Integer requestCode) {
        this.requestCode = requestCode;
    }

    /*private void displayDialogThread() {
        *//*ProgressDialog dialog = ProgressDialog.show(Xel_mine_feedback.this, "上传中...", "正在将令状上诉至衙门，请稍后！");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);*//*
        final AlertDialog.Builder builder = new AlertDialog.Builder(Family_manage_create.this);
        Boolean existImg = selImageList.isEmpty();
        String content = publishContent.getText().toString().trim();
        int selectedItems = selectFamilySpinner.getSelectedItemPosition();
        Log.d(TAG,content+" "+selectedItems);
        if (content.equals("") || content.length() > 300 || selectedItems==0) {
            if (content.equals("") || content.length() > 300) {
                builder.setTitle("内容不符合要求")
                        .setMessage("返回重新输入")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                publishContent.setText("");
                                dialog.dismiss();
                            }
                        }).show();
            }
            if (selectedItems==0) {
                Toast.makeText(this,"请选择要发布到的家庭",Toast.LENGTH_SHORT).show();
            }
        } else {
            int FM_ID = families.get(selectedItems-1).getFM_ID();
            int Member_ID = families.get(selectedItems-1).getMember_ID();
            post_essay_to_server(content,FM_ID,Member_ID);
        }
    }*/

    private void post_family_to_server(final String UID, final String family_name,final String family_address,final String phone,final String intro) {
        post_essay_to_Handler();
        final Integer req = getImageItemsRequest();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> data = new HashMap<>();
                    data.put("User_ID", UID);
                    data.put("family_name",family_name);
                    data.put("family_address", family_address);
                    data.put("phone",phone);
                    Log.d(TAG, "CONTENT:"+data);
                    /*if (req==200){
                        String filePath = selImageList.get(0).path;
                        //data.put("img",new File(filePath));
                        reCode = Post_essay.getStringCha(Constant.essay_publish, data,filePath);//向服务器提交用户上传请求
                    }else {*/
                        reCode = Post_userLogin.getStringCha(Constant.family_create, data);//向服务器提交用户上传请求
                    //}

                    Log.d(TAG, "Post upload_Essay result:" + reCode);
                    Message msg = new Message();//消息处理机制
                    Bundle bundle = new Bundle();
                    bundle.putString("response", reCode);
                    Log.d("用户请求登录返回的结果", String.valueOf(bundle));
                    msg.what = 1;
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = e;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void post_essay_to_Handler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String response = msg.getData().getString("response");
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.d(TAG, "Json内容" + json);
                            String Msg = json.getString("msg");
                            String flag = json.getString("flag");
                            if (flag.equals("1")) {
                                onSuccessDialog();
                            } else if (flag.equals("0")) {
                                Toast.makeText(Family_manage_create.this, Msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "更改用户信息捕获异常:" + e);
                        }
                        break;
                    case 2:
                        Toast.makeText(Family_manage_create.this, "更新失败:" + msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void onSuccessDialog() {
        new AlertDialog
                .Builder(Family_manage_create.this)
                .setTitle("创建成功")
                .setMessage("确定将回退至家庭界面")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).show();

    }


    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            return;
        }
        StringBuffer sb = new StringBuffer(256);
        sb.append("时间 : ");
        sb.append(location.getTime());
        sb.append("\n返回码 : ");
        sb.append(location.getLocType());
        sb.append("\n纬度 : ");
        sb.append(location.getLatitude());
        sb.append("\n经度 : ");
        sb.append(location.getLongitude());
        sb.append("\n半径 : ");
        sb.append(location.getRadius());
        sb.append("\n省 : ");
        sb.append(location.getProvince());
        sb.append("\n市 : ");
        sb.append(location.getCity());
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\n速度 : ");
            sb.append(location.getSpeed());
            sb.append("\n卫星数 : ");
            sb.append(location.getSatelliteNumber());
        }
        Log.d(TAG,sb.toString());

        if (location.getLocType() != 161) { //定位失败
            Toast.makeText(this,"定位失败,请手动输入",Toast.LENGTH_SHORT).show();
        } else {
            //familyAddress.setText(location.getProvince().substring(0, location.getProvince().lastIndexOf("省")));
            StringBuffer addr = new StringBuffer();
            boolean isFail = false;
            if (location.getProvince()!=null){
                addr.append(location.getProvince());
            }else {
                isFail = true;
            }
            if (location.getCity()!=null){
                addr.append(location.getCity());
            }
            if (location.getDistrict()!=null){
                addr.append(location.getDistrict());
            }
            if (location.getStreet()!=null){
                addr.append(location.getStreet());
            }
            if (location.getBuildingName()!=null){
                addr.append(location.getBuildingName());
            }
            if (!isFail) {
                familyAddress.setText(addr);
            }else {
                Toast.makeText(this,"定位失败,请手动输入",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
