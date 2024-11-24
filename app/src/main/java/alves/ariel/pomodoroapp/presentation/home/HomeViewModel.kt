package alves.ariel.pomodoroapp.presentation.home

import alves.ariel.pomodoroapp.R
import alves.ariel.pomodoroapp.presentation.home.EstadoTimer.PAUSA_CURTA
import alves.ariel.pomodoroapp.presentation.home.EstadoTimer.PAUSA_LONGA
import alves.ariel.pomodoroapp.presentation.home.EstadoTimer.POMODORO
import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Timer(
    var _pomodoro: Int = 30,
    var _pausaCurta: Int = 5,
    var _pausaLonga: Int = 15
)

enum class EstadoTimer {
    POMODORO,
    PAUSA_CURTA,
    PAUSA_LONGA
}
enum class EstadoBotao{
    STOPPED,
    RUNNING,
    PAUSED
}


class HomeViewModel : ViewModel() {

    private val timer = Timer()
    private val _estadoFinalizacao = MutableLiveData<EstadoTimer>()
    private val _tempoRestante = MutableLiveData<String>()
    private val _progresso = MutableLiveData(0f)
    private val _estadoTexto = MutableLiveData<String>()
    private val _tituloTarefa = MutableLiveData<String>()
    private val _notificacao = MutableLiveData<Pair<Int, Int>>()
    private val _estadoBotaoPlay = MutableLiveData(EstadoBotao.STOPPED)
    private var estadoAtual = POMODORO
    private val pomodoro = timer._pomodoro
    private val pausaCurta = timer._pausaCurta
    private val pausaLonga = timer._pausaLonga
    private var tempoRestanteEmMilissegundos: Long = 0L
    val estadoTexto: LiveData<String> = _estadoTexto
    val tituloTarefa: LiveData<String> = _tituloTarefa
    val tempoRestante: LiveData<String> = _tempoRestante
    val progresso: LiveData<Float> = _progresso
    val estadoFinalizacao: LiveData<EstadoTimer> = _estadoFinalizacao
    val notificacao: LiveData<Pair<Int, Int>> = _notificacao
    val estadoBotaoPlay:LiveData<EstadoBotao> = _estadoBotaoPlay
    var pomodorosConcluidos = 0  //variavel que armazena o progresso de pomodoros concluídos
    var _contador: CountDownTimer? = null
    var tempoInicial = pomodoro


    private fun enviarNotificacao(titulo: Int, texto: Int) {
        _notificacao.value = Pair(titulo, texto)

    }


    fun onTimerFinished(estado: EstadoTimer) {
        Log.d("onFinished", " homeViewModel onTimerFinished: $estado")
        _estadoFinalizacao.value = estado
        when (estado) {
            POMODORO -> {
                pomodorosConcluidos++
                if (pomodorosConcluidos == 4) {
                    estadoAtual = PAUSA_LONGA
                    pausaLonga()
                    enviarNotificacao(R.string.titulo_notificacao, R.string.texto_notificacao_pl)
                } else {
                    estadoAtual = PAUSA_CURTA
                    pausaCurta()
                    enviarNotificacao(R.string.titulo_notificacao, R.string.texto_notificacao_pc)
                }
                Log.d("pomodoros concluidos", "Pomodoros concluídos: $pomodorosConcluidos")
            }

            PAUSA_CURTA -> {
                estadoAtual = POMODORO
                pomodoro()
                enviarNotificacao(R.string.titulo_notificacao, R.string.texto_notificacao)
            }

            PAUSA_LONGA -> {
                pomodorosConcluidos = 0
                estadoAtual = POMODORO
                enviarNotificacao(R.string.titulo_notificacao, R.string.texto_notificacao)
                pomodoro()
            }
        }
    }


    // Função para criar um novo contador
    private fun criarContador(tempoEmMilissegundos: Long): CountDownTimer {
        return object : CountDownTimer(tempoEmMilissegundos, 1000) {
            // Função chamada a cada intervalo de tempo
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                tempoRestanteEmMilissegundos = millisUntilFinished
                val segundosRestantes = millisUntilFinished / 1000
                val minutos = segundosRestantes / 60
                val segundos = segundosRestantes % 60
                _tempoRestante.value = String.format("%02d:%02d", minutos, segundos)

                _progresso.value =
                    ((tempoEmMilissegundos - millisUntilFinished).toFloat() / tempoEmMilissegundos.toFloat()) * 100

                Log.d("Tempo restante", "Tempo restante: $minutos minutos e $segundos segundos")
            }

            override fun onFinish() {
                onTimerFinished(estadoAtual)
                Log.d("onFinished", " homeViewModel criarContador onTimerFinished: $estadoAtual")
                alterarEstadoBotaoPlay(EstadoBotao.STOPPED)
            }


        }
    }

    internal fun defineTimer(tempo: Int) {

        // Cancela o contador atual, se houver
        _contador?.cancel()
        val tempoEmMilissegundos = tempo * 60 * 1000
        tempoRestanteEmMilissegundos = tempoEmMilissegundos.toLong()
        _contador = criarContador(tempoEmMilissegundos.toLong())

    }

    fun alterarEstadoBotaoPlay(novoEstado:EstadoBotao){
        _estadoBotaoPlay.value = novoEstado
    }


    //  Funções de configuração de tempos para Pomodoro, Pausa Curta e Pausa Longa
    fun pomodoro() {
        defineTimer(pomodoro)
        tempoInicial = pomodoro

        (estadoTexto as MutableLiveData).value = "Pomodoro ${pomodorosConcluidos + 1}º"

    }

    fun pausaCurta() {
        defineTimer(pausaCurta)
        tempoInicial = pausaCurta

        (estadoTexto as MutableLiveData).value = "Pausa Curta"

    }

    fun pausaLonga() {
        defineTimer(pausaLonga)
        tempoInicial = pausaLonga

        (estadoTexto as MutableLiveData).value = "Pausa Longa"
    }

    //sobre as Funções dos botões

    // STOPPED: O botão mostra o ícone ic_play, chama startTimer e muda o estado para RUNNING.
    //RUNNING: O botão mostra o ícone ic_pause, chama pauseTimer e muda o estado para PAUSED.
    //PAUSED: O botão mostra o ícone ic_play, chama resumeTimer e muda o estado para RUNNING.
    //Quando o timer finalizar, o botão volta para o estado inicial (STOPPED).

    fun startTimer() {
        _contador?.start()

    }

    fun pauseTimer() {
        _contador?.cancel()
        Log.d("Timer", "Contador pausado. Tempo restante: $tempoRestanteEmMilissegundos ms")
    }

    fun resumeTimer() {
        _contador = criarContador(tempoRestanteEmMilissegundos)
        _contador?.start()
        Log.d("Timer", "Contador retomado. Tempo restante: $tempoRestanteEmMilissegundos ms ")
    }
    fun cancelTimer() {
        _contador?.cancel()
        defineTimer(tempoInicial)
    }


}