package alves.ariel.pomodoroapp.presentation.home

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

interface TimerListener {
    fun onTimerFinished(estado: EstadoTimer)
}

class HomeViewModel : ViewModel(), TimerListener {

    private val timer = Timer()
    private val _estadoPomodoro = MutableLiveData(0)
    private val _tempoRestante = MutableLiveData<String>()
    private val _progresso = MutableLiveData(0f)
    private var estadoAtual = POMODORO
    private val pomodoro = timer._pomodoro
    private val pausaCurta = timer._pausaCurta
    private val pausaLonga = timer._pausaLonga
    val tempoRestante: LiveData<String> = _tempoRestante
    val progresso: LiveData<Float> = _progresso
    val estadoPomodoro: LiveData<Int> = _estadoPomodoro
    var pomodorosConcluidos = 0
    var _contador: CountDownTimer? = null
    var tempoInicial = pomodoro
    var listener: TimerListener? = null



    override fun onTimerFinished(estado: EstadoTimer) {
        when (estado) {
            POMODORO -> {
                pomodorosConcluidos++
                if(pomodorosConcluidos%4==0) {
                    estadoAtual = PAUSA_LONGA
                    pausaLonga()
                } else {
                    estadoAtual = PAUSA_CURTA
                    pausaCurta()
                }
            }
            PAUSA_CURTA -> {
                estadoAtual = POMODORO
                pomodoro()
            }
            PAUSA_LONGA -> {
                estadoAtual = POMODORO
                pomodoro()
                pomodorosConcluidos = 0
            }
        }
    }

    // Função para criar um novo contador
    private fun criarContador(tempoEmMilissegundos: Long): CountDownTimer {
        return object : CountDownTimer(tempoEmMilissegundos, 1000) {
            // Função chamada a cada intervalo de tempo
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished / 1000
                val minutos = segundosRestantes / 60
                val segundos = segundosRestantes % 60
                _tempoRestante.value = String.format("%02d:%02d", minutos, segundos)

                _progresso.value =
                    ((tempoEmMilissegundos - millisUntilFinished).toFloat() / tempoEmMilissegundos.toFloat()) * 100

                println("Tempo restante: $minutos minutos e $segundos segundos")


            }

            override fun onFinish() {
                listener?.onTimerFinished(estadoAtual)
                Log.d("HomeViewModel", "Estado do timer antes de chamar o listener: $estadoAtual")
            }


        }
    }

    internal fun defineTimer(tempo: Int) {

        // Cancela o contador atual, se houver
        _contador?.cancel()
        val tempoEmMilissegundos = tempo * 60 * 1000
        _contador = criarContador(tempoEmMilissegundos.toLong())


    }

    fun pomodoro() {
        defineTimer(pomodoro)
        tempoInicial = pomodoro
        _estadoPomodoro.value = 0
    }

    fun pausaCurta() {
        defineTimer(pausaCurta)
        tempoInicial = pausaCurta
        _estadoPomodoro.value = 1
        Log.d("HomeViewModel", "Estado do timer: $estadoAtual")
    }

    fun pausaLonga() {
        defineTimer(pausaLonga)
        tempoInicial = pausaLonga
        _estadoPomodoro.value = 2
    }




}