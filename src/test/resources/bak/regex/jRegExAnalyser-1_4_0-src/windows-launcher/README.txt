Launcher for jRegExAnalyser based on freemind.

This launcher for Microsoft Windows was compiled using MinGW.

This launcher provides the following.

1) Has an icon
2) When a file is dragged and dropped over the launcher, it is passed to java application
   as a parameter
3) When the launcher is run from a DOS console and given multiple arguments, all are passed to
   java application
4) Launcher has to reside in a directory relative to the application directory. However, it
   can be run from any other directory. Therefore, if you assign the launcher to a file type,
   the application is run correctly with the file passed as an argument.


Things to watch

1) ../icon.ico must be a real icon, not an icon created by changing the extension of a file from
   bmp to ico.
2) Icon1.rc is the file telling where the icons is stored. It is a resource file. This resource
   file is compiled into Icon1.o (object file), which is then linked with jRegExAnalyser.o to create
   jRegExAnalyser.exe
