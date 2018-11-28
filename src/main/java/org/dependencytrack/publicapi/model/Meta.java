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
package org.dependencytrack.publicapi.model;

public class Meta {

    private final String application;
    private final String version;
    private final String timestamp;
    private final String baseUrl;

    public Meta(final String application, final String version, final String timestamp, final String baseUrl) {
        this.application = application;
        this.version = version;
        this.timestamp = timestamp;
        this.baseUrl = baseUrl;
    }

    public String getApplication() {
        return application;
    }

    public String getVersion() {
        return version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
