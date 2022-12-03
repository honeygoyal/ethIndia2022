package com.web3.store.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.web3.store.Imageloader;
import com.android.volley.toolbox.NetworkImageView;
import com.web3.store.R;
import com.web3.store.VolleySingleton;
import com.web3.store.model.AppElement;
import com.web3.store.ui.AppViewActivity;
import java.util.ArrayList;
import java.util.List;

public class AppFragment extends Fragment {

    private int columnCount;
    Context context;
    List<AppElement> appElementList;
    AppAdapter appAdapter;
    VolleySingleton volleySingleton;
    private String BASE_URL = "http://54.249.38.16:8080/file/";

    public AppFragment(int columnCount) {
        this.columnCount = columnCount;
        appElementList = new ArrayList<>();
    }

    public void setAppElementList(List<AppElement> appElementList) {
        this.appElementList = appElementList;
        if(appAdapter == null) appAdapter = new AppAdapter(this.appElementList);
        else {
            appAdapter.updateList(this.appElementList);
        }
        appAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_app, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.appRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                columnCount, GridLayoutManager.HORIZONTAL, false));
        context = getActivity();
        volleySingleton = VolleySingleton.getInstance(context);
        appAdapter = new AppAdapter(this.appElementList);
        recyclerView.setAdapter(appAdapter);
        appAdapter.notifyDataSetChanged();
        return view;
    }


    private class AppAdapter extends RecyclerView.Adapter<AppDataHolder> {

       // private AppData appData;
       List<AppElement> appElements;
        public AppAdapter(List<AppElement> appElementList) {
            //appData = AppData.get(appElementList);
            this.appElements = appElementList;
        }


        @NonNull
        @Override
        public AppDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View itemView = layoutInflater.inflate(R.layout.display_app, parent, false);
            return new AppDataHolder(itemView, parent);
        }

        @Override
        public void onBindViewHolder(AppDataHolder appDataHolder, int index) {
            AppElement appElement = appElements == null ?
                    new AppElement("Behance", "QmNWwiqxziEUFFBCyAbRxfPc83n9ZLscbvefXcKe6zWSAr", "QmaxDK6YpcoiPrbNfF6U5a7uJ2m7ZxizJd1nX89XYMG4SK", "social_media")
                    :appElements.get(appDataHolder.getAdapterPosition()); //appData.get(row, col);
            StringBuilder imageUrl = new StringBuilder(BASE_URL);
            imageUrl.append(appElement.getIconCid());
            imageUrl.append("/icon.png");
            appDataHolder.appIconView.setImageUrl(imageUrl.toString(), volleySingleton.getImageLoader());
            appDataHolder.bind(appElement);
            appDataHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AppViewActivity.class);
                    intent.putExtra("appData", appElements.get(appDataHolder.getAdapterPosition()));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return appElements.size();
        }

        public void updateList(List<AppElement> appElementList) {
            appElements.clear();
            appElements.addAll(appElementList);
            this.notifyDataSetChanged();
        }
    }

    private class AppDataHolder extends RecyclerView.ViewHolder {
        NetworkImageView appIconView;
        TextView appTextView;

        public AppDataHolder(View itemView, ViewGroup parent) {
            super(itemView);
            appIconView = (NetworkImageView) itemView.findViewById(R.id.app_icon);
            appTextView = (TextView) itemView.findViewById(R.id.app_text);
        }

        public void bind(AppElement appElement) {
            String text = appElement.getName();
            appTextView.setText(text);
        }
    }

}