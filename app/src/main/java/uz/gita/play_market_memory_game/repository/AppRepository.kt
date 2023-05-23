package uz.gita.play_market_memory_game.repository

import uz.gita.play_market_memory_game.R
import uz.gita.play_market_memory_game.data.CardData

class AppRepository private constructor() {


    companion object {
        private lateinit var instance: AppRepository
        fun getIntance(): AppRepository {
            if (!(::instance.isInitialized)) {
                instance = AppRepository()
            }
            return instance
        }
    }


    // singleton
    private val cardList = ArrayList<CardData>()

    init {
        cardList.add(CardData(R.drawable.img1, 1))
        cardList.add(CardData(R.drawable.img2, 2))
        cardList.add(CardData(R.drawable.img3, 3))
        cardList.add(CardData(R.drawable.img4, 4))
        cardList.add(CardData(R.drawable.img5, 5))
        cardList.add(CardData(R.drawable.img6, 6))
        cardList.add(CardData(R.drawable.img7, 7))
        cardList.add(CardData(R.drawable.img8, 8))
        cardList.add(CardData(R.drawable.img9, 9))
        cardList.add(CardData(R.drawable.img10, 10))
        cardList.add(CardData(R.drawable.img11, 11))
        cardList.add(CardData(R.drawable.img12, 12))
        cardList.add(CardData(R.drawable.img13, 13))
        cardList.add(CardData(R.drawable.img14, 14))
        cardList.add(CardData(R.drawable.img15, 15))
        cardList.add(CardData(R.drawable.img16, 16))
        cardList.add(CardData(R.drawable.img17, 17))
        cardList.add(CardData(R.drawable.img18, 18))
        cardList.add(CardData(R.drawable.img19, 19))
        cardList.add(CardData(R.drawable.img20, 20))
        cardList.add(CardData(R.drawable.img21, 21))
        cardList.add(CardData(R.drawable.img22, 22))
        cardList.add(CardData(R.drawable.img10, 10))
        cardList.add(CardData(R.drawable.img22, 22))
    }

    fun getData(count: Int): List<CardData> {
        cardList.shuffle()
        val ls = cardList.subList(0, count / 2)
        val result = ArrayList<CardData>(count)
        result.addAll(ls)
        result.addAll(ls)
        result.shuffle()
        return result
    }
}