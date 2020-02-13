import dino.Dino
import org.junit.Assert.assertEquals
import org.junit.Test

internal class DinoTest {

    @Test
    fun setLogic() {
        val d = Dino("a", "b")
        d.logic = -4
        assertEquals(d.logic, 0)
        d.logic = 11
        assertEquals(d.logic, 10)
        d.logic = 5
        assertEquals(d.logic, 5)
    }
/*
    @Test
    fun json() {
        var d = Dino()
        var json = JSONObject()
        json.put("name", "Torben");
        json.put("color", "#42f48c")
        json.put("logic", 1)
        json.put("courage", 1)
        json.put("weight", 1)
        json.put("agility", 1)
        json.put("strength", 1)
        json.put("size", 1)
        json.put("reactionTime", 1)
        json.put("front", FrontLimb.NORMAL.id)
        json.put("back", BackLimb.NORMAL.id)
        json.put("tail", Tail.NORMAL.id)
        json.put("stealth", false)
        assertEquals(d.json().toString(), json.toString())
    }
*/
}
