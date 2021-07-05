# ASMBB
Armor Stand Multi Block Builder

The ASMBB plugin will help you build and manage multi blocks.

Features:
- building the multi block easily (with armor stands)
- using the multi block as a variable inside the code (the variable is named Project)
- being able to store data inside the multi block
- saving the projects inside a config.yml file


# How to use

#Builder
In Minecraft, use the command /asmbb new [name] to start a new project and editing it. after you finished your editing click on the save button in your hot bar and the project will be saved inside the config.yml of the plugin.

List of commands:
/ASMBB new [name] - creates a new project.
/ASMBB load [name] - loads a project from file.
/ASMBB delete [name] - deletes a project.
/ASMBB build [name] - builds a multi block on your current location.

# Developer functions

to add functions to the multi blocks you will need the API for it.

# API
How to add this plugin as maven dependnecy:

```xml
<repository>
   <id>jitpack.io</id>
   <url>https://jitpack.io</url>
</repository>

<dependency>
   <groupId>com.github.ODINN555</groupId>
   <artifactId>ASMBB</artifactId>
   <version>v1.1.0</version>
</dependency>


```

The only thing you will need to use is the UserUtil class:

```java
UserUtil util = new UserUtil();
```

All methods are documented and explained so if you have an IDE you can read about each method.



