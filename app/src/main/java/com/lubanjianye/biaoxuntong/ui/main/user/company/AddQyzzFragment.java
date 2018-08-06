package com.lubanjianye.biaoxuntong.ui.main.user.company;

import android.graphics.drawable.Drawable;
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
import com.lubanjianye.biaoxuntong.util.dialog.PromptDialog;
import com.lubanjianye.biaoxuntong.util.toast.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名:   LBBXT
 * 包名:     com.lubanjianye.biaoxuntong.ui.main.fragment.user.company
 * 文件名:   AddQyzzFragment
 * 创建者:   lunious
 * 创建时间: 2017/12/15  18:05
 * 描述:     TODO
 */

public class AddQyzzFragment extends BaseFragment1 implements View.OnClickListener {


    private LinearLayout llIvBack = null;
    private AppCompatTextView mainBarName = null;
    private AppCompatTextView tvZzlx = null;
    private AppCompatTextView tvDl = null;
    private AppCompatTextView tvXl = null;
    private AppCompatTextView tvZy = null;
    private AppCompatTextView tvDj = null;
    private AppCompatTextView tvQy = null;
    private Button btnAdd = null;
    private View view = null;
    private AppCompatTextView text01 = null;
    private ImageView iv1 = null;
    private LinearLayout ll1 = null;
    private AppCompatTextView btnUpdate = null;
    private LinearLayout ll = null;


    PromptDialog promptDialog = null;

    private SpinerPopWindow<String> mSpinerZzlx;
    private SpinerPopWindow<String> mSpinerDl;
    private SpinerPopWindow<String> mSpinerXl;
    private SpinerPopWindow<String> mSpinerZy;
    private SpinerPopWindow<String> mSpinerDj;
    private SpinerPopWindow<String> mSpinerQy;

    private List<String> Zzlxlist = new ArrayList<String>();
    private List<String> Dllist = new ArrayList<String>();
    private List<String> Xllist = new ArrayList<String>();
    private List<String> Zylist = new ArrayList<String>();
    private List<String> Djlist = new ArrayList<String>();
    private List<String> Qylist = new ArrayList<String>();


    private List<String> lx_code = new ArrayList<String>();
    private List<String> dl_code = new ArrayList<String>();
    private List<String> xl_code = new ArrayList<String>();
    private List<String> zy_code = new ArrayList<String>();

    private String lx_id = "";
    private String dl_id = "";
    private String xl_id = "";
    private String zy_id = "";
    private String dj_id = "";

    String one;
    String two;
    String three;
    String four;
    String five;
    String six;


    private long id = 0;
    private String nickName = "";
    private String token = "";
    private String comid = "";
    private String imageUrl = "";
    private String mobile = "";

    private boolean hasTJ = false;

    @Override
    public Object setLayout() {
        return R.layout.fragment_add_qyzz;
    }

