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
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class FindingParser {

    private JsonReader jsonReader;
    private Meta meta;
    private Project project;
    private ArrayList<Finding> findings;
    private Format format;

    public enum Format {
        FINDING_API,
        FINDING_PACKAGING_FORMAT
    }

    protected FindingParser() { }

    public FindingParser(final String jsonString) {
        jsonReader = Json.createReader(new StringReader(jsonString));
    }

    public FindingParser(final InputStream jsonResponseInputStream) {
        jsonReader = Json.createReader(jsonResponseInputStream);
    }

    public FindingParser parse() {
        final JsonStructure structure = jsonReader.read();
        if (structure instanceof JsonArray) {
            this.format = Format.FINDING_API;
            this.findings = parseFindings((JsonArray)structure);
        } else if (structure instanceof JsonObject) {
            this.format = Format.FINDING_PACKAGING_FORMAT;
            final JsonObject root = (JsonObject)structure;
            this.meta = parseMeta(root.getJsonObject("meta"));
            this.project = parseProject(root.getJsonObject("project"));
            this.findings = parseFindings(root.getJsonArray("findings"));
        }
        return this;
    }

    private ArrayList<Finding> parseFindings(JsonArray json) {
        final ArrayList<Finding> findings = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            final Finding finding = parseFinding(json.getJsonObject(i));
            if (!finding.getAnalysis().isSuppressed()) {
                findings.add(finding);
            }
        }
        return findings;
    }

    private Finding parseFinding(JsonObject json) {
        final Project project = parseProject(json.getJsonObject("project"));
        final Component component = parseComponent(json.getJsonObject("component"));
        final Vulnerability vulnerability = parseVulnerability(json.getJsonObject("vulnerability"));
        final Analysis analysis = parseAnalysis(json.getJsonObject("analysis"));
        final String matrix = trimToNull(json.getString("matrix"));
        return new Finding(component, vulnerability, analysis, matrix);
    }

    private Component parseComponent(JsonObject json) {
        final String uuid = trimToNull(json.getString("uuid"));
        final String name = trimToNull(json.getString("name"));
        final String group = trimToNull(json.getString("group", null));
        final String version = trimToNull(json.getString("version", null));
        final String purl = trimToNull(json.getString("purl", null));
        return new Component(uuid, name, group, version, purl);
    }

    private Vulnerability parseVulnerability(JsonObject json) {
        final String uuid = trimToNull(json.getString("uuid"));
        final String source = trimToNull(json.getString("source"));
        final String vulnId = trimToNull(json.getString("vulnId", null));
        final String title = trimToNull(json.getString("title", null));
        final String subtitle = trimToNull(json.getString("subtitle", null));
        final String description = trimToNull(json.getString("description", null));
        final String recommendation = trimToNull(json.getString("recommendation", null));
        final Severity severity = Severity.valueOf(json.getString("severity", null));
        final Integer severityRank = json.getInt("severityRank", -1);
        final Integer cweId = json.getInt("cweId", -1);
        final String cweName = trimToNull(json.getString("cweName", null));
        return new Vulnerability(uuid, source, vulnId, title, subtitle, description, recommendation, severity, severityRank, cweId, cweName);
    }

    private Analysis parseAnalysis(JsonObject json) {
        final String state = trimToNull(json.getString("state", null));
        final boolean isSuppressed = json.getBoolean("isSuppressed", false);
        return new Analysis(state, isSuppressed);
    }

    /*
        Project is only included in Findings Packaging Format, not from the Findings API.
        Make it optional.
    */
    private Project parseProject(JsonObject json) {
        if (json == null) {
            return null;
        }
        final String uuid = trimToNull(json.getString("uuid", null));
        final String name = trimToNull(json.getString("name", null));
        final String version = trimToNull(json.getString("version", null));
        final String description = trimToNull(json.getString("description", null));
        final String purl = trimToNull(json.getString("purl", null));
        return new Project(uuid, name, version, description, purl);
    }

    /*
        Meta is only included in Findings Packaging Format, not from the Findings API.
        Make it optional.
     */
    private Meta parseMeta(JsonObject json) {
        if (json == null) {
            return null;
        }
        final String application = trimToNull(json.getString("application", null));
        final String version = trimToNull(json.getString("version", null));
        final String timestamp = trimToNull(json.getString("timestamp", null));
        final String baseUrl = trimToNull(json.getString("baseUrl", null));
        return new Meta(application, version, timestamp, baseUrl);
    }

    public ArrayList<Finding> getFindings() {
        return findings;
    }

    public Meta getMeta() {
        return meta;
    }

    public Project getProject() {
        return project;
    }

    public Format getFormat() {
        return format;
    }

    private static String trimToNull(final String string) {
        if (string == null) {
            return null;
        }
        final String trimmed = string.trim();
        return trimmed.length() == 0 ? null : trimmed;
    }


    public static void main(String[] args) throws Exception {
        FindingParser parser = new FindingParser(new FileInputStream(new File("/Users/steve/Desktop/findings/findings.json")));
        parser.parse();
    }
}
