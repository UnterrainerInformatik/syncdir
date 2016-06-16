```
/**************************************************************************
 * 
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org>
 * 
 ***************************************************************************/
```
 
# Disclaimer

This section of our GIT repository is free. You may copy, use or rewrite every single one of its contained projects to your hearts content.
In order to get help with basic GIT commands you may try [the GIT cheat-sheet][coding] on our [homepage][homepage].  

This repository is private since this is the master- and release-branch. You may clone it, but it will be read-only.  
If you want to contribute to our repository (push, open pull requests), please use the copy on github located here: [the public github repository][github]

All of our projects facilitate the [Project Lombok][lombok]. So please download it and 'install' it in your preferred IDE by clicking on the downloaded jar-file. Then all compile-errors should vanish.  

**Beware:** Project Lombok currently doesn't play well with Eclipse Mars (4.5). You'll get build-errors using the extension methods as of now.

# SyncDir
This is yet another synchronization program.
I've come across the problem when I wanted to backup my large music-collection periodically. Some things change, most of them don't. Depends on how many albums I am buying at the moment.
Usually I'm fine with just copy-pasting the root folders from the source-pladder to the backup-disc.

But every now and then I move a folder. And that's where the problem starts. The copy-paste technique doesn't delete the old, now superfluous folder.

That's where this program comes in. You point it at one or more source directories and a single target directory and it tells you exactly how many files and folders there are to create/replace/delete and then does so.

You may use it with a config-file or command-line parameters.

First, you'll need the Java **JRE version 1.8** (**Java SE 8**) or higher.
Make sure that java.exe is on your path, or start the following commands with the path to your java installation (the `/bin` directory of the one you've just downloaded).

Start the program, as you would start any jar by typing:
```
java -jar syncdir-0.1-jar-with-dependencies.jar
```
or put these lines in a batch-file (should be more convenient):
```
java -jar syncdir-0.1-jar-with-dependencies.jar %*
pause
```
We've put one in the target folder already for your convenience.

## The Commandline Parameters

```dos
R:\SyncDir>java -jar syncdir-0.1-jar-with-dependencies.jar -h
Usage:
syncdir
or
syncdir <configFilePathAndName>

If you specify a config file, it has to be a valid apache-configuration file.
If you don't, the program will try to fall back on a file named 'config.properties' 
located in the directory you started the application from.

You may as well call it just using command-line parameters like so:
syncdir <-analyze|-sync> <options> source <source...> target

where...
[-analyze -a]|[-sync -s]
                One of them is mandatory. If you choose -sync you may
                as well add -analyze later on in the options.
options:
-h -?           Shows this information.
-delete -del -d Does not only sync all the files and directories to the target,
                but deletes superfluous files and directories on the target as well.
-analyze -a     Does an additional analysis of the actions that will be taken
                in order to sync the directories and prints them out to the console.
                If you specify -analyze as main-parameter instead of -sync the program
                doesn't do anything else but to analyze.
```

## The Config File  

Here is an example of a config-file. I like to think it is very self-descriptive:

``` properties
# Here you can specify how the program behaves. We have several modes
# at your disposal which are described in detail below.
#
# Currently supported modes are:
#  sync			Does a synchronization run using all provided parameters.
#  analyze		When specified without 'sync' it just analyzes the directories
#				without doing any changes. When specified in addition to the
#				'sync' parameter, it prints out the analysis before doing
#				changes.
#  delete		Optional parameter for the 'sync'-mode.
#				When specified deletes directories and files present on the
#				target, but not present on the source. When not specified the
#				program just accumulates data in the target directory, never
#				deleting obsolete files or directories.
#
# The order of the words doesn't matter.
mode = analyze sync delete
#mode = sync
#mode = sync analyze
#mode = sync delete
#mode = sync analyze delete

# This is the target-directory where the program will copy the files to. 
targetDir = //babylon5/Movies3/target

# The directories where your source-files and directories are located.
#
# IMPORTANT FOR ALL PATH-SPECIFICATIONS:
# Always use forward slashes ('/') like in the examples below!
# Don't ever use backslashes ('\') since there are multiple issues with the
# JVM on different systems (non-windows) causing the command-line calls
# to fail!
#
# If you want to specify more than one, just add another sourceDirs-parameter
# (same name) below the current one.
sourceDirs = //babylon5/Movies3/source1
sourceDirs = //babylon5/Movies3/source2
#sourceDirs = C:/
#sourceDirs = G:/
#sourceDirs = H:/DVDs/
```

### Real-Life Examples  
syncdir -s -a -d c:/test/source c:/test/target
syncdir -s -d //myserver/c$ //myserver/backupdisk
syncdir -a c:/test/source c:/test/target

---
This program is brought to you by [Unterrainer Informatik][homepage]  
Project lead is [Gerald Unterrainer][geraldmail]

[geraldmail]: mailto:gerald@unterrainer.info
[homepage]: http://www.unterrainer.info
[coding]: http://www.unterrainer.info/Home/Coding
[makemkv]: http://www.makemkv.com/
[lombok]: https://projectlombok.org
[github]: https://github.com/UnterrainerInformatik/syncdir
