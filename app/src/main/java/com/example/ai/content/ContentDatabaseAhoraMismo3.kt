package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo AHORA_MISMO (D2, parte 3 de 3): CONTACTO_INEVITABLE, ENCUENTRO_CASUAL, RECUPERAR_PAREJA.
 * Los cierres del grupo ya estan cubiertos por el contenido semilla de D1 (IMPULSO_CONTACTAR).
 * Este archivo aporta reconocimiento, reencuadre y accion propios de estas tres etiquetas.
 */
object ContentDatabaseAhoraMismo3 {

    private val CONTACTO = setOf(ClinicalCategory.CONTACTO_INEVITABLE)
    private val ENCUENTRO = setOf(ClinicalCategory.ENCUENTRO_CASUAL)
    private val RECUPERAR = setOf(ClinicalCategory.RECUPERAR_PAREJA)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= CONTACTO_INEVITABLE =================
        reco(CONTACTO, "Tener que verle o hablarle por un motivo concreto no es lo mismo que elegir buscarlo."),
        reco(CONTACTO, "Un contacto necesario, por trabajo, hijos o trámites, puede sostenerse sin abrir toda la herida de golpe."),
        reco(CONTACTO, "Prepararte antes de un contacto inevitable ayuda a que te pese menos después."),
        reco(CONTACTO, "No poder evitar del todo el contacto no significa que pierdas el control de cómo lo llevas."),
        reco(CONTACTO, "Es normal sentir tensión antes de un encuentro que no puedes evitar."),

        reen(CONTACTO, ESTOICO, "No depende de ti que este contacto sea necesario; sí depende de ti cómo te presentas a él."),
        reen(CONTACTO, ESTOICO, "Puedes mantener el asunto en lo estrictamente necesario, sin alargarlo más de lo que toca."),
        reen(CONTACTO, ESTOICO, "Prepararte con calma antes es un acto que sí está en tu mano."),
        reen(CONTACTO, ESTOICO, "No hace falta que el encuentro salga perfecto, solo que tú actúes con la serenidad que puedas sostener."),
        reen(CONTACTO, ESTOICO, "Después del contacto, vuelve a lo que hoy sí depende de ti."),
        reen(CONTACTO, ESTOICO, "Un intercambio breve y claro basta cuando el motivo es práctico, no emocional."),

        reen(CONTACTO, CATOLICO, "Puedes pedir fuerza y calma antes de un encuentro que no elegiste pero que toca sostener."),
        reen(CONTACTO, CATOLICO, "Tratarle con respeto no te obliga a abrir de nuevo lo que ya decidiste cerrar."),
        reen(CONTACTO, CATOLICO, "La paz interior no depende de que el encuentro sea agradable, sino de cómo lo atraviesas."),
        reen(CONTACTO, CATOLICO, "Puedes encomendar este momento antes de que llegue, para afrontarlo con más serenidad."),
        reen(CONTACTO, CATOLICO, "Mantener la calma en lo necesario también es una forma de cuidar tu propio corazón."),
        reen(CONTACTO, CATOLICO, "Después del encuentro, date un momento de silencio para volver a tu centro."),

        reen(CONTACTO, PSICOLOGIA, "Prepararte mentalmente antes reduce la reactividad emocional durante el encuentro."),
        reen(CONTACTO, PSICOLOGIA, "Mantener el contacto breve y centrado en el motivo práctico ayuda a regular la emoción."),
        reen(CONTACTO, PSICOLOGIA, "Es normal que el cuerpo se active antes de un encuentro así; no significa que algo vaya mal."),
        reen(CONTACTO, PSICOLOGIA, "Tener un plan simple de qué decir y qué no decir reduce la incertidumbre del momento."),
        reen(CONTACTO, PSICOLOGIA, "Después del contacto, date tiempo para bajar la activación antes de seguir con el día."),
        reen(CONTACTO, PSICOLOGIA, "Un encuentro necesario no tiene que resolver nada emocional, solo cumplir su propósito práctico."),

