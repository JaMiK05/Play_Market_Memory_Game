package uz.gita.play_market_memory_game.data

enum class LevelEnum(val verCount: Int, val horCount: Int, val time: Int) {
    EASY(4, 3, 100),
    MEDIUM(6, 4, 165),
    HARD(8, 6, 240)
}