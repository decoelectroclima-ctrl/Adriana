package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo AHORA_MISMO (D2, parte 2 de 3): SENALES_DIGITALES, RECAIDA_OCURRIDA, AUTOCRITICA_RECAIDA.
 * Los cierres y las citas del grupo ya están cubiertos en ContentDatabase (contenido semilla de IMPULSO_CONTACTAR);
 * este archivo aporta reconocimiento, reencuadre y acción propios de estas tres etiquetas.
 */
object ContentDatabaseAhoraMismo2 {

    private val SENALES = setOf(ClinicalCategory.SENALES_DIGITALES)
    private val RECAIDA = setOf(ClinicalCategory.RECAIDA_OCURRIDA)
    private val AUTOCRITICA = setOf(ClinicalCategory.AUTOCRITICA_RECAIDA)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val NOCHE = AtomContext(timeOfDay = TimeOfDay.NOCHE)
    private val PRIMEROS = AtomContext(phase = TimePhase.PRIMEROS_DIAS)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String, ctx: AtomContext = AtomContext()) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw, context = ctx)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= SENALES_DIGITALES =================
        reco(SENALES, "Mirar sus redes o su última conexión es otra forma de buscar contacto sin llegar a escribir."),
        reco(SENALES, "Revisar su perfil una y otra vez suele calmar un momento y remover otro rato después."),
        reco(SENALES, "Querer saber qué hace o con quién está es una forma más del apego que aún sostienes."),
        reco(SENALES, "Cada visita a su perfil busca una respuesta que la pantalla no puede darte del todo."),
        reco(SENALES, "Notar el impulso de mirar ya es un paso antes de decidir si lo sigues o no."),

        reen(SENALES, ESTOICO, "Lo que él publique o deje de publicar no depende de ti; sí depende de ti cuánta atención le prestas."),
        reen(SENALES, ESTOICO, "Cada vez que entras a mirar, entregas un poco de tu atención a algo que no gobiernas."),
        reen(SENALES, ESTOICO, "Puedes examinar el impulso de mirar sin actuarlo de inmediato."),
        reen(SENALES, ESTOICO, "La información que buscas ahí no cambia lo que hoy sí está en tu mano hacer."),
        reen(SENALES, ESTOICO, "Entrenar la distancia con la pantalla es también entrenar el dominio sobre ti mismo/a."),
        reen(SENALES, ESTOICO, "Silenciar o dejar de seguir no es rencor; es cuidar dónde pones tu atención."),

        reen(SENALES, CATOLICO, "Buscar señales suyas en la pantalla no te va a dar la paz que en realidad necesitas."),
        reen(SENALES, CATOLICO, "Puedes elegir no mirar hoy, como un pequeño gesto de cuidado hacia tu propio corazón."),
        reen(SENALES, CATOLICO, "La curiosidad por su vida ahora mismo suele alimentar más la herida que cerrarla."),
        reen(SENALES, CATOLICO, "No tienes que vigilar su vida para sanar la tuya."),
        reen(SENALES, CATOLICO, "Cada vez que sueltas la necesidad de mirar, ganas un poco más de paz para ti."),
        reen(SENALES, CATOLICO, "Confiar en tu propio proceso incluye no depender de lo que veas en una pantalla."),

        reen(SENALES, PSICOLOGIA, "Revisar sus redes suele funcionar como un alivio rápido que después deja más malestar."),
        reen(SENALES, PSICOLOGIA, "Este comportamiento se parece a una compulsión: calma un momento y refuerza el ciclo después."),
        reen(SENALES, PSICOLOGIA, "Cuanta más información buscas, más difícil es que la mente descanse del tema."),
        reen(SENALES, PSICOLOGIA, "Poner una barrera práctica (silenciar, dejar de seguir) reduce el esfuerzo de resistir cada vez."),
        reen(SENALES, PSICOLOGIA, "Notar el impulso sin actuarlo de inmediato debilita poco a poco su fuerza."),
        reen(SENALES, PSICOLOGIA, "El alivio que da mirar es breve; el malestar que deja después suele durar más."),

        acc(SENALES, "Cierra la aplicación ahora y ponte un temporizador de diez minutos antes de decidir si vuelves a abrirla."),
        acc(SENALES, "Silencia o deja de seguir su perfil, aunque sea de forma temporal."),
        acc(SENALES, "Cambia de pantalla y haz algo con las manos durante los próximos diez minutos."),
        acc(SENALES, "Anota qué esperabas encontrar al mirar y si realmente lo has encontrado antes."),
        acc(SENALES, "Guarda el móvil en otra habitación durante un rato y dedica ese tiempo a otra cosa."),
        acc(SENALES, "Avisa a alguien de confianza de que hoy te cuesta no mirar y pídele compañía un rato."),

        // ================= RECAIDA_OCURRIDA =================
        reco(RECAIDA, "Haber recaído no borra el camino que ya llevas recorrido hasta hoy."),
        reco(RECAIDA, "Una recaída es parte común del proceso, no una señal de que todo vuelve a empezar de cero."),
        reco(RECAIDA, "Escribirle o buscarlo hoy no significa que no puedas volver a sostener tu límite mañana."),
        reco(RECAIDA, "El camino de soltar rara vez es una línea recta; también tiene estos momentos."),
        reco(RECAIDA, "Lo que hiciste hoy no define lo que puedes elegir hacer a partir de ahora."),

        reen(RECAIDA, ESTOICO, "Lo ocurrido ya no depende de ti; lo que hagas con ello a partir de ahora, sí."),
        reen(RECAIDA, ESTOICO, "Juzgarte con dureza no deshace la recaída, solo añade otro peso encima."),
        reen(RECAIDA, ESTOICO, "Cada recaída también enseña algo sobre lo que aún necesitas entrenar."),
        reen(RECAIDA, ESTOICO, "Puedes aceptar el hecho sin resignarte a que se repita del mismo modo."),
        reen(RECAIDA, ESTOICO, "Volver a empezar con calma es más útil que castigarte por haber caído."),
        reen(RECAIDA, ESTOICO, "El progreso real se mide en el conjunto del camino, no en un solo tropiezo."),

        reen(RECAIDA, CATOLICO, "Puedes empezar de nuevo hoy mismo; la misericordia no tiene límite de intentos."),
        reen(RECAIDA, CATOLICO, "Lo que hiciste no te aleja del amor de Dios ni de tu propio valor como persona."),
        reen(RECAIDA, CATOLICO, "Levantarse después de caer es también parte del camino, no su fracaso."),
        reen(RECAIDA, CATOLICO, "Puedes entregar esta caída en oración y volver a empezar con humildad."),
        reen(RECAIDA, CATOLICO, "Nadie camina este proceso sin tropiezos; lo importante es volver a levantarte."),
        reen(RECAIDA, CATOLICO, "Tu dignidad sigue intacta, aunque hoy no hayas sostenido el límite que querías."),

        reen(RECAIDA, PSICOLOGIA, "Las recaídas son parte esperable de cualquier cambio de hábito, no una excepción rara."),
        reen(RECAIDA, PSICOLOGIA, "Lo que importa ahora es qué haces en los minutos siguientes a la recaída, no la recaída en sí."),
        reen(RECAIDA, PSICOLOGIA, "Identificar qué la disparó ayuda a prepararte mejor para la próxima vez."),
        reen(RECAIDA, PSICOLOGIA, "Un tropiezo no borra el aprendizaje acumulado en los días anteriores."),
        reen(RECAIDA, PSICOLOGIA, "La autocrítica dura después de una recaída suele aumentar el riesgo de otra, no reducirlo."),
        reen(RECAIDA, PSICOLOGIA, "Volver a tu plan de cuidado ahora mismo importa más que analizar por qué pasó."),

        acc(RECAIDA, "Detén el contacto ahora, aunque ya haya empezado, y aléjate del móvil unos minutos."),
        acc(RECAIDA, "Anota qué pasó justo antes de la recaída, sin juzgarte, solo para verlo con más claridad."),
        acc(RECAIDA, "Avisa a una persona de confianza de que has recaído y que necesitas apoyo para retomar."),
        acc(RECAIDA, "Vuelve a poner en marcha una sola de tus rutinas de cuidado, aunque sea pequeña."),
        acc(RECAIDA, "Bebe agua, respira despacio unos minutos y decide el siguiente paso con calma."),
        acc(RECAIDA, "Revisa tus límites de contacto cero y ajusta lo que haga falta para sostenerlos mejor."),

        // ================= AUTOCRITICA_RECAIDA =================
        reco(AUTOCRITICA, "Castigarte después de una recaída es otra forma de dolor añadido sobre el que ya sentías."),
        reco(AUTOCRITICA, "Hablarte con dureza no deshace lo ocurrido, solo suma otra carga encima."),
        reco(AUTOCRITICA, "La voz que te juzga ahora no es más sabia que la que sigue intentándolo."),
        reco(AUTOCRITICA, "Sentir vergüenza por haber recaído es común, aunque no ayude a que no vuelva a pasar."),
        reco(AUTOCRITICA, "Ser duro/a contigo mismo/a no es garantía de que la próxima vez sea distinta."),

        reen(AUTOCRITICA, ESTOICO, "Juzgarte con dureza no está en la naturaleza de la sabiduría; el buen juicio corrige sin castigar."),
        reen(AUTOCRITICA, ESTOICO, "Puedes revisar el error con la misma serenidad con la que revisarías el de otra persona."),
        reen(AUTOCRITICA, ESTOICO, "El sabio corrige el rumbo sin flagelarse por el tramo ya recorrido."),
        reen(AUTOCRITICA, ESTOICO, "La autocrítica útil señala qué cambiar; la que solo castiga no construye nada."),
        reen(AUTOCRITICA, ESTOICO, "Puedes ser firme contigo sin dejar de ser justo/a contigo."),
        reen(AUTOCRITICA, ESTOICO, "Tratarte como tratarías a alguien que aprecias también es una forma de disciplina."),

        reen(AUTOCRITICA, CATOLICO, "La misericordia empieza también por cómo te tratas a ti mismo/a después de un error."),
        reen(AUTOCRITICA, CATOLICO, "Dios no te pide perfección, te pide que sigas caminando."),
        reen(AUTOCRITICA, CATOLICO, "Puedes perdonarte este tropiezo con la misma ternura con la que perdonarías a otro."),
        reen(AUTOCRITICA, CATOLICO, "El juicio duro contigo mismo/a no viene de un lugar de amor, sino de miedo."),
        reen(AUTOCRITICA, CATOLICO, "Aceptar tu fragilidad también es parte de aceptar tu humanidad."),
        reen(AUTOCRITICA, CATOLICO, "Puedes ofrecer esta vergüenza en oración y dejarla ahí en lugar de cargarla sola."),

        reen(AUTOCRITICA, PSICOLOGIA, "La autocrítica severa suele aumentar el malestar sin mejorar la conducta futura."),
        reen(AUTOCRITICA, PSICOLOGIA, "Tratarte con la misma amabilidad que a un amigo suele ayudar más que el castigo interno."),
        reen(AUTOCRITICA, PSICOLOGIA, "Separar el hecho de haber recaído del juicio sobre ti mismo/a cambia cómo sigues después."),
        reen(AUTOCRITICA, PSICOLOGIA, "La vergüenza intensa dificulta aprender del error; la autocompasión facilita seguir intentándolo."),
        reen(AUTOCRITICA, PSICOLOGIA, "Un error puntual no define un patrón; conviene mirarlo en el conjunto del proceso."),
        reen(AUTOCRITICA, PSICOLOGIA, "Preguntarte qué necesitas ahora ayuda más que repasar una y otra vez lo que hiciste mal."),

        acc(AUTOCRITICA, "Escribe lo que te estás diciendo a ti mismo/a y léelo como si se lo dijeras a alguien querido."),
        acc(AUTOCRITICA, "Pon una mano en el pecho y repite una frase amable hacia ti, aunque al principio no te la creas del todo."),
        acc(AUTOCRITICA, "Anota una sola cosa que sí hiciste bien hoy, por pequeña que sea."),
        acc(AUTOCRITICA, "Habla con alguien de confianza sobre lo ocurrido, sin adornarlo ni esconderlo."),
        acc(AUTOCRITICA, "Sustituye una frase dura hacia ti por otra más justa y anótala en tu diario."),
        acc(AUTOCRITICA, "Vuelve a una rutina de cuidado pequeña y concreta en los próximos minutos.")
    )
}
