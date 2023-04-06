# books-solr-ms
The books solr service act as a service responsible to make request to apache solr to get a list of books. 

## Table of contents
* [Technologies](#technologies)
* [Getting started](#getting-started)
  * [Installing](#installing)
  * [Start the service](#start-the-service)
  * [Importing new books](#importing-new-books)
* [Things To Know](#things-to-know)

## Technologies
* Spring Boot
* Apache Solr

## Getting Started
### Installing
Run maven clean to remove all target content:
> ./mvnw clean

Package the app
> ./mvnw package

If necessary, remove containers and images and create them again:
> docker container stop solr

> docker container stop books-ms

> docker rm solr

> docker rm books-ms

> docker rmi books-ms:latest

> docker rmi solr:7.6

### Start the service
Run docker compose to create the containers:
> docker compose up -d

Run a request to get the data as in the example below:
> curl --location --request GET 'http://localhost:8080/books/any-text'

An example to return data with 'microservice' in the title, author or description:
> curl --location --request GET 'http://localhost:8080/books/microservice'

### Importing new books
Solr can be opened using the URL http://localhost:8983/solr/#/. In the core selector it is possible to select the core of the book and perform some operations such as executing a query or importing data.

New books can be added to the `solr-cores/book/books.xml` file. Once included, execute the request below to proceed with the data import:
> http://localhost:8983/solr/book/dataimport?command=full-import&commit=true

Check that all data has been created with the command request below:
> curl --location --request GET 'http://localhost:8983/solr/book/select?q=*:*&rows=100'

## Things To Know
### About config files
The core will be created in the `/solr/server/solr` package inside the solr container.

All configuration files are in the `config` directory.
- solrconfig.xml: This file is responsible for server configuration. Example, number of servers that will respond to requests, urls that will respond to user requests.
- managed-schema: The purpose of this file is to store the index structure. Include in this file the data type, fields that describe the collection.

When it is necessary to remove a core manually, run the command below inside the container:
> solr delete -c book

When necessary to create a core (collection of documents):
> solr create -c book

If necessary to rename the schema file:
> cd server/solr/book/conf

> mv managed-schema schema.xml

### Adding data import configuration
The classes responsible for reading the files are configured in the `solrconfig.xml` file.
```
  <lib dir="${solr.install.dir:../../../..}/contrib/dataimporthandler/lib" regex=".*\.jar" />
  <lib dir="${solr.install.dir:../../../..}/dist/" regex="solr-dataimporthandler-\d.*\.jar" />
```

### Creating a new field type
The analyser and fields need to be added to the `managed-schema`.
- Tokenizer: Adds the field type with a tokenizer responsible for dividing the data into words (for example, dividing by ',', '-' and others). Add this tokenizer to prevent splitting by characters other than numbers or letters. It is necessary to preserve urls and ips, so it is necessary to add this tokenizer. More details at https://solr.apache.org/guide/7_1/tokenizers.html
- About the filters
  - StopFilterFactory: Discards words that are not interesting for search (for example, conjunctions, articles). User only nouns and verbs. It is necessary to define which words will be discarded using the 'words' key. Use the file that is available in the lang directory. Copy and paste in config.
  - LowerCaseFilterFactory: Changes all words to lowercase; Convert uppercase to lowercase;
  - ASCIIFoldingFilterFactory: Normalizes all letters like 'ç' 'á'. All special characters.
  - KeywordRepeatFilterFactory: Used to duplicate all words
  - RemoveDuplicatesTokenFilterFactory: If the word has no prefix, it will remove the word to not have the same word.

```
  <fieldType name="book-text-field" class="solr.TextField">
    <analyzer type="index">
      <tokenizer class="solr.UAX29URLEmailTokenizerFactory"/>
      <filter class="solr.StopFilterFactory" words="stopwords_en.txt" ignoreCase="true"/>
      <filter class="solr.LowerCaseFilterFactory"/>
      <filter class="solr.KeywordRepeatFilterFactory"/>
      <filter class="solr.EnglishMinimalStemFilterFactory"/>
      <filter class="solr.RemoveDuplicatesTokenFilterFactory"/>
    </analyzer>
    <analyzer type="query">
      <tokenizer class="solr.UAX29URLEmailTokenizerFactory"/>
      <filter class="solr.StopFilterFactory" words="stopwords_en.txt" ignoreCase="true"/>
      <filter class="solr.LowerCaseFilterFactory"/>
      <filter class="solr.KeywordRepeatFilterFactory"/>
      <filter class="solr.EnglishMinimalStemFilterFactory"/>
      <filter class="solr.RemoveDuplicatesTokenFilterFactory"/>
    </analyzer>
  </fieldType>
```

### Add fields to schema
The fields must be included in the `managed-schema` file.
- indexed: true if this field is stored as an index.
- stored: Stores the actual value without filters. Avoid using this option for large fields.
```
  <field name="author" type="book-text-field" indexed="true" stored="true"/>
  <field name="description" type="book-text-field" indexed="true" stored="false"/>
  <field name="id" type="string" multiValued="false" indexed="true" required="true" stored="true"/>
  <field name="isbn" type="string" indexed="true" stored="true"/>
  <field name="title" type="book-text-field" indexed="true" stored="true"/>
```

### Some commands
Stop the container:
> docker container stop apache-solr

> docker system prune

> docker compose up -d

Open cmd container:
> docker exec -it apache-solr sh

Create a core (documents collection):
> solr create -c book

Open book core directory:
> cd /opt/solr/server/solr/book

Inspect the container:
> docker inspect apache-solr

Copy solrconfig.xml from container: 
> docker cp apache-solr:/opt/solr/server/solr/book/conf/solrconfig.xml ./

Copy stopwords_en.txt from container:
> docker cp apache-solr:/opt/solr/server/solr/book/conf/lang/stopwords_en.txt ./

Copy schema.xml from container:
> docker cp apache-solr:/opt/solr/server/solr/book/conf/schema.xml ./

Copy lang from container:
> docker cp apache-solr:/opt/solr/server/solr/book/conf/lang ./

Copy another files:
> docker cp apache-solr:/opt/solr/server/solr/book/conf/params.json ./

> docker cp apache-solr:/opt/solr/server/solr/book/conf/protwords.txt ./

> docker cp apache-solr:/opt/solr/server/solr/book/conf/stopwords.txt ./

> docker cp apache-solr:/opt/solr/server/solr/book/conf/synonyms.txt ./

Ping:
> docker exec -ti books-ms ping solr

List images:
> docker images 

Remove books-ms image
> docker rmi books-ms:latest