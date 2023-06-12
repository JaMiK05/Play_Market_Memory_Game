package uz.gita.play_market_memory_game.presentation.screen.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import uz.gita.play_market_memory_game.R
import uz.gita.play_market_memory_game.data.LevelEnum
import uz.gita.play_market_memory_game.databinding.FragmentLevelBinding

class LevelFragment : Fragment(R.layout.fragment_level) {

    private val binding by viewBinding(FragmentLevelBinding::bind)

    private val heigh = 120f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            addMoney.setOnLongClickListener {
                coinplus()
                true
            }

            easy.apply {
                isEnabled = false
                setOnClickListener {
                    openGameScreen(LevelEnum.EASY)
                }
            }
            medium.apply {
                isEnabled = false
                animate().setDuration(500).y(heigh * 2f).withEndAction {
                    isEnabled = true
                    easy.animate().setDuration(300).translationX(easy.rotationX + 100f)
                        .withEndAction {
                            easy.isEnabled = true
                        }.start()
                }.start()
                setOnClickListener {
                    openGameScreen(LevelEnum.MEDIUM)
                }
            }

            hard.apply {
                isEnabled = false
                animate().setDuration(500).y(heigh * 4).withEndAction {
                    animate().setDuration(300).translationX(rotationX - 100f)
                        .withEndAction {
                            isEnabled = true
                        }.start()
                }.start()
                setOnClickListener {
                    openGameScreen(LevelEnum.HARD)
                }
            }
        }
    }

    private fun coinplus() {
        val shared = requireContext().getSharedPreferences(
            "local",
            Context.MODE_PRIVATE
        )
        val coin = shared.getInt("coin", 100) + 10_000
        shared.edit().putInt("coin", coin).apply()

    }

    private fun openGameScreen(level: LevelEnum) {

        findNavController().navigate(LevelFragmentDirections.actionLevelFragmentToGameFragment(level))

    }

}