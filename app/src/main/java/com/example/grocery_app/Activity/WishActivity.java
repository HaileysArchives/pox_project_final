package com.example.grocery_app.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.grocery_app.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WishActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView textViewResult;
    private ImageView imageViewQRCode; // ImageView to display the QR code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
        }

        Button btnOCR = findViewById(R.id.btnOCR);
        textViewResult = findViewById(R.id.txtResult);
        imageViewQRCode = findViewById(R.id.WishQrCode); // Make sure you have an ImageView with this ID in your layout

        btnOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            sendImageToCloudVision(imageUri);
        }
    }

    private void sendImageToCloudVision(Uri imageUri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

            String jsonBody = "{"
                    + "\"requests\": [{"
                    + "\"image\": {\"content\": \"" + base64Image + "\"},"
                    + "\"features\": [{\"type\": \"TEXT_DETECTION\"}]"
                    + "}]"
                    + "}";

            String apiKey = "YOUR_API_KEY"; // Replace with your actual API key
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
            Request request = new Request.Builder()
                    .url("https://vision.googleapis.com/v1/images:annotate?key=" + apiKey)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("WishActivity", "OCR API Request Failed", e);
                    runOnUiThread(() -> textViewResult.setText("OCR API Request Failed: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String responseBody = response.body().string();
                        runOnUiThread(() -> {
                            try {
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String ocrResult = jsonObject.getJSONArray("responses")
                                        .getJSONObject(0)
                                        .getJSONArray("textAnnotations")
                                        .getJSONObject(0)
                                        .getString("description");
                                textViewResult.setText(ocrResult);
                                generateQRCode(ocrResult); // Call to generate a QR code
                            } catch (JSONException e) {
                                textViewResult.setText("Error parsing OCR response");
                                Log.e("WishActivity", "Error parsing OCR response", e);
                            }
                        });
                    } else {
                        Log.e("WishActivity", "OCR API Request was not successful. Response code: " + response.code());
                        runOnUiThread(() -> textViewResult.setText("OCR Request was not successful: " + response.message()));
                    }
                }
            });
        } catch (IOException e) {
            Log.e("WishActivity", "Image load failed", e);
            runOnUiThread(() -> textViewResult.setText("Image load failed: " + e.getMessage()));
        }
    }

    private void generateQRCode(String text) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            runOnUiThread(() -> imageViewQRCode.setImageBitmap(bitmap));
        } catch (WriterException e) {
            Log.e("WishActivity", "Error generating QR code", e);
        }
    }
}
