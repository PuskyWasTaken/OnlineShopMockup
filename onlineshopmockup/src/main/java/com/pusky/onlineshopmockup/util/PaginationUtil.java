package com.pusky.onlineshopmockup.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static <T> HttpHeaders generatePaginationHttpHeaders(UriComponentsBuilder uriBuilder, Page<T> page) {

        HttpHeaders headers = new HttpHeaders();
        final long pageTotalElements = page.getTotalElements();
        final int pageNumber = page.getNumber();
        final int pageSize = page.getSize();

        headers.add("Total-Entry-Count", Long.toString(pageTotalElements));
        headers.add("Total-Page-Count", Long.toString(pageTotalElements / pageSize));

        StringBuilder urlBuilder = new StringBuilder();

        if (pageNumber < page.getTotalPages() - 1)
            urlBuilder.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next")).append(",");


        if (pageNumber > 0)
            urlBuilder.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev")).append(",");

        urlBuilder.append(prepareLink(uriBuilder, page.getTotalPages() - 1, pageSize, "last")).append(",").append(prepareLink(uriBuilder, 0, pageSize, "first"));
        headers.add("URL", urlBuilder.toString());
        return headers;
    }

    private static String prepareLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
        return MessageFormat.format("<{0}>; rel=\"{1}\"", preparePageUri(uriBuilder, pageNumber, pageSize), relType);
    }

    private static String preparePageUri(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize) {
        return uriBuilder.replaceQueryParam("page", new Object[]{Integer.toString(pageNumber)}).replaceQueryParam("size", new Object[]{Integer.toString(pageSize)}).toUriString().replace(",", "%2C").replace(";", "%3B");
    }
}