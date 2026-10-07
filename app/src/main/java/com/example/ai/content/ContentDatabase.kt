package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Banco de átomos. En la fase D1 solo hay contenido semilla de IMPULSO_CONTACTAR (grupo AHORA_MISMO) para
 * probar el motor; el resto de etiquetas sigue usando las tarjetas completas (LEGACY) hasta las fases D2-D4.
 * Todo texto debe pasar ContentLint (lo comprueba ContentLintTest).
 */
object ContentDatabase {

    private val IMPULSO = setOf(ClinicalCategory.IMPULSO_CONTACTAR)
    private val AHORA = setOf(ContentGroup.AHORA_MISMO)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val REPETIDO = AtomContext(repeated = true)

    private fun reconocimiento(text: String) = atom(Slot.RECONOCIMIENTO, text, tags = IMPULSO)
    private fun reencuadre(fw: Set<SoltarFramework>, text: String) = atom(Slot.REENCUADRE, text, tags = IMPULSO, frameworks = fw)
    private fun accion(text: String) = atom(Slot.ACCION, text, tags = IMPULSO)
    private fun cierre(fw: Set<SoltarFramework>, text: String) = atom(Slot.CIERRE, text, groups = AHORA, frameworks = fw)
    private fun cierreRepetido(text: String) = atom(Slot.CIERRE, text, groups = AHORA, context = REPETIDO)
    private fun cita(fw: Set<SoltarFramework>, text: String, author: String, source: String = "") =
        atom(Slot.CITA, text, groups = AHORA, frameworks = fw, author = author, source = source)

