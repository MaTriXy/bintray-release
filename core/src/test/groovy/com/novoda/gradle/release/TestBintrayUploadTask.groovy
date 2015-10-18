package com.novoda.gradle.release

import com.novoda.gradle.test.IntegrationTest
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.junit.Test
import org.junit.experimental.categories.Category

import static com.google.common.truth.Truth.assertThat

@Category(IntegrationTest.class)
public class TestBintrayUploadTask {

    @Test
    public void testBintrayUploadTask() {
        String standardOutput = runTasksOnBintrayReleasePlugin(['-PbintrayUser=U', '-PbintrayKey=K'], "bintrayUpload")

        assertThat(standardOutput).contains("BUILD SUCCESSFUL")
    }

    String runTasksOnBintrayReleasePlugin(List<String> arguments = [], String... tasks) {
        ProjectConnection conn

        try {
            GradleConnector gradleConnector = GradleConnector.newConnector().forProjectDirectory(new File("."))
            conn = gradleConnector.connect()

            ByteArrayOutputStream stream = new ByteArrayOutputStream()
            def builder = conn.newBuild()
            if (arguments) {
                builder.withArguments(*arguments)
            }
            builder.forTasks(tasks).setStandardOutput(stream).run()
            String output = stream.toString()
            return output
        }
        finally {
            conn?.close()
        }
    }
}
