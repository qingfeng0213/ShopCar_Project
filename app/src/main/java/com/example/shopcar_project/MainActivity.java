package com.example.shopcar_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.shopcar_project.adapter.CarAdapter;
import com.example.shopcar_project.bean.CartBean;
import com.example.shopcar_project.mvp.presenter.ShopCarPresenterImpl;
import com.example.shopcar_project.mvp.view.IShopCarView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IShopCarView {

    private RecyclerView bigrecy;
    private List<CartBean.DataBean> list;
    private CheckBox mIvCircle;
    private TextView mAllPriceTxt;
    private TextView nSumPrice;
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bigrecy = findViewById(R.id.bigrecy);
        mIvCircle = findViewById(R.id.iv_cricle);
        mAllPriceTxt = findViewById(R.id.all_price);
        nSumPrice = findViewById(R.id.sum_price_txt);

        mIvCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSeller(mIvCircle.isChecked());
                carAdapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        bigrecy.setLayoutManager(linearLayoutManager);

        ShopCarPresenterImpl shopCarPresenter = new ShopCarPresenterImpl(this);
        shopCarPresenter.getModelData();

        carAdapter = new CarAdapter(this);
        bigrecy.setAdapter(carAdapter);
        carAdapter.setListener(new CarAdapter.ShopCallBackListener() {
            @Override
            public void callBack(List<CartBean.DataBean> list) {
                //在这里重新遍历已经改状态后的数据，
                // 这里不能break跳出，因为还需要计算后面点击商品的价格和数目，所以必须跑完整个循环
                double totalPrice = 0;

                //勾选商品的数量，不是该商品购买的数量
                int num = 0;
                //所有商品总数，和上面的数量做比对，如果两者相等，则说明全选
                int totalNum = 0;
                for (int a = 0; a < list.size(); a++) {
                    //获取商家里商品
                    List<CartBean.DataBean.ListBean> listAll = list.get(a).getList();
                    for (int i = 0; i < listAll.size(); i++) {
                        totalNum = totalNum + listAll.get(i).getNum();
                        //取选中的状态
                        if (listAll.get(i).isCheck()) {
                            totalPrice = totalPrice + (listAll.get(i).getPrice() * listAll.get(i).getNum());
                            num = num + listAll.get(i).getNum();
                        }
                    }
                }

                if (num < totalNum) {
                    //不是全部选中
                    mIvCircle.setChecked(false);
                } else {
                    //是全部选中
                    mIvCircle.setChecked(true);
                }

                mAllPriceTxt.setText("合计：" + totalPrice);
                nSumPrice.setText("去结算(" + num + ")");
            }
        });
    }

    @Override
    public void getViewData(Object data) {
        CartBean cartBean = (CartBean) data;
        list = cartBean.getData();
        if (list != null) {
            carAdapter.setList(list);
        }
    }
    /**
     * 修改选中状态，获取价格和数量
     */
    private void checkSeller(boolean bool) {
        double totalPrice = 0;
        int num = 0;
        for (int a = 0; a < list.size(); a++) {
            //遍历商家，改变状态
            CartBean.DataBean dataBean = list.get(a);
            dataBean.setCheck(bool);

            List<CartBean.DataBean.ListBean> listAll = list.get(a).getList();
            for (int i = 0; i < listAll.size(); i++) {
                //遍历商品，改变状态
                listAll.get(i).setCheck(bool);
                totalPrice = totalPrice + (listAll.get(i).getPrice() * listAll.get(i).getNum());
                num = num + listAll.get(i).getNum();
            }
        }

        if (bool) {
            mAllPriceTxt.setText("合计：" + totalPrice);
            nSumPrice.setText("去结算(" + num + ")");
        } else {
            mAllPriceTxt.setText("合计：0.00");
            nSumPrice.setText("去结算(0)");
        }

    }
}
