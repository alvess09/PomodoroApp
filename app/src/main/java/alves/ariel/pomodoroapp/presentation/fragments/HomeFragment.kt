package alves.ariel.pomodoroapp.presentation.fragments

import alves.ariel.pomodoroapp.databinding.FragmentHomeBinding
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //define os botões e captura os ids
        val btnPlay = binding.fabPlay
        val btnStop = binding.fabStop
        val btnPomodoro = binding.btnPomodoro
        val btnPausaCurta = binding.btnPausaCurta
        val btnPausaLonga = binding.btnPausaLonga
        val displayContador = binding.tvTimer
        val displayTarefa = binding.tvTask




        // TODO() CRIAR O TIMER POMODORO
        var tempoPomodoroDefault:Int = 30

        // TODO() CRIAR AS FUNÇÕES DE CONFIGURAR OS TIMERS
        btnPomodoro.setOnClickListener {
            tempoPomodoroDefault = defineTempoDefault(30)
        }
        btnPausaCurta.setOnClickListener {
            tempoPomodoroDefault = defineTempoDefault(5)
        }
        btnPausaLonga.setOnClickListener {
            tempoPomodoroDefault = defineTempoDefault(15)
        }




        displayContador.text = "$tempoPomodoroDefault:00"
        val minutosDefault:Int = 60
        val milissegundosDefault:Int = 1000 // =  1 segundo
        var tempoEmMilissegundos = tempoPomodoroDefault * minutosDefault * milissegundosDefault // (tempo padrão = 30,5,15 minutos) convertidos em milissegundos
        val intervalo = milissegundosDefault.toLong() // intervalo de tempo em milissegundos (1 segundo)
        var contador = object: CountDownTimer(tempoEmMilissegundos.toLong(), intervalo) {
            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished / 1000
                val minutos = segundosRestantes / 60
                val segundos = segundosRestantes % 60
                val tempoRestante:String = "$minutos:$segundos"

                //define o timer na tela
                displayContador.text = tempoRestante
                println("Tempo restante: $minutos minutos e $segundos segundos")
            }

            override fun onFinish() {
                Toast.makeText(requireContext(), "Pomodoro Concluído!", Toast.LENGTH_SHORT).show()
                 println("Contagem regressiva terminada!")
            }
        }

        // Para iniciar o contador
        //iniciar quando o btn_play for clicado
        btnPlay.setOnClickListener {
            contador.start()
            //btnPlay.setImageDrawable(R.drawable.ic_pause.toDrawable())
        }
        btnStop.setOnClickListener {
            contador.cancel()
            defineTempoDefault(tempoPomodoroDefault)
        }




        //TODO() CRIAR O TIMER POMODORO
        val circularProgressBar = binding.circularProgressBar
        circularProgressBar.onProgressChangeListener = { progress ->
            // Do something
        }
        circularProgressBar.apply {
            // Set Progress
            //progress = 65f
            // or with animation
            setProgressWithAnimation(65f, 1000) // =1s

            // Set Progress Max
            progressMax = 100f

            // Set ProgressBar Color
            //progressBarColor = Color.BLACK
            // or with gradient
            //progressBarColorStart = Color.GRAY
            //progressBarColorEnd = Color.RED
            //progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            //backgroundProgressBarColor = Color.GRAY
            // or with gradient
            //backgroundProgressBarColorStart = Color.WHITE
            //backgroundProgressBarColorEnd = Color.RED
            //backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            //progressBarWidth = 7f // in DP
            //backgroundProgressBarWidth = 3f // in DP

            // Other
            //roundBorder = true
            //startAngle = 180f
            //progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }


    }
    private fun defineTempoDefault(tempo:Int):Int {
        val tempoPomodoroDefault = tempo
        binding.tvTimer.text = "$tempoPomodoroDefault:00"
        return tempoPomodoroDefault
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}