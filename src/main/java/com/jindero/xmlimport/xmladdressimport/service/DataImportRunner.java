package com.jindero.xmlimport.xmladdressimport.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DataImportRunner implements CommandLineRunner {

    private final FileService fileService;

    public DataImportRunner(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void run(String... args) throws Exception {
        InputStream xmlStream = fileService.downloadAndExtractXml("https://www.smartform.cz/download/kopidlno.xml.zip");

        //TODO: dočasný test FileService, nahradit skutečnou logikou (parsing + uložení)
        byte[] buffer = new byte[500];
        int bytesRead = xmlStream.read(buffer);
        System.out.println(new String(buffer, 0, bytesRead));
    }
}
