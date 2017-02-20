package com.geniusnine.android.geniusnineapps;

/**
 * Created by AndriodDev8 on 20-02-2017.
 *
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CategoryAdapter extends ArrayAdapter<AppCategory> {
    Context mContext;

    int mLayoutResourceId;

    public CategoryAdapter(Context context, int resource){
        super(context, resource);
        mContext = context;
        mLayoutResourceId = resource;

    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        final AppCategory currentItem = getItem(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.row_list_category, parent, false);

        }
        row.setTag(currentItem);

        final TextView textViewCategory = (TextView)row.findViewById(R.id.textViewCategories);
        textViewCategory.setText(currentItem.getCategory());
        textViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, currentItem.getCategory(), Toast.LENGTH_LONG).show();

            }
        });
        return row;

    }






}
