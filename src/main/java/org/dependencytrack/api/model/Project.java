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
package org.dependencytrack.api.model;

public class Project {

    private final String uuid;
    private final String name;
    private final String version;
    private final String description;
    private final String purl;

    public Project(final String uuid, final String name, final String version, final String description, final String purl) {
        this.uuid = uuid;
        this.name = name;
        this.version = version;
        this.description = description;
        this.purl = purl;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getPurl() {
        return purl;
    }
}