    val atoms: List<ContentAtom> = listOf(
        // ---- RECONOCIMIENTO (neutro: vale para los tres marcos)
        reconocimiento("Las ganas de escribirle llegan como una ola: intensas, pero con principio y final."),
        reconocimiento("Querer escribirle no es un fallo tuyo; es lo que hace un cuerpo acostumbrado a su presencia."),
        reconocimiento("Ese impulso suena urgente, pero urgente no significa que haya que obedecerlo ahora."),
        reconocimiento("Notar la tentación ya es una forma de estar al mando."),
        reconocimiento("Hoy toca sostener un momento difícil, no ganar la batalla entera."),

        // ---- REENCUADRE estoico
        reencuadre(ESTOICO, "Hoy solo depende de ti enviar o no el mensaje; cómo lo reciba o lo interprete él queda fuera de tu mano."),
        reencuadre(ESTOICO, "Antes de actuar, examina el impulso: ¿quieres decirle algo o quieres que el malestar se apague ya?"),
        reencuadre(ESTOICO, "Una impresión fuerte no es un mandato. Puedes mirarla, nombrarla y dejar que pase sin obedecerla."),
        reencuadre(ESTOICO, "No te perturba el silencio de su lado, sino el juicio que haces sobre ese silencio. Ese juicio sí puedes examinarlo."),
        reencuadre(ESTOICO, "Entrenar la calma consiste en dejar que el impulso pase sin permitir que decida por ti."),
        reencuadre(ESTOICO, "Lo que hagas ahora con este deseo forma el carácter que tendrás mañana; elige con calma qué hábito quieres reforzar."),

        // ---- REENCUADRE católico
        reencuadre(CATOLICO, "Tu dignidad no depende de su respuesta. Puedes entregar esta ansiedad en un minuto de silencio antes de decidir nada."),
        reencuadre(CATOLICO, "La paciencia también es una forma de cuidar tu corazón: no todo lo que arde hay que decirlo esta noche."),
        reencuadre(CATOLICO, "Ser misericordioso contigo incluye no exponerte hoy a una herida que todavía está abierta."),
        reencuadre(CATOLICO, "Una pausa de oración no arregla la situación, pero te devuelve la calma necesaria para elegir con libertad."),
        reencuadre(CATOLICO, "Esperar sin forzar también es una forma de amor: puedes ofrecer tu ansiedad y volver a lo que hoy sí está en tus manos."),
        reencuadre(CATOLICO, "Tu valor como persona no se decide en un mensaje. Descansa un momento en esa certeza antes de tocar el teléfono."),

        // ---- REENCUADRE psicología moderna
        reencuadre(PSICOLOGIA, "El impulso funciona como una ola: sube, alcanza su punto más alto y baja aunque no hagas nada."),
        reencuadre(PSICOLOGIA, "Separar el pensamiento de que tienes que escribirle de la orden de hacerlo te devuelve margen para elegir."),
        reencuadre(PSICOLOGIA, "Buscar alivio inmediato refuerza el ciclo; esperar sin actuar debilita poco a poco la fuerza del impulso."),
        reencuadre(PSICOLOGIA, "Escribirle calma un rato, pero suele reabrir la espera de su respuesta. Notar esa diferencia ayuda a decidir con claridad."),
        reencuadre(PSICOLOGIA, "Un pensamiento es un evento mental, no una instrucción. Puedes observarlo pasar mientras sigues con lo que te importa."),
        reencuadre(PSICOLOGIA, "Tu cerebro busca el alivio más rápido que conoce. Elegir otra vía, aunque cueste, entrena una respuesta nueva."),

        // ---- ACCION (neutra)
        accion("Pon un temporizador de diez minutos y sal a caminar sin el móvil."),
        accion("Escribe el mensaje en una nota que nunca vas a enviar y guárdala sin releerla."),
        accion("Lávate la cara con agua fría y respira despacio: cuatro tiempos al inhalar, seis al soltar."),
        accion("Avisa a una persona de confianza de que hoy te cuesta y pídele que te escriba en un rato."),
        accion("Cambia de habitación y haz algo con las manos durante diez minutos: cocinar, ordenar o dibujar."),
        accion("Anota qué esperas sentir si le escribes y qué sentirás si no lo haces esta noche."),

        // ---- CIERRE por marco (grupo AHORA_MISMO)
        cierre(ESTOICO, "¿Qué parte de esto sí depende de ti en la próxima hora?"),
        cierre(ESTOICO, "¿Qué juicio estás añadiendo ahora mismo a los hechos?"),
        cierre(ESTOICO, "Si un amigo sintiera lo mismo, ¿qué le aconsejarías mantener firme?"),
        cierre(ESTOICO, "¿Qué acción pequeña te acerca hoy a la persona que quieres ser?"),
        cierre(ESTOICO, "¿Qué harías si el impulso estuviera ahí, pero no mandara?"),
        cierre(ESTOICO, "¿Qué necesitas hoy para actuar con calma y no con prisa?"),

        cierre(CATOLICO, "¿Qué necesita hoy tu corazón: desahogo, silencio o compañía?"),
        cierre(CATOLICO, "¿Qué puedes entregar esta noche para descansar un poco?"),
        cierre(CATOLICO, "¿Qué gesto de cuidado hacia ti cabe en los próximos minutos?"),
        cierre(CATOLICO, "¿Quién podría acompañarte un rato sin que tengas que explicarlo todo?"),
        cierre(CATOLICO, "¿Qué pequeña esperanza puedes sostener hoy sin exigirle respuestas al futuro?"),
        cierre(CATOLICO, "¿Qué te haría bien agradecer esta noche, aunque sea algo mínimo?"),

        cierre(PSICOLOGIA, "¿Qué harías ahora si el impulso estuviera ahí, pero no mandara?"),
        cierre(PSICOLOGIA, "¿Qué te dirías con la misma amabilidad que le darías a alguien querido?"),
        cierre(PSICOLOGIA, "¿Qué actividad concreta puede ocupar tus manos y tu atención los próximos diez minutos?"),
        cierre(PSICOLOGIA, "¿En qué punto de la ola estás ahora: subiendo, en el pico o bajando?"),
        cierre(PSICOLOGIA, "¿Qué valor tuyo quieres proteger con lo que hagas en esta hora?"),
        cierre(PSICOLOGIA, "¿Qué te ayudó otras veces a pasar por un impulso parecido?"),

        // ---- CIERRE de escalada (solo si la etiqueta se ha elegido 3 o más veces en 7 días)
        cierreRepetido("Si esto vuelve una y otra vez, quizá te ayude una herramienta más firme que una frase: prueba el Modo Impulso."),
        cierreRepetido("Cuando el impulso se repite, un plan escrito de antemano pesa más que la fuerza de voluntad del momento."),
        cierreRepetido("Si te pasa a menudo, contárselo a alguien de confianza o dejarlo en tu diario puede aligerar la carga."),

        // ---- CITA (literales con fuente exacta, o ideas atribuidas a la corriente)
        cita(ESTOICO, "«De las cosas, unas dependen de nosotros y otras no.»", "Epicteto", "Enquiridión, 1"),
        cita(ESTOICO, "«Sufrimos más a menudo en la imaginación que en la realidad.»", "Séneca", "Cartas a Lucilio, 13"),
        cita(ESTOICO, "«No nos perturban las cosas, sino los juicios que hacemos sobre ellas.»", "Epicteto", "Enquiridión, 5"),

        cita(CATOLICO, "«Nos hiciste para ti, y nuestro corazón está inquieto hasta que descanse en ti.»", "Agustín de Hipona", "Confesiones, I, 1"),
        cita(CATOLICO, "«Nada te turbe, nada te espante; la paciencia todo lo alcanza.»", "Teresa de Jesús", "Poesías (Nada te turbe)"),

        cita(PSICOLOGIA, "Un impulso no necesita ser obedecido: sube, llega a su punto máximo y baja.", "Prevención de recaídas: urge surfing (G. A. Marlatt)"),
        cita(PSICOLOGIA, "Un pensamiento es un evento mental, no una orden que debas cumplir.", "Terapia de aceptación y compromiso (ACT)"),
        cita(PSICOLOGIA, "Tratarte con la amabilidad con la que tratarías a alguien querido no es debilidad: es una forma de regularte.", "Autocompasión (Kristin Neff)")
    ) + ContentDatabaseCuerpo.atoms + ContentDatabaseCuerpo.cierres + ContentDatabaseCuerpo.citas + ContentDatabaseAhoraMismo2.atoms + ContentDatabaseAhoraMismo3.atoms + ContentDatabaseAhoraMismoCitas.atoms + ContentDatabaseMente1.atoms + ContentDatabaseMente2.atoms + ContentDatabaseEmociones1.atoms + ContentDatabaseEmociones2.atoms + ContentDatabaseMenteCierresCitas.cierres + ContentDatabaseMenteCierresCitas.citas + ContentDatabaseEmocionesCierresCitas.cierres + ContentDatabaseEmocionesCierresCitas.citas + ContentDatabaseSituaciones1.atoms + ContentDatabaseSituaciones2.atoms + ContentDatabaseAvance.atoms + ContentDatabaseSituacionesCierresCitas.cierres + ContentDatabaseSituacionesCierresCitas.citas + ContentDatabaseAvanceCierresCitas.cierres + ContentDatabaseAvanceCierresCitas.citas
}
