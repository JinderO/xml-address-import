package com.jindero.xmlimport.xmladdressimport.service;

import com.jindero.xmlimport.xmladdressimport.repository.CastObceRepository;
import com.jindero.xmlimport.xmladdressimport.repository.ObecRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DataImportRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataImportRunner.class);

    private final FileService fileService;
    private final XmlParser xmlParser;
    private final ObecRepository obecRepository;
    private final CastObceRepository castObceRepository;


    public DataImportRunner(FileService fileService, XmlParser xmlParser,
                            ObecRepository obecRepository,CastObceRepository castObceRepository) {
        this.fileService = fileService;
        this.xmlParser = xmlParser;
        this.obecRepository = obecRepository;
        this.castObceRepository = castObceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        InputStream xmlStream = fileService.downloadAndExtractXml("https://www.smartform.cz/download/kopidlno.xml.zip");

        ParseResult result = xmlParser.parseXml(xmlStream);

        obecRepository.save(result.obec());
        log.info("Obec {} byla úspěšně uložena", result.obec().getNazev());

        castObceRepository.saveAll(result.casti());
        log.info("Uloženo {} částí obce", result.casti().size());
    }

}
