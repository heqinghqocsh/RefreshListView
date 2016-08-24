package com.heqing.refreshlistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heqing.refreshlistview.R;
import com.heqing.refreshlistview.model.Entity;

import java.util.List;


/**
 * Created by 何清 on 2016/7/22.
 *
 * @description
 */
public class MyAdapter extends BaseAdapter {

    private List<Entity> entityList;
    private Context context;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, List<Entity> entityList) {
        this.context = context;
        this.entityList = entityList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return entityList.size();
    }

    @Override
    public Object getItem(int position) {
        return entityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.refresh_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Entity entity = entityList.get(position);
        holder.content.setText(entity.getContent());
        holder.title.setText(entity.getTitle());
        holder.time.setText(entity.getTime());
        return convertView;
    }

    class ViewHolder {
        public TextView title;
        public TextView content;
        public TextView time;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.title);
            time = (TextView) view.findViewById(R.id.time);
        }

    }


}
