package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lubanjianye.biaoxuntong.R;
import com.lubanjianye.biaoxuntong.base.BaseFragment1;
import com.lubanjianye.biaoxuntong.database.DatabaseManager;
import com.lubanjianye.biaoxuntong.database.UserProfile;
import com.lubanjianye.biaoxuntong.app.BiaoXunTongApi;
import com.lubanjianye.biaoxuntong.ui.dropdown.SpinerPopWindow;
import com.lubanjianye.biaoxuntong.util.datapicker.CustomDatePicker;
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.company
 * 文件名:   AddRyzzFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  18:03
 * 描述:     TODO
 */

public class AddRyzzFragment extends BaseFragment1 implements View.OnClickListener {

    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatEditText etRyname = null;
    private AppCompatEditText etSfzh = null;
    private AppCompatEditText etZsh = null;
    private AppCompatTextView etStartTime = null;
    private AppCompatTextView etEndTime = null;
    private AppCompatTextView tvZzlx = null;
    private AppCompatTextView tvMc = null;
    private AppCompatTextView tvMcDj = null;
    private AppCompatTextView tvZy = null;
    private Button btnAdd = null;
    private View view6 = null;
    private AppCompatTextView text01 = null;
    private ImageView iv1 = null;
    private AppCompatTextView btnUpdate = null;
    private LinearLayout ll = null;
    private LinearLayout lll = null;


    PromptDialog promptDialog;

    String ryname = "";
    String sfzh = "";
    String zsh = "";

    String stratTime = "1900-01-01 00:00";
    String endTime = "1999-01-01 00:00";


    private SpinerPopWindow<String> mSpinerlx;
    private SpinerPopWindow<String> mSpinerZg;
    private SpinerPopWindow<String> mSpinerZgmcdg;
    private SpinerPopWindow<String> mSpinerZgzy;

    private List<String> Lxlist = new ArrayList<String>();
    private List<String> Zglist = new ArrayList<String>();
    private List<String> Zgmcdjlist = new ArrayList<String>();
    private List<String> Zgzylist = new ArrayList<String>();


    String one;
    String two;
    String three;
    String four;


    private List<String> lx_code = new ArrayList<String>();
    private List<String> zg_code = new ArrayList<String>();
    private List<String> zg_mcdj_code = new ArrayList<String>();
    private List<String> zg_zy_code = new ArrayList<String>();

    private String lx_id = "";
    private String zg_id = "";
    private String zg_mcdj_id = "";
    private String zg_zy_id = "";

    private long id = 0;
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String mobile = "";

    private boolean hasTJ = false;


    private CustomDatePicker customDatePicker1, customDatePicker2;


    @Override
    public Object setLayout() {
        return R.layout.fragment_add_ryzz;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        etRyname = getView().findViewById(R.id.et_ryname);
        etSfzh = getView().findViewById(R.id.et_sfzh);
        etZsh = getView().findViewById(R.id.et_zsh);
        etStartTime = getView().findViewById(R.id.et_start_time);
        etEndTime = getView().findViewById(R.id.et_end_time);
        tvZzlx = getView().findViewById(R.id.tv_zzlx);
        tvMc = getView().findViewById(R.id.tv_mc);
        tvMcDj = getView().findViewById(R.id.tv_mc_dj);
        tvZy = getView().findViewById(R.id.tv_zy);
        btnAdd = getView().findViewById(R.id.btn_add);
        view6 = getView().findViewById(R.id.view6);
        text01 = getView().findViewById(R.id.text01);
        iv1 = getView().findViewById(R.id.iv1);
        btnUpdate = getView().findViewById(R.id.btn_update);
        ll = getView().findViewById(R.id.ll);
        lll = getView().findViewById(R.id.lll);
        llIvBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        iv1.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("新增人员资质");
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
    }

