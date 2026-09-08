package com.jindero.xmlimport.xmladdressimport.service;

import com.jindero.xmlimport.xmladdressimport.entity.CastObce;
import com.jindero.xmlimport.xmladdressimport.entity.Obec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlParser {

    private static final Logger log = LoggerFactory.getLogger(XmlParser.class);

    private enum State {
        NONE,
        OBEC,
        CAST_OBCE,
        CAST_OBCE_OBEC
    }

    public ParseResult parseXml(InputStream xmlStream) {

        State state = State.NONE;
        Obec obec = null;
        CastObce cast = null;
        List<CastObce> listCasti = new ArrayList<>();

        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(xmlStream);
            Boolean dokonceno = false;

            while (reader.hasNext() && dokonceno==false) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        String tag = reader.getLocalName();
                        switch (tag) {
                            case "Obec":
                                if (state == State.CAST_OBCE) {
                                    state = State.CAST_OBCE_OBEC;
                                } else {
                                    state = State.OBEC;
                                    obec = new Obec();
                                }
                                break;
                            case "CastObce":
                                state = State.CAST_OBCE;
                                cast = new CastObce();
                                break;
                            case "Kod":
                                String kod = reader.getElementText();
                                if (state == State.OBEC) {
                                    obec.setKod(kod);
                                } else if (state == State.CAST_OBCE) {
                                    cast.setKod(kod);
                                } else if (state == State.CAST_OBCE_OBEC) {
                                    cast.setKodObce(kod);
                                }
                                break;
                            case "Nazev":
                                String nazev = reader.getElementText();
                                if (state == State.OBEC) {
                                    obec.setNazev(nazev);
                                } else if (state == State.CAST_OBCE) {
                                    cast.setNazev(nazev);
                                }
                                break;
                            }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        String end = reader.getLocalName();
                        if (end.equals("Obec") && state == State.CAST_OBCE_OBEC) {
                            state = State.CAST_OBCE;
                        } else if (end.equals("Obec") && state == State.OBEC) {
                            state = State.NONE;
                        }
                        if (end.equals("CastObce")){
                            listCasti.add(cast);
                            state = State.NONE;
                    } if (end.equals("CastiObci")){
                            dokonceno = true;
                    }
                        break;
                }
            }

        } catch (XMLStreamException e) {
            log.error("Chyba při parsování XML streamu: {}", e.getMessage(), e);
            throw new RuntimeException("Nepodařilo se zpracovat XML: " + e.getMessage(), e);
        }
        return new ParseResult(obec, listCasti);
    }

}
