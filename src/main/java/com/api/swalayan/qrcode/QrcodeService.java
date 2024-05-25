package com.api.swalayan.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QrcodeService {
    public static String generateQRCode(String data, int width, int height, String fileName)
            throws WriterException, IOException {

        // Ensure the upload directory exists
        String externalSaveDir = "/home/zayndev/uploads";
        Path uploadPath = Paths.get(externalSaveDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

        // Determine the full file name (add .png if missing)
        String fullFileName = fileName.endsWith(".png") ? fileName : fileName + ".png";

        // Construct the full path for saving the QR code image
        Path filePath = Paths.get(externalSaveDir, fullFileName);

        // Write the QR code to the file
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

        // Return the full file name for reference
        return fullFileName;
    }
}
