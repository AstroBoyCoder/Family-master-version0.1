package com.example.astroboy.family_master_version01.View.DongTai;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.astroboy.family.GreenDao.Family;
import com.example.astroboy.family_master_version01.Model.Constant;
import com.example.astroboy.family_master_version01.R;
import com.example.astroboy.family_master_version01.Util.Db_FamilyService;
import com.example.astroboy.family_master_version01.Util.HttpUtil.Post_essay;
import com.example.astroboy.family_master_version01.Util.ImagePickerView.GlideImageLoader;
import com.example.astroboy.family_master_version01.Util.ImagePickerView.ImagePickerAdapter;
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

public class Family_dongTai_publishEssay extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener,View.OnClickListener{

    private final static String TAG = "dongTai_publishEssay";

    private String reCode = "";
    List<Family> families;

    ImageButton publishBack;
    LinearLayout sent_essay;
    EditText publishContent;
    Spinner selectFamilySpinner;

    private Handler mHandler;

    final Map<ArrayList<ImageItem>, String> map = new HashMap<>();
    private Integer requestCode = 100;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;               //允许选择图片最大数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_dongtai_publish_essay_activity);
        init();
    }

    private void init() {
        publishBack = (ImageButton) findViewById(R.id.publish_back);
        sent_essay = (LinearLayout) findViewById(R.id.upload_essay);
        publishContent = (EditText) findViewById(R.id.publish_content);
        selectFamilySpinner = (Spinner) findViewById(R.id.publishSelectFamilySpinner);
        initImagePicker();
        initWidget();
        initData();
        publishBack.setOnClickListener(this);
        sent_essay.setOnClickListener(this);
    }

    private void initData() {
        families = queryFamiles();
        if (families != null) {
            String[] families_String = new String[families.size()+1];
            families_String[0] = "请选择家庭";
            for (int i = 0;i<families.size();i++) {
                families_String[i+1]=families.get(i).getFM_Name();
            }
            ArrayAdapter<String> familyString = new ArrayAdapter<>(this,R.layout.xel_spiner_learntrack_view,families_String);

            selectFamilySpinner.setAdapter(familyString);
        }
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
            case R.id.publish_back:
                onBackPressed();
                break;
            case R.id.upload_essay:
                displayDialogThread();
                break;
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

    private void displayDialogThread() {
        /*ProgressDialog dialog = ProgressDialog.show(Xel_mine_feedback.this, "上传中...", "正在将令状上诉至衙门，请稍后！");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);*/
        final AlertDialog.Builder builder = new AlertDialog.Builder(Family_dongTai_publishEssay.this);
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
    }

    private void post_essay_to_server(final String content, final int FM_ID, final int member_ID) {
        post_essay_to_Handler();
        final Integer req = getImageItemsRequest();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> data = new HashMap<>();
                    data.put("Essay_Content",content);
                    data.put("FM_ID", String.valueOf(FM_ID));
                    data.put("Member_ID", String.valueOf(member_ID));
                    Log.d(TAG, "CONTENT:"+data);
                    if (req==200){
                        String filePath = selImageList.get(0).path;
                        //data.put("img",new File(filePath));
                        reCode = Post_essay.getStringCha(Constant.essay_publish, data,filePath);//向服务器提交用户上传请求
                    }else {
                        reCode = Post_essay.getStringCha(Constant.essay_publish, data,null);//向服务器提交用户上传请求
                    }

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
                                Toast.makeText(Family_dongTai_publishEssay.this, Msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "更改用户信息捕获异常:" + e);
                        }
                        break;
                    case 2:
                        Toast.makeText(Family_dongTai_publishEssay.this, "更新失败:" + msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private List<Family> queryFamiles(){
        List<Family> families = new ArrayList<>();
        List<Family> allFamiles =  Db_FamilyService.getInstance(this).loadAllFamily();
        if (allFamiles.size()>0) {
            for (Family singleFamily : allFamiles) {
                //if (singleFamily.getUser_ID() == Integer.parseInt(UID)) {
                    families.add(singleFamily);
                //}
            }
            if (families.size()>0) {
                return families;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    private void onSuccessDialog() {
        new AlertDialog
                .Builder(Family_dongTai_publishEssay.this)
                .setTitle("上传成功")
                .setMessage("确定将回退至动态界面")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).show();

    }
}
