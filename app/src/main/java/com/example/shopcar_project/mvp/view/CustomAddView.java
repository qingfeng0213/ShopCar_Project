package com.example.shopcar_project.mvp.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shopcar_project.R;
import com.example.shopcar_project.adapter.ItemAdapter;
import com.example.shopcar_project.bean.CartBean;

import java.util.List;

public class CustomAddView extends RelativeLayout implements View.OnClickListener {
    Context mContext;
    private EditText mEditCar;

    private List<CartBean.DataBean.ListBean> listBeans;
    private ItemAdapter itemAdapter;
    private int i;

    public CustomAddView(Context context) {
        super(context);
        init(context);
    }

    public CustomAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }



    public CustomAddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = View.inflate(context, R.layout.shop_car_price_layout, null);
        ImageView addIamge = (ImageView) view.findViewById(R.id.add_car);
        ImageView jianIamge = (ImageView) view.findViewById(R.id.jian_car);
        mEditCar = view.findViewById(R.id.edit_shop_car);
        addIamge.setOnClickListener(this);
        jianIamge.setOnClickListener(this);
        addView(view);

//        mEditCar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                num = Integer.parseInt(s.toString());
//                //TODO:改变数量
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


    }

    private int num;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_car:
                //改变数量，设置数量，改变对象内容，回调，局部刷新
                num++;

                mEditCar.setText(num + "");
              listBeans.get(i).setNum(num);
                mCallBackListener.callBack();
                itemAdapter.notifyItemChanged(i);
                break;
            case R.id.jian_car:
                if (num > 1) {
                    num--;
                } else {
                    Toast.makeText(mContext, "商品数量不能小于1", Toast.LENGTH_LONG).show();
                }
               mEditCar.setText(num + "");
               listBeans.get(i).setNum(num);
                mCallBackListener.callBack();
                itemAdapter.notifyItemChanged(i);
                break;
            default:
                break;
        }
    }
    public void setData(ItemAdapter itemAdapter, List<CartBean.DataBean.ListBean> list, int position) {
        this.listBeans = list;
        this.itemAdapter = itemAdapter;
        i = position;
        num = listBeans.get(i).getNum();
        mEditCar.setText(this.num + "");
    }
    private CallBackListener mCallBackListener;

    public void setOnCallBack(CallBackListener listener) {
        this.mCallBackListener = listener;
    }

    public interface CallBackListener {
        void callBack();
    }
}
