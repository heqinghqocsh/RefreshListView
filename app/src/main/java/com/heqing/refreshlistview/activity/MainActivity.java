package com.heqing.refreshlistview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.heqing.refreshlistview.R;
import com.heqing.refreshlistview.adapter.MyAdapter;
import com.heqing.refreshlistview.listener.RefreshLoadMoreListener;
import com.heqing.refreshlistview.model.Entity;
import com.heqing.refreshlistview.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity
        implements AdapterView.OnItemClickListener,RefreshLoadMoreListener {

    private RefreshListView listView;
    List<Entity> dataList = new ArrayList<>();
    private MyAdapter adapter;
    private int counter = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
//                    dataList.clear();
                    counter = 0;
                    for (int i = 0;i<5;i++){
                        Entity entity = new Entity("标题#"+counter
                                ,"内容#"+counter,"星期"+(counter % 7 + 1));
                        dataList.add(entity);
                        counter++;
                    }
                    adapter.notifyDataSetChanged();
                    listView.completeRefresh();
                    break;
                case 2:
                    for (int i = 0;i<10;i++){
                        Entity entity = new Entity("标题#"+counter
                                ,"内容#"+counter,"星期"+(counter % 7 + 1));
                        dataList.add(entity);
                        counter++;
                    }
                    adapter.notifyDataSetChanged();
                    listView.completeLoadMore();
                    break;
                case 3:
                    listView.startRefresh();
                    break;
                case 4:
                    listView.startLoadmore();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_list_layout);
        listView = (RefreshListView)findViewById(R.id.refresh_listview);

        adapter = new MyAdapter(this,dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setRefreshLoadMoreListener(this);

        listView.startRefresh();
    }

    @Override
    public void loadMore() {
        Message msg = handler.obtainMessage(2,null);
        handler.sendMessageDelayed(msg,4000);
    }

    @Override
    public void refresh() {
        Message msg = handler.obtainMessage(1,null);
        handler.sendMessageDelayed(msg,3000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击了" + position, Toast.LENGTH_SHORT).show();
    }
}
