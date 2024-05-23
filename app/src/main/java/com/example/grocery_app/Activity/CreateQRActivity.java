package com.example.grocery_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.graphics.Bitmap;
import com.example.grocery_app.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
public class CreateQRActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        JSONObject json = new JSONObject();
        try {
            json.put("brand", new JSONArray().put("오리온").put("롯데").put("농협").put("동아").put("농심"));
            json.put("category", new JSONArray().put("가공").put("가공").put("신선").put("가공").put("가공"));
            json.put("product", new JSONArray().put("썬칩").put("빼빼로 오리지날").put("레몬").put("포카리스웨트").put("먹태깡"));
            json.put("item", new JSONArray().put("과자").put("과자").put("레몬").put("가공").put("가공"));
            json.put("price", new JSONArray().put(2240).put(1360).put(880).put(1980).put(1700));
            json.put("location", new JSONArray().put("B4").put("B4").put("C4").put("E4").put("B2"));
            json.put("sales", new JSONArray().put(0).put(0).put(0).put(0).put(0));
            json.put("quantity", new JSONArray().put(1).put(1).put(3).put(1).put(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonStr = json.toString();

        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(jsonStr, BarcodeFormat.QR_CODE, 300, 300, hintMap);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(matrix);
            ImageView imageViewQrCode = findViewById(R.id.imageViewQrCode);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}