package com.example.unifood.Main.Fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.unifood.Main.Data.Retrieve;
import com.example.unifood.Main.Extension.AlertPermission;
import com.example.unifood.Main.Extension.DateComparison;
import com.example.unifood.Main.Extension.editTextChange;
import com.example.unifood.Main.Main.MainActivity;
import com.example.unifood.Main.Scanner.CapAct;
import com.example.unifood.Main.Scanner.OpenFoodFactsApiClient;
import com.example.unifood.Main.Scanner.ReadQRCode;
import com.example.unifood.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, Camera.PictureCallback{

    Button button;
    private static final int CAMERA_REQUEST = 1888;
    String permission; String productName = "";

    JSONArray namesArray;
    private SurfaceHolder surfaceHolder;
    private Camera camera;

    public static final int REQUEST_CODE = 100;

    private SurfaceView surfaceView;
    int cameraID = 1;

    private static final String DATABASE_NAME = "my_database";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    DatePickerDialog.OnDateSetListener mDataSetListener;

    BottomSheetDialog bottomSheetDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_camera_fragment,
                container, false);

        button = view.findViewById(R.id.scan_qr_button);
        surfaceView = view.findViewById(R.id.surfaceArea);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        cameraID =  Camera.CameraInfo.CAMERA_FACING_BACK;

        requestCameraPermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productName = "";
                launchQRScanner();
            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                month = month + 1;

                String currentDay = DateComparison.currentDay();
                String expiryDate = day + "/" + month + "/" + year;

                Retrieve.insertRow(getActivity(), productName, currentDay, expiryDate);

                bottomSheetDialog.dismiss();
            }
        };
        return view;
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Check if the user has denied the permission before
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                AlertPermission.CameraPermission(getActivity(), getContext());
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        201);
            }
        } else {
            // Permission has already been granted
            // Start the camera preview
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If user presses allow
                    startCamera();
                }
                break;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            startCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCamera() {
        camera = Camera.open(cameraID);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchQRScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
        options.setPrompt("Scan a barcode");
        options.setCameraId(0);  // Use a specific camera of the device
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setBarcodeImageEnabled(true);
        options.setCaptureActivity(CapAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), scanResult -> {
        if(scanResult.getBarcodeImagePath() != null){
            Log.d("XAS", scanResult.getBarcodeImagePath());

            Result result = ReadQRCode.decodeQR(scanResult.getBarcodeImagePath());
            if(result != null){
                String barcode = result.getText();
                final Handler handler = new Handler(Looper.getMainLooper());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            try {
                                JSONObject productJson = OpenFoodFactsApiClient.getProductInfo(barcode);
                                if (productJson.has("product_name")) {
                                    productName = productJson.getString("product_name");
                                } else{
                                    namesArray = productJson.getJSONArray("_keywords");
                                }
                                String currentDay = DateComparison.currentDay();
                                String expiryDate = "";
                                if(productJson.has("expiration_date")) expiryDate = productJson.optString("expiration_date");

                                if(!expiryDate.equals("")){
                                    Retrieve.insertRow(getActivity(), productName, currentDay, expiryDate);
                                }else{
                                    openBottomSheet();
                                }
                            } catch (IOException | JSONException e) {

                                openBottomSheet();
                                e.printStackTrace();
                                Log.d("XAS", e.toString());
                            }
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // handle the exception in the main thread
                                    openBottomSheet();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }else{
                openBottomSheet();
            }
        }else{
            openBottomSheet();
        }
    });

    private void openBottomSheet()
    {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.activity_add_sheet);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        EditText editText;
        Button dob = bottomSheetDialog.findViewById(R.id.button);
        editText = bottomSheetDialog.findViewById(R.id.food_name);
        NumberPicker spinner = bottomSheetDialog.findViewById(R.id.spinner);

        String[] keywords;
        if(namesArray != null){
            keywords = new String[namesArray.length()];
            for(int i = 0; i < keywords.length; i++){
                try {
                    String keyword = namesArray.getString(i);
                    if (keyword == null) {
                        continue;
                    }
                    keywords[i] = keyword;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            spinner.setMinValue(0);
            spinner.setMaxValue(keywords.length - 1);
            spinner.setDisplayedValues(keywords);

            for(int i = 0; i < keywords.length; i++){
                if(keywords[i].equals(editText.getText().toString())){
                    spinner.setValue(i);
                }
            }

            spinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    editText.setText(keywords[newVal]);
                    productName = keywords[newVal];
                }
            });
        }

        if(!productName.equals("")){
            Log.d("XAS", productName);
            editText.setText(productName);
            editText.setEnabled(false);
        }else if(namesArray != null){
            spinner.setVisibility(View.VISIBLE);
            dob.setVisibility(View.GONE);

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spinner.setVisibility(View.GONE);
                    dob.setVisibility(View.VISIBLE);
                }
            });
        }

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.GONE);
                dob.setVisibility(View.VISIBLE);
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = editText.getText().toString().trim();
                String stringWithoutSpaces = inputString.replaceAll(" ", "");

                if (!stringWithoutSpaces.isEmpty()) {
                    productName = inputString;
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            getContext(),
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDataSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setTitle("Expiry Date");
                    dialog.show();
                }else{
                    editText.setText(null);
                    editText.setHint("Food name is empty");
                    editText.setHintTextColor(getActivity().getResources().getColor(R.color.red));
                    editTextChange.changeTextColor(editText, getActivity());
                }
            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        resetCamera();
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (camera != null) {
            // Stop if preview surface is already running.
            camera.stopPreview();
            try {
                // Set preview display
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera.startPreview();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        // Picture had been taken by camera. So, do appropriate action. For example, save it in file.

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

        }
    }
    public static CameraFragment newInstance() {

        CameraFragment f = new CameraFragment();
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }
}
