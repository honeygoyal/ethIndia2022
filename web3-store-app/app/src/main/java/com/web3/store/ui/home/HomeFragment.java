package com.web3.store.ui.home;

import static java.util.stream.Collectors.groupingBy;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.web3.store.R;
import com.web3.store.client.RetrofitClient;
import com.web3.store.databinding.FragmentAppBinding;
import com.web3.store.model.AppElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private FragmentAppBinding binding;
    private AppFragment supplyChainFragment, socialMediaFragment, nftFragment, tradingFragment, defiFragment;
    private Map<String, List<AppElement>> categoryToAppElementMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentAppBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FragmentManager fragmentManager = getChildFragmentManager();
        supplyChainFragment = (AppFragment) fragmentManager.findFragmentById(R.id.supply_chain);
        socialMediaFragment = (AppFragment) fragmentManager.findFragmentById(R.id.social_media);
        nftFragment = (AppFragment) fragmentManager.findFragmentById(R.id.nft);
        tradingFragment = (AppFragment) fragmentManager.findFragmentById(R.id.trading);
        defiFragment = (AppFragment) fragmentManager.findFragmentById(R.id.de_fi);
        categoryToAppElementMap = new HashMap<>();
        getAllApps();
        if (supplyChainFragment == null) {
            supplyChainFragment = new AppFragment(1);
            fragmentManager.beginTransaction().add(R.id.supply_chain, supplyChainFragment).commit();
        }
        if (socialMediaFragment == null) {
            socialMediaFragment = new AppFragment(3);
            fragmentManager.beginTransaction().add(R.id.social_media, socialMediaFragment).commit();
        }
        if (nftFragment == null) {
            nftFragment = new AppFragment(1);
            fragmentManager.beginTransaction().add(R.id.nft, nftFragment).commit();
        }
        if (tradingFragment == null) {
            tradingFragment = new AppFragment(2);
            fragmentManager.beginTransaction().add(R.id.trading, tradingFragment).commit();
        }
        if (defiFragment == null) {
            defiFragment = new AppFragment(2);
            fragmentManager.beginTransaction().add(R.id.de_fi, defiFragment).commit();
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void getAllApps() {
        Call<List<AppElement>> call = RetrofitClient.getInstance().getMyApi().getAllApps();
        call.enqueue(new Callback<List<AppElement>>() {
            @Override
            public void onResponse(Call<List<AppElement>> call, Response<List<AppElement>> response) {
                List<AppElement> appElementList = response.body();
                if (appElementList == null) {
                    Log.e("Error", "Response is empty");
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    categoryToAppElementMap = appElementList.stream().collect(groupingBy(AppElement::getCategory));
                } else {
                    for (AppElement appElement : appElementList) {
                        if (categoryToAppElementMap.containsKey(appElement.getCategory())) {
                            List<AppElement> appElementList1 = categoryToAppElementMap.get(appElement.getCategory());
                            assert appElementList1 != null;
                            appElementList1.add(appElement);
                        } else {
                            categoryToAppElementMap.put(appElement.getCategory(), Arrays.asList(appElement));
                        }
                    }
                }
                if (categoryToAppElementMap.containsKey("supply chain")) {
                    supplyChainFragment.setAppElementList(categoryToAppElementMap.get("supply chain"));
                }
                if (categoryToAppElementMap.containsKey("social media")) {
                    socialMediaFragment.setAppElementList(categoryToAppElementMap.get("social media"));
                }
                if (categoryToAppElementMap.containsKey("nft"))
                    nftFragment.setAppElementList(categoryToAppElementMap.get("nft"));
                if (categoryToAppElementMap.containsKey("defi"))
                    defiFragment.setAppElementList(categoryToAppElementMap.get("defi"));
                if (categoryToAppElementMap.containsKey("trading"))
                    tradingFragment.setAppElementList(categoryToAppElementMap.get("trading"));
                Log.d("Lists of apps", appElementList.toString());
            }

            @Override
            public void onFailure(Call<List<AppElement>> call, Throwable t) {
                Log.e("Error Retrofit", t.toString());
            }
        });
    }
}