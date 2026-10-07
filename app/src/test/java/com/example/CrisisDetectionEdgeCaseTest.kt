package com.example

import com.example.ai.SoltarAiEngine
import org.junit.Assert.*
import org.junit.Test

class CrisisDetectionEdgeCaseTest {

    // ---- DEBE detectar: frases de ideación suicida, autolesión, despedida y desesperanza extrema ----

    @Test
    fun `detecta frases positivas requeridas de crisis y desesperanza`() {
        val positiveCases = listOf(
            "me quiero matar",
            "me voy a matar",
            "me voy a suicidar",
            "quiero acabar con mi vida",
            "no quiero seguir viviendo",
            "ojala no existiera",
            "ya no quiero estar aquí",
            "estaría mejor sin mí",
            "me quiero morir",
            "voy a quitarme la vida",
            "¿para qué seguir?",
            "irme para siempre",
            "ya no puedo más con esta vida",
            "quiero acabar de una vez",
            "no encuentro salida"
        )
        positiveCases.forEach { phrase ->
            assertTrue("Debe detectar como crisis: '$phrase'", SoltarAiEngine.checkSelfHarmTrigger(phrase))
        }
    }

    @Test
    fun `detecta frase explicita de ideacion suicida`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("ya no quiero vivir, quiero morir"))
    }

    @Test
    fun `detecta frase explicita de autolesion`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("tengo ganas de cortarme otra vez"))
    }

    @Test
    fun `detecta frase en mayusculas`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("NO QUIERO VIVIR MAS"))
    }

    @Test
    fun `detecta frase en mayusculas con tildes`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("NO QUIERO VIVIR MÁS"))
    }

    @Test
    fun `detecta frase clave rodeada de otro texto`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("hoy fue un dia horrible y la verdad es que quiero morir, no aguanto mas"))
    }

    @Test
    fun `detecta no despertar y ojala me muriera con y sin tildes`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("ojalá no despertara mañana"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("ojala no despertara"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("ojala me muriera hoy"))
    }

    @Test
    fun `detecta expresiones de no tener sentido o no valer la pena la vida`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("mi vida no vale nada"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("no vale la pena seguir viviendo"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("la vida no tiene sentido para mí"))
    }

    @Test
    fun `detecta deseos de desaparecer o dejar de existir`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("solo quiero desaparecer para siempre"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("quiero dejar de existir"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("quitarme la vida es lo único en lo que pienso"))
    }

    @Test
    fun `detecta despedidas de tono definitivo`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("adiós a todos para siempre"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("adios mundo cruel"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("esta es mi despedida final"))
    }

    @Test
    fun `detecta intencion indirecta de no estar en este mundo`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("ya no quiero estar en este mundo, seria mas facil para todos si yo no estuviera"))
    }

    @Test
    fun `detecta variantes de autolesion con lastimarme o hacerme dano`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("tengo ganas de lastimarme para sentir algo distinto al vacio"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("quiero hacerme daño físico"))
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("pensé en clavarme algo"))
    }

    // ---- NO debe detectar: dolor emocional normal del duelo, expresiones coloquiales y quejas sin riesgo ----

    @Test
    fun `NO detecta frases negativas requeridas`() {
        val negativeCases = listOf(
            "quiero matar el tiempo",
            "no puedo matar la ansiedad",
            "tengo un examen mañana",
            "hola, ¿cómo estás?"
        )
        negativeCases.forEach { phrase ->
            assertFalse("NO debe disparar falso positivo en: '$phrase'", SoltarAiEngine.checkSelfHarmTrigger(phrase))
        }
    }

    @Test
    fun `NO detecta tristeza normal del duelo`() {
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("estoy muy triste, lo extraño mucho y me cuesta dormir"))
    }

    @Test
    fun `NO detecta frustracion sin referencia a la propia vida`() {
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("no puedo mas con esta situacion, estoy agotada de tanto pensar en el"))
    }

    @Test
    fun `NO detecta enfado hacia la ex pareja`() {
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("lo odio, ojala le vaya mal, no se merece nada bueno"))
    }

    @Test
    fun `NO detecta expresiones coloquiales figuradas`() {
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("estoy muerto de cansancio"))
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("me muero de risa con lo que pasó"))
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("mi ex me mató por dentro"))
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("este trabajo me está matando"))
    }
}
