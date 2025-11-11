package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProductInfoFragment extends Fragment {

    private static final String ARG_PRODUCT = "product";
    private Product product;
    private EditText productName, productPrice, productCategory, productExpirationDate, productPurchaseDate, productShopName, productDescription;
    private ExtendedFloatingActionButton saveEditButton, cancelButton;
    private LinearLayout buttonLayout;
    private boolean isEditing = false;
    private DBHandler dbHandler;
    private Calendar expirationCalendar = Calendar.getInstance();
    private Calendar purchaseCalendar = Calendar.getInstance();

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
        saveEditButton = view.findViewById(R.id.save_edit_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        buttonLayout = view.findViewById(R.id.button_layout);

        productExpirationDate.setOnClickListener(v -> {
            if (isEditing) {
                showExpirationDatePicker();
            }
        });

        productPurchaseDate.setOnClickListener(v -> {
            if (isEditing) {
                showPurchaseDatePicker();
            }
        });

        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> goBack());

        ImageButton editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(v -> toggleEdit());

        saveEditButton.setOnClickListener(v -> saveProduct());
        cancelButton.setOnClickListener(v -> toggleEdit());

        ImageButton deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete this product?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        if (product != null) {
                            dbHandler.deleteProduct(product.getId());
                            Toast.makeText(getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                        }
                        goBack();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        if (product != null) {
            productName.setText(product.getName());
            productPrice.setText(String.valueOf(product.getPrice()));
            productCategory.setText("" + product.getCategory());
            productExpirationDate.setText("" + product.getExpirationDate());
            productPurchaseDate.setText("" + product.getPurchaseDate());
            productShopName.setText("" + product.getShopName());
            productDescription.setText("" + product.getDescription());
        }

        EditProduct(false);
        buttonLayout.setVisibility(View.GONE);

        return view;
    }

    private void goBack() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private void toggleEdit() {
        isEditing = !isEditing;
        EditProduct(isEditing);
        if (isEditing) {
            buttonLayout.setVisibility(View.VISIBLE);
        } else {
            buttonLayout.setVisibility(View.GONE);
        }
    }

    private void EditProduct(boolean editing) {
        productName.setEnabled(editing);
        productPrice.setEnabled(editing);
        productCategory.setEnabled(editing);
        productExpirationDate.setEnabled(editing);
        productPurchaseDate.setEnabled(editing);
        productShopName.setEnabled(editing);
        productDescription.setEnabled(editing);
    }

    private void saveProduct() {
        String name = productName.getText().toString().trim();
        String priceString = productPrice.getText().toString().trim().replace("$", "");
        String category = productCategory.getText().toString().trim();
        String expirationDate = productExpirationDate.getText().toString().trim();
        String purchaseDate = productPurchaseDate.getText().toString().trim();
        String shopName = productShopName.getText().toString().trim();
        String description = productDescription.getText().toString().trim();

        if (name.isEmpty() || priceString.isEmpty() || expirationDate.isEmpty() || 
            category.isEmpty() || shopName.isEmpty() || purchaseDate.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceString);
            if (price <= 0) {
                Toast.makeText(getContext(), "Price must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price format.", Toast.LENGTH_SHORT).show();
            return;
        }

        Product updatedProduct = new Product(product.getId(), name, price, expirationDate, category, shopName, purchaseDate, description);
        dbHandler.updateProduct(updatedProduct);
        product = updatedProduct;

        Toast.makeText(getContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();

        toggleEdit();
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
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
        productExpirationDate.setText(dateFormat.format(expirationCalendar.getTime()));
    }

    private void updatePurchaseDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        productPurchaseDate.setText(dateFormat.format(purchaseCalendar.getTime()));
    }
}
