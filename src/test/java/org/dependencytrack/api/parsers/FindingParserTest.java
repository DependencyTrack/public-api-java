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

import org.dependencytrack.api.model.Analysis;
import org.dependencytrack.api.model.Component;
import org.dependencytrack.api.model.Finding;
import org.dependencytrack.api.model.Meta;
import org.dependencytrack.api.model.Project;
import org.dependencytrack.api.model.Severity;
import org.dependencytrack.api.model.Vulnerability;
import org.junit.Assert;
import org.junit.Test;
import java.io.InputStream;
import java.util.List;

public class FindingParserTest {

    @Test
    public void testFindingsApi() throws Exception {
        InputStream in = this.getClass().getResourceAsStream("/findings-api.json");
        FindingParser parser = new FindingParser(in).parse();
        Assert.assertEquals(FindingParser.Format.FINDING_API, parser.getFormat());
        Assert.assertNull(parser.getMeta());
        Assert.assertNull(parser.getProject());
        testFindingsArray(parser.getFindings());
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

        testFindingsArray(parser.getFindings());
    }

    private void testFindingsArray(List<Finding> findings) {
        Assert.assertEquals(7, findings.size());
        Finding finding = findings.get(0);
        Component component = finding.getComponent();
        Vulnerability vulnerability = finding.getVulnerability();
        Analysis analysis = finding.getAnalysis();

        Assert.assertEquals("b815b581-fec1-4374-a871-68862a8f8d52", component.getUuid());
        Assert.assertEquals("timespan", component.getName());
        Assert.assertNull(component.getGroup());
        Assert.assertEquals("2.3.0", component.getVersion());
        Assert.assertEquals("pkg:npm/timespan@2.3.0", component.getPurl());

        Assert.assertEquals("115b80bb-46c4-41d1-9f10-8a175d4abb46", vulnerability.getUuid());
        Assert.assertEquals("NPM", vulnerability.getSource());
        Assert.assertEquals("533", vulnerability.getVulnId());
        Assert.assertEquals("Regular Expression Denial of Service", vulnerability.getTitle());
        Assert.assertEquals("timespan", vulnerability.getSubtitle());
        Assert.assertEquals(Severity.LOW, vulnerability.getSeverity());
        Assert.assertEquals(new Integer(3), vulnerability.getSeverityRank());
        Assert.assertEquals(new Integer(400), vulnerability.getCweId());
        Assert.assertEquals("Uncontrolled Resource Consumption ('Resource Exhaustion')", vulnerability.getCweName());
        Assert.assertEquals("Affected versions of `timespan` are vulnerable to a regular expression denial of service when parsing dates.\n\nThe amplification for this vulnerability is significant, with 50,000 characters resulting in the event loop being blocked for around 10 seconds.", vulnerability.getDescription());
        Assert.assertEquals("No direct patch is available for this vulnerability.\n\nCurrently, the best available solution is to use a functionally equivalent alternative package.\n\nIt is also sufficient to ensure that user input is not being passed into `timespan`, or that the maximum length of such user input is drastically reduced. Limiting the input length to 150 characters should be sufficient in most cases.", vulnerability.getRecommendation());

        Assert.assertEquals("NOT_SET", analysis.getState());
        Assert.assertFalse(analysis.isSuppressed());
    }

}
