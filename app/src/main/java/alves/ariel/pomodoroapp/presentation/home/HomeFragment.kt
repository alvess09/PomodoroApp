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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()



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




        // inicia o contador apenas se ele já não estiver ativo
        if (viewModel._contador == null){
            // Inicia o contador com o tempo padrão do btnPomodoro
            viewModel.pomodoro()
            configuraTempoInicialTela()
        }

        //observa o tempo restante e atualiza a UI
        viewModel.tempoRestante.observe(viewLifecycleOwner) { novoTempo ->
            binding.tvTimer.text = novoTempo
        }

        viewModel.estadoFinalizacao.observe(viewLifecycleOwner) { estado ->
            onTimerFinished(estado)
        }

        //observa o progresso e atualiza a barra de progresso
        viewModel.progresso.observe(viewLifecycleOwner) { novoProgresso ->
            atualizaProgresso(novoProgresso)
        }

       viewModel.estadoTexto.observe(viewLifecycleOwner) { estado ->
           binding.tvTask.text = estado
       }

       viewModel.notificacao.observe(viewLifecycleOwner){ notificacao ->
           notificacao?.let {
               val (tituloResId, textoResId) = notificacao
               val titulo = getString(tituloResId)
               val texto = getString(textoResId)
               exibirNotificacao(titulo, texto)
           }

       }


        //Cria o canal de notiicações
        createNotificationChannel()
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

        val estadoBtnPlay:Int = 1
        if (estadoBtnPlay !=1){

        }

        //configurações botões play e stop
        binding.fabPlay.setOnClickListener {
            viewModel.startTimer()
            viewModel.progresso.value?.let { atualizaProgresso(it) }

            Log.d("progresso btnPlay", "configuraTimer: ${viewModel.progresso}")

        }

        binding.fabStop.setOnClickListener {
            // Cancela o contador quando o btn_stop for clicado e define o tempo
            // inicial na tela de acordo com o tempo padrão do momento
            viewModel._contador?.cancel()
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


    private fun onTimerFinished(estado:EstadoTimer) {

        Log.d("onFinished", " homeFragment onTimerFinished: $estado")
        when (estado) {
            EstadoTimer.POMODORO -> {
                viewModel.pausaCurta() // Transição para pausa curta
                configuraTempoInicialTela() // Atualiza a tela para o tempo inicial da pausa curta
                atualizaProgresso(0f) // Zera a barra de progresso
            }
            EstadoTimer.PAUSA_CURTA -> {
                viewModel.pomodoro() // Transição de volta para o Pomodoro
                configuraTempoInicialTela() // Atualiza a tela para o tempo inicial do Pomodoro
                atualizaProgresso(0f) // Zera a barra de progresso
            }
            EstadoTimer.PAUSA_LONGA -> {
                viewModel.pomodoro() // Transição de volta para o Pomodoro
                configuraTempoInicialTela() // Atualiza a tela para o tempo inicial do Pomodoro
                atualizaProgresso(0f) // Zera a barra de progresso
            }
        }

    }


    //cria o canal de notificação
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Channel_01", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager = requireContext()
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun exibirNotificacao(titulo: String, texto: String) {
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
