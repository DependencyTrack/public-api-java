/*
 * This file is part of Dependency-Track Public Java API
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dependencytrack.api.parsers;

import org.dependencytrack.api.model.Meta;
import org.dependencytrack.api.model.Project;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

public class FindingParserTest {

    @Test
    public void testFindingsApi() throws Exception {
        InputStream in = this.getClass().getResourceAsStream("/findings-api.json");
        FindingParser parser = new FindingParser(in).parse();
        Assert.assertEquals(FindingParser.Format.FINDING_API, parser.getFormat());
        Assert.assertNull(parser.getMeta());
        Assert.assertNull(parser.getProject());
        Assert.assertEquals(7, parser.getFindings().size());
    }

    @Test
    public void testFindingsPackagingFormat() throws Exception {
        InputStream in = this.getClass().getResourceAsStream("/findings-packaging-format.json");
        FindingParser parser = new FindingParser(in).parse();
        Assert.assertEquals(FindingParser.Format.FINDING_PACKAGING_FORMAT, parser.getFormat());

        Meta meta = parser.getMeta();
        Assert.assertNotNull(meta);
        Assert.assertEquals("Dependency-Track", meta.getApplication());
        Assert.assertEquals("3.4.0", meta.getVersion());
        Assert.assertEquals("2017-04-18T23:31:42.136Z", meta.getTimestamp());
        Assert.assertEquals("http://dtrack.example.org", meta.getBaseUrl());

        Project project = parser.getProject();
        Assert.assertNotNull(project);
        Assert.assertEquals("122a83b7-ab3b-49fe-a39e-ac9698385bb2", project.getUuid());
        Assert.assertEquals("Acme Example", project.getName());
        Assert.assertEquals("1.0", project.getVersion());
        Assert.assertEquals("A sample application", project.getDescription());
        Assert.assertNull(project.getPurl());

        Assert.assertEquals(7, parser.getFindings().size());
    }

}
