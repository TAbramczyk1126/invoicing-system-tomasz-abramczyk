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
}
