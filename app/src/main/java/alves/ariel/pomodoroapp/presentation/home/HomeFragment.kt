package alves.ariel.pomodoroapp.presentation.home

import alves.ariel.pomodoroapp.databinding.FragmentHomeBinding
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }
    private var contador: CountDownTimer? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root


    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contador = viewModel._contador



        // Inicia o contador com o tempo padrão do btnPomodoro
        viewModel.pomodoro()

        //Configura tempo inicial na tela
        configuraTempoInicialTela()

        //define o timer na tela
        viewModel.tempoRestante.observe(viewLifecycleOwner) { novoTempo ->
            binding.tvTimer.text = novoTempo
        }

        viewModel.progresso.observe(viewLifecycleOwner) { novoProgresso ->
            atualizaProgresso(novoProgresso)
        }

        viewModel.estadoPomodoro.observe(viewLifecycleOwner) { novoEstado ->
            Log.d("estadoPomodoro", "Estado Pomodoro: $novoEstado Pomodoros concluídos: ${viewModel.pomodorosConcluidos}")
            when(novoEstado){
                0 -> {binding.tvTask.text = "Pomodoro ${viewModel.pomodorosConcluidos + 1}º"
                    configuraTempoInicialTela()
                }
                1 -> {
                    binding.tvTask.text = "Pausa Curta"
                    configuraTempoInicialTela()
                }
                2 -> {
                    binding.tvTask.text = "Pausa Longa"
                    configuraTempoInicialTela()
                }
            }
        }
        //Configura Timer geral
        configuraTimer()


    }


    //    fun defineTarefa(task: String) {
//        /*TODO() RECEBER O PRIMEIRO ELEMENTO DE UMA LISTA DE TAREFAS E LISTAR NA SEQUENCIA 1 POR VEZ
//        *  QUANDO A TAREFA FOR CONCLUÍDA MARCAR CHECKED NA LISTA E PASSAR PARA A PRÓXIMA */
//        val displayTarefa = binding.tvTask
//        displayTarefa.text = task
//    }

    private fun configuraTimer() {

        // Define o tempo padrão dos timers
        binding.btnPomodoro.setOnClickListener {
            viewModel.pomodoro()
            viewModel.pomodorosConcluidos = 0
            atualizaProgresso(0f)
            configuraTempoInicialTela()
        }
        binding.btnPausaCurta.setOnClickListener {
            viewModel.pausaCurta()
            viewModel.pomodorosConcluidos = 0
            atualizaProgresso(0f)
            configuraTempoInicialTela()
        }
        binding.btnPausaLonga.setOnClickListener {
            viewModel.pausaLonga()
            viewModel.pomodorosConcluidos = 0
            atualizaProgresso(0f)
            configuraTempoInicialTela()
        }


        //iniciar quando o btn_play for clicado
        binding.fabPlay.setOnClickListener {
            viewModel._contador?.start()
            viewModel.progresso.value?.let { atualizaProgresso(it) }

            Log.d("progresso btnPlay", "configuraTimer: ${viewModel.progresso}")

        }

        binding.fabStop.setOnClickListener {
            // Cancela o contador quando o btn_stop for clicado e define o tempo
            // inicial na tela de acordo com o tempo padrão do momento
            contador?.cancel()
            viewModel.defineTimer(viewModel.tempoInicial)
            configuraTempoInicialTela()

            //zera o progresso da circleBar
            atualizaProgresso(0f)
            Log.i("progresso btnPause", "configuraTimer: ${viewModel.progresso}")
        }

    }

    private fun atualizaProgresso(progresso: Float) {

        val circularProgressBar = binding.circularProgressBar
        circularProgressBar.progress = progresso
        Log.i("progresso", "atualizaProgresso: $progresso")
    }

    @SuppressLint("DefaultLocale")
    private fun configuraTempoInicialTela() {
        val tempoinicial: Int = viewModel.tempoInicial
        val tempoFormatado: String = String.format("%02d:00", tempoinicial)
        binding.tvTimer.text = tempoFormatado
        Log.i("tempo em tela", "configuraTempoInicialTela: $tempoFormatado")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
