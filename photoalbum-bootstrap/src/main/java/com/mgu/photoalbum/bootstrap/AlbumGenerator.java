package com.mgu.photoalbum.bootstrap;

import com.mgu.photoalbum.domain.Album;
import com.mgu.photoalbum.storage.AlbumRepository;
import org.ektorp.CouchDbConnector;

import java.util.ArrayList;
import java.util.List;

public class AlbumGenerator implements Generator {

    private final AlbumRepository repository;

    public AlbumGenerator(final CouchDbConnector connector) {
        this.repository = new AlbumRepository(connector);
    }

    @Override
    public void generate() {

        List<Album> albumsToCreate = new ArrayList<Album>() {{

            final Album hamburg = Album.create().createdBy("CU-zkVDzWSctsEqqk").id("AL-zk3Dz0Sct9XccF").title("Hamburg 2014").build();
            final Album jersey = Album.create().createdBy("CU-zkVDzWSctsEqqk").id("AL-vcdjke39svm292").title("Jersey 2014").build();

            add(hamburg);
            add(jersey);
            add(Album.create().createdBy("CU-pzgBftxNCMLXql").id("AL-392952409vadsj").title("Tara").build());
        }};

        for (Album album : albumsToCreate) {
            repository.add(album);
            System.out.println("Added album '" + album.getTitle() + "' with ID " + album.getId() + " to database.");
        }
    }

    @Override
    public String toString() {
        return "Album Document Generator";
    }
}