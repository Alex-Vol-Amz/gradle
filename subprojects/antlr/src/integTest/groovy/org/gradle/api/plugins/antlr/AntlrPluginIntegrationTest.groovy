/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.plugins.antlr

import org.gradle.integtests.fixtures.WellBehavedPluginTest

class AntlrPluginIntegrationTest extends WellBehavedPluginTest {
    @Override
    String getMainTask() {
        return "build"
    }

    def "can handle grammar in nested folders"(){
        given:
        buildFile << """
            apply plugin: "java"
            apply plugin: "antlr"

            ${jcenterRepository()}
        """
        and:

        file("src/main/antlr/org/acme/TestGrammar.g") << """ class TestGrammar extends Parser;
        options {
            buildAST = true;
        }

        expr:   mexpr (PLUS^ mexpr)* SEMI!
        ;

        mexpr
        :   atom (STAR^ atom)*
        ;

        atom:   INT
        ;"""

        when:
        succeeds("generateGrammarSource")

        then:
        file("build/generated-src/antlr/main/TestGrammar.java").exists()
        file("build/generated-src/antlr/main/TestGrammar.smap").exists()
        file("build/generated-src/antlr/main/TestGrammarTokenTypes.java").exists()
        file("build/generated-src/antlr/main/TestGrammarTokenTypes.txt").exists()

    }

}
