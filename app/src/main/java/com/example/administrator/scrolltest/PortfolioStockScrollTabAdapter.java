package com.example.administrator.scrolltest ;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/3/25 12:32
 * Summary: 不能用CommonRecyclerViewAdapter,不明原因导致滑动卡顿
 */
public class PortfolioStockScrollTabAdapter extends RecyclerView.Adapter {
    private static final String TAG = PortfolioStockScrollTabAdapter.class.getSimpleName();
    private SyncHScrollView mHeadSyncRecyclerView;
    private Context mContext;
    public static final int RV_FOOT = 1;
    private List<String> mArrayList;
    protected View mFooterView;

    public PortfolioStockScrollTabAdapter(Context context, List<String> list, SyncHScrollView headSyncRecyclerView) {
        this.mHeadSyncRecyclerView = headSyncRecyclerView;
        mContext = context;
        mArrayList = list;
    }

    public final View getFooterView() {
        return mFooterView;
    }

    public final void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && mFooterView != null) {
            return RV_FOOT;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate;
        if (viewType == RV_FOOT) {
            return new BottomHolder(mFooterView);
        } else {
            inflate = LayoutInflater.from(mContext).inflate(R.layout.portfolio_stock_rv_content_item, parent, false);
        }
        return new RVHolder(inflate);
    }
        @Override
        public void onBindViewHolder (RecyclerView.ViewHolder holder,int position){
            if (position == mArrayList.size()) {
                return;
            }
            RVHolder rvHolder = (RVHolder) holder;
            rvHolder.setData(position);
        }


        @Override
        public int getItemCount () {
            int size = mArrayList == null ? 0 : mArrayList.size();
            if (mFooterView != null) {
                return size + 1;
            } else {
                return size;
            }
        }


        class BottomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public BottomHolder(@NonNull View itemView) {
                super(itemView);
            }

            @Override
            public void onClick(View v) {
            }
        }

        class RVHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private SyncHScrollView itemSyncHScrollView;
            private View contentLeftCl;
            private TextView content_name_tv;
            private View content_right_c_ll;

            public RVHolder(View itemView) {
                super(itemView);
                itemSyncHScrollView=itemView.findViewById(R.id.hsv_rv_content_right);
                content_right_c_ll=itemView.findViewById(R.id.content_right_c_ll);
                content_name_tv=itemView.findViewById(R.id.content_name_tv);
              //  content_right_c_ll.setOnLongClickListener(this);
                //content_right_c_ll.setOnClickListener(this);
               // itemSyncHScrollView.setOnLongClickListener(this);
                //itemSyncHScrollView.setOnClickListener(this);
                itemView.setOnTouchListener(new ItemTouchListener());
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                //itemSyncHScrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(mHeadSyncRecyclerView));
                //添加滑动观察者,mHeadSyncRecyclerView右侧头部 与 下面右侧item的scrollview 联动
                mHeadSyncRecyclerView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(itemSyncHScrollView));

            }

            public void setData(int position) {

                content_name_tv.setText(mArrayList.get(position));
                  //修正重新创建的item位置
                itemSyncHScrollView.smoothScrollTo(left, top);
                Log.i(TAG, "setData: left=="+left);
                Log.i(TAG, "setData:top =="+top);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.content_right_c_ll:
                        Log.i(TAG, "onClick: ");
                         break;
                     default:
                             Log.i(TAG, "onClick:  default");
                             break;
                }
            }

            @Override
            public boolean onLongClick(View v) {
                if (mContext == null) {
                    return false;
                }
                Log.i(TAG, "onLongClick: ");
                return true;
            }
        }

        private int left;
        private int top;

        class OnScrollChangedListenerImp implements SyncHScrollView.OnScrollChangedListener {
            private SyncHScrollView mScrollViewArg;

            public OnScrollChangedListenerImp(SyncHScrollView scrollViewArg) {
                this.mScrollViewArg = scrollViewArg;

            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                left = l;
                top = t;
                mScrollViewArg.smoothScrollTo(l, t);
            }
        }

        final class ItemTouchListener implements View.OnTouchListener {
            public boolean onTouch(View arg0, MotionEvent event) {

                // 滑动后 分发联动
                mHeadSyncRecyclerView.onTouchEvent(event);
                return false;
            }
        }


}
