package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDepositFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDepositFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ActivityResultLauncher<ScanOptions> barcodeLauncher;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText packagingTypeInput;
    private EditText depositValueInput;
    private EditText qrCodeInput;
    private DBHandler dbHandler;

    public AddDepositFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDepositFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDepositFragment newInstance(String param1, String param2) {
        AddDepositFragment fragment = new AddDepositFragment();
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

        barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                qrCodeInput.setText(result.getContents());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_deposit, container, false);
        
        dbHandler = new DBHandler(requireContext());

        packagingTypeInput = view.findViewById(R.id.packaging_type_input);
        depositValueInput = view.findViewById(R.id.deposit_value_input);
        qrCodeInput = view.findViewById(R.id.qr_code_input);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button addDepositButton = view.findViewById(R.id.add_deposit_button);
        addDepositButton.setOnClickListener(v -> saveDepositToDatabase());

        Button scanButton = view.findViewById(R.id.scan_qr);
        scanButton.setOnClickListener(v -> scanQR());

        return view;

    }

    private void scanQR() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
        options.setPrompt("Scan a barcode");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);
        barcodeLauncher.launch(options);
    }

    private void saveDepositToDatabase() {
        String packagingType = packagingTypeInput.getText().toString().trim();
        String depositValueStr = depositValueInput.getText().toString().trim();
        String barcode = qrCodeInput.getText().toString().trim();

        if (packagingType.isEmpty() || depositValueStr.isEmpty() || barcode.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int depositValue = Integer.parseInt(depositValueStr);
            if (depositValue <= 0) {
                Toast.makeText(requireContext(), "Deposit value must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Get today's date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String addedDate = dateFormat.format(Calendar.getInstance().getTime());
            
            dbHandler.addNewDeposit(packagingType, depositValue, barcode, addedDate);
            Toast.makeText(requireContext(), "Deposit added successfully!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid deposit value format", Toast.LENGTH_SHORT).show();
        }
    }


}