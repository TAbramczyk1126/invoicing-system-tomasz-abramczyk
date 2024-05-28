package pl.futurecollars.invoicing.utils

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FilesServiceTest extends Specification {

    private FilesService filesService = new FilesService()

    private Path path = Files.createTempFile('lines', '.txt')

    def "append line to file"() {
        given:
        def originalLine = "Sample line"

        when:
        filesService.appendLineToFile(path, originalLine)

        then:
        Files.readAllLines(path).size() == 1
        Files.readAllLines(path)[0] == originalLine

        and:
        def additionalLine = "Another line"
        filesService.appendLineToFile(path, additionalLine)

        and:
        Files.readAllLines(path).size() == 2
        Files.readAllLines(path)[1] == additionalLine
    }

    def "should write single line to file"() {
        given:
        if (!Files.exists(path)) {
            Files.createFile(path)
        }

        when:
        new FilesService().writeToFile(path, "test")

        then:
        Files.readAllLines(path) == ["test"]
    }

    def "should write all lines to file"() {
        given:
        List<String> lines = ["Line 1", "Line 2", "Line 3"]
        if (!Files.exists(path)) {
            Files.createFile(path)
        }

        when:
        new FilesService().writeLinesToFile(path, lines)

        then:
        Files.readAllLines(path) == lines
    }

    def "should read all lines from file"() {
        given:
        List<String> lines = ["line1", "line2", "line3"]
        Files.write(path, lines)

        expect:
        new FilesService().readAllLines(path) == lines
    }

    def "empty file returns empty collection"() {
        expect:
        [] == filesService.readAllLines(path)
    }

}
