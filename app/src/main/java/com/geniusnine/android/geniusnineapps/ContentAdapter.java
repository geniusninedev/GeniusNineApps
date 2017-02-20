package com.geniusnine.android.geniusnineapps;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by AndriodDev8 on 20-02-2017.
 */

public class ContentAdapter extends ArrayAdapter<AppContent> {

    Context mContext;

    int mLayoutResourceId;

    public ContentAdapter(Context context, int resource){
        super(context, resource);
        mContext = context;
        mLayoutResourceId = resource;

    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        final AppContent currentItem = getItem(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.row_list_content, parent, false);

        }
        row.setTag(currentItem);

        final TextView textViewContent = (TextView)row.findViewById(R.id.textViewContent);
        textViewContent.setText(currentItem.getContent());

        return row;







    }






}