    @Override
    public void initView() {
        llIvBack = getView().findViewById(R.id.ll_iv_back);
        mainBarName = getView().findViewById(R.id.main_bar_name);
        tvZzlx = getView().findViewById(R.id.tv_zzlx);
        tvDl = getView().findViewById(R.id.tv_dl);
        tvXl = getView().findViewById(R.id.tv_xl);
        tvZy = getView().findViewById(R.id.tv_zy);
        tvDj = getView().findViewById(R.id.tv_dj);
        tvQy = getView().findViewById(R.id.tv_qy);
        btnAdd = getView().findViewById(R.id.btn_add);
        view = getView().findViewById(R.id.view);
        text01 = getView().findViewById(R.id.text01);
        iv1 = getView().findViewById(R.id.iv1);
        ll1 = getView().findViewById(R.id.ll1);
        btnUpdate = getView().findViewById(R.id.btn_update);
        ll = getView().findViewById(R.id.ll);
        llIvBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        iv1.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);


    }

    @Override
    public void initData() {
        llIvBack.setVisibility(View.VISIBLE);
        mainBarName.setText("新增企业资质");
        //创建对象
        promptDialog = new PromptDialog(getActivity());
        //设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(3000);
    }

    @Override
    public void initEvent() {
        loadZZLX();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_add:
                if (!TextUtils.isEmpty(one)) {
                    //添加条件
                    String chooseName1 = one + "+" + two + "+" + three + "+" + four + "+" + five + "+" + six;
                    text01.setText(chooseName1);
                    ll1.setVisibility(View.VISIBLE);
                    hasTJ = true;
                } else {
                    ToastUtil.shortToast(getContext(), "请选择条件！");
                }
                break;
            case R.id.iv1:
                ll1.setVisibility(View.GONE);
                hasTJ = false;
                break;
            case R.id.btn_update:
                if (!hasTJ) {
                    ToastUtil.shortToast(getContext(), "请添加条件！");
                } else {
                    promptDialog.showLoading("正在提交...");

                    if ("特级".equals(five) || "甲级".equals(five) || "不分等级".equals(five)) {
                        dj_id = "1";
                    } else if ("一级".equals(five) || "乙级".equals(five)) {
                        dj_id = "2";
                    } else if ("二级".equals(five) || "丙级".equals(five) || "暂定级".equals(five) || "预备级".equals(five)) {
                        dj_id = "3";
                    } else if ("三级".equals(five) || "未分级".equals(five)) {
                        dj_id = "4";
                    }


                    List<UserProfile> users = DatabaseManager.getInstance().getDao().loadAll();
                    for (int i = 0; i < users.size(); i++) {
                        id = users.get(0).getId();
                        nickName = users.get(0).getNickName();
                        token = users.get(0).getToken();
                        comid = users.get(0).getComid();
                        imageUrl = users.get(0).getImageUrl();
                        mobile = users.get(0).getMobile();
                    }

                    OkGo.<String>post(BiaoXunTongApi.URL_ADDQYZZ)
                            .params("t_qyzz_query_bxts[0].user_id", id)
                            .params("t_qyzz_query_bxts[0].lx_id", lx_id)
                            .params("t_qyzz_query_bxts[0].dl_id", dl_id)
                            .params("t_qyzz_query_bxts[0].xl_id", xl_id)
                            .params("t_qyzz_query_bxts[0].zy_id", zy_id)
                            .params("t_qyzz_query_bxts[0].dj_id", dj_id)
                            .params("t_qyzz_query_bxts[0].ly", "")
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
            case R.id.tv_dl:
                tvDl.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_xl:
                tvXl.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_zy:
                tvZy.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_dj:
                tvDj.setCompoundDrawables(null, null, drawable, null);
                break;
            case R.id.tv_qy:
                tvQy.setCompoundDrawables(null, null, drawable, null);
                break;
            default:
                break;
        }

    }

    /**
     * 加载资质类型列表
     */

    public void loadZZLX() {


        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXALLLIST)
                .params("lx_code", "")
                .params("dl_code", "")
                .params("xl_code", "")
                .params("zy_code", "")
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
                                Zzlxlist.add(name);
                                lx_code.add(data.getString("lx_code"));

                            }
                            mSpinerZzlx = new SpinerPopWindow<String>(getActivity(), Zzlxlist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerZzlx.dismiss();
                                    tvZzlx.setText(Zzlxlist.get(position));
                                    one = Zzlxlist.get(position);
                                    lx_id = lx_code.get(position);


                                    //重置后面选项名称
                                    tvDl.setText("大类");
                                    tvXl.setText("小类");
                                    tvZy.setText("专业");
                                    tvDj.setText("等级");
                                    tvQy.setText("地区范围");

                                    if (Dllist.size() > 0) {
                                        Dllist.clear();
                                    }
                                    String lx_code = array.getJSONObject(position).getString("lx_code");
                                    loadDL(lx_code);


                                }
                            });

                            tvZzlx.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerZzlx == null) {
                                        return;
                                    }
                                    mSpinerZzlx.setWidth(ll.getWidth());
                                    mSpinerZzlx.setHeight(ll.getHeight() / 2);
                                    mSpinerZzlx.showAsDropDown(view);
                                    setTextImage(R.id.tv_zzlx, R.mipmap.up);
                                }
                            });

                            mSpinerZzlx.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
    public void loadDL(String lx_code) {


        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXALLLIST)
                .params("lx_code", lx_code)
                .params("dl_code", "")
                .params("xl_code", "")
                .params("zy_code", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");


                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("dl_name");
                                if (TextUtils.isEmpty(name)) {
                                    Dllist.add("不分大类");
                                } else {
                                    Dllist.add(name);
                                }

                                dl_code.add(data.getString("dl_code"));

                                JSONArray djArray = data.getJSONArray("listdj");
                                if (djArray != null) {

                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }

                                    for (int j = 0; j < djArray.size(); j++) {
                                        final JSONObject dj = djArray.getJSONObject(j);
                                        String djName = dj.getString("name");
                                        Djlist.add(djName);

                                    }
                                    loadDJ();
                                }


                                JSONArray qyArray = data.getJSONArray("listdq");
                                if (qyArray != null) {
                                    if (Qylist.size() > 0) {
                                        Qylist.clear();
                                    }
                                    for (int j = 0; j < qyArray.size(); j++) {
                                        final JSONObject qy = qyArray.getJSONObject(j);
                                        String qyName = qy.getString("name");
                                        Qylist.add(qyName);

                                    }
                                    loadQY();
                                }


                            }
                            mSpinerDl = new SpinerPopWindow<String>(getActivity(), Dllist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerDl.dismiss();

                                    String DlName = Dllist.get(position);
                                    tvDl.setText(Dllist.get(position));
                                    two = Dllist.get(position);
                                    dl_id = dl_code.get(position);

                                    //重置后面选项名称
                                    tvXl.setText("小类");
                                    tvZy.setText("专业");
                                    tvDj.setText("等级");
                                    tvQy.setText("地区范围");

                                    if (Xllist.size() > 0) {
                                        Xllist.clear();
                                    }
                                    String dl_code = array.getJSONObject(position).getString("dl_code");
                                    loadXL(dl_code);


                                }
                            });

                            tvDl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerDl == null) {
                                        return;
                                    }
                                    mSpinerDl.setWidth(ll.getWidth());
                                    mSpinerDl.setHeight(ll.getHeight() / 2);
                                    mSpinerDl.showAsDropDown(view);
                                    setTextImage(R.id.tv_dl, R.mipmap.up);
                                }
                            });

                            mSpinerDl.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_dl, R.mipmap.down);
                                }
                            });
                        }

                    }
                });

    }

    /**
     * 加载小类列表
     */
    public void loadXL(String dl_code) {

        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXALLLIST)
                .params("lx_code", "")
                .params("dl_code", dl_code)
                .params("xl_code", "")
                .params("zy_code", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("xl_name");
                                if (TextUtils.isEmpty(name)) {
                                    Xllist.add("不分小类");
                                } else {
                                    Xllist.add(name);
                                }
                                xl_code.add(data.getString("xl_code"));

                                JSONArray djArray = data.getJSONArray("listdj");
                                if (djArray != null) {

                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }
                                    for (int j = 0; j < djArray.size(); j++) {
                                        final JSONObject dj = djArray.getJSONObject(j);

                                        String djName = dj.getString("name");
                                        Djlist.add(djName);
                                    }

                                    loadDJ();
                                }

                                JSONArray qyArray = data.getJSONArray("listdq");
                                if (qyArray != null) {
                                    if (Qylist.size() > 0) {
                                        Qylist.clear();
                                    }
                                    for (int j = 0; j < qyArray.size(); j++) {
                                        final JSONObject qy = qyArray.getJSONObject(j);
                                        String qyName = qy.getString("name");
                                        Qylist.add(qyName);

                                    }
                                    loadQY();
                                }

                            }
                            mSpinerXl = new SpinerPopWindow<String>(getActivity(), Xllist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerXl.dismiss();

                                    tvXl.setText(Xllist.get(position));
                                    three = Xllist.get(position);
                                    xl_id = xl_code.get(position);

                                    //重置后面选项名称
                                    tvZy.setText("专业");
                                    tvDj.setText("等级");
                                    tvQy.setText("地区范围");

                                    if (Zylist.size() > 0) {
                                        Zylist.clear();
                                    }

                                    String xl_code = array.getJSONObject(position).getString("xl_code");
                                    loadZY(xl_code);


                                }
                            });

                            tvXl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerXl == null) {
                                        return;
                                    }
                                    mSpinerXl.setWidth(ll.getWidth());
                                    mSpinerXl.setHeight(ll.getHeight() / 2);
                                    mSpinerXl.showAsDropDown(view);
                                    setTextImage(R.id.tv_xl, R.mipmap.up);
                                }
                            });

                            mSpinerXl.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    //TODO
                                    setTextImage(R.id.tv_xl, R.mipmap.down);
                                }
                            });
                        }
                    }
                });

    }

    /**
     * 加载专业列表
     */
    public void loadZY(String xl_code) {


        OkGo.<String>post(BiaoXunTongApi.URL_GETZZLXALLLIST)
                .params("lx_code", "")
                .params("dl_code", "")
                .params("xl_code", xl_code)
                .params("zy_code", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final JSONObject object = JSON.parseObject(response.body());
                        String status = object.getString("status");

                        if ("200".equals(status)) {
                            final JSONArray array = JSON.parseObject(response.body()).getJSONArray("data");

                            for (int i = 0; i < array.size(); i++) {

                                final JSONObject data = array.getJSONObject(i);
                                String name = data.getString("zy_name");
                                if (TextUtils.isEmpty(name)) {
                                    Zylist.add("不分专业");
                                } else {
                                    Zylist.add(name);
                                }
                                zy_code.add(data.getString("zy_code"));

                                JSONArray djArray = data.getJSONArray("listdj");
                                if (djArray != null) {

                                    if (Djlist.size() > 0) {
                                        Djlist.clear();
                                    }
                                    for (int j = 0; j < djArray.size(); j++) {
                                        final JSONObject dj = djArray.getJSONObject(j);

                                        String djName = dj.getString("name");
                                        Djlist.add(djName);
                                    }

                                    loadDJ();
                                }

                                JSONArray qyArray = data.getJSONArray("listdq");
                                if (qyArray != null) {
                                    if (Qylist.size() > 0) {
                                        Qylist.clear();
                                    }
                                    for (int j = 0; j < qyArray.size(); j++) {
                                        final JSONObject qy = qyArray.getJSONObject(j);
                                        String qyName = qy.getString("name");
                                        Qylist.add(qyName);

                                    }
                                    loadQY();
                                }

                            }
                            mSpinerZy = new SpinerPopWindow<String>(getActivity(), Zylist, new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSpinerZy.dismiss();

                                    tvZy.setText(Zylist.get(position));
                                    four = Zylist.get(position);
                                    zy_id = zy_code.get(position);

                                    //重置后面选项名称
                                    tvDj.setText("等级");
                                    tvQy.setText("地区范围");


                                }
                            });

                            tvZy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mSpinerZy == null) {
                                        return;
                                    }
                                    mSpinerZy.setWidth(ll.getWidth());
                                    mSpinerZy.setHeight(ll.getHeight() / 2);
                                    mSpinerZy.showAsDropDown(view);
                                    setTextImage(R.id.tv_zy, R.mipmap.up);
                                }
                            });

                            mSpinerZy.setOnDismissListener(new PopupWindow.OnDismissListener() {
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

    /**
     * 加载等级列表
     */
    public void loadDJ() {

        mSpinerDj = new SpinerPopWindow<String>(getActivity(), Djlist, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSpinerDj.dismiss();
                tvDj.setText(Djlist.get(position));
                five = Djlist.get(position);

                //重置后面选项名称
                tvQy.setText("地区范围");
            }
        });

        tvDj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinerDj == null) {
                    return;
                }
                mSpinerDj.setWidth(ll.getWidth());
                mSpinerDj.setHeight(ll.getHeight() / 2);
                mSpinerDj.showAsDropDown(view);
                setTextImage(R.id.tv_qy, R.mipmap.up);
            }
        });

        mSpinerDj.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO
                setTextImage(R.id.tv_qy, R.mipmap.down);
            }
        });
    }

    /**
     * 加载区域列表
     */
    public void loadQY() {

        mSpinerQy = new SpinerPopWindow<String>(getActivity(), Qylist, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSpinerQy.dismiss();
                tvQy.setText(Qylist.get(position));
                six = Qylist.get(position);

            }
        });

        tvQy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinerQy == null) {
                    return;
                }
                mSpinerQy.setWidth(ll.getWidth());
                mSpinerQy.setHeight(ll.getHeight() / 2);
                mSpinerQy.showAsDropDown(view);
                setTextImage(R.id.tv_qy, R.drawable.icon_up);
            }
        });

        mSpinerQy.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextImage(R.id.tv_qy, R.drawable.icon_down);
            }
        });
    }
}
