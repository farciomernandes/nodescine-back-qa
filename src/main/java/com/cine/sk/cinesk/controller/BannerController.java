package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.banner.Banner;
import com.cine.sk.cinesk.domain.banner.BannerDTO;
import com.cine.sk.cinesk.domain.banner.BannerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Banner> createBanner(@RequestPart("banner") String bannerDtoString,
                                               @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        BannerDTO bannerDTO = objectMapper.readValue(bannerDtoString, BannerDTO.class);
        Banner banner = new Banner();
        banner.setLabel(bannerDTO.getLabel());
        banner.setType(bannerDTO.getType());
        banner.setUrl(bannerDTO.getUrl());

        Banner createdBanner = bannerService.createBanner(banner, file);
        return new ResponseEntity<>(createdBanner, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Banner>> getAllBanners() {
        List<Banner> banners = bannerService.getAllBanners();
        return new ResponseEntity<>(banners, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
