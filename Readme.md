# books-solr-ms

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
Run the maven clean to remove all target content:
> ./mvnw clean

Create a package with service:
> ./mvnw package

If necessary, remove the containers and images and create again:
> docker container stop solr
> docker container stop books-ms
> docker rm solr
> docker rm books-ms
> docker rmi books-ms:latest
> docker rmi solr:7.6

### Start the service
Run the docker compose to build the containers:
> docker compose up -d

Run a request to get the data as in the example below:
> curl --location --request GET 'http://localhost:8080/books/<any-text>'

An example to return data with 'microservice' in the title, author or description
> curl --location --request GET 'http://localhost:8080/books/microservice'

### Importing new books
The solr can be opened using the url http://localhost:8983/solr/#/. In the core selector, it's possible to select the book core and make some operations like execute a query or import data.

New books can be included in the file `solr-cores/book/books.xml`. After included, run the request below to proceed with data import:
> http://localhost:8983/solr/book/dataimport?command=full-import&commit=true

Check if all data were created with the command request below:
> curl --location --request GET 'http://localhost:8983/solr/book/select?q=*:*&rows=100'

## Things To Know
### About config files
The core will be created on `/solr/server/solr` package inside the solr container.

All configurations files are in the `config` directory.
- solrconfig.xml: This file is responsible for server configuration. Example, number of servers that will response the requests, urls that will response the user requests. 
- managed-schema: The purpose of this file is to store the index structure. Include in this file the data type, fields that describe de collection.

When necessary to remove a core manually, run the command below inside container:
> solr delete -c book

When necessary to create a core (documents collection):
> solr create -c book

If necessary to rename the schema file:
> cd server/solr/book/conf
> mv managed-schema schema.xml

### Adding data import configuration
The classes responsible to read the files are configured in the `solrconfig.xml` file.
```
  <lib dir="${solr.install.dir:../../../..}/contrib/dataimporthandler/lib" regex=".*\.jar" />
  <lib dir="${solr.install.dir:../../../..}/dist/" regex="solr-dataimporthandler-\d.*\.jar" />
```

### Creating a new field type
The analyser and fields needs to be included in the `managed-schema`.
- Tokenizer: Add field type with a tokenizer responsible for split the data in words (e.g. split by ',', '-' and others). Add this tokenizer to avoid split by characters that is not number or a letter. It's necessary to preserve urls and ips, because of this it's necessary to add this tokenizer. More details in https://solr.apache.org/guide/7_1/tokenizers.html
- About the filters
  - StopFilterFactory: Discard words that is not interesting for search (e.g conjunctions, articles). User only substantive and verbs. It's necessary to define which words will be discarded using the key 'words'. Use the file that is available in lang directory. Copy and paste in the config.
  - LowerCaseFilterFactory: Change all words to lowercase; Convert uppercase to lowercase;
  - ASCIIFoldingFilterFactory: Normalize all letters like 'ç' 'á'. All special characters.
  - KeywordRepeatFilterFactory: Used to duplicate all words
  - BrazilianStemFilterFactory: Split by prefix (Learn, Learning, Learned)
  - RemoveDuplicatesTokenFilterFactory: If word dos not have a prefix, will remove the word to not have the same word.

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
- indexed: true if this field will be stored as an index.
- stored: Store the real value without the filters. Avoid use this option for large fields. 
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