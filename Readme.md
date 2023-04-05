> docker exec -it apache-solr sh

## Creating a core

The core will be created on `/solr/server/solr`.

The `config` folder inner of core, there are the config files:
- solrconfig.xml: File configurations considering the server. For example, number of servers that will response the requests, urls that will response the user requests.
- managed-schema: Configuration about index structure. Include the data type, fields that describe de collection. It's necessary to rename to schema.xml to not use the default configuration (stateless);

Remove core:
> solr delete -c book

Create a core (documents collection):
> solr create -c book

Rename the schema:
> cd server/solr/book/conf
> mv managed-schema schema.xml


Import Java classes responsible to read the files. This class is Data Import Handler. Add the config below in `solrconfig.xml`

```
<lib dir="../../../contrib/dataimporthandler/lib" regex=".*\.jar">
<lib dir="../../../dist/" regex="solr-dataimporthandler-.*\.jar">

```



