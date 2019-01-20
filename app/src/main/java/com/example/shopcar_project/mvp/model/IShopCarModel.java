package com.example.shopcar_project.mvp.model;

public interface IShopCarModel {
    void getData(String url,Cteanview cteanview);

    interface  Cteanview{
        void Succes(Object data);
        void Fuils();

    }
}
