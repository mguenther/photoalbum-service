package com.mgu.photoalbum.service;

import com.google.inject.Inject;
import com.mgu.photoalbum.adapter.fileio.PathAdapter;
import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.identity.IdGenerator;
import com.mgu.photoalbum.storage.AlbumRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AlbumService implements AlbumCommandService, AlbumQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumService.class);

    private final AlbumRepository repository;

    private final PhotoCommandService photoCommandService;

    private final PhotoQueryService photoQueryService;

    private final PathScheme pathScheme;

    private final PathAdapter pathAdapter;

    private final Supplier<String> albumIdGenerator;

    @Inject
    public AlbumService(
            final AlbumRepository repository,
            final PhotoCommandService photoCommandService,
            final PhotoQueryService photoQueryService,
            final PathScheme pathScheme,
            final PathAdapter pathAdapter,
            final IdGenerator idGenerator) {
        this.repository = repository;
        this.photoCommandService = photoCommandService;
        this.photoQueryService = photoQueryService;
        this.pathScheme = pathScheme;
        this.pathAdapter = pathAdapter;
        this.albumIdGenerator = () -> idGenerator.generateId("AL", 14);
    }

    @Override
    public String createAlbum(final String ownerId, final String title) {
        final String uniqueAlbumId = generateUniqueAlbumId();
        final Album album = Album
                .create()
                .id(uniqueAlbumId)
                .createdBy(ownerId)
                .title(title)
                .build();
        repository.add(album);
        return uniqueAlbumId;
    }

    private String generateUniqueAlbumId() {
        String albumId = albumIdGenerator.get();
        while (repository.contains(albumId)) {
            albumId = albumIdGenerator.get();
        }
        return albumId;
    }

    @Override
    public void deleteAlbum(final String albumId) {
        if (!repository.contains(albumId)) {
            // deleting an existing album and trying to delete a non-existing album leads to the
            // same state convergence. thus, we do not treat a deletion request to a non-existing album
            // not as an error, but return immediately
            LOGGER.debug("Received a delete command for a non-existing album (ID " + albumId + ").");
            return;
        }
        final Album album = repository.get(albumId);
        repository.remove(album);
        photoCommandService.deletePhotos(albumId);
        pathAdapter.deleteDirectory(pathScheme.constructPathToAlbumFolder(album.getOwnerId(), albumId));
        LOGGER.info("Removed album with ID " + albumId + " along with all associated photos.");
    }

    @Override
    public Album albumById(final String albumId) {
        if (!repository.contains(albumId)) {
            throw new AlbumDoesNotExistException(albumId);
        }
        final Album album = repository.get(albumId);
        return album;
    }

    @Override
    public AlbumSearchResult search(final AlbumSearchRequest searchRequest) {
        final List<AlbumHit> hits = repository
                .getAllByOwner(searchRequest.getOwnerId())
                .stream()
                .map(album -> new AlbumHit(album, numberOfPhotosInAlbum(album.getId()), randomThumbnailId(album.getId())))
                .collect(Collectors.toList());
        return new AlbumSearchResult(searchRequest, hits.size(), hits);
    }

    private int numberOfPhotosInAlbum(final String albumId) {
        return photoQueryService.search(PhotoSearchRequest.create().albumId(albumId).build()).getHitCount();
    }

    private String randomThumbnailId(final String albumId) {
        return photoQueryService
                .search(PhotoSearchRequest.create().albumId(albumId).build())
                .getHits()
                .stream()
                .findFirst()
                .map((Photo p) -> p.getId())
                .orElse(StringUtils.EMPTY);
    }
}