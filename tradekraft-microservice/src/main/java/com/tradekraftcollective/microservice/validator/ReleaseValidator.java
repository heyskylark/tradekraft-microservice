package com.tradekraftcollective.microservice.validator;

import com.tradekraftcollective.microservice.exception.ErrorCode;
import com.tradekraftcollective.microservice.exception.ServiceException;
import com.tradekraftcollective.microservice.persistence.entity.Release;
import com.tradekraftcollective.microservice.utilities.ImageValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by brandonfeist on 10/22/17.
 */
@Component
public class ReleaseValidator {
    private static Logger logger = LoggerFactory.getLogger(ReleaseValidator.class);

    @Inject
    private SongValidator songValidator;

    @Inject
    private ImageValidationUtil imageValidationUtil;

    private static final String[] VALID_RELEASE_TYPES = {
            "single", "ep", "lp"
    };

    public void validateRelease(Release release, MultipartFile image, MultipartFile[] songFiles) {
        validateReleaseName(release);
        validateReleaseType(release);
        validateReleaseLinks(release);
        validateReleaseImage(image);
        songValidator.validateReleaseSongs(release.getSongs(), songFiles);
    }

    private void validateReleaseName(Release release) {
        if(release.getName() == null || release.getName().isEmpty()) {
            logger.error("Missing release name.");
            throw new ServiceException(ErrorCode.INVALID_RELEASE_NAME, "release name must be present.");
        }
    }

    private void validateReleaseType(Release release) {
        if(release.getReleaseType() == null || release.getReleaseType().isEmpty()) {
            logger.error("Missing release type.");
            throw new ServiceException(ErrorCode.INVALID_RELEASE_TYPE, "release type must be present.");
        }

        boolean validReleaseType = false;
        for(String releaseType : VALID_RELEASE_TYPES) {
            if(release.getReleaseType().toLowerCase().equals(releaseType)) {
                validReleaseType = true;
            }
        }

        if(!validReleaseType) {
            logger.error("Invalid release type. Valid release types are [single, ep, lp]");
            throw new ServiceException(ErrorCode.INVALID_RELEASE_TYPE, "valid release types are [single, ep, lp].");
        }
    }

    private void validateReleaseLinks(Release release) {

    }

    private void validateReleaseImage(MultipartFile image) {
        try {
            imageValidationUtil.validateImageExtension(image);
            imageValidationUtil.minimumImageSize(1024, 1024, ImageIO.read(image.getInputStream()));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}