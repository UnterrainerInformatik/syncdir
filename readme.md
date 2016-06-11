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


First, you'll need the Java **JRE version 1.8** (**Java SE 8**) or higher.
Make sure that java.exe is on your path, or start the following commands with the path to your java installation (the `/bin` directory of the one you've just downloaded).

Start the program, as you would start any jar by typing:
```
java -jar syncdir-0.1-jar-with-dependencies.jar
```
or put these lines in a batch-file (should be more convenient):
```
java -jar syncdir-0.1-jar-with-dependencies.jar
pause
```

## The Config File  

Here is an example of a config-file. I like to think it is very self-descriptive:

``` properties

```

### Real-Life Examples  


---
This program is brought to you by [Unterrainer Informatik][homepage]  
Project lead is [Gerald Unterrainer][geraldmail]

[geraldmail]: mailto:gerald@unterrainer.info
[homepage]: http://www.unterrainer.info
[coding]: http://www.unterrainer.info/Home/Coding
[makemkv]: http://www.makemkv.com/
[lombok]: https://projectlombok.org
[github]: https://github.com/UnterrainerInformatik/syncdir
