/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.doap;

import com.triplescape.doapamine.Person;
import com.triplescape.doapamine.Project;
import com.triplescape.doapamine.Repository;
import com.triplescape.doapamine.Version;

@Project(name = "toxiclibs",
        homepage = "http://toxiclibs.org",
        vendor = "http://postspectacular.com",
        service_endpoint = "",
        old_homepage = {},
        category = "Java",
        created = "2007-01-01",
        shortdesc = "Building blocks for computational design",
        description = "A collection of over 280+ useful classes for various computational design tasks.",
        mailinglist = "http://groups.google.com/group/toxiclibs-users/",
        license = "http://www.gnu.org/licenses/lgpl.html",
        download_page = "http://hg.postspectacular.com/toxiclibs/downloads/",
        download_mirror = {},
        _implements = {},
        language = "en",
        wiki = "",
        blog = "http://toxiclibs.org/",
        audience = "designers, artists, developers",
        bug_database = "http://hg.postspectacular.com/toxiclibs/issues/",
        screenshots = {},
        programming_language = "Java",
        os = {},
        platform = "",
        release = @Version(name = "stable",
                created = "2011-01-03",
                revision = "0020",
                file_release = "",
                platform = "",
                os = ""),
        maintainer = {},
        developer = {
            @Person(name = "Karsten Schmidt",
                    mbox = "info@toxiclibs.org",
                    seeAlso = "")
        },
        documentor = {},
        translator = {},
        tester = {},
        helper = {},
        repository = @Repository(type = Repository.type.HgRepository,
                module = "toxiclibs",
                location = "http://hg.postspectacular.com/toxiclibs/",
                browse = "http://hg.postspectacular.com/toxiclibs/src/",
                anon_root = "http://hg.postspectacular.com/toxiclibs/"))
final class DOAPInfo {

}
