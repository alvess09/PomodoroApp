package alves.ariel.pomodoroapp.presentation.fragments

import alves.ariel.pomodoroapp.databinding.FragmentHomeBinding
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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


        //DEFINE TAREFA DO POMODORO
        //TODO() CRIAR MÉTODO PARA DEFINFIR TAREFAS E MOSTRÁ-LAS EM TELA


        // CRIA O TIMER POMODORO
        var tempoPomodoroDefault: Int = 30

        // DEFINE O TEMPO PADRÃO DOS TIMERS POMODORO =
        btnPomodoro.setOnClickListener {
            tempoPomodoroDefault = defineTempoDefault(30)

        }
        btnPausaCurta.setOnClickListener {
            tempoPomodoroDefault = defineTempoDefault(5)
        }
        btnPausaLonga.setOnClickListener {
            tempoPomodoroDefault = defineTempoDefault(15)
        }


        defineTempoDefault(tempoPomodoroDefault)

        val minutosDefault: Int = 60
        val milissegundosDefault: Int = 1000 // =  1 segundo
        var tempoEmMilissegundos = tempoPomodoroDefault * minutosDefault * milissegundosDefault // (tempo padrão = 30,5,15 minutos) convertidos em milissegundos
        val intervalo = milissegundosDefault.toLong() // intervalo de tempo em milissegundos (1 segundo)

        // CRIA O CONTADOR

        var contador = object : CountDownTimer(tempoEmMilissegundos.toLong(), intervalo) {
            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished / 1000
                val minutos = segundosRestantes / 60
                val segundos = segundosRestantes % 60
                val tempoRestante: String = String.format("%02d:%02d", minutos, segundos)


                //define o timer na tela
                displayContador.text = tempoRestante
                println("Tempo restante: $minutos minutos e $segundos segundos")

                // Atualiza o progresso da barra circular
                val progresso =
                    ((tempoEmMilissegundos - millisUntilFinished).toFloat() / tempoEmMilissegundos.toFloat()) * 100
                atualizaProgresso(progresso)
                Log.i("Contador", "onTick: $progresso")
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
            atualizaProgresso(0f)
        }


    }

    fun defineTarefa(task: String) {
        /*TODO() RECEBER O PRIMEIRO ELEMENTO DE UMA LISTA DE TAREFAS E LISTAR NA SEQUENCIA 1 POR VEZ
        *  QUANDO A TAREFA FOR CONCLUÍDA MARCAR CHECKED NA LISTA E PASSAR PARA A PRÓXIMA */
        val displayTarefa = binding.tvTask
        displayTarefa.text = task
    }

    fun defineTempoDefault(tempo: Int): Int {
        val tempoPomodoroDefault = tempo
        binding.tvTimer.text = String.format("%02d:00", tempoPomodoroDefault)
        Log.d("defineTempoDefault", "defineTempoDefault: $tempoPomodoroDefault")
        return tempoPomodoroDefault
    }

    private fun atualizaProgresso(progresso: Float) {
        val circularProgressBar = binding.circularProgressBar
        //circularProgressBar.setProgressWithAnimation(progresso, 1000)
        circularProgressBar.progress = progresso

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}