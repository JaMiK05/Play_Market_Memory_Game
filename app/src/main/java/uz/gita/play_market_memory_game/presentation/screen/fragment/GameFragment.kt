package uz.gita.play_market_memory_game.presentation.screen.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.play_market_memory_game.R
import uz.gita.play_market_memory_game.data.CardData
import uz.gita.play_market_memory_game.data.LevelEnum
import uz.gita.play_market_memory_game.databinding.FragmentGameBinding
import uz.gita.play_market_memory_game.presentation.dialog.Shop_dialog
import uz.gita.play_market_memory_game.presentation.dialog.Win_Dialog
import uz.gita.play_market_memory_game.repository.AppRepository

class GameFragment : Fragment(R.layout.fragment_game) {

    private val binding by viewBinding(FragmentGameBinding::bind)
    private var defLevel = LevelEnum.EASY
    private val args by navArgs<GameFragmentArgs>()
    private val repository: AppRepository by lazy { AppRepository.getIntance() }
    private var _height = 0
    private var job: Job? = null
    private var job1: Job? = null
    private var job3: Job? = null
    private var _width = 0
    private var count = 0
    private var mins = 1
    private var shott = 0
    private var timer: Int = 120
    private var coin = 0
    private val images = ArrayList<ImageView>()
    private var first: ImageView? = null
    private var second: ImageView? = null
    private var dial: Shop_dialog? = null
    private val sharedprev: SharedPreferences by lazy {
        requireContext().getSharedPreferences("local", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        defLevel = args.level
        requireActivity().apply {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        coin = sharedprev.getInt("coin", 100)

        binding.apply {
            space.post {
                _height = binding.container.height / defLevel.verCount
                _width = binding.container.width / defLevel.horCount
                count = defLevel.horCount * defLevel.verCount
                newGame()
            }

            shop.setOnClickListener {

                dial = Shop_dialog(requireContext(), coin)
                mins = 0
                dial?.apply {
                    setSee {
                        if (coin < 100) {
                            Snackbar.make(
                                binding.root,
                                "you don't have enough money",
                                Snackbar.LENGTH_SHORT
                            ).setBackgroundTint(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.teal_700
                                )
                            ).show()
                            dismiss()
                            return@setSee
                        }
                        coin -= 100
                        shop.isEnabled = false
                        pause.isEnabled = false
                        toSee()
                        mins = 1
                        dismiss()
                    }
                    setPlusTime {
                        if (coin < 80) {
                            Snackbar.make(
                                binding.root,
                                "you don't have enough money",
                                Snackbar.LENGTH_SHORT
                            ).setBackgroundTint(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.teal_700
                                )
                            ).show()
                            dismiss()
                            return@setPlusTime
                        }
                        coin -= 80
                        timer += 60
                        mins = 1
                        dismiss()
                    }
                    setMinsCard {
                        if (coin < 60) {
                            Snackbar.make(
                                binding.root,
                                "you don't have enough money",
                                Snackbar.LENGTH_SHORT
                            ).setBackgroundTint(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.teal_700
                                )
                            ).show()
                            dismiss()
                            return@setMinsCard
                        }
                        coin -= 60
                        mins = 1
                        minusToCards()
                        dismiss()
                    }
                    setExit {
                        mins = 1
                        dismiss()
                    }
                    show()
                }

            }

            pause.setOnClickListener {

                mins = 0
                val dial = Win_Dialog(requireContext(), "pause", 0)

                dial.setHome {
                    findNavController().navigateUp()
                    mins = 1
                    dial.dismiss()
                }
                dial.setContin {
                    mins = 1
                    dial.dismiss()
                }
                dial.show()


            }
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                job1?.cancel()
                job?.cancel()
                job3?.cancel()
                findNavController().navigateUp()

            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    override fun onDestroy() {
        super.onDestroy()
        dial?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        sharedprev.edit().putInt("coin", coin).apply()
    }

    private fun minusToCards() {
        if (images.size < 2) return
        val one = images[0]
        one.isEnabled = false
        var two = images[0]
        val teg1 = images[0].tag as CardData
        for (i in 1 until images.size) {
            val teg2 = images[i].tag as CardData
            if (teg1.imgRes == teg2.imgRes) {
                two = images[i]
                break
            }
        }

        two.isEnabled = false
        one.apply {
            animate().setDuration(500).rotationY(89f).withEndAction {
                setImageResource(teg1.imgRes)
                rotationY = -89f
                animate().setDuration(500).rotationY(0f).withEndAction {
                    images.remove(one)
                    deleteImage(one)
                }.start()
            }.start()
        }
        two.apply {
            animate().setDuration(500).rotationY(89f).withEndAction {
                setImageResource(teg1.imgRes)
                rotationY = -89f
                animate().setDuration(500).rotationY(0f).withEndAction {
                    images.remove(two)
                    deleteImage(two)
                }.start()
            }.start()
        }
    }

    private fun newGame() {
        binding.apply {
            shop.isEnabled = false
            pause.isEnabled = false
            shott++
            shot.text = "$shott"
        }
        timer = defLevel.time
        binding.countdown.text = "$timer"
        val kras = timer / 5
        val jyolti = timer / 3
        binding.progressBar.apply {
            val process = this
            process.progressDrawable.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.teal_200
                )
            )
            val animation = ObjectAnimator.ofInt(process, "progress", 0, 100)
            animation.duration = 3800L
            animation.interpolator = DecelerateInterpolator()
            animation.start()
        }
        deleteAllItems()
        val ls = repository.getData(count)
        describeCardData(ls)
        if (job != null) job?.cancel()
        job = lifecycleScope.launch {

            delay(4000)
            binding.apply {
                progressBar.max = timer
                while (timer >= 0) {
                    if (timer > jyolti) {
                        progressBar.progressDrawable?.setTint(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.teal_200
                            )
                        )
                    } else if (timer > kras) {
                        progressBar.progressDrawable?.setTint(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.holo_orange_light
                            )
                        )
                    } else {
                        progressBar.progressDrawable?.setTint(
                            ContextCompat.getColor(
                                requireContext(),
                                android.R.color.holo_red_dark
                            )
                        )
                    }
                    progressBar.progress = timer
                    countdown.text = "$timer"
                    delay(1000)
                    timer -= mins
                }
                val dial = Win_Dialog(requireContext(), "lose", 0)
                dial.setHome {
                    findNavController().navigateUp()
                    dial.dismiss()
                }
                dial.setrest {
                    shott = 0
                    newGame()
                    dial.dismiss()
                }
                dial.show()
            }
        }
    }

    private fun deleteAllItems() {
        binding.apply {
            val counter = container.childCount
            for (i in 0 until counter) {
                container.removeView(container.getChildAt(0))
            }

        }
    }

    private fun describeCardData(ls: List<CardData>) {
        for (i in 0 until defLevel.horCount) {
            for (j in 0 until defLevel.verCount) {
                val image = ImageView(requireContext())
                binding.container.addView(image)
                val lp = image.layoutParams as ConstraintLayout.LayoutParams
                lp.apply {
                    width = (_width)
                    height = (_height)
                    setMargins(10, 10, 10, 10)
                }

                image.layoutParams = lp
                image.tag = ls[i * defLevel.verCount + j]
                image.setImageResource(R.drawable.background)

                image.animate().x(i * _width * 1f).y(j * _height * 1f).setDuration(1000)
                    .withEndAction {
                        toSee()
                    }.start()
                images.add(image)
            }
        }
    }

    private fun toSee() {
        images.forEach { image ->
            image.apply {
                animate().setDuration(500).rotationY(89f).withEndAction {
                    val data = image.tag as CardData
                    setImageResource(data.imgRes)
                    rotationY = -89f
                    animate().setDuration(500).rotationY(0f).withEndAction {
                        job1 = lifecycleScope.launch {
                            delay(1500L)
                            animate().setDuration(500).rotationY(-89f).withEndAction {
                                setImageResource(R.drawable.background)
                                rotationY = 89f
                                animate().setDuration(500).rotationY(0f).withEndAction {
                                    addClickListener()
                                    binding.apply {
                                        pause.isEnabled = true
                                        shop.isEnabled = true
                                    }
                                }.start()
                            }.start()
                        }
                    }.start()
                }.start()
            }
        }
    }

    private fun addClickListener() {
        images.forEach { imageView ->
            imageView.setOnClickListener {
                if (first == null) {
                    first = imageView
                    first?.apply {
                        animate().setDuration(500).rotationY(89f).withEndAction {
                            val data = it.tag as CardData
                            setImageResource(data.imgRes)
                            rotationY = -89f
                            animate().setDuration(500).rotationY(0f).withEndAction {}.start()
                        }.start()
                    }
                } else if (second == null) {
                    if (first == imageView) {
                        return@setOnClickListener
                    }
                    second = imageView
                    second?.apply {
                        animate().setDuration(500).rotationY(89f).withEndAction {
                            val data = it.tag as CardData
                            setImageResource(data.imgRes)
                            rotationY = -89f
                            animate().setDuration(500).rotationY(0f).withEndAction {}.start()
                        }.start()
                    }

                    Handler(Looper.myLooper()!!).postDelayed({
                        val card1 = first?.tag as CardData
                        val card2 = second?.tag as CardData
                        if (card1.id == card2.id) {
                            deleteImage(first!!)
                            deleteImage(second!!)
                        } else {
                            first?.apply {
                                animate().setDuration(300).rotationY(-89f).withEndAction {
                                    setImageResource(R.drawable.background)
                                    rotationY = 89f
                                    animate().setDuration(500).rotationY(0f).withEndAction {
                                        first = null
                                    }.start()
                                }.start()
                            }
                            second?.apply {
                                animate().setDuration(300).rotationY(-89f).withEndAction {
                                    setImageResource(R.drawable.background)
                                    rotationY = 89f
                                    animate().setDuration(500).rotationY(0f).withEndAction {
                                        second = null
                                    }.start()
                                }.start()
                            }
                        }
                    }, 1500)

                } else {
                }
            }
        }
    }

    private fun deleteImage(img: ImageView) {
        job3 = lifecycleScope.launch {
            img.apply {

                if (img == first) first = null
                else if (img == second) second = null

                animate().setDuration(500).scaleX(0f).scaleY(0f).rotation(360f).withEndAction {
                    binding.container.removeView(img)
                    if (images.contains(img)) images.remove(img)
                    fin()
                }.start()
            }
        }
    }

    private fun fin() {
        if (binding.container.childCount == 0) {
            var coinl = 0
            when (args.level) {
                LevelEnum.EASY -> {
                    coinl = 15
                }

                LevelEnum.MEDIUM -> {
                    coinl = 25
                }

                LevelEnum.HARD -> {
                    coinl = 50
                }
            }
            coin += coinl
            val dial = Win_Dialog(requireContext(), "win", coinl)

            dial.setHome {
                findNavController().navigateUp()
                mins = 1
                dial.dismiss()
            }
            dial.setnext {
                newGame()
                mins = 1
                dial.dismiss()
            }
            mins = 0
            dial.show()

        }
    }

}