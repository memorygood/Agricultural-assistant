package com.njau.agricultural_assistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ListViewAdapter extends BaseAdapter {

	public List<Map<String,Object>> list;
	public LayoutInflater inflater;


	public ListViewAdapter() {
	}

	public ListViewAdapter(Context context, List<Map<String,Object>> list) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return String.valueOf(list.get(position));
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void updateView(List<Map<String,Object>> nowList)
	{
		this.list = nowList;
		this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		if(convertView == null)
		{
			view = inflater.inflate(R.layout.news_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView)view.findViewById(R.id.news_listitem_title);
			holder.date = (TextView)view.findViewById(R.id.news_listitem_date);
			view.setTag(holder);//为了复用holder
		}else
		{
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.title.setText((CharSequence) list.get(position));
		holder.date.setText((CharSequence) list.get(position));
		return view;
	}
	static class ViewHolder
	{
		TextView title;
		TextView date;
	}
}

