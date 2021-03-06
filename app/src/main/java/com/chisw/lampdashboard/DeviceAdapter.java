package com.chisw.lampdashboard;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

public class DeviceAdapter extends BaseAdapter {
    private final Map<String, Device> devices = new ArrayMap<>();
    private final LayoutInflater layoutInflater;

    public DeviceAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void add(Device device) {
        String key = device.getFriendlyName() + device.getPort();
        devices.put(key, device);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Device getItem(int position) {
        return devices.values().toArray(new Device[getCount()])[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Device device = getItem(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_device, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvDeviceName.setText(device.getName());
        return convertView;
    }

    private static class ViewHolder {
        final TextView tvDeviceName;

        public ViewHolder(View view) {
            tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
        }
    }
}
