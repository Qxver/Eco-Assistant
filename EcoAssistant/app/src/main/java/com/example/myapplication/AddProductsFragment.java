package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddProductsFragment extends Fragment {

    private EditText productNameInput;
    private EditText productPriceInput;
    private EditText productExpirationDateInput;
    private EditText productCategoryInput;
    private EditText productShopNameInput;
    private EditText productPurchaseDateInput;
    private EditText productDescriptionInput;
    private Button addButton;
    private DBHandler dbHandler;

    public AddProductsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_products, container, false);

        dbHandler = new DBHandler(requireContext());

        productNameInput = view.findViewById(R.id.product_name_input);
        productPriceInput = view.findViewById(R.id.product_price_input);
        productExpirationDateInput = view.findViewById(R.id.product_expiration_date_input);
        productCategoryInput = view.findViewById(R.id.product_category_input);
        productShopNameInput = view.findViewById(R.id.product_shop_name_input);
        productPurchaseDateInput = view.findViewById(R.id.product_purchase_date_input);
        productDescriptionInput = view.findViewById(R.id.product_description_input);

        addButton = view.findViewById(R.id.add_product_button);
        addButton.setOnClickListener(v -> saveProductToDatabase());
        
        return view;
    }
    
    private void saveProductToDatabase() {
        String name = productNameInput.getText().toString().trim();
        String priceStr = productPriceInput.getText().toString().trim();
        String expirationDate = productExpirationDateInput.getText().toString().trim();
        String category = productCategoryInput.getText().toString().trim();
        String shopName = productShopNameInput.getText().toString().trim();
        String purchaseDate = productPurchaseDateInput.getText().toString().trim();
        String description = productDescriptionInput.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || expirationDate.isEmpty() || 
            category.isEmpty() || shopName.isEmpty() || purchaseDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDate(expirationDate)) {
            Toast.makeText(requireContext(), "Expiration date is incorrect. Use dd/MM/yyyy format.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!isValidDate(purchaseDate)) {
            Toast.makeText(requireContext(), "Purchase date is incorrect. Use dd/MM/yyyy format.", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceStr);
        dbHandler.addNewProduct(name, price, expirationDate, category, shopName, purchaseDate, description);
        Toast.makeText(requireContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();

    }
    
    private boolean isValidDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}