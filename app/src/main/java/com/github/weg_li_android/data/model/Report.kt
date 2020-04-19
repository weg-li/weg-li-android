package com.github.weg_li_android.data.model

data class Report(
    val id: Int = 0,
    var address: String = "",
    var type: String = "",
    var color: String = "",
    var license: String = "",
    var violation: String = "",
    var duration: String = "",
    var obstructionOthers: Boolean = false,
    var fullName: String = "",
    var userAddress: String = "",
    var userPostalCode: String = "",
    var phoneNumber: String = "",
    var userEmail: String = ""
) {

    fun getEmail(): String {
        val emailBuilder = StringBuilder()
        emailBuilder.append(greetings)
        emailBuilder.append(
            explanationWithUserEmail.format(
                userEmail,
                "www.weg-li.de",
                fullName
            )
        )
        return emailBuilder.toString()
    }

    companion object {
        const val greetings = """
Sehr geehrte Damen und Herren,

hiermit zeige ich, mit der Bitte um Weiterverfolgung, folgende Verkehrsordnungswidrigkeit an:"""

        const val explanationWithUserEmail = """

Meine oben gemachten Angaben einschließlich meiner Personalien sind zutreffend und vollständig.
Als Zeuge bin ich zur wahrheitsgemäßen Aussage und auch zu einem möglichen Erscheinen vor Gericht verpflichtet.
Vorsätzlich falsche Angaben zu angeblichen Ordnungswidrigkeiten können eine Straftat darstellen.

Beweisfotos, aus denen Kennzeichen und Tatvorwurf erkennbar hervorgehen, befinden sich im Anhang.
Bitte prüfen Sie den Sachverhalt auch auf etwaige andere Verstöße, die aus den Beweisfotos zu ersehen sind.

Bitte bestätigen Sie Ihre Zuständigkeit und den Erhalt dieser E-Mail durch eine Antwort an %s.
Falls Sie nicht zuständig sein sollten, leiten Sie bitte meine E-Mail weiter und setzen mich dabei in CC.
Dabei dürfen Sie auch meine persönlichen Daten weiterleiten und für die Dauer des Verfahrens speichern.

Diese Anzeige können Sie online abrufen, dort finden sich auch die Beweisfotos im Original zum Herunterladen:
%s

Mit freundlichen Grüßen

%s"""

    }
}