package pl.futurecollars.invoicing.db.file


import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.utils.FilesService
import pl.futurecollars.invoicing.utils.JsonService

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseIntegrationTest extends AbstractDatabaseTest {

    def databasePath

    @Override
    Database getDatabaseInstance() {

        Path databasePath = Files.createTempFile("testDatabase", ".txt")
        FilesService filesService = new FilesService()
        JsonService jsonService = new JsonService()
        IdService idService = new IdService(Files.createTempFile("testId", ".txt"), filesService)
        return new FileBasedDatabase(filesService, jsonService, idService, databasePath)
    }
/*
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

    }*/
}
