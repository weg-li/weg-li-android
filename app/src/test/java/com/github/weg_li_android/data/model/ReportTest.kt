package com.github.weg_li_android.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ReportTest {

    @Test
    fun `Generate correct Email`() {

        val report = Report(
            0,
            address = "Straße 2, 12045 Berlin",
            type = "Daewoo",
            color = "Braun",
            license = "b-sd-232",
            violation = "Parken auf einem Geh- und Radweg (Zeichen 240/241), mit Behinderung (z.B. Anhalten, Ausweichen, Absteigen)",
            date = "22.04.2020 02:02",
            duration = "02:02 ~ 02:07 (länger als 5 Minuten)",
            carWasEmpty = true,
            obstructionOthers = true,
            hints = "Hinweise",
            userFullName = "Vor und Nachname",
            userAddress = "Hallo Straße 2",
            userPostalCode = "12045 Berlin, Deutschland",
            userPhoneNumber = "051321544651",
            userEmail = "email@gmail.com"
        )

        val testEmail = """
            Sehr geehrte Damen und Herren,
            
            hiermit zeige ich, mit der Bitte um Weiterverfolgung, folgende Verkehrsordnungswidrigkeit an:
            
            Kennzeichen: b-sd-232
            Marke: Daewoo
            Farbe: Braun
            Adresse: Straße 2, 12045 Berlin
            Verstoß: Parken auf einem Geh- und Radweg (Zeichen 240/241), mit Behinderung (z.B. Anhalten, Ausweichen, Absteigen)
            Tatzeit: 22.04.2020 02:02
            Zeitraum: 02:02 ~ 02:07 (länger als 5 Minuten)
            Das Fahrzeug war verlassen.
            Hinweise
            
            Zeuge:
            Name: Vor und Nachname
            Anschrift: Hallo Straße 2, 12045 Berlin, Deutschland
            E-Mail: email@gmail.com
            Telefon: 051321544651
            
            Meine oben gemachten Angaben einschließlich meiner Personalien sind zutreffend und vollständig.
            Als Zeuge bin ich zur wahrheitsgemäßen Aussage und auch zu einem möglichen Erscheinen vor Gericht verpflichtet.
            Vorsätzlich falsche Angaben zu angeblichen Ordnungswidrigkeiten können eine Straftat darstellen.
            
            Beweisfotos, aus denen Kennzeichen und Tatvorwurf erkennbar hervorgehen, befinden sich im Anhang.
            Bitte prüfen Sie den Sachverhalt auch auf etwaige andere Verstöße, die aus den Beweisfotos zu ersehen sind.
            
            Bitte bestätigen Sie Ihre Zuständigkeit und den Erhalt dieser E-Mail durch eine Antwort an email@gmail.com.
            Falls Sie nicht zuständig sein sollten, leiten Sie bitte meine E-Mail weiter und setzen mich dabei in CC.
            Dabei dürfen Sie auch meine persönlichen Daten weiterleiten und für die Dauer des Verfahrens speichern.
            
            Diese Anzeige können Sie online abrufen, dort finden sich auch die Beweisfotos im Original zum Herunterladen:
            https://www.weg-li.de
            
            Mit freundlichen Grüßen
            
            Vor und Nachname""".trimIndent()

        assertEquals(testEmail, report.getEmail())
    }

}