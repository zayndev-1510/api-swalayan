package com.api.swalayan.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QrcodeService {
    public static String generateQRCode(String data, int width, int height,String fileName) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        String uploadDir="./uploads/qrcode";
        Path path = Paths.get(uploadDir,fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        return path.toString();
    }
}
