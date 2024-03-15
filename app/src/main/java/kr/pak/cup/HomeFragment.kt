    package kr.pak.cup

    import android.os.Bundle
    import android.os.Handler
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.TextView
    import kr.pak.cup.databinding.ActivityMainBinding

    class HomeFragment : Fragment() {

        private var startButtonEnabled = true
        private var stopButtonEnabled = false
        private var resetButtonEnabled = false

        private var timeTextSave = "00:00:000"

        private lateinit var binding: ActivityMainBinding
        private lateinit var rootView: ViewGroup

        private lateinit var timeText: TextView
        private val handler = Handler()
        private var isRunning = false
        private var startTime = 0L
        private var elapsedTime = 0L

        private lateinit var startButton: Button
        private lateinit var stopButton: Button
        private lateinit var resetButton: Button

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = ActivityMainBinding.inflate(layoutInflater)
            rootView = inflater.inflate(R.layout.fragment_home, container, false) as ViewGroup

            timeText = rootView.findViewById<TextView>(R.id.timeText)

            setup()

            return rootView
        }

        private fun setup() {
            startButton = rootView.findViewById<Button>(R.id.startButton)
            stopButton = rootView.findViewById<Button>(R.id.stopButton)
            resetButton = rootView.findViewById<Button>(R.id.resetButton)

            startButton.setOnClickListener {
                isRunning = true
                startTime = System.currentTimeMillis() - elapsedTime
                handler.post(updateTimer)

                stopButton.isEnabled = true
                resetButton.isEnabled = true
                startButton.isEnabled = false
            }

            stopButton.setOnClickListener {
                isRunning = false
                handler.removeCallbacks(updateTimer)

                stopButton.isEnabled = false
                resetButton.isEnabled = true
                startButton.isEnabled = true
            }

            resetButton.setOnClickListener {
                isRunning = false
                handler.removeCallbacks(updateTimer)
                timeText.text = "00:00:000"
                elapsedTime = 0

                stopButton.isEnabled = false
                resetButton.isEnabled = false
                startButton.isEnabled = true
            }
        }

        override fun onPause() {
            super.onPause()

            timeTextSave = timeText.text.toString()

            startButtonEnabled = startButton.isEnabled
            stopButtonEnabled = stopButton.isEnabled
            resetButtonEnabled = resetButton.isEnabled

        }

        override fun onResume() {
            super.onResume()

            timeText.text = timeTextSave

            startButton.isEnabled = startButtonEnabled
            stopButton.isEnabled = stopButtonEnabled
            resetButton.isEnabled = resetButtonEnabled
        }

        private val updateTimer = object : Runnable {
            override fun run() {
                timeText = rootView.findViewById<TextView>(R.id.timeText)
                if (isRunning) {
                    elapsedTime = System.currentTimeMillis() - startTime
                    val minutes = (elapsedTime / 60000)  % 60
                    val seconds = (elapsedTime / 1000) % 60
                    val milliseconds = elapsedTime % 1000

                    val timeString = String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
                    timeText.text = timeString

                    handler.postDelayed(this, 10)
                }
            }
        }
    }