        acc(CONTACTO, "Escribe antes del encuentro los dos o tres puntos que necesitas tratar, y quédate solo en eso."),
        acc(CONTACTO, "Respira despacio unos minutos antes de la hora del encuentro para llegar más regulado/a."),
        acc(CONTACTO, "Si puedes, elige un lugar o formato (mensaje, llamada breve) que te resulte más manejable."),
        acc(CONTACTO, "Después del contacto, haz algo que te calme: caminar, escuchar música o llamar a alguien de confianza."),
        acc(CONTACTO, "Avisa a alguien de confianza de que tienes este encuentro, para sentir apoyo antes y después."),
        acc(CONTACTO, "Anota cómo te fue después del encuentro, para reconocer que pudiste sostenerlo."),

        // ================= ENCUENTRO_CASUAL =================
        reco(ENCUENTRO, "Cruzártelo sin buscarlo remueve algo que ya creías más tranquilo."),
        reco(ENCUENTRO, "Un encuentro casual puede desestabilizar un rato, aunque el proceso siga avanzando de fondo."),
        reco(ENCUENTRO, "No elegiste este cruce, pero sí puedes elegir cómo sigues el resto del día."),
        reco(ENCUENTRO, "Sentir el corazón acelerado al verlo de pronto es una reacción del cuerpo, no una señal de retroceso."),
        reco(ENCUENTRO, "Un cruce inesperado no borra el camino recorrido hasta ahora."),

        reen(ENCUENTRO, ESTOICO, "No dependía de ti cruzártelo; sí depende de ti qué haces en los minutos siguientes."),
        reen(ENCUENTRO, ESTOICO, "La reacción del cuerpo al verlo no tiene por qué convertirse en una acción impulsiva."),
        reen(ENCUENTRO, ESTOICO, "Puedes seguir tu camino con la misma calma con la que ibas antes del cruce."),
        reen(ENCUENTRO, ESTOICO, "Un encuentro inesperado no cambia lo que hoy sí está en tu mano hacer."),
        reen(ENCUENTRO, ESTOICO, "Lo perturbador no es haberlo visto, sino el juicio que le añades después."),
        reen(ENCUENTRO, ESTOICO, "Puedes observar la emoción sin dejar que decida tus próximos pasos."),

        reen(ENCUENTRO, CATOLICO, "Puedes pedir paz en este momento, sin que un cruce inesperado borre lo caminado."),
        reen(ENCUENTRO, CATOLICO, "No tienes que reaccionar de inmediato; puedes darte un instante antes de seguir."),
        reen(ENCUENTRO, CATOLICO, "Un encuentro casual no define tu proceso entero, solo es un momento dentro de él."),
        reen(ENCUENTRO, CATOLICO, "Puedes entregar esta sacudida en oración y seguir tu camino con más calma."),
        reen(ENCUENTRO, CATOLICO, "Tu paz no depende de evitar verlo para siempre, sino de cómo sostienes estos momentos."),
        reen(ENCUENTRO, CATOLICO, "Después del cruce, busca algo que te devuelva serenidad."),

        reen(ENCUENTRO, PSICOLOGIA, "Un encuentro inesperado activa el sistema de alerta; es una respuesta automática, no una elección."),
        reen(ENCUENTRO, PSICOLOGIA, "La intensidad de la reacción suele bajar en los minutos siguientes si no la alimentas con más pensamientos."),
        reen(ENCUENTRO, PSICOLOGIA, "Notar la sensación sin juzgarla ayuda a que pase más rápido."),
        reen(ENCUENTRO, PSICOLOGIA, "Un cruce casual no borra el progreso acumulado en los días anteriores."),
        reen(ENCUENTRO, PSICOLOGIA, "Respirar despacio después del encuentro ayuda a que el cuerpo vuelva a su estado habitual."),
        reen(ENCUENTRO, PSICOLOGIA, "Registrar cómo te sentiste ayuda a entender mejor tus propias reacciones para la próxima vez."),

        acc(ENCUENTRO, "Respira despacio varias veces en cuanto puedas alejarte del lugar."),
        acc(ENCUENTRO, "Sigue con el plan que tenías para hoy, aunque el ánimo haya cambiado un poco."),
        acc(ENCUENTRO, "Llama o escribe a alguien de confianza para contarle lo que acaba de pasar."),
        acc(ENCUENTRO, "Anota cómo te sentiste al verlo, sin juzgarte por la reacción que tuviste."),
        acc(ENCUENTRO, "Haz algo que te calme los próximos minutos: caminar, escuchar música o sentarte tranquilo/a un rato."),
        acc(ENCUENTRO, "Si el malestar persiste al llegar a casa, dedica un rato a una actividad que te ayude a soltarlo."),

