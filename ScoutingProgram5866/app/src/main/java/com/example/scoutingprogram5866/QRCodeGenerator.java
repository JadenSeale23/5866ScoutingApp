package com.example.scoutingprogram5866;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.IOException;

public class QRCodeGenerator{
    private String text;
    private int width,height;

    public QRCodeGenerator(String text, int width, int height) {
        this.text = text;
        this.width = width;
        this.height = height;
    }

    public Bitmap getQRCodeImage() throws WriterException, IOException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap pngData = barcodeEncoder.createBitmap(bitMatrix);
            return pngData;
        }
        catch(WriterException e){
            e.printStackTrace();
        }
     return null;
    }
}