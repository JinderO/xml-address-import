package com.jindero.xmlimport.xmladdressimport.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DataImportRunner implements CommandLineRunner {

    private final FileService fileService;
    private final XmlParser xmlParser;

    public DataImportRunner(FileService fileService, XmlParser xmlParser) {
        this.fileService = fileService;
        this.xmlParser = xmlParser;
    }

    @Override
    public void run(String... args) throws Exception {
        InputStream xmlStream = fileService.downloadAndExtractXml("https://www.smartform.cz/download/kopidlno.xml.zip");

        ParseResult result = xmlParser.parseXml(xmlStream);

        //TODO dočasný test
        System.out.println(result);
    }
}
