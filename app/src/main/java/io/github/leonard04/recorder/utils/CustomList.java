package io.github.leonard04.recorder.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.leonard04.recorder.R;

public class CustomList extends ArrayAdapter<String> {

    private String[] titles;
    private Integer[] imgs;

    private Activity mContext;

    public CustomList(@NonNull Context context, String[] titles, Integer[] imgs) {
        super(context, R.layout.listview_layout, titles);
        this.mContext = (Activity) context;
        this.titles = titles;
        this.imgs = imgs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = mContext.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_layout, null);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();

        }
        viewHolder.im1.setImageResource(imgs[position]);
        viewHolder.tv1.setText(titles[position]);
        return r;


    }

    class ViewHolder {
        TextView tv1;
        ImageView im1;

        ViewHolder(View view) {
            tv1 = view.findViewById(R.id.tvTitleAudio);
            im1 = view.findViewById(R.id.imageV);

        }
    }
}
