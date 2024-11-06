package alves.ariel.pomodoroapp.presentation.home

import alves.ariel.pomodoroapp.R
import alves.ariel.pomodoroapp.databinding.FragmentHomeBinding
import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment(), TimerListener {

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

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contador = viewModel._contador
        createNotificationChannel()

        viewModel.listener = this

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
            Log.d(
                "estadoPomodoro",
                "Estado Pomodoro: $novoEstado Pomodoros concluídos: ${viewModel.pomodorosConcluidos}"
            )
//
            val tituloTimerPausaCurta = getString(R.string.pausa_curta)
            val tituloTimerPausaLonga = getString(R.string.pausa_longa)

            when (novoEstado) {
                0 -> {
                    binding.tvTask.text = "Pomodoro${
                        viewModel
                            .pomodorosConcluidos + 1
                    }º"
                    configuraTempoInicialTela()
                }

                1 -> {
                    binding.tvTask.text = tituloTimerPausaCurta
                    configuraTempoInicialTela()

                }

                2 -> {
                    binding.tvTask.text = tituloTimerPausaLonga
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

    override fun onTimerFinished(estado: EstadoTimer) {
        val tituloNotificacao = getString(R.string.titulo_notificacao)
        val textoNotificacaoPausaCurta = getString(R.string.texto_notificacao)
        val textoNotificacaoPausaLonga = getString(R.string.texto_notificacao_pl)
        when (estado) {
            EstadoTimer.PAUSA_CURTA -> notificar(tituloNotificacao, textoNotificacaoPausaCurta)
            EstadoTimer.PAUSA_LONGA -> notificar(tituloNotificacao, textoNotificacaoPausaLonga)
            else -> {} // Não notificar para outros estados
        }
    }


    //cria o canal de notificação
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Channel_01", "Channel 01", importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager = requireContext()
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun notificar(titulo: String, texto: String) {
        val builder = NotificationCompat.Builder(requireContext(), "Channel_01")
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(requireContext())) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return@with
            }
            // notificationId is a unique int for each notification that you must define.
            notify(1, builder.build())
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
