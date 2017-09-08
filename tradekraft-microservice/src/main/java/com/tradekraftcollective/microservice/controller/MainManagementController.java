package com.tradekraftcollective.microservice.controller;

import com.tradekraftcollective.microservice.persistence.entity.Artist;
import com.tradekraftcollective.microservice.repository.IArtistRepository;
import com.tradekraftcollective.microservice.service.IArtistManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

/**
 * Created by brandonfeist on 8/31/17.
 */
@RestController
@RequestMapping("/v1/")
public class MainManagementController {
    private static final Logger logger = LoggerFactory.getLogger(MainManagementController.class);

    private static final String DEFAULT_PAGE_NUM = "0";
    private static final String DEFAULT_PAGE_SIZE = "100";
    private static final String SORT_ORDER_DESC = "asc";
    private static final String SORT_FIELD_NAME = "name";

    @Inject
    private IArtistManagementService artistManagementService;

    @Inject
    private IArtistRepository artistRepository;

    @RequestMapping(value = "/artists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getArtists(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUM, required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortField", defaultValue = SORT_FIELD_NAME, required = false) String sortField,
            @RequestParam(value = "sortOrder", defaultValue = SORT_ORDER_DESC, required = false) String sortOrder,
            @RequestHeader(value = "X-Request-ID", required = false) String xRequestId
    ) {
        logger.info("getArtists [{}]", xRequestId);

        Page<Artist> artists = artistManagementService.getArtists(page, pageSize, sortField, sortOrder);

        return new ResponseEntity<>(artists, HttpStatus.OK);
    }

    @RequestMapping(value = "/artists/{slug}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getArtist(
            @PathVariable("slug") String artistSlug,
            @RequestHeader(value = "X-Request-ID", required = false) String xRequestId
    ) {
        logger.info("getArtists [{}]", xRequestId);

        Artist artist = artistManagementService.getArtist(artistSlug);

        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @RequestMapping(value = "/artists", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createArtist(
            @RequestParam("name") String name,
            @RequestParam("status") String status,
            @RequestParam("image") MultipartFile imageFile,
            @RequestHeader(value = "X-Request-ID", required = false) String xRequestId
    ) {
        Artist inputArtist = new Artist();
        inputArtist.setName(name);
        inputArtist.setStatus(status);
        inputArtist.setImage("test");
        logger.info("createArtist [{}] {}", xRequestId, inputArtist);

        StopWatch stopWatch = new StopWatch("createArtist");

        Artist artist = artistManagementService.createArtist(inputArtist, imageFile, stopWatch);

        return new ResponseEntity<>(artist, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/artists/{slug}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchArtist(
            @PathVariable("slug") String artistSlug,
//            @RequestBody
            @RequestHeader(value = "X-Request-ID", required = false) String xRequestId
    ) {
        logger.info("patchArtist [{}] {}", xRequestId, artistSlug);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/artists/{slug}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteArtist(
            @PathVariable("slug") String artistSlug,
            @RequestHeader(value = "X-Request-ID", required = false) String xRequestId
    ) {
        logger.info("deleteArtist [{}] {}", xRequestId, artistSlug);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // Get release(s)

    // Get release

    // Patch release

    // Delete release


    // Get event(s)

    // Get event

    // Patch event

    // Delete event


    // Get genre(s)

    // Patch genre

    // Delete genre
}
