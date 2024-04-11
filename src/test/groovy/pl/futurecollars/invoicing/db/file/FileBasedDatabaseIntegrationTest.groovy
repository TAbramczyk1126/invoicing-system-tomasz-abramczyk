package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.utils.FilesService
import pl.futurecollars.invoicing.utils.JsonService

import java.nio.file.Files

class FileBasedDatabaseIntegrationTest extends AbstractDatabaseTest {

    def databasePath

    @Override
    Database getDatabaseInstance() {

        databasePath = File.createTempFile("testDatabase", ".txt").toPath()
        FilesService filesService = new FilesService()
        JsonService jsonService = new JsonService()
        IdService idService = new IdService(File.createTempFile("testId", ".txt").toPath(), filesService)
        return new FileBasedDatabase(databasePath, idService, filesService, jsonService)
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()
        when:
        db.save(TestHelpers.invoice(4))
        then:
        1 == Files.readAllLines(databasePath).size()
        when:
        db.save(TestHelpers.invoice(5))
        then:
        2 == Files.readAllLines(databasePath).size()
    }
}
