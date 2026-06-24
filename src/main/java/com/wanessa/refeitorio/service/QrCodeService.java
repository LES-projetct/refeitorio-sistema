/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wanessa.refeitorio.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Service;

/**
 *
 * @author wanes
 */
@Service
public class QrCodeService {

    public byte[] gerarQrCodePng(String texto, int largura, int altura) {

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix matriz = qrCodeWriter.encode(
                    texto,
                    BarcodeFormat.QR_CODE,
                    largura,
                    altura
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(
                    matriz,
                    "PNG",
                    outputStream
            );

            return outputStream.toByteArray();

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erro ao gerar QR Code.", e);
        }
    }
}
