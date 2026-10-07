package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo MENTE (D3, parte 1 de 3): RUMIACION_BUCLE, RUMIACION_NOCTURNA, NOSTALGIA_IDEALIZACION, DUDA_HABER_TERMINADO.
 */
object ContentDatabaseMente1 {

    private val BUCLE = setOf(ClinicalCategory.RUMIACION_BUCLE)
    private val NOCTURNA = setOf(ClinicalCategory.RUMIACION_NOCTURNA)
    private val NOSTALGIA = setOf(ClinicalCategory.NOSTALGIA_IDEALIZACION)
    private val DUDA = setOf(ClinicalCategory.DUDA_HABER_TERMINADO)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val NOCHE = AtomContext(timeOfDay = TimeOfDay.NOCHE)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String, ctx: AtomContext = AtomContext()) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw, context = ctx)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= RUMIACION_BUCLE =================
        reco(BUCLE, "El mismo pensamiento vuelve una y otra vez, como si repetirlo pudiera resolver algo."),
        reco(BUCLE, "Darle vueltas parece ayudar a entender, pero muchas veces solo desgasta sin aclarar nada."),
        reco(BUCLE, "La mente insiste en revisar lo mismo, aunque ya no queden datos nuevos que encontrar."),
        reco(BUCLE, "Pensar en bucle no es lo mismo que reflexionar; el bucle no llega a ningún sitio nuevo."),
        reco(BUCLE, "Notar que estás en un bucle ya es un paso para poder salir de él."),

        reen(BUCLE, ESTOICO, "Pensar una y otra vez lo mismo no cambia los hechos; solo consume tu energía presente."),
        reen(BUCLE, ESTOICO, "No depende de ti que el pensamiento aparezca; sí depende de ti cuánto tiempo le dedicas."),
        reen(BUCLE, ESTOICO, "Puedes notar el pensamiento, nombrarlo como rumiación, y volver a lo que sí depende de ti."),
        reen(BUCLE, ESTOICO, "Dar vueltas a lo mismo no es sabiduría, es repetición sin dirección."),
        reen(BUCLE, ESTOICO, "Interrumpir el bucle con una acción concreta es un ejercicio de dominio propio."),
        reen(BUCLE, ESTOICO, "El pensamiento insistente pierde fuerza cuando dejas de alimentarlo con atención."),

        reen(BUCLE, CATOLICO, "No hace falta resolverlo todo en tu cabeza ahora; puedes entregar este pensamiento y descansar un poco."),
        reen(BUCLE, CATOLICO, "La mente que da vueltas también necesita silencio para encontrar paz."),
        reen(BUCLE, CATOLICO, "Puedes ofrecer este pensamiento repetido en lugar de cargarlo tú solo/a."),
        reen(BUCLE, CATOLICO, "No todo pensamiento merece tu atención completa; puedes dejarlo pasar con calma."),
        reen(BUCLE, CATOLICO, "Confiar no significa dejar de pensar, sino no quedarte atrapado/a en lo mismo."),
        reen(BUCLE, CATOLICO, "Un momento de silencio puede aquietar lo que las vueltas en la cabeza no logran resolver."),

        reen(BUCLE, PSICOLOGIA, "La rumiación repite el mismo contenido sin generar ninguna solución nueva."),
        reen(BUCLE, PSICOLOGIA, "Cambiar de actividad suele cortar el bucle mejor que intentar pensarlo hasta el final."),
        reen(BUCLE, PSICOLOGIA, "Escribir el pensamiento en vez de darle vueltas en la cabeza ayuda a soltarlo un poco."),
        reen(BUCLE, PSICOLOGIA, "El cerebro confunde repetir con resolver; rara vez son lo mismo."),
        reen(BUCLE, PSICOLOGIA, "Ponerle un límite de tiempo al pensamiento, por ejemplo cinco minutos, ayuda a no quedarte atrapado/a."),
        reen(BUCLE, PSICOLOGIA, "Notar el bucle sin juzgarte por tenerlo es el primer paso para interrumpirlo."),

        acc(BUCLE, "Escribe el pensamiento una sola vez en una nota y ciérrala."),
        acc(BUCLE, "Cambia de actividad ahora: mueve el cuerpo, sal a caminar o llama a alguien."),
        acc(BUCLE, "Ponte un límite de cinco minutos para pensarlo y, al acabar, pasa a otra cosa."),
        acc(BUCLE, "Escucha música o un podcast que ocupe tu atención unos minutos."),
        acc(BUCLE, "Haz una tarea sencilla con las manos: ordenar, cocinar o doblar ropa."),
        acc(BUCLE, "Anota tres cosas que puedes ver, oír y sentir ahora mismo para volver al presente."),

        // ================= RUMIACION_NOCTURNA =================
        reco(NOCTURNA, "La cabeza se pone más ruidosa justo cuando el cuerpo busca descansar."),
        reco(NOCTURNA, "Por la noche, sin distracciones alrededor, los pensamientos parecen más grandes de lo que son de día."),
        reco(NOCTURNA, "Dar vueltas de noche es común cuando el día no dejó espacio para procesar lo vivido."),
        reco(NOCTURNA, "El silencio nocturno no crea el pensamiento, solo le quita ruido alrededor."),
        reco(NOCTURNA, "No es que pienses peor de noche, es que hay menos con qué distraerte."),

        reen(NOCTURNA, ESTOICO, "No depende de ti que la mente se active de noche; sí depende de ti qué haces con ese pensamiento.", NOCHE),
        reen(NOCTURNA, ESTOICO, "Una idea que a la luz del día pierde fuerza no merece decidir tu descanso esta noche.", NOCHE),
        reen(NOCTURNA, ESTOICO, "Puedes posponer el análisis para mañana sin que eso signifique evitarlo para siempre."),
        reen(NOCTURNA, ESTOICO, "El pensamiento nocturno suele exagerar; conviene no darle más peso del que tiene."),
        reen(NOCTURNA, ESTOICO, "Aceptar que esta noche cuesta más no es rendirse, es realismo sereno."),
        reen(NOCTURNA, ESTOICO, "Mantener una rutina calmada antes de dormir está en tu mano, aunque el pensamiento insista."),

        reen(NOCTURNA, CATOLICO, "Puedes entregar en oración lo que la noche trae a la mente y descansar un poco más tranquilo/a.", NOCHE),
        reen(NOCTURNA, CATOLICO, "La noche también puede ser tiempo de confianza, no solo de desvelo.", NOCHE),
        reen(NOCTURNA, CATOLICO, "No hace falta resolverlo todo a estas horas; mañana habrá más luz para pensarlo."),
        reen(NOCTURNA, CATOLICO, "Un pensamiento repetido de noche también puede convertirse en una pequeña oración de confianza."),
        reen(NOCTURNA, CATOLICO, "Descansar el cuerpo, aunque la mente insista, también es un acto de fe en que mañana será otro día."),
        reen(NOCTURNA, CATOLICO, "Puedes pedir paz para esta noche, sin exigirte tener todas las respuestas ya."),

        reen(NOCTURNA, PSICOLOGIA, "Por la noche baja la actividad que normalmente distrae, y los pensamientos ganan protagonismo.", NOCHE),
        reen(NOCTURNA, PSICOLOGIA, "Escribir el pensamiento antes de dormir ayuda a la mente a soltarlo un poco.", NOCHE),
        reen(NOCTURNA, PSICOLOGIA, "Intentar resolverlo a estas horas suele generar más alerta, no menos."),
        reen(NOCTURNA, PSICOLOGIA, "Una rutina simple antes de dormir le indica al cuerpo que puede bajar la actividad mental."),
        reen(NOCTURNA, PSICOLOGIA, "Posponer el pensamiento a un horario fijo del día siguiente reduce su presencia nocturna."),
        reen(NOCTURNA, PSICOLOGIA, "El cansancio acumulado hace más difícil regular estos pensamientos; dormir, aunque cueste, ayuda."),

        acc(NOCTURNA, "Escribe el pensamiento en una nota junto a la cama y dile que lo verás mañana."),
        acc(NOCTURNA, "Apaga las pantallas y baja la luz al menos veinte minutos antes de dormir."),
        acc(NOCTURNA, "Respira despacio contando hasta cuatro al inhalar y hasta seis al soltar, varias veces."),
        acc(NOCTURNA, "Si llevas mucho rato despierto/a dándole vueltas, levántate un momento y vuelve después."),
        acc(NOCTURNA, "Escucha algo suave y repetitivo en lugar de seguir pensando en lo mismo."),
        acc(NOCTURNA, "Pon una alarma mental o escrita para retomar ese pensamiento mañana a una hora fija."),

        // ================= NOSTALGIA_IDEALIZACION =================
        reco(NOSTALGIA, "La memoria tiende a guardar lo bonito y desdibujar lo que costaba."),
        reco(NOSTALGIA, "Recordar solo los buenos momentos es humano, aunque no cuente la historia completa."),
        reco(NOSTALGIA, "Extrañar algo no significa que ese algo fuera bueno para ti en su conjunto."),
        reco(NOSTALGIA, "La nostalgia suaviza los bordes de lo que en su momento también dolía."),
        reco(NOSTALGIA, "Sentir añoranza no borra las razones que te llevaron a donde estás ahora."),

        reen(NOSTALGIA, ESTOICO, "Un recuerdo idealizado es un juicio parcial; puedes examinarlo junto a lo que también costaba."),
        reen(NOSTALGIA, ESTOICO, "La memoria no es un hecho fijo, es una interpretación que puedes revisar con más calma."),
        reen(NOSTALGIA, ESTOICO, "Añorar un momento no obliga a que ese momento vuelva ni a que fuera mejor de lo que fue."),
        reen(NOSTALGIA, ESTOICO, "Puedes reconocer lo bueno sin olvidar por qué el conjunto no te sostenía."),
        reen(NOSTALGIA, ESTOICO, "Mirar el pasado con precisión, no solo con nostalgia, es un ejercicio de buen juicio."),
        reen(NOSTALGIA, ESTOICO, "El recuerdo agradable no cambia lo que hoy sí depende de ti construir."),

        reen(NOSTALGIA, CATOLICO, "Puedes agradecer lo bueno vivido sin que eso te llame a volver a un camino que ya cerraste."),
        reen(NOSTALGIA, CATOLICO, "La nostalgia no es pecado, pero conviene mirarla con verdad, no solo con añoranza."),
        reen(NOSTALGIA, CATOLICO, "Confiar en el presente no borra lo bueno del pasado, solo te ayuda a no quedarte atrapado/a en él."),
        reen(NOSTALGIA, CATOLICO, "Puedes pedir claridad para distinguir el recuerdo bonito de la realidad completa que viviste."),
        reen(NOSTALGIA, CATOLICO, "El corazón que añora también puede aprender a agradecer y seguir camino."),
        reen(NOSTALGIA, CATOLICO, "No hace falta que el pasado fuera perfecto para haber tenido momentos que valió la pena vivir."),

        reen(NOSTALGIA, PSICOLOGIA, "La memoria selectiva tiende a reforzar los recuerdos positivos y atenuar los negativos con el tiempo."),
        reen(NOSTALGIA, PSICOLOGIA, "Contrastar el recuerdo idealizado con hechos concretos ayuda a equilibrar la imagen completa."),
        reen(NOSTALGIA, PSICOLOGIA, "La nostalgia suele intensificarse en momentos de soledad o cansancio, no porque el pasado fuera mejor."),
        reen(NOSTALGIA, PSICOLOGIA, "Registrar también lo difícil de la relación ayuda a que la memoria no quede solo con lo bonito."),
        reen(NOSTALGIA, PSICOLOGIA, "Sentir añoranza es una emoción válida; no tiene por qué convertirse en una decisión."),
        reen(NOSTALGIA, PSICOLOGIA, "Reconocer el sesgo de la memoria no invalida lo que sentiste, solo lo ordena mejor."),

        acc(NOSTALGIA, "Escribe una lista con momentos difíciles reales de la relación, no solo los buenos."),
        acc(NOSTALGIA, "Relee, si los tienes, tus propios apuntes de por qué decidiste terminar o distanciarte."),
        acc(NOSTALGIA, "Haz algo que te conecte con tu vida actual, distinto de recordar el pasado."),
        acc(NOSTALGIA, "Anota qué necesitas hoy que ese recuerdo no te puede dar."),
        acc(NOSTALGIA, "Habla con alguien de confianza que conociera también la parte difícil de esa relación."),
        acc(NOSTALGIA, "Dedica diez minutos a una actividad nueva que no tenga relación con ese recuerdo."),

        // ================= DUDA_HABER_TERMINADO =================
        reco(DUDA, "Dudar de una decisión difícil es habitual, incluso cuando fue la decisión correcta."),
        reco(DUDA, "Preguntarte si hiciste bien no significa que hayas hecho mal."),
        reco(DUDA, "La duda suele aparecer más fuerte en los días difíciles, no porque la decisión cambie."),
        reco(DUDA, "Terminar algo importante casi siempre deja preguntas abiertas por un tiempo."),
        reco(DUDA, "Sentir incertidumbre sobre lo hecho es parte del proceso, no una señal de error."),

        reen(DUDA, ESTOICO, "La decisión ya está tomada; lo que hoy depende de ti es cómo la sostienes, no si te arrepientes."),
        reen(DUDA, ESTOICO, "Dudar con calma es examinar; dudar con angustia es solo repetir un juicio sin nueva información."),
        reen(DUDA, ESTOICO, "Puedes revisar tus razones sin necesidad de deshacer lo decidido."),
        reen(DUDA, ESTOICO, "Una decisión tomada con buen juicio en su momento sigue siendo válida, aunque hoy dudes."),
        reen(DUDA, ESTOICO, "El malestar de la duda no es prueba de haberte equivocado, es parte del cambio."),
        reen(DUDA, ESTOICO, "Sostener el rumbo con calma, incluso con dudas, es también una forma de firmeza."),

        reen(DUDA, CATOLICO, "Puedes pedir claridad sin exigirte tener toda la certeza ahora mismo."),
        reen(DUDA, CATOLICO, "La duda no significa que actuaste mal; a veces es parte de aprender a confiar en el propio discernimiento."),
        reen(DUDA, CATOLICO, "Confiar no es no dudar nunca, es seguir caminando aun con preguntas abiertas."),
        reen(DUDA, CATOLICO, "Puedes entregar esta incertidumbre en oración y descansar de cargarla tú solo/a."),
        reen(DUDA, CATOLICO, "Una decisión tomada con conciencia sigue mereciendo tu confianza, aunque hoy tiemble un poco."),
        reen(DUDA, CATOLICO, "El tiempo y la oración suelen traer más paz que insistir en resolver la duda de golpe."),

        reen(DUDA, PSICOLOGIA, "La duda después de una decisión grande es un fenómeno psicológico común, no una señal de error."),
        reen(DUDA, PSICOLOGIA, "Cuanto más grande la decisión, más intensa suele ser la duda que la acompaña al principio."),
        reen(DUDA, PSICOLOGIA, "Revisar los motivos concretos que te llevaron a decidir ayuda a calmar la duda del momento."),
        reen(DUDA, PSICOLOGIA, "La incertidumbre es incómoda, pero no es lo mismo que estar equivocado/a."),
        reen(DUDA, PSICOLOGIA, "Con el tiempo, la intensidad de la duda suele bajar aunque la decisión no cambie."),
        reen(DUDA, PSICOLOGIA, "Diferenciar el miedo al cambio de una señal real de haberte equivocado ayuda a ver con más claridad."),

        acc(DUDA, "Escribe las tres razones principales que te llevaron a tomar esa decisión."),
        acc(DUDA, "Habla con alguien de confianza que conociera bien la situación cuando decidiste."),
        acc(DUDA, "Anota qué necesitarías ver para sentir más certeza, y si eso depende realmente de ti."),
        acc(DUDA, "Dedica un rato a una actividad que te aleje de pensarlo, y vuelve a ello más tarde con calma."),
        acc(DUDA, "Escribe en tu diario qué sentías justo antes de decidir, para recordar el contexto completo."),
        acc(DUDA, "Date permiso para dudar sin exigirte una respuesta definitiva hoy mismo.")
    )
}
