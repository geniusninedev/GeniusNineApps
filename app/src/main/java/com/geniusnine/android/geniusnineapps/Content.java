package com.geniusnine.android.geniusnineapps;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.geniusnine.android.geniusnineapps.R;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncTable;
import com.squareup.okhttp.OkHttpClient;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.Query;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOperations;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncContext;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncTable;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.ColumnDataType;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.MobileServiceLocalStoreException;
import com.microsoft.windowsazure.mobileservices.table.sync.localstore.SQLiteLocalStore;
import com.microsoft.windowsazure.mobileservices.table.sync.synchandler.SimpleSyncHandler;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.val;

public class Content extends AppCompatActivity {

    private MobileServiceClient mClient;

    //private MobileServiceTable<AppContent> mContent;

    private MobileServiceSyncTable<AppContent> mContent;
    private ContentAdapter mAdapter;
    private ListView listViewContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
Intent intent = getIntent();
        String cat = intent.getStringExtra("category");


        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient("https://doctorfe.azurewebsites.net", this);

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            //mContent = mClient.getTable(AppContent.class);
            mContent = mClient.getSyncTable("AppContent", AppContent.class);
            initLocalStore().get();
            mAdapter = new ContentAdapter(this, R.layout.row_list_content);
            listViewContent = (ListView)findViewById(R.id.listViewContent);
            listViewContent.setAdapter(mAdapter);


            showAll(cat);


        }
        catch (MalformedURLException e) {
            //createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            // createAndShowDialog(e, "Error");
        }
    }
    private AsyncTask<Void, Void, Void> sync() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MobileServiceSyncContext syncContext = mClient.getSyncContext();
                    syncContext.push().get();
                    mContent.pull(null).get();
                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }
                return null;
            }
        };
        return runAsyncTask(task);
    }
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
    public void showAll(String cat) {
        final String categoryId = cat;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            @Override

            protected Void doInBackground(Void... params) {
                try {

                    sync().get();
                    Query query = QueryOperations.field("category").eq(categoryId);
                    final List<AppContent> results = mContent.read(query).get();
                    //final List<AppContent> results = mContent.where().field("category").eq(categoryId).execute().get();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mAdapter.clear();
                            for (AppContent item : results) {
                                mAdapter.add(item);
                            }
                        }
                    });
                } catch (Exception exception) {
                    //createAndShowDialog(exception, "Error");
                }
                return null;
            }
        };
        //runAsyncTask(task);
        task.execute();
    }

    private AsyncTask<Void, Void, Void> initLocalStore() throws MobileServiceLocalStoreException, ExecutionException, InterruptedException {

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    MobileServiceSyncContext syncContext = mClient.getSyncContext();

                    if (syncContext.isInitialized())
                        return null;

                    SQLiteLocalStore localStore = new SQLiteLocalStore(mClient.getContext(), "OfflineStore", null, 1);

                    Map<String, ColumnDataType> tableDefinition = new HashMap<String, ColumnDataType>();
                    tableDefinition.put("id", ColumnDataType.String);
                    tableDefinition.put("category", ColumnDataType.String);
                    tableDefinition.put("content", ColumnDataType.String);

                    localStore.defineTable("AppContent", tableDefinition);

                    SimpleSyncHandler handler = new SimpleSyncHandler();

                    syncContext.initialize(localStore, handler).get();

                } catch (final Exception e) {
                    //createAndShowDialogFromTask(e, "Error");
                }

                return null;
            }
        };

        return runAsyncTask(task);
    }
}
