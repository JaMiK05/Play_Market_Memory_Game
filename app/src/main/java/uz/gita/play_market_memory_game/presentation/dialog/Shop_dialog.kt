package uz.gita.play_market_memory_game.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import uz.gita.play_market_memory_game.databinding.ShopDialogBinding

class Shop_dialog(context: Context, val coins: Int) : Dialog(context) {

    private var _binding: ShopDialogBinding? = null
    private val binding get() = _binding!!

    private var se: (() -> Unit)? = null
    private var time: (() -> Unit)? = null
    private var minu: (() -> Unit)? = null
    private var ext: (() -> Unit)? = null

    fun setSee(block: () -> Unit) {
        se = block
    }

    fun setPlusTime(block: () -> Unit) {
        time = block
    }

    fun setMinsCard(block: () -> Unit) {
        minu = block
    }

    fun setExit(block: () -> Unit) {
        ext = block
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ShopDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        binding.apply {
            cointxt.text = "$coins"
            see.setOnClickListener {
                se?.invoke()
            }
            shoptime.setOnClickListener {
                time?.invoke()
            }

            minsTwo.setOnClickListener {
                minu?.invoke()
            }
            exit.setOnClickListener {
                ext?.invoke()
            }

        }

    }

}