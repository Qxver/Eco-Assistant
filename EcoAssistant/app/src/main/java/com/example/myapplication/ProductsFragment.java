package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Collections;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private DBHandler dbHandler;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        
        dbHandler = new DBHandler(requireContext());

        FloatingActionButton addProductButton = view.findViewById(R.id.add_product_button);
        addProductButton.setOnClickListener(v -> navigateToAddProducts());

        productRecyclerView = view.findViewById(R.id.product_recycler_view);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadProducts();

        return view;
    }

    private void loadProducts() {
        List<Product> productList = dbHandler.getAllProducts();
        sortByExpirationDate(productList);
        productAdapter = new ProductAdapter(productList, product -> navigateToProductInfo(product));
        productRecyclerView.setAdapter(productAdapter);
    }

    private void sortByExpirationDate(List<Product> products) {
        products.sort((p1, p2) -> {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date1 = dateFormat.parse(p1.getExpirationDate());
                Date date2 = dateFormat.parse(p2.getExpirationDate());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                return 0;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload products when fragment comes back to foreground
        loadProducts();
    }

    private void navigateToAddProducts() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new AddProductsFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void navigateToProductInfo(Product product) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, ProductInfoFragment.newInstance(product));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}