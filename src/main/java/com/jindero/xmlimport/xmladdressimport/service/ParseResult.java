package com.jindero.xmlimport.xmladdressimport.service;

import com.jindero.xmlimport.xmladdressimport.entity.CastObce;
import com.jindero.xmlimport.xmladdressimport.entity.Obec;

import java.util.List;

public record ParseResult(Obec obec, List<CastObce> casti) {
}
