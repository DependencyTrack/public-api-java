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
package org.dependencytrack.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private static final String ZULU = "UTC";
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateUtil() {
    }

    /**
     * Formats a Date object into ISO 8601 format.
     * @param date the Date object to convert
     * @return a String representation of an ISO 8601 date
     * @since 1.0.0
     */
    public static String toISO8601(Date date) {
        TimeZone tz = TimeZone.getTimeZone(ZULU);
        DateFormat df = new SimpleDateFormat(ISO_8601_FORMAT);
        df.setTimeZone(tz);
        return df.format(date);
    }

    /**
     * Parses a ISO 8601 formatted string into a Date object.
     * @param date the string to convert
     * @return a Date
     * @since 1.0.0
     */
    public static Date fromISO8601(String date) throws ParseException {
        TimeZone tz = TimeZone.getTimeZone(ZULU);
        DateFormat df = new SimpleDateFormat(ISO_8601_FORMAT);
        df.setTimeZone(tz);
        return df.parse(date);
    }
}