    @Override
    public void initEvent() {
        loadZZlX();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                if (promptDialog != null) {
                    promptDialog.dismissImmediately();
                }
                getActivity().onBackPressed();
                break;
            case R.id.btn_add:
                //添加条件
                if (one == null) {
                    ToastUtil.shortToast(getContext(), "请添加完整条件！");
                }
                if (one != null) {
                    text01.setText(one + (two == null ? "" : ("+" + two)) + (three == null ? "" : ("+" + three)) + (four == null ? "" : ("+" + four)));
                    lll.setVisibility(View.VISIBLE);
                    hasTJ = true;
                }
                break;
            case R.id.iv1:
                //删除条件
                lll.setVisibility(View.GONE);
                hasTJ = false;
                break;
            case R.id.btn_update:
                ryname = etRyname.getText().toString().trim();
                sfzh = etSfzh.getText().toString().trim();
                zsh = etZsh.getText().toString().trim();


                if (!hasTJ) {
                    ToastUtil.shortToast(getContext(), "请添加人员资质条件！");
                } else {

                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                    for (int i = 0; i < users.size(); i++) {
                        id = users.get(0).getId();
                        nickName = users.get(0).getNickName();
                        token = users.get(0).getToken();
                        comid = users.get(0).getComid();
                        imageUrl = users.get(0).getImageUrl();
                        mobile = users.get(0).getMobile();
                    }


                    if (TextUtils.isEmpty(ryname)) {
                        ToastUtil.shortToast(getContext(), "请输入人员姓名!");
                    } else {
                        promptDialog.showLoading("提交中");

                        OkGo.<String>post(BiaoXunTongApi.URL_ADDRYZZ)
                                .params("t_ryzz_query_bxts[0].lx_id", lx_id)
                                .params("t_ryzz_query_bxts[0].user_id", id)
                                .params("t_ryzz_query_bxts[0].ry_id", "")
                                .params("t_ryzz_query_bxts[0].zg_id", zg_id)
                                .params("t_ryzz_query_bxts[0].zg_mcdj_id", zg_mcdj_id)
                                .params("t_ryzz_query_bxts[0].zg_zy_id", zg_zy_id)
                                .params("t_ryzz_query_bxts[0].ly", "")
                                .params("t_ryzz_query_bxts[0].sfz", sfzh)
                                .params("t_ryzz_query_bxts[0].zsh", zsh)
                                .params("t_ryzz_query_bxts[0].yxq_from", stratTime)
                                .params("t_ryzz_query_bxts[0].yxq_to", endTime)
                                .params("t_ryzz_query_bxts[0].ryname", ryname)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        final JSONObject object = JSON.parseObject(response.body());
                                        String status = object.getString("status");
                                        String message = object.getString("message");
                                        if ("200".equals(status)) {
                                            promptDialog.showSuccess(message);
                                        } else {
                                            promptDialog.showError(message);
                                        }
                                    }
                                });


                    }

                }
                break;
            case R.id.et_start_time:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String now = sdf.format(new Date());

                etStartTime.setText(now.split(" ")[0]);
                customDatePicker1 = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // 回调接口，获得选中的时间
                        etStartTime.setText(time.split(" ")[0]);

                        stratTime = time.split(" ")[0];

                    }
                    // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                }, "1900-01-01 00:00", "2099-12-31 00:00");
                // 不显示时和分
                customDatePicker1.showSpecificTime(false);
                // 不允许循环滚动
                customDatePicker1.setIsLoop(false);

                customDatePicker1.show(etStartTime.getText().toString());
                break;
            case R.id.et_end_time:
                SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String noww = sdff.format(new Date());
                etEndTime.setText(noww.split(" ")[0]);

                customDatePicker2 = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // 回调接口，获得选中的时间
                        etEndTime.setText(time.split(" ")[0]);
                        endTime = time.split(" ")[0];

                    }
                    // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
                }, "1999-01-01 00:00", "2099-12-31 00:00");
                // 显示时和分
                customDatePicker2.showSpecificTime(false);
                // 允许循环滚动
                customDatePicker2.setIsLoop(false);

                customDatePicker2.show(etEndTime.getText().toString());
                break;
            default:
                break;
        }
    }

    /**
     * 给TextView右边设置图片
     *
     * @param resId
     */
    public void setTextImage(int id, int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        // 必须设置图片大小，否则不显示
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (id) {
            case R.id.tv_zzlx:
                tvZzlx.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_mc:
                tvMc.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_mc_dj:
                tvMcDj.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_zy:
                tvZy.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }

    }

    /**
     * 加载资质类型列表
     */
    public void loadZZlX() {

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLRYLX)
                .params("lx_code", "")
                .params("zg_code", "")
                .params("zg_mcdj", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("lx_name");
                                Lxlist.add(name);
                                lx_code.add(data.getString("lx_code"));

                            }
                            mSpinerlx = new SpinerPopWindow<String>(getActivity(), Lxlist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerlx.dismiss();
                                    tvZzlx.setText(Lxlist.get(position));
                                    one = Lxlist.get(position);
                                    lx_id = lx_code.get(position);

                                    //重置后面选项名称
                                    tvMc.setText("名称");
                                    tvMcDj.setText("名称/等级");
                                    tvZy.setText("专业");

                                    if (Zglist.size() > 0) {
                                        Zglist.clear();
                                    }
                                    String lx_code = array.getJSONObject(position).getString("lx_code");
                                    loadMC(lx_code);


                                }
                            });

                            tvZzlx.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerlx == null) {
                                        return;
                                    }
                                    mSpinerlx.setWidth(ll.getWidth());
                                    mSpinerlx.setHeight(ll.getHeight() / 2);
                                    mSpinerlx.showAsDropDown(view6);
                                    setTextImage(R.id.tv_zzlx, R.mipmap.up);
                                }
                            });

                            mSpinerlx.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_zzlx, R.mipmap.down);
                                }
                            });
                        }
                    }
                });


    }


    /**
     * 加载大类列表
     */
    public void loadMC(final String lx_code) {


        OkGo.<String>post(BiaoXunTongApi.URL_GETALLRYLX)
                .params("lx_code", lx_code)
                .params("zg_code", "")
                .params("zg_mcdj", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("zg_name");
                                Zglist.add(name);
                                zg_code.add(data.getString("zg_code"));

                            }
                            mSpinerZg = new SpinerPopWindow<String>(getActivity(), Zglist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerZg.dismiss();
                                    tvMc.setText(Zglist.get(position));
                                    two = Zglist.get(position);
                                    zg_id = zg_code.get(position);

                                    //重置后面选项名称
                                    tvMcDj.setText("名称/等级");
                                    tvZy.setText("专业");

                                    if (Zgmcdjlist.size() > 0) {
                                        Zgmcdjlist.clear();
                                    }
                                    String zg_code = array.getJSONObject(position).getString("zg_code");
                                    loadMCDJ(zg_code);


                                }
                            });

                            tvMc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerZg == null) {
                                        return;
                                    }
                                    mSpinerZg.setWidth(ll.getWidth());
                                    mSpinerZg.setHeight(ll.getHeight() / 2);
                                    mSpinerZg.showAsDropDown(view6);
                                    setTextImage(R.id.tv_mc, R.mipmap.up);
                                }
                            });

                            mSpinerZg.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_mc, R.mipmap.down);
                                }
                            });
                        }

                    }
                });

    }

    /**
     * 加载小类类列表
     */
    public void loadMCDJ(String zg_code) {

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLRYLX)
                .params("lx_code", "")
                .params("zg_code", zg_code)
                .params("zg_mcdj", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("lx_name");
                                Zgmcdjlist.add(name);
                                zg_mcdj_code.add(data.getString("zg_mcdj"));

                            }
                            mSpinerZgmcdg = new SpinerPopWindow<String>(getActivity(), Zgmcdjlist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerZgmcdg.dismiss();
                                    tvMcDj.setText(Zgmcdjlist.get(position));
                                    three = Zgmcdjlist.get(position);
                                    zg_mcdj_id = zg_mcdj_code.get(position);

                                    //重置后面选项名称
                                    tvZy.setText("专业");

                                    if (Zgzylist.size() > 0) {
                                        Zgzylist.clear();
                                    }
                                    String zg_mcdj = array.getJSONObject(position).getString("zg_mcdj");
                                    loadZY(zg_mcdj);


                                }
                            });

                            tvMcDj.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerZgmcdg == null) {
                                        return;
                                    }
                                    mSpinerZgmcdg.setWidth(ll.getWidth());
                                    mSpinerZgmcdg.setHeight(ll.getHeight() / 2);
                                    mSpinerZgmcdg.showAsDropDown(view6);
                                    setTextImage(R.id.tv_mc_dj, R.mipmap.up);
                                }
                            });

                            mSpinerZgmcdg.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_mc_dj, R.mipmap.down);
                                }
                            });
                        }
                    }
                });

    }

    /**
     * 加载专业列表
     */
    public void loadZY(String zg_mcdj) {

        OkGo.<String>post(BiaoXunTongApi.URL_GETALLRYLX)
                .params("lx_code", "")
                .params("zg_code", "")
                .params("zg_mcdj", zg_mcdj)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {

                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("lx_name");
                                Zgzylist.add(name);
                                zg_zy_code.add(data.getString("zgzy_code"));

                            }
                            mSpinerZgzy = new SpinerPopWindow<String>(getActivity(), Zgzylist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerZgzy.dismiss();
                                    tvZy.setText(Zgzylist.get(position));
                                    four = Zgzylist.get(position);
                                    zg_zy_id = zg_zy_code.get(position);

                                }
                            });

                            tvZy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerZgzy == null) {
                                        return;
                                    }
                                    mSpinerZgzy.setWidth(ll.getWidth());
                                    mSpinerZgzy.setHeight(ll.getHeight() / 2);
                                    mSpinerZgzy.showAsDropDown(view6);
                                    setTextImage(R.id.tv_zy, R.mipmap.up);
                                }
                            });

                            mSpinerZgzy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_zy, R.mipmap.down);
                                }
                            });
                        }

                    }
                });

    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (promptDialog != null) {
            promptDialog.dismissImmediately();
        }
    }
}
