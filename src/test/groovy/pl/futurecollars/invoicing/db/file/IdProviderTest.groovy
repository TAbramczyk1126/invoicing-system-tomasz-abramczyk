package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.utils.FilesService
import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path

class IdProviderTest extends Specification {

    private Path nextIdDbPath = File.createTempFile('nextId', '.txt').toPath()

    def "next id starts from 1 if file was empty"() {
        given:
        IdService idService = new IdService(nextIdDbPath, new FilesService())

        expect:
        ['1'] == Files.readAllLines(nextIdDbPath)

        and:
        1 == idService.getNextIdAndIncrement()
        ['2'] == Files.readAllLines(nextIdDbPath)

        and:
        2 == idService.getNextIdAndIncrement()
        ['3'] == Files.readAllLines(nextIdDbPath)

        and:
        3 == idService.getNextIdAndIncrement()
        ['4'] == Files.readAllLines(nextIdDbPath)
    }

    def "next id starts from last number if file was not empty"() {
        given:
        Files.writeString(nextIdDbPath, "17")
        IdService idService = new IdService(nextIdDbPath, new FilesService())

        expect:
        ['17'] == Files.readAllLines(nextIdDbPath)

        and:
        17 == idService.getNextIdAndIncrement()
        ['18'] == Files.readAllLines(nextIdDbPath)

        and:
        18 == idService.getNextIdAndIncrement()
        ['19'] == Files.readAllLines(nextIdDbPath)

        and:
        19 == idService.getNextIdAndIncrement()
        ['20'] == Files.readAllLines(nextIdDbPath)
    }
}
