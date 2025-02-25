/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package net.sdo;

import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.ConcurrentHashMap;


@State(Scope.Benchmark)
public class MyBenchmark {

    @Param("10000")
    private int nStrings;
    private static ConcurrentHashMap<String, String> map;
    private static String[] strings;

    @Setup(Level.Iteration)
    public void setUp() {
        map = new ConcurrentHashMap<>();
        strings = new String[nStrings];
        for (int i = 0; i < nStrings; i++) {
            strings[i] = makeRandomString();
        }
    }

    @Benchmark
    public void testIntern(Blackhole bh) {
        for (int i = 0; i < nStrings; i++) {
            String t = strings[i].intern();
            bh.consume(t);
        }
    }

    @Benchmark
    public void testMap(Blackhole bh) {
        for (int i = 0; i < nStrings; i++) {
            String t = map.putIfAbsent(strings[i], strings[i]);
            bh.consume(t);
        }
    }

    private String makeRandomString() {
        return RandomStringUtils.randomAscii(5, 256);
    }

}
