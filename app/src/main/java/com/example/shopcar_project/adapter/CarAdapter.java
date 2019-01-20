package com.example.shopcar_project.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.shopcar_project.R;
import com.example.shopcar_project.bean.CartBean;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarHolder> {

    private final Context context;
    private List<CartBean.DataBean> list = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    public CarAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = layoutInflater.inflate(R.layout.recyitem, parent, false);

        CarHolder carHolder = new CarHolder(inflate);
        return carHolder;
    }

    @Override
    public void onBindViewHolder(final CarHolder holder, final int position) {
        holder.biao.setText(list.get(position).getSellerName());

        List<CartBean.DataBean.ListBean> beanList = list.get(position).getList();
        holder.tworecy.setLayoutManager(new LinearLayoutManager(context));
        final ItemAdapter itemAdapter = new ItemAdapter(context, beanList);
        holder.tworecy.setAdapter(itemAdapter);

        holder.xuan.setChecked(list.get(position).isCheck());
        itemAdapter.setListener(new ItemAdapter.ShopCallBackListener() {
            @Override
            public void callBack() {
                //从商品适配里回调回来，回给activity，activity计算价格和数量
                if(mShopCallBackListener != null) {
                    mShopCallBackListener.callBack(list);
                }

                List<CartBean.DataBean.ListBean> listBeans = list.get(position).getList();
                //创建一个临时的标志位，用来记录现在点击的状态
                boolean isAllChecked = true;
                for (CartBean.DataBean.ListBean bean : listBeans) {
                    if (!bean.isCheck()) {
                        //只要有一个商品未选中，标志位设置成false，并且跳出循环
                        isAllChecked = false;
                        break;
                    }
                }

                //刷新商家的状态
                holder.xuan.setChecked(isAllChecked);
                list.get(position).setCheck(isAllChecked);
            }
        });

        //监听checkBox的点击事件
        //目的是改变旗下所有商品的选中状态
        holder.xuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先改变自己的标志位
                list.get(position).setCheck(holder.xuan.isChecked());
                //调用产品adapter的方法，用来全选和反选
                itemAdapter.selectOrRemoveAll(holder.xuan.isChecked());
            }
        });

}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<CartBean.DataBean> list){
            this.list = list;
            notifyDataSetChanged();
    }

    public class CarHolder extends RecyclerView.ViewHolder{

        private final TextView biao;
        private final CheckBox xuan;
        private final RecyclerView tworecy;

        public CarHolder(View itemView) {
            super(itemView);
            xuan = itemView.findViewById(R.id.xuan);
            biao = itemView.findViewById(R.id.biao);
            tworecy = itemView.findViewById(R.id.errecy);
        }
    }

    private ShopCallBackListener mShopCallBackListener;

    public void setListener(ShopCallBackListener listener) {
        this.mShopCallBackListener = listener;
    }

    public interface ShopCallBackListener {
        void callBack(List<CartBean.DataBean> list);
    }
}
