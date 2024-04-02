package pl.futurecollars.invoicing.utils

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FilesServiceTest extends Specification {

    private FilesService filesService = new FilesService()
   // private Path path = Files.createTempFile(Paths.get(System.getProperty("java.io.tmpdir")), "lines", ".txt")

    private Path path = Files.createTempFile('lines', '.txt').toPath()


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
        //stworzyc string z "linijka"
        //dopisac do pliku
        //oczytac z pliku Files.readAllLines
        //porownac czy ta orginalna jest taka sama jak odczytana

        //jezeli chcesz to mozesz dorzucic jeszcze jedna i odczytac do tablicy "readlAllLines"

    }
}
