package uz.gita.play_market_memory_game.presentation.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import uz.gita.play_market_memory_game.R
import uz.gita.play_market_memory_game.databinding.DialogWinBinding

class Win_Dialog(context: Context, val str: String, val co: Int) : Dialog(context) {

    private var home1: (() -> Unit)? = null
    private var contin: (() -> Unit)? = null
    private var rest: (() -> Unit)? = null
    private var nex: (() -> Unit)? = null

    private var _binding: DialogWinBinding? = null
    private val binding get() = _binding!!


    fun setHome(block: (() -> Unit)) {
        home1 = block
    }

    fun setrest(block: (() -> Unit)) {
        rest = block
    }

    fun setnext(block: (() -> Unit)) {
        nex = block
    }

    fun setContin(block: (() -> Unit)) {
        contin = block
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DialogWinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        binding.apply {

            home.setOnClickListener {
                home1?.invoke()
            }

            continueGame.setOnClickListener {
                contin?.invoke()
            }

            restart.setOnClickListener {
                rest?.invoke()
            }

            nextGame.setOnClickListener {
                nex?.invoke()
            }

            when (str) {

                "win" -> {
                    text.setImageResource(R.drawable.you_won_the_game)
                    continueGame.visibility = View.GONE
                    restart.visibility = View.GONE
                    nextGame.visibility = View.VISIBLE

                }
                "lose" -> {
                    text.setImageResource(R.drawable.you_lost_in_the_game)
                    restart.visibility = View.VISIBLE
                    continueGame.visibility = View.GONE
                    nextGame.visibility = View.GONE
                }

                "pause" -> {
                    text.setImageResource(R.drawable.will_you_continue_the_game)
                    continueGame.visibility = View.VISIBLE
                    restart.visibility = View.GONE
                    nextGame.visibility = View.GONE
                }

            }
            when (co) {
                0 -> {
                    coinPl.visibility = View.GONE
                }

                15 -> {
                    coinPl.apply {
                        visibility = View.VISIBLE
                        text = "+$co $"
                    }
                }

                25 -> {
                    coinPl.apply {
                        visibility = View.VISIBLE
                        text = "+$co $"
                    }
                }

                50 -> {
                    coinPl.apply {
                        visibility = View.VISIBLE
                        text = "+$co $"
                    }
                }

            }

        }

    }

}