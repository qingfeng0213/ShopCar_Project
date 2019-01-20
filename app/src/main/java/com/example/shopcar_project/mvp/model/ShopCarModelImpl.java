package com.example.shopcar_project.mvp.model;

import com.example.shopcar_project.bean.CartBean;
import com.example.shopcar_project.network.HttpUtils;

public class ShopCarModelImpl implements IShopCarModel {

    @Override
    public void getData(String url, final Cteanview cteanview) {
        HttpUtils.getInstance().doGet(url, CartBean.class, new HttpUtils.NetCallBack() {
            @Override
            public void onSuccess(Object oj) {
                cteanview.Succes(oj);
            }

            @Override
            public void onFailure(Exception e) {
                cteanview.Fuils();
            }
        });
    }
}
