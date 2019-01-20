package com.example.shopcar_project.mvp.presenter;

import com.example.shopcar_project.MainActivity;
import com.example.shopcar_project.mvp.model.IShopCarModel;
import com.example.shopcar_project.mvp.model.ShopCarModelImpl;

public class ShopCarPresenterImpl implements IShopCarPresenter {

    MainActivity mView;
    private final ShopCarModelImpl shopCarModel;
    String url = "http://www.zhaoapi.cn/product/getCarts?uid=71";

    public ShopCarPresenterImpl(MainActivity mView) {
        this.mView = mView;
        shopCarModel = new ShopCarModelImpl();
    }

    @Override
    public void getModelData() {
        shopCarModel.getData(url, new IShopCarModel.Cteanview() {
            @Override
            public void Succes(Object data) {
                mView.getViewData(data);
            }

            @Override
            public void Fuils() {

            }
        });
    }
}
