/**
* Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* -Redistribution of source code must retain the above copyright notice, 
* this list of conditions and the following disclaimer.
*
* -Redistribution in binary form must reproduce the above copyright notice, 
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* Neither the name of Sun Microsystems, Inc. or the names of contributors may 
* be used to endorse or promote products derived from this software without 
* specific prior written permission.
* 
* This software is provided "AS IS," without a warranty of any kind.
* ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
* ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
* NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS
* LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
* RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
* IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
* OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
* PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
* ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
* BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
*
* You acknowledge that this software is not designed or intended for use in the
* design, construction, operation or maintenance of any nuclear facility.
*/

Project: net.java.games.joal.* & net.java.games.sound3d.*
Purpose Open Source Java Bindings for OpenAL and Object-Oriented 3D sound toolkit
Author:
    -- JOAL/Sound3D API Original Author
        Athomas Goldberg
        Wildcard
        Java Games Initiative
        Software Advanced Technologies Group,
        Sun Microsystems
    -- This file updated 08/19/2003 by Ole Arndt <ole at sugarshark dot com>
    -- This file updated 11/23/2003 by Gregory Pierce <gpierce@gregorypierce.com>    
    -- This file updated 12/21/2005 by Kenneth Russell <kbr@dev.java.net>

Introduction:
=============

This is the source tree for the Java Game Initiative (JGI) Open Source
client game programming APIs for OpenAL and Sound3D.


Build Requirements:
===================

This project has been built under Win32, Linux, and OSX. The following
packages and tools have been used:

* All Systems:
  -- Sun J2SDK 1.4.2 (available at java.sun.com)
  -- ANT 1.5.3 (available at apache.org)
  -- JUnit 3.8.1 (available at junit.org) copy junit.jar to the apache-ant lib directory

* Windows:
  -- Win32 (Win XP in the case of our machine)
  -- OpenAL1.0 SDK from Creative Labs (available at http://developer.creative.com
     under "Gaming -> Development Kits -> Open AL")
  -- MinGW 2.0.0  plus the following updates: (all available at www.mingw.org) 
     -- binutils 2.13.90
     -- w32api-2.2
     -- mingw-runtime-2.4

* Linux:
  -- Linux i386 (Redhat 9.0)
  -- gcc 3.2.2, binutils 2.13.90
  -- OpenAL CVS version from opensource.creative.com (see CVS instructions at 
     www.openal.org)

* OSX
  -- OSX 10.2 or later
  -- OSX Developer Tools
  -- OpenAL1.0 SDK from Creative Labs (avaulable at http://developer.creative.com under "Gaming->Development Kits->Open AL") 

JOAL requires the GlueGen workspace to be checked out as a sibling
directory to the joal directory ("cvs co gluegen"). GlueGen will be
built automatially during the JOAL build process.

Directory Organization:
=======================

  -- make           Build-related files and the main build.xml
  -- src            The actual source for the JOAL APIs.
  -- build          (generated directory) Where the Jar and DLL files get built to
  -- javadoc_public (generated directory) Where the public Javadoc gets built to
  -- unit_tests     A couple of small tests
  -- www            JOAL project webpage files

Preparations:
=============

* Windows:
 Grab the OpenAL SDK from the openal.org downloads and install it.

* Linux:
  OpenAL comes with various Linux distributions. You might have it already. 
  If not, try rpmfind, apt-get or emerge. 

  More up-to-date versions are available from openal.org in the
  downloads section. Install first the openal-[version].i586.rpm and
  then the openal-devel-[version]-i586.rpm. Unfortunately, due to
  confusion in the OpenAL version numbering, the version from
  openal.org may seem to be "older" than the version preinstalled on
  the Linux distribution, even though it is more recent. To work
  around this problem, do the following steps:

    # rpm --force --upgrade openal-0.0.8-1.i586.rpm
    # rpm -i openal-devel-0.0.8-1.i586.rpm

  Now test to make sure the RPMs are installed:

    # rpm -qa | grep -i openal
    openal-0.0.8-1
    openal-devel-0.0.8-1 

  For the newest version you need to compile OpenAL yourself from CVS:

    -- follow the instruction at www.openal.org to check out a fresh copy 
    -- in the linux subdir type './autogen.sh 
    -- look at the build options with ./configure --help  
    -- configure it with./configure --enable-sdl --enable-vorbis [-enable-more]
       (see openal.spec for a full set)
    -- build it with make && make test and run the tests
    -- as root type make install
    -- add /usr/local/lib to you /etc/ld.so.conf and run ldconfig

* OSX:
 Grab the OpenAL SDK from openal.org and install it.

 
JOAL Build Instructions:
===================

Download the current ANTLR jar file (http://www.antlr.org/).

Copy the gluegen.properties file from the make/ subdirectory of the
GlueGen workspace into your home directory, or in particular where the
Java system property user.home points to (on Windows, this is
C:\Documents and Settings\username\). Edit the value of the antlr.jar
property this file to point to the full path of the ANTLR jar. ANTLR
is used during the glue code generation process.

Copy the joal.properties file from the make/ subdirectory into your
home directory, or in particular where the Java system property
user.home points to (on Windows, this is C:\Documents and
Settings\username\). Edit the joal.lib.dir property to point to the
lib directory of your current OpenAL installation. For Windows this
might be "C:/Program Files/OpenAL 1.1 SDK/libs/Win32".

Under Linux set the property to the toplevel directory of your OpenAL CVS version.
If OpenAl came with your distribution and the header files are in /usr/include/AL,
set the property to '/usr'.

Under OSX the default distribution will install to "/Library/Frameworks/OpenAL.framework ".

On Windows, you may want to edit the win32.c.compiler property to
indicate the C compiler you prefer to use (vc6, vc7 or mingw).

On OS X, you can produce 'fat' dual PowerPC/x86 binaries on an
appropriately-equipped machine by uncommenting the declaration of the
'macosxfat' property.

To clean: ant clean
To build: ant all (or just ant)
To build docs: ant javadoc
To test: ant runtests

Release Info:
    Initial Release:  This release contains an implementation of the Java
    bindings for OpenAL, as well as the Sound3D Object-Oriented toolkit for games.
