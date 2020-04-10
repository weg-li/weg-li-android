package com.github.weg_li_android.ui.main.viewmodel

import com.github.weg_li_android.data.model.Report
import com.github.weg_li_android.data.repository.Repository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    lateinit var mainViewModel: MainViewModel
    private val mockRepository: Repository = mock()

    @Before
    fun setup() {
        mainViewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `sendReport calls repository`() {
        mainViewModel.sendReport(Report())
        verify(mockRepository).sendEmail("")
    }

    @Test
    fun `sendReport transforms Report into String`() {
        val report = Report(
            0,
            "Straße",
            "BMW",
            "blue",
            "B-MW-23",
            "Parked on bike lane",
            "3",
            true,
            "Mr. Smith",
            "User Straße",
            "12045",
            "1234567890",
            "mr.smith@gmail.com"
        )

        mainViewModel.sendReport(report)
        verify(mockRepository).sendEmail(
            """
Sehr geehrte Damen und Herren,

hiermit zeige ich, mit der Bitte um Weiterverfolgung, folgende Verkehrsordnungswidrigkeit an:

Meine oben gemachten Angaben einschließlich meiner Personalien sind zutreffend und vollständig.
Als Zeuge bin ich zur wahrheitsgemäßen Aussage und auch zu einem möglichen Erscheinen vor Gericht verpflichtet.
Vorsätzlich falsche Angaben zu angeblichen Ordnungswidrigkeiten können eine Straftat darstellen.

Beweisfotos, aus denen Kennzeichen und Tatvorwurf erkennbar hervorgehen, befinden sich im Anhang.
Bitte prüfen Sie den Sachverhalt auch auf etwaige andere Verstöße, die aus den Beweisfotos zu ersehen sind.

Bitte bestätigen Sie Ihre Zuständigkeit und den Erhalt dieser E-Mail durch eine Antwort an ${report.userEmail}.
Falls Sie nicht zuständig sein sollten, leiten Sie bitte meine E-Mail weiter und setzen mich dabei in CC.
Dabei dürfen Sie auch meine persönlichen Daten weiterleiten und für die Dauer des Verfahrens speichern.

Diese Anzeige können Sie online abrufen, dort finden sich auch die Beweisfotos im Original zum Herunterladen:
www.weg-li.de

Mit freundlichen Grüßen

${report.fullName}"""
        )
    }
}