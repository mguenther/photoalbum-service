package com.mgu.photoalbum.bootstrap;

import com.mgu.photoalbum.domain.Photo;
import com.mgu.photoalbum.storage.PhotoRepository;
import org.ektorp.CouchDbConnector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotoGenerator implements Generator {

    private final PhotoRepository repository;

    public PhotoGenerator(final CouchDbConnector connector) {
        this.repository = new PhotoRepository(connector);
    }

    @Override
    public void generate() {

        List<Photo> photosToCreate = new ArrayList<Photo>() {{

            add(Photo
                    .create()
                    .id("PH-aB34z0013t9XccF")
                    .belongsTo("AL-zk3Dz0Sct9XccF")
                    .createdBy("CU-zkVDzWSctsEqqk")
                    .description("Blick auf den Hamburger Hafen")
                    .originalFilename("DCIM02023424.jpg")
                    .tag(Arrays.asList("hafen"))
                    .build());

            add(Photo
                    .create()
                    .id("PH-aBffzee1ddddccF")
                    .belongsTo("AL-vcdjke39svm292")
                    .createdBy("CU-zkVDzWSctsEqqk")
                    .description("Vorbei am Greve de Lecq")
                    .originalFilename("DCIM02023412.jpg")
                    .tag(Arrays.asList("footpath"))
                    .build());
        }};

        for (Photo photo : photosToCreate) {
            repository.add(photo);
            System.out.println("Added photo '" + photo.getDescription() + "' with ID " + photo.getId() + " to database.");
        }
    }

    @Override
    public String toString() {
        return "Photo Document Generator";
    }
}