# NotesTree
If you tired of hundred tabs in Notepad++, this plugin brings consistency and availability to your notes in pretty convenient way.
The plugin adds ability to write tree structure global notes in such formats as `txt`, `md`, `json`, `xml`, `yaml`, `sql`, `csv`, `xlsx`, `sh` or `http`.
The plugin brings you the way to keep `Dockerfile` or `docker_compose` templates , images(`png`, `jpg`), `pdf`, `doc`, `docx`, `java`, `python` or `kotlin` files and create symbolic links(MacOS, Linux).
It also supports [plum](https://plantuml.com/en/) and [mermaid](https://mermaid-js.github.io/mermaid/#/)
 diagrams.

Intellij idea also provides you to possibility to directly from the plugin run some of these notes like http files,
sql scripts or docker_compose templates, it is really convenient and useful during development.

![img](https://github.com/epm-dev-priporov/idea-notes/blob/master/src/main/resources/img1.2.8.png?raw=true)

### Requirements

For correct work the following points should be done:

* plum files require [plum integration plugin](https://plugins.jetbrains.com/plugin/7017-plantuml-integration)
* http files require [HTTP client plugin](https://plugins.jetbrains.com/plugin/13121-http-client)
* Dockerfile, docker_compose.yaml require [Docker plugin](https://www.jetbrains.com/help/idea/docker.html)
* pdf, csv, xlsx, doc, docx files and png, jpg images can be inserted from system clipboard
* conf files require [Nginx Configuration plugin](https://plugins.jetbrains.com/plugin/15461-nginx-configuration)

Supported formats:
* txt
* md
* json
* xml
* yaml
* sql
* sh
* csv
* http
* pdf
* jpg
* png
* plump
* kt
* java
* py
* svg
* conf
* doc, docx
* xlsx
* log

#### Feedback
I would really appreciate any feedback from users. Also please don't hesitate and share any ideas

## IDE version Update

For a some reason IntellijIdea does not import configuration file of the plugin to a new version IDE, so if you lost your 
notes after update, you need to move ideanotes.xml config file from
`~/.config/JetBrains/IdeaIC<OldVersion>/options/ideanotes.xml` to `~/.config/JetBrains/IdeaIC<NewVersion>/options/ideanotes.xml`. 