package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProductInfoFragment extends Fragment {

    private static final String ARG_PRODUCT = "product";
    private Product product;

    public ProductInfoFragment() {
        // Required empty public constructor
    }

    public static ProductInfoFragment newInstance(Product product) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PRODUCT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);

        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> goBack());

        if (product != null) {
            ((TextView) view.findViewById(R.id.product_name_detail)).setText(product.getName());
            ((TextView) view.findViewById(R.id.product_price_detail)).setText(product.getPrice() + "$");
            ((TextView) view.findViewById(R.id.product_category_detail)).setText("" + product.getCategory());
            ((TextView) view.findViewById(R.id.product_expiration_date_detail)).setText("" + product.getExpirationDate());
            ((TextView) view.findViewById(R.id.product_purchase_date_detail)).setText("" + product.getPurchaseDate());
            ((TextView) view.findViewById(R.id.product_shop_name_detail)).setText("" + product.getShopName());
            ((TextView) view.findViewById(R.id.product_description_detail)).setText("" + product.getDescription());
        }

        return view;
    }

    private void goBack() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}