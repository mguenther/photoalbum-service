# Dropwizard by Example: Photoalbum Webservice

As part of learning Dropwizard, I cobbled together a small sample webservice that allows users to manage photo albums and exposes its API via HTTP. All functional requirements have been implemented and are fully working.

## Requirements

Given that this is first and foremost an educational project and focuses on building webservices using Dropwizard, there is a minimum set of functional requirements for the service as to make it actually useful. These requirements are:

* Users can create and delete photo albums.
* Users can upload photos and associate them with a single photo album.
* Photos are managed resources within the context of the service.
* A photo must be uniquely identifiable.
* Users can add tags to photos.
* Users can provide descriptions for their photos.
* A user only has access to the photo albums - and thus, photos - she has created.

The same applies for non-functional requirements:

* Technical aspects (e.g. security, technical data model) do not have to be fully fleshed out.
* Albums and photos are stored as documents within a CouchDB. This gives us update isolation on the level of a document, but not an aggregate of documents. The decision to use CouchDB as data store was primarily driven by the fact that I have used it before and get it up to speed using the provided CouchDB adapter very quickly.
* User management is negligible. However, to showcase the integration of a custom security component for authentication and authorization, at least a read-only model for users must be implemented.
* Strive for code clarity instead of algorithmic speed or efficiency.
* The exposed HTTP API should not enforce any kind of strong coupling between the API and a client implementation.

## Limitations

Please keep in mind that this example application has been written for educational purposes only and is not ready for production.

* Given that there is strong association between albums and photos, some sort of transactional context when removing an entire album would be beneficial.
* User passwords are SHA-1-hashed instead of using a stronger, adaptive mechanism like bcrypt. 

## Modules

* `photoalbum-bootstrap`: Creates a database in a local CouchDB instance and adds some example documents for every document type (user, album, photo) to that database. This is used for testing purposes.
* `photoalbum-common`: Provides abstractions for common concepts throughout the modules. This is supposed to be a stable package.
* `photoalbum-couchdb-adapter`: Uses Ektorp as CouchDB client and provides some additional abstractions and a thread-safe implementation of CouchDB changes feed worker.
* `photoalbum-fileio-adapter`: Provides some convenience when working with `Path`s and the like.
* `photoalbum-management`: Implements the asset management (with buildings blocks for separating query and command paths) and image conversion services used for generating thumbnails.
* `photoalbum-security`: Provides authentication and authorization in a very simplistic and pure way.
* `photoalbum-user-management`: Implements a read-only model for users. This module is used by the security component to authenticate users.
* `photoalbum-webapp`: This is the Dropwizard application that puts everything together. Integrates `dropwizard-guice` for dependency injection.

## Running the service

Module `photoalbum-webapp` is the resulting Dropwizard-powered application. The build declaration for this module uses the Maven plugin `exec-maven-plugin` to conveniently execute the service from the command line. Using `mvn exec:java` on the command line starts the service directly. Other than that, the primary artefact of the build is fat JAR with all included dependencies and can be started using the standard Dropwizard way: 

    java -jar photoalbum-webapp-0.1.0.jar server {configuration-file}

## License

This work is released under the terms of the MIT license.