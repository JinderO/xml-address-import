package com.jindero.xmlimport.xmladdressimport.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    // Vytvoření klienta
    private final HttpClient client = HttpClient.newHttpClient();

    //Zastřešující metoda -> stáhne ZIP a vrátí XML InputStream
    public InputStream downloadAndExtractXml(String url) {
        InputStream zipped = downloadFile(url);
        return unzipFile(zipped, ".xml");
    }

    public InputStream downloadFile(String url) {
        try {
            //Sestavení requesti s URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            //Odeslaní + response
            HttpResponse<InputStream> response =
                    client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            return response.body();

        } catch (IOException e) {
            log.error("Chyba při stahování souboru z URL: {}", url, e);
            throw new RuntimeException("Chyba při stahování souboru", e);
        } catch (InterruptedException e) {
            log.error("Chyba při stahování souboru z URL: {}", url, e);
            Thread.currentThread().interrupt(); // Obnovení interrupt flagu
            throw new RuntimeException("Vlákno bylo přerušeno", e);
        }
    }

    public InputStream unzipFile(InputStream file, String fileExtension) {

        ZipInputStream zis = new ZipInputStream(file);
        ZipEntry entry;
        try {
            while ((entry = zis.getNextEntry()) != null) {

                String fileName = entry.getName();

                //Kontrola přípony
                if (fileName.endsWith(fileExtension)) {
                    return zis;
                }
            }
            throw new RuntimeException("V ZIPu nebyl nalezen soubor s příponou: " + fileExtension);

        } catch (IOException e) {
            log.error("Chyba při rozbalování souboru: {}", fileExtension, e);
            throw new RuntimeException("Chyba při rozbalování ZIP souboru", e);
        }
    }
}
