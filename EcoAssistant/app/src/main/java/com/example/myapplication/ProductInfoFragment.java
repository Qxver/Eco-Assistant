package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.EditText;

public class ProductInfoFragment extends Fragment {

    private static final String ARG_PRODUCT = "product";
    private Product product;
    private EditText productName, productPrice, productCategory, productExpirationDate, productPurchaseDate, productShopName, productDescription;
    private boolean isEditing = false;
    private DBHandler dbHandler;

    public ProductInfoFragment() {
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

        dbHandler = new DBHandler(requireContext());

        productName = view.findViewById(R.id.product_name_detail);
        productPrice = view.findViewById(R.id.product_price_detail);
        productCategory = view.findViewById(R.id.product_category_detail);
        productExpirationDate = view.findViewById(R.id.product_expiration_date_detail);
        productPurchaseDate = view.findViewById(R.id.product_purchase_date_detail);
        productShopName = view.findViewById(R.id.product_shop_name_detail);
        productDescription = view.findViewById(R.id.product_description_detail);

        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> goBack());

        ImageButton editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(v -> toggleEdit());

        ImageButton deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> {
            if (product != null) {
                dbHandler.deleteProduct(product.getId());
            }
            goBack();
        });

        if (product != null) {
            productName.setText(product.getName());
            productPrice.setText(product.getPrice() + "$");
            productCategory.setText("" + product.getCategory());
            productExpirationDate.setText("" + product.getExpirationDate());
            productPurchaseDate.setText("" + product.getPurchaseDate());
            productShopName.setText("" + product.getShopName());
            productDescription.setText("" + product.getDescription());
        }

        setEditing(false);

        return view;
    }

    private void goBack() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private void toggleEdit() {
        isEditing = !isEditing;
        setEditing(isEditing);
    }

    private void setEditing(boolean editing) {
        productName.setEnabled(editing);
        productPrice.setEnabled(editing);
        productCategory.setEnabled(editing);
        productExpirationDate.setEnabled(editing);
        productPurchaseDate.setEnabled(editing);
        productShopName.setEnabled(editing);
        productDescription.setEnabled(editing);
    }
}