                                
#ArchTool
##Architectural Visualization Tool for 'C#'

##About

 Arch tool is a simple relationship viewer for C#. This
tool parses a C# project or file bundle and displays
class interactions. A class interaction is a declara-
tion of another class inside it self, or a call,
or a member, or a method parameter.

![Arch tool demo image](http://i.imgur.com/GgIKace.png)

 Currently Arch Tool doesn't differentiate between
those. It just displays a graph connecting each class
that calls another.

 Currently only supports C# but adding other languages
should be easy. I plan to add at least java, python and
maybe lua.
 
##How to load a project

![How to load a project image](http://i.imgur.com/5ZouiMv.png)

To load a project, just drag its folder(directory)
to the Arch Tool empty panel bellow the title. Drop
the folder(directory) there and wait for the graph
to build.

##How to load a file bundle.

![How to load file bundles](http://i.imgur.com/fqfQuJB.png)

Just select all files you want to analyse and drag
them to the Arch Tool empty panel bellow the title.
Drop the files there and wait for the graph to build.