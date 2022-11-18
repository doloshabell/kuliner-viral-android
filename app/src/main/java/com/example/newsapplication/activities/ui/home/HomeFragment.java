package com.example.newsapplication.activities.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.newsapplication.R;
import com.example.newsapplication.databinding.FragmentHomeBinding;
import com.example.newsapplication.enums.Apis;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentHomeBinding binding;

    private ProgressDialog progressDialog;

    ArrayList<HashMap<String, String>> listKuliner;
    String nama, keterangan;

    SimpleAdapter adapter;

    SearchView searchView;
    ListView lv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /*binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/

        getDataKuliner();

        listKuliner = new ArrayList<>();
        View contentView = inflater.inflate(R.layout.fragment_home, container, false);
        lv = contentView.findViewById(R.id.listViewKuliner);

        /*ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mobileArray);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.list_item, mobileArray);*/

        SwipeRefreshLayout swipeRefreshLayout = contentView.findViewById(R.id.swipeToRefreshKuliner);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listKuliner.clear();
                getDataKuliner();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView = contentView.findViewById(R.id.svListKuliner);
        searchView.setActivated(true);
        /*searchView.onActionViewCollapsed();*/
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(this);

        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void getDataKuliner() {
        showProgressDialog();

        AndroidNetworking.initialize(getActivity());
        AndroidNetworking.get(Apis.DATA_CULINARY)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject newJSONObject = jsonArray.getJSONObject(i);
                                nama = newJSONObject.getString("nama");
                                keterangan = newJSONObject.getString("keterangan");

                                HashMap<String, String> kuliner = new HashMap<>();
                                kuliner.put("nama", nama);
                                kuliner.put("keterangan", keterangan);
                                listKuliner.add(kuliner);
                            }
                            Log.d("List kuliner", "onResponse: " + listKuliner);

                            adapter = new SimpleAdapter(getActivity(), listKuliner, R.layout.list_item,
                                    new String[] {"nama", "keterangan"}, new int[]{R.id.textView1, R.id.textView2});
                            lv.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("catch error", "onResponse: yah error");
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error", "onError: yahh error");
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        adapter.getFilter().filter(text);
        return false;
    }

    /*public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listKuliner.clear();
        if (charText.length() == 0) {
            listKuliner.addAll(arrayList);
        } else {
            for (HashMap hm : listKuliner) {
                if (hm.get("nama").equals(charText)) {
                    listKuliner.add(hm);
                }
            }

            lv.setAdapter(adapter);
        }
    }*/
}