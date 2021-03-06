hashuri-java
============

This code generates and checks URIs that represent content such as plain bytes or RDF data, and
contain a cryptographic hash value. This hash can be used to check that the respective content has
not been accidentally or deliberately modified. This is an examle of a hash-URI:

> http://example.org/np1.RAcbjcRIQozo2wBMq4WcCYkFAjRz0AX-Ux3PquZZrC68s

See the [hash-URI specification](https://github.com/tkuhn/hashuri-spec) and the
[preprint article](http://arxiv.org/abs/1401.5775) describing the approach.


Examples
--------

See the [examples](src/main/resources/examples).


Build
-----

To build this package, simply run the script `build.sh` (requires Maven):

    $ scripts/build.sh


Run
---

Run the following command to check a file or URL with a hash-URI:

    $ scripts/CheckFile.sh [FILE-OR-URL]

The following command adds a hash to a local file using algorithm `FA`:

    $ scripts/ProcessFile.sh [FILE]

To transform an RDF document, use this command (the second argument is optional):

    $ scripts/TransformRdf.sh [FILE] [BASE-URI]

For nanopublications you can use the following command:

    $ scripts/TransformNanopub.sh [FILE]


Run with Single JAR File
------------------------

*This should work in theory, but in practice it doesn't. Maven gives many "already added, skipping"
messages and then when running the commands the required Sesame parser factories are not found...*

To generate a single jar file that includes all dependencies:

    $ mvn compile assembly:single

You may want to give the resulting jar file a shorter name:

    $ mv target/hashuri-1.0-SNAPSHOT-jar-with-dependencies.jar hashuri.jar

Then the commands can be run like this:

    $ java -jar hashuri.jar CheckFile [FILE]


License
-------

hashuri-java is free software under the MIT License. See LICENSE.txt.
