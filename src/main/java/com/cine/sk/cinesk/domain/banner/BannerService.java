package com.cine.sk.cinesk.domain.banner;

import com.cine.sk.cinesk.domain.file.aws.AwsService;
import com.cine.sk.cinesk.infrastructure.repositorys.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private AwsService awsService;

    public Banner createBanner(Banner banner, MultipartFile file) throws IOException {
        if (banner.getType() == BannerType.S3) {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("File is required for S3 upload");
            }
            String bannerId = UUID.randomUUID().toString();
            var uploadedFile = awsService.upload(file, "banners", bannerId, file.getOriginalFilename());
            banner.setUrl(uploadedFile.getUri());
        } else if (banner.getType() == BannerType.GOOGLE_DRIVE) {
            if (banner.getUrl() == null || banner.getUrl().isEmpty()) {
                throw new IllegalArgumentException("URL is required for GOOGLE_DRIVE type");
            }
        }

        return bannerRepository.save(banner);
    }

    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}
