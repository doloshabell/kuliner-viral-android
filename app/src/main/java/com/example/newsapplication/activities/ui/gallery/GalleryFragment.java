package com.example.newsapplication.activities.ui.gallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.newsapplication.R;
import com.example.newsapplication.databinding.FragmentGalleryBinding;
import com.example.newsapplication.product_grid_view.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    /*GridView coursesGV;*/

    ArrayList<HashMap<String, ?>> listProduct;
    String nameProduct, imageProduct, priceProduct, ratingProduct;
    GridView gridViewProduct;

    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;*/

        listProduct = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        /*coursesGV = view.findViewById(R.id.gridView);*/
        gridViewProduct = view.findViewById(R.id.gridView);

        setContentGridView();
        return view;
    }

    private void setContentGridView() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.initialize(getActivity());
        AndroidNetworking.get("https://fakestoreapi.com/products")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objectProduct = jsonArray.getJSONObject(i);
                                JSONObject rate = objectProduct.getJSONObject("rating");
                                nameProduct = objectProduct.getString("title");
                                imageProduct = objectProduct.getString("image");
                                priceProduct = objectProduct.getString("price");
                                ratingProduct = rate.getString("rate");

                                HashMap<String, Object> product = new HashMap<>();
                                product.put("nameProduct", nameProduct);
                                product.put("imageProduct", imageProduct);
                                product.put("priceProduct", priceProduct);
                                product.put("ratingProduct", ratingProduct);
                                listProduct.add(product);
                            }
                            Log.d("SUCCESS TRY TAG", "onResponse: " + listProduct);

                            ProductAdapter adapter = new ProductAdapter(getActivity(), listProduct,
                                    R.layout.grid_item_card, new String[] {}, new int[] {});
                            gridViewProduct.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR CATCH TAG", "onResponse: " + e.getMessage());
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR TAG", "onError: " + anError.getMessage());
                        progressDialog.dismiss();
                    }
                });

        /*ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();

        courseModelArrayList.add(new CourseModel("DSA", R.drawable.androidstudioicon));
        courseModelArrayList.add(new CourseModel("JAVA", R.drawable.androidstudioicon));
        courseModelArrayList.add(new CourseModel("C++", R.drawable.androidstudioicon));
        courseModelArrayList.add(new CourseModel("Python", R.drawable.androidstudioicon));
        courseModelArrayList.add(new CourseModel("Javascript", R.drawable.androidstudioicon));
        courseModelArrayList.add(new CourseModel("DSA", R.drawable.androidstudioicon));

        courseModelArrayList.add(new CourseModel("DSA", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"));
        courseModelArrayList.add(new CourseModel("JAVA", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"));
        courseModelArrayList.add(new CourseModel("C++", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"));
        courseModelArrayList.add(new CourseModel("Python", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"));
        courseModelArrayList.add(new CourseModel("Javascript", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"));
        courseModelArrayList.add(new CourseModel("DSA", "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"));

        CourseGVAdapter adapter = new CourseGVAdapter(getContext(), courseModelArrayList);
        coursesGV.setAdapter(adapter);*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}