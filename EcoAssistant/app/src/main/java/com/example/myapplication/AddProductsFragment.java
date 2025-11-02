package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddProductsFragment extends Fragment {

    private EditText productNameInput;
    private EditText productPriceInput;
    private TextView productExpirationDateInput;
    private EditText productCategoryInput;
    private EditText productShopNameInput;
    private TextView productPurchaseDateInput;
    private EditText productDescriptionInput;
    private Button addButton;
    private DBHandler dbHandler;
    private Calendar expirationCalendar = Calendar.getInstance();
    private Calendar purchaseCalendar = Calendar.getInstance();

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

        productExpirationDateInput.setOnClickListener(v -> showExpirationDatePicker());

        productPurchaseDateInput.setOnClickListener(v -> showPurchaseDatePicker());

        addButton = view.findViewById(R.id.add_product_button);
        addButton.setOnClickListener(v -> saveProductToDatabase());
        
        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        
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
            category.isEmpty() || shopName.isEmpty() || purchaseDate.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int price = Integer.parseInt(priceStr);
            dbHandler.addNewProduct(name, price, expirationDate, category, shopName, purchaseDate, description);
            Toast.makeText(requireContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExpirationDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            expirationCalendar.set(Calendar.YEAR, year);
            expirationCalendar.set(Calendar.MONTH, monthOfYear);
            expirationCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateExpirationDateDisplay();
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                expirationCalendar.get(Calendar.YEAR),
                expirationCalendar.get(Calendar.MONTH),
                expirationCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showPurchaseDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            purchaseCalendar.set(Calendar.YEAR, year);
            purchaseCalendar.set(Calendar.MONTH, monthOfYear);
            purchaseCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatePurchaseDateDisplay();
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                purchaseCalendar.get(Calendar.YEAR),
                purchaseCalendar.get(Calendar.MONTH),
                purchaseCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateExpirationDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        productExpirationDateInput.setText(dateFormat.format(expirationCalendar.getTime()));
    }

    private void updatePurchaseDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        productPurchaseDateInput.setText(dateFormat.format(purchaseCalendar.getTime()));
    }
}