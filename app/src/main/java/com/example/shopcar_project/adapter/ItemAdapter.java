package com.example.shopcar_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shopcar_project.R;
import com.example.shopcar_project.bean.CartBean;
import com.example.shopcar_project.mvp.view.CustomAddView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private final Context context;
    private List<CartBean.DataBean.ListBean> itemlist = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private ItemHolder itemHolder;

    public ItemAdapter(Context context, List<CartBean.DataBean.ListBean> itemlist) {
        this.context = context;
        this.itemlist = itemlist;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        itemHolder = new ItemHolder(view);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {


        holder.nei.setText(itemlist.get(position).getTitle());
        holder.price.setText("￥："+itemlist.get(position).getPrice());
        Glide.with(context).load(itemlist.get(position).getImages().split("\\|")[0].replace("https", "http")).into(holder.imageView);
        //根据我记录的状态，改变勾选
        holder.xuan2.setChecked(itemlist.get(position).isCheck());
        //商品的跟商家的有所不同，商品添加了选中改变的监听
        holder.xuan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemlist.get(position).setCheck(isChecked);
                if (mShopCallBackListener == null) {
                    mShopCallBackListener.callBack();
                }
            }
        });

        //设置自定义View里的Edit
        itemHolder.mCustomShopCarPrice.setData(this, itemlist, position);
        itemHolder.mCustomShopCarPrice.setOnCallBack(new CustomAddView.CallBackListener() {
            @Override
            public void callBack() {
                if (mShopCallBackListener != null) {
                    mShopCallBackListener.callBack();
                }
            }
        });

}

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        private final CheckBox xuan2;
        private final ImageView imageView;
        private final TextView nei;
        private final CustomAddView mCustomShopCarPrice;
        private final TextView price;

        public ItemHolder(View itemView) {
            super(itemView);
            xuan2 = itemView.findViewById(R.id.xuan2);
            imageView = itemView.findViewById(R.id.imageView);
            nei = itemView.findViewById(R.id.nei);
            price = itemView.findViewById(R.id.price);
            mCustomShopCarPrice = itemView.findViewById(R.id.custom_product_counter);
        }
    }


    /**
     * 在我们子商品的adapter中，修改子商品的全选和反选
     *
     * @param isSelectAll
     */
    public void selectOrRemoveAll(boolean isSelectAll) {
        for (CartBean.DataBean.ListBean listBean : itemlist) {
            listBean.setCheck(isSelectAll);
        }

        notifyDataSetChanged();
    }

    private ShopCallBackListener mShopCallBackListener;

    public void setListener(ShopCallBackListener listener) {
        this.mShopCallBackListener = listener;
    }

    public interface ShopCallBackListener {
        void callBack();
    }
}