        // ================= RECUPERAR_PAREJA =================
        reco(RECUPERAR, "Querer recuperar la relación es una parte más de lo que sientes en este proceso, no un error por sentirlo."),
        reco(RECUPERAR, "El deseo de volver convive muchas veces con la certeza de que la relación no funcionaba."),
        reco(RECUPERAR, "Extrañar lo bueno no significa que el conjunto de la relación fuera bueno para ti."),
        reco(RECUPERAR, "Este deseo no tiene que decidir hoy lo que vas a hacer."),
        reco(RECUPERAR, "Sentir que quieres volver es información sobre tu momento actual, no una orden a seguir."),

        reen(RECUPERAR, ESTOICO, "El deseo de volver no depende de ti del todo; lo que sí depende de ti es qué haces con él."),
        reen(RECUPERAR, ESTOICO, "Antes de actuar, examina si lo que buscas es a la persona o el alivio de dejar de sentir esto."),
        reen(RECUPERAR, ESTOICO, "Recordar solo lo bueno es un juicio parcial; la relación completa incluye también lo que te costó."),
        reen(RECUPERAR, ESTOICO, "Puedes sostener el deseo sin obedecerlo de inmediato."),
        reen(RECUPERAR, ESTOICO, "Actuar por impulso rara vez lleva a una decisión que resista el paso del tiempo."),
        reen(RECUPERAR, ESTOICO, "Lo que decidas hoy sobre este deseo forma el hábito que tendrás mañana."),

        reen(RECUPERAR, CATOLICO, "Puedes entregar este deseo en oración en lugar de actuarlo de inmediato."),
        reen(RECUPERAR, CATOLICO, "El amor verdadero también implica discernir si esa relación te ayudaba a crecer."),
        reen(RECUPERAR, CATOLICO, "No tienes que decidir hoy; puedes pedir claridad antes de dar cualquier paso."),
        reen(RECUPERAR, CATOLICO, "Extrañar no es lo mismo que estar llamado/a a volver a ese mismo camino."),
        reen(RECUPERAR, CATOLICO, "Tu valor no depende de que esa relación se recupere."),
        reen(RECUPERAR, CATOLICO, "Pide paz para distinguir el deseo del momento de lo que de verdad te conviene."),

        reen(RECUPERAR, PSICOLOGIA, "El deseo de volver suele aparecer con más fuerza en los momentos de mayor soledad o cansancio."),
        reen(RECUPERAR, PSICOLOGIA, "La memoria selectiva tiende a guardar lo positivo y suavizar lo que dolía."),
        reen(RECUPERAR, PSICOLOGIA, "Antes de actuar, puede ayudar preguntarte qué necesidad concreta buscas cubrir con eso."),
        reen(RECUPERAR, PSICOLOGIA, "Un deseo intenso no es lo mismo que una decisión meditada."),
        reen(RECUPERAR, PSICOLOGIA, "Dejar pasar tiempo entre el impulso y la acción suele mejorar la calidad de la decisión."),
        reen(RECUPERAR, PSICOLOGIA, "Revisar qué no funcionaba ayuda a equilibrar la nostalgia con la realidad completa."),

        acc(RECUPERAR, "Escribe una lista con lo que sí funcionaba y otra con lo que te costaba de esa relación."),
        acc(RECUPERAR, "Espera al menos veinticuatro horas antes de dar cualquier paso relacionado con este deseo."),
        acc(RECUPERAR, "Habla con alguien de confianza sobre lo que sientes antes de actuar."),
        acc(RECUPERAR, "Relee, si los tienes, tus propios apuntes de por qué decidiste terminar o distanciarte."),
        acc(RECUPERAR, "Haz algo que te conecte con tu vida actual: una actividad, un plan, una llamada a alguien querido."),
        acc(RECUPERAR, "Anota qué necesidad concreta sientes ahora mismo, más allá de la persona.")
    )
}
